package mg.itu;

import framework.annotations.Controller;
import framework.annotations.URLMapping;
import framework.annotations.WebApi;
import framework.utils.ModelView;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ApiController {

    @WebApi
    @URLMapping("/api/users")
    public List<User> listUsers() {
        return Arrays.asList(
            new User("Alice", 24),
            new User("Bob", 31),
            new User("Charlie", 19)
        );
    }

    @WebApi
    @URLMapping("/api/user")
    public User firstUser() {
        return new User("Alice", 24);
    }

    @WebApi
    @URLMapping("/api/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("framework", "framework_spring_mvc");
        info.put("sprint", 6);
        info.put("api", true);
        return info;
    }

    // Un ModelView retourné par une méthode @WebApi : on renvoie son modèle en JSON, sans JSP
    @WebApi
    @URLMapping("/api/users-mv")
    public ModelView usersFromModelView() {
        ModelView mv = new ModelView("users");
        mv.addAttribute("users", listUsers());
        mv.addAttribute("total", 3);
        return mv;
    }

    @WebApi
    @URLMapping("/api/error")
    public String fail() {
        throw new IllegalStateException("Erreur volontaire pour tester la réponse JSON d'erreur");
    }
}
