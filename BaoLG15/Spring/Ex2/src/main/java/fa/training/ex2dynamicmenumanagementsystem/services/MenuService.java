package fa.training.ex2dynamicmenumanagementsystem.services;

import fa.training.ex2dynamicmenumanagementsystem.entities.Menu;
import fa.training.ex2dynamicmenumanagementsystem.dto.request.CreateMenuRequest;
import fa.training.ex2dynamicmenumanagementsystem.dto.request.UpdateMenuRequest;

import java.util.List;

public interface MenuService {
    List<Menu> getMenuTreeForUser(List<String> userRoles);

    List<Menu> getAllMenus();

    List<Menu> getAllRootMenus();

    Menu getMenuById(Long id);

    Menu createMenu(CreateMenuRequest request);

    Menu updateMenu(UpdateMenuRequest request);

    void deleteMenu(Long id);

    long countTotalMenus();

    long countTotalParentMenus();

    long countTotalSubMenus();
}
