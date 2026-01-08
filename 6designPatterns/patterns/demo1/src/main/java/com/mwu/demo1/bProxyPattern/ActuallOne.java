package com.mwu.demo1.bProxyPattern;

public class ActuallOne implements ProxySet{
    @Override
    public void show() {
        System.out.println("I am ActuallOne I need a proxy");
    }

    @Override
    public void show2() {

        System.out.println("I am ActuallOne show2 method need a proxy");
    }
}
