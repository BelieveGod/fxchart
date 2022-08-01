package com.example.demo.controller;

import com.example.demo.other.Animal;
import com.example.demo.other.Bird;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author LTJ
 * @date 2021/8/16
 */
@RestController
@RequestMapping("/mock")
@Slf4j
public class MockUploadWebController {
    public static SecureRandom random=new SecureRandom();

    @RequestMapping("/str/**")
    public String mock9608(HttpServletRequest httpServletRequest){
        printRequestInfo(httpServletRequest);

        return "success";
    }

    @RequestMapping("/obj")
    public Map<String,Object> mockdd(HttpServletRequest httpServletRequest){
        printRequestInfo(httpServletRequest);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", "lining");
        map.put("id", 1);
        map.put("date", new Date());

        return map;
    }

    private void printRequestInfo(HttpServletRequest httpServletRequest){
        String requestURL = httpServletRequest.getRequestURL().toString();
        String requestURI = httpServletRequest.getRequestURI();
        String contextPath = httpServletRequest.getContextPath();
        String servletPath = httpServletRequest.getServletPath();
        String pathInfo = httpServletRequest.getPathInfo();
        log.info("requestURL:{}", requestURL);
        log.info("requestURI:{}", requestURI);
        log.info("contextPath:{}", contextPath);
        log.info("servletPath:{}", servletPath);
        log.info("pathInfo:{}", pathInfo);
    }

    @RequestMapping("/yunda")
    public Map<String,Object> yunda(){
        Map<String, Object> result = new HashMap<>();
        result.put("message", "ok");
        result.put("code","00");
        return result;
    }

    @RequestMapping("/mfrs/login")
    public Map<String,Object> mfrsLogin(){
        Map<String, Object> rsp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        rsp.put("data", data);
        data.put("token","3");
        return rsp;
    }

    @RequestMapping("/mfrs/info")
    public Map<String,Object> mfrsInfo(){
        Map<String, Object> rsp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        rsp.put("data", data);
        rsp.put("status", "200");
        int i = random.nextInt(2) + 1;
        data.put("manufacturerName","随机厂家"+i);
        data.put("manufacturerId",String.valueOf(i));
        return rsp;
    }

    @RequestMapping("/testExtendJson")
    public Animal testExtendJson(){
        Bird bird = new Bird();
        bird.setName("小鸟");
        bird.setAge(10);
        Animal animal = bird;
        return animal;
    }
}
