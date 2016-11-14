package com.cadyd.app.ui.fragment.mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;

import butterknife.Bind;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.Findadver;
import com.cadyd.app.model.FlagshipStore;
import com.cadyd.app.ui.activity.CommWebViewActivity;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import com.cadyd.app.ui.view.guide.ZImageCycleView;
import com.cadyd.app.utils.StringUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更多旗舰店
 */
public class FlagshipFragmentMore extends BaseFragement {

    @Bind(R.id.pullscrollview)
    PullToRefreshScrollView pullToRefreshScrollView;

    @Bind(R.id.ui_tesco_head)
    ZImageCycleView cycleView;
    private List<String> urlsImage = new ArrayList<>();

    @Bind(R.id.group)
    RadioGroup group;
    @Bind(R.id.nearby)
    RadioButton nearby;

    List<FlagshipStore> flagshipStoreList = new ArrayList<>();

    @Bind(R.id.flagship_store)
    GridView flagship_store;
    CommonAdapter<FlagshipStore> flagshipAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_flagship_fragment_more, "旗舰店", true);
    }

    @Override
    protected void initView() {
        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                index++;
                loadingFlagshipStore();
            }
        });
        final CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.nearby://附近
                        type = 0;
                        break;
                    case R.id.popular://人气
                        type = 1;
                        break;
                    case R.id.sales://销量
                        type = 2;
                        break;
                }
                longitude = city.longitude;
                latitude = city.latitude;
                index = 1;
                flagshipStoreList.clear();
                loadingFlagshipStore();
            }
        });
        nearby.setChecked(true);
        getAdvert();
    }


    //获得广告
    private void getAdvert() {
        CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("city", (StringUtil.hasText(city.id) ? city.id : city.areaid));
        map.put("position", "B101");
        ApiClient.send(activity, JConstant.FINDADVER_, map, Findadver.getType(), new DataLoader<List<Findadver>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Findadver> data) {
                final List<Findadver> findadvers = data;

                if (findadvers == null || findadvers.size() <= 0) {
                    cycleView.setBackgroundResource(R.mipmap.adv_top);
                } else {
//                    for (int i = 0; i < findadvers.size(); i++) {
//                        urlsImage.add(findadvers.get(i).imgurl);
//                    }
                    //广告图片单击事件
                    cycleView.setImageResources(findadvers, "imgurl", R.mipmap.adv_top, new ZImageCycleView.ImageCycleViewListener() {
                        @Override
                        public void onImageClick(int position, Object object, View imageView) {
                            Findadver findadver = (Findadver) object;
                            if (!StringUtils.isEmpty(findadver.url)) {
                                Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                                intent.putExtra("shopId", findadver.url);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void carousel() {

                        }
                    });
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDADVER_);
    }

    /**
     * 加载旗舰店
     *
     * @remark:查询推荐店铺,分别根据人气,销量,附近距离
     * @date :2016年6月23日
     * @param:type =0,根据距离查询需要传经纬度
     * @param:type =1  根据收藏查询店铺
     * @param:type =2  根据销量查询店铺
     * @param:longitude 经度  latitude 纬度  参数必须传可以传null 后台需要达到通用的效果
     */

    private int type;
    private String longitude = null;
    private String latitude = null;
    private int index = 1;

    private void loadingFlagshipStore() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);//0.位置加经纬度 1.收藏 2.销量
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("pageIndex", index);

        ApiClient.send(activity, JConstant.QUERYPOPULARITY_, map, true, FlagshipStore.getType(), new DataLoader<List<FlagshipStore>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<FlagshipStore> data) {
                if (data != null) {
                    flagshipStoreList.addAll(data);
                    if (flagshipAdapter == null) {
                        flagshipAdapter = new CommonAdapter<FlagshipStore>(activity, flagshipStoreList, R.layout.gridview_home_flagship_store_item) {
                            @Override
                            public void convert(ViewHolder helper, final FlagshipStore item) {
                                helper.setText(R.id.name, item.name);
                                StringBuffer sb = new StringBuffer();
                                TextView tv = helper.getView(R.id.collect);
                                SpannableStringBuilder builder = null;
                                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                                int leng;
                                switch (type) {
                                    case 0://附近
                                        sb.append(item.distance);
                                        leng = String.valueOf(item.distance).length();
                                        builder = new SpannableStringBuilder(sb.toString());
                                        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                                        builder.setSpan(redSpan, 0, leng, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        break;
                                    case 1://人气
                                        sb.append(item.collectNum);
                                        leng = sb.length();
                                        sb.append("人收藏");
                                        builder = new SpannableStringBuilder(sb.toString());
                                        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                                        builder.setSpan(redSpan, 0, leng, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        break;
                                    case 2://销量
                                        sb.append(item.salenum);
                                        leng = sb.length();
                                        sb.append("人消费");
                                        builder = new SpannableStringBuilder(sb.toString());
                                        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                                        builder.setSpan(redSpan, 0, leng, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        break;
                                }
                                tv.setText(builder);
                                ImageView menuImage = helper.getView(R.id.image);
                                ApiClient.displayImage(item.logo, menuImage);
                                View view = helper.getConvertView();
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (item.id != null) {
                                            Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                                            intent.putExtra("shopId", item.id);
                                            intent.putExtra("shopName", item.name);
                                            startActivity(intent);
                                        } else {
                                            toast("该官方旗舰店尚未开通");
                                        }
                                    }
                                });
                            }
                        };
                        flagship_store.setAdapter(flagshipAdapter);
                    } else {
                        flagshipAdapter.notifyDataSetChanged();
                    }
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.QUERYPOPULARITY_);

    }

    private void RefreshComplete() {
        pullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.FINDADVER_);
        ApiClient.cancelRequest(JConstant.QUERYPOPULARITY_);
    }
}
