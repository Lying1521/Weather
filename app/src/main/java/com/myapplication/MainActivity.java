package com.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements NetWorkCallBack, View.OnClickListener {

    private TextView title,city,time,humidity,temperature_now,week_today,temperature_today,climate,wind,pm_data,pm_quality;
    private ImageView manager,location,share,update,pm25_img,weather_img;
    private GridView weather_week;
    private String code ="101010100";
    private WeatherInfo weatherinfo;
    private WeatherWeekAdapter weatherWeekAdapter;
    private ProgressBar update_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
        CheckNetStatus();
    }

    private void initView() { //初始化获得控件
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
        weather_week = (GridView) findViewById(R.id.weather_week);
        update_progress = (ProgressBar) findViewById(R.id.city_update_progress);
        GetLocalDate();
    }


    private void initEvents() {//为按钮添加事件监听
        update.setOnClickListener(this);
        findViewById(R.id.city_manager).setOnClickListener(this);

    }
    private void GetLocalDate() {//从本地文件中读取曾经服务器返回的天气信息
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
            weatherinfo = Utils.parseDate(localweatherinfo);//解析返回值
            updateUI();
        }
    }
    private void GetWeatherInfo() {//发起网络请求
        NetUtil.GerRequest(code,this);
    }

    private void CheckNetStatus() {//检查手机网络状态
        code = Utils.ReadString(Utils.City_Code,code);
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


    private void updateUI()  {//更新页面

        try {
            title.setText(weatherinfo.getCity()+"天气");
            city.setText(weatherinfo.getCity());
            time.setText(weatherinfo.getUpdatetime()+ "发布");
            humidity.setText("湿度:"+weatherinfo.getShidu());
            temperature_now.setText("温度:"+weatherinfo.getWendu()+"°C");
            pm_data.setText(weatherinfo.getPm25());
            pm_quality.setText(weatherinfo.getQuality());
            week_today.setText(weatherinfo.getDays().get(0).getDate());
            temperature_today.setText(weatherinfo.getDays().get(0).getHigh()+"~"+weatherinfo.getDays().get(0).getLow());
            climate.setText(weatherinfo.getDays().get(0).getType());
            wind.setText("风力:"+weatherinfo.getDays().get(0).getFengli());
            String climate = weatherinfo.getDays().get(0).getType();
            weather_img.setImageResource(Utils.GetWertherImg(climate));
            ArrayList<WeatherInfo.Day> data = new ArrayList<>();
            data.add(weatherinfo.getYesterday());
            data.addAll(weatherinfo.getDays());
            weatherWeekAdapter = new WeatherWeekAdapter(getApplicationContext(),data);
            // 每页显示3个
            int width = getWindowManager().getDefaultDisplay().getWidth()/3;
            ViewGroup.LayoutParams lp = weather_week.getLayoutParams();
            lp.width = width*(weatherinfo.getDays().size()+1);
            weather_week.setLayoutParams(lp);
            //列数=天数
            weather_week.setNumColumns(weatherinfo.getDays().size()+1);
            //绑定adapter
            weather_week.setAdapter(weatherWeekAdapter);
            Log.d("myWeather", String.valueOf(weatherinfo.getDays().size()));
            int pm25 = Integer.valueOf(weatherinfo.getPm25());
            pm25_img.setImageResource(Utils.GetPmImg(pm25));
            Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"更新失败!",Toast.LENGTH_SHORT).show();
        }
        update_progress.setVisibility(View.INVISIBLE);
        update.setVisibility(View.VISIBLE);
    }

    @Override
    public void Success(String response) {//网络连接成功
        Utils.SaveString(code,response); //本地缓存
        weatherinfo = Utils.parseDate(response);//解析返回值
        updateUI();//更新界面信息
    }

    @Override
    public void Failure(String response) { //网络连接连接失败
        Log.e("myWeather", response);
        Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {//处理点击事件
        switch (v.getId()){
            case R.id.city_update://点击刷新
                update.setVisibility(View.INVISIBLE);
                update_progress.setVisibility(View.VISIBLE);
                CheckNetStatus();
                break;
            case R.id.city_manager://点击城市选择
                Intent i = new Intent(this,SelectCityActivity.class);
                i.putExtra("city_name",weatherinfo.getCity());
                startActivityForResult(i,Utils.ResultFromSelectActivity); //启动城市选择页面
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //处理Activity返回事件
        if( requestCode == Utils.ResultFromSelectActivity && resultCode == RESULT_OK){
            if(data.getStringExtra(Utils.City_Code)!=null){
                code = data.getStringExtra(Utils.City_Code);
            }

            Utils.SaveString(Utils.City_Code,code);
            CheckNetStatus();
        }

    }
}
