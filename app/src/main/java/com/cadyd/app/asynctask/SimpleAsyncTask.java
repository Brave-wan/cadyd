package com.cadyd.app.asynctask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.result.ResultData;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.ProgressDialogUtil;
import com.cadyd.app.ui.view.toast.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wcy.android.utils.LogUtil;
import org.wcy.android.utils.NetworkUtil;
import org.wcy.common.utils.StringHex;
import org.wcy.common.utils.StringUtil;
import org.wcy.common.utils.aesutils.AESUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * function:对AsyncTask简单封装 添加进度条 使用泛型，增加通用性
 *
 * @param <T>
 * @author wangchaoyong
 */
public class SimpleAsyncTask<T> {

    private Context context;
    private ProgressDialogUtil dialog;
    private DataLoader<T> dataLoader;
    public boolean isDialog = false;// 是否显示dialog
    public boolean isMessage = true;// 是否显示提示信息
    public boolean cancelable = true;// 点击外部是否可以取消
    public String token = "0";
    public String message;// 提示信息
    public Class<?> cl = null;
    public Type type = null;
    private Handler mDelivery;
    private AlertConfirmation confirmation;

    public void setDataLoader(DataLoader<?> dataLoader2) {
        this.dataLoader = (DataLoader<T>) dataLoader2;
    }

