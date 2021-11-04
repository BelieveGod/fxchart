package com.example.canvas.window.component;


import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DataPoint extends Canvas {
    private double contentW;
    private double contentH;
    @Getter
    @Setter
    private ObservableList<Position> positions;
    private double padding;
    private double radius;

    public DataPoint() {
    }

    public DataPoint(double width, double height) {
        super(width, height);
    }

    public DataPoint(double contentW, double contentH, ObservableList<Position> positions, double padding,double radius) {
        super(contentW + 2 * padding, contentH + 2 * padding);
        this.contentW = contentW;
        this.contentH = contentH;
        this.positions = positions;
        this.padding = padding;
        this.radius = radius;
        rePaint();
        positions.addListener(new ListChangeListener<Position>() {
            @Override
            public void onChanged(Change<? extends Position> c) {
                rePaint();
            }
        });
        initListen();

    }

    public void rePaint(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.GREEN);
        for (Position position : positions) {
            gc.fillOval(position.x-radius,position.y-radius,radius*2,radius*2);
        }
        for(int i=0;i<positions.size()-1;i++){
            gc.strokeLine(positions.get(i).x, positions.get(i).y, positions.get(i + 1).x, positions.get(i+1).y);
        }
    }


    public void initListen(){
        Position clickPoint = new Position();
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("hitIndex", -1);

        this.setOnMouseEntered(event -> {
            log.info("数据点画布被进入");
            this.setCursor(Cursor.HAND);
        });
        this.setOnMouseExited(event -> {
            log.info("数据点画布被离开");
            this.setCursor(Cursor.DEFAULT);
        });
        this.setOnMousePressed(event -> {
            log.info("数据点画布被按下");
            // 记下点击位置
            clickPoint.x = event.getX();
            clickPoint.y = event.getY();
            for(int i=positions.size()-1;i>=0;i--){
                if(intersect(clickPoint, positions.get(i), radius)){
                    log.info("clickPoint.x:{},clickPoint.y:{},p.x:{},p.y:{}",clickPoint.x,clickPoint.y,positions.get(i).x,positions.get(i).y);
                    ctx.put("hitIndex", i);
                    Position tempP = new Position();
                    tempP.x = positions.get(i).x;
                    tempP.y = positions.get(i).y;
                    ctx.put("tempP", tempP);
                    this.setCursor(Cursor.MOVE);
                    break;
                }
            }
        });

        this.setOnMouseDragged(event -> {
            log.info("数据点画布被拖动");
            Integer hitIndex = (Integer) ctx.get("hitIndex");
            if(hitIndex>=0){
//                log.info("clickPoint.x:{},clickPoint.y:{},event.x:{},event.y:{}",clickPoint.x,clickPoint.y,event.getX(),event.getY());
                double distanceX = event.getX() - clickPoint.x;
                double distanceY = event.getY() - clickPoint.y;
//                log.info("p.x:{},p.y:{},distanceX:{},distanceY:{}",clickPoint.x,clickPoint.y,distanceX,distanceY);
                Position tempP = (Position) ctx.get("tempP");
                positions.get(hitIndex).x = tempP.x + distanceX;
                positions.get(hitIndex).y = tempP.y + distanceY;

                rePaint();
            }
        });

        this.setOnMouseReleased(event -> {
            log.info("数据点画布鼠标释放");
            ctx.put("hitIndex", -1);
            this.setCursor(Cursor.HAND);
        });
    }

    private boolean intersect(Position p,Position center,double radius){

       if((p.x-center.x)*(p.x-center.x)+ (p.y-center.y)*(p.y-center.y) > radius*radius){
           return false;
       }else{
           return true;
       }
    }


}
