package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuView {

    private Long id;
    private String name;
    private String url;
    private String icon;
    private Integer displayOrder;
    private List<MenuView> children = new ArrayList<>();
}
