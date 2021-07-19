package com.wk.mockstatic;

import com.wk.util.StringUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author: Sparks
 * @Date: 2021/7/19 18:58
 * @Description
 */
public class MockStatic2Test {
    private static MockedStatic<StringUtil> mockedStatic;
    @BeforeClass
    public static void setup(){
        mockedStatic = Mockito.mockStatic(StringUtil.class);
    }

    @AfterClass
    public static void tearDown(){
        if (!mockedStatic.isClosed()){
            mockedStatic.close();
        }
    }


    @Test
    public void test1(){
        String key = "123";
        String res = "mockito core result";
        mockedStatic.when(() -> StringUtil.generateStr(key)).thenReturn(res);
        System.out.println(StringUtil.generateStr(key));
    }


    @Test
    public void test2(){
        String key = "1234";
        String res = "mockito core result2";
        mockedStatic.when(() -> StringUtil.generateStr(key)).thenReturn(res);
        System.out.println(StringUtil.generateStr(key));
    }
}
