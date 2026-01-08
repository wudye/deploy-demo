package com.mwu.demo3.aChainOfResponsibiltyPattern;

public class Wo implements IWo{
    private int type=0;
    private String request = "";

    public Wo(int type, String request) {
        this.type = type;
        switch (this.type){
            case 1:
                this.request = "for fa " + request;
                break;
            case 2:
                this.request = "for hu：" + request;
                break;
            case 3:
                this.request = "for so：" + request;
        }
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String getRequest() {
        return this.request;
    }
}
