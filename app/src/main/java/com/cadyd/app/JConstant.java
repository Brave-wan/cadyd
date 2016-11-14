package com.cadyd.app;

import com.android.volley.DefaultRetryPolicy;

/**
 * 系统变量
 */


public interface JConstant {
    final boolean isHttp = false;
    final boolean isencrypt = true;

    final String LIVEURL = "http://114.55.58.18:8080";//直播的域名地址

    //public static final String SOCKETPATH = "192.168.0.188";

    //    public static final String SOCKETPATH = "114.55.58.18";
    //public static final String SOCKETPATH = "114.55.58.18";


//    public static final int SOCKETPORT = 9002;
    //    #define kUrlhttpReq @"http://schapp.cadyd.com"//URL头


    public static final String URlP = "http://114.55.58.18:8018/sch_app";//图片上传地址
    //final String URlP = "http://192.168.0.142:8080/sch_app";//袁

    //   final String URL = "http://schapp.cadyd.com/app";
//    final String URL = "http://114.55.58.18:8018/sch_app/app";
    final String URL = "http://192.168.0.200:8018/sch_app/app";//**
    //final String URL = "http://192.168.0.192:8080/sch_app/app";//周
    // final String URL = "http://192.168.0.230:8080/sch_app/app";//胡


    final String GRAPHICDETAILS = "http://home.cadyd.com/h5/1yuangou/tuwen.html?pro=";//图文详情h5地址 pro=id
    final String JISUAN = "http://home.cadyd.com/h5/1yuangou/jisuan.html?tid=";//计算详情h5地址
    final String YUANGOUFX = "http://home.cadyd.com/h5/1yuangou/fx.html?uid= ";//一元购返享h5地址
    final String YUANGOUWENTI = "http://home.cadyd.com/h5/1yuangou/wenti.html";//一元购常见问题h5

