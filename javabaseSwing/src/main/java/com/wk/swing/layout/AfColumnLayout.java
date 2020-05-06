package com.wk.swing.layout;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 纵向布局
 */
public class AfColumnLayout implements LayoutManager2 {

    private List<Item> items = new ArrayList<>();
    private int gap = 2;
    private boolean usePerferredSize = false; // 竖立方向是否占满

    public AfColumnLayout(){

    }

    public AfColumnLayout(int gap){
        this.gap = gap;
    }

    public AfColumnLayout(int gap, boolean usePerferredSize){
        this.gap = gap;
        this.usePerferredSize = usePerferredSize;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        Item item = new Item();
        item.comp = comp;
        item.constraints = (String)constraints;
        items.add(item);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(30,30);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        Item item = new Item();
        item.comp = comp;
        item.constraints = "auto";
        items.add(item);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()){
            Item next = iterator.next();
            if (next.comp == comp){
                iterator.remove();
            }
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(30,30);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(30,30);
    }

    @Override
    public void layoutContainer(Container parent) {
        // 得到内矩形
        Rectangle rectangle = new Rectangle(parent.getWidth(), parent.getHeight());
        Insets insets = parent.getInsets();
        rectangle.x += insets.left;
        rectangle.y += insets.top;

        rectangle.width -= (insets.left + insets.right);
        rectangle.height -= (insets.top + insets.bottom);
        // 第一轮: 过滤无效的item(有些控件时隐藏的)
        List<Item> validItems = new ArrayList<>();
        for (Item item : items) {
            if (item.comp.isVisible()){
                validItems.add(item);
            }
        }

        // 第二轮: 百分比  像素  auto的,直接计算结果; 权重的,在第三轮计算
        int totalGapSize = gap * (validItems.size()-1); // 间距大小
        int validSize = rectangle.height - totalGapSize;
        int totalSize = 0;
        int totalWeight = 0;
        for (Item it : validItems) {
            Dimension preferredSize = it.comp.getPreferredSize();
            it.width = usePerferredSize?preferredSize.width : rectangle.width;
            it.height = preferredSize.height;
            it.weight = 0;

            // 计算宽度
            String cstr = it.constraints;
            if (cstr == null || cstr.length() == 0){

            }else if(cstr.equalsIgnoreCase("auto")){

            }else if (cstr.endsWith("%")){
                int num = Integer.valueOf(cstr.substring(0, cstr.length()-1));
                it.height = validSize * num / 100;
            }else if(cstr.endsWith("w")){   //权重
                int num = Integer.valueOf(cstr.substring(0, cstr.length()-1));
                it.height = 0;
                it.weight = num;
            }else if(cstr.endsWith("px")){// 像素
                int num = Integer.valueOf(cstr.substring(0, cstr.length()-2));
                it.height = num;
            }else { // 像素
                int num = Integer.valueOf(cstr);
                it.height = num;
            }
            totalSize += it.height;
            totalWeight += it.weight;
        }

        // 第三轮: 剩余控件按权重分配
        if (totalWeight > 0){
            int remainSize = validSize - totalSize;
            double unit = (double)remainSize / totalWeight;
            for (Item item : validItems) {
                if (item.weight > 0){
                    item.height = (int)(unit * item.weight);
                }
            }
        }
        // 第四轮: 按宽度和高度布局
        int y = 0;
        for (Item it : validItems) {
            int x = 0;      // 水平靠左
            if ( y + it.height > rectangle.height){
                it.height = rectangle.height - y;
            }
            if (it.height <= 0) break;

            it.comp.setBounds(rectangle.x + x, rectangle.y+y, it.width, it.height);;

            y += it.height;
            y += gap;
        }
    }

    private static class Item{
        Component comp;
        String constraints = "auto";
        int width = 0;
        int height = 0;
        int weight = 0;
    }
}
