package mg.itu.framework.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.framework.dto.MethodDTO;
import mg.itu.framework.dto.RequestMapping;
import mg.itu.framework.utils.Utilitaire;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class FrontServletController extends HttpServlet {

    private Utilitaire utilitaire;
    private List<Class<?>> listeController = new ArrayList<>();
    private Map<RequestMapping, MethodDTO> mapMethode = new LinkedHashMap<>();

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

        mapMethode.putAll(utilitaire.findMethod(listeController));
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
        handleRequest(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        handleRequest(req, res);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");

        String url = req.getPathInfo();
        if (url == null || url.trim().isEmpty()) {
            url = "/";
        }
        String httpMethod = req.getMethod();

        try (PrintWriter out = res.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Front Controller</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Front Controller</h1>");
            out.println("<p>Url demandee : " + url + " [" + httpMethod + "]</p>");

            RequestMapping demande = new RequestMapping(url, httpMethod);
            MethodDTO trouve = mapMethode.get(demande);

            if (trouve != null) {
                try {
                    Class<?> controllerClass = trouve.getMethod().getDeclaringClass();
                    Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                    trouve.getMethod().invoke(controllerInstance);

                    out.println("<h3>Mapping trouve</h3>");
                    out.println("<p>" + url + " " + trouve + "</p>");
                } catch (ReflectiveOperationException e) {
                    throw new ServletException("Erreur lors de l'invocation de " + trouve, e);
                }
            } else {
                out.println("<h4>Erreur</h4>");
                out.println("<p>Url inconnue : \"" + url + "\" [" + httpMethod
                        + "]. Cette url n'est associee a aucune methode.</p>");
                out.println("<h3>Urls connues :</h3>");
                if (mapMethode.isEmpty()) {
                    out.println("<p>Aucune url connue.</p>");
                } else {
                    out.println("<ul>");
                    for (Map.Entry<RequestMapping, MethodDTO> entry : mapMethode.entrySet()) {
                        out.println("<li>" + entry.getKey().getUrl() + " [" + entry.getKey().getMethod() + "] "
                                + entry.getValue() + "</li>");
                    }
                    out.println("</ul>");
                }
            }

            out.println("</body>");
            out.println("</html>");
        }
    }
}