package com.cadyd.app.asynctask.retrofitmode;


import android.util.Log;

import com.cadyd.app.MyApplication;
import com.cadyd.app.model.AttentionUserInfoListDataModel;
import com.cadyd.app.model.BalanceModel;
import com.cadyd.app.model.FlowerCoinsRechargeModel;
import com.cadyd.app.model.ImageVerificationCodeModel;
import com.cadyd.app.model.LiveEndInfo;
import com.cadyd.app.model.LiveHotInfo;
import com.cadyd.app.model.LiveNewInfo;
import com.cadyd.app.model.LivePersonalDetailIfon;
import com.cadyd.app.model.LiveStartInfo;
import com.cadyd.app.model.LivingInfo;
import com.cadyd.app.model.MessageCountInfo;
import com.cadyd.app.model.MessageDetailsInfo;
import com.cadyd.app.model.MessageListInfo;
import com.cadyd.app.model.OSSTokenInfo;
import com.cadyd.app.model.ProductsFeaturedInfo;
import com.cadyd.app.model.RecentVisitorsModel;
import com.cadyd.app.model.RechargeRecordModel;
import com.cadyd.app.model.ReportItemModel;
import com.cadyd.app.model.ShareInfo;
import com.cadyd.app.model.SignInModel;
import com.cadyd.app.model.SpendMoneyModel;
import com.cadyd.app.model.ThreeWayBindingModel;
import com.cadyd.app.model.UserCenterInfo;
import com.cadyd.app.model.UserCentervideoInfo;
import com.cadyd.app.model.UserMassge;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 *
 * @time 16-9-28 下午3:30
 */
public class HttpMethods {
    //线上测试服务器
//    public static final String BASE_URL = "http://114.55.58.18:7890/live.webapi/";
    //何余
//    public static final String BASE_URL = "http://192.168.0.184:8080/live.webapi/";
    //李建
    //  public static final String BASE_URL = "http://192.168.0.195:8080/live.webapi/";
    //测试服务器
    public static final String BASE_URL = "http://192.168.0.202:8090/live.webapi/";

//    public static final String BASE_URL = "http://192.168.0.222:8090/live.webapi/";

    // public static final String BASE_URL = "http://192.168.0.193:8080/live.webapi/";//袁

    // public static final String BASE_URL = "http://192.168.0.192:8080/live.webapi/";//周

    //public static final String BASE_URL = "http://114.55.58.18:8018";
    //     public static final String BASE_URL = "http://114.55.58.18:8018/sch_app/";


