package com.example.demo.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * @author LTJ
 * @date 2022/2/18
 */
//@Component
@Slf4j
public class MockSendTask {

    @Autowired
    private RestTemplate restTemplate;

    private static SecureRandom random = new SecureRandom();

    @Scheduled(fixedDelay = 7000)
    public void send(){
        log.info("测试:{}",restTemplate.toString());
        CompletableFuture.supplyAsync(this::sendDeviceData)
                .whenComplete((aBoolean, throwable) -> log.info("告警数据上传完成!"));
        UUID uuid = UUID.fastUUID();
//        boolean b = sendCheckData(uuid);
//        if(b){
//            sendAlarmData(uuid);
//        }

    }

    private boolean sendCheckData( UUID uuid ){
        JSONObject root = new JSONObject(true);
        root.set("provider", "QIHUI");
        root.set("subSysCode", "360");
        root.set("tid", uuid.toString());
        root.set("trainNo", "G5001");
        root.set("stationId", 3);
        root.set("direction", 1);
        String traceTime = DateUtil.format(new Date(), NORM_DATETIME_PATTERN);
        root.set("traceTime", traceTime);
        root.set("lowSpeed", 20.3);
        root.set("hightSpeed", 23.6);
        JSONArray wheels = new JSONArray();
        root.set("wheels", wheels);
        JSONArray carriages = new JSONArray();
        root.set("carriages", carriages);

        // 填充wheels
        for(int i=0;i<24;i++){
            JSONObject partStruct = new JSONObject(true);
            wheels.add(partStruct);
            partStruct.set("partId",(i+1)*10+1);
            JSONArray ckdatas = new JSONArray();
            partStruct.set("checkDatas", ckdatas);
            JSONObject ckData = createCkData();
            ckdatas.add(ckData);
            //==================
            JSONObject partStruct2 = new JSONObject(true);
            wheels.add(partStruct2);
            partStruct2.set("partId",(i+1)*10+2);
            JSONArray ckdatas2 = new JSONArray();
            partStruct2.set("checkDatas", ckdatas2);
            JSONObject ckData2 = createCkData();
            ckdatas2.add(ckData2);
        }
        // 填充carriages
        for(int i=0;i<6;i++){
            JSONObject partStruct = new JSONObject(true);
            carriages.add(partStruct);
            partStruct.set("partId",i+1);
            JSONArray ckdatas = new JSONArray();
            partStruct.set("checkDatas", ckdatas);
            JSONObject ckData = create360data();
            ckdatas.add(ckData);
        }
        log.info("root:{}",root);
        Instant start = Instant.now();
        JSONObject rsp = restTemplate.postForObject("http://127.0.0.1:8080/upload/checkData", root, JSONObject.class);
        Instant end = Instant.now();
        long millis = Duration.between(start, end).toMillis();
        log.info("请求耗时：{}ms", millis);
        log.info("rsp:{}",rsp);
        return true;
    }

    private JSONObject createCkData(){
        JSONObject ckData = new JSONObject(true);
        UUID uuid = UUID.fastUUID();
        ckData.set("checkdataId", uuid.toString());
        ckData.set("ckItem", "302_68");
        ckData.set("value", 23.3);
        JSONArray imgs = new JSONArray();
        ckData.set("imgs", imgs);
        JSONObject img = new JSONObject(true);
        imgs.add(img);
        img.set("url", "http://172.10.128.10/a.jpg");
        return ckData;
    }

    private JSONObject create360data(){
        JSONObject ckData = new JSONObject(true);
        UUID uuid = UUID.fastUUID();
        ckData.set("checkdataId", uuid.toString());
        ckData.set("ckItem", "360_1");
        ckData.set("value", 1.0);
        JSONArray imgs = new JSONArray();
        ckData.set("imgs", Collections.emptyList());
        return ckData;
    }

