package com.cheuks.bin.original.weixin.mp.model.customservice;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;

public class CustomServiceInfo extends MpBaseModel {

    private static final long serialVersionUID = -7286668716409384334L;

    @Alias("kf_account")
    private String kfAccount;// 完整客服帐号，格式为：帐号前缀@公众号微信号

    @Alias("kf_nick")
    private String kfNick;// 客服昵称

    @Alias("kf_id")
    private String kfId;// 客服编号

    @Alias("kf_headimgurl")
    private String kfHeadimgurl;// 客服头像

    @Alias("kf_wx")
    private String kfWx;// 如果客服帐号已绑定了客服人员微信号，则此处显示微信号

    @Alias("invite_wx")
    private String inviteWx;//      如果客服帐号尚未绑定微信号，但是已经发起了一个绑定邀请，则此处显示绑定邀请的微信号

    @Alias("inviteExpireTime")
    private String inviteExpireTime;//     如果客服帐号尚未绑定微信号，但是已经发起过一个绑定邀请，邀请的过期时间，为unix 时间戳

    @Alias("invite_status")
    private String inviteStatus;//     邀请的状态，有等待确认“waiting”，被拒绝“rejected”， 过期“expired”

    public String getKfAccount() {
        return kfAccount;
    }

    public CustomServiceInfo setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
        return this;
    }

    public String getKfNick() {
        return kfNick;
    }

    public CustomServiceInfo setKfNick(String kfNick) {
        this.kfNick = kfNick;
        return this;
    }

    public String getKfId() {
        return kfId;
    }

    public CustomServiceInfo setKfId(String kfId) {
        this.kfId = kfId;
        return this;
    }

    public String getKfHeadimgurl() {
        return kfHeadimgurl;
    }

    public CustomServiceInfo setKfHeadimgurl(String kfHeadimgurl) {
        this.kfHeadimgurl = kfHeadimgurl;
        return this;
    }

    public String getKfWx() {
        return kfWx;
    }

    public CustomServiceInfo setKfWx(String kfWx) {
        this.kfWx = kfWx;
        return this;
    }

    public String getInviteWx() {
        return inviteWx;
    }

    public CustomServiceInfo setInviteWx(String inviteWx) {
        this.inviteWx = inviteWx;
        return this;
    }

    public String getInviteExpireTime() {
        return inviteExpireTime;
    }

    public CustomServiceInfo setInviteExpireTime(String inviteExpireTime) {
        this.inviteExpireTime = inviteExpireTime;
        return this;
    }

    public String getInviteStatus() {
        return inviteStatus;
    }

    public CustomServiceInfo setInviteStatus(String inviteStatus) {
        this.inviteStatus = inviteStatus;
        return this;
    }

}
