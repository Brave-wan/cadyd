package com.cadyd.app.ui.fragment.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.ProductsFeaturedInfo;
import com.cadyd.app.model.SendDataMsgInfo;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @Description: 边买边看
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class BuyWatchingFragment extends Fragment {
    private PullToRefreshListView mListView;
    private BuyWatchAdapter mAdapter;

    private List<String> list = new ArrayList<>();
    private String ConversationID = null;
    private View liveDetailsView;
    private TextView liveDetails_msg;
    private List<ProductsFeaturedInfo.ProductsFeatBean> mBuyWatchingInfoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buywatching, null);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ConversationID = bundle.getString("roomId");
        }
        initView(view);
        return view;
    }


    private void initView(View view) {

        liveDetailsView = view.findViewById(R.id.item_liveDetals);
        liveDetails_msg = (TextView) view.findViewById(R.id.liveDetails_msg);
        mListView = (PullToRefreshListView) view.findViewById(R.id.bugwatch_listView);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        getProductsList(ConversationID);
        mListView.setEmptyView(liveDetailsView);
        mAdapter = new BuyWatchAdapter(getActivity(), mBuyWatchingInfoList);
        mListView.setAdapter(mAdapter);
        liveDetails_msg.setText("暂无商品");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBuyWatchingInfoList.get(position);
                Log.i("Wan", "点击商品id" + position);
                ProductsFeaturedInfo.ProductsFeatBean bean = (ProductsFeaturedInfo.ProductsFeatBean) mAdapter.getItem(position);
                if (bean != null) {
                    Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("gid", bean.getProductId());
                    startActivity(intent);
                    EventBus.getDefault().post(new SendDataMsgInfo());
                }

            }
        });
    }

    /**
     * 获取商品列表
     *
     * @param ConversationID
     */
    public void getProductsList(@NonNull String ConversationID) {

        Map<Object, Object> map = new HashMap<>();
        map.put("conversationId", ConversationID);
        map.put("pageIndex", 1);//页码
        map.put("pageSize", 10);//分页大小
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getProductsListInfo(new ProgressSubscriber<BaseRequestInfo<ProductsFeaturedInfo>>(new SubscriberOnNextListener<BaseRequestInfo<ProductsFeaturedInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<ProductsFeaturedInfo> info) {
                mBuyWatchingInfoList = info.getData().getData();
                mAdapter.setData(mBuyWatchingInfoList);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), key);
    }

}
