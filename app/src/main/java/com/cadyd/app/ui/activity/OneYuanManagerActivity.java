package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.adapter.OneYuanTabAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.BackHandledInterface;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.UserCententFragment;
import com.cadyd.app.ui.fragment.unitary.AllGoodsFragment;
import com.cadyd.app.ui.fragment.unitary.NewAnnouncedFragment;
import com.cadyd.app.ui.fragment.unitary.OneYuanShoppingCarFragment;
import com.cadyd.app.ui.fragment.unitary.OneYuanTescoFragment;
import com.cadyd.app.ui.view.ToastView;

import org.wcy.android.widget.OnEmptyFoundClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OneYuanManagerActivity extends FragmentActivity implements BackHandledInterface {
    MyApplication application;

    private Intent intent;

    @Bind(R.id.rgs)
    RadioGroup rgs;
    @Bind(R.id.ui_tesco_navigation_radio_button_home)
    RadioButton homeRadio;

    OneYuanTabAdapter tabAdapter;
    public List<Fragment> fragments = new ArrayList<>();
    private RadioButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_yuan_manager);
        ButterKnife.bind(this);
        application = (MyApplication) getApplication();
        //按照顺序排列
//        getSupportFragmentManager().getFragments()
        final OneYuanTescoFragment oneYuanTescoFragment = new OneYuanTescoFragment();
        fragments.add(oneYuanTescoFragment);//首页
        fragments.add(AllGoodsFragment.newInstance(AllGoodsFragment.ALL));
        final NewAnnouncedFragment newAnnouncedFragment = new NewAnnouncedFragment();
        fragments.add(newAnnouncedFragment);//最新揭晓
        final OneYuanShoppingCarFragment oneYuanShoppingCarFragment = new OneYuanShoppingCarFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBack", false);
        oneYuanShoppingCarFragment.setArguments(bundle);

        oneYuanShoppingCarFragment.setClick(new OnEmptyFoundClick() {
            @Override
            public void onFoundCilck() {
                homeRadio.setChecked(true);
            }
        });
        fragments.add(oneYuanShoppingCarFragment);//购物车
        fragments.add(new UserCententFragment());
        tabAdapter = new OneYuanTabAdapter(this, fragments, R.id.common_frame, rgs);
        button = (RadioButton) findViewById(R.id.ui_tesco_navigation_radio_button_home);//保存老的选择
        tabAdapter.setOnRgsExtraCheckedChangedListener(new OneYuanTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.ui_tesco_navigation_radio_button_home:
                        button = (RadioButton) findViewById(R.id.ui_tesco_navigation_radio_button_home);
                        break;
                    case R.id.ui_tesco_navigation_radio_button_allGoods:
                        button = (RadioButton) findViewById(R.id.ui_tesco_navigation_radio_button_allGoods);
                        break;
                    case R.id.ui_tesco_navigation_radio_button_new:
                        button = (RadioButton) findViewById(R.id.ui_tesco_navigation_radio_button_new);
                        newAnnouncedFragment.MyRefresh();
                        break;
                    case R.id.ui_tesco_navigation_radio_button_car:
                        if (!application.isLogin()) {
                            ToastView.show(OneYuanManagerActivity.this, "请先登录");
                            Intent intent = new Intent(OneYuanManagerActivity.this, SignInActivity.class);
                            startActivity(intent);
                            button.setChecked(true);
                            return;
                        }
                        button = (RadioButton) findViewById(R.id.ui_tesco_navigation_radio_button_car);
                        oneYuanShoppingCarFragment.MyRefresh();
                        break;
                    case R.id.ui_tesco_navigation_radio_button_my:

                        if (!application.isLogin()) {
                            Intent intent = new Intent(OneYuanManagerActivity.this, SignInActivity.class);
                            startActivity(intent);
                            button.setChecked(true);
//                            return;
                        }

                        /*intent = new Intent(OneYuanManagerActivity.this, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.USER_CENTENT);
                        startActivity(intent);
                        button.setChecked(true);*/
                        break;
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void setSelectedFragment(BaseFragement selectedFragment) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragments = null;
        Log.i("wan", "ondestroy");
        ApiClient.listener.cleanBitmap();
    }
}
