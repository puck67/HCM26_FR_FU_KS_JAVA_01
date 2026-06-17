package com.assessment.service.impl;

import com.assessment.model.AssessmentMaterial;
import com.assessment.service.AssessmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final List<AssessmentMaterial> materials = new CopyOnWriteArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<AssessmentMaterial> getAllMaterials() {
        return new ArrayList<>(materials);
    }

    @Override
    public void addMaterial(AssessmentMaterial material) {
        material.setId(idGenerator.getAndIncrement());
        materials.add(material);
    }

    @Override
    public void deleteMaterial(long id) {
        materials.removeIf(m -> m.getId() == id);
    }
}
