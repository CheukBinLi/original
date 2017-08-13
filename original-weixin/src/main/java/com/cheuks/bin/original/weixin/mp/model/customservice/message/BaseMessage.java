package com.cheuks.bin.original.weixin.mp.model.customservice.message;

import java.util.HashMap;
import java.util.Map;

import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseMessage extends MpBaseModel {

    private static final long serialVersionUID = -14526741556805998L;

    private String touser;//"OPENID",

    private String msgtype;//"text",

    private Map<String, Object> body = new HashMap<String, Object>();

    private Map<String, Object> customerservice = new HashMap<String, Object>();

    public String getTouser() {
        return touser;
    }

    public BaseMessage setTouser(String touser) {
        this.touser = touser;
        return this;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public BaseMessage setMsgtype(String msgtype) {
        this.msgtype = msgtype;
        return this;
    }

    public BaseMessage(String msgtype) {
        super();
        this.msgtype = msgtype;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public BaseMessage setBody(Map<String, Object> body) {
        this.body = body;
        return this;
    }

    public Map<String, Object> getCustomerservice() {
        return customerservice;
    }

    public BaseMessage setCustomerservice(Map<String, Object> customerservice) {
        this.customerservice = customerservice;
        return this;
    }

    public BaseMessage setkfAccount(String kfAccount) {
        getBody().put("kf_account", kfAccount);
        return this;
    }

    @JsonIgnore
    public String getkfAccount() {
        return (String) getBody().get("kf_account");
    }
}
