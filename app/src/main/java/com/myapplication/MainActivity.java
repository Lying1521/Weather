package com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NetWorkCallBack, View.OnClickListener {

    private TextView title,city,time,humidity,temperature_now,week_today,temperature_today,climate,wind,pm_data,pm_quality;
    private ImageView manager,location,share,update,pm25_img,weather_img;
    private String code = "101010100";
    private WeatherInfo weatherinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.initContext(this);
        NetUtil.initHttpManager(this);
        initView();
        initEvents();
        CheckNetStatus();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.city_name);
        city = (TextView) findViewById(R.id.city);
        time = (TextView) findViewById(R.id.time);
        humidity = (TextView) findViewById(R.id.humidity);
        temperature_now = (TextView) findViewById(R.id.temperature_now);
        week_today = (TextView) findViewById(R.id.week_today);
        temperature_today = (TextView) findViewById(R.id.temperature_today);
        climate = (TextView) findViewById(R.id.climate);
        wind = (TextView) findViewById(R.id.wind);
        pm_data = (TextView) findViewById(R.id.pm_data);
        pm_quality = (TextView) findViewById(R.id.pm25_quality);
        manager = (ImageView) findViewById(R.id.city_manager);
        location = (ImageView) findViewById(R.id.city_location);
        share = (ImageView) findViewById(R.id.city_share);
        update = (ImageView) findViewById(R.id.city_update);
        pm25_img = (ImageView) findViewById(R.id.pm25_img);
        weather_img = (ImageView) findViewById(R.id.weather_img);

        GetLocalDate();
    }


    private void initEvents() {
        update.setOnClickListener(this);

    }
    private void GetLocalDate() {
        String localweatherinfo = Utils.ReadString(code);
        if(localweatherinfo.equals("no history")){
            title.setText("N/A");
            city.setText("N/A");
            time.setText("N/A");
            humidity.setText("N/A");
            temperature_now.setText("N/A");
            week_today.setText("N/A");
            temperature_today.setText("N/A");
            climate.setText("N/A");
            wind.setText("N/A");
            pm_quality.setText("N/A");
            pm_data.setText("N/A");
        }else{
            weatherinfo = Utils.parseDate(localweatherinfo);
            updateUI();
        }
    }
    private void GetWeatherInfo() {
        NetUtil.GerRequest(code,this);


    }

    private void CheckNetStatus() {
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
            Log.d("myWeather", "网络OK");
            GetWeatherInfo();
            Toast.makeText(MainActivity.this,"网络OK!", Toast.LENGTH_LONG).show();
        }else{
            Log.d("myWeather", "网络挂了");
            GetLocalDate();
            Toast.makeText(MainActivity.this,"网络挂了!", Toast.LENGTH_LONG).show();
        }
    }


    private void updateUI() {

        title.setText(weatherinfo.getCity()+"天气");
        city.setText(weatherinfo.getCity());
        time.setText(weatherinfo.getUpdatetime()+ "发布");
        humidity.setText("湿度:"+weatherinfo.getShidu());
        temperature_now.setText("温度:"+weatherinfo.getWendu()+"°C");
        pm_data.setText(weatherinfo.getPm25());
        pm_quality.setText(weatherinfo.getQuality());
        week_today.setText(weatherinfo.getDate());
        temperature_today.setText(weatherinfo.getHigh()+"~"+weatherinfo.getLow());
        climate.setText(weatherinfo.getType());
        wind.setText("风力:"+weatherinfo.getFengli());
        int pm25 = Integer.valueOf(weatherinfo.getPm25());
        if(pm25<51){
            pm25_img.setImageResource(R.mipmap.biz_plugin_weather_0_50);
        }else if(pm25<101){
            pm25_img.setImageResource(R.mipmap.biz_plugin_weather_51_100);
        }else if(pm25<151){
            pm25_img.setImageResource(R.mipmap.biz_plugin_weather_101_150);
        }else if(pm25<201){
            pm25_img.setImageResource(R.mipmap.biz_plugin_weather_151_200);
        }else if(pm25<301){
            pm25_img.setImageResource(R.mipmap.biz_plugin_weather_201_300);
        }else{
            pm25_img.setImageResource(R.mipmap.biz_plugin_weather_greater_300);
        }
        String climate = weatherinfo.getType();
        if(climate.equals("暴雪")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_baoxue);
        }
        if(climate.equals("暴雨")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_baoyu);
        }
        if(climate.equals("大暴雨")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_dabaoyu);
        }
        if(climate.equals("大雪")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_daxue);
        }
        if(climate.equals("大雨")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_dayu);
        }
        if(climate.equals("多云")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_duoyun);
        }
        if(climate.equals("雷阵雨")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_leizhenyu);
        }
        if(climate.equals("雷阵雨冰雹")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_leizhenyubingbao);
        }
        if(climate.equals("晴")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_qing);
        }
        if(climate.equals("沙尘暴")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_shachenbao);
        }
        if(climate.equals("特大暴雨")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_tedabaoyu);
        }
        if(climate.equals("雾")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_wu);
        }
        if(climate.equals("小雪")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_xiaoxue);
        }
        if(climate.equals("小雨")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_xiaoyu);
        }
        if(climate.equals("阴")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_yin);
        }
        if(climate.equals("雨加雪")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_yujiaxue);
        }
        if(climate.equals("阵雪")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_zhenxue);
        }
        if(climate.equals("阵雨")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_zhenyu);
        }
        if(climate.equals("中雪")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_zhongxue);
        }
        if(climate.equals("中雨")){
            weather_img.setImageResource(R.mipmap.biz_plugin_weather_zhongyu);
        }
        Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void Success(String response) {
        Utils.SaveString(code,response); //本地缓存
        weatherinfo = Utils.parseDate(response);//解析返回值
        updateUI();//更新界面信息
    }

    @Override
    public void Failure(String response) {
        Log.e("myWeather", response);
        Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city_update:
                CheckNetStatus();
                break;
        }
    }
}
