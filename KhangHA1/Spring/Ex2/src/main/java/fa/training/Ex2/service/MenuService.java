package fa.training.Ex2.service;

import fa.training.Ex2.dto.MenuDTO;
import fa.training.Ex2.entity.Menu;

import java.util.List;

public interface MenuService
        extends GenericService<Menu, Long> {

    List<Menu> findAll();

    Menu findById(Long id);

    void save(MenuDTO dto);

    void update(Long id, MenuDTO dto);

    void delete(Long id);

    List<Menu> getSidebarMenus();

    long totalMenus();

    long totalParentMenus();

    long totalSubMenus();

    List<Menu> getMenusByRole(String roleName);
}
