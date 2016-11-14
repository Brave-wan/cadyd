package com.cadyd.app.utils;

import com.cadyd.app.model.SocketMode;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * @author wandeying
 * @ClassName: HttpAnalyze
 * @Description: TODO(gson解析)
 * @date 2015年9月21日 下午2:14:14
 */
public class HttpAnalyze {

    static Gson gson;

    public HttpAnalyze() {
        super();
        gson = new Gson();
    }

    public <T> BaseResponse<T> phoneAnalyze(String result, Type t) throws Exception {
        BaseResponse<T> response;
        try {
            response = gson.fromJson(result, t);
        } catch (Exception e) {
            response = null;
            throw e;
        }
        return response;
    }

    /**
     * 根据json解析对象
     *
     * @param result
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T analyze(String result, Type t) throws Exception {
        T response;
        try {
            response = gson.fromJson(result, t);
        } catch (Exception e) {
            response = null;
            throw e;
        }
        return response;
    }

    /**
     * @param json
     * @return
     */
    public SocketMode getParseSocket(String json) {
        SocketMode socketMode = null;
        try {
            socketMode = new HttpAnalyze().analyze(json, SocketMode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socketMode;
    }

    public String getString(Object obj, Type t) {
        return gson.toJson(obj, t);
    }
}