package com.mwu.demo1.hAdapterPattern;

import java.util.Map;

public class OuterUserInfo extends OuterUser implements IUserInfo{

    private Map baseInfo = super.getUserBaseInfo();
    private Map officeInfo = super.getUserOfficeInfo();
    private Map homeInfo = super.getUserHomeInfo();

    @Override
    public String getUserName() {
        return "";
    }

    @Override
    public String getHomeAddress() {
        String homeAddress = this.homeInfo.get("homeAddress").toString();

        System.out.println(homeAddress);
        return "";
    }

    @Override
    public String getMobileNumber() {
        return "";
    }

    @Override
    public String getOfficeTelNumber() {
        String officeTelNumber = this.officeInfo.get("officeTelNumber").toString();
        System.out.println(officeTelNumber);
        return "";
    }

    @Override
    public String getJobPosition() {
        String jobPosition = this.baseInfo.get("jobPosition").toString();
        System.out.println(jobPosition);
        return "";
    }

    @Override
    public String getHomeTelNumber() {
        return "";
    }
}
