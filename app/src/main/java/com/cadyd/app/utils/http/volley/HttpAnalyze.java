package com.cadyd.app.utils.http.volley;

import java.lang.reflect.Type;

import com.cadyd.app.utils.http.BaseResponse;
import com.google.gson.Gson;

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

    public String getString(Object obj, Type t) {
        return gson.toJson(obj, t);
    }
}