package com.mwu.demo1.hAdapterPattern;

import java.util.HashMap;
import java.util.Map;

public class OuterUser implements IOuterUser{
    @Override
    public Map getUserBaseInfo() {
        HashMap map = new HashMap();
        map.put("userName", "a");
        map.put("mobileNumber", "b");
        map.put("jobPosition", "c");
        return map;
    }

    @Override
    public Map<String, String> getUserOfficeInfo() {
        HashMap map = new HashMap();
        map.put("officeTelNumber", "aoffice");
        map.put("officeAddress", "boffice");
        return map;
    }

    @Override
    public Map<String, String> getUserHomeInfo() {
        HashMap map = new HashMap();
        map.put("homeTelNumber", "ahome");
        map.put("homeAddress", "bhome");
        return map;
    }
}
