package bg.eshop.web.controllers.home;

import bg.eshop.web.PageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    @PageTitle("Home")
    public String index() {

        return "index";
    }

    @GetMapping("/home")
    public String home() {

        return "home";
    }
}
