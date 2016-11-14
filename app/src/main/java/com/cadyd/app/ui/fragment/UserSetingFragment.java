package com.cadyd.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.LoginOutInfo;
import com.cadyd.app.ui.activity.AboutsActivity;
import com.cadyd.app.ui.activity.ChangePasswordActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.user.EditPhoneFragment;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.ypy.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

//import com.tencent.bugly.beta.Beta;
//import com.tencent.bugly.beta.UpgradeInfo;

/**
 * zjh
 * 用户设置
 */
public class UserSetingFragment extends BaseFragement implements View.OnClickListener {

    public static final int BACKRESULT = 142;

    private int CHANGPASS = 3;
    AlertConfirmation alert;
    @Bind(R.id.ui_user_information_change_update)
    RelativeLayout update;
    @Bind(R.id.user_Suggestions)
    RelativeLayout userSuggestions;
    @Bind(R.id.user_clear_cache_number)
    TextView userClearCacheNumber;//缓存大小

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_user_seting, "设置", true);
    }

    @Override
    protected void initView() {
//        long l = 0;
//        File file = new File("com.cadyd.app");
//        try {
//            l = ClearCache.getFolderSize(file);
//            Log.i("huang ", "查询缓存没有出现异常");
//        } catch (Exception e) {
//            Log.i("huang ", "查询缓存出现了异常");
//            e.printStackTrace();
//        }
//        userClearCacheNumber.setText("" + l);
        userClearCacheNumber.setOnClickListener(this);

        update.setOnClickListener(this);
        userSuggestions.setOnClickListener(this);
    }

    /**
     * 修改手机号码
     */
    @OnClick(R.id.ui_user_information_phone)
    public void onEditPhone(View view) {
        EditPhoneFragment editPhoneFragment = new EditPhoneFragment();
        editPhoneFragment.setTowObjectParameterInterface(new TowObjectParameterInterface() {
            @Override
            public void Onchange(int type, int postion, Object object) {
                //修改成功后跳转到登录
                // startActivity(new Intent(activity, SignInActivity.class));
            }
        });
        replaceFragment(R.id.common_frame, editPhoneFragment);
    }

    /**
     * 检查更新
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ui_user_information_change_update:
//                Beta.checkUpgrade();
//                loadUpgradeInfo();
                break;
            case R.id.user_Suggestions:
                /**
                 * 投诉建议
                 */
                replaceFragment(R.id.common_frame, new UserSuggestionsFragment());
                break;
            case R.id.user_clear_cache_number://清除缓存
                break;
        }
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.ui_user_information_change_password)
    public void OnChangePassword(View view) {
        Intent intent = new Intent(activity, ChangePasswordActivity.class);
        startActivityForResult(intent, CHANGPASS);
    }

    /**
     * 我的帐号
     */
    @OnClick(R.id.ui_user_my_account)
    public void onAccount(View view) {
        replaceFragment(R.id.common_frame, new MyAccountFragment());
    }


    /**
     * 关于我们
     **/
    @OnClick(R.id.about_us)
    public void onAboutUs(View view) {
        //TODO 跳转到关于我们
        startActivity(AboutsActivity.class, false);
    }

    //退出帐号
    @OnClick(R.id.ui_user_information_sign_out)
    public void OnSignOut(View view) {
        if (alert == null) {
            alert = new AlertConfirmation(getActivity(), "退出帐号", "是否退出当前帐号");
        }
        alert.show(new AlertConfirmation.OnClickListeners() {
            @Override
            public void ConfirmOnClickListener() {
                application.clear();
                alert.hide();
                application.isExit = true;
                EventBus.getDefault().post(new LoginOutInfo());
                activity.setResult(BACKRESULT);
                getActivity().finish();
            }

            @Override
            public void CancelOnClickListener() {
                alert.hide();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}
