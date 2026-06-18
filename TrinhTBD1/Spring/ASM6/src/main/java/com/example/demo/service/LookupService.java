package com.example.demo.service;

import com.example.demo.model.Lookup;
import com.example.demo.service.base.GenericService;
import java.util.List;

public interface LookupService extends GenericService<Lookup, Long> {

    List<Lookup> getLookupsByType(String type);

    String getLookupName(String type, String code);

    void saveLookup(Lookup lookup);
}
