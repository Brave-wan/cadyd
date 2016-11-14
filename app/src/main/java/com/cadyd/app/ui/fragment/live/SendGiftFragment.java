package com.cadyd.app.ui.fragment.live;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.GiftModel;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.ArrayList;

/**
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class SendGiftFragment extends Fragment {
    private GridView mGridView;
    private CommonAdapter<GiftModel> mAdapter;
    private ArrayList<GiftModel> list = new ArrayList<>();

    private ArrayList<GiftModel> sourceList = new ArrayList<>();//数据源礼物列表

    private TowObjectParameterInterface towObjectParameterInterface;

    public void setOnClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            list = getArguments().getParcelableArrayList("list");
            sourceList = getArguments().getParcelableArrayList("sourceList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sendgift, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mGridView = (GridView) view.findViewById(R.id.fragment_gitList);
        mAdapter = new CommonAdapter<GiftModel>(getActivity(), list, R.layout.item_sendgiftgridview) {
            @Override
            public void convert(ViewHolder helper, final GiftModel item) {
                helper.setText(R.id.item_sendgift_money, item.getGiftPrice() + "花币");
                ImageView imageView = helper.getView(R.id.item_sendgift_image);
                ApiClient.displayImage(item.getGiftPic(), imageView, R.mipmap.live_sendgift);//设置礼物的图片
                final ImageView checkImage = helper.getView(R.id.item_sendgift_checked);//显示选中的view
                helper.setText(R.id.item_sendgift_name, item.getGiftName());//设置礼物名字
                if (item.isCheck) {
                    checkImage.setVisibility(View.VISIBLE);
                } else {
                    checkImage.setVisibility(View.GONE);
                }
                /**
                 * 选择礼物的监听
                 */
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < sourceList.size(); i++) {
                            sourceList.get(i).isCheck = false;
                        }
                        if (towObjectParameterInterface != null) {
                            towObjectParameterInterface.Onchange(0, 0, checkImage);
                        }
                        item.isCheck = !item.isCheck;
                    }
                });
            }
        };
        mGridView.setAdapter(mAdapter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
