package com.mwu.demo1.fAbstractFactoryPattern;


import com.mwu.demo1.efactoryMethodPattern.Human;

public class MainDemo {
    public static void main(String[] args) {
        AbstractFactory factory = new AFactory();
        AbstractFactory factory1 = new BFactory();


        Human ah = factory.createAHuman();
        ah.cry();
        ah.laugh();
        ah.talk();
        Human bh = factory1.createBHuman();
        bh.cry();
        bh.laugh();
        bh.talk();


    }
}
