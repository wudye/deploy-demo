package com.mwu.demo2.fIteratorPattern;

import java.util.ArrayList;

public class IteratorDemo {
    public static void main(String[] args) {
        ArrayList<IProject> projects = new ArrayList<>();

        projects.add(new ProjectInfo("project1", 100, 10000));
        projects.add(new ProjectInfo("project2", 200, 20000));
        projects.add(new ProjectInfo("project3", 300, 30000));

        for (int i = 4 ; i < 100; i++) {
            projects.add(new ProjectInfo("project" + i, i * 100, i * 10000));
        }

        IProjectIterator  projectIterator = new ProjectIterator(projects);
        while (projectIterator.hasNext()) {
            IProject project = (IProject) projectIterator.next();
            System.out.println(project.getProjectInfo());
        }
    }
}
