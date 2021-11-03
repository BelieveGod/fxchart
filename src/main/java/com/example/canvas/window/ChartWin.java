package com.example.canvas.window;

import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author LTJ
 * @date 2021/11/3
 */
@Slf4j
public class ChartWin extends Application {
    final static double epsilon= 1e-4;
    private final double WIDTH=800;
    private final double HEIGHT=450;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setWidth(WIDTH);
//        primaryStage.setHeight(HEIGHT);
        final double padding=40;
        final double chartPadding=40;
        final double xLow=1;
        final double xHight=10;
        final double yLow=770;
        final double yHight=850;
        final double minGap=40;


        Pane root = new Pane();
//        root.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(3))));
        double[] xs = {1,9};
        Canvas xAxel = createXAxel(WIDTH-2*padding, 30, xs, 40);
        root.getChildren().add(xAxel);
        double offset = getAlignCenterX(WIDTH, xAxel.getWidth());
        xAxel.relocate(offset, HEIGHT-padding-xAxel.getHeight());
        // Y轴
        double[] ys = {770,850};
        Canvas yAxel = createYAxel(30, HEIGHT-2*padding, ys, 40);
        root.getChildren().add(yAxel);
        yAxel.relocate(offset,HEIGHT-padding-yAxel.getHeight());

        // 画数据集
        List<Position> positions1 = generateMockData(xLow, xHight, yLow, yHight, 6);
        List<Position> positions = dataConverCanvas(WIDTH - 2 * padding, HEIGHT - 2 * padding, data, xs, ys, chartPadding);
        Canvas dataPoint = createDataPoint(WIDTH - 2 * padding, HEIGHT - 2 * padding, positions,chartPadding);
        root.getChildren().add(dataPoint);
        dataPoint.relocate(offset,HEIGHT-padding-dataPoint.getHeight());

        Canvas dataLine = createDataLine(WIDTH - 2 * padding, HEIGHT - 2 * padding, positions,chartPadding);
        root.getChildren().add(dataLine);
        dataLine.relocate(offset,HEIGHT-padding-dataLine.getHeight());
        Scene scene = new Scene(root,WIDTH,HEIGHT,Color.YELLOW);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private List<Position> generateMockData(double xLow, double xHigh, double yLow, double yHigh, int count) {
        List<Position> positions = new LinkedList<>();
        for(int i=0;i<count;i++){
            double xRange = xHigh - xLow;
            double random = Math.random();
            double x = random * xRange + xLow;

            double yRange = yHigh - yLow;
            random = Math.random();
            double y = random * xRange + yLow;
            Position position = new Position();
            positions.add(position);
            position.x=x;
            position.y=y;
        }
        return positions;
    }

    private double getCharWidth(String text, Font font){
        float width = Toolkit.getToolkit().getFontLoader().computeStringWidth(text, font);
        return width;
    }

    private double getCharHeight(String text,Font font){
        float lineHeight = Toolkit.getToolkit().getFontLoader().getFontMetrics(font).getLineHeight();
        return lineHeight;
    }

    /**
     * 获取水平对其的位移量
     * @param target
     * @param src
     * @return
     */
    private double getAlignCenterX(double target,double src){
        double offset = target / 2 - src / 2;
        return offset;
    }

    private static int compareDouble(double src,double target){
        if(target-src<epsilon){
            return 0;
        }
        return Double.compare(src, target);
    }

