package framework.binding;

import framework.annotations.Param;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

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

            if (TypeConverter.isSimpleType(type)) {
                String rawValue = req.getParameter(name);
                boolean provided = rawValue != null && !rawValue.isBlank();

                if (!provided && type.isPrimitive()) {
                    throw new BindingException("Parametre manquant ou vide : \"" + name
                            + "\" de type " + type.getSimpleName()
                            + " (methode " + method.getDeclaringClass().getSimpleName()
                            + "." + method.getName() + ")");
                }

                args[i] = provided ? TypeConverter.convert(rawValue, type) : (type.isPrimitive() ? TypeConverter.convert("0", type) : null);
                continue;
            }

            args[i] = bindObject(type, req, name);
        }
        return args;
    }

    public static Object bindObject(Class<?> clazz, HttpServletRequest req, String paramName)
            throws BindingException {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);

            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                String prop = pd.getName();
                if ("class".equals(prop)) {
                    continue;
                }
                String reqParam = req.getParameter(prop);
                if (reqParam == null) {
                    continue;
                }
                boolean provided = !reqParam.isBlank();
                Class<?> ptype = pd.getPropertyType();
                if (!provided && ptype.isPrimitive()) {
                    throw new BindingException("Parametre manquant ou vide : \"" + prop
                            + "\" pour " + clazz.getSimpleName());
                }
                Object val = provided ? TypeConverter.convert(reqParam, ptype) : (ptype.isPrimitive() ? TypeConverter.convert("0", ptype) : null);
                if (pd.getWriteMethod() != null) {
                    pd.getWriteMethod().invoke(instance, val);
                    continue;
                }
                try {
                    Field f = clazz.getDeclaredField(prop);
                    f.setAccessible(true);
                    f.set(instance, val);
                } catch (NoSuchFieldException ignored) {
                }
            }
            return instance;
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new BindingException("Erreur lors du binding de l'objet " + clazz.getName()
                    + (cause.getMessage() != null ? " : " + cause.getMessage() : ""), cause);
        }
    }

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

    public static List<String> validate(Method method) {
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < method.getParameterCount(); i++) {
            Parameter parameter = method.getParameters()[i];
            Class<?> type = parameter.getType();
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                continue;
            }
            if (TypeConverter.isSimpleType(type)) {
                continue;
            }
            // Accepter les POJOs (objets) pour le binding
        }
        return errors;
    }
}
