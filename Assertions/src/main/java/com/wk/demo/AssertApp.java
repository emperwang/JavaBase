package com.wk.demo;


/**
 * jvm默认没有打开 assertion，添加  -ea
 */
public class AssertApp {
    public static void main(String[] args) {
//        assert1();
        assert2();
    }

    // 抛出异常
    public static void assert1(){
        boolean flag = 1>2;
        assert  flag;
    }

    public static void assert2(){
        boolean flag = 1 > 2;
        try {
            assert flag : "此flag为false";
        }catch (AssertionError e){
            System.out.println("AssertionError:"+e.getMessage());
        }

    }
}
