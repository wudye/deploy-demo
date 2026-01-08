package com.mwu.demo1.gFacadePattern;

public class FacadeMain {
    public static void main(String[] args) {

        LetterProcessImpl letterProcess = new LetterProcessImpl();
        letterProcess.writeContext("hello");
        letterProcess.fillEnvelope("beijing");
        letterProcess.letterIntoEnvelope();
        letterProcess.sendLetter();

       ModePostOffice modePostOffice = new ModePostOffice();
        modePostOffice.sendLetter("hello", "beijing");


    }
}
