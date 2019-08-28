package com.wk.PrintUtil;

public class PrintUtil {
    /**
     *  直接打印二进制  八进制  十六进制
     */
    public static void binaryPrint(){
        System.out.println(0b001);
        System.out.println(011);
        System.out.println(0x11c);
    }

    /**
     *  字符串转二进制
     *  转换为字符对应的二进制  ASCALL 码表对应
     *  unicode 编码 兼容 ASCALL
     * @return
     */
    public static String toBinary(String str){
        char[] chars = str.toCharArray();
        String binaryResult = "";
        for (int i = 0; i < chars.length; i++) {
            binaryResult += Integer.toBinaryString(chars[i]);
        }
        return binaryResult;
    }

    /**
     *  字符串转十六进制
     *  转换为 ASCALL 字符对应的十六进制
     *  unicode 兼容ascall 码表
     * @return
     */
    public static String toHex(String str){
        char[] chars = str.toCharArray();
        String hexResult = "";
        for (int i = 0; i < chars.length; i++) {
            hexResult += Integer.toHexString(chars[i]);
        }
        return hexResult;
    }

    /**
     *  字符串转 十进制
     *  把字符转换为 ascall表的十进制表示
     *  unicode 兼容 ascall
     * @return
     */
    public static String toOct(String str){
        char[] chars = str.toCharArray();
        String octResult = "";
        for (int i = 0; i < chars.length; i++) {
            octResult += Integer.toOctalString(chars[i]);
        }
        return octResult;
    }

    /**
     *  二进制转 int
     * @return
     */
    public static int[] binaryToIntArray(String binStr){
        char[] chars = binStr.toCharArray();
        int[] result = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            // 字符的 1 和十进制的 1 ,在ascall表中相差 48,减去48就可以把字符的1转换为数字的1
            result[i] = chars[i] - 48;
        }
        return result;
    }

    /**
     * 二进制转换为字符
     * @param binStr
     * @return
     */
    public static char binaryToChar(String binStr){
        int[] ints = binaryToIntArray(binStr);
        int sum = 0;
        for (int i = 0;i< ints.length;i++){
            sum += ints[ints.length-1-i] << i;
        }
        System.out.println(sum);
        return (char)sum;
    }

    public static void prinfDemo(){
        // 定一些变量，测试格式化输出
        double d = 345.678;
        String s = "hello";
        int i = 1234;

        // % 表示进行格式化输出，% z之后的内容为格式的定义
        myPrintf("%f",d);   // f 表示浮点数
        myPrintf("%6.3f",d); // 6.3f 中 6表示总长度,3表示小数点长度
        myPrintf("%+6.2f",d);  // + 表示输出的数字表示带正负号
        myPrintf("%+6.3f",-d); // 负数

        myPrintf("%-6.3f",d);  // - 表示输出的数左对齐

        myPrintf("%+-6.3f",d); // +- 表示输出带正负号且左对齐

        myPrintf("%d",i);   // d 表示输出八进制整数

        myPrintf("%x",i);   // x 表示输出十六进制数

        myPrintf("%#x",i);  // #x 输出带十六进制标识的数字

        myPrintf("%s",s);   // 输出字符串

        // 输出多个变量，注意顺序
        System.out.printf("输出一个浮点数:%f,一个整数:%d,一个字符串:%s",d,i,s);

        // 指定变量位置
        // 2$ 表示第2个变量
        // 1$ #x 表示第一个变量   十六进制
        System.out.printf("字符串:%2$s,带符号十六进制:%1$#x",i,s);

    }

    public static void myPrintf(String format,Object obj){
        System.out.printf(format,obj);
        System.out.println();
    }

    public static void main(String[] args) {
        // binaryPrint();
        String hex = toHex("h");
        System.out.println("0x"+hex);
        String oct = toOct("h");
        System.out.println(oct);
        String hello = toBinary("h");
        System.out.println(hello);
        char c = binaryToChar(hello);
        System.out.println(c);
        //prinfDemo();
    }
}
