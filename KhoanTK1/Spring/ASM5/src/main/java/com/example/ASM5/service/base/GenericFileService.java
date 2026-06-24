package com.example.ASM5.service.base;

import java.util.List;

public interface GenericFileService<E> {
    void saveAll(List<E> list, String path);
    List<E> getAll(String path);
}