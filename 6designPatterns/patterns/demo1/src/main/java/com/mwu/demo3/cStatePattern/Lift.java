package com.mwu.demo3.cStatePattern;

public class Lift implements ILift{
    private int state;
    @Override
    public void setState(int state) {
        this.state = state;

    }

    @Override
    public void open() {
        switch (this.state) {
            case OPENING_STATE:
                System.out.println("lift is opening");
                this.setState(CLOSING_STATE);
                break;
            case CLOSING_STATE:
                System.out.println("lift is closing");
                break;
            case RUNNING_STATE:
                System.out.println("lift is running");
                break;
            case STOPPING_STATE:
                System.out.println("lift is stopping");
                break;
        }

    }

    @Override
    public void close() {
        switch (this.state) {
            case OPENING_STATE:
                System.out.println("lift is opening");
                break;
            case CLOSING_STATE:
                System.out.println("lift is closing");
                break;
            case RUNNING_STATE:
                System.out.println("lift is running");
                break;
            case STOPPING_STATE:
                System.out.println("lift is stopping");
                this.setState(OPENING_STATE);
                break;
        }

    }

    @Override
    public void run() {
        switch (this.state) {
            case OPENING_STATE:
                System.out.println("lift is opening");
                break;
            case CLOSING_STATE:
                System.out.println("lift is closing");
                break;
            case RUNNING_STATE:
                System.out.println("lift is running");
                this.setState(STOPPING_STATE);
                break;
            case STOPPING_STATE:
                System.out.println("lift is stopping");
                break;
        }


    }

    @Override
    public void stop() {
        switch (this.state) {
            case OPENING_STATE:
                System.out.println("lift is opening");
                break;
            case CLOSING_STATE:
                System.out.println("lift is closing");
                this.setState(CLOSING_STATE);
                break;
            case RUNNING_STATE:
                System.out.println("lift is running");
                break;
            case STOPPING_STATE:
                System.out.println("lift is stopping");
                break;
        }
    }
}
