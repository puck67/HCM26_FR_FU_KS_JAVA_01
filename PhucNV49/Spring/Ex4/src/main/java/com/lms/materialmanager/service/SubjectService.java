package com.lms.materialmanager.service;

import com.lms.materialmanager.entity.Subject;
import java.util.List;

public interface SubjectService {
    List<Subject> getAllSubjects();
    Subject getSubjectById(Long id);
    Subject saveSubject(Subject subject);
    void deleteSubject(Long id);
    Subject getSubjectByCode(String code);
}
