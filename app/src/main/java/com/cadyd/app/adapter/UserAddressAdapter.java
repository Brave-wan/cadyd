package com.cadyd.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.AddressModel;
import com.cadyd.app.ui.fragment.user.UserAddressFragment;
import com.cadyd.app.ui.view.AlertConfirmation;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class UserAddressAdapter extends BaseAdapter {

    private List<AddressModel> list;
    private Context context;
    private LayoutInflater inflater;
    private TowObjectParameterInterface towObjectParameterInterface;
    AlertConfirmation alert;

    public UserAddressAdapter(Context context, List<AddressModel> list) {
        this.context = context;
        this.list = list;
        alert = new AlertConfirmation(context, "删除地址", "是否删除该地址？");
        inflater = LayoutInflater.from(context);
    }

    public void setOnclick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.address_item, null);
            holder.name = (TextView) view.findViewById(R.id.address_user_name);
            holder.phone = (TextView) view.findViewById(R.id.address_phone);
            holder.address = (TextView) view.findViewById(R.id.address_address);
            holder.IsDefault = (CheckBox) view.findViewById(R.id.address_is_default);
            holder.setting = (TextView) view.findViewById(R.id.address_setting);
            holder.delete = (TextView) view.findViewById(R.id.address_delete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final AddressModel model = list.get(position);
        holder.name.setText(model.consignee);
        holder.phone.setText(model.telphone);
        holder.address.setText(model.provincename + model.cityname + model.countyname + model.address);
        if ("0".equals(model.defaultaddress)) {
            holder.IsDefault.setChecked(true);
        } else {
            holder.IsDefault.setChecked(false);
        }

        //设置默认地址
        holder.IsDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.IsDefault.isChecked() && !"0".equals(model.defaultaddress)) {
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(1, position, "");
                    }
                } else {
                    notifyDataSetChanged();
                }
            }
        });

        //修改地址
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (towObjectParameterInterface != null) {
                    towObjectParameterInterface.Onchange(2, position, "");
                }
            }
        });

        //删除地址
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show(new AlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener() {
                        if (towObjectParameterInterface != null) {
                            towObjectParameterInterface.Onchange(3, position, holder.IsDefault.isChecked());
                        }
                        alert.hide();
                    }
                    @Override
                    public void CancelOnClickListener() {
                        alert.hide();
                    }
                });
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickAddress != null) {
                    onClickAddress.onChange(model);
                }
            }
        });
        return view;
    }

    private UserAddressFragment.OnClickAddress onClickAddress;

    public void setOnClickAddress(UserAddressFragment.OnClickAddress onClickAddress) {
        this.onClickAddress = onClickAddress;
    }

    private class ViewHolder {
        private TextView name;
        private TextView phone;
        private TextView address;
        private CheckBox IsDefault;
        private TextView setting;
        private TextView delete;
    }
}
