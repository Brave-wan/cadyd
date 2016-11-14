package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.Bind;

import com.cadyd.app.R;
import com.cadyd.app.adapter.OneOneTabAdapter;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.UserCententFragment;
import com.cadyd.app.ui.fragment.cart.HomeShoppingCartFragment;
import com.cadyd.app.ui.fragment.cart.ShoppingCartFragment;
import com.cadyd.app.ui.fragment.mall.MallTypeFragment;
import com.cadyd.app.ui.fragment.oneone.OneOneHomeFragment;
import com.cadyd.app.ui.view.BaseLayout;

import org.wcy.android.widget.OnEmptyFoundClick;

import java.util.ArrayList;
import java.util.List;

public class OneOneManagerActivity extends BaseFragmentActivity {

    private Intent intent;
    @Bind(R.id.one_one_rgs)
    RadioGroup rgs;
    OneOneTabAdapter tabAdapter;
    public List<Fragment> fragments = new ArrayList<>();
    BaseLayout layout;
    private RadioButton button;
    @Bind(R.id.one_one_home)
    RadioButton one_one_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_one_manager);
        //按照顺序排列
        final OneOneHomeFragment oneOneHomeFragment = new OneOneHomeFragment();
        fragments.add(oneOneHomeFragment);//首页
        fragments.add(MallTypeFragment.newInstance("country"));//分类
        //fragments.add(new OneOneDoingFragment());
        HomeShoppingCartFragment shoppingCartFragment = new HomeShoppingCartFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBack", false);
        shoppingCartFragment.setArguments(bundle);
        shoppingCartFragment.setClick(new OnEmptyFoundClick() {
            @Override
            public void onFoundCilck() {
                one_one_home.setChecked(true);
            }
        });
        fragments.add(shoppingCartFragment);
        fragments.add(new UserCententFragment());

        tabAdapter = new OneOneTabAdapter(this, fragments, R.id.common_frame, rgs);
        button = (RadioButton) findViewById(R.id.one_one_home);//保存老的选择
        tabAdapter.setOnRgsExtraCheckedChangedListener(new OneOneTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.one_one_home:
                        oneOneHomeFragment.setTop();
                        button = (RadioButton) findViewById(R.id.one_one_home);
                        break;
                    case R.id.one_one_type:
                        button = (RadioButton) findViewById(R.id.one_one_type);
                        break;
                    case R.id.one_one_car:
                        if (!application.isLogin()) {
                            intent = new Intent(activity, SignInActivity.class);
                            startActivity(intent);
                            button.setChecked(true);
                        }
                            /*intent = new Intent(OneOneManagerActivity.this, CommonActivity.class);
                            intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INDEX_SHOPPING_CART);
                            startActivity(intent);
                            button.setChecked(true);
                            return;*/
                        break;
                    case R.id.one_one_doing:
                        button = (RadioButton) findViewById(R.id.one_one_doing);
                        break;
                    case R.id.one_one_user://个人中心
                        if (!application.isLogin()) {
                            intent = new Intent(activity, SignInActivity.class);
                            button.setChecked(true);
                            startActivity(intent);
                        }
                        /*intent = new Intent(OneOneManagerActivity.this, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.USER_CENTENT);
                        startActivity(intent);
                        button.setChecked(true);
                        return;*/
                        break;
                }
            }
        });

    }

}
