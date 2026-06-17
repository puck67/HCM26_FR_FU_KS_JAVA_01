package com.menu.service.impl;

import com.menu.entity.Menu;
import com.menu.repository.MenuRepository;
import com.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public List<Menu> getRootMenus() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public long countAll() {
        return menuRepository.count();
    }

    @Override
    public long countParents() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc().size();
    }

    @Override
    public long countSubs() {
        return menuRepository.findByParentIsNotNull().size();
    }

    @Override
    public List<Menu> getFilteredRootMenus(String role) {
        List<Menu> allRoots = getRootMenus();
        if ("Admin".equalsIgnoreCase(role)) {
            return allRoots;
        } else if ("Teacher".equalsIgnoreCase(role)) {
            return allRoots.stream()
                    .filter(m -> m.getName().equalsIgnoreCase("Dashboard") || m.getName().equalsIgnoreCase("Training Management"))
                    .map(m -> {
                        if (m.getName().equalsIgnoreCase("Training Management")) {
                            Menu copy = cloneMenu(m);
                            copy.setChildren(m.getChildren().stream()
                                    .filter(c -> c.getName().equalsIgnoreCase("Course Management"))
                                    .toList());
                            return copy;
                        }
                        return m;
                    })
                    .toList();
        } else if ("Student".equalsIgnoreCase(role)) {
            return allRoots.stream()
                    .filter(m -> m.getName().equalsIgnoreCase("Dashboard") || m.getName().equalsIgnoreCase("Training Management"))
                    .map(m -> {
                        if (m.getName().equalsIgnoreCase("Training Management")) {
                            Menu copy = cloneMenu(m);
                            copy.setChildren(m.getChildren().stream()
                                    .filter(c -> c.getName().equalsIgnoreCase("Course Management"))
                                    .map(c -> {
                                        Menu copyC = cloneMenu(c);
                                        copyC.setChildren(c.getChildren().stream()
                                                .filter(cc -> cc.getName().equalsIgnoreCase("Online Courses"))
                                                .toList());
                                        return copyC;
                                    })
                                    .toList());
                            return copy;
                        }
                        return m;
                    })
                    .toList();
        }
        return allRoots;
    }

    private Menu cloneMenu(Menu m) {
        return Menu.builder()
                .id(m.getId())
                .name(m.getName())
                .url(m.getUrl())
                .icon(m.getIcon())
                .parent(m.getParent())
                .displayOrder(m.getDisplayOrder())
                .status(m.getStatus())
                .children(m.getChildren())
                .build();
    }

    @Override
    public boolean isCyclic(Menu menu, Long targetParentId) {
        if (targetParentId == null) return false;
        if (menu.getId() != null && menu.getId().equals(targetParentId)) return true;

        Optional<Menu> parentOpt = findById(targetParentId);
        while (parentOpt.isPresent()) {
            Menu parent = parentOpt.get();
            if (menu.getId() != null && parent.getId().equals(menu.getId())) {
                return true;
            }
            parentOpt = parent.getParent() != null ? findById(parent.getParent().getId()) : Optional.empty();
        }
        return false;
    }
}
