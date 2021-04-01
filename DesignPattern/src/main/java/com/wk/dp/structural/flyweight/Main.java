package com.wk.dp.structural.flyweight;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 17:02
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        PigmentBoxManager boxManager = new PigmentBoxManager();

        PigmentBox pigmentBox = boxManager.getPigmentBox(ColorEnum.RED);
        System.out.println(pigmentBox);
        boxManager.releasePigmentBox(pigmentBox);
        PigmentBox pigmentBox1 = boxManager.getPigmentBox(ColorEnum.RED);
        System.out.println(pigmentBox1);
    }
}
