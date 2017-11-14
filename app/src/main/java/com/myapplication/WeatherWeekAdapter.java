package com.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by liyu on 2017/11/14.
 */

public class WeatherWeekAdapter extends BaseAdapter {

    ArrayList<WeatherInfo.Day> data ;
    Context context;

    public WeatherWeekAdapter(Context context, ArrayList<WeatherInfo.Day> Days) {
        this.data = Days;
        this.context = context;
    }
    public void setData(ArrayList<WeatherInfo.Day> data) {
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherWeekAdapter.ViewHolder viewHolder = null ;
        if(convertView == null){
            viewHolder = new WeatherWeekAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.itme_gridview,parent,false);
            viewHolder.day = (TextView) convertView.findViewById(R.id.day);
            viewHolder.wind = (TextView) convertView.findViewById(R.id.wind);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.temperature = (TextView) convertView.findViewById(R.id.temperature);
            viewHolder.weather_img = (ImageView) convertView.findViewById(R.id.weather_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (WeatherWeekAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.day.setText(data.get(position).getDate());
        Log.d("myWeather", String.valueOf(position)+"  "+data.get(position).getDate());
        viewHolder.wind.setText(data.get(position).getFengli());
        Log.d("myWeather", String.valueOf(position)+"  "+data.get(position).getFengli());
        viewHolder.type.setText(data.get(position).getType());
        Log.d("myWeather", String.valueOf(position)+"  "+data.get(position).getType());
        viewHolder.temperature.setText(data.get(position).getHigh()+"~"+data.get(position).getLow());
        Log.d("myWeather", String.valueOf(position)+"  "+data.get(position).getHigh()+"~"+data.get(position).getLow());
        viewHolder.weather_img.setImageResource(Utils.GetWertherImg(data.get(position).getType()));
        return convertView;
    }
    class ViewHolder{

        TextView type,temperature,day,wind;
        ImageView weather_img;
    }

}
