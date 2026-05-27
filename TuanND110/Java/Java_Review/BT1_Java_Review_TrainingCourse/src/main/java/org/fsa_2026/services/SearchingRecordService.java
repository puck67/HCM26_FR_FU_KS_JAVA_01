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

public class SearchingRecordService {

    private final CousrseDao cousrseDao = new CousrseDaoImpl();
    private final LearnerDao learnerDao = new LearnerDaoImpl();
    private final TrainnerDao trainnerDao = new TrainnerDaoImpl();

    public boolean searchCousrse(int idCourse) {
        return cousrseDao.findById(idCourse) != null;
    }

    public boolean searchLearner(int idLearner) {
        return learnerDao.findById(idLearner) != null;
    }

    public boolean searchTrainner(int idTrainner) {
        return trainnerDao.findById(idTrainner) != null;
    }

    public boolean searchCousrse(String courseName) {
        return cousrseDao.findAll().stream().anyMatch(c -> c.getCousrseName().equals(courseName));
    }

    public boolean searchLearner(String learnerName) {
        return learnerDao.findAll().stream().anyMatch(l -> l.getStudentName().equals(learnerName));
    }

    public boolean searchTrainner(String trainnerName) {
        return trainnerDao.findAll().stream().anyMatch(t -> t.getTrainnerName().equals(trainnerName));
    }

    public Cousrse findCousrseByID(int id) {
        return cousrseDao.findById(id);
    }

    public Cousrse findCousrseByName(String name) {
        return cousrseDao.findAll().stream()
                .filter(c -> c.getCousrseName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Learner findLearnerByID(int id) {
        return learnerDao.findById(id);
    }

    public Learner findLearnerByName(String name) {
        return learnerDao.findAll().stream()
                .filter(l -> l.getStudentName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Trainner findTrainnerByID(int id) {
        return trainnerDao.findById(id);
    }

    public Trainner findTrainnerByName(String name) {
        return trainnerDao.findAll().stream()
                .filter(t -> t.getTrainnerName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
