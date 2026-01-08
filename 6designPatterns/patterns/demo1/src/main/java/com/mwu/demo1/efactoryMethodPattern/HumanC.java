package com.mwu.demo1.efactoryMethodPattern;

public class HumanC implements Human{
    @Override
    public void laugh() {
        System.out.println("C laugh");
    }

    @Override
    public void cry() {

        System.out.println("C cry");

    }

    @Override
    public void talk() {

        System.out.println("C talk");

    }
}
