package framework.binding;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

public class MySession {
    private final HttpSession httpSession;
    private final Map<String, Object> data;

    public MySession(HttpSession httpSession) {
        this.httpSession = httpSession;
        this.data = new HashMap<>();
        if (httpSession != null) {
            for (String name : java.util.Collections.list(httpSession.getAttributeNames())) {
                data.put(name, httpSession.getAttribute(name));
            }
        }
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void add(String key, Object value) {
        data.put(key, value);
        if (httpSession != null) {
            httpSession.setAttribute(key, value);
        }
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void remove(String key) {
        data.remove(key);
        if (httpSession != null) {
            httpSession.removeAttribute(key);
        }
    }
}