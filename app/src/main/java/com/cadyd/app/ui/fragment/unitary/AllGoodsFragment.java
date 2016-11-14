package com.cadyd.app.ui.fragment.unitary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import butterknife.Bind;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.AllGoodsGridAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.HorseTextView;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.MyPage;
import com.cadyd.app.model.OneGoodsModel;
import com.cadyd.app.model.QueryClassIfication;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.ToastView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllGoodsFragment extends BaseFragement {

    public int TYPE;
    public static final int ALL = 0;
    public static final int TENYUAN = 10;

    @Bind(R.id.ui_all_goods_content_grid)
    PullToRefreshGridView gridView;

    @Bind(R.id.ui_all_goods_horizontal_scroll)
    LinearLayout H_lineraLayout;
    @Bind(R.id.scroview)
    ScrollView scrollView;
    @Bind(R.id.lift_text_list)
    LinearLayout lisft_lineraLayout;

    @Bind(R.id.horse_text)
    HorseTextView horseText;

    private LayoutInflater inflater;

    private QueryClassIfication ification = null;

    private List<OneGoodsModel> goodsModels = new ArrayList<>();

    private TextView TopOldText;
    private TextView LiftOldText;
    private int oldTYPE;
    private int Light_type = 0;
    private int Top_type;

    @Bind(R.id.all_goods_relative)
    RelativeLayout all_goods_relative;
    private AllGoodsGridAdapter gridAdapter = null;
    private int pageindext = 1;

    private boolean isShowLogo = true;


    //添加购物车
    private void addCarHttp(int pos) {

        if (!application.isLogin()) {
            ToastView.show(activity, "请先登录");
            Intent intent = new Intent(activity, SignInActivity.class);
            startActivity(intent);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("tbid", goodsModels.get(pos).tbid);//抢购商品主键
        map.put("buytimes", 1);//参与人次
        map.put("type", 0);
        ApiClient.send(activity, JConstant.ADDSHOPPINGCAR_, map, true, null, new DataLoader<String>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(String data) {
                toastSuccess("添加成功");
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.ADDSHOPPINGCAR_);
    }

    public AllGoodsFragment() {
        super();
    }

    public static AllGoodsFragment newInstance(int type) {
        AllGoodsFragment newFragment = new AllGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
//              this.TYPE = args.getInt("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return setView(R.layout.activity_all_goods, "所有商品", true);
    }

    @Override
    protected void initView() {
        horseText.startScroll();//开启跑马灯
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horseText.stopScroll();//关闭跑马灯
                finishActivity();
            }
        });
        gridView.setMode(PullToRefreshBase.Mode.BOTH);
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                pageindext = 1;
                goodsModels.clear();
                GetContentHttp();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                pageindext++;
                GetContentHttp();
            }
        });

        //分类数据
        oldTYPE = TYPE;
        gridAdapter = null;
        queryClass();
    }

    //二次进入
    public void MyNotify() {
        if (ification != null && oldTYPE != TYPE) {
            oldTYPE = TYPE;
            if (TYPE == 10) {
                for (int i = 0; i < ification.categorylist.size(); i++) {
                    if ("十元专区".equals(ification.categorylist.get(i).name)) {
                        Light_type = i;
                        if (LiftOldText != null) {
                            LiftOldText.setBackgroundResource(R.color.transparent);
                            LiftOldText.setTextColor(Color.BLACK);
                        }
                        tenText.setBackgroundResource(R.color.white);
                        tenText.setTextColor(Color.RED);
                        LiftOldText = tenText;
                        changeTextLocation(lisft_lineraLayout.getChildAt((int) tenText.getTag()));//位置变动到十元专区
                        horseText.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                pageindext = 1;
                goodsModels.clear();
                GetContentHttp();
            } else if (TYPE == 0) {
                for (int i = 0; i < ification.categorylist.size(); i++) {
                    if ("全部分类".equals(ification.categorylist.get(i).name)) {
                        Light_type = i;
                        if (LiftOldText != null) {
                            LiftOldText.setBackgroundResource(R.color.transparent);
                            LiftOldText.setTextColor(Color.BLACK);
                        }
                        allText.setBackgroundResource(R.color.white);
                        allText.setTextColor(Color.RED);
                        LiftOldText = allText;
                        changeTextLocation(lisft_lineraLayout.getChildAt((int) allText.getTag()));//位置变动到全部分类
                        horseText.setVisibility(View.GONE);
                        break;
                    }
                }
                pageindext = 1;
                goodsModels.clear();
                GetContentHttp();
            }
        }
    }

    //查询分类
    private void queryClass() {
        Map<String, Object> map = new HashMap<>();
        ApiClient.send(activity, JConstant.QUERYCLASS_, map, QueryClassIfication.class, new DataLoader<QueryClassIfication>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(QueryClassIfication data) {
                ification = data;
                if (TYPE == 10) {
                    for (int i = 0; i < ification.categorylist.size(); i++) {
                        if ("十元专区".equals(ification.categorylist.get(i).name)) {
                            Light_type = i;
                            horseText.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                } else {
                    horseText.setVisibility(View.GONE);
                }

                //左边文本
                for (int i = 0; i < ification.categorylist.size(); i++) {
                    lisft_lineraLayout.addView(addLiftText(ification.categorylist.get(i).name, i));
                }
                //上面文本
                for (int i = 0; i < ification.orderlist.size(); i++) {
                    H_lineraLayout.addView(addTopText(ification.orderlist.get(i).name, i));
                }

                GetContentHttp();
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.QUERYCLASS_);
    }

    private View addTopText(String name, int type) {
        final View view = inflater.inflate(R.layout.textview, null);
        final TextView textView = (TextView) view.findViewById(R.id.text_view);
        CharSequence sequence = Html.fromHtml(name);
        textView.setTag(type);
        textView.setText(sequence);
        if (type == 0) {
            textView.setBackgroundResource(R.color.white);
            textView.setTextColor(Color.RED);
            TopOldText = textView;
        } else {
            textView.setBackgroundResource(R.color.transparent);
            textView.setTextColor(Color.BLACK);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择监听
                Top_type = (int) v.getTag();
                if (TopOldText != null) {
                    TopOldText.setBackgroundResource(R.color.transparent);
                    TopOldText.setTextColor(Color.BLACK);
                }
                textView.setBackgroundResource(R.color.white);
                textView.setTextColor(Color.RED);
                TopOldText = textView;
                pageindext = 1;
                goodsModels.clear();
                gridView.onRefreshComplete();
                isShowLogo = true;
                GetContentHttp();
            }
        });
        return view;
    }

    private TextView allText;
    private TextView tenText;


    private View addLiftText(String name, int type) {
        final View view = inflater.inflate(R.layout.list_text_item, null);
        view.setTag(type);
        final TextView textView = (TextView) view.findViewById(R.id.list_item_text);
        textView.setTag(type);
        CharSequence sequence = Html.fromHtml(name);
        textView.setText(sequence);
/**将十元专区和全部的项保存下来**/
        int liftFirst = 0;
        for (int i = 0; i < ification.categorylist.size(); i++) {
            if ("十元专区".equals(ification.categorylist.get(i).name)) {
                liftFirst = i;
                break;
            }
        }
        //保存十元专区
        if (type == liftFirst) {
            tenText = textView;
        }
        //保存全部分类
        if (type == 0) {
            allText = textView;
        }
/****/
        //初始化设置
        if (type == Light_type) {
            textView.setBackgroundResource(R.color.white);
            textView.setTextColor(Color.RED);
            LiftOldText = textView;
        } else {
            textView.setBackgroundResource(R.color.transparent);
            textView.setTextColor(Color.BLACK);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择监听
                Light_type = (int) v.getTag();
                if (LiftOldText != null) {
                    LiftOldText.setBackgroundResource(R.color.transparent);
                    LiftOldText.setTextColor(Color.BLACK);
                }
                textView.setBackgroundResource(R.color.white);
                textView.setTextColor(Color.RED);
                LiftOldText = textView;
                if ((int) tenText.getTag() == (int) textView.getTag()) {
                    horseText.setVisibility(View.VISIBLE);
                } else {
                    horseText.setVisibility(View.GONE);
                }
                pageindext = 1;
                goodsModels.clear();

                gridView.onRefreshComplete();
                changeTextLocation(lisft_lineraLayout.getChildAt((int) v.getTag()));
                isShowLogo = true;
                GetContentHttp();
            }
        });
        return view;
    }


    /**
     * 改变栏目位置
     */
    private void changeTextLocation(View view) {
        int x = (view.getTop() - getScrollViewMiddle() + (getViewheight(view) / 2));
        scrollView.smoothScrollTo(0, x);

    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        return (scrollView.getBottom() - scrollView.getTop()) / 2;
    }

    /**
     * 返回view的高度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }


    /**
     * 查询商品
     */
    private void GetContentHttp() {
        if (ification == null) {
            return;
        }

        if (ification.categorylist.size() <= 0) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageindext);//分页查询当前页数
        map.put("categroyId", ification.categorylist.get(Light_type).id);//商品分类ID
        map.put("order", ification.orderlist.get(Top_type).id);//	排序ID

        ApiClient.send(activity, JConstant.QUERYALLPRODUCTRECORD_, map, isShowLogo, MyPage.class, new DataLoader<MyPage>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(MyPage data) {
                gridView.onRefreshComplete();
                isShowLogo = false;
                if (data.data != null) {
                    goodsModels.addAll(data.data);

                    if (gridAdapter == null) {
                        gridAdapter = new AllGoodsGridAdapter(activity, goodsModels);
                        gridAdapter.setClick(new TowObjectParameterInterface() {
                            @Override
                            public void Onchange(int type, int pos, Object object) {
                                switch (type) {
                                    case 0:
                                        Intent intent1 = new Intent(activity, CommonActivity.class);
                                        intent1.putExtra(JConstant.EXTRA_INDEX, CommonActivity.ONE_YUAN_DETAIL);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("goods", goodsModels.get(pos));
                                        intent1.putExtras(bundle);
                                        startActivityForResult(intent1, 0);
                                        break;
                                    case 1:
                                        //加入购物车
                                        addCarHttp(pos);
                                        break;
                                }
                            }
                        });
                        gridView.setAdapter(gridAdapter);
                    } else {
                        gridAdapter.notifyDataSetChanged();
                    }

                }
                if (goodsModels.size() > 0) {
                    all_goods_relative.setVisibility(View.GONE);
                } else {
                    all_goods_relative.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(String message) {
                gridView.onRefreshComplete();
            }
        }, JConstant.QUERYALLPRODUCTRECORD_);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            activity.setResult(Activity.RESULT_OK);
            //刷新
            pageindext = 1;
            goodsModels.clear();
            GetContentHttp();
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
        ApiClient.cancelRequest(JConstant.QUERYCLASS_);
        ApiClient.cancelRequest(JConstant.QUERYALLPRODUCTRECORD_);
        ApiClient.cancelRequest(JConstant.ADDSHOPPINGCAR_);
    }
}
