package framework.utils;

import framework.binding.MySession;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AuthUtil {
    public static boolean isAuthenticated(HttpServletRequest req, String sessionKey) {
        if (req == null) return false;
        Object obj = req.getSession().getAttribute(sessionKey);
        if (obj == null) {
            Object ms = req.getAttribute("mySession");
            if (ms instanceof MySession) {
                obj = ((MySession) ms).get(sessionKey);
            }
        }
        if (obj == null && req.getAttribute(sessionKey) != null) {
            obj = req.getAttribute(sessionKey);
        }
        return obj != null;
    }

    public static Object getUser(HttpServletRequest req, String sessionKey) {
        if (req == null) return null;
        Object obj = req.getSession().getAttribute(sessionKey);
        if (obj != null) return obj;
        Object ms = req.getAttribute("mySession");
        if (ms instanceof MySession) {
            obj = ((MySession) ms).get(sessionKey);
            if (obj != null) return obj;
        }
        obj = req.getAttribute(sessionKey);
        return obj;
    }

    public static String getRole(HttpServletRequest req, String roleKey) {
        if (req == null) return null;
        Object role = req.getSession().getAttribute(roleKey);
        if (role != null) return role.toString();
        Object ms = req.getAttribute("mySession");
        if (ms instanceof MySession) {
            role = ((MySession) ms).get(roleKey);
            if (role != null) return role.toString();
        }
        role = req.getAttribute(roleKey);
        if (role != null) return role.toString();
        return null;
    }

    public static boolean hasRole(HttpServletRequest req, String roleKey, String[] requiredRoles) {
        if (requiredRoles == null || requiredRoles.length == 0) {
            return true;
        }
        String userRole = getRole(req, roleKey);
        if (userRole == null) {
            return false;
        }
        Set<String> allowed = new HashSet<>(Arrays.asList(requiredRoles));
        if (allowed.contains(userRole)) return true;
        for (String r : requiredRoles) {
            if (r != null && r.equalsIgnoreCase(userRole)) return true;
        }
        return false;
    }
}
