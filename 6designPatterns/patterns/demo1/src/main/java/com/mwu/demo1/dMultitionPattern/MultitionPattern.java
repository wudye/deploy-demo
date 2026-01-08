package com.mwu.demo1.dMultitionPattern;


import java.util.ArrayList;
import java.util.Random;

public class MultitionPattern {

    private static int maxCount = 3;
    private static ArrayList multiList  = new ArrayList(maxCount);
    private static ArrayList multListInfo = new ArrayList(maxCount);
    private static int count = 0;

    static {
        for (int i = 0; i < maxCount; i++) {
            multiList.add(new MultitionPattern("this is " + i + " instance"));
        }
    }

    private MultitionPattern() {

    }
    private MultitionPattern(String info) {
        multListInfo.add(info);
    }

    public static MultitionPattern getInstance(int index) {
        if (index < 0 || index >= maxCount) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return (MultitionPattern) multiList.get(index);
    }
    public static MultitionPattern getInstance() {
        Random random = new Random();
        count = random.nextInt(maxCount);
        return (MultitionPattern) multiList.get(count);
    }

    public static void  getInfo() {
        System.out.println(multListInfo.get(count));
    }
}
