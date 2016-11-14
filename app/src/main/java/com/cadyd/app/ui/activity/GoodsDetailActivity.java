package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.AppManager;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyShare;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.model.CartShop;
import com.cadyd.app.model.GoodDetail;
import com.cadyd.app.model.SaleInfo;
import com.cadyd.app.model.Saledynamic;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.cart.PromptlyGoodsFragment;
import com.cadyd.app.ui.fragment.mall.GoodsDetailBottomFragment;
import com.cadyd.app.ui.fragment.mall.GoodsDetailTopFragment;
import com.cadyd.app.ui.view.DragLayout;
import com.cadyd.app.ui.view.DragLayout.ShowNextPageNotifier;
import com.cadyd.app.ui.window.ActionItem;
import com.cadyd.app.ui.window.BabyPopWindow;
import com.cadyd.app.ui.window.TitlePopup;

import org.wcy.android.widget.EmptyLayout;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品详情
 * Created by wcy on 16/5/15.
 */
public class GoodsDetailActivity extends BaseFragmentActivity {
    @Bind(R.id.draglayout)
    DragLayout draglayout;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.shopping_cart)
    ImageView cart;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_layout)
    RelativeLayout title_bg;
    @Bind(R.id.add_shopping_card)
    Button add_shopping_card;
    @Bind(R.id.purchase)
    Button purchase;
    @Bind(R.id.collect_btn)
    TextView collect_btn;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    GoodsDetailTopFragment topFragment;
    GoodsDetailBottomFragment bottomFragment;
    private int alphaMax = 140;
    private String gid;
    GoodDetail goodsinfo = null;
    EmptyLayout mEmptyLayout;
    /**
     * 弹出商品订单信息详情
     */
    private BabyPopWindow popWindow;
    //定义标题栏弹窗按钮
    private TitlePopup titlePopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        initView();
    }

    private void initView() {
        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                GoodsDetailTopFragment topFragment = (GoodsDetailTopFragment) getVisibleFragment();
                topFragment.reload();
            }
        });
        gid = getIntent().getStringExtra("gid");
        if (StringUtil.hasText(gid)) {
            title_bg.getBackground().setAlpha(0);
            back.getBackground().setAlpha(alphaMax);
            cart.getBackground().setAlpha(alphaMax);
            more.getBackground().setAlpha(alphaMax);
            topFragment = GoodsDetailTopFragment.newInstance(gid, "商城");
            bottomFragment = GoodsDetailBottomFragment.newInstance(gid);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.first, topFragment).add(R.id.second, bottomFragment)
                    .commit();
            ShowNextPageNotifier nextIntf = new ShowNextPageNotifier() {
                @Override
                public void onDragNext() {
                    bottomFragment.showView();
                }
            };
            draglayout.setNextPageListener(nextIntf);
        } else {
            mEmptyLayout.showEmpty("数据异常，请重新加载....");
        }
