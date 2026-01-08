package com.mwu.demo1.dMultitionPattern;

public class MultiDemo {
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            MultitionPattern multitionPatternTemp =  MultitionPattern.getInstance();
            multitionPatternTemp.getInfo();
        }


    }
}
