package com.mwu.demo3.cStatePattern;

public class ClosingState extends LiftState {



    @Override
    public void open() {
        super.context.setLiftState(Context.openningState);
        super.context.getLiftState().open();
    }

    @Override
    public void close() {
        System.out.println("lift is closing");


    }

    @Override
    public void run() {

    }

    @Override
    public void stop() {

    }
}
