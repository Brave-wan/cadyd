package com.cadyd.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.OneGoodsModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class PaymentAdapter extends BaseAdapter {

    private List<OneGoodsModel> models;
    private Context context;
    private LayoutInflater inflater;

    private TowObjectParameterInterface towObjectParameterInterface;

    public PaymentAdapter(Context context, List<OneGoodsModel> models) {
        this.context = context;
        this.models = models;
        inflater = LayoutInflater.from(context);
    }

    public void setOnClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
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
            view = inflater.inflate(R.layout.payment_goods_item, null);
            holder.Title = (TextView) view.findViewById(R.id.math_tow_lift);
            holder.delete = (TextView) view.findViewById(R.id.math_tow_delete);
            holder.allNumber = (TextView) view.findViewById(R.id.math_tow_all_number);
            holder.surplusNumber = (TextView) view.findViewById(R.id.math_tow_surplus_number);
            holder.reducePeople = (TextView) view.findViewById(R.id.math_tow_reduce_people);
            holder.buyNumberPeople = (EditText) view.findViewById(R.id.math_tow_number_people);
            holder.addPeople = (TextView) view.findViewById(R.id.math_tow_add_people);
            holder.otherPeople = (Button) view.findViewById(R.id.math_tow_other_people);
            holder.numberPeriod = (TextView) view.findViewById(R.id.math_tow_number_period);
            holder.image = (ImageView) view.findViewById(R.id.math_tow_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final OneGoodsModel model = models.get(position);

        ApiClient.displayImage(model.mainImg, holder.image);

        holder.allNumber.setText("总需" + model.participatetimes + "人次");
        holder.surplusNumber.setText(MyChange.ChangeTextColor("剩余" + (Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes)) + "人次", 2, 2 + String.valueOf(Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes)).length(), Color.RED));

        holder.buyNumberPeople.setText(String.valueOf(model.number));//乐购人次

        holder.numberPeriod.setText(model.hastimes);//乐购期数

        //包尾
        holder.otherPeople.setOnClickListener(new View.OnClickListener() {//包尾
            @Override
            public void onClick(View v) {
                holder.buyNumberPeople.setText(String.valueOf(Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes)));
                towObjectParameterInterface.Onchange(0, position, String.valueOf(Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes)));
            }
        });

        //减
        holder.reducePeople.setOnClickListener(new View.OnClickListener() {//减一个人次
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(holder.buyNumberPeople.getText().toString());
                if (num > 1) {
                    num -= 1;
                    holder.buyNumberPeople.setText(String.valueOf(num));
                    towObjectParameterInterface.Onchange(1, position, String.valueOf(num));
                }
            }
        });

        //加
        holder.addPeople.setOnClickListener(new View.OnClickListener() {//加一个人次
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(holder.buyNumberPeople.getText().toString());
                if (num < (Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes))) {
                    num += 1;
                    holder.buyNumberPeople.setText(String.valueOf(num));
                    towObjectParameterInterface.Onchange(1, position, String.valueOf(num));
                }
            }
        });

        // holder.buyNumberPeople.setEnabled(false);

        //文本框变化监听
//        holder.buyNumberPeople.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        //输入框失去焦点时重新计算
        holder.buyNumberPeople.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!"".equals(holder.buyNumberPeople.getText().toString())) {
                        if (Integer.valueOf(holder.buyNumberPeople.getText().toString()) == 0) {
                            holder.buyNumberPeople.setText("1");
                        } else if (Integer.valueOf(holder.buyNumberPeople.getText().toString()) > (Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes))) {
                            holder.buyNumberPeople.setText((Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes)) + "");
                        }
                        towObjectParameterInterface.Onchange(2, position, holder.buyNumberPeople.getText().toString());
                    }
                }
            }
        });

        if ("10".equals(model.average)) {
            holder.Title.setText(MyChange.ChangeTextColor("[十元专区]" + model.title, 0, "[十元专区]".length(), Color.RED));
        } else {
            holder.Title.setText(model.title);
        }


        view.setBackgroundResource(R.color.white);
        return view;
    }

    private class ViewHolder {
        private ImageView image;
        private TextView Title;
        private TextView delete;//删除

        private TextView allNumber;//总人次
        private TextView surplusNumber;//剩余人次


        private TextView reducePeople;//乐购人次减
        private EditText buyNumberPeople;//乐购人次
        private TextView addPeople;//乐购人次加

        private Button otherPeople;//包尾

        private TextView numberPeriod;//参与期数

    }
}
