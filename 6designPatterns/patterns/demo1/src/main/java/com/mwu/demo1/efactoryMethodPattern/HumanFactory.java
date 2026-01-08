package com.mwu.demo1.efactoryMethodPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HumanFactory {


    private static HashMap<String, Human> humanHashMap = new HashMap<>();

    public static Human getHuman(Class t) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        Human human = null;
        try {
            if (humanHashMap.containsKey(t.getSimpleName())) {
                human = humanHashMap.get(t.getSimpleName());
            } else {
                human = (Human) t.getDeclaredConstructor().newInstance();
                humanHashMap.put(t.getSimpleName(), human);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return human;
    }

    public static Human getHuman() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Human human = null;
        List<Class> classes = ClassUtils.getAllClassByInterface(Human.class);
        Random random = new Random();
        int i = random.nextInt(classes.size());
        human = getHuman(classes.get(i));
        return human;

    }
}
