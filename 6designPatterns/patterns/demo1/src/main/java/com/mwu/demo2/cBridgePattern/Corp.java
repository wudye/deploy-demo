package com.mwu.demo2.cBridgePattern;

public abstract class Corp {

    private Product product;

    public Corp(Product product) {
        this.product = product;
    }


    public void makeMoney(){
        this.product.beProducted();
        this.product.beSold();
    }
}
