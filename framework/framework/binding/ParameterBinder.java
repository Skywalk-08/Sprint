package framework.binding;

import framework.annotations.Param;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Binding des donnees de la requete vers les parametres de la methode du controller.
 *
 * Regle du framework :
 *  - un parametre de type simple (String, int, boolean, ...) est rempli avec la
 *    valeur du parametre de requete portant le meme nom ;
 *  - un parametre de type objet (User, List, ModelView, ...) n'est pas bindable :
 *    une BindingException est levee.
 *
 * Exemple :
 *   @URLMapping("/save", method="POST")
 *   public ModelView save(String i, String n, int age) { ... }
 *   -> <form action="save" method="post">
 *        <input name="i"> <input name="n"> <input name="age">
 *      </form>
 */
public final class ParameterBinder {

    private ParameterBinder() {
    }

    public static Object[] bind(Method method, HttpServletRequest req, HttpServletResponse res)
            throws BindingException {

        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            String name = resolveName(parameter, i);

            if (type == HttpServletRequest.class) {
                args[i] = req;
                continue;
            }
            if (type == HttpServletResponse.class) {
                args[i] = res;
                continue;
            }

            if (!TypeConverter.isSimpleType(type)) {
                throw new BindingException("Binding d'objet non supporté : le parametre "
                        + name + " de type " + type.getName()
                        + " n'est pas un type simple (String, int, boolean, ...).");
            }

            String rawValue = req.getParameter(name);
            boolean provided = rawValue != null && !rawValue.isBlank();

            if (!provided && type.isPrimitive()) {
                throw new BindingException("Parametre manquant ou vide : \"" + name
                        + "\" de type " + type.getSimpleName()
                        + " (methode " + method.getDeclaringClass().getSimpleName()
                        + "." + method.getName() + ")");
            }

            args[i] = provided ? TypeConverter.convert(rawValue, type) : null;
        }
        return args;
    }

    /**
     * Nom du parametre : @Param("x") si present, sinon le nom reel conserve par la
     * compilation (-parameters), sinon arg0/arg1/... (ordre de declaration).
     */
    private static String resolveName(Parameter parameter, int index) {
        Param annotation = parameter.getAnnotation(Param.class);
        if (annotation != null && !annotation.value().isBlank()) {
            return annotation.value().trim();
        }
        if (parameter.isNamePresent() && parameter.getName() != null
                && !parameter.getName().startsWith("arg")) {
            return parameter.getName();
        }
        return "arg" + index;
    }

    /**
     * Verifie qu'une methode mappee est bindable : uniquement des types simples
     * (ou HttpServletRequest / HttpServletResponse).
     */
    public static List<String> validate(Method method) {
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < method.getParameterCount(); i++) {
            Parameter parameter = method.getParameters()[i];
            Class<?> type = parameter.getType();
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                continue;
            }
            if (!TypeConverter.isSimpleType(type)) {
                errors.add("parametre #" + (i + 1) + " de type " + type.getName()
                        + " (le binding d'objet n'est pas gere)");
            }
        }
        return errors;
    }
}
