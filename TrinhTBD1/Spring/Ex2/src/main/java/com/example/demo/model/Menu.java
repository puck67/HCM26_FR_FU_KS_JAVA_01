package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Menu name cannot be blank")
    @Column(nullable = false, length = 100)
    private String name;

    @Pattern(regexp = "^$|^/[a-zA-Z0-9\\-_/]*$", message = "URL must be empty or a relative path starting with '/'")
    @Column(length = 255)
    private String url;

    @Column(length = 50)
    private String icon;

    @Column(name = "display_order", nullable = false)
    @NotNull(message = "Display order cannot be null")
    @Min(value = 0, message = "Display order must be 0 or positive")
    private Integer displayOrder;

    @Column(nullable = false)
    @NotNull(message = "Status cannot be null")
    private Boolean status;

    @Column(length = 255)
    private String roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<Menu> children = new ArrayList<>();
}
