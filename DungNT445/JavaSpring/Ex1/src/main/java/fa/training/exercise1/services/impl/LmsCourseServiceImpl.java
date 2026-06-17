package fa.training.exercise1.services.impl;

import fa.training.exercise1.entities.LmsCourse;
import fa.training.exercise1.entities.LmsCourseId;
import fa.training.exercise1.repositories.LmsCourseRepository;
import fa.training.exercise1.services.LmsCourseService;
import fa.training.exercise1.services.base.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LmsCourseServiceImpl extends GenericServiceImpl<LmsCourse, LmsCourseId, LmsCourseRepository> implements LmsCourseService {

    @Autowired
    public LmsCourseServiceImpl(LmsCourseRepository courseRepository) {
        super(courseRepository);
    }
}
