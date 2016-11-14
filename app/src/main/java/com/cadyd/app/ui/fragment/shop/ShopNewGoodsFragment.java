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

import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺新品
 */
public class ShopNewGoodsFragment extends BaseFragement {

    private int index = 1;
    private String shopId = "";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<OneGoodsModel> modelList = new ArrayList<>();
    private CommonRecyclerAdapter<OneGoodsModel> adapter;


    public static ShopNewGoodsFragment newFragment(String shopId) {
        ShopNewGoodsFragment fragment = new ShopNewGoodsFragment();
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_shop_new_goods);
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecylerIsBottom.isSlideToBottom(recyclerView)) {
                    index++;
                    setContent("mg.created desc");
                }
            }
        });

        setContent("mg.created desc");
    }


    /**
     * 获取商品列表
     */
    private void setContent(String orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        map.put("shopId", shopId);
        map.put("pageSize", 10);
        map.put("orderBy", orderBy);//排序条件

        map.put("menuFirstNode", null);//分类类型
        map.put("brandId", null);//品牌

        ApiClient.send(activity, JConstant.QUERYSHOPIDTOLISTGOODS, map, true, MyPage.class, new DataLoader<MyPage>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(MyPage data) {
                if (data != null && data.data != null) {
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
                                    name.setVisibility(View.GONE);
                                }
                                helper.setText(R.id.name, item.title);
                                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
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
