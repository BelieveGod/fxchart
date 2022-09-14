package com.example.fx;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.fx.dto.WarningPortalSectionDTO;
import com.example.fx.dto.WarningPortalTrainInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Profile;
import org.ini4j.Wini;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author LTJ
 * @date 2022/9/14
 */
@Slf4j
public class ParseService {
    public static final String D_DATA_LOG = "d_data_log";
    public static final String TRIAN_INFO_INI = "TrainInfo.ini";
    public static final String WARNING_PORTAL = "warningPortal";
    public static final String TRAIN_INFO_JSON = "trainInfo.json";
    public static final String SECTION_DATA = "sectionData";
    public static final String OUT_LINE_FILE = "outLine.json";

    public static Map<Integer, String> cameraNameMap = new HashMap<>();
    static {
        cameraNameMap.put(1, "L1");
        cameraNameMap.put(2, "L2");
        cameraNameMap.put(3, "L3");
        cameraNameMap.put(4, "L4");

        cameraNameMap.put(5, "R1");
        cameraNameMap.put(6, "R2");
        cameraNameMap.put(7, "R3");
        cameraNameMap.put(8, "R4");
    }

    public static void convert(String sourceDir, String destDir,PercentReceiver percentReceiver) throws IOException {
        File trainInfoIni = FileUtil.file(sourceDir, D_DATA_LOG, TRIAN_INFO_INI);
        if (!FileUtil.exist(trainInfoIni)) {
            throw new RuntimeException(StrUtil.format("不存在文件：{}", trainInfoIni.getAbsolutePath()));
        }
        File alarmDataSql = FileUtil.file(destDir, WARNING_PORTAL,"alarmData.sql");
        FileUtil.del(alarmDataSql);
        FileUtil.mkParentDirs(alarmDataSql);


        Wini wini = new Wini(trainInfoIni);
        HashMap<String, String> iniMap = new HashMap<>();
        for (Map.Entry<String, Profile.Section> sectionEntry : wini.entrySet()) {
            Profile.Section section = sectionEntry.getValue();
            iniMap.putAll(section);
        }
        WarningPortalTrainInfoDTO warningPortalTrainInfoDTO = new WarningPortalTrainInfoDTO();
        warningPortalTrainInfoDTO.setTrainNo(iniMap.get("train_no"));
        warningPortalTrainInfoDTO.setTraceTime(DateUtil.parse(iniMap.get("trace_time")));
        warningPortalTrainInfoDTO.setDirection(Convert.toInt(iniMap.get("direction")));
        warningPortalTrainInfoDTO.setStation(Convert.toInt(iniMap.get("Station")));
        warningPortalTrainInfoDTO.setAxleNum(Convert.toInt(iniMap.get("AxlesNum")));
        warningPortalTrainInfoDTO.setCountour(iniMap.get("Countour"));
        warningPortalTrainInfoDTO.setStatus(Convert.toInt(iniMap.get("status")));


        Integer sectionSize = Convert.toInt(iniMap.get("totalData"));
        Integer cameraDataSize = Convert.toInt(iniMap.get("totalDevice"));
        if(percentReceiver!=null){
            CompletableFuture.runAsync(() -> {
                percentReceiver.receive(0.05);
            });
        }
        /* begin ==========可用相机============= */

        LinkedList<Integer> availableCameras = new LinkedList<>();
        for (int cameraId = 1; cameraId <= cameraDataSize; cameraId++) {
            File cameraDir = FileUtil.file(sourceDir, String.valueOf(cameraId));
            if (FileUtil.exist(cameraDir)) {
                availableCameras.add(cameraId);
            } else {
                log.info("不存在相机：{}", cameraId);
            }
        }
        /* end ============可用相机============ */
        warningPortalTrainInfoDTO.setAvailableCameras(availableCameras);

        /* begin ==========切面============= */
        FileOutputStream alarmDataSqlOs = new FileOutputStream(alarmDataSql);
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(alarmDataSqlOs, StandardCharsets.UTF_8));
        ArrayList<WarningPortalTrainInfoDTO.SectionData> sectionDataArrayList = new ArrayList<WarningPortalTrainInfoDTO.SectionData>(sectionSize);
        for(int sectionNum = 1; sectionNum <=sectionSize; sectionNum++){
            WarningPortalTrainInfoDTO.SectionData sectionData = new WarningPortalTrainInfoDTO.SectionData();
            sectionData.setNum(sectionNum);
            Double lowDistance = Convert.toDouble(iniMap.get(StrUtil.format("{}_dis_low", sectionNum)));
            Double highDistance = Convert.toDouble(iniMap.get(StrUtil.format("{}_dis_high", sectionNum)));
            Assert.notNull(lowDistance);
            Assert.notNull(highDistance);
            sectionData.setDistanceRange(new Double[]{lowDistance, highDistance});

            List<WarningPortalTrainInfoDTO.CameraResult> cameraResultList = new ArrayList<>(availableCameras.size());
            for (Integer availableCamera : availableCameras) {
                WarningPortalTrainInfoDTO.CameraResult cameraResult = new WarningPortalTrainInfoDTO.CameraResult();
                cameraResult.setCameraId(availableCamera);
                Double outOfBound = Convert.toDouble(iniMap.get(StrUtil.format("{}_{}", sectionNum, availableCamera)));
                cameraResult.setOutOfBound(outOfBound);
                if(outOfBound>0){   // 说明有告警
                    InputStream resourceAsStream = ParseService.class.getResourceAsStream("/img/outLine.jpg");
                    String cameraName = cameraNameMap.getOrDefault(availableCamera, "L0");
                    FileOutputStream fileOutputStream = new FileOutputStream(FileUtil.file(destDir, WARNING_PORTAL, StrUtil.format("wp_{}_{}.jpg", cameraName, sectionNum)));
                    IoUtil.copy(resourceAsStream, fileOutputStream);
                    resourceAsStream.close();
                    fileOutputStream.close();

                    String dataSql = StrUtil.format("INSERT INTO da_alarm_log(train_no,train_log_id,device_type_id,check_id,part_code,alarm_value,da_alarm_value,alarm_status,flag_alarm,alam_level,da_alarm_level,mfrs_code)"
                            + " VALUES(?1,?2,401,{},{},{},{},0,3,1,1,0);", availableCamera, sectionNum, outOfBound, outOfBound);
                    br.write(dataSql);
                    br.newLine();
                }
                // 加入集合
                cameraResultList.add(cameraResult);
            }
            sectionData.setCameraResult(cameraResultList);
            // 加入集合
            sectionDataArrayList.add(sectionData);
            if(percentReceiver!=null){
                int sectionNumCp=sectionNum;
                CompletableFuture.runAsync(() -> {
                    percentReceiver.receive(sectionNumCp / (double) sectionSize * 0.1+0.05);
                });
            }
        }
        br.close();
        /* end ============切面============ */
        warningPortalTrainInfoDTO.setSectionData(sectionDataArrayList);
        File trainInfoJson = FileUtil.file(destDir, WARNING_PORTAL, TRAIN_INFO_JSON);
        String content = JSONObject.toJSONString(warningPortalTrainInfoDTO, SerializerFeature.PrettyFormat,SerializerFeature.WriteDateUseDateFormat);
        FileUtil.writeUtf8String(content, trainInfoJson);


