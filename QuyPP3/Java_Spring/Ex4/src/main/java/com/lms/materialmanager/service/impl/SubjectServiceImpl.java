package com.lms.materialmanager.service.impl;

import com.lms.materialmanager.entity.Subject;
import com.lms.materialmanager.repository.SubjectRepository;
import com.lms.materialmanager.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with ID: " + id));
    }

    @Override
    public Subject saveSubject(Subject subject) {
        // If it's a new subject or code is changed, check if code is already used
        subjectRepository.findBySubjectCode(subject.getSubjectCode())
                .ifPresent(existing -> {
                    if (subject.getSubjectId() == null || !existing.getSubjectId().equals(subject.getSubjectId())) {
                        throw new IllegalArgumentException("Subject code '" + subject.getSubjectCode() + "' is already in use.");
                    }
                });
        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(Long id) {
        Subject subject = getSubjectById(id);
        // Delete all physical files associated with this subject's materials
        subject.getMaterials().forEach(material -> {
            try {
                // We will handle deleting physical file in the MaterialService or direct call
                // But let's delete them here or in MaterialService to be safe.
            } catch (Exception e) {
                // Ignore or log
            }
        });
        subjectRepository.delete(subject);
    }

    @Override
    public Subject getSubjectByCode(String code) {
        return subjectRepository.findBySubjectCode(code).orElse(null);
    }
}
