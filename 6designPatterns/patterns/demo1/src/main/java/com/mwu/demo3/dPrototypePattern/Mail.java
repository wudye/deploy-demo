package com.mwu.demo3.dPrototypePattern;

public class Mail implements Cloneable{
    private String receiver;
    private String subject;
    private String context;
    private String appellation;
    private String tail;

    @Override
    public Mail clone() {
        Mail mail = null;
        try {
            mail = (Mail) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return mail;

    }
    public Mail(AdvTemplate advTemplate){
        this.receiver = advTemplate.getAdvSubject();
        this.subject = advTemplate.getAdvContext();
    }

    public void setTail(String tail) {
        this.tail = tail;
    }
    public String getTail() {
        return tail;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setContext(String context) {
        this.context = context;
    }

    public String getReceiver() {
        return receiver;
    }
    public String getSubject() {
        return subject;
    }
    public String getContext() {
        return context;
    }
    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }




}
