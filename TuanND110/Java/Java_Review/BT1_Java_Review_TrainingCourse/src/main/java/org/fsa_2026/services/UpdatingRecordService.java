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

public class UpdatingRecordService {

    private final CousrseDao cousrseDao = new CousrseDaoImpl();
    private final LearnerDao learnerDao = new LearnerDaoImpl();
    private final TrainnerDao trainnerDao = new TrainnerDaoImpl();

    public void updateCousrse(Cousrse cousrse) {
        if (cousrse != null && cousrse.getId() > 0) {
            cousrseDao.update(cousrse);
        }
    }

    public void updateLearner(Learner learner) {
        if (learner != null && learner.getId() > 0) {
            learnerDao.update(learner);
        }
    }

    public void updateTrainner(Trainner trainner) {
        if (trainner != null && trainner.getId() > 0) {
            trainnerDao.update(trainner);
        }
    }

    public boolean updateCousrseName(int id, String newName) {
        Cousrse course = cousrseDao.findById(id);
        if (course != null) {
            course.setCousrseName(newName);
            return cousrseDao.update(course);
        }
        return false;
    }

    public boolean updateLearnerName(int id, String newName) {
        Learner learner = learnerDao.findById(id);
        if (learner != null) {
            learner.setStudentName(newName);
            return learnerDao.update(learner);
        }
        return false;
    }

    public boolean updateTrainnerName(int id, String newName) {
        Trainner trainer = trainnerDao.findById(id);
        if (trainer != null) {
            trainer.setTrainnerName(newName);
            return trainnerDao.update(trainer);
        }
        return false;
    }
}
