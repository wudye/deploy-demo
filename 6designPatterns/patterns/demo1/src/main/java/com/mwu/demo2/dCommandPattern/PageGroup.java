package com.mwu.demo2.dCommandPattern;

public class PageGroup extends Group{
    @Override
    public void find() {
        System.out.println("find page");

    }

    @Override
    public void add() {
        System.out.println("add page");


    }

    @Override
    public void delete() {
        System.out.println("delete page");



    }

    @Override
    public void modify() {
        System.out.println("modify page");



    }

    @Override
    public void plan() {
        System.out.println("plan page");




    }
}
