package org.fsa_2026.dao;

import org.fsa_2026.enities.Cousrse;

import java.util.List;

public interface CousrseDao {
    boolean insert(Cousrse cousrse);
    Cousrse findById(int id);
    Cousrse findByName(String name);
    List<Cousrse> findAll();
    boolean update(Cousrse cousrse);
    boolean deleteById(int id);
}

