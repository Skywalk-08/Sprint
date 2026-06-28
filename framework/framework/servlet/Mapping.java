package framework.servlet;

/**
 * Contient les informations d'un mapping URL → méthode
 * Utilisé par le FrontControllerServlet pour dispatcher les requêtes
 */
public class Mapping {

    private String className;   // nom complet de la classe Controller
    private String methodName;  // nom de la méthode annotée @UrlMapping

    public Mapping(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return className + "#" + methodName + "()";
    }
}
