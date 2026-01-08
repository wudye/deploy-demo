package com.mwu.demo3.cStatePattern;

public class StateDemo {
    public static void main(String[] args) {
       Context context = new Context();

       context.setLiftState(Context.openningState);
       context.open();
       context.close();

    }
}
