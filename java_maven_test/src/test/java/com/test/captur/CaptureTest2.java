package com.test.captur;

import com.wk.util.MathUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.testng.Assert;

/**
 * @author: Sparks
 * @Date: 2021/7/19 18:31
 * @Description
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptureTest2 {

    @Mock
    private MathUtil mathUtil;
    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;
    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    public void test(){
        Mockito.when(mathUtil.add(1,2)).thenReturn(5);
        Mockito.when(mathUtil.add(2,3)).thenReturn(10);
        Mockito.when(mathUtil.squareLong(2L)).thenReturn(10L);
        Mockito.when(mathUtil.squareLong(4L)).thenReturn(100L);

        Assert.assertEquals(5, mathUtil.add(1,2));
        Assert.assertEquals(5, mathUtil.add(1,2));
        Assert.assertEquals(10, mathUtil.add(2,3));
        Mockito.verify(mathUtil, Mockito.times(3)).add(integerArgumentCaptor.capture(),integerArgumentCaptor.capture());
        integerArgumentCaptor.getAllValues().stream().forEach(System.out::println);
        Assert.assertEquals(10L, mathUtil.squareLong(2L));
        Assert.assertEquals(100L, mathUtil.squareLong(4L));
        Mockito.verify(mathUtil, Mockito.times(2)).squareLong(longArgumentCaptor.capture());
        longArgumentCaptor.getAllValues().stream().forEach(System.out::print);
    }
}
