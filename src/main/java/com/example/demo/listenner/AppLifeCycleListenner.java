package com.example.demo.listenner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @author LTJ
 * @date 2022/4/6
 */
@Slf4j
public class AppLifeCycleListenner implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ApplicationEnvironmentPreparedEvent){
            log.info("初始化环境变量");
        }else if(applicationEvent instanceof ApplicationEnvironmentPreparedEvent){
            log.info("初始化环境变量完成");
        }else if(applicationEvent instanceof ContextStartedEvent){
            log.info("应用启动");
        }else if(applicationEvent instanceof ContextRefreshedEvent){
            log.info("应用刷新");
        }else if(applicationEvent instanceof ApplicationReadyEvent){
            log.info("应用已启动完成");
        }else if(applicationEvent instanceof ContextStoppedEvent){
            log.info("应用停止");
        }else if(applicationEvent instanceof ContextClosedEvent){
            log.info("应用关闭");
        }
    }
}
