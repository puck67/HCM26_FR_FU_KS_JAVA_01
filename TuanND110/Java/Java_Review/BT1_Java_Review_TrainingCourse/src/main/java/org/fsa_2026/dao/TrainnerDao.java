package org.fsa_2026.dao;

import org.fsa_2026.enities.Trainner;

import java.util.List;

public interface TrainnerDao {
    boolean insert(Trainner trainner);
    Trainner findById(int id);
    Trainner findByName(String name);
    List<Trainner> findAll();
    boolean update(Trainner trainner);
    boolean deleteById(int id);
}

