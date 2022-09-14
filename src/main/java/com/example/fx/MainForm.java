package com.example.fx;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


/**
 * @author LTJ
 * @date 2022/9/14
 */
public class MainForm extends StackPane {
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private static FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private static  File homeDirectory = fileSystemView.getHomeDirectory();

    private TextField textField = new TextField();
    private TextField textField2 = new TextField();

    private StringProperty dir = new SimpleStringProperty();
    private StringProperty outPutDir = new SimpleStringProperty();

    private ProgressBar progressBar = new ProgressBar(0);
    private ProgressIndicator progressIndicator = new ProgressIndicator();
    private VBox maskPane = new VBox();

    public MainForm() {
        init();
    }

    private void init(){
        VBox top = new VBox(10);


        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(5, 20, 5, 20));
        Label label = new Label("要转换的文件夹");
        Button button = new Button("选择文件夹");
        button.setOnAction(this::btnaction);
        dir.bindBidirectional(textField.textProperty());
        hBox.getChildren().addAll(label, textField, button);


        HBox hBox2 = new HBox(15);
        hBox2.setPadding(new Insets(5, 20, 5, 20));
        Label label2 = new Label("要输出的文件夹");
        Button button2 = new Button("选择文件夹");
        outPutDir.bindBidirectional(textField2.textProperty());
        hBox2.getChildren().addAll(label2, textField2, button2);
        button2.setOnAction(this::btn2action);
        top.getChildren().addAll(hBox, hBox2);

        HBox hBox3 = new HBox(15);
        hBox3.setPadding(new Insets(5, 20, 5, 20));
        Button convertBtn = new Button("转换");
        convertBtn.setOnAction(this::btn3Action);
        hBox3.setAlignment(Pos.CENTER);
        hBox3.getChildren().add(convertBtn);
        top.getChildren().add(hBox3);


        VBox bottom = new VBox();
        HBox hBox4 = new HBox(15);
        hBox4.setAlignment(Pos.CENTER);
        hBox4.setFillHeight(true);
        hBox4.getChildren().add(progressBar);
        progressBar.setPrefWidth(400);
        hBox4.widthProperty().addListener((observable, oldValue, newValue) -> {
            progressBar.setPrefWidth(Convert.toDouble(newValue));

        });
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        bottom.getChildren().add(hBox4);
        bottom.setFillWidth(true);
        bottom.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));

        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(top, bottom);
        VBox.setVgrow(top, Priority.ALWAYS);
        maskPane.setBackground(new Background(new BackgroundFill(new Color(0.1, 0.1, 0.1, 0.5), null, null), null, null));
        progressIndicator.setPrefSize(50,50);
        progressIndicator.setMaxSize(50,50);
        maskPane.setAlignment(Pos.CENTER);
        maskPane.getChildren().add(progressIndicator);

        getChildren().add(vBox2);
    }

    private void btnaction(ActionEvent event){
        directoryChooser.setTitle("要转换的文件夹");
        directoryChooser.setInitialDirectory(homeDirectory);
        File file = directoryChooser.showDialog(getScene().getWindow());
        if(file!=null){
            dir.setValue(file.getAbsolutePath());
        }
    }

    private void btn2action(ActionEvent event){
        directoryChooser.setTitle("要输出的文件夹");
        directoryChooser.setInitialDirectory(homeDirectory);
        File file = directoryChooser.showDialog(getScene().getWindow());
        if(file!=null){
            outPutDir.setValue(file.getAbsolutePath());
        }
    }

    private void btn3Action(ActionEvent event){
        if(StrUtil.isBlank(dir.getValue()) || StrUtil.isBlank(outPutDir.getValue())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择好文件夹");
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        }
        Task task1 = new Task(){
            @Override
            protected Object call() throws Exception {
                TimeInterval timer = DateUtil.timer();
                ParseService.convert(dir.getValue(),outPutDir.getValue(),(percent)->{
                    updateProgress(percent, 1);
                });
                long l = timer.intervalMs();
                System.out.println("执行耗时：" + l + "ms");
                return null;
            }
        };
        task1.progressProperty().addListener((observable, oldValue, newValue) -> {
            Double aDouble = Convert.toDouble(newValue);
            System.out.println("percent = " + aDouble);
            progressBar.setProgress(aDouble);
        });


        task1.setOnScheduled(event1 -> {
            System.out.println("setOnScheduled = " + event1.toString());
        });

        task1.setOnRunning(event1 -> {
            System.out.println("setOnRunning = " + event1.toString());
        });

        task1.setOnCancelled(event1 -> {
            System.out.println("setOnCancelled = " + event1.toString());
        });

        task1.setOnSucceeded(event1 -> {
            System.out.println("setOnSucceeded = " + event1.toString());
            getChildren().remove(maskPane);
        });
        task1.setOnFailed(event1 -> {
            System.out.println("setOnFailed = " + event1.toString());
            Throwable exception = task1.getException();
            System.out.println("exception.getMessage() = " + exception.getMessage());
            exception.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            Optional<ButtonType> opt = alert.showAndWait();
            ButtonType buttonType = opt.orElse(null);
            if(buttonType!=null){
                ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                String typeCode = buttonData.getTypeCode();
                System.out.println("typeCode = " + typeCode);
            }
            getChildren().remove(maskPane);
        });

        task1.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(StrUtil.format("observable:{},oldValue:{},newValue:{}", observable, oldValue, newValue));
        });



        getChildren().add(maskPane);
        CompletableFuture.runAsync(task1);
    }


}
