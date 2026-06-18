package com.example.ASM5.service.base;

import java.util.List;

public interface GenericFileService<T> {
    void saveAll(List<T> items, String fileName);
    List<T> getAll(String fileName);
}