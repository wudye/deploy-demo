package com.mwu.demo3.bVisitorPattern;

import java.util.ArrayList;
import java.util.List;

public class VisitorDemo {
    public static void main(String[] args) {
        List<Employee> list = new ArrayList<>();
        CommonEmployee commonEmployee = new CommonEmployee();
        commonEmployee.setJob("common");
        commonEmployee.setName("common");
        commonEmployee.setSalary(String.valueOf(1000));
        list.add(commonEmployee);
        Manager manager = new Manager();
        manager.setName("manager");
        manager.setPerformance("good");
        manager.setSalary(String.valueOf(2000));
        list.add(manager);
        for (Employee employee : list) {
            employee.accept(new Visitor());
        }

    }
}
