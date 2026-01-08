package com.mwu.demo2.fIteratorPattern;

import java.util.stream.Stream;

public interface IProject {
    public void add(String name,int num,int cost);

    public String getProjectInfo();
    public IProjectIterator iterator();
}
