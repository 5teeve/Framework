package mg.itu.framework.utils;

import mg.itu.framework.annotation.Controller;
import mg.itu.framework.annotation.UrlMapping;
import mg.itu.framework.dto.MethodDTO;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utilitaire {

    public Utilitaire() {
    }

    public Map<String, MethodDTO> findMethod(List<Class<?>> classes) {
        Map<String, MethodDTO> methodes = new LinkedHashMap<>();

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(UrlMapping.class)) {
                    UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);

                    MethodDTO dto = new MethodDTO();
                    dto.setClazz(clazz);
                    dto.setMethod(method);

                    methodes.put(urlMapping.url(), dto);
                }
            }
        }

        return methodes;
    }

    public List<Class<?>> chargerClassePath(String packageName) {
        List<Class<?>> classes = new ArrayList<>();

        boolean parcoursComplet = packageName == null
                || packageName.trim().isEmpty()
                || packageName.trim().equalsIgnoreCase("all");

        String path = parcoursComplet ? "" : packageName.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);

        if (resource == null) {
            return classes;
        }

        try {
            String decodedPath = URLDecoder.decode(resource.getFile(), "UTF-8");
            File directory = new File(decodedPath);

            if (directory.exists()) {
                explorerDossier(directory, parcoursComplet ? "" : packageName, classes);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return classes;
    }

    private void explorerDossier(File directory, String packageName, List<Class<?>> classes) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String sousPackage = packageName.isEmpty()
                        ? file.getName()
                        : packageName + "." + file.getName();
                explorerDossier(file, sousPackage, classes);
            } else if (file.getName().endsWith(".class")) {
                String simpleName = file.getName().substring(0, file.getName().length() - 6);
                String className = packageName.isEmpty() ? simpleName : packageName + "." + simpleName;
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
                    System.err.println("Utilitaire: impossible de charger " + className + " (" + e + ")");
                }
            }
        }
    }

    public List<Class<?>> findController(List<Class<?>> classes) {
        List<Class<?>> controllers = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.add(clazz);
            }
        }
        return controllers;
    }
}