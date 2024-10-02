package cn.edu.engine.qvog.executor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader {
    public static List<Class<?>> extractClassesFromJar(File file) throws IOException {
        List<String> classNames = getClassNamesFromJarFile(file);
        List<Class<?>> classes = new ArrayList<>();
        try (URLClassLoader cl = URLClassLoader.newInstance(new URL[]{ new URL("jar:file:" + file + "!/") })) {
            for (String name : classNames) {
                Class<?> clazz = cl.loadClass(name); // Load the class by its name
                classes.add(clazz);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    public static List<String> getClassNamesFromJarFile(File file) throws IOException {
        List<String> classNames = new ArrayList<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                }
            }
            return classNames;
        }
    }
}