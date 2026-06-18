package fa.training.Ex2.service.impl;

import fa.training.Ex2.dto.MenuDTO;
import fa.training.Ex2.entity.Menu;
import fa.training.Ex2.entity.MenuRole;
import fa.training.Ex2.repository.*;
import fa.training.Ex2.service.MenuService;
import fa.training.Ex2.utils.Validator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl
        extends GenericServiceImpl<Menu, Long, MenuRepository>
        implements MenuService {

    private final MenuRoleRepository menuRoleRepository;

    public MenuServiceImpl(MenuRepository repository, MenuRoleRepository menuRoleRepository) {
        super(repository);
        this.menuRoleRepository = menuRoleRepository;
    }

    @Override
    public void save(MenuDTO dto) {

        Validator.validateMenu(dto);

        Menu menu = new Menu();

        mapDtoToEntity(dto, menu);

        repository.save(menu);
    }

    @Override
    public void update(Long id, MenuDTO dto) {

        Validator.validateMenu(dto);

        Menu menu = findById(id);

        mapDtoToEntity(dto, menu);

        repository.save(menu);
    }

    @Override
    public List<Menu> getSidebarMenus() {

        return repository
                .findByParentIsNullOrderByDisplayOrder();
    }

    @Override
    public long totalMenus() {
        return repository.count();
    }

    @Override
    public long totalParentMenus() {
        return repository.countByParentIsNull();
    }

    @Override
    public long totalSubMenus() {
        return repository.countByParentIsNotNull();
    }

    private void mapDtoToEntity(
            MenuDTO dto,
            Menu menu) {

        menu.setName(dto.getName());
        menu.setUrl(dto.getUrl());
        menu.setIcon(dto.getIcon());
        menu.setDisplayOrder(dto.getDisplayOrder());
        menu.setStatus(dto.getStatus());

        if (dto.getParentId() != null) {

            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));

            menu.setParent(parent);

        } else {

            menu.setParent(null);
        }
    }

    @Override
    public List<Menu> getMenusByRole(String roleName) {

        return menuRoleRepository
                .findByRoleName(roleName)
                .stream()
                .map(MenuRole::getMenu)
                .filter(menu -> menu.getParent() == null)
                .toList();
    }
}