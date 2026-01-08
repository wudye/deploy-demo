package com.mwu.demo2.cBridgePattern;

public class IpodCorp extends Corp{


    public IpodCorp(Product product) {
        super(product);
    }

    public void makeMoney(){
        super.makeMoney();
        System.out.println("ipod makeMoney");
    }
}
