package com.example.pattern;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LTJ
 * @date 2022/6/21
 */
@Slf4j
public class PatternTest {
    public static final Pattern pattern = Pattern.compile(".+-(?<trainNo>\\w+)");

    public static void main(String[] args) {
        String a = "XADT-0317";
        String b = "DT-0156";
        String c = "sada-dada-121";
        String d = "-fdsf-df04";

        ArrayList<String> strings = CollUtil.newArrayList(a, b, c,d);
        for (String string : strings) {
            Matcher matcher = pattern.matcher(string);
            if (matcher.matches()) {
                String order = matcher.group("trainNo");
                String imgName = matcher.group("trainNo");
                log.info("{} 匹配，数字：{}，文件名：{}", string, order,imgName);
            }else {
                log.info("{} 不匹配！！！", string);
            }
        }

    }
}
