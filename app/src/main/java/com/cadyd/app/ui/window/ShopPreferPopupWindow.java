package com.cadyd.app.ui.window;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.adapter.PackageAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.LeaguePrefer;
import com.cadyd.app.model.Sales;
import com.cadyd.app.ui.view.DividerListItemDecoration;
import com.cadyd.app.ui.view.toast.ToastUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.common.utils.NumberUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销
 * Created by wcy on 2016/5/25.
 */
public class ShopPreferPopupWindow extends BottomPushPopupWindow<List<LeaguePrefer>> {
    ListView listView;

    public ShopPreferPopupWindow(Activity activity, List<LeaguePrefer> data) {
        super(activity, data);
    }

    @Override
    protected View generateCustomView(List<LeaguePrefer> data) {
        View view = View.inflate(bActivity, R.layout.shop_prefer_popuo_window, null);
        listView = (ListView) view.findViewById(R.id.list_view);
        CommonAdapter adapter = new CommonAdapter<LeaguePrefer>(bActivity, data, R.layout.league_prefer_iteam) {
            @Override
            public void convert(ViewHolder helper, final LeaguePrefer item) {
                helper.setText(R.id.title, item.title);
                helper.setText(R.id.shop_name, item.name);
                helper.setText(R.id.valid, String.format("有效期：%s", item.invalid));
                helper.setText(R.id.price, String.format("￥%d", item.money));
                View league_bg = helper.getView(R.id.league_bg);
                TextView receive = helper.getView(R.id.receive);
                if (item.money <= 20) {
                    league_bg.setBackgroundResource(R.mipmap.league_prefer_one);
                    receive.setBackgroundResource(R.drawable.league_prefer_one_bg);
                    receive.setTextColor(mContext.getResources().getColor(R.color.league_prefer_one));
                } else if (item.money > 20 && item.money <= 50) {
                    league_bg.setBackgroundResource(R.mipmap.league_prefer_two);
                    receive.setBackgroundResource(R.drawable.league_prefer_two_bg);
                    receive.setTextColor(mContext.getResources().getColor(R.color.league_prefer_two));
                } else if (item.money > 50 && item.money <= 100) {
                    league_bg.setBackgroundResource(R.mipmap.league_prefer_three);
                    receive.setBackgroundResource(R.drawable.league_prefer_three_bg);
                    receive.setTextColor(mContext.getResources().getColor(R.color.league_prefer_three));
                } else {
                    league_bg.setBackgroundResource(R.mipmap.league_prefer_four);
                    receive.setBackgroundResource(R.drawable.league_prefer_four_bg);
                    receive.setTextColor(mContext.getResources().getColor(R.color.league_prefer_four));
                }

                receive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("preferid", item.id);
                        ApiClient.send(bActivity, JConstant.PREFER_RECEIVE_, map, true, null, new DataLoader<Object>() {
                            @Override
                            public void task(String data) {

                            }

                            @Override
                            public void succeed(Object data) {
                                ToastUtils.show(mContext, "领取成功", ToastUtils.SUCCESS_TYPE);
                            }

                            @Override
                            public void error(String message) {

                            }
                        }, JConstant.PREFER_RECEIVE_);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        view.findViewById(R.id.colse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
