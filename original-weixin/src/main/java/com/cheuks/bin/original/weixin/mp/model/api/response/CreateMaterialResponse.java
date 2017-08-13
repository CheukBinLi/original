package com.cheuks.bin.original.weixin.mp.model.api.response;

import com.cheuks.bin.original.common.annotation.reflect.Alias;
import com.cheuks.bin.original.weixin.mp.model.MpBaseModel;

public class CreateMaterialResponse extends MpBaseModel {

    private static final long serialVersionUID = 4769980764693935321L;

    @Alias("media_id")
    private String mediaId;//返回的即为新增的图文消息素材的media_id。

    public String getMediaId() {
        return mediaId;
    }

    public CreateMaterialResponse setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

}
