package com.example.demo.activemq;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @author LTJ
 * @date 2022/7/19
 */
@Slf4j
@Component
public class ActiveMqListenner {
    /**
     * JMS 的高级抽象层次的客户端对象
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    @PostConstruct
    private void init(){
        log.info("activeMQ 监听器已启动...");
    }

    /**
     * 非持久型的订阅者
     * @param msg
     * @throws InterruptedException
     */
    @JmsListener(containerFactory = "jmsTopicListenerContainerFactory",destination = "topic.foo")
    public void onCmd(String msg) throws InterruptedException {
        log.info("接收到消息：{}", msg);

    }

    /**
     * 持久型的订阅者
     * @param msg
     * @throws InterruptedException
     */
    @JmsListener(containerFactory = "jmsDurableTopicListenerContainerFactory",destination = "topic.foo", subscription = "demoTopicConsumer")
    public void onCmd2(String msg) throws InterruptedException {
        log.info("接收到消息：{}", msg);

    }
}
