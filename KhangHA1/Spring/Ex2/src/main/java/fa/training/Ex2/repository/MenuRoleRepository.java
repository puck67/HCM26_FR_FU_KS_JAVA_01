package fa.training.Ex2.repository;

import fa.training.Ex2.entity.MenuRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRoleRepository extends JpaRepository<MenuRole, Long> {
    List<MenuRole> findByRoleName(String roleName);
}
