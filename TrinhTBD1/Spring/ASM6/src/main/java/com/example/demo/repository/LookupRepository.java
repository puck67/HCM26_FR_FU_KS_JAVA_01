package com.example.demo.repository;

import com.example.demo.model.Lookup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LookupRepository extends JpaRepository<Lookup, Long> {

    List<Lookup> findByTypeOrderByPositionAsc(String type);
}
