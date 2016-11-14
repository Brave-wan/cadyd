package com.cadyd.app.ui.fragment.global;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Bind;
import com.cadyd.app.R;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 全球购动态
 */
public class GlobalDoingFragment extends BaseFragement {

    @Bind(R.id.global_doing_group)
    RadioGroup group;
    @Bind(R.id.radio_button_new)
    RadioButton radioButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_global_doing, "动态", true);
    }

    @Override
    protected void initView() {
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_follow://关注
                        break;
                    case R.id.radio_button_new://最新
                        break;
                    case R.id.radio_button_want_bay://求购
                        break;
                }
            }
        });
        radioButton.setChecked(true);
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }


}
