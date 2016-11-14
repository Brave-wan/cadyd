package com.cadyd.app.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.UserVipListAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.IntegeralListInfo;
import com.cadyd.app.ui.activity.MemberRuleActivity;
import com.cadyd.app.ui.base.BaseFragement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心会员信息
 */
public class UserVipInformationFragment extends BaseFragement {

    @Bind(R.id.vip_massage)
    LinearLayout massageLinearLayout;
    @Bind(R.id.vip_text)
    TextView vipText;
    @Bind(R.id.integral)
    TextView integral;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private UserVipListAdapter adapter;
    private int pageNo = 1;
    private int totalCount = 0;
    private List<IntegeralListInfo> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_user_vip_information, "会员等级", true, "积分规则");
    }

    @Override
    protected void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView) && datas.size() < totalCount) {
                    loadDatas();
                }
            }
        });

        datas = new ArrayList<>();
        adapter = new UserVipListAdapter(datas);
        recyclerView.setAdapter(adapter);

        integral.setText(getString(R.string.member_vip_count, 0));

        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MemberRuleActivity.class, false);
            }
        });

        loadDatas();
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void showMemberLevel(String vipgrade) {
        float index = Float.valueOf(vipgrade);
        int s = (int) (index * 10) % 10;
        int g = (int) (index % 10);
        switch (g) {
            case 1://花童
                vipText.setText("  花童  ");
                break;
            case 2://花学士
                vipText.setText("  花学士  ");
                break;
            case 3://花博士
                vipText.setText("  花博士  ");
                break;
            case 4://花仙
                vipText.setText("  花仙  ");
                break;
        }
        //删除以前的花
        int num = massageLinearLayout.getChildCount();
        if (num >= 1) {
            for (int i = 0; i < num - 1; i++) {
                massageLinearLayout.removeViewAt(i + 1);
            }
        }
        //添加花
        for (int j = 0; j < s; j++) {
            ImageView imageView = new ImageView(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 0, 0);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.mipmap.vip_hua);
            massageLinearLayout.addView(imageView, 1);
        }
    }

    private void loadDatas() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", pageNo);

        ApiClient.send(activity, JConstant.INQUIRYINTEGRAL, params, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                try {
                    JSONObject obj = new JSONObject(data);

                    totalCount = obj.optInt("totalCount");

                    JSONArray list = obj.getJSONArray("data");
                    if (list.length() > 0) {
                        JSONObject temp = list.getJSONObject(0);

                        JSONObject info = temp.getJSONObject("info");
                        JSONArray detail = temp.getJSONArray("detail");

                        if (detail.length() > 0) {
                            List<IntegeralListInfo> result = ApiClient.getGson().fromJson(detail.toString(), IntegeralListInfo.getType());
                            if (result != null && result.size() > 0) {
                                datas.addAll(result);
                                adapter.notifyDataSetChanged();
                                pageNo += 1;
                            }
                        }

                        if (info != null) {
                            showMemberLevel(info.optString("vipgrade"));
                            integral.setText(getString(R.string.member_vip_count, info.optString("historyIntegral")));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.INQUIRYINTEGRAL);
    }


    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.INQUIRYINTEGRAL);
    }
}
