package com.wk.encryption.baseEncryption;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DESCoderTest {

    @Test
    public void testDESCoder() throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String inputStr = "DES";
        byte[] inputStrBytes = inputStr.getBytes();
        String key = DESCoder.initKey();
        System.out.println("init key is "+key);
        byte[] encrypt = DESCoder.encrypt(inputStrBytes, key);
        System.out.println("encrypt is "+DESCoder.encryptionBASE64(encrypt));
        System.out.println();
        byte[] decript = DESCoder.decript(encrypt, key);
        System.out.println("descipt is "+ new String(decript));
        System.out.println();

    }
}
