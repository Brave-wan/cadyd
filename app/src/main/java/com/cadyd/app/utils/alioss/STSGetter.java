package com.cadyd.app.utils.alioss;

import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.cadyd.app.model.OSSTokenInfo;

/**
 * Created by Administrator on 2015/12/9 0009.
 * 重载OSSFederationCredentialProvider生成自己的获取STS的功能
 */
public class STSGetter extends OSSFederationCredentialProvider {

    private OSSTokenInfo ossTokenInfo;

    public STSGetter(OSSTokenInfo ossTokenInfo) {
        this.ossTokenInfo = ossTokenInfo;
    }

    public OSSFederationToken getFederationToken() {
        return new OSSFederationToken(ossTokenInfo.getAccessKeyId(), ossTokenInfo.getAccessKeySecret(), ossTokenInfo.getSecurityToken(), ossTokenInfo.getExpiration());
    }

}
