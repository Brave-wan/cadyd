package com.cadyd.app.ui.fragment.mall;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.GoodsTheSunModel;
import com.cadyd.app.model.IntegralListData;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.user.integral.IntegralEvaluateFragment;
import com.cadyd.app.ui.fragment.user.OrderEvaluateFragment;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评价晒单列表
 *
 * @author wcy
 */
public class GoodsEvaluateFragment extends BaseFragement {
    @Bind(R.id.list_view)
    ListView list_view;
    private String orderid;
    private String type;
    List<GoodsTheSunModel> mlist;

    public static GoodsEvaluateFragment newInstance(String orderid, String type) {
        GoodsEvaluateFragment newFragment = new GoodsEvaluateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderid", orderid);
        bundle.putString("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.orderid = args.getString("orderid");
            this.type = args.getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_goods_evaluate, "评价晒单", true);
    }

    @Override
    protected void initView() {
        loading();
    }

    private void loading() {
        Map<String, Object> map = new HashMap<>();
        if ("mall".equals(type)) {
            map.put("oid", orderid);
            MallHttp(JConstant.UNCOMMENT_, map);
        } else {
            map.put("id", orderid);
            IntegralHttp(JConstant.INTEGRALSBYOID, map);
        }

    }

    /**
     * 商城的网络请求及数据处理
     */
    private void MallHttp(int url, Map<String, Object> map) {
        ApiClient.send(activity, url, map, GoodsTheSunModel.getType(), new DataLoader<List<GoodsTheSunModel>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<GoodsTheSunModel> data) {
                if (data != null && data.size() > 0) {
                    CommonAdapter<GoodsTheSunModel> adapter = new CommonAdapter<GoodsTheSunModel>(activity, data, R.layout.goods_evaluate_iteam) {
                        @Override
                        public void convert(ViewHolder helper, final GoodsTheSunModel item) {
                            helper.setText(R.id.name, item.goodsName);
                            ImageView iv = helper.getView(R.id.image_view);
                            ApiClient.displayImage(item.thumb, iv);
                            TextView evaluate = helper.getView(R.id.evaluate);
                            if (item.commentstate == 1) {//已评论
                                evaluate.setText("已经评价");
                                evaluate.setEnabled(false);
                            } else if (item.commentstate == 0) {//未评论
                                evaluate.setText("发表评价");
                                evaluate.setEnabled(true);
                                evaluate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //评价跳转
                                        replaceFragment(R.id.common_frame, OrderEvaluateFragment.newInstance(item));
                                    }
                                });
                            }
                        }
                    };
                    list_view.setAdapter(adapter);
                }
            }

            @Override
            public void error(String message) {

            }
        }, url);
    }

    /**
     * 积分商城的网络请求及数据处理
     */
    private void IntegralHttp(int url, Map<String, Object> map) {
        ApiClient.send(activity, url, map, IntegralListData.class, new DataLoader<IntegralListData>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(IntegralListData data) {
                if (data != null && data.orderIntegralGoods != null && data.orderIntegralGoods.size() > 0) {
                    CommonAdapter<IntegralListData.OrderIntegralGoods> adapter = new CommonAdapter<IntegralListData.OrderIntegralGoods>(activity, data.orderIntegralGoods, R.layout.goods_evaluate_iteam) {
                        @Override
                        public void convert(ViewHolder helper, final IntegralListData.OrderIntegralGoods item) {
                            helper.setText(R.id.name, item.goodsName);
                            ImageView iv = helper.getView(R.id.image_view);
                            ApiClient.displayImage(item.path, iv);
                            TextView evaluate = helper.getView(R.id.evaluate);
                            if (item.commentstate == 1) {//已评论
                                evaluate.setText("已经评价");
                                evaluate.setEnabled(false);
                            } else if (item.commentstate == 0) {//未评论
                                evaluate.setText("发表评价");
                                evaluate.setEnabled(true);
                                evaluate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //评价跳转
                                        replaceFragment(R.id.common_frame, IntegralEvaluateFragment.newInstance(item, orderid));
                                    }
                                });
                            }
                        }
                    };
                    list_view.setAdapter(adapter);
                }
            }

            @Override
            public void error(String message) {

            }
        }, url);
    }


    @Override
    public boolean onBackPressed() {
        activity.setResult(Activity.RESULT_CANCELED);
        finishActivity();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.UNCOMMENT_);
        ApiClient.cancelRequest(JConstant.INTEGRALSBYOID);
    }
}
