package com.cadyd.app.ui.view.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cadyd.app.R;
import com.cadyd.app.model.AreaInfo;
import com.cadyd.app.model.CityInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.cadyd.app.model.ProvinceInfo;
import com.cadyd.app.ui.fragment.user.EditAddressFragment;
import com.cadyd.app.utils.CacheManager;

/**
 * 城市Picker
 *
 * @author zihao
 */
public class CityPicker extends LinearLayout {
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker counyPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempCounyIndex = -1;
    private List<ProvinceInfo> province_list = new ArrayList<ProvinceInfo>();
    private HashMap<String, List<AreaInfo>> city_map = new HashMap<String, List<AreaInfo>>();
    private HashMap<String, List<CityInfo>> couny_map = new HashMap<String, List<CityInfo>>();

    private CitycodeUtil citycodeUtil;
    private String city_code_string;
    private String city_string;
    private String provinceid_code;
    private String area_code;

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        getaddressinfo();
        // TODO Auto-generated constructor stub
    }

    public CityPicker(Context context) {
        super(context);
        getaddressinfo();
    }

    String provinceid = EditAddressFragment.provinceid, cityid = EditAddressFragment.cityid, counyid = EditAddressFragment.counyid;

    // 获取城市信息
    private void getaddressinfo() {
        // TODO Auto-generated method stub
        CityList list = CacheManager.getCacheManager().loadCitylist();
        // 读取城市信息string
        province_list = list.province_list;
        city_map = list.city_map;
        couny_map = list.couny_map;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
        citycodeUtil = CitycodeUtil.getSingleton();
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
        provincePicker.setData(citycodeUtil.getProvince(province_list, provinceid));
        provincePicker.setDefault(citycodeUtil.provinceIndex);
        cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(citycodeUtil.provinceIndex),
                cityid));
        cityPicker.setDefault(citycodeUtil.cityIndex);
        counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(citycodeUtil.cityIndex),
                counyid));
        counyPicker.setDefault(citycodeUtil.counyIndex);
        provinceid_code = citycodeUtil.getProvince_list_code().get(0);
        area_code = citycodeUtil.getCity_list_code().get(0);
        city_code_string = citycodeUtil.getCouny_list_code().get(0);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {
                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
                    cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(id), ""));
                    cityPicker.setDefault(citycodeUtil.cityIndex);
                    counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(0), ""));
                    counyPicker.setDefault(0);
                    // 城市数组
                    provinceid_code = citycodeUtil.getProvince_list_code().get(id);
                    area_code = citycodeUtil.getCity_list_code().get(0);
                    city_code_string = citycodeUtil.getCouny_list_code().get(0);
                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;

                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(id), ""));
                    counyPicker.setDefault(0);
                    // 城市数组
                    city_code_string = citycodeUtil.getCouny_list_code().get(0);
                    area_code = citycodeUtil.getCity_list_code().get(id);
                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub

            }
        });
        counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (tempCounyIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = cityPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
                    city_code_string = citycodeUtil.getCouny_list_code().get(id);
                    int lastDay = Integer.valueOf(counyPicker.getListSize());
                    if (id > lastDay) {
                        counyPicker.setDefault(lastDay - 1);
                    }
                }
                tempCounyIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String[] getCity_code() {
        String[] str = new String[3];
        str[0] = provinceid_code;
        str[1] = area_code;
        str[2] = city_code_string;
        return str;
    }

    public void setProvincePicker(String provinceid, String cityid, String counyid) {
        this.provinceid = provinceid;
        this.cityid = cityid;
        this.counyid = counyid;
        invalidate();
    }

    public String getCity_string() {
        city_string = provincePicker.getSelectedText() + "-" + cityPicker.getSelectedText() + "-" + counyPicker.getSelectedText();
        return city_string;
    }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }
}
