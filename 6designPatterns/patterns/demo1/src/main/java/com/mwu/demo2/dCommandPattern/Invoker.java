package com.mwu.demo2.dCommandPattern;

public class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }
    public void action() {
        command.execute();
    }
}

