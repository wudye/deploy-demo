package com.mwu.demo2.eDecoratorPattern;

public class FouthGradeSchoolReport extends SchoolReport{
    @Override
    public void report() {
        System.out.println("report");
        System.out.println("check this report");
        System.out.println("report end");
        System.out.println("sign this report");
    }

    @Override
    public void sign(String name) {
        System.out.println("your sign" + name);

    }
}