        /* begin ==========坐标文件============= */
        for(int sectionNum=1;sectionNum<=sectionSize;sectionNum++){
            File sectionDataJson = FileUtil.file(destDir, WARNING_PORTAL, SECTION_DATA, StrUtil.format("section_{}.json", sectionNum));
            WarningPortalSectionDTO warningPortalSectionDTO = new WarningPortalSectionDTO();

            List<WarningPortalSectionDTO.CameraData> cameraDataList = new ArrayList<>(availableCameras.size());
            for(int i=0;i<availableCameras.size();i++){
                Integer cameraId = availableCameras.get(i);
                File rowFile = FileUtil.file(sourceDir, String.valueOf(cameraId), "R" + sectionNum);
                if(!FileUtil.exist(rowFile)){
                    log.error("不存在文件：{}", rowFile.getAbsolutePath());
                    continue;
                }
                File colFile = FileUtil.file(sourceDir, String.valueOf(cameraId), "C" + sectionNum);
                if(!FileUtil.exist(colFile)){
                    log.error("不存在文件：{}", colFile.getAbsolutePath());
                    continue;
                }
                BufferedReader rowReader = new BufferedReader(new InputStreamReader(new FileInputStream(rowFile), Charset.forName("GBK")));
                BufferedReader colReader = new BufferedReader(new InputStreamReader(new FileInputStream(colFile), Charset.forName("GBK")));
                List<Double[]> cordinateList = parseCordinate(rowReader, colReader);
                rowReader.close();
                colReader.close();
                WarningPortalSectionDTO.CameraData cameraData = new WarningPortalSectionDTO.CameraData();
                cameraData.setCameraId(cameraId);
                cameraData.setCordinateList(cordinateList);
                //加入集合
                cameraDataList.add(cameraData);
            }
            // 设置值
            warningPortalSectionDTO.setCameraDataList(cameraDataList);
            content = JSONObject.toJSONString(warningPortalSectionDTO, SerializerFeature.PrettyFormat,SerializerFeature.WriteDateUseDateFormat);
            FileUtil.writeUtf8String(content, sectionDataJson);
            if(percentReceiver!=null){
                double sectionNumCp=sectionNum;
                CompletableFuture.runAsync(() -> {
                    percentReceiver.receive(sectionNumCp / sectionSize *0.8 +0.15 );
                });
            }
        }

