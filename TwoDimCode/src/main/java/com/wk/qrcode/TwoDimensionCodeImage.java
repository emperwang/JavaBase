package com.wk.qrcode;

import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

/**
 * @author: wk
 * @Date: 2020/10/28 14:04
 * @Description
 */
public class TwoDimensionCodeImage implements QRCodeImage {
    BufferedImage bufImg;
    public TwoDimensionCodeImage(BufferedImage bufImg){
        this.bufImg = bufImg;
    }

    @Override
    public int getWidth() {
        return bufImg.getWidth();
    }

    @Override
    public int getHeight() {
        return bufImg.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        return bufImg.getRGB(x,y);
    }
}
