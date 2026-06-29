package com.example.training.controller.base;



import com.example.training.service.base.GenericService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class GenericController<T, ID> {

    protected final GenericService<T, ID> service;

    public GenericController(GenericService<T, ID> service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {

        T entity = service.getById(id);

        if(entity == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entity);
    }


    @PostMapping
    public ResponseEntity<Void> save(@RequestBody T entity){

        service.save(entity);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(

            @PathVariable ID id,

            @RequestBody T entity

    ){


        T oldEntity =
                service.getById(id);



        if(oldEntity == null){

            return ResponseEntity.notFound().build();

        }



        return ResponseEntity.ok(
                service.save(entity)
        );


    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id){

        service.delete(id);

        return ResponseEntity.ok().build();
    }
}