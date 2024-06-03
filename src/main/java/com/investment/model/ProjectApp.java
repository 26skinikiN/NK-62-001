package com.investment.model;

import com.investment.model.enums.ProjectAppStatus;
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
public class ProjectApp implements Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String date;
    private float sum;

    @Enumerated(EnumType.STRING)
    private ProjectAppStatus status = ProjectAppStatus.WAITING;

    @ManyToOne
    private Project project;
    @ManyToOne
    private AppUser owner;
    @ManyToOne
    private AppUser admin = null;

    public ProjectApp(String date, float sum, Project project, AppUser owner) {
        this.date = date;
        this.sum = sum;
        this.project = project;
        this.owner = owner;
    }
}