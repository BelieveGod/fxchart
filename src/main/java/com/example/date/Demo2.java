package com.example.date;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * @author LTJ
 * @date 2022/4/25
 */
public class Demo2 {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate localDate = today.with(TemporalAdjusters.lastDayOfMonth()).minusDays(2);
        System.out.println("localDate = " + localDate);
    }
}
