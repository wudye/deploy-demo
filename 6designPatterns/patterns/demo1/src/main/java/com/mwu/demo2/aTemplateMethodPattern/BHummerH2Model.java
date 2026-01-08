package com.mwu.demo2.aTemplateMethodPattern;

public class BHummerH2Model extends HummerModel{
    @Override
    public void start() {
        System.out.println("hummer h2 start");
    }

    @Override
    public void stop() {
        System.out.println("hummer h2 stop");

    }

    @Override
    public void alarm() {
        System.out.println("hummer h2 alarm");


    }

    @Override
    public void engineBoom() {

        System.out.println("hummer h2 engineBoom");

    }

    @Override
    protected boolean isAlarm() {
        return false;
    }




}
