package com.wk.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//MD5加密工具类
public class MD5Util {
    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    public static String md5(String str){

        return encodeByMD5(str);
    }

    //对输入字符进行MD5加密
    private static String encodeByMD5(String str) {
        if(str != null){
            try {
                //创建具有指定算法的实例
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                //对指定的字符进行加密
                byte[] digest = md5.digest(str.getBytes());
                //将得到的字节数组变成字符串返回
                String result = byteArrayToHexString(digest);
                return result;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        }
        return null;
    }

    /**
     * 将给定的字节数组转换为字符串
     * @param digest
     * @return
     */
    public static String byteArrayToHexString(byte[] digest) {
            StringBuffer resultSb = new StringBuffer();
            for(int i=0;i<digest.length;i++){
                resultSb.append(byteToHexString(digest[i]));
            }
            return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if(n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1]+hexDigits[d2];
    }

    public static void main(String[] args) {
        byte b = 1;
        System.out.println(MD5Util.byteToHexString(b));
        String str = "213";
        System.out.println(MD5Util.encodeByMD5(str));
    }
}
