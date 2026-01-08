package com.mwu.demo1.aStrategyPattern.strategyImpl;

import com.mwu.demo1.aStrategyPattern.strategy.IStrategy;

public class StrategyImpl2 implements IStrategy {
    @Override
    public void doSomething() {
        System.out.println("StrategyImpl2");
    }
}
