package com.mwu.demo3.aChainOfResponsibiltyPattern;

public class Hus extends Handler {

    public Hus() {
        super(
                2
        );
    }


    @Override
    protected void handle(IWo women) {
        System.out.println("Hus handle: " + women.getRequest());
        System.out.println("Hus finish");

    }
}
