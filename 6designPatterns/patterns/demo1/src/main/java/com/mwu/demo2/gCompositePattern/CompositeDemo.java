package com.mwu.demo2.gCompositePattern;

import java.util.ArrayList;

public class CompositeDemo {
    public static void main(String[] args) {
        IRoot ceo =    new Root("CEO", "President", 500000);


        IBranch cto = new Branch("CTO", "Chief Technology Officer", 400000);
        IBranch cfo = new Branch("CFO", "Chief Financial Officer", 400000);
        IBranch productManager = new Branch("Product Manager", "Product Manager", 300000);
        IBranch salesManager = new Branch("Sales Manager", "Sales Manager", 300000);
        Leaf sales = new Leaf("Sales", "Sales", 100000);
        Leaf product = new Leaf("Product", "Product", 100000);
        Leaf finance = new Leaf("Finance", "Finance", 100000);


        ceo.add(cto);
        ceo.add(cfo);
        cto.add(productManager);
        cto.add(salesManager);
        productManager.add(product);
        salesManager.add(sales);
        cfo.add(finance);
        getAllSubordinateInfo(ceo.getSubordinateInfo());



    }

    private static void getAllSubordinateInfo(ArrayList subordinateInfo) {
        int length = subordinateInfo.size();
        for (int i = 0; i < length; i++) {
            Object object = subordinateInfo.get(i);
            if (object instanceof IBranch) {
                IBranch branch = (IBranch) object;
                System.out.println(branch.getInfo());
                getAllSubordinateInfo(branch.getSubordinateInfo());
            } else if (object instanceof ICorp) {
                ICorp leaf = (ICorp) object;
                System.out.println(leaf.getInfo());
            }
        }
    }
}
