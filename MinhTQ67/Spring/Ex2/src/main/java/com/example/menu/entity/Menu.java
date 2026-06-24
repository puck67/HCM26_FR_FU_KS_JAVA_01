package com.example.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String url;

    private String icon;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @ToString.Exclude // Prevent infinite loop in toString
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("displayOrder ASC")
    @ToString.Exclude // Prevent infinite loop in toString
    private List<Menu> children;

    @Column(name = "display_order")
    private Integer displayOrder;

    private Boolean status = true;
}
