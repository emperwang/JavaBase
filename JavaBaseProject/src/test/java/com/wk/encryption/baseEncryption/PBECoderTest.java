package com.wk.encryption.baseEncryption;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PBECoderTest {
    @Test
    public void testPBE() throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        String intputStr = "ABC";
        byte[] intputStrBytes = intputStr.getBytes();
        String pwd = "efg";

        byte[] salt = PBECoder.initSalt();
        System.out.println("salt is "+PBECoder.encryptionBASE64(salt));
        byte[] encrypt = PBECoder.encrypt(intputStrBytes, pwd, salt);
        System.out.println("加密后:"+PBECoder.encryptionBASE64(encrypt));
        System.out.println("");
        byte[] decrypt = PBECoder.decrypt(encrypt, pwd, salt);
        System.out.println("解密后:"+new String(decrypt));
        System.out.println("");
    }
}
