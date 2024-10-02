package cn.edu.engine.qvog.executor;

import cn.edu.engine.qvog.engine.dsl.IQueryable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryLoader {
    public static List<Class<?>> load(String[] libPaths, String packageName) {
        var queries = loadQueryLibraries(libPaths).stream().filter(clazz ->
                clazz.getPackageName().contains(packageName) && IQueryable.class.isAssignableFrom(clazz)
        ).toList();

        // remove duplicates
        Set<String> queryNames = new HashSet<>();
        List<Class<?>> uniqueQueries = new ArrayList<>();
        for (var query : queries) {
            if (queryNames.add(query.getCanonicalName())) {
                uniqueQueries.add(query);
            }
        }

        return uniqueQueries;
    }

    private static List<Class<?>> loadQueryLibraries(String[] libPaths) {
        List<Class<?>> queries = new ArrayList<>();
        for (var libPath : libPaths) {
            Path path = Path.of(libPath);
            if (Files.isRegularFile(path) && path.toString().endsWith(".jar")) {
                queries.addAll(loadQueryLibrary(path));
            } else if (Files.isDirectory(path)) {
                try (var stream = Files.list(path)) {
                    stream.forEach(file -> {
                        if (file.toString().endsWith(".jar")) {
                            queries.addAll(loadQueryLibrary(file));
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Error loading library: " + e.getMessage());
                }
            }
        }
        return queries;
    }

    private static List<Class<?>> loadQueryLibrary(Path path) {
        // get all classes in the jar
        System.out.println("Loading library: " + path.toAbsolutePath());
        try {
            return JarLoader.extractClassesFromJar(path.toFile());
        } catch (IOException e) {
            System.err.println("Error loading library: " + e.getMessage());
        }

        return new ArrayList<>();
    }
}
