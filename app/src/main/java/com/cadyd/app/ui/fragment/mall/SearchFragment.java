package com.cadyd.app.ui.fragment.mall;

import java.util.List;

import android.view.Gravity;
import android.widget.*;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.AppManager;
import com.cadyd.app.R;
import com.cadyd.app.adapter.SearchHistoryAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.mall.GoodsSelectFragment;

import org.wcy.common.utils.StringUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 商品搜索
 */

public class SearchFragment extends BaseFragement {
    @Bind(R.id.listview)
    ListView listview;

    private PopupWindow popupShop;
    private String isShop = "pre"; //店铺 shop , 宝贝 pre
    @Bind(R.id.check_baby)//搜索店铺或宝贝选择
            TextView check_baby;

    @Bind(R.id.search_text)
    EditText search;
    @Bind(R.id.search_delete)
    LinearLayout search_delete;
    @Bind(R.id.titleLayout)
    RelativeLayout title;

    List<String> list;

    @Bind(R.id.listviewEmpty)
    View viewEmpty;

    SearchHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return setView(inflater, R.layout.search);

    }

    /**
     * 宝贝选择监听
     */
    @OnClick(R.id.check_baby)
    public void onCheck(View view) {
        shopCheckPopup(check_baby);
    }

    @OnClick(R.id.search_btn)
    public void onSearchBtn(View view) {
        if (StringUtil.hasText(search.getText().toString())) {
            ApiClient.addSearchHistory(search.getText()
                    .toString());
            replaceFragment(R.id.common_frame, GoodsSelectFragment.newInstance(search.getText().toString(), isShop));
        } else {
            toast("请输入搜索内容");
        }
    }

    @OnClick(R.id.delete_btn)
    public void onDeleteBtn(View view) {
        ApiClient.deleteSearchHistory();
        listview.setVisibility(View.GONE);
        search_delete.setVisibility(View.GONE);
        viewEmpty.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.back)
    public void onBackBtn(View view) {
        AppManager.getAppManager().finishActivity(activity);
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void initView() {
      //  title.setBackgroundColor(getResources().getColor(R.color.title_bg));
        list = ApiClient.getListSearchHistory(list, search.getText().toString());
        adapter = new SearchHistoryAdapter(list, getActivity());
        listview.setAdapter(adapter);
        if (list.size() <= 0) {
            listview.setVisibility(View.GONE);
            search_delete.setVisibility(View.GONE);
            viewEmpty.setVisibility(View.VISIBLE);
        } else {
            listview.setVisibility(View.VISIBLE);
            search_delete.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        }

        setListViewHeightBasedOnChildren(listview);

        search_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.deleteSearchHistory();
                list.clear();
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listview);
                listview.setVisibility(View.GONE);
                search_delete.setVisibility(View.GONE);
                viewEmpty.setVisibility(View.VISIBLE);
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String keyword = ((TextView) arg1).getText().toString();
                replaceFragment(R.id.common_frame, GoodsSelectFragment.newInstance(keyword, isShop));
            }
        });
    }

    /**
     * 店铺货宝贝的选择window
     *
     * @param showView
     */
    private void shopCheckPopup(View showView) {
        if (popupShop == null) {
            popupShop = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.shop_popup, null);

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.parent_layout);
            int[] position = new int[2];
            check_baby.getLocationOnScreen(position);
            int h = position[1] + check_baby.getHeight();
            int w = position[0];
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            params.setMargins(w, h, 0, 0);
            final TextView baby = (TextView) view.findViewById(R.id.check_baby);
            final TextView shop = (TextView) view.findViewById(R.id.check_shop);

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.pp_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupShop.dismiss();
                }
            });

            baby.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShop = "pre";
                    check_baby.setText(baby.getText().toString());
                    popupShop.dismiss();
                }
            });

            shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShop = "shop";
                    check_baby.setText(shop.getText().toString());
                    popupShop.dismiss();
                }
            });

            popupShop.setContentView(view);
        }
        popupShop.showAtLocation(showView, Gravity.CENTER, 0, 0);
        //    popupShop.showAsDropDown(showView);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }
}
