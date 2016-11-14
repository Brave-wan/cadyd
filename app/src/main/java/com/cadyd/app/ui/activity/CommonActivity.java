package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.cadyd.app.AppManager;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.*;
import com.cadyd.app.ui.fragment.Integralmall.IntegralCarFragment;
import com.cadyd.app.ui.fragment.Integralmall.IntegralPaymentFragment;
import com.cadyd.app.ui.fragment.cart.ConfirmShoppingCartFragment;
import com.cadyd.app.ui.fragment.cart.HomeShoppingCartFragment;
import com.cadyd.app.ui.fragment.cart.PromptlyGoodsFragment;
import com.cadyd.app.ui.fragment.cart.ShoppingCartFragment;
import com.cadyd.app.ui.fragment.mall.*;
import com.cadyd.app.ui.fragment.pay.NewPayGoodsFragment;
import com.cadyd.app.ui.fragment.pay.PayGoodsFragment;
import com.cadyd.app.ui.fragment.pay.PaymentResultFragment;
import com.cadyd.app.ui.fragment.unitary.*;
import com.cadyd.app.ui.fragment.unitary.OneYuanDetailsFragment;
import com.cadyd.app.ui.fragment.unitary.OneYuanShoppingCarFragment;
import com.cadyd.app.ui.fragment.unitary.PaymentFragment;
import com.cadyd.app.ui.fragment.user.*;
import com.cadyd.app.ui.fragment.user.integral.IntegralEvaluateListFragment;
import com.cadyd.app.ui.fragment.user.integral.IntegralMyOrderFragment;
import com.cadyd.app.ui.fragment.user.integral.IntegralOrderDetailFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class CommonActivity extends BaseFragmentActivity {
    public static final int INDEX_CITY = 1;
    public static final int ADDRESSFRAGMENT = 2;
    public static final int INDEX_SHOPPING = 3;
    public static final int INDEX_SHOPPING_CART = 4;
    public static final int USER_CENTENT = 5;
    public static final int ONE_YUAN_DETAIL = 6;
    public static final int THE_SUN = 7;
    public static final int MY_ORDER = 8;
    public static final int ORDER_DETAIL = 9;
    public static final int USERINFO_EDIT = 10;
    public static final int GOODS_COMMENT = 11;
    public static final int GOODS_SEARCH = 12;
    public static final int ONE_YUAN_CAR = 13;
    public static final int GOODS_TYPE = 14;
    public static final int RANK_GOODS = 15;
    public static final int RECOMMENT_GOODS = 16;
    public static final int GOODS_PAY = 17;
    public static final int ONE_YUAN_PAY_MENT = 18;
    public static final int GOODS_EVALUATE = 19;
    public static final int NEW_ANNOUNCED = 20;
    public static final int GOODS_PURCHASE = 21;
    public static final int PAY_STATUS = 22;
    public static final int MY_COLLECTION = 23;
    public static final int FLAGSHIP = 24;
    public static final int SALESPACKAGE = 25;
    public static final int LEAGUEPREFER = 26;
    public static final int INTEGRALDETAIL = 27;
    public static final int INTEGRALCAR = 28;
    public static final int INTEGRALMYORDER = 29;
    public static final int INTEGRALORDERDETAIL = 30;
    public static final int MYHEAD = 31;
    public static final int MYBALANCEFRAGMENT = 32;
    public static final int USERSHOPPINGCOLLECTIONFRAGMENT = 33;
    public static final int MYRECORDFRAGMENT = 34;
    public static final int THESUNFRAGMENT = 35;
    public static final int USERADDRESSFRAGMENT = 36;
    public static final int USERSETINGFRAGMENT = 37;
    public static final int INTEGRALEVALUATELISTFRAGMENT = 38;
    public static final int CONFIRMSHOPPINGCARTFRAGMENT = 39;
    public static final int SEARCH_PAGE = 40;
    public static final int FLOWERCOINSRECHARGE = 41;
    public static final int NEWFLOWERCOINSRECHARGE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        final Intent intent = getIntent();
        int index = intent.getIntExtra(JConstant.EXTRA_INDEX, -1);
        switch (index) {
            case INDEX_CITY://地区选择
                replaceFragment(new CityFragment());
                break;
            case ADDRESSFRAGMENT:
                replaceFragment(new UserAddressFragment());
                break;
            case INDEX_SHOPPING://商城首页
                replaceFragment(new MallFragment());
                break;
            case INDEX_SHOPPING_CART://购物车
                replaceFragment(HomeShoppingCartFragment.newInstance(true));
                break;
            case USER_CENTENT://用户中心
                replaceFragment(new UserCententFragment());
                break;
            case ONE_YUAN_DETAIL://一元购商品详情
                OneGoodsModel model = (OneGoodsModel) intent.getSerializableExtra("goods");
                String tbid;
                String hastimes = null;
                if (model == null) {
                    tbid = intent.getStringExtra("tbid");
                } else {
                    tbid = model.tbid;
                    hastimes = model.hastimes;
                }
                OneYuanDetailsFragment oneYuanDetailsFragment = OneYuanDetailsFragment.newInstance(tbid, hastimes);
                oneYuanDetailsFragment.setTop();
                replaceFragment(oneYuanDetailsFragment);
                break;
            case THE_SUN://晒单
                TheSunFragment theSunFragment = new TheSunFragment();
                String title = intent.getExtras().getString("title");
                theSunFragment.Title = title;
                replaceFragment(theSunFragment);
                break;
            case USERINFO_EDIT://用户信息编辑
                replaceFragment(new UserInformationFragment());
                break;
            case GOODS_COMMENT://商品评论
                GoodsCommentFragment fragment = GoodsCommentFragment.newInstance(intent.getStringExtra("gid"), intent.getBooleanExtra("image", false));
                replaceFragment(fragment);
                break;
            case GOODS_SEARCH://商品搜索
                replaceFragment(new SearchFragment());
                break;
            case ONE_YUAN_CAR://进入一元购购物车
                OneYuanShoppingCarFragment carFragment = new OneYuanShoppingCarFragment();
                replaceFragment(carFragment);
                break;
            case GOODS_TYPE://商品类型
                ShopGoodsSelectFragment goodsTypefragment = ShopGoodsSelectFragment.newInstance(intent.getStringExtra("mid"));
                if (intent.getStringExtra("Append") != null) {
                    goodsTypefragment.isAppend = intent.getStringExtra("Append");
                }
                replaceFragment(goodsTypefragment);
                break;
            case RANK_GOODS://排行榜
                replaceFragment(RankGoodsFragment.newInstance(intent.getStringExtra("gid")));
                break;
            case RECOMMENT_GOODS://热门推荐
                replaceFragment(RecommendGoodsFragment.newInstance(intent.getStringExtra("gid")));
                break;
            case GOODS_PAY://支付
                PayGoodsFragment payGoodsFragment;
                payGoodsFragment = PayGoodsFragment.newInstance(intent.getStringExtra("orderid"), intent.getStringExtra("integral"), intent.getStringExtra("money"), intent.getStringExtra("orderType"));
                replaceFragment(payGoodsFragment);
                break;
            case MY_ORDER://我的订单
                replaceFragment(MyOrderFragment.newInstance(intent.getBooleanExtra("isEvaluate", false)));
                break;
            case ONE_YUAN_PAY_MENT://一元购物车(到支付页面)
                List<OneGoodsModel> list = (List<OneGoodsModel>) intent.getSerializableExtra("models");
                PaymentFragment paymentFragment = new PaymentFragment();
                paymentFragment.model = list;
                replaceFragment(paymentFragment);
                break;
            case GOODS_EVALUATE://待评价商品列表
                String orderid = getIntent().getStringExtra("orderid");
                String TYPE = getIntent().getStringExtra("type") == null ? "mall" : getIntent().getStringExtra("type");
                if ("mall".equals(TYPE)) {
                    AllGoodsEvaluateFragment goodsEvaluateFragment = AllGoodsEvaluateFragment.newInstance(orderid, TYPE);
                    replaceFragment(goodsEvaluateFragment);
                } else {
                    GoodsEvaluateFragment goodsEvaluateFragment = GoodsEvaluateFragment.newInstance(orderid, TYPE);
                    replaceFragment(goodsEvaluateFragment);
                }
                break;
            case NEW_ANNOUNCED://最新揭晓详情
                NewAnnounce n = (NewAnnounce) getIntent().getSerializableExtra("newModel");
                replaceFragment(NewAnnouncedDetailsFragment.newInstance(n));
                break;
            case ORDER_DETAIL://订单详情
                OrderDetailFragment orderDetailFragment = OrderDetailFragment.newInstance(getIntent().getStringExtra("orderid"));
                replaceFragment(orderDetailFragment);
                break;
            case GOODS_PURCHASE://立即支付
                List<CartShop> cartShopList = (List<CartShop>) intent.getExtras().getSerializable("model");
                PromptlyGoodsFragment promptlyGoodsFragment = PromptlyGoodsFragment.newInstance(cartShopList);
                replaceFragment(promptlyGoodsFragment);
                break;
            case PAY_STATUS://支付成功
                PaymentResultFragment paymentResultFragment = PaymentResultFragment.newInstance(intent.getBooleanExtra("status", false), intent.getStringExtra("message"));
                replaceFragment(paymentResultFragment);
                break;
            case MY_COLLECTION://我的收藏
                replaceFragment(new UserShoppingCollectionFragment());
                break;
            case FLAGSHIP://旗舰店更多
                replaceFragment(new FlagshipFragmentMore());
                break;
            case SALESPACKAGE://优惠套餐
                Bundle bundle = intent.getExtras();
                replaceFragment(SalesPackageFragment.newInstance((Sales) bundle.getSerializable("sale")));
                break;
            case LEAGUEPREFER:
                replaceFragment(LeaguePreferFragment.newInstance(intent.getStringExtra("merchanid")));
                break;
            case INTEGRALDETAIL://积分商城支付界面
                List<CartGoods> cartGoodses = (List<CartGoods>) getIntent().getSerializableExtra("goodsList");
                boolean type = getIntent().getBooleanExtra("type", false);
                replaceFragment(IntegralPaymentFragment.newInstens(cartGoodses, type));
                break;
            case INTEGRALCAR://积分商城购物车
                replaceFragment(new IntegralCarFragment());
                break;
            case INTEGRALMYORDER://积分商城-我的订单
                replaceFragment(IntegralMyOrderFragment.newInstance(intent.getBooleanExtra("isEvaluate", false)));
                break;
            case INTEGRALORDERDETAIL://积分商城-订单详情
                replaceFragment(IntegralOrderDetailFragment.newInstance(getIntent().getStringExtra("orderid")));
                break;
            case MYHEAD://个人信息页
                replaceFragment(new UserInformationFragment());
                break;
            case MYBALANCEFRAGMENT://花币明细 积分明细 余额 提现
                replaceFragment(MyBalanceFragment.newInstance(getIntent().getIntExtra("type", 0)));
                break;
            case USERSHOPPINGCOLLECTIONFRAGMENT://我的收藏
                replaceFragment(new UserShoppingCollectionFragment());
                break;
            case MYRECORDFRAGMENT://我的记录 获得的奖品
                boolean booleen = getIntent().getBooleanExtra("type", false);
                MyRecordFragment myRecordFragment = new MyRecordFragment();
                if (booleen) {
                    myRecordFragment.setIsPrize(booleen);
                }
                replaceFragment(myRecordFragment);
                break;
            case THESUNFRAGMENT://我的晒单 所有晒单
                theSunFragment = new TheSunFragment();
                theSunFragment.Title = getIntent().getStringExtra("title");
                replaceFragment(theSunFragment);
                break;
            case USERADDRESSFRAGMENT://地址管理
                replaceFragment(new UserAddressFragment());
                break;
            case USERSETINGFRAGMENT://用户设置
                replaceFragment(new UserSetingFragment());
                break;
            case INTEGRALEVALUATELISTFRAGMENT://积分商城的评论列表
                replaceFragment(new IntegralEvaluateListFragment());
                break;
            case CONFIRMSHOPPINGCARTFRAGMENT:
                replaceFragment(ConfirmShoppingCartFragment.newInstance(getIntent().getStringExtra("gids")));
                break;
            case SEARCH_PAGE:
                String key = getIntent().getStringExtra("key");
                GoodsSelectFragment newFragment = new GoodsSelectFragment();
                bundle = new Bundle();
                bundle.putString("key", key);
                bundle.putString("isShop", "pre");
                newFragment.setArguments(bundle);
                replaceFragment(newFragment);
                break;
            case FLOWERCOINSRECHARGE:
                replaceFragment(RechargeFragment.newInstance(0));
                break;
            case NEWFLOWERCOINSRECHARGE:
                String money = getIntent().getExtras().getString("money");
                replaceFragment(NewPayGoodsFragment.newInstance(money));
                break;
        }
    }

    protected void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // ft.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        ft.replace(R.id.common_frame, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        Log.i("wan", "commonActivity finish");
        ImageLoader.getInstance().stop();
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }


}
