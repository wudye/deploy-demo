package com.mwu.demo1.fAbstractFactoryPattern;

import com.mwu.demo1.efactoryMethodPattern.Human;

public abstract class AbstractAHuman implements Human {


    public void laugh() {
        System.out.println("Abtract A laugh");
    }
    public void cry() {
        System.out.println("Abtract A cry");
    }
    public void talk() {
        System.out.println("Abtract A talk");
    }
}
