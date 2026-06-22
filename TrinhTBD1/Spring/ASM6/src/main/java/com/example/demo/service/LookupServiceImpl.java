package com.example.demo.service;

import com.example.demo.model.Lookup;
import com.example.demo.repository.LookupRepository;
import com.example.demo.service.base.GenericServiceImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class LookupServiceImpl extends GenericServiceImpl<Lookup, Long, LookupRepository>
        implements LookupService {

    public LookupServiceImpl(LookupRepository lookupRepository) {
        super(lookupRepository);
    }

    @Override
    public List<Lookup> getLookupsByType(String type) {
        if (type == null) throw new IllegalArgumentException("Type cannot be null");
        return repository.findByTypeOrderByPositionAsc(type);
    }

    @Override
    public String getLookupName(String type, String code) {
        if (type == null || code == null) return "";
        return repository.findByTypeOrderByPositionAsc(type).stream()
                .filter(l -> code.equals(l.getCode()))
                .map(Lookup::getName)
                .findFirst()
                .orElse(code);
    }

    @Override
    @Transactional
    public void saveLookup(Lookup lookup) {
        if (lookup == null) throw new IllegalArgumentException("Lookup cannot be null");
        save(lookup);
    }
}
