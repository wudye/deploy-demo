package com.mwu.demo2.hOberserverPattern;

public class Wang implements Observer{
    @Override
    public void update(String context) {
        System.out.println("Wang update: " + context);
        this.receive(context);
        System.out.println("Wang finish");
    }
    private void receive(String context) {
        System.out.println("Wang receive: " + context);
    }
}
