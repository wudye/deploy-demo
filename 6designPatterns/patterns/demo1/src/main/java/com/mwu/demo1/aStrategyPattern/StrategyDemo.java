package com.mwu.demo1.aStrategyPattern;

import com.mwu.demo1.aStrategyPattern.strategyImpl.StrategyImpl1;
import com.mwu.demo1.aStrategyPattern.strategyImpl.StrategyImpl2;
import com.mwu.demo1.aStrategyPattern.strategyImpl.StrategyImpl3;

public class StrategyDemo {
    public static void main(String[] args) {
        Content content = new Content(new StrategyImpl1());
        content.execute();
        Content content2 = new Content(new StrategyImpl2());
        content2.execute();
        Content content3 = new Content(new StrategyImpl3());
        content3.execute();
    }
}
