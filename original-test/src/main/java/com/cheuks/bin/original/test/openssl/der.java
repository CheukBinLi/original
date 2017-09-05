package com.cheuks.bin.original.test.openssl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;

public class der {

    public void a() throws Exception {

        FileInputStream in = new FileInputStream(new File("d:/ca.private.der"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[512];
        int len;
        while (-1 != (len = in.read(buff))) {
            out.write(buff, 0, len);
        }
        //加密
        KeySpec keySpec = new PKCS8EncodedKeySpec(out.toByteArray());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        //        PublicKey key1 = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] output = cipher.doFinal("你好吗？".getBytes());
        System.err.println(new String(output));

        //解码
        BASE64Decoder base64Decoder = new BASE64Decoder();
        String a = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdzpR7Qhrq27fnlbNaZ5XN1ChD" + "pfb2hLTd8loRyrbij/imnOolvaHIb5dXAB6LcrUd4Vg2KbB5+PtJS1qA3TPphmLc" + "2I411o9xL5VuTzbzQATE8VQaiX91hsenysJWJb7/Xr2jf8vGWu1UiMzsz0QG347P" + "JxE78b+cSNCEu14C5QIDAQAB";
        byte[] buffer = base64Decoder.decodeBuffer(a);
        //####
        KeySpec keySpec1 = new X509EncodedKeySpec(buffer);
        PublicKey publicKey = keyFactory.generatePublic(keySpec1);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        output = cipher.doFinal(output);
        System.err.println(new String(output));
    }

    public static void main(String[] args) throws Exception {
        new der().a();
    }

}
