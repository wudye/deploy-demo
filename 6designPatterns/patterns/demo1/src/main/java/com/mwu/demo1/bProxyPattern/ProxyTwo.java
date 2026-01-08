package com.mwu.demo1.bProxyPattern;

public class ProxyTwo implements ProxySet{

    private ProxySet proxySet;

    public ProxyTwo(ProxySet proxySet) {
        this.proxySet = proxySet;
    }
    @Override
    public void show() {
          System.out.println("ProxyTwo before show");
            proxySet.show();
            System.out.println("ProxyTwo after show");
    }

    @Override
    public void show2() {
            System.out.println("ProxyTwo before show2");
                proxySet.show2();
                System.out.println("ProxyTwo after show2");

    }
}
