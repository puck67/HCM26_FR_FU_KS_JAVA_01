package com.example.ex2.service.impl;

import com.example.ex2.entity.Menu;
import com.example.ex2.repository.MenuRepository;
import com.example.ex2.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getMenusForRole(String role) {
        List<Menu> allRoots = menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
        List<Menu> filteredRoots = new ArrayList<>();

        for (Menu root : allRoots) {
            Menu clone = cloneAndFilterMenu(root, role);
            if (clone != null) {
                filteredRoots.add(clone);
            }
        }
        return filteredRoots;
    }

    @Override
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalMenusCount() {
        return menuRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalParentMenusCount() {
        return menuRepository.countByParentIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalSubMenusCount() {
        return menuRepository.countByParentIsNotNull();
    }

    // Sao chép sâu và lọc các menu con theo phân quyền
    private Menu cloneAndFilterMenu(Menu original, String role) {
        if (!hasRole(original.getRoles(), role)) {
            return null;
        }

        Menu clone = new Menu();
        clone.setId(original.getId());
        clone.setName(original.getName());
        clone.setUrl(original.getUrl());
        clone.setRoles(original.getRoles());
        clone.setDisplayOrder(original.getDisplayOrder());

        List<Menu> filteredSubs = new ArrayList<>();
        for (Menu sub : original.getSubMenus()) {
            Menu subClone = cloneAndFilterMenu(sub, role);
            if (subClone != null) {
                subClone.setParent(clone);
                filteredSubs.add(subClone);
            }
        }
        clone.setSubMenus(filteredSubs);
        return clone;
    }

    // Kiểm tra xem phân quyền của menu có chứa role của người dùng hiện tại không
    private boolean hasRole(String rolesStr, String targetRole) {
        if (rolesStr == null || rolesStr.trim().isEmpty()) {
            return true; // Nếu rỗng thì mặc định hiển thị cho tất cả
        }
        String[] roles = rolesStr.split(",");
        for (String r : roles) {
            if (r.trim().equalsIgnoreCase(targetRole)) {
                return true;
            }
        }
        return false;
    }
}