    public SimpleAsyncTask(Context context) {
        this.context = context;
        mDelivery = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ResultData<T> result = (ResultData<T>) msg.obj;
                if (dialog != null) {
                    dialog.hide();
                }
                if (result != null) {
                    if (result.resultCode == 0) {
                        if (result != null && dataLoader != null) {
                            dataLoader.succeed(result.data);
                        }
                    } else {
                        toast(result.msg);
                    }
                }
            }
        };
    }

    //HttpUrl 域名
    public void start(String HttpUrl, final int url, final Map<String, Object> params, final Object object, Object tag) {
        final String str = StringHex.bytesToHexString(StringHex.intToByteArray1(url));
        LogUtil.i(SimpleAsyncTask.class, url + "请求地址：" + str);
        if (NetworkUtil.isNetworkAvailable(context)) {
            if (isDialog) {
                dialog = new ProgressDialogUtil(context, message);
                dialog.setCanselable(cancelable);
                try {
                    // 开启进度条
                    if (!((Activity) context).isFinishing())
                        dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /**判断是否有新的域名传入，如果没有就使用默认的域名*/
            String httpUrl;
            if (StringUtil.hasText(HttpUrl)) {
                httpUrl = HttpUrl;
            } else {
                /**判断是否使用自定义的域名，如果没有就使用默认的域名*/
                if (JConstant.isHttp) {
                    SharedPreferences sharedata = context.getSharedPreferences("data", 0);
                    String data = sharedata.getString("HttpUrl", null);
                    if (StringUtil.hasText(data)) {
                        httpUrl = data;
                    } else {
                        httpUrl = JConstant.URL;
                    }
                } else {
                    httpUrl = JConstant.URL;
                }
                Log.i("MyHttp", httpUrl);
            }
            final StringRequest request = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, httpUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonObject = null;
                    // response = AESUtils.encode(response, JConstant.key);
                    Log.i(str + "response----->" + url, response);
                    try {
                        if (StringUtil.hasText(response)) {
                            jsonObject = new JSONObject(response);
                        }
                        if (jsonObject != null) {
                            int code = jsonObject.getInt("state");
                            //成功
                            if (code == 100) {
                                if (cl == null && type == null) {
                                    dataLoader.succeed((T) "操作成功");
                                    // 关闭进度条
                                    if (isDialog)
                                        dialog.hide();
                                } else {
                                    String data = getData(jsonObject.getString("data"));
                                    if (StringUtil.hasText(data)) {
                                        thread(data);
                                    } else {
                                        dialog.hide();
                                    }
                                }
                            } else if (code == 401) {//用戶登錄過期
                                if (isDialog)
                                    dialog.hide();
                                Log.i("huang", "----------------------------弹出提示框");
                                if (confirmation != null && !context.equals(confirmation.getContext())) {
                                    confirmation = null;
                                }
                                if (confirmation == null) {
                                    confirmation = new AlertConfirmation(context, "掉线通知", "您已掉线是否重新登录!");
                                }
                                confirmation.show(new AlertConfirmation.OnClickListeners() {
                                    @Override
                                    public void ConfirmOnClickListener() {
                                        confirmation.hide();
                                        Intent intent = new Intent(context, SignInActivity.class);
                                        context.startActivity(intent);
                                    }

                                    @Override
                                    public void CancelOnClickListener() {
                                        confirmation.hide();
                                    }
                                });
                            } else if (code == -100) {//系統錯誤
                                dialog.hide();
                                dataLoader.error("");
                                toast(JConstant.network_msg);
                            } else if (code == 10000) {//業務錯誤
                                if (dialog != null)
                                    dialog.hide();
                                String msg = jsonObject.getString("errorMsg");
                                if (StringUtil.hasText(msg)) {
                                    toast(msg);
                                    dataLoader.error(msg);
                                } else {
                                    toast("业务错误请重试");
                                }
                            }
                        } else {
                            toast(JConstant.network_msg);
                        }
                    } catch (Exception e) {
                        toast(JConstant.interface_msg + str);
                        Log.e("xiongmao", "-------------------------------");
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    toast(JConstant.network_msg);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json;charset=UTF-8");
                    /****/
                    MyApplication application = (MyApplication) context.getApplicationContext();
                    if (application.isLogin()) {
                        token = application.model.token;
                        Log.e("token", token);
                    }
                    /****/
                    headers.put("security", token);
                    headers.put("ver", "1.2");
                    headers.put("operation", String.valueOf(url));
                    return headers;
                }

                @Override
                public byte[] getPostBody() throws AuthFailureError {
                    String p = "";
                    if (params != null) {
                        p = ApiClient.getGson().toJson(params);
                        Log.i("xiongmao", str + "---->" + url + "参数：" + p);
                        if (JConstant.isencrypt) {
                            p = AESUtil.AESDoubleEncode(JConstant.key, p);//将上传数据进行加密
                            System.out.println(str + "---->" + url + "参数：" + p);
                        }
                        return p.getBytes();
                    } else if (object != null) {
                        System.out.println("first ++ :" + str + "---->" + url + "参数：" + p);
                        p = ApiClient.getGson().toJson(object);
                        if (JConstant.isencrypt) {
                            p = AESUtil.AESDoubleEncode(JConstant.key, p);//将上传数据进行加密
                            System.out.println(str + "---->" + url + "参数：" + p);
                        }
                        return p.getBytes();
                    } else {
                        return super.getPostBody();
                    }
                }
            };
            //超时时间10s,最大重连次数2次
            request.setRetryPolicy(new DefaultRetryPolicy(JConstant.DEFAULT_TIMEOUT_MS, JConstant.DEFAULT_MAX_RETRIES, JConstant.DEFAULT_BACKOFF_MULT));
            ApiClient.addRequest(request, tag);
        } else {
            toast(JConstant.network_not);
        }

    }


    private String getData(String json) {
        String dataValue = "";
        try {
            JSONObject dataObj = new JSONObject(json);
            JSONArray array = dataObj.names();
            if (array.length() == 1) {
                dataValue = dataObj.get(array.get(0).toString()).toString();
            } else {
                if (type != null) {
                    dataValue = dataObj.getString("data");
                } else {
                    dataValue = json;
                }
            }
        } catch (Exception e) {
            dataValue = json;
        }
        return dataValue;
    }


    private void thread(final String content) {
        // 创建子线程，在子线程中处理耗时工作
        new Thread() {
            @Override
            public void run() {
                if (dataLoader != null) {
                    dataLoader.task(content);
                    ResultData<T> resultData = new ResultData<>();
                    resultData.resultCode = 0;
                    if (StringUtil.hasText(content)) {
                        try {
                            if (cl != null) {
                                if (cl == Boolean.class) {
                                    resultData.data = (T) Double.valueOf(content);
                                } else if (cl == String.class) {
                                    resultData.data = (T) content;
                                } else if (cl == Integer.class) {
                                    resultData.data = (T) Integer.getInteger(content);
                                } else {
                                    resultData.data = (T) ApiClient.getGson().fromJson(content, cl);
                                }
                            } else if (type != null) {
                                resultData.data = ApiClient.getGson().fromJson(content, type);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            resultData.resultCode = -1;
                            resultData.msg = JConstant.handle_msg;
                        }
                    } else {
                        resultData.resultCode = -1;
                        resultData.msg = JConstant.handle_msg;
                    }
                    Message message = new Message();
                    message.obj = resultData;
                    mDelivery.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    private void toast(String msg) {
        if (isDialog) {
            if (dialog != null) {
                dialog.hide();
            }
            if (isMessage) {
                ToastUtils.show(context, msg, ToastUtils.ERROR_TYPE);
            }
        }
        if (dataLoader != null) {
            dataLoader.error(msg);
        }
    }


}