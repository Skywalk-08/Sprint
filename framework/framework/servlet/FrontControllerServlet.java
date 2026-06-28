package framework.servlet;

import framework.annotations.Controller;
import framework.annotations.UrlMapping;
import framework.utils.PackageScanner;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * FrontControllerServlet - Sprint 2
 *
 * Nouveautés par rapport au Sprint 1 :
 *  - Au démarrage, scan toutes les méthodes annotées @UrlMapping
 *    dans les classes @Controller du package configuré
 *  - Construit automatiquement la Map<String, Mapping> urlMappings
 *  - GET sur "/" → affiche tous les mappings trouvés (page de debug)
 *  - GET sur "/xxx" → dispatche vers la méthode correspondante
 */
public class FrontControllerServlet extends HttpServlet {

    // URL (ex: "/home") → Mapping(className, methodName)
    private Map<String, Mapping> urlMappings = new HashMap<>();

    // Sprint 1 : liste des classes @Controller trouvées
    private List<String> listController = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        // Lire le package depuis web.xml (init-param ou context-param)
        String controllerPackage = getServletConfig().getInitParameter("controller_package_name");
        if (controllerPackage == null || controllerPackage.isBlank()) {
            controllerPackage = getServletContext().getInitParameter("controllerPackage");
        }

        if (controllerPackage == null || controllerPackage.isBlank()) {
            throw new ServletException(
                "[FRAMEWORK] Paramètre 'controller_package_name' manquant dans web.xml !"
            );
        }

