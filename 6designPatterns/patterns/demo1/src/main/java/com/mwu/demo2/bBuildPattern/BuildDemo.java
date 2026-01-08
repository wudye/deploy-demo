package com.mwu.demo2.bBuildPattern;

import java.util.ArrayList;

public class BuildDemo {
    public static void main(String[] args) {
        BenzModel benz = new BenzModel();
        ArrayList<String> sequence = new ArrayList<>();
        sequence.add("start");
        sequence.add("alarm");
        sequence.add("stop");
        benz.setSequence(sequence);
        benz.run();


        BenzBuilder benzBuilder = new BenzBuilder();
        benzBuilder.setSequence(sequence);
        benzBuilder.getCarModel().run();

        Direcrtor direcrtor = new Direcrtor();
        direcrtor.getABenzModel().run();

    }
}
