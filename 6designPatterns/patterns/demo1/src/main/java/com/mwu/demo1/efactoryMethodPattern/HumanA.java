package com.mwu.demo1.efactoryMethodPattern;

public class HumanA implements Human{
    @Override
    public void laugh() {
        System.out.println("A laugh");
    }

    @Override
    public void cry() {
        System.out.println("A cry");

    }

    @Override
    public void talk() {

        System.out.println("A talk");

    }
}
