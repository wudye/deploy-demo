package com.mwu.demo2.cBridgePattern;

public class BridgeDemo {
    public static void main(String[] args) {

        House house = new House();
        HouseCorp houseCorp = new HouseCorp(house);
        houseCorp.makeMoney();

    }
}