    /**
     * 舍去若干位
     * @param d
     * @param precision
     * @return
     */
    private static double round(double d,int precision){
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

    private static boolean hasTail(double d){
        double round = round(d, 4);
        if(d-round<epsilon){
            return false;
        }else{
            return true;
        }
    }


    private Canvas createXAxel(double contentW,double contentH,double[] xs,double minGap){
        // 内间距
        final double padding=40;
        Canvas canvas = new Canvas(contentW + 2 * padding, contentH + 2 * padding);
        Position o = new Position();
        o.x=padding;
        o.y=contentH+padding;
        Position oe = new Position();
        oe.x = o.x + contentW;
        oe.y=o.y;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(3);
        gc.strokeLine(o.x, o.y, oe.x, oe.y);

        if(xs!=null && xs.length>0){
            double temp1 = contentW / minGap;
            // 刻度数量
            double labelCnt = Math.floor(contentW / minGap)+1;

            double start = xs[0];
            double end = xs[xs.length - 1];
            double range = end - start;
            // 刻度增长步长
            double step = range / (labelCnt - 1);
            step = round(step, 2);
            DecimalFormat df = new DecimalFormat("#.00");
            final double labelLen=10;
            for(int i=0;i<labelCnt;i++){
                gc.setLineWidth(1);
                // 画刻度
                gc.strokeLine(o.x + i * minGap, o.y, o.x + i * minGap, o.y - labelLen);
                // 画数字
                temp1 = start + i * step;
                String x = df.format(temp1);
                double charHeight = getCharHeight(x, gc.getFont());
                double charWidth = getCharWidth(x, gc.getFont());
                gc.fillText(x, o.x + i * minGap-charWidth/2,o.y+charHeight);
            }
        }

        return canvas;
    }


    private Canvas createYAxel(double contentW,double contentH,double[] ys,double minGap){
        // 内间距
        final double padding=40;
        Canvas canvas = new Canvas(contentW + 2 * padding, contentH + 2 * padding);
        Position o = new Position();
        o.x=padding;
        o.y=contentH+padding;
        Position oe = new Position();
        oe.x = o.x;
        oe.y=o.y-contentH;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(3);
        gc.strokeLine(o.x, o.y, oe.x, oe.y);

        if(ys!=null && ys.length>0){
            double temp1 = contentH / minGap;
            // 刻度数量
            double labelCnt = Math.floor(contentH / minGap)+1;

            double start = ys[0];
            double end = ys[ys.length - 1];
            double range = end - start;
            // 刻度增长步长
            double step = range / (labelCnt - 1);
            step = round(step, 2);
            DecimalFormat df = new DecimalFormat("#.00");
            final double labelLen=10;
            for(int i=0;i<labelCnt;i++){
                gc.setLineWidth(1);
                // 画刻度
                gc.strokeLine(o.x, o.y-i*minGap, o.x + labelLen, o.y-i*minGap);
                // 画数字
                temp1 = start + i * step;
                String x = df.format(temp1);
                double charHeight = getCharHeight(x, gc.getFont());
                double charWidth = getCharWidth(x, gc.getFont());
                gc.fillText(x, o.x-charWidth,o.y-i*minGap+charHeight/4);
            }
        }
        return canvas;
    }


    private Canvas createDataPoint(double contentW,double contentH,List<Position> positions,final double padding){
        Canvas canvas = new Canvas(contentW + 2 * padding, contentH + 2 * padding);
        final double radius=4;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        for (Position position : positions) {
            gc.fillOval(position.x-radius,position.y-radius,radius*2,radius*2);
        }
        return canvas;
    }

    private Canvas createDataLine(double contentW,double contentH,List<Position> positions,final double padding){
        Canvas canvas = new Canvas(contentW + 2 * padding, contentH + 2 * padding);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.setLineDashes(5);
        gc.setLineWidth(2);
        gc.setLineCap(StrokeLineCap.BUTT);

        for(int i=0;i<positions.size()-1;i++){
            gc.strokeLine(positions.get(i).x, positions.get(i).y, positions.get(i + 1).x, positions.get(i+1).y);
        }
        return canvas;
    }

    private List<Position> dataConverCanvas(double contentW, double contentH, double[][] data, double[] xs, double[] ys, final double padding){
        Position o = new Position();
        o.x=padding;
        o.y=contentH+padding;
        List<Position> positions = new LinkedList<>();
        for (double[] point : data) {
            double diff = point[0] - xs[0];
            double range = xs[1] - xs[0];
            if(diff<0){
                continue;
            }
            double percent = diff / range;
            double xOffset = contentW * percent;
            // 计算Y轴
            diff = point[1] - ys[0];
            range = ys[1] - ys[0];
            if(diff<0){
                continue;
            }
            percent = diff / range;
            double yOffset = contentH * percent;
            Position position = new Position();
            positions.add(position);
            position.x = o.x+xOffset;
            position.y = o.y - yOffset;
        }
        return positions;
    }



    private Canvas createCanvas2(double width,double height){
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.GREEN);
        gc.setLineDashes(3);
        gc.strokeLine(50, 10, 50, 100);
        return canvas;
    }

    private void draggable(Node node) {
        final Position pos = new Position();

        // 提示用户该结点可点击
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> node.setCursor(Cursor.HAND));
        node.addEventHandler(MouseEvent.MOUSE_EXITED,event -> node.setCursor(Cursor.DEFAULT));

        // 提示用户该结点可拖拽
        node.addEventHandler(MouseEvent.MOUSE_PRESSED,event -> {
            node.setCursor(Cursor.MOVE);

            // 当按压事件发生时，缓存事件发生的位置坐标
            pos.x = event.getX();
            pos.y = event.getY();
        });

        // 实现拖拽功能
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
                double distanceX = event.getX() - pos.x;
                double distanceY = event.getY() - pos.y;

                double x = node.getLayoutX() + distanceX;
                double y = node.getLayoutY() + distanceY;

                // 计算出 x、y 后将结点重定位到指定坐标点 (x,y)
                node.relocate(x,y);

        });

    }

    private static class Position {
        double x;
        double y;
    }
}
