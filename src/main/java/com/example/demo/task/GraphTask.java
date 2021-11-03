package com.example.demo.task;

import com.example.canvas.window.ChartWin;
import javafx.application.Application;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author LTJ
 * @date 2021/11/3
 */
@Component
public class GraphTask implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Application.launch(ChartWin.class);
    }
}
