package com.example.menumanagement.dto;

import java.util.List;

public class MenuDTO {
    private Long id;
    private String name;
    private String url;
    private String icon;
    private Integer displayOrder;
    private Boolean status;
    private Long parentId;
    private List<MenuDTO> children;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    
    public List<MenuDTO> getChildren() { return children; }
    public void setChildren(List<MenuDTO> children) { this.children = children; }
}
