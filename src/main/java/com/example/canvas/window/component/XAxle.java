package com.example.canvas.window.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.text.DecimalFormat;

import static com.example.canvas.window.util.MathUtil.*;

/**
 * @author LTJ
 * @date 2021/11/3
 */
public class XAxle extends Canvas {
    private Position o;
    private Position oe;
    private double contentW;
    private double contentH;
    private double xLow;
    private double xHigh;
    private double padding;
    private double minGap;

    protected XAxle() {
    }

    protected XAxle(double width, double height) {
        super(width, height);
    }

    public XAxle(double contentW,double contentH,double xLow,double xHigh,double padding,double minGap){
        this(contentW + 2 * padding,contentH + 2 * padding);
        this.contentW = contentW;
        this.contentH = contentH;
        this.xLow = xLow;
        this.xHigh = xHigh;
        this.padding = padding;
        this.minGap = minGap;
        o = new Position();
        o.x=padding;
        o.y=contentH+padding;
        oe = new Position();
        oe.x = o.x + contentW;
        oe.y=o.y;
    }

    public void rePaint(double xLow,double xHigh,double minGap){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setLineWidth(3);
        gc.strokeLine(o.x, o.y, oe.x, oe.y);
        double temp1 = contentW / minGap;
        // 刻度数量
        double labelCnt = Math.floor(contentW / minGap) + 1;

        double range = xHigh - xLow;
        // 刻度增长步长
        double step = range / (labelCnt - 1);
        step = round(step, 2);
        DecimalFormat df = new DecimalFormat("#.00");
        final double labelLen = 10;
        for (int i = 0; i < labelCnt; i++) {
            gc.setLineWidth(1);
            // 画刻度
            gc.strokeLine(o.x + i * minGap, o.y, o.x + i * minGap, o.y - labelLen);
            // 画数字
            temp1 = xLow + i * step;
            String x = df.format(temp1);
            double charHeight = getCharHeight(x, gc.getFont());
            double charWidth = getCharWidth(x, gc.getFont());
            gc.fillText(x, o.x + i * minGap - charWidth / 2, o.y + charHeight);
        }
    }

    public void rePaint(){
        rePaint(xLow, xHigh, minGap);
//        GraphicsContext gc = getGraphicsContext2D();
//        gc.fillRect(1,1,30,30);
    }

}
