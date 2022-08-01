package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LTJ
 * @date 2022/7/14
 */
@Slf4j
@RestController
public class ActionController {
    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    @RequestMapping("/testKafka")
    public Map<String, Object> testKafka(String idx, String status, String dataSourceCode, String dataSourceName,
                                         String updatorname) {
        Map<String, Object> data = new HashMap<>();
        data.put("idx", idx);
        data.put("status", status);
        data.put("dataSourceCode", dataSourceCode);
        data.put("dataSourceName", dataSourceName);
        data.put("updatorname", updatorname);
        data.put("updatetime", new Date());
        String s = JSONObject.toJSONString(data, SerializerFeature.WriteDateUseDateFormat);
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("returnWarnData", s);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("{}-生产者发送消息失败：{}", "returnWarnData", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("{}-生产者发送消息成功：{}", "quickstart-events", result.toString());
            }
        });
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }
}