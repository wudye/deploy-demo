package com.mwu.demo2.gCompositePattern;

import java.util.ArrayList;

public interface IRoot {
    public String getInfo();
    public void add(ICorp leaf);
    public void add(IBranch branch);
    public ArrayList getSubordinateInfo();

}
