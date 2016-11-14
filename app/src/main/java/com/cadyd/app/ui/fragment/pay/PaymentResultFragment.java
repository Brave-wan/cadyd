package com.cadyd.app.ui.fragment.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;

import com.cadyd.app.R;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.unitary.OneYuanTescoFragment;
import com.cadyd.app.ui.fragment.unitary.PaymentFragment;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * 支付结果界面
 */
public class PaymentResultFragment extends BaseFragement {

    private boolean isPaySucceed;
    private String message;

    public static PaymentResultFragment newInstance(boolean isPaySucceed, String message) {
        PaymentResultFragment newFragment = new PaymentResultFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPaySucceed", isPaySucceed);
        bundle.putString("message", message);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.isPaySucceed = args.getBoolean("isPaySucceed");
            if (!this.isPaySucceed) {
                message = args.getString("message");
            }
        }
    }

    @Bind(R.id.title)
    TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_payment_result, getResources().getString(R.string.pay_result), true);
    }

    @Override
    protected void initView() {
        if (isPaySucceed) {
            title.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.pay_success_image, 0, 0);
            title.setText(getResources().getString(R.string.pay_succeed));
        } else {
            title.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.pay_error_image, 0, 0);
            title.setText(StringUtils.isEmpty(message) ? getResources().getString(R.string.pay_error) : message);
        }
    }

    //如果是支付成功就携带成功返回值
    @Override
    protected void TitleBarEvent(int btnID) {
        activity.setResult(isPaySucceed ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        finishActivity();
    }

    @Override
    public boolean onBackPressed() {
        activity.setResult(isPaySucceed ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        finishActivity();
        return false;
    }
}
