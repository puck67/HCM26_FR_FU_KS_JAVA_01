package fa.training.controller;

import fa.training.controller.base.GenericController;
import fa.training.entity.Role;
import fa.training.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roles")
public class RoleController extends GenericController<Role, Long> {

    @Autowired
    public RoleController(RoleService roleService) {
        super(roleService, "roles", "role");
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }
}
