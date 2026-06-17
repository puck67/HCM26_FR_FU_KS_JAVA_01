package com.courses.repository;

import com.courses.entity.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LookupRepository extends JpaRepository<Lookup, Long> {
    List<Lookup> findByTypeOrderByPositionAsc(String type);
}