    final String LOGIN = URL.concat("member/login");
    final String VCODE = URL.concat("member/queryLoginSmsCode");//获取短信验证码
    final String REGISTER = URL.concat("member/regMember");//注册
    final String FINDPASSWORD = URL.concat("member/back");//找回密码
    final String CHANGEHEADIMAGE = URL.concat("member/editUserImg");//修改会员头像
    final String CHANGEUSERMESSAGE = URL.concat("member/editMember");//保存会员信息
    final String CHANGEPASSWORD = URL.concat("member/editPassword");//修改密码
    final String QUERYCLASS = URL.concat("groupBuyProduct/queryMallGroupbuyFilter");//一元购查询分类
    final String QUERYALLPRODUCTRECORD = URL.concat("groupBuyProduct/queryAllProductRecord");//{pageIndex}/{categroyId}/{order}查询商品
    final String QUERYBASERECORD = URL.concat("groupBuyProduct/queryBaseRecord/");//查询商品详情
    final String QUERYPRODUCTSHOWDOC = URL.concat("groupBuyProduct/queryProductShowDoc/");//{productId}/{pageIndex}分页查询商品晒单
    final String QURTYALLDOCUMENT = URL.concat("showdocument/queryAllDocument/");//pageIndex分页查询所有晒单
    final String QUERYPARTICIPATIONRECORD = URL.concat("groupBuyProduct/queryParticipationRecord/");//商品的乐购记录
    final String ADDSHOPPINGCAR = URL.concat("shoppingCart/addShoppingCart");//一元购加入购物车
    final String SHOPPINGCHECKOUT = URL.concat("shoppingCart/shoppingCheckOut");//立即一元乐购
    final String SELECUSERBYDE = URL.concat("member/selectUserByDe/");//通过会员id查询用户信息
    final String SAVELIKES = URL.concat("groupbuy/comments/saveLikes");//点赞
    final String QUERYSHOPPINGCARBYUSER = URL.concat("shoppingCart/queryShoppingCartByUser/");//查询购物车商品
    final String SAVEORDER = URL.concat("groupBuyProduct/saveOrder");//生成充值订单
    final String SELECTBYADDRESS = URL.concat("member/selectByAddress/");//查询地址
    final String DEFAULTTOADDRESS = URL.concat("member/defaultToAddress");//设置默认地址
    final String INSERTADDRESS = URL.concat("member/insertAddress?id=");//新增地址
    final String EDITADDRESS = URL.concat("member/editAddress");//修改地址
    final String DELETEADDRESS = URL.concat("member/deleteAddress");//删除地址
    final String PREKEY = URL.concat("mall/goods/range/pre?key=");//搜索宝贝的筛选信息
    final String SEARCHPRE = URL.concat("mall/goods/search/");//查询宝贝的查询
    final String QUERYMYALLRECORD = URL.concat("myRecord/queryMyAllRecord/");//我的记录查询
    final String QUERYMYALLPRODCT = URL.concat("myRecord/queryMyAllProdct/");//我的获得的奖品
    final String SELECTDETAILBYMID = URL.concat("member/selectDetailByMid");//查询用户信息post
    final String SHOPCOLLECT = URL.concat("mall/goods/shop/collect");//查询收藏的店铺
    final String COLLECTCANCELGOODS = URL.concat("mall/goods/collect/");//取消店铺收藏
    final String SELECTINTEGRATIONBYMID = URL.concat("member/selectIntegrationByMid");//通过会员ID查询积分明细
    final String SELECTCURRENTBYMID = URL.concat("myRecord/queryNature");//通过会员id查询花币明细 /myRecord/queryNature/{pageIndex}/{userId}
    final String SELECTRECHARGEBYID = URL.concat("/member/selectRechargeById");//通过id查询会员充值信息
    final String CLASSIFYMENUID = URL.concat("mall/goods/range/classify?menuId="); //商城商品的筛选信息 0541f1328adb4c25b7f636e093c30fdf
    final String FINDMENU = URL.concat("mall/goods/find/menu/");//0541f1328adb4c25b7f636e093c30fdf/1?field=overall //商城查询商品
    final String DELETESCHOPPIHNGCARTLIST = URL.concat("shoppingCart/deleteSchoppihngCartById");//一元购物车删除
    final String QUERYCOMMENTSBYPAGE = URL.concat("groupbuy/comments/queryCommentsByPage/");//一元购晒单全部评论 e3b6a8fc5ac74e3092831c09d2dcab24/1
    final String SAVECOMMENTS = URL.concat("groupbuy/comments/saveComments");//发送评论  AlertConfirmation
    final String SAVEDOCUMENT = URL.concat("showdocument/savedocumentpic");//新增晒单
    final String QUERYLUCKCODEBYUSERID = URL.concat("groupBuyAnnounced/queryLuckCodeByUserId/");//查询我的所有乐购码tbid / id / index
    final String UNCOMMENT = URL.concat("order/uncomment/");//商城商品晒单获得商品信息 d2b75525145a442d89a731d103f4c32d
    final String SAVEPIC = URL.concat("order/comment/savePic");//保存商城里面的评论的信息(新增晒单)
    final String FINDADVER_ONEYUAN = URL.concat("advertisement/findadver?position=");//一元购广告（广告，广告）
    final String UPDATEPHONE = URL.concat("member/updatePhone");//修改手机号码
    final String GROUPBUYANNOUNCED = URL.concat("groupBuyAnnounced/queryBaseRecord/");//最新揭晓详情查询{hastimes}/{tbid}/{productid}
    final String SAVEBALANCEORDER = URL.concat("/groupBuyProduct/saveBalanceOrder");//余额充值
    final String GOODSGLOBAL = URL.concat("mall/goods/global");//全球购的分类
    final String GOODSCOUNTRY = URL.concat("mall/goods/country");//一乡一物的分类
    final String GOODSLIST = URL.concat("mall/goods/list/");//商品列表
    final String QUERYPOPULARITY = URL.concat("seller/shop/queryPopularity");//更多旗舰店

