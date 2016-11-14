package com.cadyd.app.asynctask.retrofitmode;


import com.cadyd.app.JConstant;
import com.cadyd.app.api.ApiClient;
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
import com.cadyd.app.model.StringModel;
import com.cadyd.app.model.ThreeWayBindingModel;
import com.cadyd.app.model.UserCenterInfo;
import com.cadyd.app.model.UserCentervideoInfo;
import com.cadyd.app.model.UserMassge;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;


/**
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 *
 * @time 16-9-28 下午3:30
 */
public interface AppService {

    /**
     * ④：多文件上传时：@PartMap r(@PartMap Map photos, @Part("description") RequestBody description);
     * ①：携带数据类型为对象时：@Body
     * ③：单文件上传时：@Part (@Part("photo") RequestBody photo, @Part("description") RequestBody description);
     * ②：携带数据类型为表单键值对时：@Field (@Field("first_name") String first, @Field("last_name") String last);
     *
     * @GET("group/{id}/users") Call> groupList(@Path("id") int groupId, @QueryMap Map options);
     * Header : 一种用于携带消息头的请求方式
     * @Headers("Cache-Control: max-age=640000")
     * @Headers({ "Accept: application/vnd.github.v3.full+json",
     * "User-Agent: Retrofit-Sample-App"
     * )
     */

    @POST("app")
    @Headers("operation:" + 0x1000174)
    Observable<StringModel> setSigna(@Body String string);

    @POST("app")
    @Headers("operation:" + 0x2020302)
    Observable<BaseRequestInfo<UserCenterInfo>> getUserCenter(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2010202)
    Observable<BaseRequestInfo> getUserAttention(@Body RequestBody body);


    @POST("app")
    @Headers("operation:" + 0x2010203)
    Observable<BaseRequestInfo> doCancelFoucs(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020305)
    Observable<BaseRequestInfo<AttentionUserInfoListDataModel>> getAttentionList(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020304)
    Observable<BaseRequestInfo<AttentionUserInfoListDataModel>> getFansList(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020303)
    Observable<BaseRequestInfo<UserCentervideoInfo>> getUserCenterVideo(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020101)
    Observable<BaseRequestInfo<LiveHotInfo>> getLiveHotList(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020102)
    Observable<BaseRequestInfo<LiveNewInfo>> getLiveNewList(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020205)
    Observable<BaseRequestInfo<LiveStartInfo>> doLiveOn(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2010201)
    Observable<BaseRequestInfo<ShareInfo>> getShareInfo(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020206)
    Observable<BaseRequestInfo<LiveEndInfo>> doLiveEnd();

    @POST("app")
    @Headers("operation:" + 0x2010101)
    Observable<BaseRequestInfo<List<ReportItemModel>>> getDictionaryItem(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2010204)
    Observable<BaseRequestInfo<String>> ConfirmTheReport(@Body RequestBody body);//执行举报

    @POST("app")
    @Headers("operation:" + 0x2030101)
    Observable<BaseRequestInfo<MessageCountInfo>> getMessageTotalCount();

    @POST("app")
    @Headers("operation:" + 0x2030102)
    Observable<BaseRequestInfo<List<MessageCountInfo>>> getMessageCount();

    @POST("app")
    @Headers("operation:" + 0x2030104)
    Observable<BaseRequestInfo<MessageListInfo>> getMessageList(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2030105)
    Observable<BaseRequestInfo<MessageDetailsInfo>> getSignMessageDetail(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020207)
    Observable<BaseRequestInfo> doSignWithMerchant(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2030103)
    Observable<BaseRequestInfo> setRead(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020202)
    Observable<BaseRequestInfo<LivePersonalDetailIfon>> getLivePersonalDetails(@Body RequestBody body);


    @POST("app")
    @Headers("operation:" + 0x2020201)
    Observable<BaseRequestInfo<LivePersonalDetailIfon>> getLiveBusinessDetails(@Body RequestBody body);


    @POST("app")
    @Headers("operation:" + 0x2020306)
    Observable<BaseRequestInfo<RecentVisitorsModel>> getRecentVisitors(@Body RequestBody body);//最近访客

    @POST("app")
    @Headers("operation:" + 0x2020208)
    Observable<BaseRequestInfo> validateSignCode(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2020301)
    Observable<BaseRequestInfo<BalanceModel>> getBalance(@Body RequestBody body);//查询余额

    @POST("app")
    @Headers("operation:" + 0x2020204)
    Observable<BaseRequestInfo> Gifts(@Body RequestBody body);//查询余额

    @POST("app")
    @Headers("operation:" + 0x2020203)
    Observable<BaseRequestInfo<ProductsFeaturedInfo>> getProduectList(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x2040000)
    Observable<BaseRequestInfo<OSSTokenInfo>> getFederationToken();

    @POST("app")
    @Headers("operation:" + 0x1000178)
    Observable<BaseRequestInfo<SignInModel>> doThirdLogin(@Body RequestBody body);


    @POST("app")
    @Headers("operation:" + 0x1000179)
    Observable<BaseRequestInfo<SignInModel>> getLoginBinding(@Body RequestBody body);

    @POST("app")
    @Headers("operation:" + 0x1000180)
    Observable<BaseRequestInfo<SignInModel>> tripartiteRegistration(@Body RequestBody body);//绑定注册


    @POST("app")
    @Headers("operation:" + 0x1000089)
    Observable<BaseRequestInfo<ImageVerificationCodeModel>> getImageVerificationCode();//获取图片验证码

    @POST("app")
    @Headers("operation:" + 0x1000090)
    Observable<BaseRequestInfo> getSMSCode(@Body RequestBody body);//获取短信验证码


    @POST("app")
    @Headers("operation:" + 0x1000175)
    Observable<BaseRequestInfo<List<ThreeWayBindingModel>>> getThreeWayBinding();  //查询三方绑定

    @POST("app")
    @Headers("operation:" + 0x1000176)
    Observable<BaseRequestInfo> setBinding(@Body RequestBody body);  //绑定用户

    @POST("app")
    @Headers("operation:" + 0x1000177)
    Observable<BaseRequestInfo> Unbind(@Body RequestBody body);  //绑定用户

    @POST("app")
    @Headers("operation:" + JConstant.SELECUSERBYDE_)
    Observable<BaseRequestInfo<UserMassge>> getUserMassge();  //查询用户基础信息

    @POST("app")
    @Headers("operation:" + 0x2020209)
    Observable<BaseRequestInfo<LivingInfo>> getLiving();  //查询用户直播状态

    @POST("app")
    @Headers("operation:" + 0x2010205)
    Observable<BaseRequestInfo<FlowerCoinsRechargeModel>> FlowerCoinsRecharge(@Body RequestBody body);//花币充值

    @POST("app")
    @Headers("operation:" + 0x2010206)
    Observable<BaseRequestInfo> PayTheSuccessNotification(@Body RequestBody body);//花币充值支付成功通知

    @POST("app")
    @Headers("operation:" + 0x2010207)
    Observable<BaseRequestInfo<SpendMoneyModel>> getRechargeInformation();//获得充值信息

    @POST("app")
    @Headers("operation:" + 0x2010208)
    Observable<BaseRequestInfo<RechargeRecordModel>> getRechargeRecord(@Body RequestBody body);//获得花币充值记录

    @POST("app")
    @Headers("operation:" + 0x1000181)
    Observable<BaseRequestInfo> suggestions(@Body RequestBody body);//提交投诉建议
}
