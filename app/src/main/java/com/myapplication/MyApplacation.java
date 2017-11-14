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
        NetUtil.initHttpManager(getApplicationContext()); //初始化网络连接队列
        Utils.GetCityList(getApplicationContext()); //从数据库获得城市信息
    }
}
