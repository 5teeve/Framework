package mg.itu.framework.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.framework.utils.Utilitaire;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FrontServletController extends HttpServlet {

    private Utilitaire utilitaire;
    private List<Class<?>> listeController = new ArrayList<>();

    @Override
    public void init() {
        utilitaire = new Utilitaire();
        List<String> packagesName = getPackageNames(getParamName());

        if (packagesName.isEmpty()) {
            List<Class<?>> classes = utilitaire.chargerClassePath("all");
            listeController.addAll(utilitaire.findController(classes));
        } else {
            for (String packageName : packagesName) {
                List<Class<?>> classes = utilitaire.chargerClassePath(packageName);
                listeController.addAll(utilitaire.findController(classes));
            }
        }
    }

    private String getParamName() {
        return "packages";
    }

    private List<String> getPackageNames(String paramName) {
        String linePackages = getInitParameter(paramName);
        if (linePackages == null || linePackages.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] splitPackages = linePackages.split(";;");
        return Arrays.asList(splitPackages);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = res.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Bienvenue</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Bienvenue dans ma Framework !</h1>");
            out.println("<p>Cette page est générée dynamiquement par le framework.</p>");

            out.println("<h2>Controllers detectes (" + listeController.size() + ")</h2>");
            if (listeController.isEmpty()) {
                out.println("<p>Aucune classe annotee @Controller n'a ete trouvee.</p>");
            } else {
                out.println("<ul>");
                for (Class<?> controller : listeController) {
                    out.println("<li>" + controller.getName() + "</li>");
                }
                out.println("</ul>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        this.doGet(req, res);
    }
}