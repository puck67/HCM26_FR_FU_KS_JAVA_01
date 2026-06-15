package fa.training.Ex2.repository;

import fa.training.Ex2.entity.Menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByParentIsNullOrderByDisplayOrder();

    long countByParentIsNull();

    long countByParentIsNotNull();
}