    final String QUERYHOMEMENU = URL.concat("mainpage/channel");//查询系统主菜单分类
    final String RECOMMAND = URL.concat("/seller/shop/recommand");//推荐的旗舰店
    final String FINDADVER = URL.concat("advertisement/findadver?position=A101,A103,A104,A105,A106,A107,A108,A109,A110,A111&city=");//首页图片
    final String QUERYALLANNOUNCEDRECORD = URL.concat("groupBuyAnnounced/queryAllAnnouncedRecord/1/0");//最新揭晓
    final String ONEQUERYALLANNOUNCEDRECORD = URL.concat("groupBuyAnnounced/queryAllAnnouncedRecord/");//一元购最新揭晓
    final String QUERYRECOMMENDGOODS = URL.concat("productRelease/queryRecommendGoods/");//推荐商品
    final String ADVERTISEMENT_FINDADVER = URL.concat("advertisement/findadver?position=S101,S102,S103,S104");//商城首页图片
    final String MALL_INDEX = URL.concat("mall/goods/list/mall/1");//商城首页
    final String MALLGOODS = URL.concat("mainpage/find/classify/");//推荐商品
    final String FINDGOODS = URL.concat("/mall/goods/findgoods/new/");//商品详情/mall/goods/findgoods/{goodsId}?uid=xxxx&distr=xxxx
    final String FINDETAILGOODS = URL.concat("/mall/goods/findetailgoods/");//展示商品的图文详情及一些动态属性
    final String COMMENTCOUNT = URL.concat("/mall/goods/findgoods/comment/count/");//商品评论统计信息
    final String GOODSRECOMMEND = URL.concat("mall/goods/recommend/");//http://192.168.0.184:8080/sch/mall/goods/{type}/{goodsId}/{pageIndex} //为你推荐
    final String GOODSRANK = URL.concat("mall/goods/rank/");//http://192.168.0.184:8080/sch/mall/goods/{type}/{goodsId}/{pageIndex} //排行榜
    final String MALLGOODSTYPE = URL.concat("mall/goods/menu/");//商城首页分类
    final String GOODSMENU = URL.concat("mall/goods/menu/");/// 商城的分类信息mall/goods/menu/{menuId}/{pageIndex}
    final String SALEDYNAMIC = URL.concat("mall/goods/saledynamic/");//查询商品库存
    final String SAVE_CART = URL.concat("mall/goods/cart/save");//加入购物车
    final String PURCHASE = URL.concat("mall/goods/immediatelyOrder");//立即购买
    final String COLLECT = URL.concat("/mall/goods/collect");//商品收藏
    final String CANCEL_COLLECT = URL.concat("/mall/goods/collect/");//商品取消收藏
    final String CART_QUERY = URL.concat("mall/goods/cart/user/");//购物车查询
    final String UPDATE_CART = URL.concat("/mall/goods/cart/update");//购物车数量修改
    final String DELETE_CART = URL.concat("/mall/goods/cart/delete?key=");//删除购物车
    final String PREFER_FIND = URL.concat("/seller/prefer/find/net/");//商家优惠卷查询
    final String PREFER_RECEIVE = URL.concat("/seller/prefer/receive");//领取优惠卷
    final String ADDRESS_DEFAULT = URL.concat("mall/goods/cart/address/");//用户名人收货地址
    final String CONFIRM_SHOPPING_CART = URL.concat("/mall/goods/cart/");//确认订单页面

    final String GOODS_COMMENT_COUNT = URL.concat("mall/goods/findgoods/comment/count/");//评论统计
    final String GOODS_COMMENT = URL.concat("mall/goods/findgoods/comment/");//nice 好评  general 中评  bad 差评 img有图
    final String ORDER_SAVE = URL.concat("order/appSave");//确认下单
    final String ORDER_PAY = URL.concat("order/pay/");//支付
    final String ORDER_WX = URL.concat("order/pay");//支付
    final String ORDER_ALL = URL.concat("order/find/usr/");//订单查询
    final String ORDER_DELETE = URL.concat("/order/delete/");//删除订单
    final String ORDER_USR = URL.concat("order/cancel/usr");//订单取消原因获取
    final String ORDER_CANSEL = URL.concat("/order/cancel/usr/");//订单取消
    final String ORDER_UMIX = URL.concat("/order/umix?key=");//订单合并结算
    final String ORDER_DETAIL = URL.concat("/order/detail/%s/%s");///order/detail/{userId}/{oid}{userId}:用户的id。{oid}:订单的id 订单详情
    final String PROMPTLY_GOODS = URL.concat("/order/savesingle");//立即购买

    //***********************************************************************************************************************************************************************************************
    final String key = "PBKDF2WithHmacSHA1";

