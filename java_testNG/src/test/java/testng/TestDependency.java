package testng;

import org.testng.annotations.Test;

/**
 * @author: Sparks
 * @Date: 2021/5/23 9:01
 * @Description
 */
public class TestDependency {

    @Test(description = "测试用例 1")
    public void testCase1(){
        System.out.println("testCase1");
    }

    @Test(priority = 2)
    public void testcase2(){
        System.out.println("testcase2");
    }

    @Test(priority = 1)
    public void testCase3(){
        System.out.println("testCase3");
    }

    @Test
    public void testcase4(){
        System.out.println("testcase4");
    }

    @Test(groups = "g1")
    public void testCase5(){
        System.out.println("g1 testCase5");
    }

    @Test(enabled = false)
    public void testCase6(){
        System.out.println("testCase6");
    }

    @Test(dependsOnGroups = "g1", dependsOnMethods = "testcase4",alwaysRun = true)
    public void testcase7(){
        System.out.println("testcase7");
    }
}
