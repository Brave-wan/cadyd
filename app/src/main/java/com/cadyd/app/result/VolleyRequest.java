package com.cadyd.app.result;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cadyd.app.MyApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    public static Context context;
    public static JsonObjectRequest request;


    public static void RequestJsonPost(String url, String tag, JSONObject jsonObject, VolleyJsonInterface vif) {
        MyApplication.getHttp().cancelAll(tag);
        request = new JsonObjectRequest(Method.POST, url, jsonObject, vif.loadingListener(), vif.errorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json;charset=UTF-8");
                headers.put("ver", "1.1");
                headers.put("operation", "16777237");
                return super.getHeaders();
            }
        };

        request.setTag(tag);
        MyApplication.getHttp().add(request);
        MyApplication.getHttp().start();
    }
}
