package testng;

import org.testng.annotations.Test;

/**
 * @author: Sparks
 * @Date: 2021/5/23 8:35
 * @Description
 */
// 数据驱动测试
public class DataProvider {

    @Test(dataProvider = "data")
    public void testCase1(String username, String password, String message){
        System.out.println("如果输入: "+username+","+ password+","+"那么提示为: "+ message);
    }

    @org.testng.annotations.DataProvider(name = "data")
    public Object[][] dataProvider(){
        return new Object[][] {new Object[]{"空账号","正确密码","账号不能为空"}, new Object[]{
                "正确账号","空密码","密码不能为空"}};

    }
}
