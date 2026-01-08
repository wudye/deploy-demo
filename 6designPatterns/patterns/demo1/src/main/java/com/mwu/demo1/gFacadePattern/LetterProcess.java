package com.mwu.demo1.gFacadePattern;

public interface LetterProcess {

    public void writeContext(String context);

    public void fillEnvelope(String address);

    public void letterIntoEnvelope();

    public void sendLetter();

}
