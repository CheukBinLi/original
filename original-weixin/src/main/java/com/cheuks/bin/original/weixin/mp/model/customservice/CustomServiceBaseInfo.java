package com.cheuks.bin.original.weixin.mp.model.customservice;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;

public class CustomServiceBaseInfo extends MpBaseModel {

    private static final long serialVersionUID = -7286668716409384334L;

    @Alias("kf_account")
    private String kfAccount;// 完整客服帐号，格式为：帐号前缀@公众号微信号

    private String status;// 客服在线状态，目前为：1、web 在线

    @Alias("kf_id")
    private String kfId;// 客服编号

    @Alias("accepted_case")
    private String acceptedCase;// 客服当前正在接待的会话数      

    public String getKfAccount() {
        return kfAccount;
    }

    public CustomServiceBaseInfo setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public CustomServiceBaseInfo setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getKfId() {
        return kfId;
    }

    public CustomServiceBaseInfo setKfId(String kfId) {
        this.kfId = kfId;
        return this;
    }

    public String getAcceptedCase() {
        return acceptedCase;
    }

    public CustomServiceBaseInfo setAcceptedCase(String acceptedCase) {
        this.acceptedCase = acceptedCase;
        return this;
    }

}
