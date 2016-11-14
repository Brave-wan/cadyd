package com.cadyd.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.Login;
import com.cadyd.app.interfaces.OnLoginListener;
import com.cadyd.app.model.ThreeWayBindingModel;
import com.cadyd.app.model.UserInfo;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.EditAlertConfirmation;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.RequestBody;

/**
 * 我的帐号
 */
public class MyAccountFragment extends BaseFragement {

    @Bind(R.id.list_view)
    ListView listView;
    private EditAlertConfirmation editAlertConfirmation;

    private CommonAdapter<ThreeWayBindingModel> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setView(R.layout.fragment_my_account, "我的帐号", true);
        return view;
    }

    @Override
    protected void initView() {
        editAlertConfirmation = new EditAlertConfirmation(activity, "解除绑定", "请输入密码");

        adapter = new CommonAdapter<ThreeWayBindingModel>(activity, models, R.layout.account_list_item) {
            @Override
            public void convert(ViewHolder helper, ThreeWayBindingModel item) {
                View view = helper.getConvertView();
                TextView textView = helper.getView(R.id.bind_text);
                ImageView image = helper.getView(R.id.image);
                switch (item.getUsertype()) {//用户类型（1-微信,2-QQ，，3-新浪微博）
                    case 1:
                        image.setBackgroundResource(R.mipmap.icon_share_weixin);
                        helper.setText(R.id.name_text, "微信");
                        changeText(item.getBindStatus(), textView, view, item, Wechat.NAME);
                        break;
                    case 2:
                        image.setBackgroundResource(R.mipmap.icon_share_qq);
                        helper.setText(R.id.name_text, "QQ");
                        changeText(item.getBindStatus(), textView, view, item, QQ.NAME);
                        break;
                    case 3:
                        image.setBackgroundResource(R.mipmap.icon_share_xinglang);
                        helper.setText(R.id.name_text, "新浪微博");
                        changeText(item.getBindStatus(), textView, view, item, SinaWeibo.NAME);
                        break;
                }
            }
        };
        listView.setAdapter(adapter);
        getThreeWayBinding();
    }

    private void changeText(int state, TextView textView, View view, ThreeWayBindingModel item, final String name) {
        if (state == 1) {
            textView.setBackgroundResource(0);
            textView.setTextColor(getResources().getColor(R.color.text_primary_gray));
            textView.setText(item.getNickname());
            //解除绑定
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editAlertConfirmation.show(new EditAlertConfirmation.OnClickListeners() {
                        @Override
                        public void ConfirmOnClickListener(String string) {
                            editAlertConfirmation.hide();
                            Unbind(name, string);
                        }

                        @Override
                        public void CancelOnClickListener() {
                            editAlertConfirmation.hide();
                        }
                    });
                }
            });
        } else if (state == 2) {
            textView.setBackgroundResource(R.drawable.round_yellow_untransparent);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setText("绑定");
            //绑定
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("huang", "绑定" + name);
                    Login.login(activity, name, new OnLoginListener() {
                        @Override
                        public void onLogin(UserInfo userInfo) {
                            userInfo.setPlatformName(name);
                            setBinding(userInfo);
                        }
                    });
                }
            });
        }
    }

    /**
     * 查询三方绑定
     */
    private List<ThreeWayBindingModel> models = new ArrayList<>();

    private void getThreeWayBinding() {
        HttpMethods.getInstance().getThreeWayBinding(new ProgressSubscriber<BaseRequestInfo<List<ThreeWayBindingModel>>>(new SubscriberOnNextListener<BaseRequestInfo<List<ThreeWayBindingModel>>>() {
            @Override
            public void onNext(BaseRequestInfo<List<ThreeWayBindingModel>> o) {
                models.clear();
                models.addAll(o.getData());
                Log.i("huang", "---------------------查询三方绑定" + models.size());
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity));

    }

    /**
     * 绑定三方账户
     */
    private void setBinding(UserInfo userInfo) {
        Map<Object, Object> map = new HashMap<>();
//"openid":"123456"//openeid,类型：String
//"nickname":"张三",//昵称，类型：String
//"usertype":"1" //用户类型（1-微信，2-QQ，3-新浪微博），类型：int
        map.put("openid", userInfo.getToken());
        map.put("nickname", userInfo.getUserName());
        int type = 0;
        if (QQ.NAME.equals(userInfo.getPlatformName())) {
            type = 2;
        } else if (Wechat.NAME.equals(userInfo.getPlatformName())) {
            type = 1;
        } else if (SinaWeibo.NAME.equals(userInfo.getPlatformName())) {
            type = 3;
        }
        map.put("usertype", type);
        RequestBody body = ApiClient.getRequestBody(map, true);
        HttpMethods.getInstance().setBinding(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                toastSuccess("绑定成功");
                getThreeWayBinding();
            }

            @Override
            public void onError(Throwable e) {
                toastError("绑定失败");
            }

            @Override
            public void onLogicError(String msg) {
                toastError("绑定失败");
            }
        }, activity), body);
    }

    /**
     * 解除绑定
     */
    private void Unbind(String Type, String password) {
//        "usertype":"1",//用户类型（1-微信，2-QQ，3-新浪微博） 类型：String
//        "password":"123456"//用户密码 类型：String
        Map<Object, Object> map = new HashMap<>();
        int type = 0;
        if (QQ.NAME.equals(Type)) {
            type = 2;
        } else if (Wechat.NAME.equals(Type)) {
            type = 1;
        } else if (SinaWeibo.NAME.equals(Type)) {
            type = 3;
        }
        map.put("usertype", type);
        map.put("password", password);
        RequestBody body = ApiClient.getRequestBody(map, true);
        HttpMethods.getInstance().Unbind(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                toastSuccess("解除绑定成功");
                getThreeWayBinding();
            }

            @Override
            public void onError(Throwable e) {
                toastError("解除失败");
            }

            @Override
            public void onLogicError(String msg) {
                toastError("解除失败");
            }
        }, activity), body);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
