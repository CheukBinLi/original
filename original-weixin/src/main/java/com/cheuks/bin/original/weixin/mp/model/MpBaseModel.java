package com.cheuks.bin.original.weixin.mp.model;

import com.cheuks.bin.original.weixin.BaseModel;

public class MpBaseModel extends BaseModel {

    private static final long serialVersionUID = -3010110299114129850L;

    // errcode
    private String errCode;

    //
    private String errmsg;

    public String getErrCode() {
        return errCode;
    }

    public MpBaseModel setErrCode(String errCode) {
        this.errCode = errCode;
        return this;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public MpBaseModel setErrmsg(String errmsg) {
        this.errmsg = errmsg;
        return this;
    }

}
