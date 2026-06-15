package fa.training.lms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        // Trả về tiêu đề hiển thị trên Banner
        model.addAttribute("title", "Banner");
        
        // Trả về trang index.html trong thư mục templates
        return "index";
    }
}
