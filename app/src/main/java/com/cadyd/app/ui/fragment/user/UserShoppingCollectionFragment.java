package com.cadyd.app.ui.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.UserShoppingCollectionAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.CommaSplice;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.ShoppingCollectionData;
import com.cadyd.app.model.ShoppingCollectionModle;
import com.cadyd.app.model.ShoppingCollectionShopData;
import com.cadyd.app.model.ShoppingCollectionShopModle;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.ToastView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 */
public class UserShoppingCollectionFragment extends BaseFragement {

    private int TYPE = 0;
    private boolean BabyIsCheckAll = false;
    private boolean ShopIsCheckAll = false;

    private boolean BabyIsCheck = false;
    private boolean ShopIsCheck = false;

    private int babyIndex = 1;
    private int shopIndex = 1;

    private AlertConfirmation alertConfirmation;

    @Bind(R.id.user_shopping_collection_group)
    RadioGroup group;
    @Bind(R.id.user_shopping_collection_baby)
    RadioButton baby;
    @Bind(R.id.user_shopping_collection_shop)
    RadioButton shop;

    @Bind(R.id.check_layout)
    LinearLayout checkLayout;
    @Bind(R.id.user_shopping_collection_check_all)
    Button checkAll;
    @Bind(R.id.user_shopping_collection_delete)
    Button delete;

    private UserShoppingCollectionAdapter adapter;
    private UserShoppingCollectionAdapter shopAdapter;

