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
		
		String public_Key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8V96cZKNyfROPOsC1V732VtoH\r\n" + 
				"B15NGFTOIf+Y0DRf8PrELctfPcdjrEZ1lPsjJVj6cjJdP0k65yV9zoBhw0yw6sYQ\r\n" + 
				"XpA5Eir0wN6VcuqXdJgeY2fgT7VtIDMgjN3qtrUfdHxxT2DLqjArCWeYbgYjrZHi\r\n" + 
				"5EjjcTU7glVQ629kcQIDAQAB";
		byte[] buffer = base64Decoder.decodeBuffer(public_Key);
		
		
		KeySpec x509 = new X509EncodedKeySpec(buffer);
		PublicKey publicKey = keyFactory.generatePublic(x509);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//		byte[] output = cipher.doFinal("appid=1543028514&appkey=1543028514&command=order.push&param={}&timestamp=1543028514&version=1.0".getBytes());
		byte[] output = cipher.doFinal("123456".getBytes());
		System.err.println(new String(output));
		System.err.println(DigestUtils.md5DigestAsHex(output));
		System.err.println(DigestUtils.md5DigestAsHex("123".getBytes()));
		System.err.println(new String(output));
		
		
		String private_Key="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALxX3pxko3J9E486\r\n" + 
				"wLVXvfZW2gcHXk0YVM4h/5jQNF/w+sQty189x2OsRnWU+yMlWPpyMl0/STrnJX3O\r\n" + 
				"gGHDTLDqxhBekDkSKvTA3pVy6pd0mB5jZ+BPtW0gMyCM3eq2tR90fHFPYMuqMCsJ\r\n" + 
				"Z5huBiOtkeLkSONxNTuCVVDrb2RxAgMBAAECgYBbD4jbyHSV+A8q4tclVkTMA3Xq\r\n" + 
				"jA916tulxh5T/eY0tMgpbZK0e6QZdsOiOeOqPHZmsCvVlbGMPxGLX/oGSmybEGuh\r\n" + 
				"p97CtnNEUItUckXUs55q7yFgO94YfG78gKSARSQGPmkweDLR/Mthz4l+oyYGlR+n\r\n" + 
				"7iIIOgpHf3gUPuCwoQJBAOchYeqKzDe4TawhoH4sVOGUffaasD2XVVtvhbTdhCmK\r\n" + 
				"e8mc0tyPRT9G2WHz+awWVGo/kqEcLgB7NEKJKGFfYY0CQQDQm+P9k1t0+Ohtf/uN\r\n" + 
				"SzxPJ8TVeWDkUlB/PPKzaRWh3QCfxvQw1SjEB+sav6Dq6NTP7m3Rb1PbtViX6dwu\r\n" + 
				"sct1AkEAkblSavYn29mv3x80eViqC6/720ecZrjLcGLYAjjH4wkVEwB1Uepi1ops\r\n" + 
				"9H1GpWFOx85dPIbv2g3T3T9s3jes4QJAIExysVk2aNb9Da8qIIdMkCjlJfREFzXT\r\n" + 
				"ds/V+AdGLSiNpy9jsYffZvKe6SZQYO6pvIP7BtlIz5S1Ydf4mlY23QJBAM2J3U3y\r\n" + 
				"3JO34gUMwkT1Sb63kDmO9tYh2ALxnP/NdVftMCG0dUV//m5OfGiAfZgDuE/cQmHN\r\n" + 
				"Sz2rh4owUkP2ZgI=";
		
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
