package com.fsoft.training.courseshare.repository;

import com.fsoft.training.courseshare.entity.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LookupRepository extends JpaRepository<Lookup, Long> {
}
