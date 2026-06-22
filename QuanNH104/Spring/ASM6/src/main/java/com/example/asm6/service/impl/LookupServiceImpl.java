package com.example.asm6.service.impl;

import com.example.asm6.model.Lookup;
import com.example.asm6.repository.LookupRepository;
import com.example.asm6.service.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LookupServiceImpl implements LookupService {

    @Autowired
    private LookupRepository lookupRepository;

    @Override
    public String getValue(String type, String code) {
        Optional<Lookup> lookupOpt = lookupRepository.findByTypeAndCode(type, code);
        if (lookupOpt.isPresent()) {
            return lookupOpt.get().getValue();
        }
        
        // Fallback mặc định nếu chưa khởi tạo DB
        if ("COURSE_STATUS".equalsIgnoreCase(type)) {
            switch (code) {
                case "1": return "Draft";
                case "2": return "Published";
                case "3": return "Archived";
                default: return "Unknown";
            }
        } else if ("REVIEW_STATUS".equalsIgnoreCase(type)) {
            switch (code) {
                case "1": return "Pending";
                case "2": return "Approved";
                default: return "Unknown";
            }
        }
        return "Unknown";
    }
}
