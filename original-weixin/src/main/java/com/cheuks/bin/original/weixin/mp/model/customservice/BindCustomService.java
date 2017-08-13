package com.cheuks.bin.original.weixin.mp.model.customservice;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;

public class BindCustomService extends MpBaseModel {

    private static final long serialVersionUID = -7286668716409384334L;

    @Alias("kf_account")
    private String kfAccount;//完整客服帐号，格式为：帐号前缀@公众号微信号

    @Alias("invite_wx")
    private String inviteWx;//   接收绑定邀请的客服微信号

    public String getKfAccount() {
        return kfAccount;
    }

    public BindCustomService setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
        return this;
    }

    public String getInviteWx() {
        return inviteWx;
    }

    public BindCustomService setInviteWx(String inviteWx) {
        this.inviteWx = inviteWx;
        return this;
    }

}
