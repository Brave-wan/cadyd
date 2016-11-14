package com.cadyd.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.AttentionUserInfo;
import com.cadyd.app.model.AttentionUserInfoListDataModel;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.utils.CircleImageView;
import com.cadyd.app.utils.EmptyRecyclerView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.RequestBody;

public class AttentionFragment extends BaseFragement {

    @Bind(R.id.textViewMessage)
    TextView textView;
    @Bind(R.id.recyclerView)
    PullToRefreshListView pullToRefreshListView;

    private List<AttentionUserInfo> list = new ArrayList<>();
    private CommonAdapter<AttentionUserInfo> adapter;

    private String userId;//用户id

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("userId", "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_attention_or_fans);
    }

    @Override
    protected void initView() {
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                getAttentListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                getAttentListData();
            }
        });
        setContent();
    }

    private void setContent() {
        if (adapter == null) {
            adapter = new CommonAdapter<AttentionUserInfo>(activity, list, R.layout.attention_and_fans_item) {
                @Override
                public void convert(ViewHolder helper, final AttentionUserInfo item) {
                    if (item == null) return;
                    CircleImageView imageView = (CircleImageView) helper.getConvertView().findViewById(R.id.image);
                    ApiClient.displayImage(item.getHeadImageUrl(), imageView, R.mipmap.user_default);
                    helper.setText(R.id.name, item.getNickName());
                    TextView textview = helper.getView(R.id.check_box);
                    if (item.getFocusStatus() == 1) {//关注
                        textview.setTag(true);
                        ChangeText(textview, true);
                    } else if (item.getFocusStatus() == 2) {//未关注
                        textview.setTag(false);
                        ChangeText(textview, false);
                    }
                    /**关注和取消关注*/
                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView text = (TextView) v;
                            if ((boolean) text.getTag()) {
                                //取消关注
                                cancelFoucs(item.getUserId(), text, position);
                            } else {
                                //进行关注
                                userAddAttention(item.getUserId(), text, position);
                            }
                        }
                    });
                }
            };
            pullToRefreshListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        getAttentListData();
    }

    private void ChangeText(TextView textView, boolean type) {
        if (type) {
            textView.setTag(true);
            textView.setText("已关注");
            textView.setTextColor(getResources().getColor(R.color.Orange));
            textView.setBackgroundResource(R.drawable.oval_yellow_transparent);
        } else {
            textView.setTag(false);
            textView.setText(" +关注");
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.oval_yellow_untransparent);
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private SubscriberOnNextListener mOnNextListener;


    /**
     * 关注用户
     *
     * @param userId
     */
    public void userAddAttention(String userId, final TextView text, final int postion) {

        MyApplication application = (MyApplication) getActivity().getApplication();
        if (userId.equals(application.model.id)) {//如果相等则为自己的个人中心
            toast("不能自己关注自己");
            return;
        }

        mOnNextListener = new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo info) {
                Log.i("wan", "关注用户成功");
                list.get(postion).setFocusStatus(1);
                ChangeText(text, true);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onLogicError(String msg) {

            }
        };

        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getUserAttention(new ProgressSubscriber<BaseRequestInfo>(mOnNextListener, activity), body);
    }

    /**
     * 取消关注
     */
    private void cancelFoucs(String userId, final TextView text, final int postion) {
        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().doCancelFoucs(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                Log.i("wan", "取消关注用户成功");
                list.get(postion).setFocusStatus(2);
                ChangeText(text, false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity), body);
    }

    private int pageIndex = 1;

    /**
     * 关注列表
     */
    public void getAttentListData() {
        mOnNextListener = new SubscriberOnNextListener<BaseRequestInfo<AttentionUserInfoListDataModel>>() {
            @Override
            public void onNext(BaseRequestInfo<AttentionUserInfoListDataModel> info) {
                Log.i("wan", "获取用户关注列表");
                pullToRefreshListView.onRefreshComplete();
                if (pageIndex == 1) {
                    list.clear();
                }
                if (info != null) {
                    if (info.getData().getData() == null || info.getData().getData().size() <= 0) {
                        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    list.addAll(info.getData().getData());
                    adapter.notifyDataSetChanged();
                }
                //如果最终没有数据则当前列表为空
                if (list.size() <= 0) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onLogicError(String msg) {

            }
        };
        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", "10");
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getAttentionList(new ProgressSubscriber<BaseRequestInfo<AttentionUserInfoListDataModel>>(mOnNextListener, getActivity()), body);
    }
}
