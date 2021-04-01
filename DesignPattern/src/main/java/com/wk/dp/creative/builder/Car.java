package com.wk.dp.creative.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 9:45
 * @Description
 */
public class Car {
    private List<Component> components = new ArrayList<>();

    public void addComponent(Component ent){
        components.add(ent);
    }

    public void remoteComponent(Component component){
        components.remove(component);
    }

    public void showInfo(){
        StringBuilder builder = new StringBuilder();
        builder.append("car : ").append("\n");
        components.forEach(component -> {
            builder.append(component.getName()).append("  ");
        });
        System.out.println(builder.toString());
    }
}
