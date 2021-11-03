package com.example.canvas.window.component;


import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


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



}
