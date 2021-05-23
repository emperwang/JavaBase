package testng;

import org.testng.annotations.*;

/**
 * @author: Sparks
 * @Date: 2021/5/19 15:51
 * @Description
 */
public class testNGAnnotation {

    @BeforeSuite   // 在该suit的所有test前执行
    public void beforeSuit(){
        System.out.println("before suit");
    }
    /*
    一个suit对应一个顶级模块,比如一个软件项目分为4个模块,那么每个模块就是一个suit. 一般结合 testng.xml 文件使用
     */
    @AfterSuite   // 在该suit的所有test后执行
    public void afterSuit(){
        System.out.println("afterSuit");
    }

    @BeforeTest   // 在该test所有的class前执行
    public void beforeTest(){
        System.out.println("beforeTest");
    }

    @AfterTest  // 在该test的所有class执行后再执行
    public void afterTest(){
        System.out.println("afterTest");
    }

    @BeforeClass   // 在该class的所有@Test 方法执行前执行
    public void beforeCLass(){
        System.out.println("beforeCLass");
    }

    @AfterClass
    public void afterClass(){
        System.out.println("afterClass");
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.println("beforeMethod");
    }

    @AfterMethod
    public void afterMethod(){
        System.out.println("afterMethod");
    }

    @BeforeGroups(groups = {"g1"})
    public void beforeGroup(){
        System.out.println("beforeGroup");
    }

    @AfterGroups(groups = {"g1"})
    public void afterGroup(){
        System.out.println("afterGroup");
    }

    @BeforeGroups(groups = {"g2"})
    public void beforeGroup2(){
        System.out.println("beforeGroup2");
    }

    @AfterGroups(groups = {"g2"})
    public void afterGroup2(){
        System.out.println("afterGroup2 ");
    }

    @Test(groups = {"g1"})
    public void testcase1(){
        System.out.println("g1 testcase1");
    }

    @Test(groups = {"g2"})
    public void testCase2(){
        System.out.println("g2 testCase2");
    }

    @Test(groups = {"g2"})
    public void testcase3(){
        System.out.println("g2 testcase3");
    }

    @Test(groups = {"g1","g2"})
    public void testcase4(){
        System.out.println("g1 g2 testcase4");
    }
}


