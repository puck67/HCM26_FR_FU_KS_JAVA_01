package com.lms.service;

import java.util.List;

public interface DataService<T> {

    void save(List<T> items);

    List<T> getAll();
}
