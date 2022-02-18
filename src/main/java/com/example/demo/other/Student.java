package com.example.demo.other;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LTJ
 * @date 2021/12/6
 */
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = 2L;
    private int id;
    private boolean flag;
    public Student(int id,boolean flag) {
        this.id = id;
        this.flag = flag;
    }
}
