package com.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyu on 2017/11/1.
 */

public class CityDB {
    public static final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";
    private SQLiteDatabase db;
    public CityDB(Context context, String path) {
        //数据库连接函数
        db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
    }
    public List<CityInfo> getAllCity() {
        //从数据库文件逐条取出城市信息
        List<CityInfo> list = new ArrayList<CityInfo>();
        Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME, null);
        while (c.moveToNext()) {
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            CityInfo item = new CityInfo(province, city, number, firstPY, allPY,allFirstPY);
            list.add(item);
        }
        return list;
    }
}

