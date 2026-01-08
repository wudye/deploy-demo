package com.mwu.demo2.hOberserverPattern;

import java.util.ArrayList;

public class Han implements Observable{
    private ArrayList<Observer> observerList = new ArrayList<Observer>();


    @Override
    public void addObserver(Observer observer) {
        this.observerList.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        this.observerList.remove(observer);

    }

    @Override
    public void notifyObservers(String context) {
        for (Observer observer : observerList) {
            observer.update(context);
        }

    }

    public void haveBreakfast() {
        System.out.println("Han have breakfast");
        this.notifyObservers("Han have breakfast");
    }
    public void haveFun() {
        System.out.println("Han have fun");
        this.notifyObservers("Han have fun");
    }
}
