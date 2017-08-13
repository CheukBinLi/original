package com.cheuks.bin.original.weixin.mp.model.customservice;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;

public class CustomService extends MpBaseModel {

    private static final long serialVersionUID = -7286668716409384334L;

    @Alias("kf_account")
    private String kfAccount;//完整客服帐号，格式为：帐号前缀@公众号微信号，帐号前缀最多10个字符，必须是英文、数字字符或者下划线，后缀为公众号微信号，长度不超过30个字符

    @Alias("nickname")
    private String nickName;//   客服昵称，最长16个字

    public String getKfAccount() {
        return kfAccount;
    }

    public CustomService setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public CustomService setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

}
