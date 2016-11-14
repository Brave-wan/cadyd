package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.cadyd.app.ui.fragment.live.SendGiftFragment;

import java.util.ArrayList;

/**
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class SendGiftViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<ImageView> list = new ArrayList<>();
    private SendGiftFragment mSendGiftFragment;

private Context mContext;
    public SendGiftViewPagerAdapter(FragmentManager fragmentManager, Context mContext) {
        super(fragmentManager);
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return  3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Fragment getItem(int position) {
        return new SendGiftFragment();
    }
}
