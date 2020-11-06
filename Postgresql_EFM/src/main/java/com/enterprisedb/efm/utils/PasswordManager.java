package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.exceptions.PasswordDecryptException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Formatter;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class PasswordManager {
    public static final String AES = "AES";

    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    public static String encrypt(String password, String seed) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getRawKey(seed), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, key);
        return hexEncode(cipher.doFinal(password.getBytes("UTF-8")));
    }

    public static String decrypt(String gibberish, String seed) throws PasswordDecryptException {
        try {
            SecretKeySpec key = new SecretKeySpec(getRawKey(seed), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, key);
            return new String(cipher.doFinal(hexDecode(gibberish)), "UTF-8");
        } catch (IOException ioe) {
            throw new PasswordDecryptException(ioe);
        } catch (GeneralSecurityException gse) {
            throw new PasswordDecryptException(gse);
        }
    }

    private static byte[] getRawKey(String seed) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        int iterations = 0;
        while (iterations < 1000) {
            for (char c : seed.toCharArray())
                iterations += c;
        }
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(seed.getBytes("UTF-8"));
        byte[] raw = md.digest();
        String hash = hexEncode(raw);
        KeySpec spec = new PBEKeySpec(hash.toCharArray(), seed.getBytes("UTF-8"), iterations, 256);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return f.generateSecret(spec).getEncoded();
    }

    private static String hexEncode(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", new Object[] { Byte.valueOf(b) });
        }
        return sb.toString();
    }

    private static byte[] hexDecode(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }
}
