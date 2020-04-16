package com.cheuks.bin.original.oauth.model.encryption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/***
 * * *
 *
 * @CREATE ON 2018年10月31日 下午5:49:13
 * @EMAIL:20796698@QQ.COM
 *
 * @author CHEUK.BIN.LI
 * @see 证书管理
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeyModel {

    public static enum EncryptionType {
        HMAC256,
        HMAC384,
        HMAC512,
        ECDSA256,
        ECDSA384,
        ECDSA512RSA256,
        RSA384,
        RSA512
    }

    private String issuer;//发行方
    private String secret;//密匙
    private EncryptionType encryptionType = EncryptionType.HMAC256;//加密类型

    public KeyModel setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public KeyModel setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public KeyModel setEncryptionType(EncryptionType encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

}
