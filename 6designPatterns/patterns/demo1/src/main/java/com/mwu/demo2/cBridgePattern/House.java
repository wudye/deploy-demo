package com.mwu.demo2.cBridgePattern;

public class House extends Product{
    @Override
    public void beProducted() {
        System.out.println("house beProducted");
    }

    @Override
    public void beSold() {
        System.out.println("house beSold");


    }
}
