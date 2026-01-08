package com.mwu.demo1.efactoryMethodPattern;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

@SuppressWarnings("all")
public class ClassUtils {
    
    public static List<Class> getAllClassByInterface(Class c) {
        List<Class> result = new ArrayList<Class>();
        try {

            if (c.isInterface()) {
                String packageName = c.getPackage().getName();
                List<Class> allClass = getClasses(packageName);
                for (int i = 0; i < allClass.size(); i++) {
                    if (c.isAssignableFrom(allClass.get(i))) {
                        result.add(allClass.get(i));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static List<Class> getClasses(String packageName) throws IOException, ClassNotFoundException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        URL url = classLoader.getResource(path);
        Enumeration<URL> result = classLoader.getResources(path);
        List<File> dir = new ArrayList<>();

        while (result.hasMoreElements()) {
            URL url1 = result.nextElement();
            dir.add(new File(url1.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory: dir) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;


    }

    private static Collection<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' +
                        file.getName().substring(0, file.getName().length() - 6)));
                }
        }
        return classes;
    }
}
