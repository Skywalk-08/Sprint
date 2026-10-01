package framework.listener;

import framework.annotations.Controller;
import framework.annotations.URLMapping;
import framework.binding.ParameterBinder;
import framework.routing.Mapping;
import framework.routing.UrlMethod;
import framework.utils.PackageScanner;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrameworkInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String packageName = context.getInitParameter("controller_package_name");
        if (packageName == null || packageName.trim().isEmpty()) {
            throw new IllegalArgumentException("Paramètre 'controller_package_name' manquant.");
        }

        try {
            Map<UrlMethod, Mapping> routes = new HashMap<>();
            List<Class<?>> controllers = PackageScanner.getAnnotatedClassesInPackage(packageName, Controller.class);

            for (Class<?> clazz : controllers) {
                String className = clazz.getName();
                for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(URLMapping.class)) {
                        URLMapping ann = method.getAnnotation(URLMapping.class);
                        String url = ann.value();
                        String httpMethod = ann.method();

                        UrlMethod key = new UrlMethod(url, httpMethod);
                        if (routes.containsKey(key)) {
                            throw new IllegalArgumentException("Route dupliquée : " + url + " " + httpMethod);
                        }

                        List<String> bindingErrors = ParameterBinder.validate(method);
                        if (!bindingErrors.isEmpty()) {
                            throw new IllegalArgumentException("Binding impossible pour "
                                    + className + "." + method.getName() + " (" + url + ") : "
                                    + String.join(" ; ", bindingErrors));
                        }

                        routes.put(key, new Mapping(className, method.getName(), method));
                    }
                }
            }

            context.setAttribute("routesWithMethod", routes);
        } catch (Exception e) {
            throw new RuntimeException("Échec de l'initialisation du framework", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