    @Bind(R.id.user_shopping_collection_list)
    PullToRefreshListView listView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_user_shopping_collection, "我的收藏", true, "删除");
    }

    @Override
    protected void initView() {
        alertConfirmation = new AlertConfirmation(activity, "删除", "是否确认删除？");
        checkAll.setTag(false);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                switch (TYPE) {
                    case 0:
                        babyIndex = 1;
                        collectionDatas.clear();
                        initBabyHttp();
                        break;
                    case 1:
                        shopIndex = 1;
                        collectionShopDatas.clear();
                        initShopHttp();
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                switch (TYPE) {
                    case 0:
                        babyIndex++;
                        initBabyHttp();
                        break;
                    case 1:
                        initShopHttp();
                        break;
                }
            }
        });
        //进入编辑状态
        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (TYPE) {
                    case 0:
                        if (adapter.isCheckShow()) {
                            //动画
                            startAnimation(checkLayout, listView, false);
                        } else {
                            //动画
                            startAnimation(checkLayout, listView, true);
                        }
                        break;
                    case 1:
                        if (shopAdapter.isCheckShow()) {
                            //动画
                            startAnimation(checkLayout, listView, false);
                        } else {
                            //动画
                            startAnimation(checkLayout, listView, true);
                        }
                        break;
                }
            }
        });
        //切换
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.user_shopping_collection_baby:
                        TYPE = 0;
                        if (BabyIsCheckAll) {
                            checkAll.setText("全不选");
                        } else {
                            checkAll.setText("全选");
                        }
                        if (BabyIsCheck) {
                            checkLayout.setVisibility(View.VISIBLE);
                            layout.rightButton.setText("完成");
                        } else {
                            checkLayout.setVisibility(View.GONE);
                            layout.rightButton.setText("删除");
                        }
                        //如果没有商品就隐藏删除功能
                        if (collectionDatas.size() <= 0) {
                            layout.rightButton.setVisibility(View.GONE);
                        } else {
                            layout.rightButton.setVisibility(View.VISIBLE);
                        }

                        if (adapter == null) {
                            initBabyHttp();
                        } else {
                            listView.setAdapter(adapter);
                        }
                        break;
                    case R.id.user_shopping_collection_shop:
                        TYPE = 1;
                        if (ShopIsCheckAll) {
                            checkAll.setText("全不选");
                        } else {
                            checkAll.setText("全选");
                        }
                        if (ShopIsCheck) {
                            checkLayout.setVisibility(View.VISIBLE);
                            layout.rightButton.setText("完成");
                        } else {
                            checkLayout.setVisibility(View.GONE);
                            layout.rightButton.setText("删除");
                        }

                        if (collectionShopDatas.size() <= 0) {
                            layout.rightButton.setVisibility(View.GONE);
                        } else {
                            layout.rightButton.setVisibility(View.VISIBLE);
                        }

                        if (shopAdapter == null) {
                            initShopHttp();
                        } else {
                            listView.setAdapter(shopAdapter);
                        }
                        break;
                }
            }
        });
        baby.setChecked(true);
    }

    /****/

    private void startAnimation(View checkLayout, View listView, boolean isStart) {
        /**
         * 应对显示控件时会挤占位置导致重新布局造成突兀感（显示动画）
         * */
        if (isStart) {
            Animation mShowAction = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(500);
            checkLayout.startAnimation(mShowAction);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(500);
            listView.startAnimation(animation);

            checkLayout.setVisibility(View.VISIBLE);
            switch (TYPE) {
                case 0:
                    BabyIsCheck = true;
                    adapter.isCheckShow(true);
                    break;
                case 1:
                    ShopIsCheck = true;
                    shopAdapter.isCheckShow(true);
                    break;
            }
            layout.rightButton.setText("完成");
        } else {
            Animation mShowAction = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);
            mShowAction.setDuration(500);
            checkLayout.startAnimation(mShowAction);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(500);
            listView.startAnimation(animation);

            checkLayout.setVisibility(View.GONE);
            switch (TYPE) {
                case 0:
                    BabyIsCheck = false;
                    adapter.isCheckShow(false);
                    break;
                case 1:
                    ShopIsCheck = false;
                    shopAdapter.isCheckShow(false);
                    break;
            }

            layout.rightButton.setText("删除");
        }

    }

    private List<ShoppingCollectionData> collectionDatas = new ArrayList<>();
    private List<ShoppingCollectionShopData> collectionShopDatas = new ArrayList<>();

    //查询收藏的商品
    private void initBabyHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", babyIndex);
        map.put("type", 0);
        ApiClient.send(activity, JConstant.SHOPCOLLECT_, map, ShoppingCollectionModle.class, new DataLoader<ShoppingCollectionModle>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(ShoppingCollectionModle data) {
                listView.onRefreshComplete();
                collectionDatas.addAll(data.data);
                if (collectionDatas.size() <= 0) {
                    layout.rightButton.setVisibility(View.GONE);
                } else {
                    layout.rightButton.setVisibility(View.VISIBLE);
                }
                if (adapter == null) {
                    adapter = new UserShoppingCollectionAdapter(activity, collectionDatas);
                    adapter.setClick(new TowObjectParameterInterface() {
                        @Override
                        public void Onchange(int type, int postion, Object object) {
                            switch (type) {
                                case 0://勾选删除
                                    collectionDatas.get(postion).checked = (boolean) object;
                                    if ((boolean) object) {
                                        int num = 0;
                                        for (int i = 0; i < collectionDatas.size(); i++) {
                                            if (collectionDatas.get(i).checked) {
                                                num++;
                                            }
                                        }
                                        if (num == collectionDatas.size()) {
                                            checkAll.setText("全不选");
                                            BabyIsCheckAll = true;
                                        }
                                    } else {
                                        checkAll.setText("全选");
                                        BabyIsCheckAll = false;
                                    }
                                    break;
                            }
                        }
                    });
                    listView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String message) {
                listView.onRefreshComplete();
            }
        }, JConstant.SHOPCOLLECT_);
    }

    //查询收藏的店铺
    private void initShopHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", shopIndex);
        map.put("type", 1);
        ApiClient.send(activity, JConstant.SHOPCOLLECT_, map, ShoppingCollectionShopModle.class, new DataLoader<ShoppingCollectionShopModle>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(ShoppingCollectionShopModle data) {
                listView.onRefreshComplete();
                if (data.data != null) {
                    collectionShopDatas.addAll(data.data);
                }

                if (collectionShopDatas.size() <= 0) {
                    layout.rightButton.setVisibility(View.GONE);
                } else {
                    layout.rightButton.setVisibility(View.VISIBLE);
                }
                if (shopAdapter == null) {
                    shopAdapter = new UserShoppingCollectionAdapter(activity, collectionShopDatas, "");
                    shopAdapter.setClick(new TowObjectParameterInterface() {
                        @Override
                        public void Onchange(int type, int postion, Object object) {
                            switch (type) {
                                case 0://勾选删除
                                    collectionShopDatas.get(postion).checked = (boolean) object;
                                    if ((boolean) object) {
                                        int num = 0;
                                        for (int i = 0; i < collectionShopDatas.size(); i++) {
                                            if (collectionShopDatas.get(i).checked) {
                                                num++;
                                            }
                                        }
                                        if (num == collectionShopDatas.size()) {
                                            checkAll.setText("全不选");
                                            BabyIsCheckAll = true;
                                        }
                                    } else {
                                        checkAll.setText("全选");
                                        BabyIsCheckAll = false;
                                    }
                                    shopAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    });
                    listView.setAdapter(shopAdapter);
                } else {
                    shopAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String message) {
                listView.onRefreshComplete();
            }
        }, JConstant.SHOPCOLLECT_);

    }

    //取消收藏
    private void initDeleteHttp(String str) {
        int url = 0;
        Map<String, Object> map = new HashMap<>();
//        {goodsId}:商品id,多个以,号隔开
//        {shopId}:店铺id,多个以,号分割
        switch (TYPE) {
            case 0://商品
                map = new HashMap<>();
                map.put("goodsId", str);
                url = JConstant.CANCEL_COLLECT_;
                break;
            case 1://店铺
                map = new HashMap<>();
                map.put("shopId", str);
                url = JConstant.CANCEL_COLLECT_;
                break;
        }
        ApiClient.send(activity, url, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("取消成功");
                switch (TYPE) {
                    case 0:
                        //当取消之后退出编辑状态
                        BabyIsCheck = false;
                        BabyIsCheckAll = false;
                        adapter.isCheckShow(false);
                        layout.rightButton.setText("删除");
                        checkLayout.setVisibility(View.GONE);

                        collectionDatas.clear();
                        babyIndex = 1;
                        initBabyHttp();
                        break;
                    case 1:
                        //当取消之后退出编辑状态
                        ShopIsCheck = false;
                        ShopIsCheckAll = false;
                        shopAdapter.isCheckShow(false);
                        layout.rightButton.setText("删除");
                        checkLayout.setVisibility(View.GONE);

                        collectionShopDatas.clear();
                        shopIndex = 1;
                        initShopHttp();
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void error(String message) {

            }
        }, url);
    }

    //全选
    @OnClick(R.id.user_shopping_collection_check_all)
    public void onCheckAll(View view) {
        switch (TYPE) {
            case 0:
                if (BabyIsCheckAll) {
                    for (int i = 0; i < collectionDatas.size(); i++) {
                        collectionDatas.get(i).checked = false;
                    }
                    checkAll.setText("全选");
                    BabyIsCheckAll = false;
                } else {
                    for (int i = 0; i < collectionDatas.size(); i++) {
                        collectionDatas.get(i).checked = true;
                    }
                    checkAll.setText("全不选");
                    BabyIsCheckAll = true;
                }
                adapter.notifyDataSetChanged();
                break;
            case 1:
                if (ShopIsCheckAll) {
                    for (int i = 0; i < collectionShopDatas.size(); i++) {
                        collectionShopDatas.get(i).checked = false;
                    }
                    checkAll.setText("全选");
                    ShopIsCheckAll = false;
                } else {
                    for (int i = 0; i < collectionShopDatas.size(); i++) {
                        collectionShopDatas.get(i).checked = true;
                    }
                    checkAll.setText("全不选");
                    ShopIsCheckAll = true;
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private List<String> strings = new ArrayList<>();

    //删除
    @OnClick(R.id.user_shopping_collection_delete)
    public void onDelete(View view) {
        switch (TYPE) {
            case 0://店铺删除
                strings.clear();
                for (int i = 0; i < collectionDatas.size(); i++) {
                    if (collectionDatas.get(i).checked) {
                        strings.add(collectionDatas.get(i).goodsId);
                    }
                }
                if (strings.size() <= 0) {
                    ToastView.show(activity, "请选择要删除的商品");
                    return;
                }
                alertConfirmation.show(new AlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener() {
                        initDeleteHttp(CommaSplice.Splice(strings).toString());
                        alertConfirmation.hide();
                    }

                    @Override
                    public void CancelOnClickListener() {
                        alertConfirmation.hide();
                    }
                });
                break;
            case 1://商品删除
                strings.clear();
                for (int i = 0; i < collectionShopDatas.size(); i++) {
                    if (collectionShopDatas.get(i).checked) {
                        strings.add(collectionShopDatas.get(i).shopId);
                    }
                }
                if (strings.size() <= 0) {
                    ToastView.show(activity, "请选择要删除的商品");
                    return;
                }
                alertConfirmation.show(new AlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener() {
                        initDeleteHttp(CommaSplice.Splice(strings).toString());
                        alertConfirmation.hide();
                    }

                    @Override
                    public void CancelOnClickListener() {
                        alertConfirmation.hide();
                    }
                });
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.SHOPCOLLECT_);
        ApiClient.cancelRequest(JConstant.CANCEL_COLLECT_);
    }
}
