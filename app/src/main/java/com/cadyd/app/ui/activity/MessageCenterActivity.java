package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.cadyd.app.R;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.MessageCountInfo;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.widget.BadgeView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description: 消息中心
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class MessageCenterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.system_badgeView)
    View systemBadgeView;
    @Bind(R.id.order_badgeView)
    View orderBadgeView;
    @Bind(R.id.yidian_badgeView)
    View yidianBadgeView;
    @Bind(R.id.service_badgeView)
    View serviceBadgeView;
    private List<MessageCountInfo> messageListInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_messagecenter, "消息中心", true);
        initView();
    }

    private void initView() {
        HttpMethods.getInstance().getMessageCount(new ProgressSubscriber<BaseRequestInfo<List<MessageCountInfo>>>(new SubscriberOnNextListener<BaseRequestInfo<List<MessageCountInfo>>>() {
            @Override
            public void onNext(BaseRequestInfo<List<MessageCountInfo>> o) {
                messageListInfos = o.getData();
                for (int i = 0; i < messageListInfos.size(); i++) {
                    if (Integer.parseInt(messageListInfos.get(i).getTotalCount()) > 0) {
                        switch (messageListInfos.get(i).getKindCode()){
                            case "100":
                                BadgeView badgeView1 = new BadgeView(MessageCenterActivity.this, systemBadgeView);
                                badgeView1.setText(messageListInfos.get(i).getTotalCount());
                                badgeView1.setTextColor(Color.WHITE);
                                badgeView1.setTextSize(10);
                                badgeView1.show();
                                break;
                            case "200":
                                BadgeView badgeView2 = new BadgeView(MessageCenterActivity.this, orderBadgeView);
                                badgeView2.setText(messageListInfos.get(i).getTotalCount());
                                badgeView2.setTextColor(Color.WHITE);
                                badgeView2.setTextSize(10);
                                badgeView2.show();
                                break;
                            case "300":
                                BadgeView badgeView3 = new BadgeView(MessageCenterActivity.this, yidianBadgeView);
                                badgeView3.setText(messageListInfos.get(i).getTotalCount());
                                badgeView3.setTextColor(Color.WHITE);
                                badgeView3.setTextSize(10);
                                badgeView3.show();
                                break;
                            case "400":
                                BadgeView badgeView4 = new BadgeView(MessageCenterActivity.this, serviceBadgeView);
                                badgeView4.setText(messageListInfos.get(i).getTotalCount());
                                badgeView4.setTextColor(Color.WHITE);
                                badgeView4.setTextSize(10);
                                badgeView4.show();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, MessageCenterActivity.this));
    }

    @Override
    @OnClick({R.id.messageCenter_system, R.id.messageCenter_order, R.id.messageCenter_yidian, R.id.messageCenter_service})
    public void onClick(View v) {
        Intent intent = new Intent(MessageCenterActivity.this, MessageListActivity.class);
        switch (v.getId()) {
            case R.id.messageCenter_system:
                intent.putExtra("kindCode", "100");
                break;
            case R.id.messageCenter_order:
                intent.putExtra("kindCode", "200");
                break;
            case R.id.messageCenter_yidian:
                intent.putExtra("kindCode", "300");
                break;
            case R.id.messageCenter_service:
                intent.putExtra("kindCode", "400");
                break;
        }
        startActivity(intent);
    }

}
