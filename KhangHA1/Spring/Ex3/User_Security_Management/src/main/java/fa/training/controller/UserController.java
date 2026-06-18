package fa.training.controller;

import fa.training.controller.base.GenericController;
import fa.training.entity.User;
import fa.training.entity.UserStatus;
import fa.training.service.RoleService;
import fa.training.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController extends GenericController<User, Long> {

    @Autowired
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService) {
        super(userService, "users", "user");
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("statuses", UserStatus.values());
    }
}
