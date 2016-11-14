package com.cadyd.app.ui.window;

import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.GoodDetail;
import com.cadyd.app.model.SaleInfo;
import com.cadyd.app.model.Saledynamic;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.view.AddAndSubView;
import com.cadyd.app.ui.view.SelectFixGridLayout;

import java.util.*;


/**
 * 宝贝详情界面的弹窗
 *
 * @author wcy
 */
public class BabyPopWindow extends BottomPushPopupWindow<GoodDetail> {
    private GoodsDetailActivity context;
    private View mMenuView;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.number)
    TextView number;
    @Bind(R.id.select)
    TextView select;
    @Bind(R.id.goods_image)
    ImageView goods_image;
    @Bind(R.id.select_layout)
    LinearLayout select_layout;
    @Bind(R.id.add_shopping_card)
    Button add_shopping_card;
    @Bind(R.id.purchase)
    Button purchase;
    @Bind(R.id.determine)
    Button determine;
    @Bind(R.id.bottom_layout)
    LinearLayout bottom_layout;
    @Bind(R.id.add_sub_view)
    AddAndSubView add_sub_view;
    private GoodDetail goodDetail;
    private List<SelectFixGridLayout> selectFixGridLayouts;
    Saledynamic saledynamic = null;
    public boolean isPurchase = false;
    List<SaleInfo> mlist;

    public BabyPopWindow(Activity context, GoodDetail goodDetail) {
        super(context, goodDetail);
        this.context = (GoodsDetailActivity) context;
    }

    public void setMlist(List<SaleInfo> mlist) {
        this.mlist = mlist;
        loading();
    }

    @Override
    protected View generateCustomView(GoodDetail data) {
        if (data != null) {
            this.goodDetail = data;
            selectFixGridLayouts = new ArrayList<>();
            mMenuView = View.inflate(bActivity, R.layout.adapter_popwindow, null);
            ButterKnife.bind(this, mMenuView);
            if (goodDetail.picList != null) {
                ApiClient.displayImage(goodDetail.picList.get(0).thumb, goods_image);
            }
            price.setText("￥" + goodDetail.goods.price);
            number.setText("库存" + goodDetail.goods.number + "件");
            add_sub_view.setMaxNum(goodDetail.goods.number);

        }
        return mMenuView;
    }


    //加入购物车
    @OnClick({R.id.add_shopping_card, R.id.determine, R.id.cancel})
    public void onBtnClick(View view) {
        if (view.getId() == R.id.add_shopping_card || view.getId() == R.id.determine) {
            if (selectFixGridLayouts.size() > 0) {
                if (getSaledynamic() == null) {
                    context.toast("请选择商品属性");
                    return;
                } else if (getSaledynamic().number <= 0) {
                    context.toast("无货，请重新选择");
                    return;
                }
            }
            switch (view.getId()) {
                case R.id.add_shopping_card:
                    context.addCard();
                    break;
                case R.id.determine:
                    if (isPurchase) {
                        context.addPurchase();
                    } else {
                        context.addCard();
                    }
                    break;
                case R.id.purchase:
                    context.addPurchase();
                    break;
            }

        } else {
            hide();
        }

    }


    private void loading() {
        if (mlist != null && mlist.size() > 0) {
            if (selectFixGridLayouts != null) {
                selectFixGridLayouts.clear();
            }
            StringBuffer sb = new StringBuffer("请选择 ");
            StringBuffer sbSelect = new StringBuffer("已选  ");
            boolean isSend = true;
            for (int i = 0; i < mlist.size(); i++) {
                SaleInfo info = mlist.get(i);
                if (info.list.size() > 1) {
                    sb.append("\"");
                    sb.append(info.attrname);
                    sb.append("\" ");
                    isSend = false;
                } else if (info.list.size() == 1) {
                    sbSelect.append("\"");
                    sbSelect.append(info.list.get(0).attrvname);
                    sbSelect.append("\" ");
                }
                addView(info, i);
            }
            if (isSend) {
                select.setText(sbSelect.toString());
            } else {
                select.setText(sb.toString());
            }
        }
    }

    public void show(Activity activity, boolean isbuy) {
        if (!isShowing()) {
            if (isbuy) {
                determine.setVisibility(View.GONE);
                bottom_layout.setVisibility(View.VISIBLE);
            } else {
                determine.setVisibility(View.VISIBLE);
                bottom_layout.setVisibility(View.GONE);
            }
            show(activity);
            if (saledynamic == null && mlist != null) {
                StringBuffer cx = new StringBuffer("");
                if (selectFixGridLayouts.size() == 1) {
                    SelectFixGridLayout si = selectFixGridLayouts.get(0);
                    if (si.getSelect() != null) {
                        cx.append(si.getParentSale().aid);
                        cx.append("-");
                        cx.append(si.getSelect().vid);
                        send(cx.toString());
                    }
                } else {
                    boolean isNoe = true;
                    for (SaleInfo info : mlist) {
                        if (info.list.size() == 1) {
                            cx.append(info.aid);
                            cx.append("-");
                            cx.append(info.list.get(0).vid);
                            cx.append("_");
                        } else {
                            isNoe = false;
                        }
                    }
                    if (isNoe) {
                        if (cx.length() > 0) {
                            cx.delete(cx.length() - 1, cx.length());
                        }
                        send(cx.toString());
                    }
                }

            }
        }
    }

    public void hide() {
        if (isShowing()) {
            dismiss();
        }
    }

    private void addView(SaleInfo si, int i) {
        View view = View.inflate(bActivity, R.layout.babypop_list, null);
        TextView tv = (TextView) view.findViewById(R.id.type_name);
        tv.setText(si.attrname);
        SelectFixGridLayout sfl = (SelectFixGridLayout) view.findViewById(R.id.select_layout);
        sfl.setList(si);
        selectFixGridLayouts.add(sfl);
        sfl.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClickOKPop() {
                StringBuffer sbSelect = new StringBuffer("已选  ");
                StringBuffer sbNoSelect = new StringBuffer("请选择  ");
                StringBuffer cx = new StringBuffer("");
                boolean isSelect = true;
                List<SelectFixGridLayout> cxList = new ArrayList<SelectFixGridLayout>();
                for (SelectFixGridLayout sfgl : selectFixGridLayouts) {
                    if (isSelect && sfgl.isSelect()) {
                        isSelect = true;
                        sbSelect.append("\"");
                        sbSelect.append(sfgl.getSelect().attrvname);
                        sbSelect.append("\" ");
                        cxList.add(sfgl);
                    } else if (!sfgl.isSelect()) {
                        isSelect = false;
                        sbNoSelect.append(sfgl.getParentSale().attrname);
                        sbNoSelect.append(" ");
                    }
                }
                if (isSelect) {
                    for (SelectFixGridLayout sfgl : cxList) {
                        cx.append(sfgl.getParentSale().aid);
                        cx.append("-");
                        cx.append(sfgl.getSelect().vid);
                        cx.append("_");
                    }
                    cx.delete(cx.length() - 1, cx.length());
                    send(cx.toString());
                    select.setText(sbSelect.toString());
                } else {
                    saledynamic = null;
                    select.setText(sbNoSelect.toString());
                }
            }
        });
        select_layout.addView(view, i);
    }

    private void send(String cx) {
//        入参格式：1、{goodsId}//商品的id
//        2、{cx:xxxx,//为该销售属性的组合数据：组合格式:aid-vid或者aid-vid-dictvid，多种销售属性以下划线连接，诸如（aid-vid_aid-vid-dictvid），这里的aid即，属性的id ;vid：属性值的id ;dictvid：关联的数据字典的id（system_code的id）
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", goodDetail.goods.id);
        map.put("cx", cx);
        ApiClient.send(context, JConstant.SALEDYNAMIC_, map, true, Saledynamic.class, new DataLoader<Saledynamic>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Saledynamic data) {
                saledynamic = data;
                replace();
            }

            @Override
            public void error(String message) {
                context.toast("加载失败");
                saledynamic = null;
                replace();
            }
        }, JConstant.SALEDYNAMIC_);
    }

    private void replace() {
        if (saledynamic != null) {
            if (add_sub_view.getNum() > saledynamic.number) {
                add_sub_view.setNum(saledynamic.number);
                add_sub_view.setMaxNum(saledynamic.number);
            }
            price.setText("￥" + saledynamic.price);
            number.setText("库存" + saledynamic.number + "件");
            add_sub_view.setMaxNum(saledynamic.number);
            if (saledynamic.number <= 0) {
                add_shopping_card.setBackgroundResource(R.color.btn_disable);
                purchase.setBackgroundResource(R.color.btn_disable);
                determine.setBackgroundResource(R.color.btn_disable);
            } else {
                add_shopping_card.setBackgroundResource(R.drawable.add_card_button_selector);
                purchase.setBackgroundResource(R.drawable.purchase_button_selector);
                determine.setBackgroundResource(R.drawable.purchase_button_selector);
            }
        } else {
            add_shopping_card.setBackgroundResource(R.color.btn_disable);
            purchase.setBackgroundResource(R.color.btn_disable);
            determine.setBackgroundResource(R.color.btn_disable);
            price.setText("￥0.00");
            number.setText("库存0件");
            add_sub_view.setNum(0);
            add_sub_view.setMaxNum(0);
        }
        context.Replace(1);
    }


    public Saledynamic getSaledynamic() {
        return saledynamic;
    }

    public void setSaledynamic(Saledynamic saledynamic) {
        this.saledynamic = saledynamic;
        replace();
    }

    public interface OnItemClickListener {
        /**
         * 设置点击确认按钮时监听接口
         */
        public void onClickOKPop();
    }

    public int Number() {
        return add_sub_view.getNum();
    }

}
