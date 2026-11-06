package mg.itu.framework.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class ControllerRegistry {
    private final Map<Class<?>, Object> instances = new LinkedHashMap<>();

    public Object getOrCreate(Class<?> controllerClass) throws ReflectiveOperationException {
        Object instance = instances.get(controllerClass);
        if (instance == null) {
            instance = controllerClass.getDeclaredConstructor().newInstance();
            instances.put(controllerClass, instance);
        }
        return instance;
    }
}
