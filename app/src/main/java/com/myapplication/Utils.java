package com.myapplication;

import android.content.Context;
import android.content.SharedPreferences;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by liyu on 17/10/11.
 */

public class Utils {
    public static int ResultFromSelectActivity = 0 ;
    public static String City_Code = "city_code";
    private static SharedPreferences sharedPreferences;
    private static Context MyContext;

    public static void initContext(Context context){
        MyContext = context;
    }
    public static void SaveString(String key,String value){
        if(sharedPreferences == null) {
            sharedPreferences = MyContext.getSharedPreferences("Weather",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String ReadString(String key,String defaultString) {
        if(sharedPreferences == null){
            sharedPreferences = MyContext.getSharedPreferences("Weather",Context.MODE_PRIVATE);
        }
        return  sharedPreferences.getString(key,defaultString);
    }

    public static WeatherInfo parseDate(String xml) {
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        WeatherInfo weatherinfo = new WeatherInfo();
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xml));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setCity(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setUpdatetime(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setShidu(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("wendu")) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setWendu(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setPm25(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setQuality(xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setFengxiang(xmlPullParser.getText());
                            fengxiangCount++;
                        }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setFengli(xmlPullParser.getText());
                            fengliCount++;
                        } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setDate(xmlPullParser.getText());
                            dateCount++;
                        } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                            eventType = xmlPullParser.next();
                            String str = xmlPullParser.getText().replace("高温","");
                            weatherinfo.setHigh(str);
                            highCount++;
                        } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setLow(xmlPullParser.getText().replace("低温",""));
                            lowCount++;
                        } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                            eventType = xmlPullParser.next();
                            weatherinfo.setType(xmlPullParser.getText());
                            typeCount++;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return  weatherinfo;
    }
}

