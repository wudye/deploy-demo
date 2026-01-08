package com.mwu.demo1.bProxyPattern;

public class ProxyDemo {
    public static void main(String[] args) {

        ActuallOne actuallOne = new ActuallOne();

        ProxyTwo proxyTwo = new ProxyTwo(actuallOne);
        proxyTwo.show();
        proxyTwo.show2();
    }
}
