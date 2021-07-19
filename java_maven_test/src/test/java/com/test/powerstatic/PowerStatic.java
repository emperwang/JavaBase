package com.test.powerstatic;

import com.wk.util.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testng.Assert;

/**
 * @author: Sparks
 * @Date: 2021/7/19 18:38
 * @Description
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StringUtil.class})
public class PowerStatic {


    @Test
    public void test1(){
        PowerMockito.mockStatic(StringUtil.class);
        String res = "power mock string";
        Mockito.when(StringUtil.generateStr("123")).thenReturn(res);
        System.out.println(StringUtil.generateStr("123"));
    }


    @Test
    public void test2(){
        PowerMockito.mockStatic(StringUtil.class);
        String res = "power mock string2";
        Mockito.when(StringUtil.generateStr("123")).thenReturn(res);
        Assert.assertEquals(res, StringUtil.generateStr("123"));
    }
}
