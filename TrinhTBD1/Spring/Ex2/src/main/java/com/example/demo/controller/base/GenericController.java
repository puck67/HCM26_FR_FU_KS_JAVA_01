package com.example.demo.controller.base;

import com.example.demo.service.base.GenericService;

public abstract class GenericController<T, ID, S extends GenericService<T, ID>> {

    protected final S service;

    protected GenericController(S service) {
        this.service = service;
    }
}
