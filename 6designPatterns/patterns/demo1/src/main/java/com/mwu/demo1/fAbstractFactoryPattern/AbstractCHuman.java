package com.mwu.demo1.fAbstractFactoryPattern;

import com.mwu.demo1.efactoryMethodPattern.Human;

public abstract class AbstractCHuman implements Human {
    public void laugh() {
        System.out.println("Abtract C laugh");
    }
    public void cry() {
        System.out.println("Abtract C cry");
    }
    public void talk() {
        System.out.println("Abtract C talk");
    }

}
