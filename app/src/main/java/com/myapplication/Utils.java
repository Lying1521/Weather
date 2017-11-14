package com.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyu on 17/10/11.
 */

public class Utils {
    public static int ResultFromSelectActivity = 0 ;
    public static String City_Code = "city_code";
    private static SharedPreferences sharedPreferences;
    private static Context MyContext;
    public static List<CityInfo> City_list;
    private static CityDB db;


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
        //利用push解析xml文件
        int count = 0;
        WeatherInfo weatherinfo = new WeatherInfo();
        WeatherInfo.Day yesterday = null;
        WeatherInfo.Day weekday = null;
        ArrayList<WeatherInfo.Day> forecast = null;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xml));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG://判断标记
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
                        }else if (xmlPullParser.getName().equals("yesterday")) {
                            yesterday = new WeatherInfo.Day();
                        }else if(xmlPullParser.getName().equals("forecast")){
                            forecast = new ArrayList<>();
                        }else if (xmlPullParser.getName().equals("fl_1")) {
                            eventType = xmlPullParser.next();
                            yesterday.setFengli(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("date_1")) {
                            eventType = xmlPullParser.next();
                            yesterday.setDate(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("high_1") ) {
                            eventType = xmlPullParser.next();
                            String str = xmlPullParser.getText().replace("高温","");
                            yesterday.setHigh(str);
                        } else if (xmlPullParser.getName().equals("low_1") ) {
                            eventType = xmlPullParser.next();
                            yesterday.setLow(xmlPullParser.getText().replace("低温",""));
                        } else if (xmlPullParser.getName().equals("type_1")) {
                            eventType = xmlPullParser.next();
                            yesterday.setType(xmlPullParser.getText());
                        }else if(xmlPullParser.getName().equals("weather")){
                            weekday = new WeatherInfo.Day();
                            Log.d("myweather","new day");
                            count++;
                        } else if (xmlPullParser.getName().equals("fengli" )&& count!=0) {
                            eventType = xmlPullParser.next();
                            weekday.setFengli(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("date")) {
                            eventType = xmlPullParser.next();
                            weekday.setDate(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("high") ) {
                            eventType = xmlPullParser.next();
                            String str = xmlPullParser.getText().replace("高温","");
                            weekday.setHigh(str);
                        } else if (xmlPullParser.getName().equals("low") ) {
                            eventType = xmlPullParser.next();
                            weekday.setLow(xmlPullParser.getText().replace("低温",""));
                        } else if (xmlPullParser.getName().equals("type")) {
                            eventType = xmlPullParser.next();
                            weekday.setType(xmlPullParser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG: //处理结束标记
                        if(xmlPullParser.getName().equals("yesterday")){
                            weatherinfo.setYesterday(yesterday);
                        }else if(xmlPullParser.getName().equals("weather")){
                            forecast.add(weekday);
                        }else if(xmlPullParser.getName().equals("forecast")){
                            weatherinfo.setDays(forecast);
                        }
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

    public static int GetWertherImg(String climate){//根据天气状况返回对应图标
        if(climate.equals("暴雪")){
            return(R.mipmap.biz_plugin_weather_baoxue);
        }
        if(climate.equals("暴雨")){
            return(R.mipmap.biz_plugin_weather_baoyu);
        }
        if(climate.equals("大暴雨")){
            return(R.mipmap.biz_plugin_weather_dabaoyu);
        }
        if(climate.equals("大雪")){
            return(R.mipmap.biz_plugin_weather_daxue);
        }
        if(climate.equals("大雨")){
            return(R.mipmap.biz_plugin_weather_dayu);
        }
        if(climate.equals("多云")){
            return(R.mipmap.biz_plugin_weather_duoyun);
        }
        if(climate.equals("雷阵雨")){
            return(R.mipmap.biz_plugin_weather_leizhenyu);
        }
        if(climate.equals("雷阵雨冰雹")){
            return(R.mipmap.biz_plugin_weather_leizhenyubingbao);
        }
        if(climate.equals("晴")){
            return(R.mipmap.biz_plugin_weather_qing);
        }
        if(climate.equals("沙尘暴")){
            return(R.mipmap.biz_plugin_weather_shachenbao);
        }
        if(climate.equals("特大暴雨")){
            return(R.mipmap.biz_plugin_weather_tedabaoyu);
        }
        if(climate.equals("雾")){
            return(R.mipmap.biz_plugin_weather_wu);
        }
        if(climate.equals("小雪")){
            return(R.mipmap.biz_plugin_weather_xiaoxue);
        }
        if(climate.equals("小雨")){
            return(R.mipmap.biz_plugin_weather_xiaoyu);
        }
        if(climate.equals("阴")){
            return(R.mipmap.biz_plugin_weather_yin);
        }
        if(climate.equals("雨加雪")){
            return(R.mipmap.biz_plugin_weather_yujiaxue);
        }
        if(climate.equals("阵雪")){
            return(R.mipmap.biz_plugin_weather_zhenxue);
        }
        if(climate.equals("阵雨")){
            return(R.mipmap.biz_plugin_weather_zhenyu);
        }
        if(climate.equals("中雪")){
            return(R.mipmap.biz_plugin_weather_zhongxue);
        }if(climate.equals("中雨")){
            return(R.mipmap.biz_plugin_weather_zhongyu);
        }
        return 0;
    }
    public static int GetPmImg(int pm25){ //根据pm值返回对应图标
        if(pm25<51){
            return (R.mipmap.biz_plugin_weather_0_50);
        }else if(pm25<101){
            return (R.mipmap.biz_plugin_weather_51_100);
        }else if(pm25<151){
            return (R.mipmap.biz_plugin_weather_101_150);
        }else if(pm25<201){
            return (R.mipmap.biz_plugin_weather_151_200);
        }else if(pm25<301){
            return (R.mipmap.biz_plugin_weather_201_300);
        }else{
            return (R.mipmap.biz_plugin_weather_greater_300);
        }
    }

    public static void GetCityList(Context applicationContext) {
        db = openCityDB(applicationContext);
        City_list = db.getAllCity();
    }
    private static  CityDB openCityDB(Context applicationContext) {
        String path = "/data" + Environment.getDataDirectory().getAbsolutePath()//将数据库文件中信息写入到一个文件中
                + File.separator + applicationContext.getPackageName()
                + File.separator + "databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        if (!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + applicationContext.getPackageName()
                    + File.separator + "databases1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.i("MyApp","mkdirs");
            }
            Log.i("MyApp","db is not exists");
            try {
                InputStream is = applicationContext.getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            } }
        return new CityDB(applicationContext, path);
    }
}

