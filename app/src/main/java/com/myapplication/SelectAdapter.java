package com.myapplication;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyu on 2017/10/25.
 */

public class SelectAdapter  extends BaseAdapter{

    private List<CityInfo> city_list;
    private Context context;

    public SelectAdapter(List<CityInfo> city_list, Context context) {

        this.city_list = city_list;
        this.context = context;
    }

    public void setData(List<CityInfo> city_list){
        this.city_list = city_list;
    }
    @Override
    public int getCount() {
        return city_list.size();
    }

    @Override
    public Object getItem(int position) {
        return city_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(city_list.get(position).getNumber());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null ;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.itme_citylist,parent,false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.city_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(city_list.get(position).getCity());
        return convertView;
    }
    class ViewHolder{
        TextView name ;
    }

}
