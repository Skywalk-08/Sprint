package mg.itu;

import framework.annotations.Controller;
import framework.annotations.URLMapping;
import framework.utils.ModelView;

@Controller
public class HomeController {

    @URLMapping("/")
    public ModelView index() {
        return new ModelView("index");
    }
}
