package com.example.lms.service;

import com.example.lms.entity.Menu;
import com.example.lms.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> getMenusByRole(String role) {
        return menuRepository.findByRoleOrderByOrderIndex(role);
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Page<Menu> getAllMenus(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    public void saveMenu(Menu menu) {
        menuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }
}
