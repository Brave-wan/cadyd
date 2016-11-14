package com.cadyd.app.ui.fragment.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.pay.PayGoodsFragment;
import com.cadyd.app.ui.fragment.user.UserAddressFragment;
import com.cadyd.app.ui.window.DistributionTypePopupWindow;
import com.cadyd.app.ui.window.PreferUsePopupWindow;
import com.cadyd.app.utils.StringUtils;

import org.wcy.android.utils.LogUtil;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.MyListView;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 立即购买->查看订单
 * Created by wcy on 2016/5/25.
 */
public class PromptlyGoodsFragment extends BaseFragement {
    private String gids;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.my_list_view)
    MyListView list_view;
    @Bind(R.id.total_money)
    TextView total_money;
    List<CartShop> mlist;
    private String aid = "";
    List<CartShop> data;
    @Bind(R.id.integral_checkbox)
    CheckBox integral_checkbox;
    @Bind(R.id.integral)
    TextView integral;

    public static final int PROMPTLYGOODSFRAGMENT = 8080;

    public static PromptlyGoodsFragment newInstance(List<CartShop> data) {
        PromptlyGoodsFragment newFragment = new PromptlyGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", (Serializable) data);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mlist = new ArrayList<>();
        if (args != null) {
            data = (List<CartShop>) args.getSerializable("model");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_confirm_shopping_cart, "确认订单", true);//
    }

    @Override
    protected void initView() {
        integral.setVisibility(View.GONE);
        list_view.setVisibility(View.VISIBLE);
        addressLoading();
        loading();
    }

    @OnClick({R.id.top_layout, R.id.purchase})
    public void OnClicklinener(View view) {
        switch (view.getId()) {
            case R.id.top_layout://设置默认地址
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.ADDRESSFRAGMENT);
                startActivityForResult(intent, 0);
                break;
            case R.id.purchase:
                save();
                break;
        }
    }

    /**
     * zjh
     **/
    private void save() {
        if (!StringUtil.hasText(aid)) {
            toastError("请先选择送货地址");
            return;
        }
        List<AppShopVo> appShopVoList = new ArrayList<>();
        AppShopVo shopVo = new AppShopVo();
        Map<String, Object> map = new HashMap<>();
        List<AppGoodsVo> goodsVoList = new ArrayList<>();
        for (CartShop cs : mlist) {
            if (!StringUtil.hasText(cs.freightid)) {
                toastError("请先选择\n" + cs.shopname + "\n店铺的配送方式");
                return;
            }
            map.put("addressid", aid);//默认地址id
            map.put("useIntegral", integral_checkbox.isChecked());//是否使用积分
            shopVo.freightid = cs.freightid;//配送方式id
            shopVo.shopid = cs.shopid;   //店铺id
            shopVo.spid = cs.spId;//优惠券id
            shopVo.message = messageText.getText().toString();  // 买家留言
            for (Suit suit : cs.suitList) {
                AppGoodsVo goodsVo = new AppGoodsVo();
                goodsVo.cartid = suit.id;// 购物车ID
                goodsVo.rebateid = suit.rebateId;// 返利Id
                goodsVo.isReabte = suit.reabte;// 是否返利
                goodsVo.isTescode = suit.luckCode;// 是否使用乐购码
                goodsVo.isGoodsPrefer = suit.goodsPrefer;// 是否使用单品优惠券
                goodsVoList.add(goodsVo);
            }
        }
        shopVo.goodsList = goodsVoList;
        appShopVoList.add(shopVo);
        map.put("shopList", appShopVoList);
        ApiClient.send(activity, JConstant.ORDER_SAVE_, map, Order.class, new DataLoader<Order>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Order data) {
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_PAY);
                intent.putExtra("orderid", data.flowno);
                intent.putExtra("money", data.actual);
                intent.putExtra("orderType", InterFace.OrderType.MALL.ecode);
                startActivityForResult(intent, 1);
            }

            @Override
            public void error(String message) {
                toastError(message + "无法购买");
            }
        }, JConstant.ORDER_SAVE_);
    }

    //获取地址
    private void addressLoading() {
        ApiClient.send(activity, JConstant.ADDRESS_DEFAULT_, null, AddressModel.class, new DataLoader<AddressModel>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(AddressModel data) {
                aid = data.id;
                name.setText("收货人：" + data.consignee);
                phone.setText(data.telphone);
                address.setText("收货地址：" + data.getAddress());
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.ADDRESS_DEFAULT_);
    }

    int totalNum = 0;
    BigDecimal totalMoney = new BigDecimal(0);
    private EditText messageText;

    BigDecimal money = new BigDecimal(0);
    int num = 0;


    private void loading() {
        if (data != null) {
            mlist.addAll(data);
            CommonAdapter<CartShop> adapter = new CommonAdapter<CartShop>(activity, mlist, R.layout.confirm_shopping_cart_list_iteam) {
                @Override
                public void convert(final ViewHolder helper, final CartShop item) {
                    messageText = helper.getView(R.id.message);
                    helper.setText(R.id.shop_name, item.shopname);
                    MyListView lv = helper.getView(R.id.my_list_view);
                    lv.setVisibility(View.VISIBLE);
                    money = new BigDecimal(0);
                    num = 0;
                    num += item.suitList.get(0).num;
                    money = money.add(new BigDecimal(String.valueOf(item.suitList.get(0).price)).multiply(new BigDecimal(item.suitList.get(0).num)));
                    Log.i("xiongmao", "单价：" + item.suitList.get(0).price + "数量：" + item.suitList.get(0).num + "总价：" + money);
                    final TextView subtotal_tv = helper.getView(R.id.subtotal);
                    subtotal_tv.setText("共" + num + "件商品，合计：￥" + NumberUtil.getString(money, 2));
                    subtotal_tv.setTag(money);
                    final CommonAdapter<Suit> adapter2 = new CommonAdapter<Suit>(activity, item.suitList, R.layout.confirm_shopping_cart_list_iteam_list) {
                        @Override
                        public void convert(ViewHolder helper, final Suit item) {
                            final CartGoods cartGoods = item.goodsList.get(0);//结构转换
                            helper.setText(R.id.good_name, cartGoods.title);
                            helper.setText(R.id.salemix, cartGoods.salemix);
                            helper.setText(R.id.price, "￥" + item.price);
                            helper.setText(R.id.number, "X" + item.num);
                            ImageView goods_image = helper.getView(R.id.goods_image);
                            ApiClient.ImageLoadersRounde(cartGoods.thumb, goods_image);
                            /**能否使用乐购码*/
                            if (item.luckCode) {
                                helper.setText(R.id.luckCode_number, "是否使用乐购码");
                                RelativeLayout relativeLayout = helper.getView(R.id.luckCode_layout);
                                relativeLayout.setVisibility(View.VISIBLE);
                                final CheckBox checkBox = helper.getView(R.id.luckCode_checkbox);
                                checkBox.setText(item.maxCount + "个（共有" + item.luckCodeCount + "个）");
                                // checkBox.setChecked(item.luckCode);
                                checkBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO 选择使用乐购码
                                        item.luckCode = checkBox.isChecked();
                                        if (checkBox.isChecked()) {
                                            totalMoney = totalMoney.subtract(new BigDecimal(item.maxCount));
                                        } else {
                                            totalMoney = totalMoney.add(new BigDecimal(item.maxCount));
                                        }
                                        updateMoney();
                                        subtotal_tv.setText("共" + num + "件商品，合计：￥" + NumberUtil.getString(totalMoney, 2));
                                        subtotal_tv.setTag(totalMoney);
                                    }
                                });
                            }
                            /**能否使优惠券*/
                            RelativeLayout relativeLayout = helper.getView(R.id.goodsPrefer_layout);
                            if (item.goodsPrefer) {
                                relativeLayout.setVisibility(View.VISIBLE);
                                CheckBox checkBox = helper.getView(R.id.goodsPrefer_checkbox);
                                checkBox.setText("￥" + item.goodsPreferAmount);
                                checkBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CheckBox c = (CheckBox) v;
                                        item.goodsPrefer = c.isChecked();
                                        if (c.isChecked()) {
                                            totalMoney = totalMoney.subtract(new BigDecimal(item.goodsPreferAmount));
                                        } else {
                                            totalMoney = totalMoney.add(new BigDecimal(item.goodsPreferAmount));
                                        }
                                        updateMoney();
                                        subtotal_tv.setText("共" + num + "件商品，合计：￥" + NumberUtil.getString(totalMoney, 2));
                                        subtotal_tv.setTag(totalMoney);
                                    }
                                });
                            }
                        }
                    };
                    lv.setAdapter(adapter2);
                    if (item.freightList != null && item.freightList.size() > 0) {
                        final TextView distribution_type = helper.getView(R.id.distribution_type);
//                        if (item.freightList.size() == 1) {
//                            Freight f = item.freightList.get(0);
//                            distribution_type.setText(f.name + " " + NumberUtil.getString(f.extra, 2) + "元");
//                            distribution_type.setTag(f);
//                            mlist.get(helper.getPosition()).freightid = f.id;
//                        }
                        final int finalNum = num;
                        helper.getView(R.id.distribution_type).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Freight f = (Freight) distribution_type.getTag();
                                DistributionTypePopupWindow window = new DistributionTypePopupWindow(activity, item.freightList, f == null ? "" : f.id);
                                window.setOnCheckChangeListener(new DistributionTypePopupWindow.OnCheckChangeListener() {
                                    @Override
                                    public void onClickListener(Freight data) {
                                        //TODO 选择物流方式
                                        mlist.get(helper.getPosition()).freightid = data.id;
                                        Freight f2 = (Freight) distribution_type.getTag();
                                        BigDecimal extra = f2 != null && f2.extra != null ? NumberUtil.getBigDecimal(f2.extra) : new BigDecimal(0);
                                        BigDecimal finalMoney = (BigDecimal) subtotal_tv.getTag();
                                        distribution_type.setText(data.name + " " + NumberUtil.getString(data.extra, 2) + "元");//显示选择的物流方式

                                        totalMoney = totalMoney.subtract(extra);
                                        BigDecimal am = finalMoney.subtract(extra);
                                        am = am.add(NumberUtil.getBigDecimal(data.extra));
                                        totalMoney = totalMoney.add(NumberUtil.getBigDecimal(data.extra));
                                        subtotal_tv.setText("共" + finalNum + "件商品，合计：￥" + NumberUtil.getString(am, 2));
                                        subtotal_tv.setTag(am);

                                        distribution_type.setTag(data);
                                        updateMoney();
                                    }
                                });
                                window.show(activity);
                            }
                        });
                        final TextView prefer_tv = helper.getView(R.id.prefer_choos);
                        final BigDecimal finalMoney = money;
                        prefer_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (item.preferList != null && item.preferList.size() > 0) {
                                    final Prefer f = (Prefer) prefer_tv.getTag();
                                    PreferUsePopupWindow window = new PreferUsePopupWindow(activity, item.preferList, f == null ? "" : f.id, finalMoney);
                                    window.setOnCheckChangeListener(new PreferUsePopupWindow.OnCheckChangeListener() {
                                        @Override
                                        public void onClickListener(Prefer data, boolean isChecked) {
                                            if (data == null) {
                                                return;
                                            }
                                            mlist.get(helper.getPosition()).spId = data.spreferId;
                                            Prefer f2 = (Prefer) prefer_tv.getTag();
                                            BigDecimal money = f2 != null && f2.money != null ? f2.money : new BigDecimal(0);
                                            BigDecimal finalMoney = (BigDecimal) subtotal_tv.getTag();
                                            BigDecimal am = finalMoney.add(money);
                                            totalMoney = totalMoney.add(money);
                                            Log.i("woshixiongmaobieda", "被减数：" + am.toString() + "减数：" + data.money.toString());
                                            if (data != null && isChecked) {
                                                am = am.subtract(data.money);
                                                Log.i("woshixiongmaobieda", "进入判断了" + "获得了新的总价：" + am.toString());
                                                totalMoney = totalMoney.subtract(data.money);

                                                prefer_tv.setText(data.des + data.money + "元");//显示选择的优惠券
                                                prefer_tv.setTag(data);
                                            } else {
                                                prefer_tv.setText("");
                                                prefer_tv.setTag(null);
                                            }
                                            updateMoney();
                                            subtotal_tv.setText("共" + finalNum + "件商品，合计：￥" + NumberUtil.getString(am, 2));
                                            subtotal_tv.setTag(am);
                                        }
                                    });
                                    window.show(activity);
                                } else {
                                    toastWarning("暂时没有可用的优惠卷");
                                }

                            }
                        });

                    }

                }
            };
            list_view.setAdapter(adapter);
            for (CartShop cs : mlist) {
                int num = 0;
                BigDecimal money = new BigDecimal(0);
                for (Suit cg : cs.suitList) {
                    num += cg.num;
                    money = money.add(new BigDecimal(String.valueOf(cg.price)).multiply(new BigDecimal(cg.num)));
                }
                totalNum += num;
                totalMoney = totalMoney.add(money);
            }
            updateMoney();
        }
    }

    private void updateMoney() {
        total_money.setText("共" + totalNum + "件商品，合计：￥" + NumberUtil.getString(totalMoney, 2));
    }

    class OderList {
        /**
         * shopid :
         * spId :
         * freightid :
         */
        public String shopid;
        public String spId;
        public String freightid;
        public List<DetailList> detailList;
    }

    class DetailList {

        /**
         * salemix : 颜色:乳白色,尺寸:M
         * goodsid : 6fc271d477d8451490a8a516a29adf9c
         * goodstitle : t1
         * number : 1
         * userid :
         * cartid : 00902487bac446e38349056fea79948e
         * rid : e23ec8b1e56e441e984028e163c46d5e
         * spid :
         */
        public String salemix;
        public String goodsid;
        public String goodstitle;
        public String number;
        public String userid;
        public String cartid;
        public String rid;
        public String spid;
    }

    public class Order {
        public String flowno;////业务流水号
        public String actual;//实际支付金额
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == UserAddressFragment.ADDRESSRESULT && requestCode == 0) {
            addressLoading();
        } else if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            activity.setResult(PROMPTLYGOODSFRAGMENT);
            finishActivity();
        } else if (resultCode == Activity.RESULT_CANCELED && requestCode == 1) {
            finishActivity();
        }
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.ORDER_SAVE_);
        ApiClient.cancelRequest(JConstant.ADDRESS_DEFAULT_);
    }
}
