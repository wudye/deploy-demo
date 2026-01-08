package com.mwu.demo1.aStrategyPattern;


import com.mwu.demo1.aStrategyPattern.strategy.IStrategy;

public class Content {
    private IStrategy strategy;

    public Content(IStrategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        strategy.doSomething();
    }
}
