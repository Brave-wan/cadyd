package com.cadyd.app.ui.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.android.volley.toolbox.StringRequest;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.interfaces.OneParameterInterface;
import com.cadyd.app.model.AddressModel;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.NiftyDialogBuilder;
import com.cadyd.app.ui.view.ToastView;
import com.cadyd.app.ui.view.city.CityPicker;
import com.cadyd.app.ui.view.dialog.effects.Effectstype;
import com.ta.utdid2.android.utils.PhoneInfoUtils;
import com.ta.utdid2.android.utils.StringUtils;

import org.wcy.android.utils.ActivityUtil;
import org.wcy.common.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加修改收货地址
 */
public class EditAddressFragment extends BaseFragement {
    private OneParameterInterface anInterface;
    public static String provinceid = "", cityid = "", counyid = "";

    //如果为空则是添加，不为空则是修改
    public AddressModel addressModel;

    public static EditAddressFragment newInstance(AddressModel addressModel) {
        EditAddressFragment newFragment = new EditAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", addressModel);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.addressModel = (AddressModel) args.getSerializable("model");
        }
    }

    public void setAnInterface(OneParameterInterface anInterface) {
        this.anInterface = anInterface;
    }

    @Bind(R.id.add_address_name)
    EditText name;

    @Bind(R.id.add_address_phone)
    EditText phone;

    @Bind(R.id.add_address_ssq)
    TextView ssq;//省市区

    @Bind(R.id.add_address_address)
    TextView address;

    @Bind(R.id.add_address_code)
    EditText code;


    @Bind(R.id.save)
    Button save;
    NiftyDialogBuilder addrDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = "";
        if (addressModel == null) {
            title = "新增收货地址";
        } else {
            title = "修改收货地址";
        }
        return setView(R.layout.fragment_add_address, title, true);
    }

    @Override
    protected void initView() {
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(name, activity);
                KeyBoardUtils.closeKeybord(phone, activity);
                KeyBoardUtils.closeKeybord(code, activity);//关闭软键盘
                finishFramager();
            }
        });
        if (addressModel != null) {
            name.setText(addressModel.consignee);
            phone.setText(addressModel.telphone);
            ssq.setText(addressModel.provincename + "-" + addressModel.cityname + "-" + addressModel.countyname);
            address.setText(addressModel.address);
            code.setText(addressModel.zipcode);
            provinceid = addressModel.province;
            cityid = addressModel.city;
            counyid = addressModel.county;
        }
        initAddr();
    }

    @OnClick({R.id.save, R.id.add_address_ssq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                initSave();
                break;
            case R.id.add_address_ssq:
                if (addrDialog == null) {
                    initAddr();
                    addrDialog.show();
                } else {
                    addrDialog.show();
                }
                break;
        }

    }


    private void initSave() {
        int url;
        if (addressModel != null) {
            url = JConstant.EDITADDRESS_;
        } else {
            url = JConstant.INSERTADDRESS_;
        }

        String consignee = name.getText().toString();
        String telphone = phone.getText().toString();
        String county = ssq.getText().toString();
        String addres = address.getText().toString();
        String zipcode = code.getText().toString();

        if (!ActivityUtil.isEditTextNull(name)) {
            name.requestFocus();
            toast("请先完善信息");
            return;
        } else if (!ActivityUtil.isEditTextNull(phone)) {
            phone.requestFocus();
            toast("请先完善信息");
            return;
        } else if (!StringUtil.hasText(addres)) {
            toast("请先完善信息");
            return;
        } else if (!ActivityUtil.isEditTextNull(code)) {
            code.requestFocus();
            toast("请先完善信息");
            return;
        } else if (telphone.length() != 11) {
            toast("请输入正确的联系方式");
            return;
        } else if (!"0".equals(telphone.subSequence(0, 1)) && !"1".equals(telphone.subSequence(0, 1))) {
            toast("请输入正确的联系方式");
            return;
        } else if (addres.length() > 50 || addres.length() < 5) {
            toast("请输5-50位的详细地址");
            return;
        } else if (code.length() != 6) {
            toast("请输正确的邮编");
            return;
        } else if (name.length() > 8) {
            toast("用户名不得大于8位");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        if (addressModel != null) {
            map.put("id", addressModel.id);
        }
        map.put("consignee", consignee);
        map.put("telphone", telphone);
        map.put("province", provinceid);
        map.put("city", cityid);
        map.put("county", counyid);
        map.put("address", addres);
        map.put("zipcode", zipcode);
        ApiClient.send(activity, url, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                anInterface.Onchange(0);
                finishFramager();
            }

            @Override
            public void error(String message) {

            }
        }, url);

    }

    private void initAddr() {
//        provinceid = "";
//        cityid = "";
//        counyid = "";
        addrDialog = NiftyDialogBuilder.getInstance(activity).withEffect(Effectstype.RotateBottom);
        View window = View.inflate(activity, R.layout.alert_city, null);
        final CityPicker cp = (CityPicker) window.findViewById(R.id.citypicker);
        Button confirm = (Button) window.findViewById(R.id.confirm);
        Button cancel = (Button) window.findViewById(R.id.cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssq.setText(cp.getCity_string());
                String codes[] = cp.getCity_code();
                provinceid = codes[0];
                cityid = codes[1];
                counyid = codes[2];
                addrDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrDialog.dismiss();
            }
        });
        addrDialog.setView(window);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.EDITADDRESS_);
        ApiClient.cancelRequest(JConstant.INSERTADDRESS_);
    }
}
