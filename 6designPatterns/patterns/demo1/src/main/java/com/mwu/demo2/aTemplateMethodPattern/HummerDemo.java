package com.mwu.demo2.aTemplateMethodPattern;


public class HummerDemo {
    public static void main(String[] args) {
        HummerModel h1 = new AHummerH1Model();
        ((AHummerH1Model) h1).setAlarmFlag(false);
        h1.run();
        HummerModel h2 = new BHummerH2Model();
        h2.run();
    }
}
