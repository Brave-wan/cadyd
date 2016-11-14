package com.cadyd.app.ui.fragment.unitary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.NewAnnounceAdapter;
import com.cadyd.app.adapter.RTescoAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.OneParameterInterface;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.activity.CommWebViewActivity;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.ToastView;
import com.cadyd.app.ui.view.WrapContentHeightViewPager;
import com.cadyd.app.ui.view.adapter.MyViewPagerAdapter;
import com.cadyd.app.ui.view.control.PageControl;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import com.ypy.eventbus.EventBus;

import org.wcy.android.utils.PreferenceUtils;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一元购首页
 */

public class OneYuanTescoFragment extends BaseFragement {

    private OneParameterInterface oneParameterInterface;

    @Bind(R.id.ui_tesco_head)
    ImageCycleView cycleView;
    //商品选择导航
    @Bind(R.id.ui_tesco_group)
    RadioGroup group;
    @Bind(R.id.ui_tesco_rq)
    RadioButton button_rq;
    @Bind(R.id.ui_tesco_jd)
    RadioButton button_jd;
    @Bind(R.id.ui_tesco_zx)
    RadioButton button_zx;
    @Bind(R.id.ui_tesco_zxrc)
    RadioButton button_zxrc;
    //一元购的最新揭晓
    @Bind(R.id.new_announce_title)
    TextView new_announce_title;
    @Bind(R.id.new_announce_viewpage)
    WrapContentHeightViewPager new_announce_viewpage;
    @Bind(R.id.new_announceviewpage_control)
    LinearLayout new_announceviewpage_control;
    @Bind(R.id.new_announce_layout)
    LinearLayout new_announce_layout;
    @Bind(R.id.new_announce_line)
    View new_announce_line;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swip;

    private PageControl announceviewPageControl = null;
    private static final float APP_PAGE_SIZE = 4.0f;

    private RTescoAdapter adapter;

    private int LightType = 0;
    private int TopType = -1;

    private List<OneGoodsModel> goodsModels = new ArrayList<>();

    private List<String> urlsImage = new ArrayList<>();


    public void setOnClick(OneParameterInterface oneParameterInterface) {
        this.oneParameterInterface = oneParameterInterface;
    }

    private int alphaMax = 160;
    @Bind(R.id.title_layout)//大标题
            RelativeLayout title_bg;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;

    LayoutInflater inflater;
    int pageindext = 1;

