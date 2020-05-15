package com.wk.demo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    @Test
    public void testClazz(){
        Integer iter = new Integer(1);
        boolean b = Object.class.isAssignableFrom(iter.getClass());
        System.out.println(b);

    }
}
