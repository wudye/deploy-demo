package com.mwu.demo1.cSingletonPattern;


import javax.swing.plaf.PanelUI;

public class ASingleton {

    private static ASingleton instance = null;

    private ASingleton() {
        // private constructor to prevent instantiation
    }

    public static ASingleton getInstance() {
        if (instance == null) {
            synchronized (ASingleton.class) {
                if (instance == null) {
                    instance = new ASingleton();
                }
            }
        }
        return instance;
    }
    /*
    public synchronized static ASingleton getInstance() {
        if (instance == null) {
            instance = new ASingleton();
        }
        return instance;
    }

     */

    public static void showMessage() {
        System.out.println("Hello from ASingleton!");
    }
}
