package com.mwu.demo2.dCommandPattern;

public class CommandDemo {
    public static void main(String[] args) {
        Invoker i1    = new Invoker();
        i1.setCommand(new AddRequirementCommand());
        i1.action();
    }
}
