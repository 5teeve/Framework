package mg.itu.framework.dto;

import java.lang.reflect.Method;

public class MethodDTO {
    private String url;
    private Class<?> clazz;
    private Method method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return url + " " + clazz.getSimpleName() + " -> " + method.getName();
    }
}