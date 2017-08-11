package com.cheuks.bin.original.weixin.mp.model.message;

import com.cheuks.bin.original.common.annotation.reflect.Alias;

/***
 * 
 * @Title: original-weixin
 * @Description: 回复文本消息
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年8月10日 下午2:12:54
 *
 */
public class TextMessageResponse extends BaseMessageResponse {

    private static final long serialVersionUID = 1277430317241352680L;

    public TextMessageResponse(String msgType) {
        super("text");
    }

    @Alias("Content")
    private String content;// 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）

    public String getContent() {
        return content;
    }

    public TextMessageResponse setContent(String content) {
        this.content = content;
        return this;
    }

}
