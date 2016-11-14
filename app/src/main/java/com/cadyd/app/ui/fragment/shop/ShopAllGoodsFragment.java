package com.cadyd.app.ui.fragment.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.RecylerIsBottom;
import com.cadyd.app.model.MyPage;
import com.cadyd.app.model.OneGoodsModel;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.utils.StringUtils;

import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * zjh
 * 店铺的所有商品
 */
public class ShopAllGoodsFragment extends BaseFragement {
    private boolean ISREFRESH = true;//是否上拉加载
    private boolean ISSWITCH = false;
    private String shopId = "";
    private int index = 1;
    private String type;
    private String title;//记录搜索的内容

    private String menuFirstNode = null;//分类
    private String brandId = null;//品牌

    private List<OneGoodsModel> modelList = new ArrayList<>();
    private CommonRecyclerAdapter<OneGoodsModel> adapter;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    public static ShopAllGoodsFragment newFragment(String shopId) {
        ShopAllGoodsFragment fragment = new ShopAllGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            shopId = bundle.getString("id");
        }
    }


    /**
     * 显示相应分类的商品
     **/
    public void showMenuFirstNode(String str, String title) {
        if (brandId != null) {
            brandId = null;
        }
        menuFirstNode = str;
        ISSWITCH = true;
        initialization(false, title);
    }

    /**
     * 显示相应分类的商品
     **/
    public void showBrandId(String str, String title) {
        if (menuFirstNode != null) {
            menuFirstNode = null;
        }
        brandId = str;
        ISSWITCH = true;
        initialization(false, title);
    }

    /**
     * 左右切换时 bool 为true
     */
    public boolean initialization(boolean bool, String title) {
        if (bool) {
            if (ISSWITCH) {
                brandId = null;
                menuFirstNode = null;
                initializationStart(title);
                ISSWITCH = false;
                return true;
            }
        } else {
            initializationStart(title);
            return true;
        }
        return false;
    }

    /**
     * 初始化该页面的所有数据
     */
    private void initializationStart(String title) {
        index = 1;
        if (!StringUtils.isEmpty(title)) {
            setContent(title, null, false);
        } else {
            setContent(null, null, false);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_shop_all_goods);
    }


    @Override
    protected void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        //上拉加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecylerIsBottom.isSlideToBottom(recyclerView)) {
                    if (ISREFRESH) {
                        index++;
                        setContent(title, type, false);
                    }
                }
            }
        });
        setContent(title, null, false);
    }

    /**
     * 获取商品列表
     */
    public void setContent(String MyTitle, String orderBy, boolean isChange) {
        if (isChange) {
            index = 1;
        }

        if (!StringUtils.isEmpty(MyTitle)) {
            title = MyTitle;
        } else {
            title = null;
        }

        if (!"-1".equals(orderBy)) {
            type = orderBy;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        map.put("shopId", shopId);
        map.put("pageSize", 10);
        map.put("orderBy", type);//排序条件
        map.put("title", title);

        map.put("menuFirstNode", menuFirstNode);//分类类型
        map.put("brandId", brandId);//品牌

        ApiClient.send(activity, JConstant.QUERYSHOPIDTOLISTGOODS, map, true, MyPage.class, new DataLoader<MyPage>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(MyPage data) {
                if (index == 1) {
                    modelList.clear();
                    ISREFRESH = true;
                    if (adapter != null) {
                        recyclerView.setAdapter(adapter);
                    }
                }
                if (data != null && data.data != null) {
                    if (data.data.size() < 10) {
                        ISREFRESH = false;
                    }
                    modelList.addAll(data.data);
                    if (adapter == null) {
                        adapter = new CommonRecyclerAdapter<OneGoodsModel>(activity, modelList, R.layout.shop_all_goods_item) {
                            @Override
                            public void convert(ViewRecyclerHolder helper, final OneGoodsModel item) {
                                ImageView imageView = helper.getView(R.id.image_view);
                                ApiClient.displayImage(item.thumb, imageView, R.mipmap.defaiut_on);
                                helper.setText(R.id.price, "￥ " + item.price);
                                TextView name = helper.getView(R.id.type);
                                if (StringUtil.hasText(item.name)) {
                                    name.setText(item.name);
                                } else {
                                    name.setVisibility(View.INVISIBLE);
                                }
                                helper.setText(R.id.name, item.title);
                                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {//跳转到商品详情
                                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                                        intent.putExtra("gid", item.id);
                                        activity.startActivity(intent);
                                    }
                                });
                            }
                        };
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.QUERYSHOPIDTOLISTGOODS);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.QUERYSHOPIDTOLISTGOODS);
    }
}
