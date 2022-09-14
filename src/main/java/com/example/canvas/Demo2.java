package com.example.canvas;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LTJ
 * @date 2022/4/25
 */
@Slf4j
public class Demo2 {
    public static SecureRandom secureRandom = new SecureRandom();


    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate localDate = today.with(TemporalAdjusters.lastDayOfMonth()).minusDays(2);
//        System.out.println("localDate = " + localDate);

        LocalDateTime time = LocalDateTime.of(2009, 3, 29, 9, 0, 0);
        Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
        Date from = Date.from(instant);
        DateTime dateTime = DateUtil.offsetMonth(from, -1);
        LocalDateTime localDateTime = dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("format = " + format);
        try {
//            jsonTest();
//            read();
            parseOutLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void jsonTest() throws IOException {
        final int CODINATE_SIZE = 6400;
        List<Double[]> codinateList = new ArrayList<>(CODINATE_SIZE);
        for(int i=0;i<CODINATE_SIZE;i++){
            Double[] codinate = new Double[2];
            codinate[0] = secureRandom.nextDouble();
            codinate[1] = secureRandom.nextDouble();
            codinateList.add(codinate);
        }
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File desktop = fileSystemView.getHomeDirectory();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("codinates", codinateList);
        File json = FileUtil.file(desktop, "codinate.json");
        String content = jsonObject.toString();
        FileUtil.writeUtf8String(content, json);
        System.out.printf("文件：%s 输出完毕\n", FileUtil.getCanonicalPath(json));

//        String content2 = jsonObject.toString(SerializerFeature.PrettyFormat);
//        File json2 = FileUtil.file(desktop, "codinatepretty.json");
//        FileUtil.writeUtf8String(content2, json2);
//        System.out.printf("文件：%s 输出完毕", FileUtil.getCanonicalPath(json2));

        File txt = FileUtil.file(desktop, "codinate.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(txt);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        for (Double[] codinate : codinateList) {
//            printWriter.print(codinate[0]+","+ codinate[1]+";");
            printWriter.println(codinate[0]+","+ codinate[1]);
        }
        printWriter.close();
        System.out.printf("文件：%s 输出完毕\n", FileUtil.getCanonicalPath(txt));

//        File binFile = FileUtil.file(desktop, "cordinate.b2");
//        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(binFile));
//        DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
//        for (Double[] codinate : codinateList) {
//            dataOutputStream.writeDouble(codinate[0]);
//            dataOutputStream.writeDouble(codinate[1]);
//            dataOutputStream.writeChar('\n');
//        }
//        dataOutputStream.close();
//        System.out.printf("文件：%s 输出完毕", FileUtil.getCanonicalPath(binFile));
    }

    public static void read() throws IOException {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File desktop = fileSystemView.getHomeDirectory();
        File outputDir = FileUtil.file(desktop, "转换数据文件夹");
        FileUtil.mkdir(outputDir);


        File traceFile = FileUtil.file("C:\\Users\\tianjie_liang\\Desktop\\20220815161918_01_");
        for(int sectionNum=1;sectionNum<=106;sectionNum++){
            JSONObject sectionData = new JSONObject();
            JSONObject cameraDataList = new JSONObject(true);
            sectionData.put("cameraDataList", cameraDataList);
            for(int cameraCode=1;cameraCode<=8;cameraCode++){
                JSONObject cameraData = new JSONObject();
                cameraDataList.put("camera" + cameraCode, cameraData);

                JSONArray cordinateList = new JSONArray();
                cameraData.put("cordinateList", cordinateList);

                File c = FileUtil.file(traceFile, cameraCode + "\\" + "C" + sectionNum);
                File r = FileUtil.file(traceFile, cameraCode + "\\" + "R" + sectionNum);
                if(!FileUtil.exist(c) || !FileUtil.exist(r)){
                    continue;
                }
                BufferedReader cReader = getBufferedReader(c);
                BufferedReader rReader = getBufferedReader(r);
                parseCol(rReader, cReader, cordinateList);
                cReader.close();
                rReader.close();
            }
            File sectionJson = FileUtil.file(outputDir, "section" + sectionNum + ".json");
            String content = sectionData.toString(SerializerFeature.PrettyFormat);
//            String content = JSON.toJSONString(sectionData, valueFilter, SerializerFeature.PrettyFormat);
            FileUtil.writeUtf8String(content, sectionJson);
            System.out.printf("文件：%s 输出完毕", FileUtil.getCanonicalPath(sectionJson));

            
        }
    }

    public static void parseOutLine() throws IOException {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File desktop = fileSystemView.getHomeDirectory();
        File output = FileUtil.file(desktop, "outline.json");

        File c = FileUtil.file(desktop, "限界C.tup");
        File r = FileUtil.file(desktop, "限界R.tup");
        if(!FileUtil.exist(c) || !FileUtil.exist(r)){
            return;
        }
        BufferedReader cReader = getBufferedReader(c);
        BufferedReader rReader = getBufferedReader(r);
        JSONArray cordinateList = new JSONArray();
        JSONObject outline = new JSONObject();
        outline.put("outLine", cordinateList);
        parseCol(rReader, cReader, cordinateList);
        cReader.close();
        rReader.close();
        String content = outline.toString(SerializerFeature.PrettyFormat);
        FileUtil.writeUtf8String(content, output);
        System.out.printf("文件：%s 输出完毕", FileUtil.getCanonicalPath(output));
    }

    private static BufferedReader getBufferedReader(File file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader;
    }

    private static void parseCol(BufferedReader rbr,BufferedReader cbr, JSONArray cordinateList) throws IOException {
        String s=null;
        s = cbr.readLine();
        String s1 = rbr.readLine();
        if(s==null || s1==null){
            log.error("错误的读取");
            return;
        }
        Integer count1 = Convert.toInt(s1);
        Integer count = Convert.toInt(s);
        Assert.notNull(count);
        if(!count.equals(count1)){
            log.error("横纵坐标数量不等，row:{}，col:{}", count1, count);
            return;
        }
        for(int i=0;i<count;i++){
            Double[] cordinate = new Double[2];
            s = rbr.readLine();
            if(s==null){
                log.error("第{}个坐标读取无数据", i);
                return;
            }

            String[] split = s.split("\\s");
            if(split.length!=2){
                log.error("{}的格式不符合提取格式", s);
                return;
            }
            cordinate[0] = Convert.toDouble(split[1]);
            //================
            s = cbr.readLine();
            if(s==null){
                log.error("第{}个坐标读取无数据", i);
                return;
            }

            split = s.split("\\s");
            if(split.length!=2){
                log.error("{}的格式不符合提取格式", s);
                return;
            }
            cordinate[1] = Convert.toDouble(split[1]);

//            System.out.printf("[0]=%s [1]=%s \n", split[0], split[1]);
            cordinateList.add(cordinate);
        }
    }
}
