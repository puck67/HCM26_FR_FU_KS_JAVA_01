package com.example.asm4.service;

import java.util.List;

public interface GenericService<T> {
    void saveCourses(List<T> items);
    List<T> getCourses();
}
