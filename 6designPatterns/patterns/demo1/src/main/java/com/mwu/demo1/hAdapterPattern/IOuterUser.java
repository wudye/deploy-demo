package com.mwu.demo1.hAdapterPattern;

import java.util.Map;

@SuppressWarnings("all")
public interface IOuterUser {

    public Map getUserBaseInfo() ;
    public Map<String, String> getUserOfficeInfo() ;
    public Map<String, String> getUserHomeInfo() ;
}
