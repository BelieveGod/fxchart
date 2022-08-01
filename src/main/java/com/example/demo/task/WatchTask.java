package com.example.demo.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.CompletableFuture;

/**
 * 监控文件的
 * @author LTJ
 * @date 2022/6/17
 */
@Component
@Slf4j
public class WatchTask implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        File dir = FileUtil.file("D:\\app\\web\\webData\\alarm360sync");

        WatchMonitor watchMonitor = WatchMonitor.create(dir, WatchMonitor.ENTRY_CREATE,WatchMonitor.ENTRY_MODIFY);
        watchMonitor.setWatcher(new DelayWatcher(new SimpleWatcher() {
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                try {
                    Object obj = event.context();
                    if(true){
                        throw new RuntimeException("");
                    }
                    log.info("创建：{}-> {}", currentPath, obj);
                } catch (RuntimeException e) {
                    log.error("线程异常，稍后重新监听");
                }
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                log.info("修改：{}-> {}", currentPath, obj);
            }
        },500));
        CompletableFuture.runAsync(() -> {
            int i=0;
            while(true){
                try {
                    log.info("监听:{}",++i);
                    watchMonitor.watch();
                } catch (Exception e) {
                    log.error("线程异常，稍后重新监听");
                }
            }
        }).exceptionally(t -> {
            log.error("", t);
            return null;
        });
        log.info("开始监听目录：{}", dir.getAbsolutePath());
    }
}
