package com.wk.mvel.demo1;

import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MainUserDemo1 {
    public static void main(String[] args) {
//        demo1();
        demo2();
        demo3();
    }

    public static void demo3(){
        HashMap<String, String> map = new HashMap<>();
        map.put("x","10");
        map.put("y","20");
        Object result = MVEL.eval("x+y", map);
        System.out.println("result :"+result);
    }

    public static void demo2(){
        String expression = "foobar > 99";
        Map map = new HashMap();
        map.put("foobar",new Integer(100));
        Serializable compileExpression = MVEL.compileExpression(expression);

        Boolean result = (Boolean) MVEL.executeExpression(compileExpression, map);
        System.out.println("result is : "+result.toString());
    }

    public static void demo1(){
        String expression = "foobar > 99";
        Map map = new HashMap();
        map.put("foobar",new Integer(100));

        Boolean eval = (Boolean) MVEL.eval(expression, map);
        System.out.println(eval.booleanValue());
    }
}
