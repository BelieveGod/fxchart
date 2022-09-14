package com.example.reflection;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author LTJ
 * @date 2022/9/5
 */
public class Demo {
    public static void main(String[] args) {
        Class<User> userClass = User.class;

        User user = new User();
        user.setAge(11);
        user.setName("小明");
        System.out.println("-------TOMAP----------");
        Map<String, Object> map = BeanUtil.beanToMap(user);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
        }

        System.out.println("--------TOBEAN---------");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", "大傻");
        hashMap.put("age", 21);
        hashMap.put("city", "福建");
        hashMap.put("nation", "中国");
        User user1 = BeanUtil.toBean(hashMap, userClass);
        System.out.println("user1.toString() = " + user1.toString());


        System.out.println("-------TOJSON----------");
        String userStr = JSONObject.toJSONString(user);
        System.out.println("userStr = " + userStr);
        System.out.println("-------fromJSON----------");
        String hashMapStr = JSONObject.toJSONString(hashMap);
        System.out.println("hashMapStr = " + hashMapStr);
        User user2 = JSONObject.parseObject(hashMapStr, User.class);
        System.out.println("user2 = " + user2);

    }
}
