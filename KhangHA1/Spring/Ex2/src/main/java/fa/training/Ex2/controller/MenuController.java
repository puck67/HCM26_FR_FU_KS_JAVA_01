package fa.training.Ex2.controller;

import fa.training.Ex2.dto.MenuDTO;
import fa.training.Ex2.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menus")
public class MenuController
        extends GenericController<MenuDTO, Long> {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    protected String getViewFolder() {
        return "menu";
    }

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "menus",
                menuService.findAll());

        return "menu/list";
    }

    @Override
    protected void loadCreateData(Model model) {

        model.addAttribute(
                "menu",
                new MenuDTO());

        model.addAttribute(
                "parents",
                menuService.getSidebarMenus());
    }

    @Override
    protected void loadEditData(
            Model model,
            Long id) {

        model.addAttribute(
                "menu",
                menuService.findById(id));

        model.addAttribute(
                "parents",
                menuService.getSidebarMenus());
    }

    @Override
    protected void saveEntity(MenuDTO dto) {
        menuService.save(dto);
    }

    @Override
    protected void updateEntity(
            Long id,
            MenuDTO dto) {

        menuService.update(id, dto);
    }

    @Override
    protected void deleteEntity(Long id) {
        menuService.delete(id);
    }
}