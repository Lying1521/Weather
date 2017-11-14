package com.myapplication;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    private String city_code;
    private EditText city_search;
    private TextView title;
    private ListView city_list;
    private ImageView back;
    private SelectAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citylist);
        initView();
        initEvents();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_name);
        title.setText("当前城市:"+getIntent().getStringExtra("city_name"));
        city_search = (EditText) findViewById(R.id.city_search);
        back = (ImageView) findViewById(R.id.title_back);
        city_list = (ListView) findViewById(R.id.city_list);
        adapter = new SelectAdapter(Utils.City_list,getApplicationContext());
        city_list.setAdapter(adapter);
    }

    private void initEvents() {
        back.setOnClickListener(this);
        city_list.setOnItemClickListener(this);
        city_search.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.title_back:
                Intent i = new Intent();
                i.putExtra(Utils.City_Code, city_code);
                setResult(RESULT_OK,i);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        city_code = ((CityInfo)adapter.getItem(position)).getNumber();
        title.setText("当前城市:"+((CityInfo)adapter.getItem(position)).getCity());
        Log.d("Myweather",((CityInfo)adapter.getItem(position)).getCity()+"|"+((CityInfo)adapter.getItem(position)).getAllFristPY()+"|"+
        ((CityInfo)adapter.getItem(position)).getAllPY()+"|"+((CityInfo)adapter.getItem(position)).getFirstPY());
        Toast.makeText(this,"当前城市:"+((CityInfo)adapter.getItem(position)).getCity(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String city = s.toString();
        if(!city.equals("")){
            List<CityInfo> cityInfos = new ArrayList<>();
            for(CityInfo cityInfo:Utils.City_list){
                if(cityInfo.getCity().equals(city)
                        || cityInfo.getAllPY().toLowerCase().equals(city.toLowerCase())
                        || cityInfo.getFirstPY().toLowerCase().equals(city.toLowerCase())
                        || cityInfo.getAllFristPY().toLowerCase().equals(city.toLowerCase())){
                    cityInfos.add(cityInfo);
                }
            }
            adapter.setData(cityInfos);
            adapter.notifyDataSetChanged();
        }
        else {
            adapter.setData(Utils.City_list);
            adapter.notifyDataSetChanged();
        }
    }
}
