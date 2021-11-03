package com.example.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LTJ
 * @date 2021/8/23
 */
@RestController
@Slf4j
public class HanglinController {
    @PostMapping("/api/trainLog/checkOutData")
    public Map<String,Object> reveive1(@RequestBody Map<String,Object> body){
        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info(s);
        Map<String, Object> result = new HashMap<>();
        result.put("message", "ok");
        result.put("code",0);
        return result;
    }

    @PostMapping("/api/trainLog/alarmData")
    public Map<String,Object> reveive2(@RequestBody Map<String,Object> body){
        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info(s);
        Map<String, Object> result = new HashMap<>();
        result.put("message", "ok");
        result.put("code",0);
        return result;
    }

    @PostMapping("/api/trainLog/selfAlarmData")
    public Map<String,Object> reveive3(@RequestBody Map<String,Object> body){
        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info(s);
        Map<String, Object> result = new HashMap<>();
        result.put("message", "ok");
        result.put("code",0);
        return result;
    }
}
