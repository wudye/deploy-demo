package com.mwu.demo3.cStatePattern;

public class OpenningState extends LiftState{


    @Override
    public void open() {
        super.context.setLiftState(Context.closeingState);

        super.context.getLiftState().close();
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
