package mg.itu;

import framework.annotations.Controller;
import framework.annotations.Param;
import framework.annotations.URLMapping;
import framework.annotations.WebApi;
import framework.binding.MySession;
import framework.binding.UploadFile;
import framework.utils.ModelView;

import java.util.List;

@Controller
public class TestController {

    @URLMapping("/")
    public ModelView index() {
        return new ModelView("index");
    }

    @URLMapping("/form")
    public ModelView form() {
        return new ModelView("form");
    }

    @URLMapping(value = "/save", method = "POST")
    public ModelView save(String i, String n, int age) {
        ModelView mv = new ModelView("result");
        mv.addAttribute("id", i);
        mv.addAttribute("nom", n);
        mv.addAttribute("age", age);
        return mv;
    }

    @URLMapping(value = "/save-named", method = "POST")
    public ModelView saveNamed(@Param("i") String identifiant, @Param("n") String nom, @Param("age") int age) {
        ModelView mv = new ModelView("result");
        mv.addAttribute("id", identifiant);
        mv.addAttribute("nom", nom);
        mv.addAttribute("age", age);
        return mv;
    }

    @URLMapping(value = "/save-user", method = "POST")
    public ModelView saveUser(User user) {
        ModelView mv = new ModelView("result-user");
        mv.addAttribute("nom", user.getName());
        mv.addAttribute("age", user.getAge());
        return mv;
    }

    @URLMapping("/form-upload")
    public ModelView formUpload() {
        return new ModelView("form-upload");
    }

    @URLMapping(value = "/upload", method = "POST")
    public ModelView upload(String description, UploadFile file) {
        ModelView mv = new ModelView("result-upload");
        mv.addAttribute("description", description);
        mv.addAttribute("file", file);
        return mv;
    }

    @URLMapping(value = "/upload-multi", method = "POST")
    public ModelView uploadMulti(String description, List<UploadFile> files) {
        ModelView mv = new ModelView("result-upload-multi");
        mv.addAttribute("description", description);
        mv.addAttribute("files", files);
        return mv;
    }

    @URLMapping("/session")
    public ModelView sessionPage(MySession session) {
        Object counter = session.get("counter");
        int c = (counter instanceof Integer) ? (Integer) counter : 0;
        c++;
        session.add("counter", c);
        ModelView mv = new ModelView("session");
        mv.addAttribute("counter", c);
        return mv;
    }

    @WebApi
    @URLMapping(value = "/api/save", method = "POST")
    public ModelView saveApi(String i, String n, int age) {
        ModelView mv = new ModelView("result");
        mv.addAttribute("id", i);
        mv.addAttribute("nom", n);
        mv.addAttribute("age", age);
        return mv;
    }
}
    // Sprint 10 - Auth simple
    @URLMapping("/login-form")
    public ModelView loginForm() {
        return new ModelView("login");
    }

    @URLMapping(value = "/login", method = "POST")
    public ModelView login(framework.binding.MySession session, String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            session.add("user", "admin");
            session.add("role", "ADMIN");
            ModelView mv = new ModelView("login-success");
            mv.addAttribute("user", "admin");
            mv.addAttribute("role", "ADMIN");
            return mv;
        } else if ("user".equals(username) && "user".equals(password)) {
            session.add("user", "user");
            session.add("role", "USER");
            ModelView mv = new ModelView("login-success");
            mv.addAttribute("user", "user");
            mv.addAttribute("role", "USER");
            return mv;
        }
        ModelView mv = new ModelView("login");
        mv.addAttribute("error", "Identifiants invalides");
        return mv;
    }

    @URLMapping("/logout")
    public ModelView logout(framework.binding.MySession session) {
        session.remove("user");
        session.remove("role");
        ModelView mv = new ModelView("login");
        mv.addAttribute("error", "Déconnecté");
        return mv;
    }

    @framework.annotations.Auth
    @URLMapping("/protected")
    public ModelView protectedPage(framework.binding.MySession session) {
        ModelView mv = new ModelView("protected");
        mv.addAttribute("user", session.get("user"));
        mv.addAttribute("role", session.get("role"));
        return mv;
    }

    @framework.annotations.Auth(roles = {"ADMIN"})
    @URLMapping("/admin")
    public ModelView adminOnly(framework.binding.MySession session) {
        ModelView mv = new ModelView("admin");
        mv.addAttribute("user", session.get("user"));
        mv.addAttribute("role", session.get("role"));
        return mv;
    }

    // Sprint 10 - Auth simple
    @URLMapping("/login-form")
    public ModelView loginForm() {
        return new ModelView("login");
    }

    @URLMapping(value = "/login", method = "POST")
    public ModelView login(framework.binding.MySession session, String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            session.add("user", "admin");
            session.add("role", "ADMIN");
            ModelView mv = new ModelView("login-success");
            mv.addAttribute("user", "admin");
            mv.addAttribute("role", "ADMIN");
            return mv;
        } else if ("user".equals(username) && "user".equals(password)) {
            session.add("user", "user");
            session.add("role", "USER");
            ModelView mv = new ModelView("login-success");
            mv.addAttribute("user", "user");
            mv.addAttribute("role", "USER");
            return mv;
        }
        ModelView mv = new ModelView("login");
        mv.addAttribute("error", "Identifiants invalides");
        return mv;
    }

    @URLMapping("/logout")
    public ModelView logout(framework.binding.MySession session) {
        session.remove("user");
        session.remove("role");
        ModelView mv = new ModelView("login");
        mv.addAttribute("error", "Déconnecté");
        return mv;
    }

    @framework.annotations.Auth
    @URLMapping("/protected")
    public ModelView protectedPage(framework.binding.MySession session) {
        ModelView mv = new ModelView("protected");
        mv.addAttribute("user", session.get("user"));
        mv.addAttribute("role", session.get("role"));
        return mv;
    }

    @framework.annotations.Auth(roles = {"ADMIN"})
    @URLMapping("/admin")
    public ModelView adminOnly(framework.binding.MySession session) {
        ModelView mv = new ModelView("admin");
        mv.addAttribute("user", session.get("user"));
        mv.addAttribute("role", session.get("role"));
        return mv;
    }
}

