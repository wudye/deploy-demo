package com.mwu.demo2.eDecoratorPattern;

public class Decorator extends SchoolReport{

    private SchoolReport sc;

    public Decorator(SchoolReport sc) {
        this.sc = sc;
    }
    @Override
    public void report() {
        sc.report();
    }

    @Override
    public void sign(String name) {
        sc.sign(name);
    }
}
