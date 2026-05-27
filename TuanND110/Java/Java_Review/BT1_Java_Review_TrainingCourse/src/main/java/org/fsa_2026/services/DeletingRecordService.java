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

public class DeletingRecordService {

    private final CousrseDao cousrseDao = new CousrseDaoImpl();
    private final LearnerDao learnerDao = new LearnerDaoImpl();
    private final TrainnerDao trainnerDao = new TrainnerDaoImpl();

    public boolean deleteCousrseByID(int id) {
        return cousrseDao.deleteById(id);
    }

    public boolean deleteLearnerByID(int id) {
        return learnerDao.deleteById(id);
    }

    public boolean deleteTrainnerByID(int id) {
        return trainnerDao.deleteById(id);
    }

    public boolean deleteCousrseByName(String name) {
        boolean deleted = false;
        for (Cousrse c : cousrseDao.findAll()) {
            if (c.getCousrseName().equals(name)) {
                deleted |= cousrseDao.deleteById(c.getId());
            }
        }
        return deleted;
    }

    public boolean deleteLearnerByName(String name) {
        boolean deleted = false;
        for (Learner l : learnerDao.findAll()) {
            if (l.getStudentName().equals(name)) {
                deleted |= learnerDao.deleteById(l.getId());
            }
        }
        return deleted;
    }

    public boolean deleteTrainnerByName(String name) {
        boolean deleted = false;
        for (Trainner t : trainnerDao.findAll()) {
            if (t.getTrainnerName().equals(name)) {
                deleted |= trainnerDao.deleteById(t.getId());
            }
        }
        return deleted;
    }
}
