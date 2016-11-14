package com.cadyd.app.comm;

import android.content.Context;

import com.cadyd.app.interfaces.OnLoginListener;

/**
 * Created by xiongmao on 2016/10/25.
 */

public class Login {

    /*
    * 演示执行第三方登录/注册的方法
    * <p>
    * 这不是一个完整的示例代码，需要根据您项目的业务需求，改写登录/注册回调函数
    *
    * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
    */
    public static void login(Context context, final String platformName, OnLoginListener loginListener) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(loginListener);
        //userInfo.setPlatformName(platformName);
        api.login(context);
    }

}
