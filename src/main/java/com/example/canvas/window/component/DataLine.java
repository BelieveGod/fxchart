package com.example.canvas.window.component;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import lombok.Getter;
import lombok.Setter;


public class DataLine extends Canvas {
    private double contentW;
    private double contentH;
    @Getter
    @Setter
    private ObservableList<Position> positions;
    private double padding;

    public DataLine() {
    }

    public DataLine(double width, double height) {
        super(width, height);
    }

    public DataLine(double contentW, double contentH, ObservableList<Position> positions, double padding) {
        super(contentW + 2 * padding, contentH + 2 * padding);
        this.contentW = contentW;
        this.contentH = contentH;
        this.positions = positions;
        this.padding = padding;
        rePaint();
        positions.addListener(new ListChangeListener<Position>() {
            @Override
            public void onChanged(Change<? extends Position> c) {
                positions.removeListener(this);
                positions.sort((o1, o2) -> Double.compare(o1.x, o2.x));
                positions.addListener(this);
                rePaint();
            }
        });
    }

    public void rePaint(){
        GraphicsContext gc =getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.BLUE);
        gc.setLineDashes(5);
        gc.setLineWidth(2);
        gc.setLineCap(StrokeLineCap.BUTT);
        for(int i=0;i<positions.size()-1;i++){
            gc.strokeLine(positions.get(i).x, positions.get(i).y, positions.get(i + 1).x, positions.get(i+1).y);
        }
    }
}
