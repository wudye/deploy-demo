package com.mwu.demo1.fAbstractFactoryPattern;

import com.mwu.demo1.efactoryMethodPattern.Human;

public class BFactory extends AbstractFactory{
    @Override
    public Human createAHuman() {
        return super.createHuman(AllEnum.AB);
    }

    @Override
    public Human createBHuman() {
        return super.createHuman(AllEnum.BB);
    }

    @Override
    public Human createCHuman() {
        return super.createHuman(AllEnum.CB);
    }
}
