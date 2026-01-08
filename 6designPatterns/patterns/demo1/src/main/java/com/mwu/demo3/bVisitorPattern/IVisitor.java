package com.mwu.demo3.bVisitorPattern;

public interface IVisitor {
    public void visit(CommonEmployee commonEmployee);
    public void visit(Manager manager);
}
