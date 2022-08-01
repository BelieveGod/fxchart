package com.example.demo.activemq;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author LTJ
 * @date 2022/3/23
 */
@Component
@Slf4j
@ConditionalOnProperty(name ="nannar.enable-mock-robot" )
public class MockRdps implements CommandLineRunner {

    private static Random random = ThreadLocalRandom.current();

    private static int seqNum=0;

    private int frameNum=1;
    private int frameMax=8;

    private DelayQueue<DelayInteger> delayQueue = new DelayQueue<>();

    public static class DelayInteger implements Delayed {
        @Getter
        private Integer num;
        private long mills;

        public DelayInteger(Integer num, long mills) {
            this.num = num;
            this.mills = mills;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return mills-System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(getDelay(null), o.getDelay(null));
        }
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(containerFactory = "jmsTopicListenerContainerFactory",destination = "/web/cmd")
    public void onCmd(String msg) throws InterruptedException {
        JSONObject RdpsFrame = JSONObject.parseObject(msg);
        Integer seq = RdpsFrame.getInteger("seq");
        JSONObject data = RdpsFrame.getJSONObject("data");
        String cmd = data.getString("cmd");
        // 模拟操作时间
        log.info("接收到cmd:{},seq:{}", cmd, seq);
//        Thread.sleep(mockSleep());
        RdpsBoolAck.Result result= new RdpsBoolAck.Result();
        result.setSuccess(true);
        RdpsBoolAck rdpsBoolAck = new RdpsBoolAck();
        rdpsBoolAck.setCmd(cmd);
        rdpsBoolAck.setResult(result);
        RdpsFrame<RdpsBoolAck> frame = new RdpsFrame<>();
        frame.setPublisher("rdps");
        frame.setMsgType("rsp");
        frame.setPublishTime(new Date());
        frame.setSeq(seq);
        frame.setData(rdpsBoolAck);

        ActiveMQTopic activeMQTopic = new ActiveMQTopic("/rdps/ack");
        String content = JSONObject.toJSONString(frame);
        log.info("回馈cmd:{},seq:{}", cmd, seq);
        jmsTemplate.convertAndSend(activeMQTopic,content);

    }


    /**
     * 模拟发送实时状态信息
     */
    @Scheduled(initialDelay = 1000,fixedDelay = 5000)
    public void sendRealData() throws IOException {
        try {
            ClassPathResource json = new ClassPathResource("/static/rdps/frame" + String.valueOf(frameNum) + ".json");
            if (json.exists()) {
                InputStream inputStream = json.getInputStream();
                String content = IoUtil.readUtf8(inputStream);
                inputStream.close();
                ActiveMQTopic activeMQTopic = new ActiveMQTopic("/rdps/realTime/data");
                log.info("文件：{}", json.getPath());
                jmsTemplate.convertAndSend(activeMQTopic, content);
                DelayInteger delayInteger = new DelayInteger(frameNum, System.currentTimeMillis() + 1500);
                delayQueue.offer(delayInteger);
            }
            if (frameNum < frameMax) {
                frameNum++;
            }else{
                frameNum=1;
            }
        } catch (IORuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JmsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture.runAsync(() -> {

            try {
                while (!Thread.interrupted()){
                    DelayInteger delayInteger = delayQueue.take();
                    Integer num = delayInteger.getNum();
                    ClassPathResource json = new ClassPathResource("/static/rdps/donePoint/doneFrame"+String.valueOf(num)+".json");
                    if(json.exists()){
                        InputStream inputStream = json.getInputStream();
                        String content = IoUtil.readUtf8(inputStream);
                        inputStream.close();
                        ActiveMQTopic activeMQTopic = new ActiveMQTopic("/rdps/realTime/resultDone");
                        log.info("文件：{}", json.getPath());
                        jmsTemplate.convertAndSend(activeMQTopic,content);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IORuntimeException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JmsException e) {
                e.printStackTrace();
            }
        });

    }
}
