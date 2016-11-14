package com.cadyd.app.ui.fragment.oneone;

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
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.model.Findadver;
import com.cadyd.app.model.Good;
import com.cadyd.app.model.Key;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import com.cadyd.app.ui.view.guide.ZImageCycleView;
import com.cadyd.app.utils.StringUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.MyListView;
import org.wcy.android.widget.NoScrollGridView;
import org.wcy.common.utils.NumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一乡一物首页
 */
public class OneOneHomeFragment extends BaseFragement {
    @Bind(R.id.topedit)
    EditText topedit;

    @Bind(R.id.one_one_home_scroll)
    PullToRefreshScrollView scrollView;
    @Bind(R.id.one_one_home_cycle)
    ZImageCycleView cycleView;
    private List<Findadver> urlsImage = new ArrayList<>();

    @Bind(R.id.one_one_home_type)
    NoScrollGridView typeGridView;

    @Bind(R.id.one_one_home_type_list)
    NoScrollGridView GridViewtype;

    @Bind(R.id.one_one_home_list)
    MyListView listView;

    private List<Findadver> typeList = new ArrayList<>();
    private List<Findadver> findadverList = new ArrayList<>();

    @Bind(R.id.one_one_content_list)
    NoScrollGridView contentList;//商品内容

    private int alphaMax = 160;
    @Bind(R.id.title_layout)//大标题
            RelativeLayout title_bg;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;

    CommonAdapter<Key> Gridviewaddapter;//分类小的
    CommonAdapter<Findadver> addapterGridview;//分类大的
    CommonAdapter<Findadver> adapter;//列表
    CommonAdapter<Good> goodsAdapter;
    private List<Good> goodList = new ArrayList<>();
    private int index = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_one_one_home);
    }

    @Override
    protected void initView() {
        title_bg.getBackground().setAlpha(0);
        back.getBackground().setAlpha(alphaMax);
        title.setAlpha(1 - alphaMax / 100);
        scrollView.setMode(PullToRefreshBase.Mode.DISABLED);
        scrollView.setOnScrollChange(new PullToRefreshScrollView.ScrollChange() {
            @Override
            public void overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY) {
                MyChange.ChangeHandAlpha(title_bg, back, title, scrollY);
            }
        });

        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                index++;
                loadingGoods();
            }
        });

        getImages();
        loadingGoods();
        getType();
    }

    //获得广告
    private void getImages() {
        Map<String, Object> map = new HashMap<>();
        map.put("position", "X101,X102,X103,X104,X106,X107");
        ApiClient.send(activity, JConstant.FINDADVER_, map, true,Findadver.getType(), new DataLoader<List<Findadver>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<Findadver> data) {

                final List<Findadver> findadvers = data;
                for (int i = 0; i < findadvers.size(); i++) {
                    if ("X101".equals(findadvers.get(i).positionid)) {//轮播广告
                        urlsImage.add(findadvers.get(i));
                    } else if ("X102".equals(findadvers.get(i).positionid)) {//选择类型
                        typeList.add(findadvers.get(i));
                    } else if ("X103".equals(findadvers.get(i).positionid)) {//选择类型
                        typeList.add(findadvers.get(i));
                    } else {//列表
                        findadverList.add(findadvers.get(i));
                    }
                }

                if (urlsImage.size() <= 0) {
                    cycleView.setBackgroundResource(R.mipmap.adv_top);
                } else {
                    //广告图片单击事件
                    cycleView.setImageResources(urlsImage, "imgurl", R.mipmap.adv_top, new ZImageCycleView.ImageCycleViewListener() {
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

                setType();//设置类型选择

                //列表图片事件
                adapter = new CommonAdapter<Findadver>(activity, findadverList, R.layout.one_one_home_item) {
                    @Override
                    public void convert(ViewHolder helper, final Findadver item) {
                        ImageView imageView = helper.getView(R.id.image);
                        ApiClient.displayImage(item.imgurl, imageView);
                        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                                intent.putExtra("shopId", item.url);
                                startActivity(intent);
                            }
                        });
                    }
                };
                listView.setAdapter(adapter);

            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDADVER_);
    }

    private void setType() {

        addapterGridview = new CommonAdapter<Findadver>(activity, typeList, R.layout.one_one_home_type_item) {
            @Override
            public void convert(ViewHolder helper, final Findadver item) {
                ImageView menuImage = helper.getView(R.id.image);
                ApiClient.displayImageNoCache(item.imgurl, menuImage, R.mipmap.goods_type_ico);
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                        intent.putExtra("shopId", item.url);
                        startActivity(intent);
                    }
                });
            }
        };
        typeGridView.setAdapter(addapterGridview);
    }

    private void getType() {
        Map<String, Object> map = new HashMap<>();
//        map.put("type", "country");
        ApiClient.send(activity, JConstant.GOODSCOUNTRY_, null, Key.getType(), new DataLoader<List<Key>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<Key> data) {
                if (data != null && data.size() > 0) {
                    Gridviewaddapter = new CommonAdapter<Key>(activity, data, R.layout.gridview_home_topmenu_item) {
                        @Override
                        public void convert(ViewHolder helper, final Key item) {
                            helper.setText(R.id.name, item.name);
                            ImageView menuImage = helper.getView(R.id.image);
                            ApiClient.displayImageNoCache(item.icon, menuImage, R.mipmap.goods_type_ico);
                            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(activity, CommonActivity.class);
                                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_TYPE);
                                    intent.putExtra("mid", item.id);
                                    intent.putExtra("Append", "isglobal=0");
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    GridViewtype.setAdapter(Gridviewaddapter);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.GOODSCOUNTRY_);
    }

    /**
     * 推荐商品
     */
    private void loadingGoods() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "country");
        map.put("pageIndex", index);
        ApiClient.send(activity, JConstant.GOODSLIST_, map, Good.getType(), new DataLoader<List<Good>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<Good> data) {
                if (data != null) {
                    goodList.addAll(data);
                    if (goodsAdapter == null) {
                        goodsAdapter = new CommonAdapter<Good>(activity, goodList, R.layout.country_goodes_iteam_list) {
                            @Override
                            public void convert(ViewHolder helper, final Good item) {
                                ImageView iv = helper.getView(R.id.image_view);
                                ApiClient.ImageLoadersRounde(item.thumb, iv, 7);
                                TextView tv = helper.getView(R.id.price);
                                StringBuffer sb = new StringBuffer("￥");
                                sb.append(NumberUtil.getString(item.price, 2));
                                tv.setText(sb.toString());
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
                        contentList.setAdapter(goodsAdapter);
                    } else {
                        goodsAdapter.notifyDataSetChanged();
                    }
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.GOODSLIST_);

    }

    @OnClick(R.id.back)
    public void onBack(View view) {
        finishActivity();
    }

    private void RefreshComplete() {
        scrollView.onRefreshComplete();
    }

    public void setTop() {
        topedit.requestFocus();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.GOODSCOUNTRY_);
        ApiClient.cancelRequest(JConstant.FINDADVER_);
        ApiClient.cancelRequest(JConstant.GOODSLIST_);
    }
}
