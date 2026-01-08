package com.mwu.demo2.fIteratorPattern;

import java.util.ArrayList;

public class ProjectInfo implements IProject{
    private ArrayList<IProject> projectList = new ArrayList<IProject>();
    private String name = "";
    private int num = 0;
    private int cost = 0;

    public ProjectInfo(String name, int num, int cost) {
        this.name = name;
        this.num = num;
        this.cost = cost;
    }

    @Override
    public void add(String name, int num, int cost) {
        this.projectList.add(new ProjectInfo(name,num,cost));

    }

    @Override
    public String getProjectInfo() {
        String info = "";
        info = info + "project name: " + this.name;
        info = info + "project num: " + this.num;
        info = info + "project cost: " + this.cost;
        return info;
    }

    @Override
    public IProjectIterator iterator() {
        return new ProjectIterator(this.projectList);
    }
}
