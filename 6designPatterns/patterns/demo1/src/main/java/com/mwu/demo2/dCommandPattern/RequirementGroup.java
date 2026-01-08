package com.mwu.demo2.dCommandPattern;

public class RequirementGroup extends Group{
    @Override
    public void find() {
        System.out.println("find requirement");

    }

    @Override
    public void add() {
        System.out.println("add requirement");

    }

    @Override
    public void delete() {
        System.out.println("delete requirement");


    }

    @Override
    public void modify() {
        System.out.println("modify requirement");


    }

    @Override
    public void plan() {
        System.out.println("plan requirement");




    }
}
