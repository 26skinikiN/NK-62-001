package com.investment.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Project implements Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;
    private String date;
    private String founder;
    private String photo;
    private float need;
    private String description;

    @ManyToOne
    private AppUser owner;

    public Project(String name, String date, String description, String founder, float need, AppUser owner) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.founder = founder;
        this.need = need;
        this.owner = owner;
    }

    public void set(String name, String date, String description, String founder, float need) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.founder = founder;
        this.need = need;
    }
}