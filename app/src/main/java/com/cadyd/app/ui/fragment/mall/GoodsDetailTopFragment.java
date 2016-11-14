package com.cadyd.app.ui.fragment.mall;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import com.cadyd.app.comm.MyShare;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.gaode.route.RouteActivity;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.CustScrollView;
import com.cadyd.app.ui.view.WrapContentHeightViewPager;
import com.cadyd.app.ui.view.adapter.AppAdapter;
import com.cadyd.app.ui.view.adapter.MyViewPagerAdapter;
import com.cadyd.app.ui.view.control.PageControl;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import com.cadyd.app.ui.window.CerificationPopupWindow;
import com.cadyd.app.ui.window.SalesPopupWindow;
import com.cadyd.app.ui.window.ShopPreferPopupWindow;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import com.cadyd.app.ui.view.FixGridLayout;

import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品详情顶部
 *
 * @author wcy
 */
public class GoodsDetailTopFragment extends BaseFragement implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.source)
    TextView source;
    @Bind(R.id.guid_top_view)
    ImageCycleView guid_top_view;
    @Bind(R.id.goods_title)
    TextView goods_title;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.original)
    TextView original;
    @Bind(R.id.express_name)
    TextView express_name;
    @Bind(R.id.salenum)
    TextView salenum;
    @Bind(R.id.integral)
    TextView integral;
    @Bind(R.id.certification)
    FixGridLayout certification;
    @Bind(R.id.shop_name)
    TextView shop_name;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.comment_listview)
    ListView comment_listview;
    @Bind(R.id.comment_num)
    TextView comment_num;
    @Bind(R.id.comment_image_number)
    TextView comment_image_number;
    @Bind(R.id.shop_logo)
    ImageView shop_logo;
    @Bind(R.id.shop_info_name)
    TextView shop_info_name;
    @Bind(R.id.shop_info_des)
    TextView shop_info_des;
    @Bind(R.id.shop_level)
    TextView shop_level;
    @Bind(R.id.shop_totalInter)
    TextView shop_totalInter;
    @Bind(R.id.shop_goods_number)
    TextView shop_goods_number;
    @Bind(R.id.shop_totalNew)
    TextView shop_totalNew;
    @Bind(R.id.custScrollView)
    CustScrollView csv;
    @Bind(R.id.recomment_more_rg)
    RadioGroup recomment_more_rg;
    @Bind(R.id.recomment_more)
    TextView recomment_more;
    @Bind(R.id.choose_layout)
    LinearLayout choose_layout;
    @Bind(R.id.rank_layout)
    LinearLayout rank_layout;
    @Bind(R.id.myviewpager_control)
    LinearLayout group;
    @Bind(R.id.rank_viewpage_control)
    LinearLayout rankGroup;
    @Bind(R.id.myviewpager)
    WrapContentHeightViewPager viewPager;
    @Bind(R.id.rank_viewpage)
    WrapContentHeightViewPager rank_viewpage;
    @Bind(R.id.comment_toast)
    TextView comment_toast;
    @Bind(R.id.goods_service_score)
    TextView goods_service_score;
    @Bind(R.id.service_score)
    TextView service_score;
    @Bind(R.id.logistics_score)
    TextView logistics_score;
    @Bind(R.id.navigation)
    ImageView navigation;
    @Bind(R.id.one_yuan_buy)
    TextView OneYuanBuy;//一元购
    @Bind(R.id.price_notice)
    TextView priceNotice;//降价通知
    @Bind(R.id.preferential)
    TextView preferential;//最高优惠价格
    @Bind(R.id.sales_layout)
    LinearLayout sales_layout;//优惠套装
    @Bind(R.id.score_b)
    TextView score_b;
    @Bind(R.id.score_price)
    TextView score_price;
    @Bind(R.id.integral_number)
    TextView integral_number;
    private PageControl pageControl;
    private PageControl rankPageControl = null;
    private static final float APP_PAGE_SIZE = 4.0f;
    List<String> urlsImage = new ArrayList<String>();
    LayoutInflater inflater;
    private String gid;
    public String source_title;
    GoodsDetailActivity gdActivity;
    CerificationPopupWindow cerificationPopupWindow;
    private String reviews;//评论数

    public static GoodsDetailTopFragment newInstance(String id, String source_title) {
        GoodsDetailTopFragment newFragment = new GoodsDetailTopFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("source_title", source_title);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.gid = args.getString("id");
            this.source_title = args.getString("source_title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        gdActivity = (GoodsDetailActivity) getActivity();
        return setView(inflater, R.layout.fragment_goods_detail_top);
    }


    @Override
    protected void initView() {
        source.setText(source_title);
        guid_top_view.setFocusable(true);
        guid_top_view.setFocusableInTouchMode(true);
        guid_top_view.requestFocus();
        csv.setScrollchanged(new CustScrollView.Scrollchanged() {
            @Override
            public void onScrollChanged(int y) {
                ((GoodsDetailActivity) getActivity()).onBgTitle(y);
            }
        });
        recomment_more_rg.setOnCheckedChangeListener(this);
        reload();
    }

    public void reload() {
        loadingFindgoods();
        CommentToalt();
        loadingRecommend();
    }

    /**
     * 进入商品店铺
     */
    @OnClick({R.id.shop_btn, R.id.shop_button})
    public void onToShop(View view) {
        goToShop();
    }

    public void goToShop() {
        if (goodDetail != null && goodDetail.shopInfo != null && goodDetail.shopInfo.id != null) {
            Intent intent = new Intent(getContext(), ShopHomeActivity.class);
            intent.putExtra("shopId", goodDetail.shopInfo.id);
            intent.putExtra("shopName", goodDetail.shopInfo.name);
            startActivity(intent);
        }
    }

    /**
     * 推荐
     */
    private void loadingRecommend() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "recommend");
        map.put("pageIndex", 1);
        map.put("goodsId", gid);
        ApiClient.send(activity, JConstant.GOODSRECOMMEND_, map, Good.getType(), new DataLoader<List<Good>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Good> data) {
                if (data != null) {
                    final int PageCount = (int) Math.ceil(data.size() / APP_PAGE_SIZE);
                    Map<Integer, GridView> map = new HashMap<Integer, GridView>();
                    for (int i = 0; i < PageCount; i++) {
                        GridView appPage = (GridView) inflater.inflate(R.layout.recomment_gridview, null);
                        final AppAdapter adapter = new AppAdapter(activity, data, i);
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
        }, JConstant.GOODSRECOMMEND_);
    }

    /**
     * 排行榜
     */
    private void loadingRank() {
        if (rankPageControl == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "rank");
            map.put("pageIndex", 1);
            map.put("goodsId", gid);
            ApiClient.send(activity, JConstant.GOODSRECOMMEND_, map, Good.getType(), new DataLoader<List<Good>>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(List<Good> data) {
                    if (data != null) {
                        int PageCount = (int) Math.ceil(data.size() / APP_PAGE_SIZE);
                        Map<Integer, GridView> map = new HashMap<Integer, GridView>();
                        for (int i = 0; i < PageCount; i++) {
                            GridView appPage = (GridView) inflater.inflate(R.layout.recomment_gridview, null);
                            final AppAdapter adapter = new AppAdapter(activity, data, i);
                            appPage.setNumColumns(2);
                            appPage.setAdapter(adapter);
                            map.put(i, appPage);
                        }
                        rankPageControl = new PageControl(getContext(), rankGroup, PageCount);
                        rank_viewpage.setAdapter(new MyViewPagerAdapter(activity, map));
                        rank_viewpage.setOnPageChangeListener(new RankListener());
                    }
                }

                @Override
                public void error(String message) {

                }
            }, JConstant.GOODSRECOMMEND_);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.choose_rb:
                if (rankPageControl != null) {
                    rankPageControl.selectPage(0);
                }
                recomment_more.setText(getResources().getString(R.string.recomment_more));
                choose_layout.setVisibility(View.VISIBLE);
                rank_layout.setVisibility(View.GONE);
                break;
            case R.id.ranking_rb:
                pageControl.selectPage(0);
                recomment_more.setText(getResources().getString(R.string.rank_more));
                choose_layout.setVisibility(View.GONE);
                rank_layout.setVisibility(View.VISIBLE);
                loadingRank();
                break;
        }
    }

    /**
     * gridView 的onItemLick响应事件
     */
    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
        }
    };

    class RankListener implements ViewPager.OnPageChangeListener {

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
            rankPageControl.selectPage(arg0);
        }

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

    List<SaleInfo> mlist;
    public GoodDetail goodDetail;

    /**
     * 商品基本信息
     */
    private void loadingFindgoods() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", gid);
        ApiClient.send(activity, JConstant.FINDGOODS_, map, true, GoodDetail.class, new DataLoader<GoodDetail>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(GoodDetail data) {

                if (getActivity() == null || data == null) {
                    return;
                }

                goodDetail = data;
                //是否有优惠套装
                if (goodDetail.suitNumber == 0) {
                    sales_layout.setVisibility(View.GONE);
                }
                //是否有返利
                if (data.goods.rebatescale == 0) {
                    score_b.setVisibility(View.GONE);
                    score_price.setVisibility(View.GONE);
                } else {
                    score_b.setText("可返利 " + data.goods.rebatescale + "%");
                    score_price.setText(String.valueOf(data.goods.useintegral));
                }
                integral_number.setText("可使用乐购码数量：" + data.goods.luckycodecount);
                goods_title.setText(data.goods.title);
                StringBuffer sbp = new StringBuffer("￥");
                sbp.append(NumberUtil.getString(data.goods.price, 2));
                SpannableStringBuilder builderp = new SpannableStringBuilder(sbp.toString());
                builderp.setSpan(new AbsoluteSizeSpan(30), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builderp.setSpan(new AbsoluteSizeSpan(40), 1, sbp.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builderp.setSpan(new AbsoluteSizeSpan(30), sbp.length() - 2, sbp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                price.setText(builderp);
                StringBuffer sbo = new StringBuffer("实体店价：￥");
                sbo.append(NumberUtil.getString(data.goods.original, 2));
                SpannableStringBuilder buildero = new SpannableStringBuilder(sbo.toString());
                buildero.setSpan(new AbsoluteSizeSpan(30), 5, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                buildero.setSpan(new AbsoluteSizeSpan(40), 6, sbo.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builderp.setSpan(new AbsoluteSizeSpan(30), sbp.length() - 2, sbp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                original.setText(buildero);
                //商品图片
                if (data.picList != null) {
                    for (PicInfo pic : data.picList) {
                        if (goodDetail.goods.thumb == null) {
                            goodDetail.goods.thumb = pic.path;
                        }
                        urlsImage.add(pic.path);
                    }
                    if (urlsImage.size() > 0) {
                        guid_top_view.setImageResources(urlsImage, new ImageCycleView.ImageCycleViewListener() {
                            @Override
                            public void onImageClick(int position, View imageView) {
                            }

                            @Override
                            public void carousel() {
                            }
                        }, R.mipmap.goods_top_defalut);
                    }
                }
                //是否显示一元购
                if (data.goods.islucky == 2) {
                } else {
                    OneYuanBuy.setBackgroundResource(R.drawable.round_dark_gray_untransparent);
                    OneYuanBuy.setEnabled(false);
                }
                //是否已经申请降价通知
                if (data.depState == 0) {//没有
                } else if (data.depState == 1) {
                    priceNotice.setBackgroundResource(R.drawable.round_dark_gray_untransparent);
                    priceNotice.setEnabled(false);
                }
                //
                if (data.certificationList != null && data.certificationList.size() > 0) {
                    certification.setList(data.certificationList);
                    cerificationPopupWindow = new CerificationPopupWindow(activity, data.certificationList);
                }

                ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));
                SpannableStringBuilder salenumb = new SpannableStringBuilder("月销：" + data.goods.saleNum);
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                salenumb.setSpan(redSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                StringBuffer express_s = new StringBuffer("快递：");
                if (data.freight.rmoney != null) {
                    if (NumberUtil.getDouble(data.freight.rmoney) > 0) {
                        express_s.append("￥");
                        express_s.append(NumberUtil.getString(data.freight.rmoney, 2));
                    } else {
                        express_s.append("全国包邮");
                    }
                } else {
                    express_s.append("全国包邮");
                }
                SpannableStringBuilder express_nameb = new SpannableStringBuilder(express_s.toString());
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                express_nameb.setSpan(redSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableStringBuilder integral_b = new SpannableStringBuilder("积分：" + Math.round(Math.floor(data.goods.price.doubleValue())));
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                integral_b.setSpan(redSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                salenum.setText(salenumb);
                express_name.setText(express_nameb);
                integral.setText(integral_b);
                shop_name.setText(data.shopInfo.name);
                address.setText(data.shopInfo.address);
                if (data.saleList != null && data.saleList.size() > 0) {
                    Map<String, SaleInfo> map = new HashMap<>();
                    mlist = new ArrayList<>();
                    for (SaleInfo si : data.saleList) {
                        SaleInfo info;
                        if (map.containsKey(si.aid)) {
                            info = map.get(si.aid);
                        } else {
                            info = new SaleInfo();
                            info.attrname = si.attrname;
                            info.aid = si.aid;
                            map.put(si.aid, info);
                        }
                        if (!info.IsSave(si.vid)) {
                            SaleInfo chile = new SaleInfo();
                            chile.aid = info.aid;
                            chile.attrname = info.attrname;
                            chile.vid = si.vid;
                            chile.attrvname = si.attrvname;
                            info.list.add(chile);
                        }

                    }
                    if (map.size() > 0) {
                        for (String key : map.keySet()) {
                            SaleInfo info = map.get(key);
                            mlist.add(info);
                        }
                    }
                }
                gdActivity.setGoods(data, "", mlist);
                reviews = (StringUtil.hasText(data.commentNum) ? data.commentNum : "0");
                SpannableStringBuilder integral_c = new SpannableStringBuilder("宝贝评价(" + reviews + ")");
                integral_c.setSpan(redSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment_num.setText(integral_c);
                if (data.commentList != null && data.commentList.size() > 0) {
                    CommonAdapter<Comment> adapter = new CommonAdapter<Comment>(activity, data.commentList, R.layout.comment_list_iteam) {
                        @Override
                        public void convert(ViewHolder helper, Comment item) {
                            helper.setText(R.id.user_name, item.uname);
                            helper.setText(R.id.content, item.content);
                            helper.setText(R.id.time, item.created);
                            helper.setText(R.id.salemix, item.salemix);
                            RatingBar rb = helper.getView(R.id.room_ratingbar);
                            rb.setRating(NumberUtil.getInteger(item.startLevel));
                            GridView gv = helper.getView(R.id.comment_images);
                            if (item.imgList != null) {
                                gv.setVisibility(View.VISIBLE);
                                CommonAdapter<PicInfo> cententMneuAdapter = new CommonAdapter<PicInfo>(activity, item.imgList, R.layout.gridview_comment_item) {
                                    @Override
                                    public void convert(ViewHolder helper, PicInfo pic) {
                                        ImageView iv = helper.getView(R.id.image);
                                        ApiClient.displayImage(pic.thumb, iv);
                                    }
                                };
                                gv.setAdapter(cententMneuAdapter);
                            } else {
                                gv.setVisibility(View.GONE);
                            }
                        }
                    };
                    comment_listview.setAdapter(adapter);
                    comment_image_number.setVisibility(View.VISIBLE);
                    comment_toast.setVisibility(View.GONE);
                } else {
                    comment_image_number.setVisibility(View.GONE);
                    comment_toast.setVisibility(View.VISIBLE);
                }
                ApiClient.displayImage(data.shopInfo.logo, shop_logo);
                shop_info_name.setText(data.shopInfo.name);
                ForegroundColorSpan shop_level_fc = new ForegroundColorSpan(getResources().getColor(R.color.text_primary_gray));
                SpannableStringBuilder shop_level_ssb = new SpannableStringBuilder("综合" + data.shopInfo.level);
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                shop_level_ssb.setSpan(shop_level_fc, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                shop_level.setText(shop_level_ssb);
                shop_totalInter.setText(String.valueOf(data.shopInfo.totalInter));
                shop_goods_number.setText(String.valueOf(data.shopInfo.totalGoods));
                shop_totalNew.setText(String.valueOf(data.shopInfo.totalNew));

                /**店铺信息*/
                redSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));
                SpannableStringBuilder gsss = new SpannableStringBuilder("商品 " + data.shopInfo.level + " | " + data.shopInfo.levelString);
                SpannableStringBuilder ss = new SpannableStringBuilder("服务 " + data.shopInfo.serviceLevel + " | " + data.shopInfo.servicelevelString);
                SpannableStringBuilder ls = new SpannableStringBuilder("物流 " + data.shopInfo.logisticsLevel + " | " + data.shopInfo.logisticslevelString);
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                gsss.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ls.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                goods_service_score.setText(gsss);
                service_score.setText(ss);
                logistics_score.setText(ls);

                Summary();
            }

            @Override
            public void error(String message) {
                gdActivity.setGoods(null, message, null);
            }
        }, JConstant.FINDGOODS_);
    }

    /**
     * 降价通知
     */
    private void getSaveNotice(String GoodsId) {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", GoodsId);
        ApiClient.send(activity, JConstant.SAVENOTICE, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                priceNotice.setBackgroundResource(R.drawable.round_dark_gray_untransparent);
                priceNotice.setEnabled(false);
                toastSuccess("通知已保存");
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SAVENOTICE);
    }

    /**
     * 评论数量统计
     */
    private void CommentToalt() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", gid);
        ApiClient.send(activity, JConstant.GOODS_COMMENT_COUNT_, map, Comment.class, new DataLoader<Comment>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Comment data) {
                Message message = new Message();
                message.what = 0123;
                message.obj = data.img;
                handler.sendMessage(message);
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.GOODS_COMMENT_COUNT_);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0123:
                    if (isAdded()) {
                        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));
                        SpannableStringBuilder salenumb = new SpannableStringBuilder("晒图相册(" + (String) msg.obj + ")");
                        salenumb.setSpan(redSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        comment_image_number.setText(salenumb);
                    }
                    break;
            }
        }
    };

    // gdActivity.showPopupWindow();
    @OnClick({R.id.good_comment, R.id.comment_image_number, R.id.share_btn, R.id.recomment_more, R.id.certification,
            R.id.sales_layout, R.id.navigation, R.id.work_shop_coupon, R.id.alliance_coupon, R.id.one_yuan_buy, R.id.price_notice})
    public void OnclickListener(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.good_comment:
                if (!"0".equals(reviews)) {
                    intent = new Intent(activity, CommonActivity.class);
                    intent.putExtra("gid", gid);
                    intent.putExtra("image", false);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_COMMENT);
                    activity.startActivity(intent);
                }
                break;
            case R.id.comment_image_number:
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra("gid", gid);
                intent.putExtra("image", true);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_COMMENT);
                activity.startActivity(intent);
                break;
            case R.id.share_btn://分享
                if (goodDetail != null && goodDetail.goods != null) {
                    MyShare.showShare(activity, "第一点", goods_title.getText().toString(), "http://home.cadyd.com/cadyd_user.apk", goodDetail.goods.thumb);
                }
                break;
            case R.id.recomment_more:
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra("gid", gid);
                if (recomment_more.getText().toString().equals(getResources().getString(R.string.recomment_more))) {
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.RECOMMENT_GOODS);
                } else {
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.RANK_GOODS);
                }
                activity.startActivity(intent);
                break;
            case R.id.certification:
                if (cerificationPopupWindow != null) {
                    cerificationPopupWindow.show(activity);
                }
                break;
            case R.id.sales_layout:
                if (sales != null) {
                    salesPopupWindow = new SalesPopupWindow(activity, sales);
                    salesPopupWindow.setClick(new TowObjectParameterInterface() {
                        @Override
                        public void Onchange(int type, int postion, Object object) {
                            Intent intent = new Intent(activity, CommonActivity.class);
                            intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.SALESPACKAGE);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sale", sales);
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    });
                    salesPopupWindow.show(activity);
                }
                break;
            case R.id.navigation:
                intent = new Intent(getActivity(), RouteActivity.class);
                intent.putExtra("endLat", goodDetail.goods.latitude);
                intent.putExtra("endLong", goodDetail.goods.longitude);
                activity.startActivity(intent);
                break;
            case R.id.work_shop_coupon://优惠券
                ShopPrefer();
                break;
            case R.id.alliance_coupon://联盟优惠券
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.LEAGUEPREFER);
                intent.putExtra("merchanid", goodDetail.shopInfo.merchantId);
                activity.startActivity(intent);
                break;
            case R.id.one_yuan_buy://立即一元购
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.ONE_YUAN_DETAIL);
                intent.putExtra("tbid", goodDetail.goods.id);
                startActivity(intent);
                break;
            case R.id.price_notice:
                getSaveNotice(goodDetail.goods.id);
                break;
        }

    }

    ShopPreferPopupWindow shopPreferPopupWindow;

    private void ShopPrefer() {
        Map<String, Object> map = new HashMap<>();
        map.put("merchantId", goodDetail.shopInfo.merchantId);
        map.put("goodsId", goodDetail.goods.id);
        ApiClient.send(activity, JConstant.PREFER_FIND_, map, true, LeaguePrefer.getType(), new DataLoader<List<LeaguePrefer>>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<LeaguePrefer> data) {
                if (data != null) {
                    if (shopPreferPopupWindow == null) {
                        shopPreferPopupWindow = new ShopPreferPopupWindow(activity, data);
                        shopPreferPopupWindow.show(activity);
                    } else {
                        shopPreferPopupWindow.show(activity);
                    }
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.PREFER_FIND_);

    }


    SalesPopupWindow salesPopupWindow;

    private Sales sales;

    private void Summary() {
        if (salesPopupWindow == null) {
            Map<String, Object> map = new HashMap<>();
            // map.put("goodsId", "e0b817c756b0450a8db48c54afb33d1b");
            map.put("goodsId", goodDetail.goods.id);
            ApiClient.send(activity, JConstant.SUMMARY, map, true, Sales.class, new DataLoader<Sales>() {

                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(final Sales data) {
                    sales = data;
                    if (data != null) {
                        preferential.setText("最高省 " + data.money + "元");
                    }
                }

                @Override
                public void error(String message) {

                }
            }, JConstant.SUMMARY);
        } else {
            salesPopupWindow.show(activity);
        }

    }

    public void replace(Saledynamic saledynamic) {
        StringBuffer sbp = new StringBuffer("网上会员价：￥");
        if (saledynamic != null) {
            sbp.append(saledynamic.price);
        } else {
            sbp.append("0.00");
        }
        SpannableStringBuilder builderp = new SpannableStringBuilder(sbp.toString());
        builderp.setSpan(new AbsoluteSizeSpan(20), 6, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderp.setSpan(new AbsoluteSizeSpan(40), 7, sbp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        price.setText(builderp);
    }

    /**
     * 拨打电话
     */
    @OnClick(R.id.tell_phone)
    public void tellPhone(View view) {
        if (goodDetail != null || goodDetail.goods != null || !StringUtil.hasText(goodDetail.goods.minorphone)) {
            Utils.tellPhone(activity, goodDetail.goods.minorphone, "联系卖家");
        }
    }

    /**
     * 联系卖家
     */
    @OnClick(R.id.contact_seller)
    public void onContactSeller(View view) {
        if (goodDetail != null || goodDetail.goods != null || !StringUtil.hasText(goodDetail.goods.minorphone)) {
            Utils.tellPhone(activity, goodDetail.goods.minorphone, "联系卖家");
        }
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.GOODSRECOMMEND_);
        ApiClient.cancelRequest(JConstant.GOODS_COMMENT_COUNT_);
        ApiClient.cancelRequest(JConstant.FINDGOODS_);
        ApiClient.cancelRequest(JConstant.SAVENOTICE);
        ApiClient.cancelRequest(JConstant.PREFER_FIND_);
        ApiClient.cancelRequest(JConstant.SUMMARY);
        ApiClient.cancelRequest(JConstant.PREFER_RECEIVE_);
    }
}
