package com.mwu.demo3.aChainOfResponsibiltyPattern;

public class Fa extends Handler{

    public Fa() {
        super(1);
    }

    @Override
    protected void handle(IWo women) {
        System.out.println("Fa handle: " + women.getRequest());
        System.out.println("Fa finish");
    }
}
