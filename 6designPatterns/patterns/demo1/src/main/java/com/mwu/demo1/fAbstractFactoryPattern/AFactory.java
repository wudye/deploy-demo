package com.mwu.demo1.fAbstractFactoryPattern;

import com.mwu.demo1.efactoryMethodPattern.Human;

public class AFactory  extends AbstractFactory{
    @Override
    public Human createAHuman() {
        return super.createHuman(AllEnum.AA);
    }

    @Override
    public Human createBHuman() {
        return super.createHuman(AllEnum.BA);
    }

    @Override
    public Human createCHuman() {
        return super.createHuman(AllEnum.CA);
    }
}
