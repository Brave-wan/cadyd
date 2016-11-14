package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.AppManager;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.model.CartGoods;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.Integralmall.IntergralDetailTopFragment;
import com.cadyd.app.ui.fragment.mall.GoodsDetailBottomFragment;
import com.cadyd.app.ui.view.DragLayout;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.widget.OnEmptyFoundClick;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegralGoodsDetailActivity extends BaseFragmentActivity {

    private int alphaMax = 140;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.shopping_cart)
    ImageView cart;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_layout)
    RelativeLayout title_bg;
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.draglayout)
    DragLayout draglayout;

    private IntergralDetailTopFragment topFragment;
    private GoodsDetailBottomFragment bottomFragment;

    private String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_goods_detail);
        gid = getIntent().getStringExtra("gid");//获得id
        initView();
    }

    private void initView() {
        if (StringUtil.hasText(gid)) {

            title.setAlpha(1 - alphaMax / 100);
            title_bg.getBackground().setAlpha(0);
            back.getBackground().setAlpha(alphaMax);
            cart.getBackground().setAlpha(alphaMax);
            more.getBackground().setAlpha(alphaMax);

            topFragment = IntergralDetailTopFragment.newInstance(gid);
            bottomFragment = GoodsDetailBottomFragment.newInstance(gid);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.first, topFragment)
                    .add(R.id.second, bottomFragment)
                    .commit();
            DragLayout.ShowNextPageNotifier nextIntf = new DragLayout.ShowNextPageNotifier() {
                @Override
                public void onDragNext() {
                    bottomFragment.showView();
                    bottomFragment.noChange();
                }
            };
            draglayout.setNextPageListener(nextIntf);
        }

    }

    public void onBgTitle(int scrollY) {
        MyChange.ChangeHandAlpha(title_bg, back, title, scrollY);
    }

    private void addCar() {
        if (topFragment != null && topFragment.goodDetail != null && topFragment.goodDetail.mallGoods != null && topFragment.goodDetail.mallGoods.id != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("goodsId", topFragment.goodDetail.mallGoods.id);
            map.put("num", topFragment.number);
            ApiClient.send(activity, JConstant.SAVEINTEGRAL, map, true, null, new DataLoader<String>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(String data) {
                    toastSuccess("加入成功");
                }

                @Override
                public void error(String message) {

                }
            }, JConstant.SAVEINTEGRAL);
        }
    }


    @OnClick({R.id.call_phone, R.id.buy_goods, R.id.back, R.id.add_car})
    public void onCallPhone(View view) {
        switch (view.getId()) {
            case R.id.call_phone: //联系卖家
                Utils.tellPhone(activity, JConstant.customerServicePhone, "联系客服");
                break;
            case R.id.buy_goods://立即购买
                if (topFragment == null || topFragment.goodDetail == null) {
                    return;
                }
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INTEGRALDETAIL);
                intent.putExtra("type", false);
                List<CartGoods> cartGoodses = new ArrayList<>();
                CartGoods cartGoods = new CartGoods();

                if (topFragment.goodDetail.picList.size() > 0) {
                    cartGoods.thumb = topFragment.goodDetail.picList.get(0).thumb;
                } else {
                    cartGoods.thumb = topFragment.goodDetail.mallGoods.thumb;
                }
                cartGoods.title = topFragment.goodDetail.mallGoods.title;
                cartGoods.goodsId = topFragment.goodDetail.mallGoods.id;
                cartGoods.integral = topFragment.goodDetail.mallGoods.integral;
                cartGoods.price = topFragment.goodDetail.mallGoods.price.toString();
                cartGoods.number = String.valueOf(topFragment.number);
                cartGoodses.add(cartGoods);
                intent.putExtra("goodsList", (Serializable) cartGoodses);
                startActivity(intent);
                break;
            case R.id.back://返回
                finishActivity();
                break;
            case R.id.add_car://加入购物车
                addCar();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        ApiClient.cancelRequest(JConstant.SAVEINTEGRAL);
    }

}
