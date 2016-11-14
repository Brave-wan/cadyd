package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Bind;
import com.cadyd.app.R;
import com.cadyd.app.adapter.IntegralMallTabAdapter;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.Integralmall.IntegralCarFragment;
import com.cadyd.app.ui.fragment.Integralmall.IntegralHomeFragment;
import com.cadyd.app.ui.fragment.UserCententFragment;
import com.cadyd.app.ui.fragment.mall.MallTypeFragment;
import com.cadyd.app.ui.view.BaseLayout;
import org.wcy.android.widget.OnEmptyFoundClick;

import java.util.ArrayList;
import java.util.List;

public class IntegralMallManagerActivity extends BaseFragmentActivity {

    private Intent intent;
    @Bind(R.id.one_one_rgs)
    RadioGroup rgs;
    IntegralMallTabAdapter tabAdapter;
    public String ID = null;
    public List<Fragment> fragments = new ArrayList<>();
    BaseLayout layout;
    private RadioButton button;
    private RadioButton rb;
    @Bind(R.id.one_one_home)
    RadioButton one_one_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_one_manager);

        //按照顺序排列
        final IntegralHomeFragment integralHome = new IntegralHomeFragment();
        integralHome.setInterface(new TowObjectParameterInterface() {
            @Override
            public void Onchange(int type, int postion, Object object) {
                ID = (String) object;
                rb = (RadioButton) rgs.getChildAt(1);
                rb.setChecked(true);
            }
        });
        fragments.add(integralHome);//首页
        final MallTypeFragment mallTypeFragment = MallTypeFragment.newInstance("integral");
        fragments.add(mallTypeFragment);//分类
        IntegralCarFragment integralCarFragment = new IntegralCarFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBack", false);
        integralCarFragment.setArguments(bundle);
        integralCarFragment.setClick(new OnEmptyFoundClick() {
            @Override
            public void onFoundCilck() {
                one_one_home.setChecked(true);
            }
        });
        fragments.add(integralCarFragment);
        fragments.add(new UserCententFragment());

        tabAdapter = new IntegralMallTabAdapter(this, fragments, R.id.common_frame, rgs);
        button = (RadioButton) findViewById(R.id.one_one_home);//保存老的选择
        tabAdapter.setOnRgsExtraCheckedChangedListener(new IntegralMallTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.one_one_home:
                        button = (RadioButton) findViewById(R.id.one_one_home);
                        break;
                    case R.id.one_one_type:
                        if (mallTypeFragment != null && ID != null) {
                            mallTypeFragment.ChageItem(ID);
                            ID = null;
                        }
                        button = (RadioButton) findViewById(R.id.one_one_type);
                        break;
                    case R.id.one_one_car:
                        button = (RadioButton) findViewById(R.id.one_one_car);
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
                        /*intent = new Intent(IntegralMallManagerActivity.this, CommonActivity.class);
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
