package com.cadyd.app.ui.fragment.global;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.Findadver;
import com.cadyd.app.model.Key;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.ta.utdid2.android.utils.StringUtils;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.MyListView;
import org.wcy.android.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GlobalHomeFragment extends BaseFragement {
    @Bind(R.id.topedit)
    EditText topedit;

    @Bind(R.id.global_home_scroll)
    PullToRefreshScrollView scrollView;
    @Bind(R.id.global_home_cycle)
    ImageCycleView cycleView;
    @Bind(R.id.cycle_title)
    TextView cycleTitle;
    private List<String> urlsImage = new ArrayList<>();
    private List<String> ImageTitle = new ArrayList<>();
    private List<String> ImageUrl = new ArrayList<>();

    @Bind(R.id.global_home_type)
    NoScrollGridView typeGridView;

    @Bind(R.id.global_home_list)
    MyListView listView;
    private List<Findadver> findadverList = new ArrayList<>();

    private int alphaMax = 160;
    @Bind(R.id.title_layout)//大标题
            RelativeLayout title_bg;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;

    CommonAdapter<Key> addapterGridview;//分类
    CommonAdapter<Findadver> adapter;//列表

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_global_home);
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

        getImages();
        getType();
    }

    //获得广告
    private void getImages() {
        Map<String, Object> map = new HashMap<>();
        map.put("position", "Q101,Q102,Q103,Q104,Q105");
        ApiClient.send(activity, JConstant.FINDADVER_, map, true, Findadver.getType(), new DataLoader<List<Findadver>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<Findadver> data) {

                final List<Findadver> findadvers = data;
                for (int i = 0; i < findadvers.size(); i++) {
                    if ("Q101".equals(findadvers.get(i).positionid)) {
                        urlsImage.add(findadvers.get(i).imgurl);
                        ImageTitle.add(findadvers.get(i).name);
                        ImageUrl.add(findadvers.get(i).url);
                    } else {
                        findadverList.add(findadvers.get(i));
                    }
                }

                if (urlsImage.size() <= 0) {
                    cycleView.setBackgroundResource(R.mipmap.adv_top);
                } else {
                    //广告图片单击事件
                    cycleView.setImageResources(urlsImage, new ImageCycleView.ImageCycleViewListener() {
                        @Override
                        public void onImageClick(int position, View imageView) {
                            // TODO 单击图片处理事件
                            if (!StringUtils.isEmpty(ImageUrl.get(position))) {
                                Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                                intent.putExtra("shopId", ImageUrl.get(position));
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void carousel() {
                        }
                    }, R.mipmap.adv_top);
                    cycleTitle.setText(ImageTitle.get(0));
                    //轮播变化监听
                    cycleView.setPageChangeListener(new TowObjectParameterInterface() {
                        @Override
                        public void Onchange(int type, int postion, Object object) {
                            if (ImageTitle != null && urlsImage != null && cycleTitle != null) {
                                cycleTitle.setText(ImageTitle.get(postion % urlsImage.size()));
                            }
                        }
                    });
                }

                //列表图片事件
                adapter = new CommonAdapter<Findadver>(activity, findadverList, R.layout.global_home_item) {
                    @Override
                    public void convert(ViewHolder helper, final Findadver item) {
                        ImageView imageView = helper.getView(R.id.image);
                        ApiClient.displayImage(item.imgurl, imageView);
                        helper.setText(R.id.title, item.name);
                        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO 广告图片处理事件
                                if (!StringUtils.isEmpty(item.url)) {
                                    Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                                    intent.putExtra("shopId", item.url);
                                    startActivity(intent);
                                }
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

    private void getType() {
        ApiClient.send(activity, JConstant.GOODSGLOBAL_, null, Key.getType(), new DataLoader<List<Key>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<Key> data) {
                if (data != null && data.size() > 0) {
                    addapterGridview = new CommonAdapter<Key>(activity, data, R.layout.gridview_home_topmenu_item) {
                        @Override
                        public void convert(ViewHolder helper, final Key item) {
                            helper.setText(R.id.name, item.name);
                            ImageView menuImage = helper.getView(R.id.image);
                            ApiClient.displayImageNoCache(item.imgurl, menuImage, R.mipmap.goods_type_ico);
                            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(activity, CommonActivity.class);
                                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_TYPE);
                                    intent.putExtra("mid", item.menuid);
                                    intent.putExtra("Append", "isglobal=0");
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    typeGridView.setAdapter(addapterGridview);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.GOODSGLOBAL_);
    }


    @OnClick(R.id.back)
    public void onBack(View view) {
        finishActivity();
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
        ApiClient.cancelRequest(JConstant.FINDADVER_);
        ApiClient.cancelRequest(JConstant.GOODSGLOBAL_);
    }
}
