package mg.itu.framework.utils;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private Map<String, Object> attributs;

    public Session() {
        this.attributs = new HashMap<>();
    }

    public Map<String, Object> getAttributs() {
        return this.attributs;
    }

    public void setAttributs(Map<String, Object> attributs) {
        this.attributs = attributs;
    }

    public void setAttribut(String nom, Object valeur) {
        this.attributs.put(nom, valeur);
    }

    public Object getAttribut(String nom) {
        return this.attributs.get(nom);
    }
}