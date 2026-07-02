package com.fpt.lms.service.impl;

import com.fpt.lms.model.AssessmentMaterial;
import com.fpt.lms.repository.AssessmentMaterialRepository;
import com.fpt.lms.service.AssessmentMaterialService;
import org.springframework.stereotype.Service;

@Service
public class AssessmentMaterialServiceImpl extends GenericServiceImpl<AssessmentMaterial, Long, AssessmentMaterialRepository> implements AssessmentMaterialService {

    public AssessmentMaterialServiceImpl(AssessmentMaterialRepository repository) {
        super(repository);
    }
}
