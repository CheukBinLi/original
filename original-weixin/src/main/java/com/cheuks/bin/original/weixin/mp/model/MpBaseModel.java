package com.cheuks.bin.original.weixin.mp.model;

import com.cheuks.bin.original.weixin.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MpBaseModel extends BaseModel {

    private static final long serialVersionUID = -3010110299114129850L;

    @JsonProperty("errcode")
    protected Integer errCode;

    @JsonProperty("errmsg")
    protected String errMsg;

    public Integer getErrCode() {
        return errCode;
    }

    public MpBaseModel setErrCode(int errCode) {
        this.errCode = errCode;
        return this;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public MpBaseModel setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

}