    private boolean sendAlarmData( UUID uuid ){
        JSONObject root = new JSONObject(true);
        root.set("provider", "QIHUI");
        root.set("subSysCode", "360");
        root.set("tid", uuid.toString());
        JSONArray wheels = new JSONArray();
        root.set("wheels", wheels);
        JSONArray carriages = new JSONArray();
        root.set("carriages", carriages);
        // 填充wheels
        for(int i=0;i<24;i++){
            JSONObject partStruct = new JSONObject(true);
            wheels.add(partStruct);
            partStruct.set("partId",(i+1)*10+1);
            JSONArray ckdatas = new JSONArray();
            partStruct.set("alarmDatas", ckdatas);
            JSONObject ckData = createAlarmData();
            ckdatas.add(ckData);
            //==================
            JSONObject partStruct2 = new JSONObject(true);
            wheels.add(partStruct2);
            partStruct2.set("partId",(i+1)*10+2);
            JSONArray ckdatas2 = new JSONArray();
            partStruct2.set("alarmDatas", ckdatas2);
            JSONObject ckData2 = createAlarmData();
            ckdatas2.add(ckData2);
        }

        // 填充carriages
        for(int i=0;i<6;i++){
            JSONObject partStruct = new JSONObject(true);
            carriages.add(partStruct);
            partStruct.set("partId",i+1);
            JSONArray ckdatas = new JSONArray();
            partStruct.set("alarmDatas", ckdatas);
            JSONObject ckData = create360Alarmdata();
            ckdatas.add(ckData);
        }

        log.info("root:{}",root);
        Instant start = Instant.now();
        JSONObject rsp = restTemplate.postForObject("http://127.0.0.1:8080/upload/alarmData", root, JSONObject.class);
        Instant end = Instant.now();
        long millis = Duration.between(start, end).toMillis();
        log.info("请求耗时：{}ms", millis);
        log.info("rsp:{}",rsp);
        return true;
    }

    private JSONObject create360Alarmdata() {
        JSONObject ckData = new JSONObject(true);
        UUID uuid = UUID.fastUUID();
        ckData.set("alarmId", uuid.toString());
        ckData.set("ckItem", "360_1");
        ckData.set("value", 1);
        JSONArray imgs = new JSONArray();
        ckData.set("imgs", Collections.emptyList());

        ckData.set("alarmLevel", 1);
        ckData.set("alarmStatus", 0);
        ckData.set("confirmMsg", "");
        return ckData;
    }

    private JSONObject createAlarmData(){
        JSONObject ckData = new JSONObject(true);
        UUID uuid = UUID.fastUUID();
        ckData.set("alarmId", uuid.toString());
        ckData.set("ckItem", "302_68");
        ckData.set("value", 23.3);
        JSONArray imgs = new JSONArray();
        ckData.set("imgs", imgs);
        JSONObject img = new JSONObject(true);
        imgs.add(img);
        img.set("url", "http://172.10.128.10/a.jpg");
        ckData.set("alarmLevel", 1);
        ckData.set("alarmStatus", 0);
        ckData.set("confirmMsg", "");
        return ckData;
    }

    private boolean sendDeviceData(){
        JSONArray root = new JSONArray();
        JSONObject deviceData = new JSONObject(true);
        root.add(deviceData);
        deviceData.set("provider", "QIHUI");
        deviceData.set("subSysCode", "360");
        deviceData.set("stationId", 3);
//        deviceData.set("objType", 25);
        deviceData.set("objTypeName", "QIHUI");
        deviceData.set("objCode",1);
        deviceData.set("objName", "LD");
        deviceData.set("pointItemCode", 27);
        deviceData.set("pointItemName","LD状态");
        deviceData.set("status", 1);
        String updateTime = DateUtil.format(new Date(), NORM_DATETIME_PATTERN);
        deviceData.set("updateTime",updateTime );
        deviceData.set("objDesc", "探伤异常");

        log.info("root:{}",root);
        Instant start = Instant.now();
        JSONObject rsp = restTemplate.postForObject("http://127.0.0.1:8080/upload/deviceData", root, JSONObject.class);
        Instant end = Instant.now();
        long millis = Duration.between(start, end).toMillis();
        log.info("请求耗时：{}ms", millis);
        log.info("rsp:{}",rsp);
        return true;
    }
}
