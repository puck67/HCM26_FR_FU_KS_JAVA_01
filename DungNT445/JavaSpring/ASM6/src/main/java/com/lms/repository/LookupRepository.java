package com.lms.repository;

import com.lms.model.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LookupRepository extends JpaRepository<Lookup, Long> {
    List<Lookup> findByType(String type);
    Optional<Lookup> findByTypeAndCode(String type, Integer code);
}
