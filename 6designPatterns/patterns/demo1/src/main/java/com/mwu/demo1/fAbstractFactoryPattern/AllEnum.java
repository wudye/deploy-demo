package com.mwu.demo1.fAbstractFactoryPattern;

public enum AllEnum {
    AA("com.mwu.demo1.fAbstractFactoryPattern.AAImple"),
    AB("com.mwu.demo1.fAbstractFactoryPattern.ABImple"),
    BA("com.mwu.demo1.fAbstractFactoryPattern.BAImple"),
    BB("com.mwu.demo1.fAbstractFactoryPattern.BBImple"),
    CA("com.mwu.demo1.fAbstractFactoryPattern.CAImple"),
    CB("com.mwu.demo1.fAbstractFactoryPattern.CBImple");

    private String value = " ";
    private AllEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}
