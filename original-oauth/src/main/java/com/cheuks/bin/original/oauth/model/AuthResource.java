package com.cheuks.bin.original.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
/***
 * *
 *
 * @Title: original-oauth
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2019年01月04日 下午12:18:04
 * @see  资料权限
 */
public class AuthResource implements Serializable {

    private static final long serialVersionUID = -5595446116772882061L;

    private String name;
    private String url;

    public AuthResource setName(String name) {
        this.name = name;
        return this;
    }

    public AuthResource setUrl(String url) {
        this.url = url;
        return this;
    }

}
