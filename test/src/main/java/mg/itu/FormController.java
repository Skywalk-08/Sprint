package mg.itu;

import framework.annotations.Controller;
import framework.annotations.Param;
import framework.annotations.URLMapping;
import framework.annotations.WebApi;
import framework.utils.ModelView;

@Controller
public class FormController {

    @URLMapping("/form")
    public ModelView form() {
        return new ModelView("form");
    }

    /**
     * Binding : le framework associe chaque parametre de la requete au parametre
     * de meme nom dans la signature. Le controller ne fait que utiliser les valeurs.
     */
    @URLMapping(value = "/save", method = "POST")
    public ModelView save(String i, String n, int age) {
        User user = new User(n, age);

        ModelView mv = new ModelView("result");
        mv.addAttribute("id", i);
        mv.addAttribute("nom", user.getName());
        mv.addAttribute("age", user.getAge());
        return mv;
    }

    /** Meme binding, reponse JSON grâce à @WebApi. */
    @WebApi
    @URLMapping(value = "/api/save", method = "POST")
    public ModelView saveApi(String i, String n, int age) {
        ModelView mv = new ModelView("result");
        mv.addAttribute("id", i);
        mv.addAttribute("nom", n);
        mv.addAttribute("age", age);
        return mv;
    }

    /** Nom du parametre de requete impose par l'annotation @Param. */
    @URLMapping(value = "/save-named", method = "POST")
    public ModelView saveNamed(@Param("i") String identifiant, @Param("n") String nom, @Param("age") int age) {
        ModelView mv = new ModelView("result");
        mv.addAttribute("id", identifiant);
        mv.addAttribute("nom", nom);
        mv.addAttribute("age", age);
        return mv;
    }
}
