package com.mwu.demo2.gCompositePattern;

import java.util.ArrayList;

@SuppressWarnings("all")
public class Root implements IRoot{

    private ArrayList subordinateList = new ArrayList();
    private String name = "";
    private String position = "";
    private int salary = 0;

    public Root(String name, String position, int salary) {
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    @Override
    public void add(IBranch branch) {
        this.subordinateList.add(branch);
    }

    @Override
    public String getInfo() {
        String info = "";

        info = info + "name: " + this.name;
        info = info + "position: " + this.position;
        info = info + "salary: " + this.salary;
        return info;
    }

    @Override
    public void add(ICorp leaf) {
        this.subordinateList.add(leaf);
    }

    public void display(int depth) {
        System.out.println(this.getInfo());
    }




    @Override
    public ArrayList getSubordinateInfo() {
        return this.subordinateList;
    }
}
