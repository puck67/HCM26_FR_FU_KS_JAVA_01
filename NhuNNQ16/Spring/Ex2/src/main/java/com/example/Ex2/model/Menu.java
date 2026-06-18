package com.example.Ex2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String url;
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<Menu> children = new ArrayList<>();

    private int displayOrder;
    
    private boolean status = true;

    // Comma-separated roles (e.g. "ADMIN,TEACHER,STUDENT")
    private String roles = "ADMIN,TEACHER,STUDENT";

    @Transient
    private String displayName;
}
