package com.mwu.demo2.cBridgePattern;

public class Clothes extends Product{
    @Override
    public void beProducted() {
        System.out.println("clothes beProducted");
    }

    @Override
    public void beSold() {
        System.out.println("clothes beSold");



    }
}
