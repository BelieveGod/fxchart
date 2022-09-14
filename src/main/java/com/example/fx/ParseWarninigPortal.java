package com.example.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author LTJ
 * @date 2022/9/14
 */
public class ParseWarninigPortal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainForm mainForm = new MainForm();

        Scene scene = new Scene(mainForm, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("限界门转换工具");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
