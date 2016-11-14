package com.cadyd.app.ui.fragment.unitary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
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
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.OneYuanCalculationDetailsActivity;
import com.cadyd.app.ui.activity.OneYuanDetialWebViewActivity;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.user.TheSunFragment;
import com.cadyd.app.ui.view.ToastView;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.widget.MyListView;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一元购商品详情
 * 一元购往期商品详情
 */

public class OneYuanDetailsFragment extends BaseFragement {
    public String tbid;
    public String newHastimes;
    private boolean isMall = false;

    public static OneYuanDetailsFragment newInstance(String tbid, String newHastimes) {
        OneYuanDetailsFragment newFragment = new OneYuanDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tbid", tbid);
        bundle.putString("newHastimes", newHastimes);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.tbid = args.getString("tbid");
            if (args.getString("newHastimes") == null) {
                isMall = true;
                this.newHastimes = "0";
            } else {
                isMall = false;
                this.newHastimes = args.getString("newHastimes");
            }
        }
    }

    private boolean IsShow = true;
    public int HASRIME = 0;//最新期数
    private ProductDetails details;
    private int pageindext = 1;
    @Bind(R.id.ui_one_yuan_details_PullToRefreshScrollView)
    PullToRefreshScrollView scroView;

    @Bind(R.id.title)
    EditText bigTitle;

    @Bind(R.id.LinearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.ui_one_yuan_details_cycview)
    ImageCycleView cycleView;
    @Bind(R.id.ui_one_yuan_details_title)
    TextView title;
    @Bind(R.id.ui_one_yuan_details_all_man)
    TextView AllMan;
    @Bind(R.id.ui_one_yuan_details_lost_man)
    TextView LostMan;
    @Bind(R.id.ui_one_yuan_details_isDoing)
    TextView IsDoing;
    @Bind(R.id.ui_one_yuan_details_bar)
    ProgressBar bar;
    @Bind(R.id.ui_one_yuan_details_luck_number)
    TextView luckNumber;
    //查看计算详情
    @Bind(R.id.ui_one_yuan_details_look_luck)
    RelativeLayout lookLuck;
    @Bind(R.id.ui_one_yuan_details_bar_content)
    RelativeLayout barContent;
    @Bind(R.id.ui_one_yuan_details_business_isshow)
    RelativeLayout isBusiness;
    @Bind(R.id.ui_one_yuan_details_business_is_winning)
    LinearLayout isWinning;
    @Bind(R.id.ui_one_yuan_details_business_winning_image)
    ImageView WinningImage;
    @Bind(R.id.ui_one_yuan_details_business_winning_name)
    TextView WinningName;
    @Bind(R.id.ui_one_yuan_details_business_winning_ip)
    TextView WinningIp;
    @Bind(R.id.ui_one_yuan_details_business_winning_number)
    TextView WinningNumber;
    @Bind(R.id.ui_one_yuan_details_business_winning_time)
    TextView WinningTime;
    @Bind(R.id.ui_one_yuan_details_business)
    TextView business;
    @Bind(R.id.ui_one_yuan_details_business_image)
    ImageView businessImage;
    //往期揭晓
    @Bind(R.id.ui_one_yuan_details_ToAnnounce)
    RelativeLayout ToAnnounce;
    @Bind(R.id.ui_one_yuang_details_record)
    MyListView listView;

    private int alphaMax = 160;
    @Bind(R.id.title_layout)//大标题
            RelativeLayout title_bg;
    @Bind(R.id.back)
    ImageView back;

    private List<String> urlsImage;
    private QueryBaseRecord queryBaseRecords;
    private List<QueryBaseRecordData> queryBaseRecordDatas = new ArrayList<>();
    private OneForTwoAdapter adapter;

    public void setTop() {
        if (bigTitle != null) {
            bigTitle.setFocusable(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.on_yuan_details_fragment);
    }

    @Override
    protected void initView() {
        if (HASRIME == 0) {//当前期数的商品详情
            IsShow = true;
            HASRIME = Integer.valueOf(newHastimes);
        } else {//其他情况则是往期的商品
            IsShow = false;
            ToAnnounce.setEnabled(false);
        }
        scroView.setMode(PullToRefreshBase.Mode.BOTH);
        scroView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                queryBaseRecordDatas.clear();
                pageindext = 1;
                QueryRecord();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindext++;
                QueryBaseRecord();
            }
        });

        back.getBackground().setAlpha(alphaMax);
        bigTitle.setAlpha(1 - alphaMax / 100);
        title_bg.getBackground().setAlpha(1 - alphaMax / 100);
        scroView.setOnScrollChange(new PullToRefreshScrollView.ScrollChange() {
            @Override
            public void overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY) {
                MyChange.ChangeHandAlpha(title_bg, back, bigTitle, scrollY);
            }
        });

        QueryRecord();
    }

    //进入官方旗舰店
    @OnClick(R.id.ui_one_yuan_details_business_image)
    public void onImage(View view) {
        Intent intent = new Intent(getContext(), ShopHomeActivity.class);
        intent.putExtra("shopId", details.sellerInfo.shopid);
        intent.putExtra("shopName", "旗舰店");
        startActivity(intent);
    }

    @OnClick(R.id.back)
    public void onBack(View view) {
        finishFramager();
    }

    //图文详情
    @OnClick(R.id.ui_one_yuan_details_Graphic_details)
    public void onGraphicDetails(View view) {
        Intent intent = new Intent(activity, OneYuanDetialWebViewActivity.class);
        intent.putExtra("goodsId", details.baseInfo.productid);
        startActivity(intent);
    }

    //往期揭晓
    @OnClick(R.id.ui_one_yuan_details_ToAnnounce)
    public void onToAnnounce(View view) {
        ToAnnounceFragment announceFragment = ToAnnounceFragment.newInstance(tbid);
        announceFragment.HASRIME = Integer.valueOf(newHastimes);
        announceFragment.newHasTime = newHastimes;
        replaceFragment(R.id.common_frame, announceFragment);
    }

    //晒单
    @OnClick(R.id.ui_one_yuan_details_sun)
    public void onTheSun(View view) {
        TheSunFragment theSunFragment = new TheSunFragment();
        theSunFragment.ID = details.baseInfo.productid;
        theSunFragment.Title = "商品晒单";
        replaceFragment(R.id.common_frame, theSunFragment);
    }

    //立即一元购
    @OnClick(R.id.ui_one_yuan_details_buy)
    public void onBuy(View view) {
        if (details != null && details.baseInfo != null) {
            if (!application.isLogin()) {
                ToastView.show(activity, "请先登录");
                Intent intent = new Intent(activity, SignInActivity.class);
                startActivity(intent);
                return;
            }
            List<OneGoodsModel> models = new ArrayList<>();
            OneGoodsModel model1 = new OneGoodsModel();
            //模型转换
            model1.title = details.baseInfo.title;
            model1.hastimes = details.baseInfo.hastimes;
            model1.price = details.baseInfo.price;
            model1.participatetimes = details.baseInfo.participatetimes;
            model1.hasparticipatetimes = details.baseInfo.hasparticipatetimes;
            model1.tbid = details.baseInfo.tbid;
            model1.average = details.baseInfo.average;
            model1.mainImg = details.baseInfo.headimg;
            models.add(model1);

            Intent intent = new Intent(activity, CommonActivity.class);
            intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.ONE_YUAN_PAY_MENT);
            intent.putExtra("models", (Serializable) models);
            startActivityForResult(intent, 0);
        }

    }

    //前往购物车
    @OnClick(R.id.ui_one_yuan_details_goToShoppingCar)
    public void onGoToShoppingCar(View view) {
        if (!application.isLogin()) {
            ToastView.show(activity, "请先登录");
            Intent intent = new Intent(activity, SignInActivity.class);
            startActivity(intent);
            return;
        }
        OneYuanShoppingCarFragment shoppingCar = new OneYuanShoppingCarFragment();
        replaceFragment(R.id.common_frame, shoppingCar);
    }

    //添加购物车
    @OnClick(R.id.ui_one_yuan_details_AddCar)
    public void onAddCar(View view) {
        if (tbid != null) {
            if (!application.isLogin()) {
                ToastView.show(activity, "请先登录");
                Intent intent = new Intent(activity, SignInActivity.class);
                startActivity(intent);
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("tbid", tbid);//抢购商品主键
            map.put("buytimes", 1);//参与人次
            map.put("type", 0);//包尾 0不包尾 1包尾
            ApiClient.send(activity, JConstant.ADDSHOPPINGCAR_, map, true, null, new DataLoader<String>() {
                @Override
                public void task(String data) {
                }

                @Override
                public void succeed(String data) {
                    toastSuccess("添加成功");
                }

                @Override
                public void error(String message) {
                }
            }, JConstant.ADDSHOPPINGCAR_);
        }
    }

    //商品详情
    private void QueryRecord() {
        Map<String, Object> map = new HashMap<>();
        if (IsShow) {
            if (isMall) {
                map.put("goodsId", tbid);
            } else {
                map.put("hastimes", HASRIME);//当前期数
                map.put("tbid", tbid);//抢购商品主键
            }
        } else {
            map.put("hastimes", HASRIME);//当前期数
            map.put("tbid", tbid);//抢购商品主键
        }

        ApiClient.send(activity, JConstant.QUERYBASERECORD_, map, true, ProductDetails.class, new DataLoader<ProductDetails>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(ProductDetails data) {
                if (data != null) {
                    details = data;
                    newHastimes = data.baseInfo.hastimes;//获得当前期数
                    HASRIME = Integer.valueOf(newHastimes);
                    QueryBaseRecord();
                    QueryRecordFor();
                }
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.QUERYBASERECORD_);
    }

    //商品的乐购记录
    private void QueryBaseRecord() {
        Map<String, Object> map = new HashMap<>();
        map.put("tbId", tbid);//抢购商品主键
        map.put("pageIndex", pageindext);//当前页

        ApiClient.send(activity, JConstant.QUERYPARTICIPATIONRECORD_, map, QueryBaseRecord.class, new DataLoader<QueryBaseRecord>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(QueryBaseRecord data) {
                scroView.onRefreshComplete();
                queryBaseRecords = data;
                if (data.data != null) {
                    queryBaseRecordDatas.addAll(queryBaseRecords.data);
                    if (adapter == null) {
                        adapter = new OneForTwoAdapter(activity, queryBaseRecordDatas);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    queryBaseRecords = new QueryBaseRecord();
                }
            }

            @Override
            public void error(String message) {
                scroView.onRefreshComplete();
            }
        }, JConstant.QUERYPARTICIPATIONRECORD_);
    }

    private void QueryRecordFor() {
        //设置展示图片
        urlsImage = new ArrayList<String>();

        if (details.baseInfo != null && details.baseInfo.imgList != null && details.baseInfo.imgList.size() > 0) {
            for (int i = 0; i < details.baseInfo.imgList.size(); i++) {
                urlsImage.add(details.baseInfo.imgList.get(i).path);
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
            }, R.mipmap.adv_top);
        } else {
            cycleView.setBackgroundResource(R.mipmap.adv_top);
        }
        //设置商品的基本信息
        if (!StringUtil.hasText(details.baseInfo.hastimes)) {
            details.baseInfo.hastimes = "0";
        }
        title.setText("(第" + details.baseInfo.hastimes + "期)" + details.baseInfo.title);
        //人次判断
        if (!StringUtil.hasText(details.baseInfo.participatetimes)) {
            details.baseInfo.participatetimes = "0";
        }
        if (!StringUtil.hasText(details.baseInfo.hasparticipatetimes)) {
            details.baseInfo.hasparticipatetimes = "0";
        }
        bar.setMax(Integer.valueOf(details.baseInfo.participatetimes));
        bar.setProgress(Integer.valueOf(details.baseInfo.hasparticipatetimes));
        int start;
        int end;
        StringBuffer buffer = new StringBuffer();
        buffer.append("总需");
        start = buffer.length();
        buffer.append(details.baseInfo.participatetimes);
        end = buffer.length();
        buffer.append("人次");
        AllMan.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, Color.RED));//共需人次
        buffer = new StringBuffer();
        buffer.append("剩余");
        start = buffer.length();
        buffer.append((Integer.valueOf(details.baseInfo.participatetimes) - Integer.valueOf(details.baseInfo.hasparticipatetimes)));
        end = buffer.length();
        buffer.append("人次");
        LostMan.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, Color.RED));//剩余人次
        //如果商品已经被买完，隐藏购买等相关功能
        if ((Integer.valueOf(details.baseInfo.participatetimes) - Integer.valueOf(details.baseInfo.hasparticipatetimes)) <= 0) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }

        bigTitle.setText("商品详情");

        //如果有商家信息，并且不是往期信息
        if (details.sellerInfo != null && IsShow) {
            isBusiness.setVisibility(View.VISIBLE);
            bar.setVisibility(View.VISIBLE);
            barContent.setVisibility(View.VISIBLE);
            business.setText(details.sellerInfo.address);
            isWinning.setVisibility(View.GONE);
            lookLuck.setVisibility(View.GONE);
            ToAnnounce.setVisibility(View.VISIBLE);
            ApiClient.displayImage(details.sellerInfo.logo, businessImage);
        } else if (!IsShow) {
            isBusiness.setVisibility(View.GONE);
            bar.setVisibility(View.GONE);
            barContent.setVisibility(View.GONE);
            isWinning.setVisibility(View.VISIBLE);
            lookLuck.setVisibility(View.VISIBLE);
            luckNumber.setText("幸运乐购码：" + details.baseInfo.luckcode);
            ToAnnounce.setVisibility(View.GONE);//往期揭晓
            bigTitle.setText("往期揭晓");
            /**
             * 查看幸运乐购码
             * */
            lookLuck.setOnClickListener(new View.OnClickListener() {//查看幸运号码计算详情
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, OneYuanCalculationDetailsActivity.class);
                    intent.putExtra("id", details.baseInfo.tbid);
                    activity.startActivity(intent);

                }
            });
            IsDoing.setText("已揭晓");
            IsDoing.setBackgroundResource(R.drawable.round_green_untransparent);
            ApiClient.displayImage(details.baseInfo.headimg, WinningImage);
            WinningName.setText(details.baseInfo.nickname);
            WinningIp.setText(details.baseInfo.loginip);
            if (!StringUtil.hasText(details.baseInfo.buytims)) {
                details.baseInfo.buytims = "0";
            }
            WinningNumber.setText(MyChange.ChangeTextColor(details.baseInfo.buytims + "人次", 0, details.baseInfo.buytims.length(), Color.RED));
            WinningTime.setText(details.baseInfo.buyproducttime);
        } else {//如果既不是往期信息也没有厂家，就全部隐藏
            isBusiness.setVisibility(View.GONE);
            isWinning.setVisibility(View.GONE);
            lookLuck.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            activity.setResult(Activity.RESULT_OK);
            //刷新
            queryBaseRecordDatas.clear();
            pageindext = 1;
            QueryRecord();
        }
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.ADDSHOPPINGCAR_);
        ApiClient.cancelRequest(JConstant.QUERYBASERECORD_);
        ApiClient.cancelRequest(JConstant.QUERYPARTICIPATIONRECORD_);
    }
}
