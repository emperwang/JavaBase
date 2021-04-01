package com.wk.dp.structural.compose;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 11:52
 * @Description
 */
public abstract class ComponentComposite extends AbstractComponent {
    private List<Component> components;
    public ComponentComposite(String name){
        super(name);
        this.components = new ArrayList<>();
    }

    public void addComponent(Component component){
        components.add(component);
    }

    public void remoteComponent(Component component){
        components.remove(component);
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public void showInfo() {
        StringBuilder builder = new StringBuilder();
        components.forEach(component -> {
            builder.append(component.getName()).append("\n");
            if (component.isComposite()) {
                ((ComponentComposite)component).getComponents().forEach(component1 -> {
                    builder.append(component1.getName()).append("\n");
                    ((ComponentComposite)component1).getComponents().forEach(component2 -> {
                        builder.append(component2.getName()).append("\n");
                    });
                });
            }
        });
        System.out.println(builder.toString());
    }

    public List<Component> getComponents() {
        return components;
    }
}
