package com.wk.swing.layout;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
// 横向布局
public class AfRowLayout implements LayoutManager2{

    private List<Item> items = new ArrayList<>();
    private int gap = 2;
    private boolean usePerferredSize = false;

    public AfRowLayout(){

    }
    public AfRowLayout(int gap){
        this.gap = gap;
    }
    public AfRowLayout(int gap, boolean usePerferredSize){
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
            Item it = iterator.next();
            if (it.comp == comp){
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
        Rectangle rect = new Rectangle(parent.getWidth(), parent.getHeight());
        Insets insets = parent.getInsets();
        rect.x += insets.left;
        rect.y += insets.top;
        rect.width -= (insets.left + insets.right);
        rect.height -= (insets.top + insets.bottom);

        // 第一轮: 过滤掉无效的item
        List<Item> validItems = new ArrayList<>();
        for (Item item : items) {
            if (item.comp.isVisible()){
                validItems.add(item);
            }
        }

        // 第二轮:百分比   像素  auto的直接计算出结果; 权重的 在第三轮计算
        int totalGapSize = gap * (validItems.size() - 1);
        int validSize = rect.width - totalGapSize;
        int totalSize = 0;
        int totalWeight = 0;
        for (Item item : validItems) {
            Dimension preferredSize = item.comp.getPreferredSize();
            item.width = preferredSize.width;
            item.height = usePerferredSize?preferredSize.height : rect.height;
            item.weight = 0;
            // 计算宽度
            String cstr = item.constraints;
            if (cstr == null || cstr.length() == 0){

            }else if (cstr.equalsIgnoreCase("auto")){

            }else if(cstr.endsWith("%")){ // 按照百分比
                int num = Integer.valueOf(cstr.substring(0, cstr.length()-1));
                item.width = validSize * num / 100;
            }else if (cstr.endsWith("w")){ // 按照权重
                int num = Integer.valueOf(cstr.substring(0, cstr.length()-1));
                item.width = 0;
                item.weight= num;
            }else if (cstr.endsWith("px")){ // 按照像素
                int num = Integer.valueOf(cstr.substring(0, cstr.length()-2));
                item.width = num;
            } else {  // 按照像素
                int num = Integer.valueOf(cstr);
                item.width = num;
            }
            totalSize += item.width;
            totalWeight += item.weight;
        }
        // 第三轮: 剩余空间按权重分配
        if (totalWeight > 0){
            int remainSize = validSize - totalSize;
            double unit = (double)remainSize / totalWeight;
            for (Item validItem : validItems) {
                if (validItem.weight > 0){
                    validItem.width = (int)(unit * validItem.weight);
                }
            }
        }
        // 第四轮: 按宽度和高度布局
        int x =0;
        for (Item item : validItems) {
            int y = (rect.height - item.height)/2;
            if((x + item.width) > rect.width){
                item.width = rect.width - x;
            }
            if (item.width <= 0) break;

            item.comp.setBounds(rect.x +x, rect.y+y, item.width, item.height);;

            x += item.width;
            x += gap;
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
