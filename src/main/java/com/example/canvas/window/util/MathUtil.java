package com.example.canvas.window.util;

import com.sun.javafx.tk.Toolkit;
import javafx.scene.text.Font;

/**
 * @author LTJ
 * @date 2021/11/3
 */
public class MathUtil {
    public final static double epsilon= 1e-4;
    public static double round(double d,int precision){
        if(precision<1){
            return (long)d;
        }
        long factor=10;
        for(int i=0;i<precision-1;i++){
            factor *= factor;
        }

        double round = (double)Math.round(d*factor)/factor;
        return round;
    }

    public static boolean hasTail(double d){
        double round = round(d, 4);
        if(d-round<epsilon){
            return false;
        }else{
            return true;
        }
    }

    public static double getCharWidth(String text, Font font){
        float width = Toolkit.getToolkit().getFontLoader().computeStringWidth(text, font);
        return width;
    }

    public static double getCharHeight(String text,Font font){
        float lineHeight = Toolkit.getToolkit().getFontLoader().getFontMetrics(font).getLineHeight();
        return lineHeight;
    }
}
