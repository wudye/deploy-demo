package com.mwu.demo2.aTemplateMethodPattern;

public class AHummerH1Model extends HummerModel{

    private boolean alarmFlag = true;
    @Override
    public void start() {
        System.out.println("hummer h1 start");
    }

    @Override
    public void stop() {
        System.out.println("hummer h1 stop");

    }

    @Override
    public void alarm() {
        System.out.println("hummer h1 alarm");

    }

    @Override
    public void engineBoom() {
        System.out.println("hummer h1 engineBoom");


    }

    @Override
    protected boolean isAlarm() {
        return this.alarmFlag;
    }

    public void setAlarmFlag(boolean alarmFlag) {
        this.alarmFlag = alarmFlag;
    }


}
