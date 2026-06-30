package mg.itu.framework.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import mg.itu.framework.dto.MethodDTO;
import mg.itu.framework.dto.RequestMapping;
import mg.itu.framework.utils.Utilitaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebListener
public class FrameworkInitializerListener implements ServletContextListener {

    public static final String PACKAGES_PARAM = "packages";
    public static final String CONTROLLERS_ATTRIBUTE = "mg.itu.framework.listeController";
    public static final String MAPPING_ATTRIBUTE = "mg.itu.framework.mapMethode";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Utilitaire utilitaire = new Utilitaire();

        List<String> packagesName = Utilitaire.parsePackages(context.getInitParameter(PACKAGES_PARAM));
        List<Class<?>> listeController = new ArrayList<>();

        if (packagesName.isEmpty()) {
            List<Class<?>> classes = utilitaire.chargerClassePath("all");
            listeController.addAll(utilitaire.findController(classes));
        } else {
            for (String packageName : packagesName) {
                List<Class<?>> classes = utilitaire.chargerClassePath(packageName);
                listeController.addAll(utilitaire.findController(classes));
            }
        }

        Map<RequestMapping, MethodDTO> mapMethode = utilitaire.findMethod(listeController);

        context.setAttribute(CONTROLLERS_ATTRIBUTE, listeController);
        context.setAttribute(MAPPING_ATTRIBUTE, mapMethode);

        System.out.println("FrameworkInitializerListener : " + listeController.size()
                + " controleur(s) et " + mapMethode.size() + " route(s) charges au demarrage de l'application.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.removeAttribute(CONTROLLERS_ATTRIBUTE);
        context.removeAttribute(MAPPING_ATTRIBUTE);
    }
}
