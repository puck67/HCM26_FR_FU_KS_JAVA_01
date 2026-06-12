package fa.training.ex3.repositories;

import fa.training.ex3.entities.Role;
import fa.training.ex3.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.role_name = :roleName")
    Optional<Role> findByRoleName(@Param("roleName") RoleEnum roleName);
}
