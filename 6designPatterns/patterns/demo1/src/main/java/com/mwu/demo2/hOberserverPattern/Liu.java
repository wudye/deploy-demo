package com.mwu.demo2.hOberserverPattern;

public class Liu implements Observer{
    @Override
    public void update(String context) {
        System.out.println("Liu update: " + context);
        this.receive(context);
        System.out.println("Liu finish");

    }
    private void receive(String context) {
        System.out.println("Liu receive: " + context);
    }
}
