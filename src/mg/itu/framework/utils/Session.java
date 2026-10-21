package mg.itu.framework.utils;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Session implements Serializable {

    private static final String CLE_INSTANCE = "mg.itu.framework.utils.Session.INSTANCE";

    private final Map<String, Object> attributs = new HashMap<>();

    private Session() {
    }

    public static Session getInstance(HttpServletRequest req) {
        HttpSession http = req.getSession(true);
        Session s = (Session) http.getAttribute(CLE_INSTANCE);
        if (s == null) {
            s = new Session();
            http.setAttribute(CLE_INSTANCE, s);
        }
        s.charger(http);
        return s;
    }

    private void charger(HttpSession http) {
        attributs.clear();
        Enumeration<String> noms = http.getAttributeNames();
        while (noms.hasMoreElements()) {
            String nom = noms.nextElement();
            if (!CLE_INSTANCE.equals(nom)) {
                attributs.put(nom, http.getAttribute(nom));
            }
        }
    }

    public void sauvegarder(HttpServletRequest req) {
        HttpSession http = req.getSession(true);

        Set<String> existants = new HashSet<>();
        Enumeration<String> noms = http.getAttributeNames();
        while (noms.hasMoreElements()) {
            existants.add(noms.nextElement());
        }
        existants.remove(CLE_INSTANCE);

        for (String nom : existants) {
            if (!attributs.containsKey(nom)) {
                http.removeAttribute(nom);
            }
        }
        for (Map.Entry<String, Object> e : attributs.entrySet()) {
            if (http.getAttribute(e.getKey()) != e.getValue()) {
                http.setAttribute(e.getKey(), e.getValue());
            }
        }
    }

    public Map<String, Object> getAttributs() {
        return this.attributs;
    }

    public void setAttributs(Map<String, Object> nouveaux) {
        this.attributs.clear();
        if (nouveaux != null) {
            this.attributs.putAll(nouveaux);
        }
    }

    public void setAttribut(String nom, Object valeur) {
        this.attributs.put(nom, valeur);
    }

    public Object getAttribut(String nom) {
        return this.attributs.get(nom);
    }

    public void removeAttribut(String nom) {
        this.attributs.remove(nom);
    }

    public void vider() {
        this.attributs.clear();
    }
}