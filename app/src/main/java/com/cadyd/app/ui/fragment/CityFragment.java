package com.cadyd.app.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.AreaInfo;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.MyLetterListView;
import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.widget.NoScrollGridView;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 一级城市列表
 *
 * @author wangchaoyong
 */
public class CityFragment extends BaseFragement {
    private BaseAdapter adapter;
    @Bind(R.id.city_list)
    ListView mCityLit;
    private TextView overlay;
    @Bind(R.id.cityLetterListView)
    MyLetterListView letterListView;
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread;
    private List<AreaInfo> mCityNames;
    boolean isallcity;
    private List<AreaInfo> holdName = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setView(R.layout.city_list, "城市列表", true);
        initOverlay();
        return view;
    }

    @Override
    protected void initView() {
        isallcity = activity.getIntent().getBooleanExtra("issave", false);
        mCityNames = new ArrayList<AreaInfo>();
        AreaInfo area = PreferenceUtils.getObject(activity, AreaInfo.class);//获取GPS的定位列表
        if (area != null && StringUtil.hasText(area.id)) {
            mCityNames.add(area);
        } else {
            AreaInfo areaInfo = new AreaInfo();
            areaInfo.name = "全国";
            mCityNames.add(areaInfo);
        }
        List<AreaInfo> areaInfoList = new ArrayList<>();
        areaInfoList.addAll(ApiClient.getArea(isallcity));
        mCityNames.addAll(areaInfoList);//从数据库中读取地区列表

        for (int i = 0; i < areaInfoList.size(); i++) {
            if ("北京市".equals(areaInfoList.get(i).name)) {
                holdName.add(areaInfoList.get(i));
            } else if ("上海市".equals(areaInfoList.get(i).name)) {
                holdName.add(areaInfoList.get(i));
            } else if ("广州市".equals(areaInfoList.get(i).name)) {
                holdName.add(areaInfoList.get(i));
            } else if ("深圳市".equals(areaInfoList.get(i).name)) {
                holdName.add(areaInfoList.get(i));
            } else if ("成都市".equals(areaInfoList.get(i).name)) {
                holdName.add(areaInfoList.get(i));
            }
        }

        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        setAdapter(mCityNames);
        mCityLit.setOnItemClickListener(new CityListOnItemClick());
    }

    /**
     * 城市列表点击事件
     *
     * @author sy
     */
    class CityListOnItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
            AreaInfo cityModel = (AreaInfo) mCityLit.getAdapter().getItem(pos);
            if (cityModel == null || !StringUtil.hasText(cityModel.id)) {
                CityInfo city = new CityInfo();
                city.name = "全国";
                Intent intent = activity.getIntent();
                if (!isallcity) {
                    PreferenceUtils.setObject(activity, city);
                }
                intent.putExtra("city", city);
                activity.setResult(Activity.RESULT_OK, intent);
                finishFramager();
            } else {//跳转到二级列表

//                if (mCityNames != null && mCityNames.size() > 0 && pos != 0) {
//                    mCityNames.set(0, cityModel);
//                    adapter.notifyDataSetChanged();
//                    mCityLit.smoothScrollToPosition(0);
//                }

                CityTwoFragment cityTwo = new CityTwoFragment();
                cityTwo.areainfo = cityModel;
                cityTwo.setListener(new ICustomDialogEventListener() {
                    @Override
                    public void customDialogEvent(CityInfo city) {
                        Intent intent = activity.getIntent();
                        intent.putExtra("city", city);
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }
                });
                replaceFragment(R.id.common_frame, cityTwo);
            }
        }

    }

    /**
     * 为ListView设置适配器
     *
     * @param list
     */
    private void setAdapter(List<AreaInfo> list) {
        if (list != null) {
            adapter = new ListAdapter(activity, list);
            mCityLit.setAdapter(adapter);
        }
    }

    /**
     * ListViewAdapter
     *
     * @author sy
     */
    private class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<AreaInfo> list;

        public ListAdapter(Context context, List<AreaInfo> list) {

            this.inflater = LayoutInflater.from(context);
            this.list = list;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                // getAlpha(list.get(i));
                String currentStr = list.get(i).nameSort;
                // 上一个汉语拼音首字母，如果不存在为“ ”
                String previewStr = (i - 1) >= 0 ? list.get(i - 1).nameSort : " ";
                if (previewStr != null) {
                    if (!previewStr.equals(currentStr)) {
                        String name = list.get(i).nameSort;
                        alphaIndexer.put(name, i);
                        sections[i] = name;
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.noScrollGridView = (NoScrollGridView) convertView.findViewById(R.id.names);
                holder.arrow = (TextView) convertView.findViewById(R.id.right_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(list.get(position).name);
            String currentStr = list.get(position).nameSort;
            String previewStr = (position - 1) >= 0 ? list.get(position - 1).nameSort : " ";

            //无论第一个alpha是什么（都显示"当前位置"）
            if (position == 0) {
                holder.alpha.setText("当前位置");
                holder.arrow.setVisibility(View.GONE);//第一个显示选择区县
            } else {
                holder.alpha.setText(currentStr);
                holder.arrow.setVisibility(View.GONE);
            }

            if (previewStr != null) {
                if (!previewStr.equals(currentStr)) {
                    holder.alpha.setVisibility(View.VISIBLE);
                } else {
                    holder.alpha.setVisibility(View.GONE);
                }
                /****/
                if ("热门城市".equals(currentStr)) {
                    holder.noScrollGridView.setVisibility(View.VISIBLE);
                    holder.name.setVisibility(View.GONE);
                    holder.noScrollGridView.setAdapter(new CommonAdapter<AreaInfo>(activity, holdName, R.layout.city_text_view) {
                        @Override
                        public void convert(org.wcy.android.utils.adapter.ViewHolder helper, final AreaInfo item) {
                            helper.setText(R.id.text_view, item.name);
                            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mCityNames != null && mCityNames.size() > 0) {
//                                        mCityNames.set(0, item);
//                                        adapter.notifyDataSetChanged();
                                        CityTwoFragment cityTwo = new CityTwoFragment();
                                        cityTwo.areainfo = item;
                                        cityTwo.setListener(new ICustomDialogEventListener() {
                                            @Override
                                            public void customDialogEvent(CityInfo city) {
                                                Intent intent = activity.getIntent();
                                                intent.putExtra("city", city);
                                                activity.setResult(Activity.RESULT_OK, intent);
                                                activity.finish();
                                            }
                                        });
                                        replaceFragment(R.id.common_frame, cityTwo);
                                    }

                                }
                            });
                        }
                    });
                } else {
                    holder.noScrollGridView.setVisibility(View.GONE);
                    holder.name.setVisibility(View.VISIBLE);
                }
            } else {
                holder.alpha.setVisibility(View.GONE);
            }
            /**选择地区的区县**/
            holder.arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CityTwoFragment cityTwo = new CityTwoFragment();
                    cityTwo.areainfo = mCityNames.get(0);
                    cityTwo.setListener(new ICustomDialogEventListener() {
                        @Override
                        public void customDialogEvent(CityInfo city) {
                            Intent intent = activity.getIntent();
                            intent.putExtra("city", city);
                            activity.setResult(Activity.RESULT_OK, intent);
                            activity.finish();
                        }
                    });
                    replaceFragment(R.id.common_frame, cityTwo);
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView alpha;
            TextView name;
            NoScrollGridView noScrollGridView;
            TextView arrow;
        }

    }

    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.getBackground().setAlpha(80);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                mCityLit.setSelection(position);
                overlay.setText(sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 500);
            }
        }

    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {

        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }

    //增加一个回调函数,用以从外部接收返回值
    public interface ICustomDialogEventListener {
        public void customDialogEvent(CityInfo city);
    }

    @Override
    public void onDestroy() {
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(overlay);
        super.onDestroy();
    }


    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }
}