package com.wk.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author: wk
 * @Date: 2020/12/10 17:04
 * @Description
 */
public class ComposeTest extends TestCase {

    public static Test suite(){
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(TestOne.class);
        suite.addTestSuite(TestTwo.class);
        return suite;
    }
}
