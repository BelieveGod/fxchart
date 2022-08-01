package com.example.demo.activemq;

import lombok.Data;

import java.util.Date;

/**
 * 用于与C++ 对接的 通用帧结构
 * @author LTJ
 * @date 2022/3/15
 */
@Data
public class RdpsFrame<T> {
    private String msgType;

    private Integer seq;

    private String publisher;

    private Date publishTime;

    private T data;
}