    @Bind(R.id.ui_tesco_content)
    RecyclerView content;

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return setView(inflater, R.layout.activity_one_yuan_tesco);
    }

    @Override
    protected void initView() {
        swip.setColorSchemeResources(R.color.Orange);
        //下拉刷新
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageindext = 1;
                goodsModels.clear();
                GetContentHttp();
                loadingNewAnnounce();//获得最新揭晓
            }
        });
        //上拉加载
        content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isSlideToBottom(recyclerView)) {
                    swip.setRefreshing(true);
                    pageindext++;
                    GetContentHttp();
                }
            }
        });


        RecyclerView.LayoutManager manager = new GridLayoutManager(activity, 2);
        content.setLayoutManager(manager);
        adapter = null;
        //商品选择导航
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pageindext = 1;
                switch (checkedId) {
                    case R.id.ui_tesco_rq:
                        //1人气
                        TopType = 1;
                        goodsModels.clear();
                        GetContentHttp();
                        break;
                    case R.id.ui_tesco_jd:
                        //2进度
                        TopType = 2;
                        goodsModels.clear();
                        GetContentHttp();
                        break;
                    case R.id.ui_tesco_zx:
                        //0最新
                        TopType = 0;
                        goodsModels.clear();
                        GetContentHttp();
                        break;
                    case R.id.ui_tesco_zxrc:
                        //4总需人次
                        TopType = 4;
                        goodsModels.clear();
                        GetContentHttp();
                        break;
                }
            }
        });
        if (TopType == -1) {
            button_rq.setChecked(true);
        }
        title_bg.getBackground().setAlpha(0);
        back.getBackground().setAlpha(alphaMax);
        title.setAlpha(1 - alphaMax / 100);
        getAdvert();//获得广告
        loadingNewAnnounce();//获得最新揭晓
    }


    @OnClick(R.id.back)
    public void onBack(View view) {
        finishActivity();
    }

    //所有分类
    @OnClick(R.id.ui_tesco_all_class)
    public void onAllClass(View view) {
        if (oneParameterInterface != null) {
            oneParameterInterface.Onchange(0);
        }
    }

    //十元专区
    @OnClick(R.id.ui_tesco_ten_yuan)
    public void onTenYuan(View view) {
        if (oneParameterInterface != null) {
            oneParameterInterface.Onchange(10);
        }

    }

    //所有晒单
    @OnClick(R.id.ui_tesco_the_sun)
    public void onTheSun(View view) {
        //登录判断
        if (!application.isLogin()) {
            ToastView.show(activity, "请先登录");
            Intent intent = new Intent(activity, SignInActivity.class);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.THE_SUN);
        intent.putExtra("title", "所有晒单");
        activity.startActivity(intent);
    }

    //返享
    @OnClick(R.id.ui_tesco_enjoy)
    public void onEnjoy(View view) {
        //登录判断
        if (!application.isLogin()) {
            ToastView.show(activity, "请先登录");
            Intent intent = new Intent(activity, SignInActivity.class);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(activity, CommWebViewActivity.class);
        String ID = null;
        if (application.model != null && application.model.id != null) {
            ID = application.model.id;
        }
        intent.putExtra("url", JConstant.YUANGOUFX + ID);
        activity.startActivity(intent);
    }

    //常见问题
    @OnClick(R.id.ui_tesco_common_problem)
    public void onProblem(View view) {
        Intent intent = new Intent(activity, CommWebViewActivity.class);
        intent.putExtra("url", JConstant.YUANGOUWENTI);
        activity.startActivity(intent);
    }

    //查询商品
    private void GetContentHttp() {

        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageindext);//分页查询当前页数
        map.put("categroyId", LightType);//商品分类ID
        map.put("order", TopType);//	排序ID
        boolean isDialog = false;
        if (pageindext == 1) {
            isDialog = false;
        } else {
            isDialog = true;
        }
        ApiClient.send(activity, JConstant.QUERYALLPRODUCTRECORD_, map, isDialog, MyPage.class, new DataLoader<MyPage>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(MyPage data) {
                swip.setRefreshing(false);
                if (data != null) {
                    if (data.data != null) {
                        goodsModels.addAll(data.data);
                    }
                    //设置
                    if (adapter == null) {
                        adapter = new RTescoAdapter(activity, goodsModels);
                        adapter.setClick(new TowParameterInterface() {
                            @Override
                            public void Onchange(int type, String pos) {
                                switch (type) {
                                    case 0://跳转详情
                                        Intent intent1 = new Intent(activity, CommonActivity.class);
                                        intent1.putExtra(JConstant.EXTRA_INDEX, CommonActivity.ONE_YUAN_DETAIL);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("goods", goodsModels.get(Integer.valueOf(pos)));
                                        intent1.putExtras(bundle);
                                        startActivityForResult(intent1, 0);
                                        break;
                                    case 1://加入购物车
                                        if (!application.isLogin()) {
                                            ToastView.show(activity, "请先登录");
                                            Intent intent = new Intent(activity, SignInActivity.class);
                                            startActivity(intent);
                                            return;
                                        }
                                        addCarHttp(Integer.valueOf(pos));
                                        break;
                                }
                            }
                        });
                        content.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void error(String message) {
                swip.setRefreshing(false);
            }

        }, JConstant.QUERYALLPRODUCTRECORD_);
    }

    //加入购物车
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

    //获得广告
    private void getAdvert() {
        CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("position", "Y101");
        map.put("city", StringUtil.hasText(city.id) ? city.id : city.areaid);
        ApiClient.send(activity, JConstant.FINDADVER_, map, Findadver.getType(), new DataLoader<List<Findadver>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Findadver> data) {
                final List<Findadver> findadvers = data;

                if (findadvers == null || findadvers.size() <= 0) {
                    cycleView.setBackgroundResource(R.mipmap.adv_top);
                } else {
                    for (int i = 0; i < findadvers.size(); i++) {
                        urlsImage.add(findadvers.get(i).imgurl);
                    }
                    //广告图片单击事件
                    cycleView.setImageResources(urlsImage, new ImageCycleView.ImageCycleViewListener() {
                        @Override
                        public void onImageClick(int position, View imageView) {
                            // TODO 单击图片处理事件
                        }

                        @Override
                        public void carousel() {
                        }
                    }, R.mipmap.adv_top);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDADVER_);
    }

    /**
     * 最新揭晓
     */
    private void loadingNewAnnounce() {
        Map<String, Object> map = new HashMap<>();
        map.put("categroyId", 0);
        map.put("pageIndex", 1);
        ApiClient.send(activity, JConstant.ONEQUERYALLANNOUNCEDRECORD_, map, NewAnnounce.getType(), new DataLoader<List<NewAnnounce>>() {

            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<NewAnnounce> data) {
                swip.setRefreshing(false);
                if (data != null && data.size() > 0) {
                    new_announce_layout.setVisibility(View.VISIBLE);
                    new_announce_title.setVisibility(View.VISIBLE);
                    int PageCount = (int) Math.ceil(data.size() / APP_PAGE_SIZE);
                    Map<Integer, GridView> map = new HashMap<>();
                    for (int i = 0; i < PageCount; i++) {
                        GridView appPage = (GridView) inflater.inflate(R.layout.newannounce_gridview, null);
                        final NewAnnounceAdapter adapter = new NewAnnounceAdapter(activity, data, i);
                        appPage.setNumColumns(2);
                        appPage.setAdapter(adapter);
                        map.put(i, appPage);
                    }

                    if (PageCount > 1) {
                        announceviewPageControl = new PageControl(activity, new_announceviewpage_control, PageCount);
                        new_announceviewpage_control.setVisibility(View.VISIBLE);
                        new_announce_line.setVisibility(View.VISIBLE);
                        new_announce_layout.setPadding(0, 0, 0, 10);
                    } else {
                        new_announceviewpage_control.setVisibility(View.GONE);
                        new_announce_layout.setPadding(0, 0, 0, 0);
                        new_announce_line.setVisibility(View.GONE);
                    }
                    new_announce_viewpage.setAdapter(new MyViewPagerAdapter(activity, map));
                    new_announce_viewpage.addOnPageChangeListener(new MyListener());
                } else {
                    new_announce_layout.setVisibility(View.GONE);
                    new_announce_title.setVisibility(View.GONE);
                }
            }

            @Override
            public void error(String message) {
                new_announce_title.setVisibility(View.GONE);
                swip.setRefreshing(false);
            }
        }, JConstant.ONEQUERYALLANNOUNCEDRECORD_);

    }

    class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            announceviewPageControl.selectPage(arg0);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
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
        ApiClient.cancelRequest(JConstant.QUERYALLPRODUCTRECORD_);
        ApiClient.cancelRequest(JConstant.ADDSHOPPINGCAR_);
        ApiClient.cancelRequest(JConstant.FINDADVER_);
        ApiClient.cancelRequest(JConstant.ONEQUERYALLANNOUNCEDRECORD_);
    }
}
