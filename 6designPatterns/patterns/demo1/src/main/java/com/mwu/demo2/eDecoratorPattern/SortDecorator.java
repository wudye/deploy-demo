package com.mwu.demo2.eDecoratorPattern;

public class SortDecorator extends Decorator{
    public SortDecorator(SchoolReport sc) {
        super(sc);


    }

    private void reportSort() {
        System.out.println("reportSort");
    }

    @Override
    public void report() {
        super.report();
        reportSort();
    }
}
