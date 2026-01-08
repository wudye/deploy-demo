package com.mwu.demo2.eDecoratorPattern;

public class HignScoreDecorator extends Decorator{
    public HignScoreDecorator(SchoolReport sc) {
        super(sc);
    }

    private void reportHighScore() {
        System.out.println("reportHighScore");
    }

    @Override
    public void report() {
        super.report();
        reportHighScore();
    }

}
