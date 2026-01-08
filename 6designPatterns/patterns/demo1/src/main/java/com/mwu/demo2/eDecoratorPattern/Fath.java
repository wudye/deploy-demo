package com.mwu.demo2.eDecoratorPattern;

public class Fath {
    public static void main(String[] args) {
        SchoolReport sr ;
        sr = new FouthGradeSchoolReport();
        sr = new HignScoreDecorator(sr);
        sr = new SortDecorator(sr);
        sr.report();
        sr.sign("mwu");
    }
}
