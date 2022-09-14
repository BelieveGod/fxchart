package com.example.reflection;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LTJ
 * @date 2022/9/5
 */
public class Demo2 {
    public static void main(String[] args) throws JsonProcessingException {
        Class<User> userClass = User.class;
        Field[] declaredFields = userClass.getDeclaredFields();
        for(int i=0;i<declaredFields.length;i++){
            System.out.println(i + " filed:" + declaredFields[i].getName());
        }

        Method[] declaredMethods = userClass.getDeclaredMethods();
        for(int i=0;i<declaredMethods.length;i++){
            System.out.println(i+" method:"+declaredMethods[i].getName());
        }

        System.out.println("--------TOMAP---------");
        User user = new User();
        user.setAge(11);
        user.setName("小明");
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        BeanUtils.copyProperties(user,linkedHashMap);
        for (Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {
            System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
        }

        System.out.println("-------TOBEAN----------");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", "大傻");
        hashMap.put("age", 21);
        hashMap.put("city", "福建");
        hashMap.put("nation", "中国");
        User user1 = new User();
        BeanUtils.copyProperties(hashMap,user1);
        System.out.println("user1.getName() = " + user1.getName());
        System.out.println("user1.getAge() = " + user1.getAge());
        System.out.println("user1.getCity() = " + user1.getCity());


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        System.out.println("-------TOJSON----------");
        String userStr = objectMapper.writeValueAsString(user);
        System.out.println("userStr = " + userStr);
        System.out.println("-------fromJSON----------");
        String hashMapStr = JSONObject.toJSONString(hashMap);
        System.out.println("hashMapStr = " + hashMapStr);
        User user2 = objectMapper.readValue(hashMapStr, User.class);
        System.out.println("user2 = " + user2);
    }
}
