package com.example.Ex4.controller.base;

import com.example.Ex4.service.base.GenericService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public abstract class GenericController<T>{

    protected GenericService<T> service;
    protected String view;
    protected String url;

    public GenericController(
            GenericService<T> service,
            String view,
            String url){
        this.service = service;
        this.view = view;
        this.url = url;
    }

    @GetMapping
    public String list(Model model){
        model.addAttribute(
                "items",
                service.findAll()
        );
        model.addAttribute("view", view + "/list");
        return "layout/main";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:" + url;
    }
    @GetMapping("/add")
    public String addForm(Model model, @ModelAttribute T item) {
        model.addAttribute("item", item);
        model.addAttribute("view", view + "/form");
        return "layout/main";
    }
    @PostMapping("/add")
    public String save(@ModelAttribute T item) {
        service.save(item);
        return "redirect:" + url;
    }
}