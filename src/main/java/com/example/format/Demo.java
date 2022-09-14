package com.example.format;

/**
 * @author LTJ
 * @date 2022/8/31
 */
public class Demo {
    public static void main(String[] args) {
        System.out.printf("%1$s\t十进制:%1$,+010d\t八进制:%1$0#10o\t十六进制:%1$#010x%n", -254);
        System.out.printf("%1$s\t科学计数%1$e\t十进制浮点数:%1$f\t自动:%1$g\t16进制浮点:%1$a%n", Math.E);

        System.out.printf("%d%n", (int)92.4);
        System.out.printf("%d%n", (int)17.6);
    }
}
