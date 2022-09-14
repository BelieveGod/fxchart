package com.example.reflection;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LTJ
 * @date 2022/9/5
 */
public class Demo3 {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Class<User> userClass = User.class;
        Field[] declaredFields = userClass.getDeclaredFields();
        for(int i=0;i<declaredFields.length;i++){
            System.out.println(i + " filed:" + declaredFields[i].getName());
        }

        Method[] declaredMethods = userClass.getDeclaredMethods();
        for(int i=0;i<declaredMethods.length;i++){
            System.out.println(i+" method:"+declaredMethods[i].getName());
        }

        System.out.println("---------- get/set 或者直接 field");
        Field name = userClass.getDeclaredField("name");
        User userRflect = new User();
        name.setAccessible(true);
        name.set(userRflect, "反射设置");
        Method setAge = userClass.getMethod("setAge", Integer.class);
        setAge.invoke(userRflect, 13);
        System.out.println("userRflect = " + userRflect);


        System.out.println("-------TOMAP----------");
        User user = new User();
        user.setAge(11);
        user.setName("小明");
        Map map = BeanUtils.describe(user);
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
        }

        System.out.println("--------TOBEAN---------");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", "大傻");
        hashMap.put("age", 21);
        hashMap.put("city", "福建");
        hashMap.put("nation", "中国");
        User user1 = new User();
        BeanUtils.populate(user1,hashMap);

        System.out.println("user1.getName() = " + user1.getName());
        System.out.println("user1.getAge() = " + user1.getAge());
        System.out.println("user1.getCity() = " + user1.getCity());
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
