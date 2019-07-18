package com.wk.encryption.baseEncryption;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CoderTest {

    @Test
    public void testCoder() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String intputStr = "简单加密";
        byte[] intputStrBytes = intputStr.getBytes();
        // BASE64 加解密
        String base64 = Coder.encryptionBASE64(intputStrBytes);
        System.out.println("BASE64加密:"+base64);
        System.out.println("");
        byte[] deccryBASE64 = Coder.decryptBASE64(base64);
        System.out.println("BASE解密:"+new BigInteger(deccryBASE64));
        System.out.println("");
        byte[] encryptMD5 = Coder.encryptMD5(intputStrBytes);
        System.out.println("MD5加密:"+new BigInteger(encryptMD5));
        System.out.println("");

        byte[] encryptSHA = Coder.encryptSHA(intputStrBytes);
        System.out.println("SHA加密:"+new BigInteger(encryptSHA));
        System.out.println("");
        String macKey = Coder.initMacKey();
        byte[] encryptHMAC = Coder.encryptHMAC(intputStrBytes, macKey);
        System.out.println("HMAC加密:"+ new BigInteger(encryptHMAC));
        System.out.println("");
    }
}
