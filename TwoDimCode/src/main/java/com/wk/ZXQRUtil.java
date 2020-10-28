package com.wk;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * @author: wk
 * @Date: 2020/10/28 10:21
 * @Description 使用zxing 生成二维码以及解析二维码的工具类
 */
public class ZXQRUtil {
    private static final String CHARSET = "UTF-8";
    private static final String FORMATNAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_Size = 300;
    // logo 宽度
    private static final int WIDTH = 60;
    // logo 高度
    private static final int HEIGHT = 60;
    /**
    * @author: wk
    * @Date: 2020/10/28 10:44
    * @Param: 图片编码内容; 嵌入图片; 是否压缩
    * @Return  
    * @Description
    */
    private static BufferedImage createImage(String content, String imagePath, boolean isCompress) throws WriterException, IOException {
        Hashtable hists = new Hashtable<>();
        // 纠错级别
        hists.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hists.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hists.put(EncodeHintType.MARGIN, 1);
        final BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_Size, QRCODE_Size, hists);

        final int width = bitMatrix.getWidth();
        final int height = bitMatrix.getHeight();

        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x=0;x<width;x++){
            for (int y=0; y<height; y++){
                image.setRGB(x,y,bitMatrix.get(x,y)?0xFF000000:0xFFFFFFFF);
            }
        }
        if (imagePath == null || "".equalsIgnoreCase(imagePath)){
            return image;
        }
        // 插入图片
        ZXQRUtil.insertImg(image, imagePath, isCompress);
        return image;
    }

    private static void insertImg(BufferedImage source, String imgPath, boolean isCompress) throws IOException {
        final File file = new File(imgPath);
        if (!file.exists()){
            System.out.println(imgPath +" 该文件不存在");
            return;
        }

        Image src = ImageIO.read(file);
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (isCompress){
            if (width > WIDTH){
                width = WIDTH;
            }
            if (height > HEIGHT){
                height = HEIGHT;
            }

            Image srcScaledInstance = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            final Graphics graphics = tag.getGraphics();
            graphics.drawImage(srcScaledInstance,0,0,null);
            graphics.dispose();
            src = srcScaledInstance;
        }
        // 插入logo
        final Graphics2D graphics = source.createGraphics();
        int x = (QRCODE_Size - width) / 2;
        int y = (QRCODE_Size - height) / 2;

        graphics.drawImage(src,x,y,width,height,null);
        Shape shape = new RoundRectangle2D.Float(x,y,width,height,6,6);
        graphics.setStroke(new BasicStroke(3f));
        graphics.draw(shape);
        graphics.dispose();
    }

    public static void encode(String content, String impath, String destPath, boolean isCompress) throws IOException, WriterException {
        final BufferedImage image = ZXQRUtil.createImage(content, impath, isCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMATNAME, new File(destPath));
    }

    public static void encode(String content, String impath, String destPath) throws IOException, WriterException {
        ZXQRUtil.encode(content,impath,destPath,false);
    }

    public static void encode(String content, String imgPath, OutputStream out, boolean isCompress) throws IOException, WriterException {
        final BufferedImage image = ZXQRUtil.createImage(content, imgPath, isCompress);
        ImageIO.write(image, FORMATNAME, out);
    }

    public static void encode(String content, OutputStream out) throws IOException, WriterException {
        ZXQRUtil.encode(content, null, out, false);
    }

    public static String decode(File file) throws IOException, NotFoundException {
        final BufferedImage image = ImageIO.read(file);
        if (image == null){
            return null;
        }
        final BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable hits = new Hashtable();
        hits.put(DecodeHintType.CHARACTER_SET, CHARSET);
        final Result decode = new MultiFormatReader().decode(bitmap, hits);
        final String resStr = decode.getText();
        return resStr;
    }

    public static String decode(String path) throws IOException, NotFoundException {
        return ZXQRUtil.decode(new File(path));
    }

    public static void mkdirs(String destpath){
        final File file = new File(destpath);
        // 文件不存在时，创建
        if (! file.exists() && !file.isDirectory()){
            file.mkdirs();
        }
    }

    // for test
    public static void main(String[] args) throws IOException, WriterException, NotFoundException {
        String content = "www.baidu.com";
        String imgPath = "C:\\code-workspace\\source\\JavaBase\\TwoDimCode\\src\\main\\resources\\tree.png";
        String destPath = "C:\\code-workspace\\source\\JavaBase\\TwoDimCode\\src\\main\\resources\\bit.png";
        // ZXQRUtil.encode(content, imgPath, destPath,true);
        System.out.println("encode complete.");
        final String decode = ZXQRUtil.decode(destPath);
        System.out.println("decode: " + decode);
    }
}
























