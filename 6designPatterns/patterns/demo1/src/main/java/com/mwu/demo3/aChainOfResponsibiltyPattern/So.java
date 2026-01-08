package com.mwu.demo3.aChainOfResponsibiltyPattern;

public class So extends Handler {
    public So() {
        super(3);
    }


    @Override
    protected void handle(IWo women) {
        System.out.println("So handle: " + women.getRequest());
        System.out.println("So finish");

    }
}
