package com.proglint.camundaquickstart;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Underwriter {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Underwriter() {
    }

    public Underwriter(String name) {
        this.name = name;
    }

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
}
