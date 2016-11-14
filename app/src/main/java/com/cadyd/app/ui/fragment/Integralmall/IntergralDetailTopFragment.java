package com.cadyd.app.ui.fragment.Integralmall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.comm.MyShare;
import com.cadyd.app.model.Good;
import com.cadyd.app.model.GoodDetail;
import com.cadyd.app.model.PicInfo;
import com.cadyd.app.ui.activity.IntegralGoodsDetailActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.*;
import com.cadyd.app.ui.view.adapter.IntegralAdapter;
import com.cadyd.app.ui.view.adapter.MyViewPagerAdapter;
import com.cadyd.app.ui.view.control.PageControl;
import com.cadyd.app.ui.view.guide.ImageCycleView;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城商品详情
 */
public class IntergralDetailTopFragment extends BaseFragement {

    private String GoodsId;

    private IntegralGoodsDetailActivity GetActivity;
    @Bind(R.id.custScrollView)
    CustScrollView csv;
    private List<String> urlsImage = new ArrayList<>();
    @Bind(R.id.guid_top_view)
    ImageCycleView cycleView;
    @Bind(R.id.goods_title)
    TextView title;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.freight)
    TextView freight;//运费
    @Bind(R.id.exchange)
    TextView exchange;//已兑换
    @Bind(R.id.stock)
    TextView stock;//库存
    @Bind(R.id.check_number)
    NEAddAndSubView checkNumber;
    private static final float APP_PAGE_SIZE = 4.0f;
    private PageControl pageControl;
    @Bind(R.id.myviewpager)
    WrapContentHeightViewPager viewPager;
    @Bind(R.id.myviewpager_control)
    LinearLayout group;
    private LayoutInflater inflater;
    public GoodDetail goodDetail;
    public int number = 1;
    private AlertAddSubConfirmation alertAddSubConfirmation;

    public static IntergralDetailTopFragment newInstance(String id) {
        IntergralDetailTopFragment newFragment = new IntergralDetailTopFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            GoodsId = args.getString("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_intergral_detail_top);
    }

    @Override
    protected void initView() {
        checkNumber.setEnabled(false);
        inflater = LayoutInflater.from(activity);
        GetActivity = (IntegralGoodsDetailActivity) getActivity();
        csv.setScrollchanged(new CustScrollView.Scrollchanged() {
            @Override
            public void onScrollChanged(int y) {
                GetActivity.onBgTitle(y);
            }
        });
        getGoods();
        loadingRecommend();
    }

    @OnClick(R.id.share_btn)
    public void onSend(View view) {
        if (goodDetail != null && goodDetail.goods != null) {
            MyShare.showShare(activity, "第一点", goodDetail.mallGoods.title, "http://home.cadyd.com/cadyd_user.apk", goodDetail.goods.thumb);
        }

    }

    //获得商品详情
    private void getGoods() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", GoodsId);//"4f93e8c11f0643f6bf3a3d89e1322a79"
        ApiClient.send(activity, JConstant.GETBYID, map, GoodDetail.class, new DataLoader<GoodDetail>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(GoodDetail data) {
                if (data == null) {
                    return;
                }
                goodDetail = data;
                StringBuffer buffer;
                int start;
                int end;
                //商品图片
                if (data.picList != null) {
                    for (PicInfo pic : data.picList) {
                        if (goodDetail.goods.thumb == null) {
                            goodDetail.goods.thumb = pic.path;
                        }
                        urlsImage.add(pic.path);
                    }
                    if (urlsImage.size() > 0) {
                        cycleView.setImageResources(urlsImage, new ImageCycleView.ImageCycleViewListener() {
                            @Override
                            public void onImageClick(int position, View imageView) {
                            }

                            @Override
                            public void carousel() {
                            }
                        }, R.mipmap.goods_top_defalut);
                    }
                }
                title.setText(data.mallGoods.title);
                buffer = new StringBuffer();
                buffer.append(data.mallGoods.integral == null ? 0 : data.mallGoods.integral);
                if (!StringUtil.hasText(data.mallGoods.price.toString()) || "0".equals(data.mallGoods.price.toString())) {
                    price.setText(buffer.toString());
                } else {
                    buffer.append("+");
                    start = buffer.length();
                    buffer.append("￥");
                    end = buffer.length();
                    buffer.append(data.mallGoods.price.toString());//钱
                    price.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, getResources().getColor(R.color.text_primary_gray)));
                }
                buffer = new StringBuffer("运费：");
                start = buffer.length();
                buffer.append(StringUtil.hasText(data.freight.rmoney) ? data.freight.rmoney : "0");
                end = buffer.length();
                freight.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, getResources().getColor(R.color.red)));//运费
                buffer = new StringBuffer("已兑换：");
                start = buffer.length();
                buffer.append(data.mallGoods.bought);
                end = buffer.length();
                exchange.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, getResources().getColor(R.color.red)));//已兑换
                buffer = new StringBuffer("库存：");
                start = buffer.length();
                buffer.append(data.mallGoods.number);
                end = buffer.length();
                stock.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, getResources().getColor(R.color.red)));//库存

                checkNumber.setNum(1);
                checkNumber.setMaxNum(data.mallGoods.number);
                //数量选择监听
                checkNumber.setOnNumChangeListener(new NEAddAndSubView.OnNumChangeListener() {
                    @Override
                    public void onNumChange(View view, int bnum, int num) {
                        number = num;
                    }
                });
                checkNumber.editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alertAddSubConfirmation == null) {
                            alertAddSubConfirmation = new AlertAddSubConfirmation(activity, "确定", "取消");
                            alertAddSubConfirmation.show(checkNumber.getNum(), new AlertAddSubConfirmation.OnClickListeners() {
                                @Override
                                public void ConfirmOnClickListener(int number) {
                                    checkNumber.setNum(number);
                                    alertAddSubConfirmation.hide();
                                }

                                @Override
                                public void CancelOnClickListener() {
                                    alertAddSubConfirmation.hide();
                                }
                            });
                        } else {
                            alertAddSubConfirmation.show(checkNumber.getNum(), new AlertAddSubConfirmation.OnClickListeners() {
                                @Override
                                public void ConfirmOnClickListener(int number) {
                                    checkNumber.setNum(number);
                                    alertAddSubConfirmation.hide();
                                }

                                @Override
                                public void CancelOnClickListener() {
                                    alertAddSubConfirmation.hide();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.GETBYID);
    }

    /**
     * 推荐
     */
    private void loadingRecommend() {
        ApiClient.send(activity, JConstant.FINDNEWGOODSINTEGRAL, null, Good.getType(), new DataLoader<List<Good>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Good> data) {
                if (data != null) {
                    final int PageCount = (int) Math.ceil(data.size() / APP_PAGE_SIZE);
                    Map<Integer, GridView> map = new HashMap<>();
                    for (int i = 0; i < PageCount; i++) {
                        GridView appPage = (GridView) inflater.inflate(R.layout.recomment_gridview, null);
                        final IntegralAdapter adapter = new IntegralAdapter(activity, data, i);
                        appPage.setNumColumns(2);
                        appPage.setAdapter(adapter);
                        map.put(i, appPage);
                    }
                    pageControl = new PageControl(getContext(), group, PageCount);
                    viewPager.setAdapter(new MyViewPagerAdapter(activity, map));
                    viewPager.addOnPageChangeListener(new MyListener());
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDNEWGOODSINTEGRAL);
    }

    class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            pageControl.selectPage(arg0);
        }

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.GETBYID);
        ApiClient.cancelRequest(JConstant.FINDNEWGOODSINTEGRAL);
    }
}
