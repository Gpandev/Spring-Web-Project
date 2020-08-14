package bg.eshop.web.controllers.user;

import bg.eshop.domain.models.binding.UserEditBindingModel;
import bg.eshop.domain.models.service.UserServiceModel;
import bg.eshop.domain.models.view.UserViewModel;
import bg.eshop.domain.models.view.UsersAllViewModel;
import bg.eshop.service.UserService;
import bg.eshop.validation.user.UserEditValidator;
import bg.eshop.web.PageTitle;
import bg.eshop.web.controllers.BaseController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class LoginController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserEditValidator userEditValidator;

    @Autowired
    public LoginController(UserService userService, ModelMapper modelMapper, UserEditValidator userEditValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userEditValidator = userEditValidator;
    }


    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public String login() {

        return "user/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Profile")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        UserViewModel model = this.modelMapper.map(userServiceModel, UserViewModel.class);
        modelAndView.addObject("model", model);

        return super.view("user/profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView, @ModelAttribute(name = "model") UserEditBindingModel model) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        model = this.modelMapper.map(userServiceModel, UserEditBindingModel.class);
        model.setPassword(null);
        modelAndView.addObject("model", model);

        return super.view("user/edit-profile", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") UserEditBindingModel model, BindingResult bindingResult) {
        this.userEditValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            model.setOldPassword(null);
            model.setPassword(null);
            model.setConfirmPassword(null);
            modelAndView.addObject("model", model);

            return super.view("user/edit-profile", modelAndView);
        }

        UserServiceModel userServiceModel = this.modelMapper.map(model, UserServiceModel.class);
        this.userService.editUserProfile(userServiceModel, model.getOldPassword());

        return super.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Users")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<UsersAllViewModel> users = this.userService.getAllUsers()
                .stream()
                .map(u -> {
                    UsersAllViewModel user = this.modelMapper.map(u, UsersAllViewModel.class);
                    Set<String> authorities = u.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());
                    user.setAuthorities(authorities);

                    return user;
                })
                .collect(Collectors.toList());

        modelAndView.addObject("users", users);

        return super.view("user/all-users", modelAndView);
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable String id) {
        this.userService.addUserRole(id, "user");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModerator(@PathVariable String id) {
        this.userService.addUserRole(id, "moderator");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdmin(@PathVariable String id) {
        this.userService.addUserRole(id, "admin");

        return super.redirect("/users/all");
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
