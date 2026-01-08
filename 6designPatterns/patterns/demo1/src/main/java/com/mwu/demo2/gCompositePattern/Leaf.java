package com.mwu.demo2.gCompositePattern;

public class Leaf implements ICorp{
    private String name = "";
    private String position = "";
    private int salary = 0;
    public Leaf(String name, String position, int salary) {
        this.name = name;
        this.position = position;
        this.salary = salary;
    }
    @Override
    public String getInfo() {
        String info = "";
        info = info + "leaf name: " + this.name;
        info = info + "leaf position: " + this.position;
        info = info + "leaf salary: " + this.salary;
        return info;


    }
}
