package framework.routing;

import java.lang.reflect.Method;

public class Mapping {
    private String className;
    private String method;
    private String[] parameterTypes;
    private Method handler;

    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    public Mapping(String className, String method, Method handler) {
        this.className = className;
        this.method = method;
        this.handler = handler;
        if (handler != null) {
            Class<?>[] types = handler.getParameterTypes();
            this.parameterTypes = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                this.parameterTypes[i] = types[i].getName();
            }
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getControllerClass() {
        return className;
    }

    public String getMethod() {
        return method;
    }

    public String getMethodName() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public Method getHandler() {
        return handler;
    }
}
