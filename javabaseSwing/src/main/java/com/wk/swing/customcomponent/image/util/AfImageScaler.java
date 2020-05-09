package com.wk.swing.customcomponent.image.util;

import java.awt.*;

// 图片缩放工具
public class AfImageScaler {

    // 输入参数
    // 图片的宽度  高度
    private int imgW, imgH;
    // 要绘制的目标区域
    private Rectangle rect;

    // imgW, imgH 图片的宽度/高度
    // rect 目标区域
    public AfImageScaler(int imgw, int imgh, Rectangle rect){
        this.imgW = imgw;
        this.imgH = imgh;
        this.rect = rect;
    }

    public AfImageScaler(int imgW, int imgH, int dstW, int dstH){
        this(imgW, imgH, new Rectangle(dstW, dstH));
    }

    // 拉伸显示, 占满空间(比例可能失调)
    public Rectangle fitxy(){
        return this.rect;
    }

    // 居中显示, 保持长宽比, 且适合目标矩形
    public Rectangle fitCenter(){
        int width = rect.width;
        int height = rect.height;

        int fitW = width;
        int fitH = width * imgH/imgW;
        if (fitH > height){
            fitH = height;
            fitW = height * imgW/imgH;
        }
        int x = (width - fitW)/2;
        int y = (height - fitH)/2;

        return new Rectangle(rect.x+x, rect.y+y,fitW, fitH);
    }

    // 如果蹄片小于目标矩形, 则直接居中显示
    // 如果图片大于目标矩形, 则按照fitCenter() 缩放后显示
    public Rectangle fitCenterInside(){

        int width = rect.width;
        int height = rect.height;

        int fitW, fitH;

        if (imgW <= width && imgH <= height){
            fitW = imgW;
            fitH = imgH;

            int x = (width - fitW)/2;
            int y = (height - fitH)/2;
            return new Rectangle(rect.x+x, rect.y+y, fitW, fitH);
        }else{
            return fitCenter();
        }
    }

}
