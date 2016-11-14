package com.cadyd.app.ui.fragment.mall;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.Bind;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.Findetailgoods;
import com.cadyd.app.ui.base.BaseFragement;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品顶部信息
 *
 * @author wcy
 */
public class GoodsDetailBottomFragment extends BaseFragement implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.webView)
    WebView preWebView;
    @Bind(R.id.findetailgoods_list_view)
    ListView findetailgoods_list_view;
    @Bind(R.id.detail_rg)
    RadioGroup detail_rg;
    @Bind(R.id.pack_info)
    TextView pack_info;
    private String gid;
    private boolean hasInited = false;

    public static GoodsDetailBottomFragment newInstance(String id) {
        GoodsDetailBottomFragment newFragment = new GoodsDetailBottomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.gid = args.getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_goods_detail_bottom);
    }

    public void showView() {
        if (!hasInited) {
            preWebView.setInitialScale(50);//这里一定要设置，数值可以根据各人的需求而定，我这里设置的是50%的缩放
            WebSettings webSettings = preWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setBuiltInZoomControls(true);// support zoom
            /**设置webview自适应屏幕大小*/
            webSettings.setUseWideViewPort(true);// 这个很关键
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持重新排版(单列)
            webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
            loadingGoodsDetail();
            hasInited = true;
        }
    }

    /**
     * 商品更多信息
     */
    private void loadingGoodsDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", gid);
        int tag = JConstant.FINDETAILGOODS_;
        ApiClient.send(activity, JConstant.FINDETAILGOODS_, map, true, Findetailgoods.getType(), new DataLoader<List<Findetailgoods>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Findetailgoods> data) {
                if (data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        Findetailgoods fg = data.get(i);
                        if ("des".equals(fg.attrname)) {
                            showWeb(fg.mixfield);
                            data.remove(fg);
                        }
                    }
                    CommonAdapter<Findetailgoods> adapterFg = new CommonAdapter<Findetailgoods>(activity, data, R.layout.list_goods_detail_parameters_iteam) {
                        @Override
                        public void convert(ViewHolder helper, Findetailgoods item) {
                            helper.setText(R.id.des, item.attrdes);
                            helper.setText(R.id.mixfield, item.mixfield);
                        }
                    };
                    findetailgoods_list_view.setAdapter(adapterFg);
                }
            }

            @Override
            public void error(String message) {

            }
        }, tag);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.goods_image_info_rb:
                preWebView.setVisibility(View.VISIBLE);
                findetailgoods_list_view.setVisibility(View.GONE);
                pack_info.setVisibility(View.GONE);
                break;
            case R.id.goods_standard_info_rb:
                preWebView.setVisibility(View.GONE);
                pack_info.setVisibility(View.GONE);
                findetailgoods_list_view.setVisibility(View.VISIBLE);
                break;
            case R.id.goods_pack_info_rb:
                preWebView.setVisibility(View.GONE);
                pack_info.setVisibility(View.VISIBLE);
                findetailgoods_list_view.setVisibility(View.GONE);
                break;
            default:
                preWebView.setVisibility(View.GONE);
                pack_info.setVisibility(View.GONE);
                findetailgoods_list_view.setVisibility(View.GONE);
                break;
        }
    }

    public void setPackages(String packages) {
        pack_info.setText(packages);
    }

    @Override
    protected void initView() {
        detail_rg.setOnCheckedChangeListener(this);
    }

    public void noChange() {
        if (detail_rg == null) {
            detail_rg = (RadioGroup) activity.findViewById(R.id.detail_rg);
        }
        detail_rg.setVisibility(View.GONE);
    }

    private void showWeb(String html) {
        StringBuilder data = new StringBuilder("<html><body bgcolor=\"#F2F6F8\">");
        data.append("<meta name='viewport' content='width=320' />");
        data.append(html);
        data.append("</body></html>");
        preWebView.loadDataWithBaseURL("", data.toString(), "text/html", "UTF-8", "");
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.FINDETAILGOODS_);
    }
}
