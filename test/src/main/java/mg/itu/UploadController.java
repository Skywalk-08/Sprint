package mg.itu;

import framework.annotations.Controller;
import framework.annotations.URLMapping;
import framework.binding.UploadFile;
import framework.utils.ModelView;

import java.util.List;

@Controller
public class UploadController {

    @URLMapping("/form-upload")
    public ModelView form() {
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
}