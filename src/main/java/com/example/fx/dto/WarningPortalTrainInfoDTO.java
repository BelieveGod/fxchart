package com.example.fx.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author LTJ
 * @date 2022/9/8
 */
@Data
public class WarningPortalTrainInfoDTO {
    private String trainNo;

    private Date traceTime;

    private Integer direction;

    private Integer station;

    private Integer axleNum;

    private String  countour;

    private Integer status;

    private List<Integer> availableCameras;

    private List<SectionData> sectionData;

    @Data
    public static class SectionData {
        private Integer num;
        private Double[] distanceRange;
        private List<CameraResult> cameraResult;
    }

    @Data
    public static class CameraResult{
        private Integer cameraId;

        private Double outOfBound;
    }
}
