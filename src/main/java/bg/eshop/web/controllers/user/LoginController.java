package bg.eshop.web.controllers.user;

import bg.eshop.web.PageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class LoginController {

    @GetMapping("/login")
    @PageTitle("Login")
    public String login() {

        return "user/login";
    }
}
