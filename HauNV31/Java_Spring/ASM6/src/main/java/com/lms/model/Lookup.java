package com.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_lookup")
public class Lookup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String code;

    @Column(name = "lookup_value")
    private String value;

    public Lookup() {}

    public Lookup(String type, String code, String value) {
        this.type = type;
        this.code = code;
        this.value = value;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lookup{id=").append(id)
          .append(", type='").append(type).append('\'')
          .append(", code='").append(code).append('\'')
          .append(", value='").append(value).append('\'')
          .append('}');
        return sb.toString();
    }
}
