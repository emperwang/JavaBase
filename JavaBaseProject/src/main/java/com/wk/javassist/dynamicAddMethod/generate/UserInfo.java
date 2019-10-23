package com.wk.javassist.dynamicAddMethod.generate;
// 动态生成后的类
public class UserInfo
{
    private String name;
    private Integer age;

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getAge()
    {
        return this.age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public String toString()
    {
        return "UserInfo{name='" + this.name + '\'' + ", age=" + this.age + '}';
    }

    public int sum(int paramInt1, int paramInt2)
    {
        return paramInt1 + paramInt2;
    }
}
