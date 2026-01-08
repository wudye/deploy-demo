package com.mwu.demo1.cSingletonPattern;

public class BDemo {
    public static void main(String[] args) {
        ASingleton singletonDemo1 = ASingleton.getInstance();
        ASingleton singletonDemo2 = ASingleton.getInstance();

        System.out.println(singletonDemo1 == singletonDemo2); // true


    }
}
