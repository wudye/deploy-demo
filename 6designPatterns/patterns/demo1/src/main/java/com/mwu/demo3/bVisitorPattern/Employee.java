package com.mwu.demo3.bVisitorPattern;

public abstract class Employee {
    public final static int MALE = 0;
    public final static int FEMALE = 1;

    private String name;
    private int sex;
    private String salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public final void report() {
        String info = "";

        info = info + "name:" + this.getName() + ",";
        info = info + "sex:" + (this.getSex() == MALE ? "male" : "female") + ",";
        info = info + "salary:" + this.getSalary();

        System.out.println(info);
    }

    public abstract void accept(IVisitor visitor);
}

