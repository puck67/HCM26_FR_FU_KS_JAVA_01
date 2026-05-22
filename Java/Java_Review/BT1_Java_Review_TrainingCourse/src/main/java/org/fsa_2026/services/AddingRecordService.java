package org.fsa_2026.services;

import org.fsa_2026.dao.CousrseDao;
import org.fsa_2026.dao.LearnerDao;
import org.fsa_2026.dao.TrainnerDao;
import org.fsa_2026.daoImpl.CousrseDaoImpl;
import org.fsa_2026.daoImpl.LearnerDaoImpl;
import org.fsa_2026.daoImpl.TrainnerDaoImpl;
import org.fsa_2026.enities.Cousrse;
import org.fsa_2026.enities.Learner;
import org.fsa_2026.enities.Trainner;

import java.util.HashSet;
import java.util.Set;

public class AddingRecordService {
    private final CousrseDao cousrseDao = new CousrseDaoImpl();
    private final LearnerDao learnerDao = new LearnerDaoImpl();
    private final TrainnerDao trainnerDao = new TrainnerDaoImpl();

    public Set<Cousrse> addingNewCousrse(Cousrse cousrse){
        if (cousrse != null) {
            cousrseDao.insert(cousrse);
        }
        return getCousrses();
    }

    public Set<Learner> addingNewLearner(Learner learner){
        if (learner != null) {
            learnerDao.insert(learner);
        }
        return getLearners();
    }

    public Set<Trainner> addingNewTrainner(Trainner trainner){
        if (trainner != null) {
            trainnerDao.insert(trainner);
        }
        return getTrainners();
    }

    public Set<Cousrse> getCousrses() {
        return new HashSet<>(cousrseDao.findAll());
    }

    public Set<Learner> getLearners() {
        return new HashSet<>(learnerDao.findAll());
    }

    public Set<Trainner> getTrainners() {
        return new HashSet<>(trainnerDao.findAll());
    }

    public int getTotalCourses() {
        return cousrseDao.findAll().size();
    }

    public int getTotalLearners() {
        return learnerDao.findAll().size();
    }

    public int getTotalTrainers() {
        return trainnerDao.findAll().size();
    }
}
