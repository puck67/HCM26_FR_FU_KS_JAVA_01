package com.lms.repository;

import com.lms.model.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LookupRepository extends JpaRepository<Lookup, Long> {
    List<Lookup> findByType(String type);
    Optional<Lookup> findByTypeAndCode(String type, String code);
}
