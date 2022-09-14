package com.example.reflection;

import lombok.Data;
import lombok.ToString;

/**
 * @author LTJ
 * @date 2022/9/5
 */
@ToString
public class User {
    private String name;
    private Integer age;

    public String getCity(){
        System.out.println("getCity 被调用了");
        return "广东";
    }

    public void setNation(){
        System.out.println("setNation 被调用了");
    }

    public String getName() {
        System.out.println("getName 被调用了");
        return name;
    }

    public void setName(String name) {
        System.out.println("setName 被调用了");
        this.name = "prefix"+name;
    }

    public Integer getAge() {
        System.out.println("getAge 被调用了");
        return age;
    }

    public void setAge(Integer age) {
        System.out.println("setAge 被调用了");
        this.age = age;
    }
}
