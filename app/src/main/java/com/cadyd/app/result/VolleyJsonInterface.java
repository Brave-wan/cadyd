package com.cadyd.app.result;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by root on 16-8-30.
 */
public abstract class VolleyJsonInterface {



    public static Response.Listener<JSONObject> mListener;
    public static Response.ErrorListener mErrorListener;

    public VolleyJsonInterface( Response.Listener<JSONObject> listener) {
        this.mListener = listener;
    }

    public abstract void onMySuess(JSONObject result);



    public Response.Listener<JSONObject> loadingListener() {
        mListener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                onMySuess(response);
            }
        };
        return mListener;
    }

    public Response.ErrorListener errorListener() {
        mErrorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("wan","错误日志---"+error.getMessage());
            }

        };

        return mErrorListener;

    }
}
