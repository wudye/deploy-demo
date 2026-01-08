package com.mwu.demo2.cBridgePattern;

public class ClothesCorp extends Corp{


    public ClothesCorp(Product product) {
        super(product);
    }

    public void makeMoney(){
        super.makeMoney();
        System.out.println("clothes makeMoney");
    }
}
