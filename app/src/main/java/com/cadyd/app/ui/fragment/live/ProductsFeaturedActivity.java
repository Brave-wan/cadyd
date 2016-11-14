package com.cadyd.app.ui.fragment.live;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cadyd.app.R;
import com.cadyd.app.adapter.ProductsFeaturedAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.ProductsFeaturedInfo;
import com.cadyd.app.utils.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @Description: 主播商品推荐
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class ProductsFeaturedActivity extends DialogFragment implements View.OnClickListener {
    private EmptyRecyclerView listView;
    private ProductsFeaturedAdapter adapter;
    private List<ProductsFeaturedInfo.ProductsFeatBean> list = new ArrayList<>();
    private RelativeLayout rootView;
    private String conversationId;
    private int pageIndex = 1;
    private View live_products_view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            conversationId = bundle.getString("conversationId", "");
        } else {
            conversationId = "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_productsfeatured, null);
        setStyle(STYLE_NO_FRAME, R.style.TranslucentNoTitle);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView(view);
        return view;
    }

    private void initView(View view) {

        WindowManager wm = getActivity().getWindowManager();
        live_products_view = view.findViewById(R.id.live_products_view);
        rootView = (RelativeLayout) view.findViewById(R.id.products_view);
        listView = (EmptyRecyclerView) view.findViewById(R.id.live_products);
        adapter = new ProductsFeaturedAdapter(getActivity(), list, wm);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        listView.setEmptyView(live_products_view);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.products_view:
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getProductsListInfo();
    }

    /**
     * 获得推荐商品列表
     */
    private void getProductsListInfo() {
        Map<Object, Object> map = new HashMap<>();
        map.put("conversationId", conversationId);//房间号
        map.put("pageIndex", pageIndex);//页码
        map.put("pageSize", 10);//分页大小
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getProductsListInfo(new ProgressSubscriber<BaseRequestInfo<ProductsFeaturedInfo>>(new SubscriberOnNextListener<BaseRequestInfo<ProductsFeaturedInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<ProductsFeaturedInfo> info) {
                Log.i("wan", "获得推荐商品列表" + info.toString());
                if (info != null) {
                    list.addAll(info.getData().getData());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), body);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
