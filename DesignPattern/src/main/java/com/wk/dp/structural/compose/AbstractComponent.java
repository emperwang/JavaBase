package com.wk.dp.structural.compose;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 12:39
 * @Description
 */
public abstract class AbstractComponent implements Component {
    private String name;

    public AbstractComponent(String name){
        this.name = name;
    }
    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public void showInfo() {
        System.out.println("this is : " + name);
    }
    @Override
    public String getName() {
        return name;
    }
}