//实例化标题栏弹窗
        titlePopup = new TitlePopup(this, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(this, "首页", R.mipmap.goods_detail_home_popup));
        titlePopup.addAction(new ActionItem(this, "搜索", R.mipmap.search_ico));
        titlePopup.addAction(new ActionItem(this, "分享", R.mipmap.goods_detail_share_popup));
        titlePopup.addAction(new ActionItem(this, "我的收藏", R.mipmap.goods_detail_collection_popup));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0:
                        AppManager.getAppManager().finishAllActivity(MainActivity.class);
                        break;
                    case 1:
                        Intent intent = new Intent(activity, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_SEARCH);
                        startActivity(intent);
                        break;
                    case 2:
                        if (goodsinfo != null) {
                            MyShare.showShare(getApplicationContext(), goodsinfo.goods.title, "第一点", "http://home.cadyd.com/cadyd_user.apk", goodsinfo.goods.thumb);
                        }

                        break;
                    case 3:
                        intent = new Intent(activity, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MY_COLLECTION);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        ApiClient.cancelRequest(JConstant.SAVE_CART_);
        ApiClient.cancelRequest(JConstant.COLLECT_);
        ApiClient.cancelRequest(JConstant.CANCEL_COLLECT_);
        ApiClient.cancelRequest(JConstant.PURCHASE_);
        ApiClient.cancelRequest(JConstant.SALEDYNAMIC_);
    }

    public void onBgTitle(int scrollY) {
        if (scrollY > 200 && scrollY <= 510) {
            title_bg.getBackground().setAlpha(scrollY / 2);
        } else if (scrollY <= 100) {
            title_bg.getBackground().setAlpha(0);
        } else if (scrollY > 510) {
            title_bg.getBackground().setAlpha(255);
        }

        if (scrollY > 200 && scrollY <= 510) {
            int alphaReverse = alphaMax - (scrollY - 200);
            if (alphaReverse < 0) {
                alphaReverse = 0;
            }
            back.getBackground().setAlpha(alphaReverse);
            cart.getBackground().setAlpha(alphaReverse);
            more.getBackground().setAlpha(alphaReverse);

            back.setImageResource(R.mipmap.black_back);
            cart.setImageResource(R.mipmap.shoppingcar_image_ico_gray);
            more.setImageResource(R.mipmap.gray_mall);

        } else if (scrollY <= 200) {
            back.getBackground().setAlpha(alphaMax);
            cart.getBackground().setAlpha(alphaMax);
            more.getBackground().setAlpha(alphaMax);

            back.setImageResource(R.mipmap.back_left_ico);
            cart.setImageResource(R.mipmap.shoppingcar_image_ico_writh);
            more.setImageResource(R.mipmap.goods_detail_more);

        } else if (scrollY > 510) {
            back.getBackground().setAlpha(0);
            cart.getBackground().setAlpha(0);
            more.getBackground().setAlpha(0);
        }
    }

    public void showPopupWindow() {
        if (goodsinfo != null) {
            popWindow.isPurchase = false;
            showPopupWindow(true);
        }
    }

    public void showPopupWindow(boolean isbuy) {
        if (goodsinfo != null) {
            if (popWindow == null) {
                popWindow = new BabyPopWindow(GoodsDetailActivity.this, goodsinfo);
                popWindow.setMlist(saleInfoList);
            }
            popWindow.show(GoodsDetailActivity.this, isbuy);
        }
    }

    @OnClick({R.id.add_shopping_card, R.id.purchase, R.id.back, R.id.collect_btn, R.id.shopping_cart, R.id.more, R.id.service, R.id.shop_btn})
    public void OnclickListener(View view) {
        switch (view.getId()) {
            case R.id.add_shopping_card:// 加入购物车
                if (saleInfoList == null || saleInfoList.size() <= 0) {
                    addCard();
                } else {
                    popWindow.isPurchase = false;
                    showPopupWindow(false);
                }
                break;
            case R.id.purchase:// 立即购买
                if (saleInfoList == null || saleInfoList.size() <= 0) {
                    addPurchase();
                } else {
                    popWindow.isPurchase = true;
                    showPopupWindow(false);
                }
                break;
            case R.id.back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.collect_btn:
                if (goodsinfo != null && goodsinfo.goods != null && StringUtil.hasText(goodsinfo.goods.collid) && !goodsinfo.goods.collid.equals("-1")) {
                    CancelCollect();
                } else {
                    Collect();
                }
                break;
            case R.id.shopping_cart:
                if (application.isLogin()) {
                    Intent intent = new Intent(activity, CommonActivity.class);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INDEX_SHOPPING_CART);
                    startActivity(intent);
                } else {
                    startActivity(SignInActivity.class, false);
                }
                break;
            case R.id.more:
                titlePopup.show(view);
                break;
            case R.id.service://联系客服
                Utils.tellPhone(activity, topFragment.goodDetail.goods.minorphone, "联系客服");
                break;
            case R.id.shop_btn:
                if (topFragment != null) {
                    topFragment.goToShop();
                }
                break;
        }
    }

    List<SaleInfo> saleInfoList;

    public void setGoods(GoodDetail goods, String msg, List<SaleInfo> slist) {
        if (goods != null) {
            bottomFragment.setPackages(goods.goods.packages);
            if (slist != null) {
                saleInfoList = slist;
            } else {
                saleInfoList = new ArrayList<>();
            }
            goodsinfo = goods;
            popWindow = new BabyPopWindow(this, goodsinfo);
            popWindow.setMlist(saleInfoList);
            if (StringUtil.hasText(goodsinfo.goods.collid) && !goodsinfo.goods.collid.equals("-1")) {
                collect_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.collect_sel, 0, 0);
            } else {
                collect_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.collect_nor, 0, 0);
            }
            mEmptyLayout.showView();
        } else {
//            layout.title_relative.setVisibility(View.VISIBLE);
            mEmptyLayout.showEmpty(msg);
        }

    }

    /**
     * 1 更新按钮  2确定  3加入购物车  4立即购买
     *
     * @param type
     */
    public void Replace(int type) {
        switch (type) {
            case 1:
                Saledynamic saledynamic = popWindow.getSaledynamic();
                if (saledynamic != null) {
                    if (saledynamic.number <= 0) {
                        add_shopping_card.setBackgroundResource(R.color.btn_disable);
                        purchase.setBackgroundResource(R.color.btn_disable);
                    } else {
                        add_shopping_card.setBackgroundResource(R.drawable.add_card_button_selector);
                        purchase.setBackgroundResource(R.drawable.purchase_button_selector);
                    }
                } else {
                    add_shopping_card.setBackgroundResource(R.color.btn_disable);
                    purchase.setBackgroundResource(R.color.btn_disable);
                }
                GoodsDetailTopFragment topFragment = (GoodsDetailTopFragment) getVisibleFragment();
                topFragment.replace(saledynamic);
                break;
        }
    }


    /**
     * 加入购物车
     */
    public void addCard() {
        if (popWindow != null && goodsinfo != null && goodsinfo.goods != null && goodsinfo.userId != null && goodsinfo.goods.id != null) {
            popWindow.hide();
            if (application.isLogin()) {
                Map<String, Object> map = new HashMap<>();
                if (popWindow != null && popWindow.getSaledynamic() != null) {
                    map.put("rid", popWindow.getSaledynamic().id);
                    map.put("num", popWindow.Number());
                } else {
                    map.put("rid", "");
                    map.put("num", 1);
                }
                map.put("distrid", goodsinfo.userId);
                map.put("goodsid", goodsinfo.goods.id);
                int tag = JConstant.SAVE_CART_;
                ApiClient.send(getApplicationContext(), JConstant.SAVE_CART_, map, true, null, new DataLoader<Object>() {
                    @Override
                    public void task(String data) {

                    }

                    @Override
                    public void succeed(Object data) {
                        toastSuccess("加入购物车成功");
                    }

                    @Override
                    public void error(String message) {

                    }
                }, tag);
            } else {
                startActivity(SignInActivity.class, false);
            }
        }
    }

    /**
     * 收藏
     */
    private void Collect() {
        if (application.isLogin() && goodsinfo != null && goodsinfo.goods != null && goodsinfo.goods.id != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("goodsid", goodsinfo.goods.id);
            int tag = JConstant.COLLECT_;
            ApiClient.send(getApplicationContext(), JConstant.COLLECT_, map, true, null, new DataLoader<Object>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(Object data) {
                    goodsinfo.goods.collid = "1";
                    collect_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.collect_sel, 0, 0);
                    toastSuccess("收藏成功");
                }

                @Override
                public void error(String message) {
                    toastError("收藏失败");
                }
            }, tag);
        } else {
            startActivity(SignInActivity.class, false);
        }

    }

    /**
     * 取消收藏
     */
    public void CancelCollect() {
        if (application.isLogin() && goodsinfo != null && goodsinfo.goods != null && goodsinfo.goods.id != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("goodsId", goodsinfo.goods.id);
            int tag = JConstant.CANCEL_COLLECT_;
            ApiClient.send(getApplicationContext(), JConstant.CANCEL_COLLECT_, map, true, null, new DataLoader<Object>() {

                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(Object data) {
                    goodsinfo.goods.collid = "";
                    collect_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.collect_nor, 0, 0);
                    toastSuccess("取消收藏成功");
                }

                @Override
                public void error(String message) {
                }
            }, tag);
        } else {
            startActivity(SignInActivity.class, false);
        }
    }

    /**
     * 立即购买
     */
    public void addPurchase() {
        if (popWindow != null) {
            popWindow.hide();
        }
        if (popWindow.getSaledynamic() == null && goodsinfo == null && goodsinfo.userId == null && goodsinfo.goods == null && goodsinfo.goods.id == null) {
            return;
        }
        if (application.isLogin()) {
            Map<String, Object> map = new HashMap<>();
            if (popWindow != null && popWindow.getSaledynamic() != null) {
                map.put("rid", popWindow.getSaledynamic().id);//销售属性的行号id
                map.put("num", popWindow.Number());
            } else {
                map.put("rid", "");
                map.put("num", popWindow.Number());//商品的数量
            }
            map.put("distrid", goodsinfo.userId);// 分销人的id
            map.put("goodsid", goodsinfo.goods.id);//商品的id
            int tag = JConstant.PURCHASE_;
            ApiClient.send(this, JConstant.PURCHASE_, map, true, CartShop.getType(), new DataLoader<List<CartShop>>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(List<CartShop> data) {
                    if (data != null) {
                        Intent intent = new Intent(activity, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_PURCHASE);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model", (Serializable) data);//传递的是模型哦
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 2);
                    } else {
                        toastError("数据异常");
                    }
                }

                @Override
                public void error(String message) {

                }
            }, tag);
        } else {
            startActivity(SignInActivity.class, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == PromptlyGoodsFragment.PROMPTLYGOODSFRAGMENT) {
            Intent intent = new Intent(activity, CommonActivity.class);
            intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MY_ORDER);
            intent.putExtra("isEvaluate", false);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

}
