package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.Bind;

import com.cadyd.app.R;
import com.cadyd.app.adapter.GlobalTabAdapter;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.UserCententFragment;
import com.cadyd.app.ui.fragment.cart.HomeShoppingCartFragment;
import com.cadyd.app.ui.fragment.cart.ShoppingCartFragment;
import com.cadyd.app.ui.fragment.global.GlobalHomeFragment;
import com.cadyd.app.ui.fragment.mall.MallTypeFragment;
import com.cadyd.app.ui.view.BaseLayout;

import org.wcy.android.widget.OnEmptyFoundClick;

import java.util.ArrayList;
import java.util.List;

public class GlobalManagerActivity extends BaseFragmentActivity {

    private Intent intent;
    @Bind(R.id.global_rgs)
    RadioGroup rgs;
    GlobalTabAdapter tabAdapter;
    public List<Fragment> fragments = new ArrayList<>();
    BaseLayout layout;
    private RadioButton button;
    @Bind(R.id.global_home)
    RadioButton global_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_manager);

        //按照顺序排列

        final GlobalHomeFragment globalHome = new GlobalHomeFragment();

        fragments.add(globalHome);
        fragments.add(MallTypeFragment.newInstance("global"));//分类
        //fragments.add(new GlobalDoingFragment());

        HomeShoppingCartFragment shoppingCartFragment = new HomeShoppingCartFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBack", false);
        shoppingCartFragment.setArguments(bundle);
        shoppingCartFragment.setClick(new OnEmptyFoundClick() {
            @Override
            public void onFoundCilck() {
                global_home.setChecked(true);
            }
        });
        fragments.add(shoppingCartFragment);

        fragments.add(new UserCententFragment());

        tabAdapter = new GlobalTabAdapter(this, fragments, R.id.common_frame, rgs);
        button = (RadioButton) findViewById(R.id.global_home);//保存老的选择
        tabAdapter.setOnRgsExtraCheckedChangedListener(new GlobalTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.global_home:
                        globalHome.setTop();
                        button = (RadioButton) findViewById(R.id.global_home);
                        break;
                    case R.id.global_type:
                        button = (RadioButton) findViewById(R.id.global_type);
                        break;
                    /*case R.id.global_car:
                        intent = new Intent(GlobalManagerActivity.this, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INDEX_SHOPPING_CART);
                        button.setChecked(true);
                        startActivity(intent);
                        break;*/
                    case R.id.global_doing:
                        button = (RadioButton) findViewById(R.id.global_doing);
                        break;
                    case R.id.global_user://登录
                        if (!application.isLogin()) {
                            Intent intent = new Intent(GlobalManagerActivity.this, SignInActivity.class);
                            startActivity(intent);
                            button.setChecked(true);
//                         return;
                        }
                        /*button.setChecked(true);
                        button = (RadioButton) findViewById(R.id.global_user);
                        intent = new Intent(GlobalManagerActivity.this, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.USER_CENTENT);
                        startActivity(intent);*/
                        break;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
