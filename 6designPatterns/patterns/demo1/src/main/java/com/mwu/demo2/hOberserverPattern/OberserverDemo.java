package com.mwu.demo2.hOberserverPattern;

public class OberserverDemo {
    public static void main(String[] args) throws InterruptedException {
        Observer li  = new Li();
        Observer wang = new Wang();
        Observer liu = new Liu();
        Han han = new Han();

        han.addObserver(li);
        han.addObserver(wang);
        han.addObserver(liu);

        han.haveBreakfast();
    }
}
