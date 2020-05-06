package com.wk.swing.layout;

public class AfMargin {
    // 上 左 下 右
    public int top, left, bottom, right;

    // 九个常见位置
    public static final AfMargin FULL = new AfMargin(0,0,0,0);
    public static final AfMargin TOP_LEFT = new AfMargin(0,0,-1,-1);
    public static final AfMargin TOP_CENTER = new AfMargin(0,-1,-1,-1);
    public static final AfMargin TOP_RIGHT = new AfMargin(0,-1,-1,0);
    public static final AfMargin CENTER_LEFT = new AfMargin(-1,0, -1,-1);
    public static final AfMargin CENTER = new AfMargin(-1,-1,-1,-1);
    public static final AfMargin CENTER_RIGHT = new AfMargin(-1,-1,-1,0);
    public static final AfMargin BOTTOM_LEFT = new AfMargin(-1,0,0,-1);
    public static final AfMargin BOTTOM_CENTER = new AfMargin(-1,-1,0,-1);
    public static final AfMargin BOTTOm_RIGHT = new AfMargin(-1,-1,0,0);

    public AfMargin(){}
    public AfMargin(int val){
        top = left = bottom = right = val;
    }
    public AfMargin(int hval, int vVal){
        this.left = this.right = hval;
        this.top = this.bottom = vVal;
    }
    public AfMargin(int top, int left, int bottom, int right){
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
}
