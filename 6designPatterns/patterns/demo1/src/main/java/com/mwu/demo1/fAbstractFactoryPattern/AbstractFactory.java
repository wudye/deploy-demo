package com.mwu.demo1.fAbstractFactoryPattern;

import com.mwu.demo1.efactoryMethodPattern.Human;

public abstract class AbstractFactory implements  FactoryDemo{

    protected Human createHuman(AllEnum  type) {
        Human human = null;
        if (!type.getValue().equals("")) {
            try {
                human = (Human) Class.forName(type.getValue()).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return human;
    }


}
