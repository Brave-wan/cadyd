package com.cadyd.app.ui.fragment.unitary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.OneForTwoAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.activity.CommWebViewActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.user.TheSunFragment;
import com.cadyd.app.ui.view.TimeTextView;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.wcy.android.widget.MyListView;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAnnouncedDetailsFragment extends BaseFragement {

    private NewAnnounce newAnnounce;

    public static NewAnnouncedDetailsFragment newInstance(NewAnnounce newAnnounce) {
        NewAnnouncedDetailsFragment newFragment = new NewAnnouncedDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("newAnnounce", newAnnounce);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    private int pageindext = 1;
    @Bind(R.id.ui_one_yuan_details_PullToRefreshScrollView)
    PullToRefreshScrollView scroView;
    //顶部轮播图
    @Bind(R.id.ui_one_yuan_details_cycview)
    ImageCycleView cycleView;
    @Bind(R.id.ui_one_yuan_details_title)
    TextView title;
    @Bind(R.id.announced_money)
    TextView money;
    //图文详情
    @Bind(R.id.ui_one_yuang_details_record)
    MyListView listView;
    @Bind(R.id.timeTextView)
    TimeTextView mTimeTextView;

    private List<String> urlsImage;
    private QueryBaseRecord queryBaseRecords;
    private List<QueryBaseRecordData> queryBaseRecordDatas = new ArrayList<>();
    private OneForTwoAdapter adapter;

    @Bind(R.id.title)
    TextView bigTitle;
    private int alphaMax = 160;
    @Bind(R.id.title_layout)//大标题
            RelativeLayout title_bg;
    @Bind(R.id.back)
    ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            newAnnounce = (NewAnnounce) args.getSerializable("newAnnounce");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.new_announced_details_fragment);
    }

    @Override
    protected void initView() {
        scroView.setMode(PullToRefreshBase.Mode.BOTH);
        scroView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                queryBaseRecordDatas.clear();
                pageindext = 1;
                initGetDetails();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindext++;
                QueryBaseRecord();
            }
        });

        back.getBackground().setAlpha(alphaMax);
        bigTitle.setAlpha(1 - alphaMax / 100);
        scroView.setOnScrollChange(new PullToRefreshScrollView.ScrollChange() {
            @Override
            public void overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY) {
                MyChange.ChangeHandAlpha(title_bg, back, bigTitle, scrollY);
            }
        });

        initGetDetails();
    }

    @OnClick(R.id.back)
    public void onBack(View view) {
        finishActivity();
    }

    private ProductDetails productDetails;

    private void initGetDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("hastimes", newAnnounce.seq);
        map.put("tbid", newAnnounce.tbid);

        ApiClient.send(activity, JConstant.GROUPBUYANNOUNCED_, map, ProductDetails.class, new DataLoader<ProductDetails>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(ProductDetails data) {
                productDetails = data;

                //设置展示图片
                urlsImage = new ArrayList<String>();
                if (productDetails.baseInfo.imgList == null || productDetails.baseInfo.imgList.size() <= 0) {
                    cycleView.setBackgroundResource(R.mipmap.adv_top);
                } else {
                    for (int i = 0; i < productDetails.baseInfo.imgList.size(); i++) {
                        urlsImage.add(productDetails.baseInfo.imgList.get(i).path);
                    }
                    //广告图片单击事件
                    cycleView.scaleType = ImageView.ScaleType.FIT_CENTER;
                    cycleView.setImageResources(urlsImage, new ImageCycleView.ImageCycleViewListener() {
                        @Override
                        public void onImageClick(int position, View imageView) {
                            // TODO 单击图片处理事件
                        }

                        @Override
                        public void carousel() {
                        }
                    },R.mipmap.adv_top);
                }
                title.setText(productDetails.baseInfo.title);
                money.setText("￥" + productDetails.baseInfo.price);
                if (StringUtil.secToTime(productDetails.baseInfo.countdown) != null) {
                    //特卖倒计时开始
                    mTimeTextView.setTimes(StringUtil.secToTime(productDetails.baseInfo.countdown));
                    //已经在倒计时的时候不再开启计时
                    if (!mTimeTextView.isRun()) {
                        mTimeTextView.run();
                    }
                } else {
                    mTimeTextView.setText("正在计算...");
                }
                QueryBaseRecord();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.GROUPBUYANNOUNCED_);
    }


    //往期揭晓
    @OnClick(R.id.ui_one_yuan_details_ToAnnounce)
    public void onToAnnounce(View view) {
        ToAnnounceFragment announceFragment = ToAnnounceFragment.newInstance(productDetails.baseInfo.tbid);
        announceFragment.HASRIME = Integer.valueOf(productDetails.baseInfo.hastimes);
        announceFragment.newHasTime = productDetails.baseInfo.hastimes;
        replaceFragment(R.id.common_frame, announceFragment);
    }

    //图文详情
    @OnClick(R.id.ui_one_yuan_details_Graphic_details)
    public void onGraphicDetails(View view) {
        Intent intent = new Intent(activity, CommWebViewActivity.class);
        intent.putExtra("url", JConstant.GRAPHICDETAILS + newAnnounce.productid);
        activity.startActivity(intent);
    }

    //晒单
    @OnClick(R.id.ui_one_yuan_details_sun)
    public void onTheSun(View view) {
        TheSunFragment theSunFragment = new TheSunFragment();
        theSunFragment.ID = newAnnounce.productid;
        theSunFragment.Title = "商品晒单";
        replaceFragment(R.id.common_frame, theSunFragment);
    }


    //商品的乐购记录
    private void QueryBaseRecord() {
        Map<String, Object> map = new HashMap<>();
        map.put("tbId", newAnnounce.tbid);//抢购商品主键
        map.put("pageIndex", pageindext);//当前页

        ApiClient.send(activity, JConstant.QUERYPARTICIPATIONRECORD_, map, QueryBaseRecord.class, new DataLoader<QueryBaseRecord>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(QueryBaseRecord data) {
                scroView.onRefreshComplete();
                queryBaseRecords = data;
                queryBaseRecordDatas.addAll(queryBaseRecords.data);

                if (adapter == null) {
                    adapter = new OneForTwoAdapter(activity, queryBaseRecordDatas);
                    listView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String message) {
                scroView.onRefreshComplete();
            }
        }, JConstant.QUERYPARTICIPATIONRECORD_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.GROUPBUYANNOUNCED_);
        ApiClient.cancelRequest(JConstant.QUERYPARTICIPATIONRECORD_);
    }
}
