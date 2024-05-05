package com.startups.model;

import com.startups.model.enums.ApplicationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Applications {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users owner;
    @ManyToOne(fetch = FetchType.LAZY)
    private Startups startup;


    public Applications(Users owner, Startups startup) {
        this.status = ApplicationStatus.REGISTRATION;
        this.owner = owner;
        this.startup = startup;
    }



    public String getName() {
        if (startup != null) return startup.getName();
        return "";
    }

    public String getDescription() {
        if (startup != null) return startup.getDescription();
        return "";
    }

    public String getPhoto() {
        if (startup != null) return startup.getPhoto();
        return "";
    }

    public List<String> getInfo() {
        if (startup != null) return startup.getInfo();
        return new ArrayList<>();
    }
}
