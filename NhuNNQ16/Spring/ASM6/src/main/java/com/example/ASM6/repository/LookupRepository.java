package com.example.ASM6.repository;

import com.example.ASM6.model.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LookupRepository extends JpaRepository<Lookup, Long> {
    
    // Find all lookup definitions of a specific type (e.g., "COURSE_STATUS")
    List<Lookup> findByType(String type);
    
    // Find a specific lookup value by type and code
    Optional<Lookup> findByTypeAndCode(String type, String code);
}
