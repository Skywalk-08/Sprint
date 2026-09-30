package framework.utils;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private final String view;
    private final Map<String, Object> model;

    public ModelView(String view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public ModelView addAttribute(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public ModelView addAllAttributes(Map<String, Object> attributes) {
        if (attributes != null) {
            model.putAll(attributes);
        }
        return this;
    }

    public String getView() {
        return view;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
