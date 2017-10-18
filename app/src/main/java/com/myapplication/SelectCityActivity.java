package com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectCityActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citylist);
        findViewById(R.id.title_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.title_back:
                Intent i = new Intent();
                i.putExtra(Utils.City_Code,"101160101");
                setResult(RESULT_OK,i);
                finish();
                break;
        }
    }
}
