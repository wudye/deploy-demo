package com.mwu.demo3.aChainOfResponsibiltyPattern;

public abstract class Handler {
    private int level = 0;

    private Handler nexrHandler;

    public Handler(int level) {
        this.level = level;
    }

    public void setNexrHandler(Handler nexrHandler) {
        this.nexrHandler = nexrHandler;
    }

    public final void handleMessage(IWo women) {
        if (women.getType() == this.level) {
            this.handle(women);
        } else {
            if (this.nexrHandler != null) {
                this.nexrHandler.handleMessage(women);
            } else {
                System.out.println("没有找到合适的");
            }
        }
    }

    protected abstract void handle(IWo women);
}
