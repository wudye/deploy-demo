package com.mwu.demo3.aChainOfResponsibiltyPattern;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("all")
public class ChainDemo {
    public static void main(String[] args) {
        Random random = new Random();
        ArrayList<IWo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new Wo(random.nextInt(4), "go request"));
        }
        Handler so = new So();
        Handler fa = new Fa();
        Handler hus = new Hus();
        fa.setNexrHandler(hus);
        hus.setNexrHandler(so);
        for (IWo wo : list) {
            fa.handleMessage(wo);
        }

    }
}
