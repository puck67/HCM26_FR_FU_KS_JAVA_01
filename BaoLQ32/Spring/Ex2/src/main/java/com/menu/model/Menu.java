package com.menu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Menu name is required")
    @Column(nullable = false)
    private String name;

    private String url;

    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<Menu> children = new ArrayList<>();

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    private Boolean status = true;

    // Roles authorized to view this menu (comma-separated, e.g., "ADMIN,TEACHER,STUDENT")
    private String roles;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    // Helper method to check if a specific role is allowed
    public boolean hasRole(String roleName) {
        if (roles == null || roles.trim().isEmpty()) {
            return true; // If no roles are defined, it's public
        }
        String[] roleArray = roles.split(",");
        for (String r : roleArray) {
            if (r.trim().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }

    // Dynamic depth calculation for styling tree nodes (0 = root, 1 = submenu, 2 = sub-submenu)
    public int getDepth() {
        int depth = 0;
        Menu p = this.parent;
        while (p != null) {
            depth++;
            p = p.getParent();
        }
        return depth;
    }

    // Dynamic tree character generator for hierarchical table display (e.g. ├── or └──)
    public String getTreePrefix() {
        if (this.parent == null) {
            return "";
        }
        boolean isLast = false;
        List<Menu> siblings = this.parent.getChildren();
        if (siblings != null && !siblings.isEmpty() && siblings.get(siblings.size() - 1).getId().equals(this.id)) {
            isLast = true;
        }
        
        StringBuilder prefix = new StringBuilder();
        int depth = getDepth();
        for (int i = 1; i < depth; i++) {
            // Non-breaking spaces for indentation
            prefix.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        prefix.append(isLast ? "└── " : "├── ");
        return prefix.toString();
    }
}
