package com.wk.javaCoreEncoding;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class JavaEncode1 {
    public static void main(String[] args) {
        //encode();
        encode2();
    }

    public static void encode2(){
        // "你" 的gb码是: 0xC4E3, unicode是0x4F60
        String a = "你";
        String encode1 = "gb2312";
        String encode2 = "unicode";
        String encode3 = "UTF-8";

        Charset gb23 = Charset.forName(encode1);
        Charset unicode = Charset.forName(encode2);
        Charset utf8 = Charset.forName(encode3);
        System.out.println("----------------------------------------------------");
        ByteBuffer gb = gb23.encode(a);
        for (int i =0; gb.hasRemaining(); i++){
            int anInt = gb.get();
            int val = anInt & 0xff;
            char cval = (char)val;
            System.out.println(""+i+"  "+val+" ; hex 0x"+ Integer.toHexString(val) + "  ("+cval+")");
        }

        System.out.println("----------------------------------------------------");
        ByteBuffer uc = unicode.encode(a);
        for (int i =0; uc.hasRemaining(); i++){
            int anInt = uc.get();
            int val = anInt & 0xff;
            char cval = (char)val;
            System.out.println(""+i+"  "+val+" ; hex 0x"+ Integer.toHexString(val) + "  ("+cval+")");
        }
        System.out.println("----------------------------------------------------");
        ByteBuffer f8 = utf8.encode(a);
        for (int i =0; f8.hasRemaining(); i++){
            int anInt = f8.get();
            int val = anInt & 0xff;
            char cval = (char)val;
            System.out.println(""+i+"  "+val+" ; hex 0x"+ Integer.toHexString(val) + "  ("+cval+")");
        }
        System.out.println("----------------------------------------------------");
        System.out.println(Integer.toHexString(-1));
        System.out.println(Integer.toBinaryString(-1));

    }


    // 分别使用 gb2312  unicode 编码
    // 并打印byte
    public static void encode(){
        // "你" 的gb码是: 0xC4E3, unicode是0x4F60
        char a = '你';
        String encode1 = "gb2312";
        String encode2 = "unicode";
        String encode3 = "UTF-8";

        Charset gb23 = Charset.forName(encode1);
        Charset unicode = Charset.forName(encode2);
        Charset utf8 = Charset.forName(encode3);

        byte[] array = gb23.encode("你").array();
        for (int i = 0; i < array.length; i++){
            System.out.println(Integer.toHexString(array[i]));
        }
        System.out.println("----------------------------------------------------");
        byte[] array1 = unicode.encode("你").array();
        for (int i = 0; i < array1.length; i++) {
            System.out.println(Integer.toHexString(array1[i]));
        }
        System.out.println("----------------------------------------------------");
        byte[] array2 = utf8.encode("你").array();
        for (int i = 0; i < array2.length; i++) {
            System.out.println(Integer.toHexString(array2[i]));
        }
    }


    // 使用 gb2312 解码
    public static void decode1(){
        // "你" 的gb码是: 0xC4E3, unicode是0x4F60
        String encoding = "gb2312";
        byte b[] = {(byte)'\u00c4', (byte)'\u00e3'};
        Charset charset = Charset.forName(encoding);
        ByteBuffer wrap = ByteBuffer.wrap(b);
        CharBuffer decode = charset.decode(wrap);
        int length = decode.length();
        for (int i=0; i< length; i++){
            System.out.println(decode.get(i));
        }
    }
}
