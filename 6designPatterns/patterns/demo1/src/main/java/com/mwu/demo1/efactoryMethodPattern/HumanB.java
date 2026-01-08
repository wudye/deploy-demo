package com.mwu.demo1.efactoryMethodPattern;


public class HumanB implements Human{
    @Override
    public void laugh() {
        System.out.println("B laugh");
    }

    @Override
    public void cry() {
        System.out.println("B cry");

    }

    @Override
    public void talk() {
        System.out.println("B talk");

    }
}
