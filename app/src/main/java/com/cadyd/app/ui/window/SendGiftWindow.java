package com.cadyd.app.ui.window;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cadyd.app.R;
import com.cadyd.app.adapter.SendGiftViewPagerAdapter;
import com.cadyd.app.ui.fragment.live.SendGiftFragment;

import java.util.ArrayList;

/**
 * @Description: 赠送礼物
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class SendGiftWindow extends PopupWindow implements ViewPager.OnPageChangeListener {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private View mView;
    private PercentRelativeLayout rootView;
    private PopupWindow mPopupWindow;
    private ViewPager mViewPager;
    private LinearLayout mPoint;
    private ArrayList<String> list = new ArrayList<>();
    private ImageView[] tips;
    private SendGiftViewPagerAdapter adapter;
    FragmentActivity fragmentActivity;
    private FragmentPagerAdapter mAdpter;
    public SendGiftWindow(Context context, FragmentActivity fragmentActivity) {
        super(context);
        this.mContext = context;
        this.fragmentActivity = fragmentActivity;
        mLayoutInflater = LayoutInflater.from(context);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 30; i++) {
            list.add("" + i);
        }
    }

    private void initView() {

        mView = mLayoutInflater.inflate(R.layout.window_sendgift, null);
        rootView = (PercentRelativeLayout) mView.findViewById(R.id.sendgift_view);
        mViewPager = (ViewPager) mView.findViewById(R.id.window_sendgift_pager);
        mPoint = (LinearLayout) mView.findViewById(R.id.window_bottomPoint);
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
        mViewPager.setOnPageChangeListener(this);
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        mViewPager.setCurrentItem((list.size()) * 100);
        adapter = new SendGiftViewPagerAdapter(fragmentActivity.getSupportFragmentManager(), mContext);
//        initPoint(list, mPoint);
        mAdpter=new FragmentPagerAdapter(fragmentActivity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new SendGiftFragment();
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        mViewPager.setAdapter(mAdpter);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismitPopWindow();
            }
        });

    }

    public void dismitPopWindow() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showPopWindow(View v) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    }

    public void initPoint(ArrayList<String> list, LinearLayout view) {
        tips = new ImageView[list.size() / 8];
        Log.i("wan", "tips-----" + tips.length);
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.bg_circle_sel);
            } else {
                tips[i].setBackgroundResource(R.drawable.bg_circle_white);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            view.addView(imageView, layoutParams);
        }

        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % list.size());
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.bg_circle_sel);
            } else {
                tips[i].setBackgroundResource(R.drawable.bg_circle_white);
            }
        }
    }
}
