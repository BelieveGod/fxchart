package com.example.demo.activemq;

import lombok.Data;

/**
 * @author LTJ
 * @date 2022/3/14
 */
@Data
public class Robot {
    private Integer id;
    /**
     * 机器人名称
     */
    private String botName;
    /**
     * 机器人编码
     */
    private Integer botCode;
    /**
     * 机器人型号
     */
    private String botModel;
    /**
     * 机器人ip
     */
    private String botIp;
    /**
     * 机器人优先级
     */
    private String botPriority;
    /**
     * 机器人状态
     */
    private Integer botStatus;
}
