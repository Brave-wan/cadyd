package com.cadyd.app.ui.fragment.live;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cadyd.app.LiveDetailProductsAdapter;
import com.cadyd.app.R;
import com.cadyd.app.adapter.LiveDetailPagerAdapter;
import com.cadyd.app.model.LivePersonalDetailIfon;
import com.cadyd.app.widget.SpaceItemDecoration;

import org.wcy.android.utils.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class LiveDetailActivity extends DialogFragment implements View.OnClickListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private LiveDetailPagerAdapter mAdapter;
    private FragmentManager fragmentManager;
    private PercentRelativeLayout mView;
    private RecyclerView mGridView;
    private CommonAdapter<String> gridAdapter;
    private List<Fragment> list = new ArrayList<>();
    private LiveDetailProductsAdapter adapter;
    private LivePersonalDetailIfon mDetailInfo = new LivePersonalDetailIfon();
    private List<LivePersonalDetailIfon.advertListBean> advertList = new ArrayList<>();
    private ArrayList<String> noticeList = new ArrayList<>();

    private BusinessAFragment mBusinessAFragment;
    private PeopleSayFragment mPeopleSayFragment;
    private BuyWatchingFragment mBuyWatchingFragment;
    private LinearLayout rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.window_livedetail, null);
        setStyle(STYLE_NO_FRAME, R.style.TranslucentNoTitle);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        fragmentManager = getChildFragmentManager();
        Bundle bundle = getArguments();
        mDetailInfo = (LivePersonalDetailIfon) bundle.getSerializable("info");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView(view);
        return view;
    }


    private void initView(View view) {
        if (mDetailInfo != null) {
            advertList = mDetailInfo.getAdvertList();
            noticeList = mDetailInfo.getNotice();
        }
        rootView = (LinearLayout) view.findViewById(R.id.liveDetails_root);
        mGridView = (RecyclerView) view.findViewById(R.id.liveDeatils_grvidView);
        mTabLayout = (TabLayout) view.findViewById(R.id.liveDetail_tabTitle);
        mViewPager = (ViewPager) view.findViewById(R.id.liveDetail_pager);
        mView = (PercentRelativeLayout) view.findViewById(R.id.bugywacth_rootView);
        rootView.setBackgroundColor(getResources().getColor(R.color.white));
        mGridView.setBackgroundColor(getResources().getColor(R.color.white));
        mBusinessAFragment = new BusinessAFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("notice", noticeList);
        mBusinessAFragment.setArguments(bundle);
        list.add(mBusinessAFragment);
        mPeopleSayFragment = new PeopleSayFragment();
        if (mDetailInfo != null) {
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("info",mDetailInfo);
            mPeopleSayFragment.setArguments(bundle1);
            list.add(mPeopleSayFragment);
        }

        mBuyWatchingFragment = new BuyWatchingFragment();
        if (mDetailInfo != null) {
            Bundle bundle1 = new Bundle();
            bundle1.putString("roomId", mDetailInfo.getConversationId());
            mBuyWatchingFragment.setArguments(bundle1);
        }

        if (advertList.size() <= 0) {
            mGridView.setVisibility(View.GONE);
        }
        list.add(mBuyWatchingFragment);

        WindowManager wm = getActivity().getWindowManager();
        adapter = new LiveDetailProductsAdapter(getActivity(), advertList, wm);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGridView.setLayoutManager(linearLayoutManager);

        mGridView.setItemAnimator(new DefaultItemAnimator());

        mGridView.addItemDecoration(new SpaceItemDecoration(10, 1));
        mGridView.setAdapter(adapter);

        mAdapter = new LiveDetailPagerAdapter(fragmentManager, getActivity(), list);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mView.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
    }

    @Override
    public void onClick(View v) {
        getDialog().dismiss();
    }
}
