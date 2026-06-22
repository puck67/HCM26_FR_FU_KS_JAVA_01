package com.example.ASM6.service;

import com.example.ASM6.model.Lookup;
import com.example.ASM6.repository.LookupRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LookupService {

    private final LookupRepository lookupRepo;

    public LookupService(LookupRepository lookupRepo) {
        this.lookupRepo = lookupRepo;
    }

    public String getValue(String type, String code) {
        Optional<Lookup> match = lookupRepo.findByTypeAndCode(type, code);
        if (match.isPresent()) {
            return match.get().getValue();
        }
        return "Unknown (" + code + ")";
    }

    public List<Lookup> getByType(String type) {
        return lookupRepo.findByType(type);
    }

    public void seedDefaultLookups() {
        if (lookupRepo.count() == 0) {
            lookupRepo.save(new Lookup(null, "COURSE_STATUS", "1", "Draft"));
            lookupRepo.save(new Lookup(null, "COURSE_STATUS", "2", "Published"));
            lookupRepo.save(new Lookup(null, "COURSE_STATUS", "3", "Archived"));

            lookupRepo.save(new Lookup(null, "REVIEW_STATUS", "1", "Pending"));
            lookupRepo.save(new Lookup(null, "REVIEW_STATUS", "2", "Approved"));
        }
    }
}
