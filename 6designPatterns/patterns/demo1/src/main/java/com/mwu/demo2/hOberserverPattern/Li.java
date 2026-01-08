package com.mwu.demo2.hOberserverPattern;

public class Li implements Observer{
    @Override
    public void update(String context) {
        System.out.println("Li update: " + context);
        this.receive(context);
        System.out.println("Li finish");
    }

    private void receive(String context) {
        System.out.println("Li receive: " + context);
    }
}
