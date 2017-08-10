package com.cheuks.bin.original.weixin.mp.model.response;

import java.util.List;

import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeiXinUserInfoResponse extends MpBaseModel {

    private static final long serialVersionUID = -5799575434367963063L;

    // subscribe 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    @JsonProperty("subscribe")
    private Integer subscribe;

    // openid 用户的标识，对当前公众号唯一
    @JsonProperty("openid")
    private String openId;

    // nickname 用户的昵称
    @JsonProperty("nickname")
    private String nickName;

    // sex 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    @JsonProperty("sex")
    private Integer sex;

    // city 用户所在城市
    @JsonProperty("city")
    private String city;

    // country 用户所在国家
    @JsonProperty("country")
    private String country;

    // province 用户所在省份
    @JsonProperty("province")
    private String province;

    // language 用户的语言，简体中文为zh_CN
    @JsonProperty("language")
    private String language;

    // headimgurl
    // 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
    @JsonProperty("headimgurl")
    private String headImgurl;

    // subscribe_time 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    @JsonProperty("subscribe_time")
    private Long subscribeTime;

    // unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
    @JsonProperty("unionid")
    private String unionId;

    // remark 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
    @JsonProperty("remark")
    private String remark;

    // groupid 用户所在的分组ID（兼容旧的用户分组接口）
    @JsonProperty("groupid")
    private String groupId;

    // tagid_list 用户被打上的标签ID列表
    @JsonProperty("tagid_list")
    private List<Long> tagidList;

    public Integer getSubscribe() {
        return subscribe;
    }

    public WeiXinUserInfoResponse setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public WeiXinUserInfoResponse setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public WeiXinUserInfoResponse setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Integer getSex() {
        return sex;
    }

    public WeiXinUserInfoResponse setSex(Integer sex) {
        this.sex = sex;
        return this;
    }

    public String getCity() {
        return city;
    }

    public WeiXinUserInfoResponse setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public WeiXinUserInfoResponse setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public WeiXinUserInfoResponse setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public WeiXinUserInfoResponse setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getHeadImgurl() {
        return headImgurl;
    }

    public WeiXinUserInfoResponse setHeadImgurl(String headImgurl) {
        this.headImgurl = headImgurl;
        return this;
    }

    public Long getSubscribeTime() {
        return subscribeTime;
    }

    public WeiXinUserInfoResponse setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
        return this;
    }

    public String getUnionId() {
        return unionId;
    }

    public WeiXinUserInfoResponse setUnionId(String unionId) {
        this.unionId = unionId;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public WeiXinUserInfoResponse setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public WeiXinUserInfoResponse setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public List<Long> getTagidList() {
        return tagidList;
    }

    public WeiXinUserInfoResponse setTagidList(List<Long> tagidList) {
        this.tagidList = tagidList;
        return this;
    }

}
