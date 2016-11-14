package com.cadyd.app.wxapi;

import com.cadyd.app.ui.base.BaseActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by admin on 2016/6/6.
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) {
                toastSuccess("支付成功");
            } else {
                toastError(baseResp.errStr);
            }
        }
    }
}
