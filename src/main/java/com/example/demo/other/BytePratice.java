package com.example.demo.other;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;

import java.io.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author LTJ
 * @date 2021/12/6
 */
@Slf4j
public class BytePratice {
    public static void main(String[] args) {
        Integer i=null;
        doa(i);
    }


    private static void doa(int i){
        System.out.println("i = " + i);
    }
}
