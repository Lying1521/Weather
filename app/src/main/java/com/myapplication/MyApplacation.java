package com.myapplication;

import android.app.Application;

/**
 * Created by liyu on 2017/11/1.
 */

public class MyApplacation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.initContext(getApplicationContext());
        NetUtil.initHttpManager(getApplicationContext());
        Utils.GetCityList(getApplicationContext());
    }
}