    final int SHOP_COUPON_LIST = 0x1000160;//店铺优惠卷
    final int USER_VIP_DETAIL_LIST = 0x1000153;//店铺品牌
    final int SHOP_BRANDS_CATEGORY = 0x1000155;//店铺品牌
    final int SHOP_GOODS_CATEGORY = 0x1000156;//店铺分类
    final int SHOP_INFO = 0x1000150;//店铺详情
    final int MESSAGE_LIST = 0x1000149;//消息中心列表
    final int LOGIN_ = 0x1000050;//登录
    final int QUERYMSGCODE = 0x1000089;//APP请求前获取验证码编号
    final int VCODE_ = 0x1000090;//获取短信验证码
    final int REGISTER_ = 0x1000051;//注册
    final int FINDNEWGOODSINTEGRAL = 0x1000104;//新品推荐
    final int AGREEMENTHTML = 0x1000157;//获得用户协议的html

    final int OSSFILEUOLPAD = 0x1000159;//上传图片接口


    final int QUERYHOMEMENU_ = 0x1000003;//16777219 //查询系统主菜单分类
    final int QUERYHOMEMENUALL_ = 0x1000004;//16777219 //查询所有菜单分类
    final int AllMENU_ = 0x1000004;//所有主页图标
    final int QUERYRECOMMENDGOODS_ = 0x1000048;//16777288 //推荐商品
    final int RECOMMAND_ = 0x1000079;//16777337  //推荐的旗舰店
    final int PREFER_RECEIVE_ = 0x1000080;//16777344 领取优惠卷
    final int FINDADVER_ = 0x1000002;//16777218 首页图片


    final int FINDPASSWORD_ = 0x1000049;//找回密码
    final int CHANGEHEADIMAGE_ = 0x1000052;//修改会员头像
    final int CHANGEUSERMESSAGE_ = 0x1000053;//保存会员信息
    final int CHANGEPASSWORD_ = 0x1000054;//修改密码
    final int QUERYCLASS_ = 0x1000027;//一元购查询分类
    final int QUERYALLPRODUCTRECORD_ = 0x1000028;//{pageIndex}/{categroyId}/{order}查询商品
    final int QUERYBASERECORD_ = 0x1000026;//查询商品详情(即将揭晓商品详情)
    final int QUERYPRODUCTSHOWDOC_ = 0x1000029;//{productId}/{pageIndex}分页查询商品晒单
    final int QURTYALLDOCUMENT_ = 0x1000088;//pageIndex分页查询我的晒单
    final int QUERYPARTICIPATIONRECORD_ = 0x1000030;//商品的乐购记录(参与记录)
    final int ADDSHOPPINGCAR_ = 0x1000020;//一元购加入购物车(商城加入购物车)
    final int SHOPPINGCHECKOUT_ = 0x1000021;//立即一元乐购
    final int SELECUSERBYDE_ = 0x1000055;//通过会员id查询用户信息
    final int SAVELIKES_ = 0x1000012;//点赞
    final int QUERYSHOPPINGCARBYUSER_ = 0x1000022;//查询一元购购物车商品
    final int SAVEORDER_ = 0x1000024;//生成充值订单
    final int SELECTBYADDRESS_ = 0x1000056;//查询地址
    final int DEFAULTTOADDRESS_ = 0x1000057;//设置默认地址
    final int INSERTADDRESS_ = 0x1000058;//新增地址
    final int EDITADDRESS_ = 0x1000059;//修改地址
    final int DELETEADDRESS_ = 0x1000060;//删除地址
    final int PREKEY_ = 0x1000006;//搜索宝贝的筛选信息
    final int SEARCHPRE_ = 0x1000005;//查询宝贝的查询
    final int QUERYMYALLRECORD_ = 0x1000017;//我的记录查询
    final int QUERYMYALLPRODCT_ = 0x1000018;//我的获得的奖品
    final int SELECTDETAILBYMID_ = 0x1000063;//查询用户信息post
    final int SHOPCOLLECT_ = 0x1000007;//查询收藏的 店铺 宝贝
    final int SELECTINTEGRATIONBYMID_ = 0x1000064;//通过会员ID查询积分明细
    final int SELECTCURRENTBYMID_ = 0x1000019;//通过会员id查询花币明细
    final int SELECTRECHARGEBYID_ = 0x1000065;//通过id查询会员充值信息
    final int CLASSIFYMENUID_ = 0x1000006; //商城商品的筛选信息 0541f1328adb4c25b7f636e093c30fdf
    final int FINDMENU_ = 0x1000009;//0541f1328adb4c25b7f636e093c30fdf/1?field=overall //商城查询商品
    final int DELETESCHOPPIHNGCARTLIST_ = 0x1000023;//一元购物车删除
    final int QUERYCOMMENTSBYPAGE_ = 0x1000014;//一元购晒单全部评论 e3b6a8fc5ac74e3092831c09d2dcab24/1
    final int SAVECOMMENTS_ = 0x1000013;//发送评论  AlertConfirmation
    final int SAVEDOCUMENT_ = 0x1000016;//新增晒单
    final int QUERYLUCKCODEBYUSERID_ = 0x1000010;//查询我的所有乐购码tbid / id / index
    final int UNCOMMENT_ = 0x1000075;//商城商品晒单获得商品信息 d2b75525145a442d89a731d103f4c32d
    final int SAVEPIC_ = 0x1000077;//保存商城里面的评论的信息(新增晒单)
    final int UPDATEPHONE_ = 0x1000061;//修改手机号码
    final int GROUPBUYANNOUNCED_ = 0x1000026;//最新揭晓详情查询{hastimes}/{tbid}/{productid}
    final int SAVEBALANCEORDER_ = 0x1000025;//余额充值
    final int GOODSGLOBAL_ = 0x1000081;//全球购的分类
    final int GOODSCOUNTRY_ = 0x1000086;//一乡一物的分类
    final int GOODSLIST_ = 0x1000047;//商品列表
    final int QUERYPOPULARITY_ = 0x1000085;//更多旗舰店
    final int QUERYAREASELLERSHOP = 0x1000148;//为您推荐的店铺
    final int QUERYGOODSGROUP = 0x1000152;//店铺首页商品查询
    final int QUERYBRABDABDADVERTISENEBT = 0x1000151;//店铺广告位和品牌
    final int QUERYSHOPIDTOLISTGOODS = 0x1000154;//店铺商品列表
    final int CONFIRMORDER = 0x1000092;//确认收货
    final int JJ = 0x1000161;//积分订单生成
    final int LOOK_LOGISTICS = 0x1000162;//物流
    final int DENIALREASON = 0x1000164;//查看退款信息

