package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Bind;
import com.cadyd.app.AppManager;
import com.cadyd.app.R;
import com.cadyd.app.adapter.FragmentMallTabAdapter;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.UserCententFragment;
import com.cadyd.app.ui.fragment.cart.ShoppingCartFragment;
import com.cadyd.app.ui.fragment.mall.MallFragment;
import com.cadyd.app.ui.fragment.mall.MallTypeFragment;
import org.wcy.android.widget.OnEmptyFoundClick;

import java.util.ArrayList;
import java.util.List;

/**
 * shang cheng
 */
public class MallMainActivity extends BaseFragmentActivity {
    @Bind(R.id.rgs)
    RadioGroup rgs;
    FragmentMallTabAdapter tabAdapter;
    public List<Fragment> fragments = new ArrayList<Fragment>();
    @Bind(R.id.home)
    RadioButton home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_main);
        
        fragments.add(new MallFragment());
        fragments.add(MallTypeFragment.newInstance("mall"));

        ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBack", false);
        shoppingCartFragment.setArguments(bundle);
        shoppingCartFragment.setClick(new OnEmptyFoundClick() {
            @Override
            public void onFoundCilck() {
                home.setChecked(true);
            }
        });
        fragments.add(shoppingCartFragment);
        fragments.add(new UserCententFragment());
        tabAdapter = new FragmentMallTabAdapter(MallMainActivity.this, fragments, R.id.common_frame, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentMallTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                if (index == 2) {
                    if (!application.isLogin()) {
                        startActivity(SignInActivity.class, false);
                    }
                    /*else {
                        Intent intent = new Intent(MallMainActivity.this, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INDEX_SHOPPING_CART);
                        startActivity(intent);
                    }*/
                } else if (index == 3) {
                    if (!application.isLogin()) {
                        startActivity(SignInActivity.class, false);
                    }
                    /*else {
                        Intent intent = new Intent(MallMainActivity.this, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.USER_CENTENT);
                        startActivity(intent);
                    }*/
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
