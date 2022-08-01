package com.example.demo.activemq;

import lombok.Data;

import java.util.List;

/**
 * 用于对接C++ 实时推送消息的 接收领域模型
 * @author LTJ
 * @date 2022/3/15
 */
@Data
public class RealTimeDataDTO {
    /**
     * 机器人的信息
     */
    private List<RobotInfo> robotInfos;
    /**
     * 升降台的信息
     */
    private List<LiftInfo> liftInfos;
    /**
     * 充电桩的信息
     */
    private List<ChargingPileInfo> chargingPileInfos;


    @Data
    public static class RobotInfo{
        /**
         * 机器人编码
         */
        private Integer id;
        /**
         * 机器人工作状态
         * RobotStatusEnum
         */
        private Integer status;
        /**
         * 电量百分比
         */
        private Integer batteryPercent;
        /**
         * 电量剩余时间
         */
        private Integer batteryRemainTime;
        /**
         * 是否充电中
         */
        private Boolean charging;
        /**
         * 当前任务
         */
        private Task task;
        /**
         * 任务队列
         */
        private List<BriefTask> taskQueue;
        /**
         * 设备健康信息
         */
        private List<HealthInfo> health;
    }

    /**
     * 升降台信息
     */
    @Data
    public static class LiftInfo{
        /**
         * 升降台id
         */
        private Integer id;
        /**
         * 升降台名字
         */
        private String name;
        /**
         * 升降台状态
         * healthStatusEnum
         */
        private Integer status;
    }

    /**
     * 充电桩信息
     */
    @Data
    public static class ChargingPileInfo{
        /**
         * 充电桩Id
         */
        private Integer id;
        /**
         * 充电桩名称
         */
        private String name;
        /**
         * 充电桩状态
         * healthStatusEnum
         */
        private Integer status;
    }

    /**
     * 当前任务信息
     */
    @Data
    public static class Task{
        /**
         * 任务id
         */
        private Long id;
        /**
         * 股道名称
         */
//        private String laneName;
        /**
         * 股道编码
         */
        private Integer laneId;
        /**
         * 任务命令来源 |  string | 取值：rdps 或者 web
         */
        private String cmdSrc;

        /**
         * 当前检测的列车号
         */
        private String trainNo;
        /**
         * 当前任务的位置
         */
        private Position position;
        /**
         * 当前任务百分比
         */
        private Integer percent;
        /**
         * 任务累计执行时间
         */
        private Integer accumulatedTime;

        /**
         * 任务状态
         * TaskStatusEnum
         */
        private Integer status;
    }

    /**
     * 简略任务
     */
    @Data
    public static class BriefTask{
        /**
         * 任务id
         */
        private Long taskId;
        /**
         * 股道名称
         */
        private Integer laneId;
        /**
         * 任务命令来源
         */
        private String cmdSrc;
    }

    /**
     * 位置信息
     */
    @Data
    public static class Position{
        /**
         * x轴坐标
         */
        private Double x;
        /**
         * y轴坐标
         */
        private Double y;
        /**
         * 角度
         */
        private Double angle;
        /**
         * 当前拍照点序号
         */
        private Integer collectPointNum;
        /**
         * 当前所在停车点
         */
        private Integer parkPointSeq;
        /**
         * carriage
         */
        private Integer carriage;
        /**
         * 车厢位置百分比
         */
        private Integer agvPosPercent;
    }

    /**
     * 健康信息
     */
    @Data
    public static class HealthInfo{
        /**
         * 对象名称
         */
        private String objName;
        /**
         * 健康状态
         */
        private Integer status;
        /**
         * 健康信息描述
         */
        private String desc;
    }

}
