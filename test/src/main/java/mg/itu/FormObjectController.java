package mg.itu;

import framework.annotations.Controller;
import framework.annotations.URLMapping;
import framework.utils.ModelView;

@Controller
public class FormObjectController {

    @URLMapping("/form-user")
    public ModelView form() {
        return new ModelView("form-user");
    }

    @URLMapping(value = "/save-user", method = "POST")
    public ModelView saveUser(User user) {
        ModelView mv = new ModelView("result-user");
        mv.addAttribute("nom", user.getName());
        mv.addAttribute("age", user.getAge());
        return mv;
    }
}
