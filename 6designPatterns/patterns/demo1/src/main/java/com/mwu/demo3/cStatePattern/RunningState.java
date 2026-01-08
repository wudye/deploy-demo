package com.mwu.demo3.cStatePattern;

public class RunningState extends LiftState {



    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public void run() {
        System.out.println("lift is running");


    }

    @Override
    public void stop() {
        super.context.setLiftState(Context.stoppingState);
        super.context.getLiftState().stop();

    }
}
