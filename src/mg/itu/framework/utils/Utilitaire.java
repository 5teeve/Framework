package mg.itu.framework.utils;

import mg.itu.framework.annotation.Controller;
import mg.itu.framework.annotation.Json;
import mg.itu.framework.annotation.UrlMapping;
import mg.itu.framework.dto.MethodDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.framework.dto.RequestMapping;
import mg.itu.framework.exception.RouteDejaDefinieException;
import mg.itu.framework.vue.ModelAndView;
import mg.itu.framework.vue.ViewResolver;

public class Utilitaire {

    public Utilitaire() {
    }

    public static List<String> parsePackages(String linePackages) {
        if (linePackages == null || linePackages.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(linePackages.split(";;"));
    }

    public static Object[] resolveArguments(Method method, HttpServletRequest req) throws Exception {
        java.lang.reflect.Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Class<?> type = params[i].getType();
            String name = params[i].getName();

            if (isSimpleType(type)) {
                //paramètre simple
                String raw = req.getParameter(name);
                args[i] = convertValue(raw, type);
            } else {
                // objet custom
                Object obj = type.getDeclaredConstructor().newInstance();
                for (java.lang.reflect.Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    String raw = req.getParameter(field.getName());
                    if (raw != null) {
                        field.set(obj, convertValue(raw, field.getType()));
                    }
                }
                args[i] = obj;
            }
        }
        return args;
    }

    private static boolean isSimpleType(Class<?> type) {
        return type == String.class
                || type == int.class    || type == Integer.class
                || type == long.class   || type == Long.class
                || type == double.class || type == Double.class
                || type == float.class  || type == Float.class
                || type == boolean.class|| type == Boolean.class;
    }

    private static Object convertValue(String value, Class<?> type) {
        if (value == null) return null;
        if (type == String.class)                    return value;
        if (type == int.class || type == Integer.class)  return Integer.parseInt(value);
        if (type == long.class || type == Long.class)    return Long.parseLong(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        if (type == float.class || type == Float.class)  return Float.parseFloat(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        return null;
    }

    public static Object invokeMethod(MethodDTO dto, HttpServletRequest req) throws Exception {
        Method method = dto.getMethod();
        Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
        Object[] args = resolveArguments(method, req);
        return method.invoke(instance, args);
    }

    public static void render(Method method, Object result, ViewResolver vr,
            HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (method.isAnnotationPresent(Json.class)) {
            res.setContentType("application/json");
            res.getWriter().write(new ObjectMapper().writeValueAsString(result));
            return;
        }
        
        if (result instanceof String) {
            req.getRequestDispatcher(vr.resolve((String) result))
                    .forward(req, res);
        } else if (result instanceof ModelAndView) {
            ModelAndView mav = (ModelAndView) result;
            if (mav.getData() != null) {
                mav.getData().forEach(req::setAttribute);
            }
            req.getRequestDispatcher(vr.resolve(mav.getView()))
                    .forward(req, res);
        } else {
            throw new IllegalArgumentException(
                    "La methode doit retourner un String ou un ModelAndView");
        }
    }

    public Map<RequestMapping, MethodDTO> findMethod(List<Class<?>> classes) {
        Map<RequestMapping, MethodDTO> methodes = new LinkedHashMap<>();
        Set<Integer> hashCodesVus = new HashSet<>();

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(UrlMapping.class)) {
                    UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);

                    MethodDTO dto = new MethodDTO();
                    dto.setClazz(clazz);
                    dto.setMethod(method);

                    RequestMapping key = new RequestMapping(urlMapping.url(), urlMapping.method());
                    int hash = key.hashCode();

                    if (hashCodesVus.contains(hash)) {
                        throw new RouteDejaDefinieException(
                                "Route en doublon : \"" + key.getUrl() + "\" [" + key.getMethod()
                                        + "] (hashCode=" + hash + ") est deja associee a une methode existante ;"
                                        + " impossible de l'associer aussi a " + dto + ".");
                    }

                    hashCodesVus.add(hash);

                    methodes.put(key, dto);
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