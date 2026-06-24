package com.example.demo.util;

public class FieldDescriptor {
    private String name;
    private String label;
    private String type;
    private Object value;

    public FieldDescriptor(String name, String label, String type, Object value) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
