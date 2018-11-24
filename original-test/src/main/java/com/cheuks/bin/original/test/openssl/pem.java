package com.cheuks.bin.original.test.openssl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.DigestUtils;

import com.cheuks.bin.original.common.util.Encryption;

import sun.misc.BASE64Decoder;

public class pem {

	public void a() throws Exception {
		BASE64Decoder base64Decoder = new BASE64Decoder();

		//        FileInputStream in = new FileInputStream(new File("d:/ca.private.der"));
//		InputStream in = pem.class.getClassLoader().getResourceAsStream("ca.private.der");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		byte[] buff = new byte[512];
//		int len;
//		while (-1 != (len = in.read(buff))) {
//			out.write(buff, 0, len);
//		}
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
		
		String public_Key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDg1vn4jVKYO8WpTtiwhPaIQjmG" + 
				"PUX25QgJKnFYzNVxw0LwCtelCYAf8fFFsjjccf/nJqXSAKNydS4O0Dnit4MACJ14" + 
				"Ai8uqQDC5q6vSeVikU0xWAnEwiXVCLIi2aMu9yhSFG1uQruP49GW2NaY6NVQPCOB" + 
				"WKb1bpYgKfEw9Rq00wIDAQAB";
		byte[] buffer = base64Decoder.decodeBuffer(public_Key);
		
		
		KeySpec x509 = new X509EncodedKeySpec(buffer);
		PublicKey publicKey = keyFactory.generatePublic(x509);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] output = cipher.doFinal("appid=1543028514&appkey=1543028514&command=order.push&param={}&timestamp=1543028514&version=1.0".getBytes());
		System.err.println(new String(output));
		System.err.println(DigestUtils.md5DigestAsHex(output));
		System.err.println(DigestUtils.md5DigestAsHex("123".getBytes()));
		System.err.println(new String(output));
		
		
		String private_Key="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAODW+fiNUpg7xalO" + 
				"2LCE9ohCOYY9RfblCAkqcVjM1XHDQvAK16UJgB/x8UWyONxx/+cmpdIAo3J1Lg7Q" + 
				"OeK3gwAInXgCLy6pAMLmrq9J5WKRTTFYCcTCJdUIsiLZoy73KFIUbW5Cu4/j0ZbY" + 
				"1pjo1VA8I4FYpvVuliAp8TD1GrTTAgMBAAECgYEAkosrc9fQnI5gv4A3dZ3D/X2E" + 
				"dr0bC9etTCQQqnsfJz3INCIs+VFe4/NSeUW3D99xllw3Oc/WuDQt59PJ1r4bL8Ib" + 
				"51JUQ6vkWtdSEZQXFF0bWwYullKljs7KQ1DFXVEAsGS5HpM3byBWJMCgy3psgGI0" + 
				"O7xe8Ojrao4Xc10TyKkCQQD+8A/pkldSnneTSmBCcc4kF6FOD7CVrjC6bSMMvCNd" + 
				"B0nA96i3X8lbpGgc9lKrS5Q1jVDERL+FtY/g2ukwxxCVAkEA4cbPLAbzcdUsd/mV" + 
				"C/2KcNfBkdPKiKG0l9wX6rh9Ui++VNRUrBZdt9ImsT8wLx5Uz/qVGBkufzV9lWkZ" + 
				"COBNxwJBALQKP4KZmde8GABKF171VjqBAabKR8/9W2ZdKtKUj3jka76+aEVQfTie" + 
				"C/TgNJPKZVnIehCR6Jv6FCkdEHAlNV0CQAJpWTUjLd59ZGgzpj/miWADLfIczQEA" + 
				"mSk2FJtRNSV3lRTa37ym0IFyIS+reRxHuqguCQGk4zZAoALEiJvrqWkCQCO2QzUC" + 
				"rvQpb9LParaeIHIutnSZ1N9ViQ8VMOjgmXjz5fbDoaozgy/F0RGlDj5/rJwVAw7i" + 
				"V2Kjk0VJixiRQwE=";
		
		//解密
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64Decoder.decodeBuffer(private_Key));
        PrivateKey privateKey =keyFactory.generatePrivate(keySpec);
		cipher=Cipher.getInstance("RSA", new BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		System.out.println(new String(cipher.doFinal(output)));
		
		
		System.err.println(System.currentTimeMillis()/1000);
		
		
		//加密
//		KeySpec keySpec = new PKCS8EncodedKeySpec(out.toByteArray());
//		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//		PrivateKey key = keyFactory.generatePrivate(keySpec);
		//        PublicKey key1 = keyFactory.generatePublic(keySpec);

//		Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
//		cipher.init(Cipher.ENCRYPT_MODE, key);
//		byte[] output = cipher.doFinal("111111".getBytes());
//		System.out.println(Encryption.newInstance().MD5(output));
//		System.err.println(new String(output));
//		
//		
//		
//		
//
//		//        //解码
//
//		String a = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdzpR7Qhrq27fnlbNaZ5XN1ChD" + "pfb2hLTd8loRyrbij/imnOolvaHIb5dXAB6LcrUd4Vg2KbB5+PtJS1qA3TPphmLc" + "2I411o9xL5VuTzbzQATE8VQaiX91hsenysJWJb7/Xr2jf8vGWu1UiMzsz0QG347P" + "JxE78b+cSNCEu14C5QIDAQAB";
//		byte[] buffer = base64Decoder.decodeBuffer(a);

		//####
//		KeySpec keySpec1 = new X509EncodedKeySpec(buffer);
//		PublicKey publicKey = keyFactory.generatePublic(keySpec1);
//		cipher.init(Cipher.DECRYPT_MODE, publicKey);
//		output = cipher.doFinal(output);
//		System.err.println(new String(output));
	}

	public static void main(String[] args) throws Exception {
		new pem().a();
	}

}
