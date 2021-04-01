package com.wk.dp.structural.flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:56
 * @Description
 */
public class PigmentBoxManager {
    private Map<ColorEnum,PigmentBox> pigmentBoxes;

    public PigmentBoxManager(){
        pigmentBoxes = new HashMap<>();
        pigmentBoxes.put(ColorEnum.RED,new RedPigmentBox());
        pigmentBoxes.put(ColorEnum.BLUE,new BluePigmentBox());
        pigmentBoxes.put(ColorEnum.GREEN,new GreenPigmentBox());
    }

    public PigmentBox getPigmentBox(ColorEnum color){
        PigmentBox pigmentBox = pigmentBoxes.get(color);
        if (pigmentBox == null){
            System.out.println("No such color box");
            return null;
        }
        if (pigmentBox.isInUser()){
            System.out.println(color.name()+" box in use");
            return null;
        }
        pigmentBox.setUse();
        return pigmentBox;
    }

    public void releasePigmentBox(PigmentBox pigmentBox){
        pigmentBox.release();
    }
}