    private static final int DEFAULT_TIMEOUT = 120;
    private final static int WRITE_TIMEOUT = 40;
    private final static int READ_TIMEOUT = 60;
    private Retrofit retrofit;
    private AppService mAppService;
    private static String token;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);//设置读取超时时间
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);//设置写的超时时间
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);//设置链接时间
        builder.addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY));
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json;charset=UTF-8")
                        .addHeader("security", token)
                        .addHeader("ver", "1.2")//版本號
                        .addHeader("deviceType", "20")
                        .build();
                return chain.proceed(newRequest);
            }
        });

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        mAppService = retrofit.create(AppService.class);
    }


    public static HttpMethods getInstance() {
        SignInModel model = MyApplication.getInstance().getSignInModel();
        if (model != null) {

            if (model.token != null) {
                token = model.token;
            } else {
                token = "";
            }
            Log.i("wan", "请求token----" + token);
        } else {
//            throw new ApiException(ApiException.AUTHORIZATION_EXPIRED);
        }
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用户中心
     *
     * @param subscriber
     * @param body
     */
    public void getUserCenterInfo(Subscriber<BaseRequestInfo<UserCenterInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.getUserCenter(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 关注
     */
    public void getUserCenterVideo(Subscriber<BaseRequestInfo<UserCentervideoInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.getUserCenterVideo(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 取消关注
     *
     * @param subscriber
     * @param body
     */
    public void doCancelFoucs(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.doCancelFoucs(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 关注
     *
     * @param subscriber
     * @param body
     */
    public void getUserAttention(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.getUserAttention(body);
        toSubscribe(observable, subscriber);
    }

    public void getAttentionList(Subscriber<BaseRequestInfo<AttentionUserInfoListDataModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.getAttentionList(body);
        toSubscribe(observable, subscriber);
    }

    public void getFansList(Subscriber<BaseRequestInfo<AttentionUserInfoListDataModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.getFansList(body);
        toSubscribe(observable, subscriber);
    }

    public void getLiveHotList(Subscriber<BaseRequestInfo<LiveHotInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.getLiveHotList(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 首页直播热门列表
     *
     * @param subscriber
     * @param body
     */
    public void getLiveHot(Subscriber<BaseRequestInfo<List<LiveHotInfo>>> subscriber, RequestBody body) {
        Observable observable = mAppService.getLiveHotList(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 首页直播最新列表
     *
     * @param subscriber
     * @param body
     */
    public void getLiveNewList(Subscriber<BaseRequestInfo<LiveNewInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.getLiveNewList(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 开启直播
     *
     * @param subscriber
     * @param body
     */
    public void doLiveOn(Subscriber<BaseRequestInfo<LiveStartInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.doLiveOn(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 分享
     *
     * @param subscriber
     * @param body
     */
    public void getShareInfo(Subscriber<BaseRequestInfo<ShareInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.getShareInfo(body);
        toSubscribe(observable, subscriber);
    }

    public void getMessageTotalCount(Subscriber<BaseRequestInfo<MessageCountInfo>> subscriber) {
        Observable observable = mAppService.getMessageTotalCount();
        toSubscribe(observable, subscriber);
    }

    public void getMessageCount(Subscriber<BaseRequestInfo<List<MessageCountInfo>>> subscriber) {
        Observable observable = mAppService.getMessageCount();
        toSubscribe(observable, subscriber);
    }

    public void getMessageList(Subscriber<BaseRequestInfo<MessageListInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.getMessageList(body);
        toSubscribe(observable, subscriber);
    }

    public void getSignMessageDetail(Subscriber<BaseRequestInfo<MessageDetailsInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.getSignMessageDetail(body);
        toSubscribe(observable, subscriber);
    }

    public void doSignWithMerchant(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.doSignWithMerchant(body);
        toSubscribe(observable, subscriber);
    }

    public void setRead(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.setRead(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 结束直播
     *
     * @param subscriber
     */
    public void doLiveEnd(Subscriber<BaseRequestInfo<LiveEndInfo>> subscriber) {

        Observable observable = mAppService.doLiveEnd();
        toSubscribe(observable, subscriber);
    }

    /**
     * 字典查询
     *
     * @param subscriber
     */
    public void getDictionaryItem(Subscriber<BaseRequestInfo<List<ReportItemModel>>> subscriber, RequestBody body) {
        Observable observable = mAppService.getDictionaryItem(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 执行举报
     *
     * @param subscriber
     * @param body
     */
    public void ConfirmTheReport(Subscriber<BaseRequestInfo<String>> subscriber, RequestBody body) {
        Observable observable = mAppService.ConfirmTheReport(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 直播个人详情页
     *
     * @param subscriber
     * @param body
     */
    public void getLivePersonalDetailInfo(Subscriber<BaseRequestInfo<LivePersonalDetailIfon>> subscriber, RequestBody body) {
        Observable observable = mAppService.getLivePersonalDetails(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 直播商家详情页
     *
     * @param subscriber
     * @param body
     */
    public void getLiveBusinessDetailsInfo(Subscriber<BaseRequestInfo<LivePersonalDetailIfon>> subscriber, RequestBody body) {
        Observable observable = mAppService.getLiveBusinessDetails(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 最近访客
     *
     * @param subscriber
     * @param body
     */
    public void getRecentVisitors(Subscriber<BaseRequestInfo<RecentVisitorsModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.getRecentVisitors(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 验证商家码
     *
     * @param subscriber
     * @param body
     */
    public void validateSignCode(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.validateSignCode(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 查询余额
     *
     * @param subscriber
     * @param body
     */
    public void getBalance(Subscriber<BaseRequestInfo<BalanceModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.getBalance(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 送礼
     *
     * @param subscriber
     * @param body
     */
    public void Gifts(Subscriber subscriber, RequestBody body) {
        Observable observable = mAppService.Gifts(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 商家推荐商品列表
     *
     * @param subscriber
     * @param body
     */
    public void getProductsListInfo(Subscriber<BaseRequestInfo<ProductsFeaturedInfo>> subscriber, RequestBody body) {
        Observable observable = mAppService.getProduectList(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取上传图片的token
     *
     * @param subscriber
     */
    public void getFederationToken(Subscriber<BaseRequestInfo<OSSTokenInfo>> subscriber) {
        Observable observable = mAppService.getFederationToken();
        toSubscribe(observable, subscriber);
    }

    /**
     * 三方登录
     *
     * @param subscriber
     */
    public void doThirdLogin(Subscriber<BaseRequestInfo<SignInModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.doThirdLogin(body);
        toSubscribe(observable, subscriber);
    }


    /**
     * 绑定登录
     *
     * @param subscriber
     */
    public void getLoginBindingInfo(Subscriber<BaseRequestInfo<SignInModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.getLoginBinding(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 三方绑定注册
     *
     * @param subscriber
     * @param body
     */
    public void tripartiteRegistration(Subscriber<BaseRequestInfo<SignInModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.tripartiteRegistration(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取图片验证码
     *
     * @param subscriber
     */
    public void getImageVerificationCode(Subscriber<BaseRequestInfo<ImageVerificationCodeModel>> subscriber) {
        Observable observable = mAppService.getImageVerificationCode();
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取短信验证码
     *
     * @param subscriber
     * @param body
     */
    public void getSMSCode(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.getSMSCode(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取三方绑定
     *
     * @param subscriber
     */
    public void getThreeWayBinding(Subscriber<BaseRequestInfo<List<ThreeWayBindingModel>>> subscriber) {
        Observable observable = mAppService.getThreeWayBinding();
        toSubscribe(observable, subscriber);
    }

    /**
     * 绑定用户
     *
     * @param subscriber
     * @param body
     */
    public void setBinding(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.setBinding(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 解除绑定
     *
     * @param subscriber
     * @param body
     */
    public void Unbind(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.Unbind(body);
        toSubscribe(observable, subscriber);
    }

    public void getUserMassge(Subscriber<BaseRequestInfo<UserMassge>> subscriber) {
        Observable observable = mAppService.getUserMassge();
        toSubscribe(observable, subscriber);
    }

    public void getLiving(Subscriber<BaseRequestInfo<LivingInfo>> subscriber) {
        Observable observable = mAppService.getLiving();
        toSubscribe(observable, subscriber);
    }

    /**
     * 花币充值
     *
     * @param subscriber
     * @param body
     */
    public void FlowerCoinsRecharge(Subscriber<BaseRequestInfo<FlowerCoinsRechargeModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.FlowerCoinsRecharge(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 花币充值支付成功通知
     *
     * @param subscriber
     * @param body
     */
    public void PayTheSuccessNotification(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.PayTheSuccessNotification(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获得充值信息
     *
     * @param subscriber
     */
    public void getRechargeInformation(Subscriber<BaseRequestInfo<SpendMoneyModel>> subscriber) {
        Observable observable = mAppService.getRechargeInformation();
        toSubscribe(observable, subscriber);
    }

    /**
     * 获得花币充值记录
     *
     * @param subscriber
     */
    public void getRechargeRecord(Subscriber<BaseRequestInfo<RechargeRecordModel>> subscriber, RequestBody body) {
        Observable observable = mAppService.getRechargeRecord(body);
        toSubscribe(observable, subscriber);
    }

    public void suggestions(Subscriber<BaseRequestInfo> subscriber, RequestBody body) {
        Observable observable = mAppService.suggestions(body);
        toSubscribe(observable, subscriber);
    }

}