        /* end ============坐标文件============ */

        /* begin ==========轮廓文件============= */
        File outLineRow = FileUtil.file(sourceDir, "限界R.tup");
        File outLineCol = FileUtil.file(sourceDir, "限界C.tup");

        if(FileUtil.exist(outLineRow) && FileUtil.exist(outLineCol)){
            BufferedReader rowReader = new BufferedReader(new InputStreamReader(new FileInputStream(outLineRow), Charset.forName("GBK")));
            BufferedReader colReader = new BufferedReader(new InputStreamReader(new FileInputStream(outLineCol), Charset.forName("GBK")));
            List<Double[]> cordinateList = parseCordinate(rowReader, colReader);
            rowReader.close();
            colReader.close();
            JSONObject outLineJson = new JSONObject();
            File outLineFile = FileUtil.file(destDir, WARNING_PORTAL, OUT_LINE_FILE);
            outLineJson.put("outLine", cordinateList);
            content = JSONObject.toJSONString(outLineJson, SerializerFeature.PrettyFormat,SerializerFeature.WriteDateUseDateFormat);
            FileUtil.writeUtf8String(content, outLineFile);
        }else{
            log.warn("文件夹：{}不存在标准轮廓的转换数据", sourceDir);
        }
        /* end ============轮廓文件============ */


        if(percentReceiver!=null){
            CompletableFuture.runAsync(() -> {
                percentReceiver.receive(1d);
            });
        }
    }


    private static List<Double[]> parseCordinate(BufferedReader rowReader, BufferedReader colReader) throws IOException {
        String totalStr = rowReader.readLine();
        Integer rowTotal = Convert.toInt(totalStr);
        if(rowTotal ==null){
            log.error("行文件的行数错误");
            return Collections.emptyList();
        }
        totalStr = colReader.readLine();
        Integer colTotal = Convert.toInt(totalStr);
        if (!rowTotal.equals(colTotal)) {
            log.error("行文件的行数：{}不等于列文件的行数：{}", rowTotal, colTotal);
            return Collections.emptyList();
        }
        List<Double[]> cordinateList = new ArrayList<>(rowTotal);
        for(int i=0;i<rowTotal;i++){
            String s = rowReader.readLine();
            Double y = getCordinate(s);
            s = colReader.readLine();
            Double x = getCordinate(s);
            cordinateList.add(new Double[]{x, y});
        }
        return cordinateList;
    }

    private static Double getCordinate(String s){
        Assert.notNull(s);
        String[] fileds = s.split("\\s");
        if(fileds.length!=2){
            throw new RuntimeException(StrUtil.format("解析的行数据格式错误：{}", s));
        }
        Double aDouble = Convert.toDouble(fileds[1]);
        if(aDouble==null){
            throw new RuntimeException(StrUtil.format("解析的行数据格式错误：{}", fileds[1]));
        }
        return aDouble;
    }


    public static interface PercentReceiver{
        public void receive(Double percent);
    }

}
