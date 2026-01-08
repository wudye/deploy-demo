package com.mwu.demo1.efactoryMethodPattern;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class HumanMain {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Human humanFactory = HumanFactory.getHuman(HumanA.class);
        humanFactory.laugh();
        humanFactory.cry();
        humanFactory.talk();
        Human human = HumanFactory.getHuman();
        human.laugh();
        human.cry();
        human.talk();



        Class<String> s0Class = String.class;
        System.out.println(s0Class);
        String s1 = "123";

        Class<?> s1Class = s1.getClass();
        System.out.println(s1Class);

        Class<?> s1Class1 = Class.forName("java.lang.String");
        System.out.println(s1Class1);

        System.out.println(s1Class1.getSimpleName() + "    " + s1Class1.getName());


        Constructor<?>[] constructors = s1Class1.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor);
        }
        Constructor<?> constructor = s1Class.getConstructor(String.class);

        Objects new1 = (Objects) constructor.newInstance("Hello");
        System.out.println(new1);

        Method method = s1Class.getMethod("length", String.class);
        method.invoke(s1Class, "123");
    }

}
