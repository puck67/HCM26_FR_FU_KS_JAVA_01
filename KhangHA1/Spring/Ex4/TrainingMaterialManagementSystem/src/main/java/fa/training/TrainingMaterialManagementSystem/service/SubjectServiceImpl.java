package fa.training.TrainingMaterialManagementSystem.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import fa.training.TrainingMaterialManagementSystem.entity.Subject;
import fa.training.TrainingMaterialManagementSystem.repository.SubjectRepository;
import fa.training.TrainingMaterialManagementSystem.service.base.GenericServiceImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl
        extends GenericServiceImpl<Subject, Long>
        implements SubjectService {

    private final SubjectRepository repository;

    @Override
    protected JpaRepository<Subject, Long> getRepository() {
        return repository;
    }

    @Override
    public Subject update(Long id, Subject entity) {

        Subject subject = repository.findById(id)
                .orElseThrow();
        subject.setSubjectCode(entity.getSubjectCode());
        subject.setSubjectName(entity.getSubjectName());
        subject.setDuration(entity.getDuration());
        return repository.save(subject);
    }
}
