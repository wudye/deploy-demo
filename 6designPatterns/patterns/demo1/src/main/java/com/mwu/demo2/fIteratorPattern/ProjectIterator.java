package com.mwu.demo2.fIteratorPattern;

import java.util.ArrayList;

public class ProjectIterator implements IProjectIterator {
    private ArrayList<IProject> projectList = new ArrayList<IProject>();
    private int currentItem = 0;
    public ProjectIterator(ArrayList<IProject> projectList) {
        this.projectList = projectList;

    }
    @Override
    public boolean hasNext() {
        if (currentItem >= projectList.size() || projectList.get(currentItem) == null) {
            return false;
        }
        return true;
    }

    @Override
    public Object next() {
        IProject project = projectList.get(currentItem);
        currentItem++;
        return project;

    }

}
