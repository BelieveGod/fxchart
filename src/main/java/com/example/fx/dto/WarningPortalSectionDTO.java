package com.example.fx.dto;

import lombok.Data;

import java.util.List;

/**
 * 和C++ 约定的，存储切面坐标的数据结构体
 * @author LTJ
 * @date 2022/9/9
 */
@Data
public class WarningPortalSectionDTO {

    private List<CameraData> cameraDataList;

    @Data
    public static class CameraData{
        private Integer cameraId;
        private List<Double[]> cordinateList;
    }
}
