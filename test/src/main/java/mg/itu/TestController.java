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