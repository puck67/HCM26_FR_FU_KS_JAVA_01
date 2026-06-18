package com.example.Ex4.service;

import com.example.Ex4.entity.Subject;
import com.example.Ex4.repository.SubjectRepository;
import com.example.Ex4.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SubjectService extends GenericServiceImpl<Subject> {

    public SubjectService(SubjectRepository repository) {
        super(repository);
    }
}

