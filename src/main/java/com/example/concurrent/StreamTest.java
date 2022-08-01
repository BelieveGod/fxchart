package com.example.concurrent;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * @author LTJ
 * @date 2022/4/20
 */
public class StreamTest {
    public static void main(String[] args) {
        ArrayList<Integer> list = CollUtil.newArrayList(1, 2, 3, 4, 5);
        CompletableFuture.runAsync(() -> {
            for (int i = 6; i < 10; i++) {
                list.add(i);
                System.out.println("添加了"+i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        int sum=0;
        for (Integer integer : list) {
            System.out.println("等待300ms");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            sum += integer;
        }

    }


}
