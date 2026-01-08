package com.mwu.demo2.gCompositePattern;

import java.util.ArrayList;

@SuppressWarnings("all")
public class Branch  implements IBranch, ICorp{
    private ArrayList subordinateList = new ArrayList();
    private String name = "";
    private String position = "";
    private int salary = 0;

    public Branch(String name, String position, int salary) {
        this.name = name;
        this.position = position;
        this.salary = salary;
    }


    @Override
    public String getInfo() {
        String info = "";
        info = info + "branch name: " + this.name;
        info = info + "branch position: " + this.position;
        info = info + "branch salary: " + this.salary;
        return info;
    }

    @Override
    public void add(ICorp leaf) {
        this.subordinateList.add(leaf);

    }

    @Override
    public void add(IBranch branch) {
        this.subordinateList.add(branch);

    }

    @Override
    public ArrayList getSubordinateInfo() {
        return this.subordinateList;
    }
}
