package com.example.canvas;

import cn.hutool.core.io.FileUtil;
import org.ini4j.Profile;
import org.ini4j.Wini;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author LTJ
 * @date 2022/9/9
 */
public class ParseWarninigPortal {

    public static void main(String[] args) throws IOException {
        ParseWarninigPortal parseWarninigPortal = new ParseWarninigPortal();
        parseWarninigPortal.readTrainInfo();
    }

    public void readTrainInfo() throws IOException {
        String path = "C:\\Users\\tianjie_liang\\Desktop\\20220815161918_01_\\d_data_log\\TrainInfo.ini";
        InputStreamReader gbk = new InputStreamReader(new FileInputStream(path), Charset.forName("GBK"));
        Wini wini = new Wini(gbk);
        Set<Map.Entry<String, Profile.Section>> entries = wini.entrySet();
        for (Map.Entry<String, Profile.Section> entry : entries) {
            String key = entry.getKey();
            Profile.Section section = entry.getValue();
//            System.out.printf("[%s] %n", key);
            Set<Map.Entry<String, String>> entries1 = section.entrySet();
            for (Map.Entry<String, String> stringStringEntry : entries1) {
                String key1 = stringStringEntry.getKey();
                String value = stringStringEntry.getValue();
//                System.out.printf("%s=%s  %n", key1,value);
            }
//            System.out.println();
        }
        String s = wini.get("限界門檢測項", "104_dis_high");
        System.out.println("s = " + s);
        Profile.Section section = wini.get("d_train_log");
        List<Profile.Section> d_train_log = wini.getAll("d_train_log");
        section.put("Station", 2);
        FileWriter fileWriter = new FileWriter(path);
        wini.store(fileWriter);
    }
}
