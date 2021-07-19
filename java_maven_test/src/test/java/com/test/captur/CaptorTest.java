package com.test.captur;

import com.wk.util.MathUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

/**
 * @author: Sparks
 * @Date: 2021/7/19 18:25
 * @Description
 */
@RunWith(MockitoJUnitRunner.class)
public class CaptorTest {

    @Mock
    private MathUtil mathUtil;

    @Test
    public void test1(){
        Mockito.when(mathUtil.add(1,2)).thenReturn(5);
        Mockito.when(mathUtil.squareLong(1L)).thenReturn(20L);
        ArgumentCaptor<Integer> intCapture = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Assert.assertEquals(5, mathUtil.add(1,2));;
        Assert.assertEquals(20L, mathUtil.squareLong(1L));

        Mockito.verify(mathUtil).add(intCapture.capture(),intCapture.capture());
        List<Integer> allValues = intCapture.getAllValues();
        allValues.stream().forEach(System.out::println);

        Mockito.verify(mathUtil).squareLong(longArgumentCaptor.capture());
        Mockito.verify(mathUtil, Mockito.times(1)).squareLong(longArgumentCaptor.getValue());

    }
}
