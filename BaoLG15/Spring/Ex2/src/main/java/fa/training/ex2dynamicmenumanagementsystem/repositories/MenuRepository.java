package fa.training.ex2dynamicmenumanagementsystem.repositories;

import fa.training.ex2dynamicmenumanagementsystem.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();

    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();

    long countByParentIsNull();

    long countByParentIsNotNull();
}
