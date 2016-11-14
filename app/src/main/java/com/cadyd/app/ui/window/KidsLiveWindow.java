package com.cadyd.app.ui.window;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.KidsLiveWindowRecyclerAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.MyPage;
import com.cadyd.app.model.OneGoodsModel;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.widget.PLVideoViewActivity;

import org.wcy.android.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiongmao on 2016/8/25.
 */

public class KidsLiveWindow extends PopupWindow {
    private Activity context;
    private LayoutInflater inflater;
    private KidsLiveWindowRecyclerAdapter adapter;
    private String id;
    private View view;
    private RecyclerView recyclerView;
    private LinearLayout listviewEmpty;

    public KidsLiveWindow(Activity context, String id) {
        this.context = context;
        this.id = id;
        inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.kids_live_window_layout, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        listviewEmpty = (LinearLayout) view.findViewById(R.id.listviewEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        setContentView(view);

        getContent("mg.created");
        getContent(null);

    }

    private void setContent() {
        if (adapter == null) {
            adapter = new KidsLiveWindowRecyclerAdapter(context, AllModelList, hotModelList);
            adapter.setClick(new TowObjectParameterInterface() {
                @Override
                public void Onchange(int type, int postion, Object object) {
                    //TODO adapter中的内容的回调监听
                    switch (type) {
                        case 0://最热推荐商品点击监听
                            OneGoodsModel oneGoodsModel = (OneGoodsModel) object;
                            Intent intent = new Intent(context, GoodsDetailActivity.class);
                            intent.putExtra("gid", oneGoodsModel.id);
                            context.startActivity(intent);
                            break;
                        case 1://全部商品的商品点击监听
                            oneGoodsModel = (OneGoodsModel) object;
                            intent = new Intent(context, GoodsDetailActivity.class);
                            intent.putExtra("gid", oneGoodsModel.id);
                            context.startActivity(intent);
                            break;
                        case 2://全部商品加入购物车的点击监听
                            oneGoodsModel = (OneGoodsModel) object;
                            intent = new Intent(context, GoodsDetailActivity.class);
                            intent.putExtra("gid", oneGoodsModel.id);
                            context.startActivity(intent);
                            break;
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }


    /**
     * 获取商品列表
     */
    private List<OneGoodsModel> AllModelList;
    private List<OneGoodsModel> hotModelList;

    private void getContent(final String str) {
        LogUtil.i(KidsLiveWindow.class, "直播获取商品列表");
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", 1);
        map.put("shopId", id);
        map.put("pageSize", 10);
        map.put("orderBy", str);//排序条件
        map.put("menuFirstNode", null);//分类类型
        map.put("brandId", null);//品牌

        ApiClient.send(context, JConstant.QUERYSHOPIDTOLISTGOODS, map, true, MyPage.class, new DataLoader<MyPage>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(MyPage data) {
                if (data == null || data.data == null || data.data.size() > 0) {
                    if ("mg.created".equals(str)) {//新品上架  热门商品
                        hotModelList = data.data;
                    } else {
                        if (data.data != null) {
                            AllModelList = data.data;
                        }
                    }
                    if (AllModelList != null && hotModelList != null) {
                        if (AllModelList.size() > 0) {
                            listviewEmpty.setVisibility(View.GONE);
                            setContent();
                        } else {
                            Toast.makeText(context, "暂时没有商品", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }
                } else {
                    if ("mg.created".equals(str)) {//新品上架  热门商品
                        hotModelList = new ArrayList<OneGoodsModel>();
                    } else {
                        if (data.data != null) {
                            AllModelList = new ArrayList<OneGoodsModel>();
                        }
                    }
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.QUERYSHOPIDTOLISTGOODS);
    }

    /**
     * 显示window
     */

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
