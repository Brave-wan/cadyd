package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.cadyd.app.AppManager;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.Findadver;
import com.cadyd.app.model.HomeMenu;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.view.BaseLayout;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import org.wcy.android.utils.ActivityUtil;
import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有分类
 */
public class ShopAllClassificationActivity extends BaseActivity {
    @Bind(R.id.guid_top_view)
    ImageCycleView cycleView;
    private List<String> urlsImage = new ArrayList<String>();

    @Bind(R.id.topMenu)
    GridView topMenu;
    CommonAdapter<HomeMenu> addapterGridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtil.hideTitleBar(this);
        ActivityUtil.setScreenVertical(this);
        AppManager.getAppManager().addActivity(this);
        layout = new BaseLayout(this, R.layout.activity_shop_all_classification);
        layout.title_relative.getBackground().setAlpha(255);
        layout.titleText.setText("全部分类");
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        setContentView(layout);
        ButterKnife.bind(this);

        getAdvert();
        loadingMenu();
    }

    /**
     * 顶部轮播图
     */
    private void getAdvert() {
        CityInfo city = PreferenceUtils.getObject(activity, CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("city", (StringUtil.hasText(city.id) ? city.id : city.areaid));
        map.put("position", "A101");

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
                    for (int i = 0; i < findadvers.size(); i++) {
                        urlsImage.add(findadvers.get(i).imgurl);
                    }
                    //广告图片单击事件
                    cycleView.setImageResources(urlsImage, new ImageCycleView.ImageCycleViewListener() {
                        @Override
                        public void onImageClick(int position, View imageView) {
                            // TODO 单击图片处理事件
                        }

                        @Override
                        public void carousel() {
                        }
                    }, R.mipmap.adv_top);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDADVER_);
    }


    private void loadingMenu() {
        ApiClient.send(activity, JConstant.QUERYHOMEMENUALL_, null, HomeMenu.getType(), new DataLoader<List<HomeMenu>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<HomeMenu> data) {
                if (data != null) {
                    if (data != null && data.size() > 0) {
                        addapterGridview = new CommonAdapter<HomeMenu>(activity, data, R.layout.gridview_home_topmenu_item) {
                            @Override
                            public void convert(ViewHolder helper, final HomeMenu item) {
                                helper.setText(R.id.name, item.name);
                                ImageView menuImage = helper.getView(R.id.image);
                                ApiClient.displayImageNoCache(item.icon, menuImage, R.mipmap.goods_type_ico);
                                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (item.id.equals("3418aad9d3d74c4dbfc2aeab113167af")) {//商城
                                            Intent intent = new Intent(activity, MallMainActivity.class);
                                            activity.startActivity(intent);
                                        } else if (item.id.equals("c86d8136721b4ab598590139882ba858")) {//一元乐购
                                            Intent intent = new Intent(activity, OneYuanManagerActivity.class);
                                            startActivity(intent);
                                        } else if (item.id.equals("3b2dd77d882b4d078f090667d37d21a8")) {//全球购
                                            Intent intent = new Intent(activity, GlobalManagerActivity.class);
                                            startActivity(intent);
                                        } else if (item.id.equals("d69429bd4a3d431689257cda443cdf8d")) {//一乡一物
                                            Intent intent = new Intent(activity, OneOneManagerActivity.class);
                                            startActivity(intent);
                                        }

                                    }
                                });

                            }
                        };
                        topMenu.setAdapter(addapterGridview);
                    }
                }
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.QUERYHOMEMENUALL_);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.FINDADVER_);
        ApiClient.cancelRequest(JConstant.QUERYHOMEMENUALL_);
    }
}
