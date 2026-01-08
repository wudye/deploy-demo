package com.mwu.demo1.fAbstractFactoryPattern;

import com.mwu.demo1.efactoryMethodPattern.Human;

public abstract  class AbstractBHuman implements Human {
    public void laugh() {
        System.out.println("Abtract B laugh");
    }
    public void cry() {
        System.out.println("Abtract B cry");
    }
    public void talk() {
        System.out.println("Abtract B talk");
    }

}
