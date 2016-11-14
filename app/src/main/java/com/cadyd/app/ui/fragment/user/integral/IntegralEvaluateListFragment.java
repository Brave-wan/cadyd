package com.cadyd.app.ui.fragment.user.integral;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.ForPxTp;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.model.GoodsTheSun;
import com.cadyd.app.model.GoodsTheSunData;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城的评论列表
 * Created by xiongmao on 2016/7/18.
 */
public class IntegralEvaluateListFragment extends BaseFragement {

    @Bind(R.id.relative)
    RelativeLayout relativeLayout;
    @Bind(R.id.pull_to_refresh_list_view)
    PullToRefreshListView pullToRefreshListView;

    private int index = 1;
    private List<GoodsTheSunData> dates = new ArrayList<>();
    private CommonAdapter<GoodsTheSunData> commonAdapter;

    private EmptyLayout mEmptyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_integral_evaluate_list, "我的评论", true);
    }

    @Override
    protected void initView() {
        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                dates.clear();
                loading();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                loading();
            }
        });

        loading();
    }

    private void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        ApiClient.send(activity, JConstant.SELECTCOMMENTINTEGRALGOODSBYUSERID, map, GoodsTheSun.class, new DataLoader<GoodsTheSun>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(GoodsTheSun data) {
                pullToRefreshListView.onRefreshComplete();
                if (data != null && data.data != null) {
                    dates.addAll(data.data);
                    setAdapter();
                }
                if (dates.size()<=0){
                    mEmptyLayout.showEmpty();
                }else{
                    mEmptyLayout.showView();
                }
            }

            @Override
            public void error(String message) {
                pullToRefreshListView.onRefreshComplete();
            }
        }, JConstant.SELECTCOMMENTINTEGRALGOODSBYUSERID);
    }

    private void setAdapter() {
        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<GoodsTheSunData>(activity, dates, R.layout.integral_evaluate_list_item) {
                @Override
                public void convert(ViewHolder helper, GoodsTheSunData item) {
                    ImageView imageView = helper.getView(R.id.image);//图片
                    ApiClient.displayImage(item.path, imageView);
                    helper.setText(R.id.title, item.goodsName);//标题
                    if (item.price == null || Double.valueOf(item.price) == 0) {//价格
                        helper.setText(R.id.price, String.valueOf(item.integral));
                    } else {
                        int start;
                        int end;
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(item.integral);
                        start = buffer.length();
                        buffer.append("\t\t￥");
                        end = buffer.length();
                        buffer.append(item.price);
                        helper.setText(R.id.price, MyChange.ChangeTextColor(buffer.toString(), start, end, getResources().getColor(R.color.gray)));
                    }
                    TextView content = helper.getView(R.id.content);//评论内容
                    if (item.content == null) {
                        content.setVisibility(View.GONE);
                    } else {
                        content.setText("评论：" + item.content);
                    }
                    HorizontalScrollView scrollView = helper.getView(R.id.horizontal);//展示的图片
                    LinearLayout layout = helper.getView(R.id.image_layout);
                    layout.removeAllViews();
                    if (item.imgList == null || item.imgList.size() <= 0) {
                        scrollView.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < item.imgList.size(); i++) {
                            layout.addView(NewImageView(item.imgList.get(i).path));//添加展示图片
                        }
                    }
                    helper.setText(R.id.create, item.created);//评论的时间
                }
            };
            pullToRefreshListView.setAdapter(commonAdapter);
        } else {
            commonAdapter.notifyDataSetChanged();
        }
    }

    private ImageView NewImageView(String url) {
        ImageView imageView = new ImageView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ForPxTp.dip2px(activity, 110.0f), ForPxTp.dip2px(activity, 130.0f));
        params.setMargins(ForPxTp.dip2px(activity, 10), 0, 0, 0);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(params);
        // imageView.setTag(i);//设置标签
        ApiClient.displayImage(url, imageView, R.mipmap.defaiut_on);
        //进入详情
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return imageView;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.SELECTCOMMENTINTEGRALGOODSBYUSERID);
    }
}
