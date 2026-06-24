package com.lms.menumanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Menu name is required")
    @Column(nullable = false)
    private String name;

    private String url;

    private String icon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("displayOrder ASC")
    private List<Menu> children = new ArrayList<>();

    @NotNull(message = "Display order is required")
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @NotNull(message = "Status is required")
    @Column(nullable = false)
    private Boolean status = true;

    // Comma-separated roles, e.g. "ADMIN,TEACHER,STUDENT"
    private String roles;

    // Helper method to add child
    public void addChild(Menu child) {
        children.add(child);
        child.setParent(this);
    }

    // Helper method to remove child
    public void removeChild(Menu child) {
        children.remove(child);
        child.setParent(null);
    }
}
