package com.cadyd.app.ui.activity;

import android.os.Bundle;

import com.cadyd.app.R;
import com.cadyd.app.ui.base.BaseActivity;

/**
 * 个人中心投诉建议
 */
public class SuggestionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_suggestions, "投诉建议", true);
    }

}
