package com.wk.swing.event.mouseEvent.viewer;

import com.wk.swing.customcomponent.image.util.AfImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ButtonImage extends AfImageView {

    public Image image;

    public ButtonImage(File file){
        try{
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setImage(image);
        this.setScaleType(AfImageView.FIT_CENTER);
    }
}
