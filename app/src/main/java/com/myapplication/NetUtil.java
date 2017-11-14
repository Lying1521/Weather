package com.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * Created by liyu on 17/10/7.
 */

public class NetUtil {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;
    public static RequestQueue mQueue;
    public static final String  URL = "http://wthrcdn.etouch.cn/WeatherApi?citykey=";
    public static void initHttpManager(Context context){
        if(context != null)
        {
            mQueue = Volley.newRequestQueue(context);
        }
    }
    public static void GerRequest(String code,  final NetWorkCallBack callback){//get请求
        MyStringRequest mStringRequest = new MyStringRequest(Request.Method.GET,URL+code,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.i("GET", response);
                        callback.Success(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.Failure("error");
                    }
                });
        mQueue.add(mStringRequest);
        Log.i("GET", "star");
    }
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NETWORN_NONE;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return NETWORN_MOBILE;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NETWORN_WIFI;
        }
        return NETWORN_NONE;
    }

}
