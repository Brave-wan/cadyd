package com.cadyd.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import com.cadyd.app.R;
import com.cadyd.app.ui.activity.*;
import com.cadyd.app.ui.base.BaseFragement;

public class FoundFragment extends BaseFragement implements View.OnClickListener {

    @Bind(R.id.btn_found_shopping)
    ImageView shopping;
    @Bind(R.id.btn_found_onedollar)
    ImageView onedollar;
    @Bind(R.id.btn_found_globalbuy)
    ImageView globalbuy;
    @Bind(R.id.btn_found_ovot)
    ImageView ovot;
    @Bind(R.id.btn_found_ef)
    ImageView efood;
    @Bind(R.id.btn_found_integral)
    ImageView integral;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_found_layout, getString(R.string.found), false);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    protected void initView() {
        shopping.setImageResource(R.mipmap.found_shopping);
        onedollar.setImageResource(R.mipmap.found_onedollar);
        globalbuy.setImageResource(R.mipmap.found_globalbuy);
        ovot.setImageResource(R.mipmap.found_ovot);
        efood.setImageResource(R.mipmap.found_ef);
        integral.setImageResource(R.mipmap.found_integral);

        shopping.setOnClickListener(this);
        onedollar.setOnClickListener(this);
        globalbuy.setOnClickListener(this);
        ovot.setOnClickListener(this);
        efood.setOnClickListener(this);
        integral.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_found_shopping:
                startActivity(MallMainActivity.class, false);
                break;
            case R.id.btn_found_onedollar:
                startActivity(OneYuanManagerActivity.class, false);
                break;
            case R.id.btn_found_globalbuy:
                startActivity(GlobalManagerActivity.class, false);
                break;
            case R.id.btn_found_ovot:
                startActivity(OneOneManagerActivity.class, false);
                break;
            case R.id.btn_found_ef:
                //startActivity(MallMainActivity.class, false);
                break;
            case R.id.btn_found_integral:
                startActivity(IntegralMallManagerActivity.class, false);
                break;
        }
    }
}
