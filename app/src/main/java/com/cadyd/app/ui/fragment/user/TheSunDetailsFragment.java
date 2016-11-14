package com.cadyd.app.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.GoodsTheSunData;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.unitary.TheSunAllCommentFragment;
import com.cadyd.app.ui.view.ToastView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 晒单详情
 */
public class TheSunDetailsFragment extends BaseFragement {

    private GoodsTheSunData goodsTheSunDatas;
    private int pos;

    public static TheSunDetailsFragment newInstance(GoodsTheSunData model, int pos) {
        TheSunDetailsFragment newFragment = new TheSunDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        bundle.putInt("pos", pos);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.goodsTheSunDatas = (GoodsTheSunData) args.getSerializable("model");
            this.pos = args.getInt("pos");
        }
    }


    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.content_text)
    TextView content;

    //点评
    @Bind(R.id.details_comment)
    TextView comment;
    //点赞
    @Bind(R.id.details_praise)
    TextView praise;

    private List<View> lists = new ArrayList<View>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return setView(R.layout.fragment_the_sun_details, "详情" + goodsTheSunDatas.imgList.size() + "/" + 1, true);
    }

    @Override
    protected void initView() {
        content.setText(goodsTheSunDatas.des);//评论内容
        comment.setText(goodsTheSunDatas.commentCount + "");//评论次数
        praise.setText(goodsTheSunDatas.praiseCount);//点赞次数

        if (goodsTheSunDatas.imgList != null) {
            for (int i = 0; i < goodsTheSunDatas.imgList.size(); i++) {
                ImageView imageView = new ImageView(activity);
                ViewPager.LayoutParams params = new ViewPager.LayoutParams();
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ApiClient.displayImage(goodsTheSunDatas.imgList.get(i).path, imageView);
                lists.add(imageView);
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    layout.titleText.setText("详情" + goodsTheSunDatas.imgList.size() + "/" + (position + 1));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(pos);
        }
    }


    PagerAdapter adapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(lists.get(position));
            return lists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(lists.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };

    @OnClick(R.id.details_praise)
    public void onPraise(View view) {
        initSaveLikes();
    }

    //查看全部评论
    @OnClick(R.id.details_comment)
    public void onComment(View view) {
        replaceFragment(R.id.common_frame, TheSunAllCommentFragment.newInstance(goodsTheSunDatas.id));
    }


    //点赞
    private void initSaveLikes() {
        if (!application.isLogin()) {
            ToastView.show(activity, "请先登录");
            Intent intent = new Intent(activity, SignInActivity.class);
            startActivity(intent);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("listid", goodsTheSunDatas.id);
        map.put("userid", application.model.id);
        ApiClient.send(activity, JConstant.SAVELIKES_, map, true, null, new DataLoader<String>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                int num = Integer.valueOf(goodsTheSunDatas.praiseCount);
                num++;
                goodsTheSunDatas.praiseCount = String.valueOf(num);
                praise.setText(goodsTheSunDatas.praiseCount);//点赞次数
                toastSuccess("点赞成功");
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.SAVELIKES_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SAVELIKES_);
    }
}
