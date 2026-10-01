
package framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import framework.annotations.WebApi;
import framework.binding.BindingException;
import framework.binding.ParameterBinder;
import framework.routing.Mapping;
import framework.routing.UrlMethod;
import framework.utils.JsonUtil;
import framework.utils.ModelView;

public class FrontControllerServlet extends HttpServlet {

    private Map<UrlMethod, Mapping> routes;
    private String viewPrefix;
    private String viewSuffix;

    @Override
    public void init() throws ServletException {
        loadParams();
    }

    private void loadParams() {
        Map<UrlMethod, Mapping> ctxRoutes = (Map<UrlMethod, Mapping>) getServletContext()
                .getAttribute("routesWithMethod");
        if (ctxRoutes == null) {
            throw new IllegalArgumentException("La map 'routesWithMethod' est introuvable dans le contexte.");
        }
        this.routes = ctxRoutes;

        this.viewPrefix = getServletContext().getInitParameter("view_prefix");
        if (this.viewPrefix == null) {
            this.viewPrefix = "/WEB-INF/views/";
        }
        this.viewSuffix = getServletContext().getInitParameter("view_suffix");
        if (this.viewSuffix == null) {
            this.viewSuffix = ".jsp";
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        String url = requestURI.substring(contextPath.length());
        String httpMethod = req.getMethod();

        res.setContentType("text/html; charset=UTF-8");

        boolean api = false;
        try {
            UrlMethod key = new UrlMethod(url, httpMethod);

            if (routes.containsKey(key)) {
                Mapping mapping = routes.get(key);
                Method method = resolveMethod(mapping);
                Class<?> controllerClass = Class.forName(mapping.getClassName());
                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                method.setAccessible(true);
                api = method.isAnnotationPresent(WebApi.class);

                Object[] args = ParameterBinder.bind(method, req, res);

                if (api) {
                    handleApi(res, controller, method, args);
                    return;
                }

                Object result = method.invoke(controller, args);

                if (result instanceof ModelView mv) {
                    Map<String, Object> model = mv.getModel();
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                    req.setAttribute("mapping", mapping);
                    String view = viewPrefix + mv.getView() + viewSuffix;
                    RequestDispatcher rd = req.getRequestDispatcher(view);
                    rd.forward(req, res);
                } else if (result instanceof String html) {
                    writeHtml(res, html);
                } else {
                    writeError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            url + " -> " + mapping.getClassName() + " -> "
                                    + mapping.getMethod() + "() : resultat non reconnu");
                }
            } else {
                writeNotFound(res, url, httpMethod);
            }
        } catch (BindingException e) {
            writeBindingError(res, api, e);
        } catch (Exception e) {
            throw new ServletException("Erreur interne: " + e.getMessage(), e);
        }
    }

    /**
     * Retrouve la methode du controller a partir du Mapping. Les signatures de
     * methode sont conservees (nom + types des parametres) lors du scan, ce qui
     * est indispensable pour les methodes mappees qui recoivent des parametres.
     */
    private Method resolveMethod(Mapping mapping) throws ClassNotFoundException, NoSuchMethodException {
        Method handler = mapping.getHandler();
        if (handler != null) {
            return handler;
        }
        Class<?> controllerClass = Class.forName(mapping.getClassName());
        String[] types = mapping.getParameterTypes();
        if (types == null || types.length == 0) {
            return controllerClass.getDeclaredMethod(mapping.getMethod());
        }
        Class<?>[] parameterTypes = new Class<?>[types.length];
        for (int i = 0; i < types.length; i++) {
            parameterTypes[i] = Class.forName(types[i]);
        }
        return controllerClass.getDeclaredMethod(mapping.getMethod(), parameterTypes);
    }

    private void handleApi(HttpServletResponse res, Object controller, Method method, Object[] args) throws IOException {
        try {
            Object result = method.invoke(controller, args);
            Object body = (result instanceof ModelView mv) ? mv.getModel() : result;
            writeJson(res, HttpServletResponse.SC_OK, JsonUtil.toJson(body));
        } catch (Exception e) {
            Throwable cause = (e instanceof InvocationTargetException && e.getCause() != null) ? e.getCause() : e;
            String message = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
            writeJson(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    JsonUtil.toJson(Map.of("error", message)));
        }
    }

    private void writeBindingError(HttpServletResponse res, boolean api, BindingException e) throws IOException {
        if (api) {
            writeJson(res, HttpServletResponse.SC_BAD_REQUEST, JsonUtil.toJson(Map.of("error", e.getMessage())));
            return;
        }
        writeError(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    }

    private void writeJson(HttpServletResponse res, int status, String json) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.print(json);
        out.flush();
    }

    private void writeHtml(HttpServletResponse res, String html) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            out.println("<!DOCTYPE html><html><body>");
            out.println(html);
            out.println("</body></html>");
        }
    }

    private void writeError(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        try (PrintWriter out = res.getWriter()) {
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Erreur " + status + "</h1>");
            out.println("<pre>" + message + "</pre>");
            out.println("</body></html>");
        }
    }

    private void writeNotFound(HttpServletResponse res, String url, String httpMethod) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>URL non trouvee : " + url + " [" + httpMethod + "]</h1>");
            out.println("<h2>Routes disponibles :</h2><ul>");
            for (Map.Entry<UrlMethod, Mapping> entry : routes.entrySet()) {
                Mapping m = entry.getValue();
                out.println("<li>" + entry.getKey().getUrl() + " (" + entry.getKey().getMethod()
                        + ") -> " + m.getClassName() + " -> " + m.getMethod() + "()</li>");
            }
            out.println("</ul>");
            out.println("</body></html>");
        }
    }
}
