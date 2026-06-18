package com.example.ASM6.service;

import com.example.ASM6.model.Lookup;
import com.example.ASM6.repository.LookupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LookupService {

    private final LookupRepository lookupRepository;

    public String getValue(String type, String code) {
        return lookupRepository.findByTypeAndCode(type, code)
                .map(Lookup::getValue)
                .orElse("Unknown (" + code + ")");
    }

    public List<Lookup> getByType(String type) {
        return lookupRepository.findByType(type);
    }

    public void seedDefaultLookups() {
        if (lookupRepository.count() == 0) {
            // Seed Course Statuses
            lookupRepository.save(new Lookup(null, "COURSE_STATUS", "1", "Draft"));
            lookupRepository.save(new Lookup(null, "COURSE_STATUS", "2", "Published"));
            lookupRepository.save(new Lookup(null, "COURSE_STATUS", "3", "Archived"));

            // Seed Review Statuses
            lookupRepository.save(new Lookup(null, "REVIEW_STATUS", "1", "Pending"));
            lookupRepository.save(new Lookup(null, "REVIEW_STATUS", "2", "Approved"));
        }
    }
}