        try {
            scanUrlMappings(controllerPackage);
        } catch (Exception e) {
            throw new ServletException("[FRAMEWORK] Erreur lors du scan des @UrlMapping : " + e.getMessage(), e);
        }
    }

    /**
     * Sprint 2 - Cœur du scan :
     * 1. Trouve toutes les classes @Controller dans le package
     * 2. Pour chaque méthode annotée @UrlMapping, enregistre le mapping
     * 3. Lève une exception si deux méthodes ont la même URL
     */
    private void scanUrlMappings(String packageName) throws Exception {
        List<Class<?>> allClasses = PackageScanner.findClassesInPackage(packageName);

        for (Class<?> clazz : allClasses) {

            // Sprint 1 : ne traiter que les classes @Controller
            if (!clazz.isAnnotationPresent(Controller.class)) continue;

            listController.add(clazz.getName());

            // Sprint 2 : parcourir les méthodes de la classe
            for (Method method : clazz.getDeclaredMethods()) {

                if (!method.isAnnotationPresent(UrlMapping.class)) continue;

                String url = method.getAnnotation(UrlMapping.class).value();

                // Vérification : URL dupliquée ?
                if (urlMappings.containsKey(url)) {
                    Mapping existing = urlMappings.get(url);
                    throw new ServletException(
                        "[FRAMEWORK] URL dupliquée : '" + url + "' est déjà mappée sur "
                        + existing.getClassName() + "#" + existing.getMethodName()
                        + " — conflit avec " + clazz.getName() + "#" + method.getName()
                    );
                }

                Mapping mapping = new Mapping(clazz.getName(), method.getName());
                urlMappings.put(url, mapping);

                System.out.println("[FRAMEWORK] @UrlMapping : " + url + " → " + mapping);
            }
        }

        System.out.println("[FRAMEWORK] Scan terminé : "
            + listController.size() + " controller(s), "
            + urlMappings.size() + " mapping(s) trouvé(s).");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo(); // ex: "/home" ou null

        // ── Page d'accueil : afficher tous les @UrlMapping ────────────────
        if (pathInfo == null || pathInfo.equals("/")) {
            afficherTousLesMappings(out, request.getContextPath());
            return;
        }

        // ── Chercher le mapping pour cette URL ────────────────────────────
        Mapping mapping = urlMappings.get(pathInfo);

        if (mapping == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("<html><body>");
            out.println("<h2 style='color:red'>❌ 404 — Aucun @UrlMapping pour : <code>"
                + pathInfo + "</code></h2>");
            out.println("<p><a href='" + request.getContextPath() + "/'>← Retour à la liste</a></p>");
            out.println("</body></html>");
            return;
        }

        // ── Dispatcher vers la méthode correspondante ─────────────────────
        try {
            Class<?> controllerClass = Class.forName(mapping.getClassName());
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            Method method = controllerClass.getDeclaredMethod(mapping.getMethodName());

            // Exécution de la méthode
            Object result = method.invoke(controllerInstance);

            // Afficher le résultat
            out.println("<html><head><meta charset='UTF-8'>");
            out.println("<title>Résultat</title></head><body>");
            out.println("<h2>✅ URL : <code>" + pathInfo + "</code></h2>");
            out.println("<table border='1' cellpadding='8' style='border-collapse:collapse'>");
            out.println("<tr><th>Classe</th><th>Méthode</th><th>Résultat</th></tr>");
            out.println("<tr>");
            out.println("<td>" + mapping.getClassName() + "</td>");
            out.println("<td>" + mapping.getMethodName() + "()</td>");
            out.println("<td>" + (result != null ? result.toString() : "<i>void</i>") + "</td>");
            out.println("</tr></table>");
            out.println("<br><a href='" + request.getContextPath() + "/'>← Retour à la liste</a>");
            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<h2 style='color:red'>Erreur lors de l'exécution</h2>");
            out.println("<p>" + e.getClass().getName() + " : " + e.getMessage() + "</p>");
        }
    }

    /**
     * Page de debug : affiche tous les @UrlMapping scannés
     */
    private void afficherTousLesMappings(PrintWriter out, String contextPath) {
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<title>Framework — @UrlMapping</title>");
        out.println("<style>");
        out.println("  body { font-family: Arial, sans-serif; padding: 24px; background: #f9f9f9; }");
        out.println("  h1 { color: #2c3e50; }");
        out.println("  table { border-collapse: collapse; width: 100%; background: white; }");
        out.println("  th { background: #2c3e50; color: white; padding: 10px 14px; text-align: left; }");
        out.println("  td { border: 1px solid #ddd; padding: 8px 14px; }");
        out.println("  tr:nth-child(even) { background: #f2f2f2; }");
        out.println("  code { background: #eee; padding: 2px 6px; border-radius: 3px; }");
        out.println("  a { color: #2980b9; }");
        out.println("  .badge { background: #27ae60; color: white; border-radius: 12px;");
        out.println("           padding: 2px 10px; font-size: 0.85em; }");
        out.println("</style></head><body>");

        out.println("<h1>🔍 Méthodes annotées <code>@UrlMapping</code></h1>");
        out.println("<p>");
        out.println("  <span class='badge'>" + listController.size() + " controller(s)</span>&nbsp;");
        out.println("  <span class='badge'>" + urlMappings.size() + " mapping(s)</span>");
        out.println("</p>");

        if (urlMappings.isEmpty()) {
            out.println("<p style='color:orange'>⚠️ Aucun @UrlMapping trouvé.</p>");
        } else {
            out.println("<table>");
            out.println("<tr><th>URL</th><th>Classe Controller</th><th>Méthode</th><th>Test</th></tr>");

            // Trier par URL pour lisibilité
            List<String> urls = new ArrayList<>(urlMappings.keySet());
            Collections.sort(urls);

            for (String url : urls) {
                Mapping m = urlMappings.get(url);
                out.println("<tr>");
                out.println("<td><code>" + url + "</code></td>");
                out.println("<td>" + m.getClassName() + "</td>");
                out.println("<td><code>" + m.getMethodName() + "()</code></td>");
                out.println("<td><a href='" + contextPath + url + "'>▶ Tester</a></td>");
                out.println("</tr>");
            }

            out.println("</table>");
        }

        out.println("</body></html>");
    }

    // Getter Sprint 1 conservé
    public List<String> getListController() {
        return listController;
    }
}
