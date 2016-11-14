package com.cadyd.app.ui.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.UserAddressAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.OneParameterInterface;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.AddressModel;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.user.EditAddressFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户地址管理
 */
public class UserAddressFragment extends BaseFragement {

    private List<AddressModel> list;
    private UserAddressAdapter adapter;
    public static final int ADDRESSRESULT = 0;

    @Bind(R.id.user_address_list)
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_user_address, "地址管理", true);
    }

    @Override
    protected void initView() {
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishFramager();
            }
        });
        activity.setResult(ADDRESSRESULT);
        getInit();
    }

    //新增地址
    @OnClick(R.id.user_address_add)
    public void onClick(View view) {
        EditAddressFragment add = EditAddressFragment.newInstance(null);
        add.setAnInterface(new OneParameterInterface() {
            @Override
            public void Onchange(int type) {
                //地址保存成功更新本界面数据
                getInit();
            }
        });
        replaceFragment(R.id.common_frame, add);
    }

    private OnClickAddress towParameterInterface;

    public void setOnclick(OnClickAddress towParameterInterface) {
        this.towParameterInterface = towParameterInterface;
    }

    public interface OnClickAddress {
        public void onChange(AddressModel model);
    }

    //获取地址信息
    private void getInit() {
        ApiClient.send(activity, JConstant.SELECTBYADDRESS_, null, AddressModel.getType(), new DataLoader<List<AddressModel>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<AddressModel> data) {
                if (adapter == null) {
                    list = data;
                    adapter = new UserAddressAdapter(activity, list);
                    adapter.setOnclick(new TowObjectParameterInterface() {
                        @Override
                        public void Onchange(int type, int pos, Object conten) {
                            switch (type) {
                                case 1://设置默认地址
                                    setInit(pos);
                                    break;
                                case 2://修改地址
                                    EditAddressFragment editAddressFragment = EditAddressFragment.newInstance(list.get(pos));
                                    editAddressFragment.setAnInterface(new OneParameterInterface() {
                                        @Override
                                        public void Onchange(int type) {
                                            //地址修改成功更新本界面数据
                                            getInit();
                                        }
                                    });
                                    replaceFragment(R.id.common_frame, editAddressFragment);
                                    break;
                                case 3://删除地址
                                    boolean isDefault = (boolean) conten;
                                    deleteInit(pos, isDefault);
                                    break;
                            }

                        }
                    });
                    adapter.setOnClickAddress(new OnClickAddress() {
                        @Override
                        public void onChange(AddressModel model) {
                            if (towParameterInterface != null) {
                                towParameterInterface.onChange(model);
                                finishFramager();
                            }
                        }
                    });
                    listView.setAdapter(adapter);
                } else {
                    list.clear();
                    list.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SELECTBYADDRESS_);
    }

    //设置默认地址
    private void setInit(final int pos) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", list.get(pos).id);
        ApiClient.send(activity, JConstant.DEFAULTTOADDRESS_, map, null, new DataLoader<String>() {

                    @Override
                    public void task(String data) {
                    }

                    @Override
                    public void succeed(String data) {
                        getInit();
                    }

                    @Override
                    public void error(String message) {
                        getInit();
                    }
                }

                ,JConstant.DEFAULTTOADDRESS_);
    }

    //删除地址
    private void deleteInit(final int pos, final boolean isDefault) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", list.get(pos).id);
        ApiClient.send(activity, JConstant.DELETEADDRESS_, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(String data) {
                if (isDefault) {
                    if (list.size() - 1 > 0) {

                        if (pos == 0) {
                            setInit(1);
                        } else {
                            setInit(0);
                        }

                    } else {
                        getInit();
                    }
                } else {
                    getInit();
                }
            }

            @Override
            public void error(String message) {
            }
        },JConstant.DELETEADDRESS_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SELECTBYADDRESS_);
        ApiClient.cancelRequest(JConstant.DEFAULTTOADDRESS_);
        ApiClient.cancelRequest(JConstant.DELETEADDRESS_);
    }
}
