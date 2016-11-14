package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.cadyd.app.R;
import com.cadyd.app.adapter.DirectRoomFragmentAdapter;
import com.cadyd.app.comm.ForPxTp;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.InfoDetailsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-8-24.
 */
public class DirectRoomActivity extends BaseFragmentActivity {
    //将ToolBar与TabLayout结合放入AppBarLayout
    private Toolbar mToolbar;
    //Tab菜单，主界面上面的tab切换菜单
    private TabLayout mTabLayout;
    //v4中的ViewPager控件
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_directroom, "一点直播", true);
        initView();
    }

    private void initView() {
        mTabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        ForPxTp.px2dip(this,100);
        mViewPager = (ViewPager) this.findViewById(R.id.view_pager);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setSelected(true);

    }
}
