package com.mwu.demo3.bVisitorPattern;

public class Visitor implements IVisitor{
    @Override
    public void visit(CommonEmployee commonEmployee) {
        System.out.println(this.getCommonEmployee(commonEmployee));
    }

    private String getCommonEmployee(CommonEmployee commonEmployee) {
        String basicInfo = this.getBasicInfo(commonEmployee);
        String otherInfo ="job" + commonEmployee.getJob() + "\t";
        return basicInfo + otherInfo;
    }

    private String getBasicInfo(Employee commonEmployee) {
        String info = "name:" + commonEmployee.getName() + "\t";
        info += "sex:" + commonEmployee.getSex() + "\t";
        info += "salary:" + commonEmployee.getSalary() + "\t";
        return info;
    }

    @Override
    public void visit(Manager manager) {
        System.out.println(this.getManagerInfo(manager));
    }

    private String  getManagerInfo(Manager manager) {
        String basicInfo = this.getBasicInfo(manager);
        String otherInfo = "performance:" + manager.getPerformance() + "\t";
        return basicInfo + otherInfo;
    }
}
