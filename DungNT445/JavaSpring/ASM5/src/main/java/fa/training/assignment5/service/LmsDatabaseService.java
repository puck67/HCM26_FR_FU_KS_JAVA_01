package fa.training.assignment5.service;

import fa.training.assignment5.entity.Learner;
import fa.training.assignment5.entity.SystemUser;
import fa.training.assignment5.repository.LearnerRepository;
import fa.training.assignment5.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LmsDatabaseService {

    private final SystemUserRepository userRepository;
    private final LearnerRepository learnerRepository;

    @Autowired
    public LmsDatabaseService(SystemUserRepository userRepository, LearnerRepository learnerRepository) {
        this.userRepository = userRepository;
        this.learnerRepository = learnerRepository;
    }

    @Transactional
    public void saveSystemUsers(List<SystemUser> users) {
        userRepository.saveAll(users);
    }

    @Transactional
    public void saveLearners(List<Learner> learners) {
        learnerRepository.saveAll(learners);
    }

    public List<SystemUser> getAllSystemUsers() {
        return userRepository.findAll();
    }

    public List<Learner> getAllLearners() {
        return learnerRepository.findAll();
    }
}
