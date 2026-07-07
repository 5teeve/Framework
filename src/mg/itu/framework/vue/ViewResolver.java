package mg.itu.framework.vue;

public class ViewResolver {
    private String prefix;
    private String suffix;

    public ViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String resolve(String viewName) {
        return prefix + viewName + suffix;
    }

    // getters/setters
}