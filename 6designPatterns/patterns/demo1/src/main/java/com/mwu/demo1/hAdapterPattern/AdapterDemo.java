package com.mwu.demo1.hAdapterPattern;

public class AdapterDemo {
    public static void main(String[] args) {
        IUserInfo userInfo = new OuterUserInfo();
        for(int i = 0; i < 10; i++) {
            System.out.println(userInfo.getHomeAddress());
        }
    }
}

