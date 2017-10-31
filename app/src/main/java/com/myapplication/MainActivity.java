package com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements NetWorkCallBack, View.OnClickListener {

    private TextView title,city,time,humidity,temperature_now,week_today,temperature_today,climate,wind,pm_data,pm_quality;
    private ImageView manager,location,share,update,pm25_img,weather_img;
    private String code ;
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
        findViewById(R.id.city_manager).setOnClickListener(this);

    }
    private void GetLocalDate() {
        String localweatherinfo = Utils.ReadString(code,"no history");
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
        code = Utils.ReadString(Utils.City_Code,"101010100");
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
        pm25_img.setImageResource(Utils.GetPmImg(pm25));
        String climate = weatherinfo.getType();
        weather_img.setImageResource(Utils.GetWertherImg(climate));
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
            case R.id.city_manager:
                startActivityForResult(new Intent(this,SelectCityActivity.class),Utils.ResultFromSelectActivity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == Utils.ResultFromSelectActivity && resultCode == RESULT_OK){
            code = data.getStringExtra(Utils.City_Code);
            Utils.SaveString(Utils.City_Code,code);
            CheckNetStatus();
        }

    }
}