    /************************
     * 积分商城
     ********************************/
    final int FINDMENUINTEGRAL = 0x1000093;//查询积分商城类目
    final int FINDHOTGOODLIST = 0x1000102;//查询热门积分商品
    final int FINDDATA_INTEGRAL = 0x1000096;//今日新品
    final int GETBYID = 0x1000094;//查询商品详情
    final int SAVEINTEGBATCHORDER = 0x1000097;//积分商城下单
    final int FINDUSERINGRAL = 0x1000107;//查询积分
    final int SAVEINTEGRAL = 0x1000109;//加入购物车
    final int FINDBYMENUID = 0x1000095;//根据类目id查询商品
    final int PAYINTEGRAL = 0x1000098;//支付积分
    final int FINDINTEGRALGOODSCART = 0x1000112;//查询积分商品购物车列表
    final int INTEGRALSBYUID = 0x1000099;//查询消费记录
    final int UPDATEORDERINTEGRALSTATE = 0x1000138;//积分商城订单操作
    final int DELETEINTEGRAL = 0x1000100;//积分商城删除订单
    final int INTEGRALSBYOID = 0x1000139;//积分商城根据订单ID查询订单详情
    final int SAVEINTEGRALCOMMENT = 0x1000140;//保存评论信息
    final int SELECTCOMMENTINTEGRALGOODSBYUSERID = 0x1000141;//积分商城的我的评论列表
    final int INQUIRYINTEGRAL = 0x1000153;//查询积分
    final int MEMBER_RULE = 0x1000157;//积分规则
    final int INQUIRSHOPPREFER = 0x1000160;//分页查询店铺优惠券


