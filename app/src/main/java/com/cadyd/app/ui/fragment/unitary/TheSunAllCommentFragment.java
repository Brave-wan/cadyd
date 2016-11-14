package com.cadyd.app.ui.fragment.unitary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.GoodsTheSun;
import com.cadyd.app.model.GoodsTheSunData;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.ToastView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全部评论
 */
public class TheSunAllCommentFragment extends BaseFragement {

    @Bind(R.id.content)
    EditText content;

    private int index = 1;
    private List<GoodsTheSunData> goodsTheSunDataList = new ArrayList<>();

    @Bind(R.id.pullList)
    PullToRefreshListView pullToRefreshListView;
    private CommonAdapter<GoodsTheSunData> adapter;

    private String id;


    public static TheSunAllCommentFragment newInstance(String id) {
        TheSunAllCommentFragment newFragment = new TheSunAllCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", id);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            id = args.getString("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_the_sun_all_comment, "全部评论", true);
    }

    @Override
    protected void initView() {
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                goodsTheSunDataList.clear();
                initHttp();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                initHttp();
            }
        });
        initHttp();
    }

    //发送评论
    @OnClick(R.id.send)
    public void onSend(View view) {
        String str = content.getText().toString();
        if ("".equals(str) || str == null) {
            ToastView.show(activity, "输入内容不能为空");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("des", str);
        map.put("listid", id);
        ApiClient.send(activity, JConstant.SAVECOMMENTS_, map, true, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                content.setText("");
                index = 1;
                goodsTheSunDataList.clear();
                initHttp();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SAVECOMMENTS_);
    }

    //获取评论
    private void initHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("listid", id);//晒单主键
        map.put("pageIndex", index);
        ApiClient.send(activity, JConstant.QUERYCOMMENTSBYPAGE_, map, GoodsTheSun.class, new DataLoader<GoodsTheSun>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(GoodsTheSun data) {
                pullToRefreshListView.onRefreshComplete();
                goodsTheSunDataList.addAll(data.data);
                if (adapter == null) {
                    adapter = new CommonAdapter<GoodsTheSunData>(activity, goodsTheSunDataList, R.layout.the_sun_all_comment_item) {
                        @Override
                        public void convert(ViewHolder helper, GoodsTheSunData item) {
                            helper.setText(R.id.name, item.name);
                            helper.setText(R.id.content, item.des);
                            helper.setText(R.id.time, item.createTime);
                            ImageView imageView = helper.getView(R.id.photo);
                            ApiClient.displayImage(item.photo, imageView, R.mipmap.user_default);
                        }
                    };
                    pullToRefreshListView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void error(String message) {
                pullToRefreshListView.onRefreshComplete();
            }
        }, JConstant.QUERYCOMMENTSBYPAGE_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SAVECOMMENTS_);
        ApiClient.cancelRequest(JConstant.QUERYCOMMENTSBYPAGE_);
    }
}
