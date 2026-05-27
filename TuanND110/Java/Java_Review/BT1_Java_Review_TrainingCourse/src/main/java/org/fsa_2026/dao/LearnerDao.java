package org.fsa_2026.dao;

import org.fsa_2026.enities.Learner;

import java.util.List;

public interface LearnerDao {
    boolean insert(Learner learner);
    Learner findById(int id);
    List<Learner> findAll();
    boolean update(Learner learner);
    boolean deleteById(int id);
}

