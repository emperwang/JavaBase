package com.wk;

import com.swetake.util.Qrcode;
import com.wk.qrcode.TwoDimensionCodeImage;
import jp.sourceforge.qrcode.QRCodeDecoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author: wk
 * @Date: 2020/10/28 14:02
 * @Description
 */
public class QRCodeUtil {
    /**
    * @author: wk
    * @Date: 2020/10/28 14:06
    * @Param: content:文件信息, imgPath:二维码路径,imgType: 二维码类型:png, size: 二维码尺寸
    * @Return  
    * @Description
    */
    public void encodeQRCode(String content, String imgPath,String imgType,String logoPath, int size) throws IOException {
        // image 内存中的一张图片
        final BufferedImage image = qRecodeCommon(content, imgType, logoPath, size);
        mkdirs(imgPath);
        ImageIO.write(image, imgType,new File(imgPath));
    }

    public static void mkdirs(String path){
        final File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    // content: 内容, imgType: 图片类型, size:图片大小(1-40)
    private BufferedImage qRecodeCommon(String content, String imgType,String logoPath, int size) throws IOException {
        final Qrcode qrcodeHandler = new Qrcode();
        // 设置排错率:7% L < M < Q < H 30%: 排错率越高,可存储的信息越少,但是对二维码的清晰度要求越小
        qrcodeHandler.setQrcodeErrorCorrect('M');
        // 可存放的信息类型:N 数字, A: 数字+A-Z, B: 所有
        qrcodeHandler.setQrcodeEncodeMode('B');
        // 尺寸: 取值返回 1-40
        if (size<1 || size>40){
            System.out.println("invalid size. should in [1,40]");
            return null;
        }
        qrcodeHandler.setQrcodeVersion(size);

        // string -> byte[]
        final byte[] contentBytes = content.getBytes("UTF-8");
        final boolean[][] calQrcode = qrcodeHandler.calQrcode(contentBytes);
        // 计算二维码图片的尺寸
        int imgSize = 67 + 12 * (size - 1);
        // BufferedImage: 内存中的图片
        final BufferedImage bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);

        // 创建一个画板
        final Graphics2D gs = bufImg.createGraphics();
        gs.setBackground(Color.WHITE);  // 将画板的背景色设置为白色
        // 初始化
        gs.clearRect(0,0,imgSize,imgSize);
        // 设置画板上图像的颜色(二维码颜色)
        gs.setColor(Color.BLACK);
        int pixoff = 2;
        for (int j = 0; j <calQrcode.length; j++){
            for (int i=0; i < calQrcode.length; i++){
                if (calQrcode[j][i]){
                    gs.fillRect(j*3+pixoff, i*3+pixoff,3,3);
                }
            }
        }

        // 增加logo
        final BufferedImage logoImg = ImageIO.read(new File(logoPath));
        final int height = bufImg.getHeight();
        final int width = bufImg.getWidth();

        // 在已生成的二维码上 画 logo
        gs.drawImage(logoImg, imgSize/5*2, imgSize/5*2,width/5,height/5,null);
        // 释放空间
        gs.dispose();
        // 清理
        bufImg.flush();
        return bufImg;
    }

    // 解密二维码
    public String decodeQrcode(String imgPath) throws IOException {
        final BufferedImage bufImg = ImageIO.read(new File(imgPath));

        // 解密
        final QRCodeDecoder decoder = new QRCodeDecoder();
        final TwoDimensionCodeImage codeImage = new TwoDimensionCodeImage(bufImg);

        final byte[] decodes = decoder.decode(codeImage);

        // byte -> String
        final String res = new String(decodes, "UTF-8");
        return res;
    }

    // test
    public static void main(String[] args) throws IOException {
        String CHARSET = "UTF-8";
        String FORMATNAME = "JPG";
        int QRCODE_Size = 6;
        String content = "www.baidu.com";
        String imgPath = "C:\\code-workspace\\source\\JavaBase\\TwoDimCode\\src\\main\\resources\\tree.png";
        String destPath = "C:\\code-workspace\\source\\JavaBase\\TwoDimCode\\src\\main\\resources\\qrocde.png";

        final QRCodeUtil qrCodeUtil = new QRCodeUtil();
        qrCodeUtil.encodeQRCode(content,destPath,FORMATNAME,imgPath,QRCODE_Size);
        System.out.println("code complete");

        final String s = qrCodeUtil.decodeQrcode(destPath);
        System.out.println("decode msg: "+s);
    }
}





























