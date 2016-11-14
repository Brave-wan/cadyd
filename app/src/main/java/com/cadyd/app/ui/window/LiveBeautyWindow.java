package com.cadyd.app.ui.window;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cadyd.app.R;

/**
 * @Description: 设置美颜效果
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class LiveBeautyWindow extends PopupWindow implements View.OnClickListener{
    private LayoutInflater mInflater;
    private  Context mContext;
    private View mView;
    private SeekBar mSeekBar;
    private TextView mSure;

    public LiveBeautyWindow(Context context) {
        super(context);
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        mView=mInflater.inflate(R.layout.window_livebeauty,null);
        mSeekBar= (SeekBar) mView.findViewById(R.id.beautyLevel_seekBar);
        mSure= (TextView) mView.findViewById(R.id.liveBeauty_sure);
        mSure.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

    }
}
