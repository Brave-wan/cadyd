package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.LiveEndInfo;
import com.cadyd.app.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LiveEndActivity extends BaseActivity {
    @Bind(R.id.live_end_header)
    ImageView liveEndHeader;
    @Bind(R.id.live_end_personal)
    TextView liveEndPersonal;
    @Bind(R.id.live_end_spend)
    TextView liveEndSpend;
    @Bind(R.id.liveEnde_liveMinutes)
    TextView liveMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_end);
        ButterKnife.bind(this);
        LiveEndHttp();
    }

    /**
     * 结束直播
     */
    private void LiveEndHttp() {
        HttpMethods.getInstance().doLiveEnd(new ProgressSubscriber<BaseRequestInfo<LiveEndInfo>>(new SubscriberOnNextListener<BaseRequestInfo<LiveEndInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<LiveEndInfo> o) {
                ApiClient.displayImage(o.getData().getHeadImageUrl(), liveEndHeader);
                liveEndPersonal.setText(o.getData().getScoreIncome() + "");
                liveEndSpend.setText(o.getData().getVisitCount() + "");
                liveMinutes.setText("直播时间：" + o.getData().getLiveMinutes()+" 分钟");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, LiveEndActivity.this));
    }

    public void BackHomeClick(View v) {
        finish();
    }
}
