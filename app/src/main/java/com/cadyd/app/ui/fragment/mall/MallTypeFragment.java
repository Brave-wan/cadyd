package com.cadyd.app.ui.fragment.mall;


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
import com.cadyd.app.model.Good;
import com.cadyd.app.model.HomeMenu;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.IntegralGoodsDetailActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.EmptyLayout;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城分类
 *
 * @author wcy
 */
public class MallTypeFragment extends BaseFragement {

    private String type = "";
    @Bind(R.id.mall_type_root)
    LinearLayout mallTypeRoot;

    @Bind(R.id.tools_scrlllview)
    ScrollView scrollView;
    @Bind(R.id.tools)
    LinearLayout toolsLayout;
    @Bind(R.id.goods_menu)
    PullToRefreshGridView gridView;

    private TextView toolsTextViews[];
    private View views[];
    LayoutInflater inflater;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private int currentItem = 0;
    List<HomeMenu> listmenu = new ArrayList<>();
    List<Good> listGoods = new ArrayList<>();
    CommonAdapter<Good> adapter;
    int page = 1;
    @Bind(R.id.empty_layout)
    RelativeLayout empty_layout;

    EmptyLayout mEmptyLayout;

    public static MallTypeFragment newInstance(String key) {
        MallTypeFragment newFragment = new MallTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", key);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.type = args.getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return setView(inflater, R.layout.fragment_mall_type);
    }

    @Override
    protected void initView() {
        mEmptyLayout = new EmptyLayout(activity, empty_layout);
     //   mallTypeRoot.setBackgroundColor(getResources().getColor(R.color.title_bg));
        setPullListView();
        loadingType();
        adapter = new CommonAdapter<Good>(activity, listGoods, R.layout.mall_type_goodes_iteam) {
            @Override
            public void convert(ViewHolder helper, final Good item) {
                ImageView iv = helper.getView(R.id.image_view);
                ApiClient.displayImage(item.thumb, iv);
                if ("integral".equals(type)) {
                    helper.setText(R.id.name, item.title);
                } else {
                    helper.setText(R.id.name, item.name);
                }
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.hasText(item.menuId)) {
                            if ("integral".equals(type)) {
                                Intent intent = new Intent(mContext, IntegralGoodsDetailActivity.class);
                                intent.putExtra("gid", item.id);
                                mContext.startActivity(intent);
                            } else {
                                Intent intent = new Intent(activity, CommonActivity.class);
                                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_TYPE);
                                intent.putExtra("mid", item.menuId);
                                activity.startActivity(intent);
                            }
                        }
                    }
                });

            }
        };
        gridView.setAdapter(adapter);
    }

    @OnClick(R.id.search)
    public void onClickSearch(View view) {
        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_SEARCH);
        activity.startActivity(intent);
    }

    private void setPullListView() {
        gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullDownToRefresh");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullUpToRefresh");
                loaingGoodsMenu();
            }
        });
    }

    //获得商品
    private void loaingGoodsMenu() {
        Map<String, Object> map = new HashMap<>();

//        {pageIndex}	分页查询当前页数
//        {categroyId}	商品分类ID
//        {order}	排序ID
        int url;
        if ("integral".equals(type)) {
            url = JConstant.FINDBYMENUID;
        } else {
            url = JConstant.FINDGOODSBYROOTMENUID_;
        }

        if (listmenu.size() - 1 < currentItem) {
            return;
        }

        map.put("menuId", listmenu.get(currentItem).id);
        map.put("pageIndex", page);
        ApiClient.send(activity, url, map, true, Good.getType(), new DataLoader<List<Good>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Good> data) {
                if (data != null) {
                    page++;
                    listGoods.addAll(data);
                    adapter.notifyDataSetChanged();
                }
                if (listGoods.size() <= 0) {
                    mEmptyLayout.showEmpty();
                } else {
                    mEmptyLayout.showView();
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, url);
    }

    //获得类型
    private void loadingType() {
        int url;
        Map<String, Object> map = null;
        if ("integral".equals(type)) {
            url = JConstant.FINDMENUINTEGRAL;
        } else {
            url = JConstant.GOODSMENU_;
            map = new HashMap<>();
            map.put("type", type);
        }
        listmenu.clear();//清空数组避免装入重复数据
        ApiClient.send(activity, url, map, true, HomeMenu.getType(), new DataLoader<List<HomeMenu>>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<HomeMenu> data) {
                if (data != null) {
                    listmenu.addAll(data);
                    toolsTextViews = new TextView[data.size()];
                    views = new View[data.size()];
                    for (int i = 0; i < data.size(); i++) {
                        HomeMenu home = data.get(i);
                        View view = inflater.inflate(R.layout.item_b_top_nav_layout, null);
                        view.setId(i);
                        view.setOnClickListener(toolsItemListener);
                        TextView textView = (TextView) view.findViewById(R.id.text);
                        textView.setText(home.name);
                        toolsLayout.addView(view);
                        toolsTextViews[i] = textView;
                        views[i] = view;
                    }
                    changeTextColor(0);
                    for (int i = 0; i < listmenu.size(); i++) {
                        if (ID.equals(listmenu.get(i).id)) {
                            currentItem = i;
                            changeTextColor(i);
                            changeTextLocation(i);
                        }
                    }
                }
            }

            @Override
            public void error(String message) {

            }
        }, url);
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int arg0 = v.getId();
            if (currentItem != arg0) {
                currentItem = arg0;
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
        }
    };

    private String ID = "";

    public void ChageItem(String id) {
        if (id != null) {
            ID = id;
            for (int i = 0; i < listmenu.size(); i++) {
                if (id.equals(listmenu.get(i).id)) {
                    currentItem = i;
                    changeTextColor(i);
                    changeTextLocation(i);
                }
            }
        }
    }

    private void RefreshComplete() {
        gridView.onRefreshComplete();
    }

    /**
     * 改变textView的颜色
     *
     * @param position
     */
    private void changeTextColor(int position) {
        page = 1;
        listGoods.clear();
        adapter.notifyDataSetChanged();
        for (int i = 0; i < toolsTextViews.length; i++) {
            if (i != position) {
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff000000);
            }
        }
        //判断postion是否大于，view长度
        if (position <= (toolsTextViews.length - 1)) {
            toolsTextViews[position].setBackgroundResource(android.R.color.white);
            toolsTextViews[position].setTextColor(0xffff5d5e);
            loaingGoodsMenu();
        }

    }


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.FINDBYMENUID);
        ApiClient.cancelRequest(JConstant.GOODSMENU_);
        ApiClient.cancelRequest(JConstant.FINDGOODSBYROOTMENUID_);
        ApiClient.cancelRequest(JConstant.FINDMENUINTEGRAL);
    }
}
