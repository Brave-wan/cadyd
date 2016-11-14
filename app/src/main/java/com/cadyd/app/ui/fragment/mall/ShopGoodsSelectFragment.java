package com.cadyd.app.ui.fragment.mall;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.AppManager;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.ScreenPopuAdapter;
import com.cadyd.app.adapter.SelectGoodsAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.ForPxTp;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.Key;
import com.cadyd.app.model.SelectGoodsModle;
import com.cadyd.app.model.SelectGoodsModleData;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.wcy.android.widget.EmptyLayout;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品类型分类
 */
public class ShopGoodsSelectFragment extends BaseFragement {

    private String id;

    public static ShopGoodsSelectFragment newInstance(String gid) {
        ShopGoodsSelectFragment newFragment = new ShopGoodsSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gid", gid);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.id = args.getString("gid");
        }
    }

    @Bind(R.id.big_layout)
    LinearLayout big_layout;

    private int TYPE = 1;//判断选中了哪一项
    private TextView oldTextView;

    private PopupWindow PopupScreen;

    //宝贝选择
    @Bind(R.id.check_baby)
    TextView check_baby;

    //搜索内容
    @Bind(R.id.search_content)
    TextView searchContent;
    //新品
    @Bind(R.id.select_new)
    TextView selectNew;

    //销量
    @Bind(R.id.sales_volume)
    TextView salesVolume;

    @Bind(R.id.titleLayout)
    LinearLayout title;
    //价格
    @Bind(R.id.sales_price)
    TextView salesPrice;

    //筛选
    @Bind(R.id.sales_screen)
    TextView salesScreen;
    @Bind(R.id.check_linear)
    LinearLayout check_linear;

    @Bind(R.id.empty_layout)
    RelativeLayout empty_layout;
    EmptyLayout mEmptyLayout;

    private List<TextView> textViews = new ArrayList<>();
    private List<String> names;

    @Bind(R.id.select_list)
    PullToRefreshListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_shop_goods_select);
    }

    @Override
    protected void initView() {
        names = new ArrayList<>();
        names.add("综合");
        names.add("新品");
        names.add("评分");
        oldTextView = selectNew;
        salesPrice.setTag(1);
        salesVolume.setTag(1);
        mEmptyLayout = new EmptyLayout(activity, empty_layout);
        preKey();
        initHttp();
        title.setBackgroundColor(getResources().getColor(R.color.title_bg));
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                selectGoodsModleDatas.clear();
                pageIndex = 1;
                initHttp();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                initHttp();
            }
        });


        searchContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_SEARCH);
                getActivity().startActivity(intent);
                AppManager.getAppManager().finishActivity(activity);
            }
        });

        //新品
        selectNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInit();
            }
        });

        //销量
        salesVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE == 1) {
                    oldTextView.setTextColor(getResources().getColor(R.color.text_primary_gray));
                    oldTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrow_down, 0);
                } else if (TYPE == 3) {
                    oldTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.doulbe_arrows_ico, 0);
                    oldTextView.setTextColor(getResources().getColor(R.color.text_primary_gray));
                } else if (TYPE == 2) {
                    int image = 0;
                    int type = (int) salesVolume.getTag();
                    type = (type + 1) % 2;
                    switch (type) {
                        case 0:
                            image = R.mipmap.doulbe_arrows_up_ico;
                            dnumOrder = "asc";
                            break;
                        case 1:
                            image = R.mipmap.doulbe_arrows_down_ico;
                            dnumOrder = "desc";
                            break;
                    }
                    salesVolume.setTag(type);
                    salesVolume.setCompoundDrawablesWithIntrinsicBounds(0, 0, image, 0);
                    selectGoodsModleDatas.clear();
                    pageIndex = 1;
                    initHttp();
                }
                if (TYPE != 2) {
                    //设置价格的状态
                    int image = 0;
                    int type = (int) salesVolume.getTag();
                    type = (type + 1) % 2;
                    switch (type) {
                        case 0:
                            image = R.mipmap.doulbe_arrows_up_ico;
                            dnumOrder = "asc";
                            break;
                        case 1:
                            image = R.mipmap.doulbe_arrows_down_ico;
                            dnumOrder = "desc";
                            break;
                    }
                    salesVolume.setTag(type);
                    salesVolume.setTextColor(getResources().getColor(R.color.red));
                    salesVolume.setCompoundDrawablesWithIntrinsicBounds(0, 0, image, 0);
                    oldTextView = salesVolume;
                    TYPE = 2;
                    field = "dnum";
                    selectGoodsModleDatas.clear();
                    pageIndex = 1;
                    initHttp();
                }
            }
        });

        //价格
        salesPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE == 1) {
                    oldTextView.setTextColor(getResources().getColor(R.color.text_primary_gray));
                    oldTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrow_down, 0);
                } else if (TYPE == 2) {
                    oldTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.doulbe_arrows_ico, 0);
                    oldTextView.setTextColor(getResources().getColor(R.color.text_primary_gray));
                } else if (TYPE == 3) {
                    int image = 0;
                    int type = (int) salesPrice.getTag();
                    type = (type + 1) % 2;
                    switch (type) {
                        case 0:
                            image = R.mipmap.doulbe_arrows_up_ico;
                            order = "asc";
                            break;
                        case 1:
                            image = R.mipmap.doulbe_arrows_down_ico;
                            order = "desc";
                            break;
                    }
                    salesPrice.setTag(type);
                    salesPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, image, 0);
                    selectGoodsModleDatas.clear();
                    pageIndex = 1;
                    initHttp();
                }
                if (TYPE != 3) {
                    //设置价格的状态
                    int image = 0;
                    int type = (int) salesPrice.getTag();
                    type = (type + 1) % 2;
                    switch (type) {
                        case 0:
                            image = R.mipmap.doulbe_arrows_up_ico;
                            order = "asc";
                            break;
                        case 1:
                            image = R.mipmap.doulbe_arrows_down_ico;
                            order = "desc";
                            break;
                    }
                    salesPrice.setTag(type);
                    salesPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, image, 0);
                    salesPrice.setTextColor(getResources().getColor(R.color.red));
                    oldTextView.setTextColor(getResources().getColor(R.color.text_primary_gray));
                    oldTextView = salesPrice;
                    TYPE = 3;
                    field = "price";
                    selectGoodsModleDatas.clear();
                    pageIndex = 1;
                    initHttp();
                }
            }
        });

        //筛选
        salesScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PopupScreen != null) {
                    PopupScreen.showAtLocation(big_layout, Gravity.CENTER, 0, 0);
                }
            }
        });


    }

    @OnClick(R.id.select_back)
    public void onBack(View view) {
        finishFramager();
    }

    private PopupWindow popupWindow;

    //第二排选择的功能
    private void checkInit() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.list_popu, null);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.LinearLayout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            TextView line = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
            line.setLayoutParams(params);
            line.setBackgroundColor(getResources().getColor(R.color.white_light_transparent));
            layout.addView(line);

            for (int i = 0; i < names.size(); i++) {
                //获得在该控件的位置
                final TextView textView;
                if (i == 0) {
                    textView = NewText(0);
                    textView.setTextColor(getResources().getColor(R.color.red));
                    textView.setPadding(20, 0, 20, 0);
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rad_gou, 0);
                } else {
                    textView = NewText(0);
                }
                textView.setText(names.get(i));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < textViews.size(); i++) {
                            textViews.get(i).setTextColor(getResources().getColor(R.color.text_primary_gray));
                            textViews.get(i).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        }
                        textView.setTextColor(getResources().getColor(R.color.red));
                        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rad_gou, 0);

                        if (TYPE == 2) {
                            oldTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.doulbe_arrows_ico, 0);
                            oldTextView.setTextColor(getResources().getColor(R.color.text_primary_gray));
                        } else if (TYPE == 3) {
                            oldTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.doulbe_arrows_ico, 0);
                            oldTextView.setTextColor(getResources().getColor(R.color.text_primary_gray));
                        } else {
                            oldTextView.setTextColor(getResources().getColor(R.color.text_primary_gray));
                        }
                        selectNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rad_arrow_down, 0);
                        selectNew.setTextColor(getResources().getColor(R.color.red));
                        if (selectNew.getText().toString().equals(textView.getText().toString())) {
                            popupWindow.dismiss();
                        } else {
                            if ("综合".equals(textView.getText().toString())) {
                                field = "overall";
                            } else if ("新品".equals(textView.getText().toString())) {
                                field = "new";
                            } else if ("评分".equals(textView.getText().toString())) {
                                field = "score";
                            }
                            selectNew.setText(textView.getText().toString());
                            oldTextView = selectNew;
                            TYPE = 1;
                            popupWindow.dismiss();
                            selectGoodsModleDatas.clear();
                            pageIndex = 1;
                            initHttp();
                        }
                    }
                });
                //设置默认
                textViews.add(textView);
                layout.addView(textView);
                layout.addView(view());
            }
            popupWindow.setContentView(view);
            popupWindow.showAsDropDown(check_linear);
        } else {
            popupWindow.showAsDropDown(check_linear);
        }
    }

    private TextView NewText(int Y) {
        TextView textView = new TextView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ForPxTp.dip2px(activity, 40f));
        params.setMargins(0, Y, 0, 0);
        textView.setLayoutParams(params);
        textView.setPadding(20, 0, 20, 0);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setBackgroundResource(R.color.white);
        textView.setTextSize(14);
        return textView;
    }

    private View view() {
        View textView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ForPxTp.dip2px(activity, 1f));
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.color.line_bg);
        return textView;
    }

    private List<Key> KeyList;

    //获得搜索宝贝的筛选信息
    private void preKey() {
        Map<String, Object> map = new HashMap<>();
        map.put("menuId", id);
        ApiClient.send(activity, JConstant.CLASSIFYMENUID_, map, Key.getType(), new DataLoader<List<Key>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<Key> data) {
                KeyList = data;
                KeyList.add(0, new Key());
                PopupScreen = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                //设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
                PopupScreen.setFocusable(true);
                //这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
                PopupScreen.setBackgroundDrawable(new BitmapDrawable());
                //防止虚拟软键盘被弹出菜单遮住
                PopupScreen.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                LayoutInflater inflater = LayoutInflater.from(activity);
                View view = inflater.inflate(R.layout.screen_popu, null);
                ListView listView = (ListView) view.findViewById(R.id.screen_popu_list);
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupScreen.dismiss();
                    }
                });
                final ScreenPopuAdapter adapter = new ScreenPopuAdapter(activity, KeyList);
                adapter.setClick(new TowParameterInterface() {
                    @Override
                    public void Onchange(int type, String conten) {
                        if (start.equals(String.valueOf(type)) && end.equals(conten)) {
                            PopupScreen.dismiss();
                        } else {
                            start = String.valueOf(type);
                            end = conten;
                            PopupScreen.dismiss();
                            selectGoodsModleDatas.clear();
                            pageIndex = 1;
                            initHttp();
                        }
                    }
                });
                final EditText startE = (EditText) view.findViewById(R.id.screen_popu_star);
                final EditText endE = (EditText) view.findViewById(R.id.screen_popu_end);
                Button button = (Button) view.findViewById(R.id.screen_popu_send);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (start.equals(startE.getText().toString()) && end.equals(endE.getText().toString())) {
                            PopupScreen.dismiss();
                        } else {
                            start = startE.getText().toString();
                            end = endE.getText().toString();
                            PopupScreen.dismiss();
                            selectGoodsModleDatas.clear();
                            pageIndex = 1;
                            initHttp();
                        }


                    }
                });

                listView.setAdapter(adapter);
                PopupScreen.setContentView(view);
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.CLASSIFYMENUID_);
    }

    private SelectGoodsAdapter adapter;
    private int pageIndex = 1;
    private int totalPage;
    private String field = "overall";//新品 new , 评分 score , 综合 overall
    private String order = "";//asc价格上 desc价格下
    private String dnumOrder = "";
    private String start = "-1";
    private String end = "-1";

    public String isAppend = null;//如果有这个参数则表示为全球购

    private List<SelectGoodsModleData> selectGoodsModleDatas = new ArrayList<>();

    private void initHttp() {
        Map<String, Object> map = new HashMap<>();

        map.put("menuId", id);
        map.put("pageIndex", pageIndex);
        //综合 新品 评分
        map.put("field", field);
        if ("price".equals(field)) {//价格  是否选择价格排序
            map.put("order", order);
        }
        if ("dnum".equals(field)) {//销量  是否选择价格排序
            map.put("order", dnumOrder);
        }
        //判断用户是否进行了筛选
        if ("-1".equals(start) && "-1".equals(end)) {//没有进行筛选
        } else {//有进行筛选
            map.put("start", start);
            map.put("end", end);
        }

        //判断是否需要拼接
        if (StringUtil.hasText(isAppend)) {
            map.put("isglobal", 0);
        }


        ApiClient.send(activity, JConstant.FINDMENU_, map, SelectGoodsModle.class, new DataLoader<SelectGoodsModle>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(SelectGoodsModle data) {
                listView.onRefreshComplete();
                if (data != null && data.data != null) {
                    totalPage = data.totalPage;
                    selectGoodsModleDatas.addAll(data.data);
                    if (selectGoodsModleDatas.size() <= 0) {
                        mEmptyLayout.showEmpty();
                    } else {
                        mEmptyLayout.showView();
                    }
                    if (adapter == null) {
                        adapter = new SelectGoodsAdapter(activity, selectGoodsModleDatas, "pre");
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    if (pageIndex == totalPage) {
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        listView.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                } else {
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            @Override
            public void error(String message) {
                listView.onRefreshComplete();
            }
        }, JConstant.FINDMENU_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.CLASSIFYMENUID_);
        ApiClient.cancelRequest(JConstant.FINDMENU_);
    }
}