    final int ONEQUERYALLANNOUNCEDRECORD_ = 0x1000042;//一元购最新揭晓
    final int ADVERTISEMENT_FINDADVER_ = 0;//商城首页图片
    final int MALL_INDEX_ = 0x1000047;//商城首页
    final int FINDGOODS_ = 0x1000039;//商品详情/mall/goods/findgoods/{goodsId}?uid=xxxx&distr=xxxx
    final int FINDETAILGOODS_ = 0x1000046;//展示商品的图文详情及一些动态属性
    final int COMMENTCOUNT_ = 0;//商品评论统计信息
    final int GOODSRECOMMEND_ = 0x1000045;//http://192.168.0.184:8080/sch/mall/goods/{type}/{goodsId}/{pageIndex} //为你推荐
    final int GOODSRANK_ = 0;//http://192.168.0.184:8080/sch/mall/goods/{type}/{goodsId}/{pageIndex} //排行榜
    final int MALLGOODSTYPE_ = 0x1000048;//商城首页分类
    final int GOODSMENU_ = 0x1000038;/// 商城的分类信息mall/goods/menu/{menuId}/{pageIndex}
    final int SALEDYNAMIC_ = 0x1000040;//查询商品库存
    final int SAVE_CART_ = 0x1000037;//加入购物车
    final int PURCHASE_ = 0x1000036;//立即购买
    final int COLLECT_ = 0x1000084;//商品收藏
    final int CANCEL_COLLECT_ = 0x1000008;//商品取消收藏
    final int CART_QUERY_ = 0x1000035;//购物车查询
    final int UPDATE_CART_ = 0x1000034;//购物车数量修改
    final int DELETE_CART_ = 0x1000033;//删除购物车
    final int PREFER_FIND_ = 0x1000078;//商家优惠卷查询
    final int ADDRESS_DEFAULT_ = 0x1000044;//用户默认收货地址
    final int CONFIRM_SHOPPING_CART_ = 0x1000032;//确认订单页面
    final int FINDGOODSBYROOTMENUID_ = 0x1000041;//商城商品分类查询
    final int SAVENOTICE = 0x1000101;//查询降价通知

    final int GOODS_COMMENT_COUNT_ = 0x1000043;//评论统计
    final int GOODS_COMMENT_ = 0x1000031;//nice 好评  general 中评  bad 差评 img有图
    final int ORDER_SAVE_ = 0x1000074;//确认下单
    final int ORDER_PAY_ = 0x1000073;//花幣支付
    final int ORDER_PAY_S = 0x1000072;//花商城支付
    final int ORDER_WX_ = 0x1000072;//支付
    final int ORDER_ALL_ = 0x1000076;//订单查询
    final int ORDER_DELETE_ = 0x1000071;//删除订单
    final int ORDER_USR_ = 0x1000069;//订单取消原因获取
    final int ORDER_CANSEL_ = 0x1000070;//订单取消
    final int APPLYREFUND = 0x1000163;//退款（需要退款原因）
    final int ORDER_UMIX_ = 0x1000068;//订单合并结算
    final int ORDER_DETAIL_ = 0x1000067;///order/detail/{userId}/{oid}{userId}:用户的id。{oid}:订单的id 订单详情
    final int PROMPTLY_GOODS_ = 0x1000066;//立即购买
    final int SUMMARY = 0x1000103;//套装查询
    final int SAVE_SUMMARY_CART = 0x1000037;//套装加入购物车
    final int LEAGUEPREFER = 0x1000105;
    final int LIVEDETIL = 0x1000170;
    final int LIVEWINDOW = 0x1000171;
    final int LOOKEVALUATE = 0x1000166;//查询晒单
    final int YIYUANMANAGER = 0x1000172;//一元乐购商品详情
    final int CALCULATIONDETAI = 0x1000173;//一元乐购的计算详情查询
    final int UPDATASIGNATURE = 0x1000174;//修改個人簽名
    final int SUGGESTIONS = 0x1000181;//修改個人簽名

    /**
     * 连接服务器失败，请稍后重试
     */
    final String network_msg = "您的网络不太给力，请稍后再试";
    final String network_not = "网络未连接";
    /**
     * 数据解析异常，请稍后重试
     */
    final String interface_msg = "数据解析异常\n请稍后重试";

    /**
     * 操作失败，请稍后重试
     */

    final String handle_msg = "操作失败\n请稍后重试";
    final int toast_time = 16000;
    final int DEFAULT_TIMEOUT_MS = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2;// FIXME
    final int DEFAULT_MAX_RETRIES = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;// 最大连接数
    final float DEFAULT_BACKOFF_MULT = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    final String EXTRA_INDEX = "COM.DIANXIN.MERCHANT.INDEX";
    final String customerServicePhone = "4000367887";//客服电话号码
    final int TITLETEXTCOLOR = R.color.text_primary_gray;//新标题的字体颜色

}
