package com.mwu.demo2.cBridgePattern;

public class HouseCorp extends Corp{
    public HouseCorp(Product product) {
        super(product);
    }



    public void makeMoney(){
        super.makeMoney();
        System.out.println("house makeMoney");
    }
}
