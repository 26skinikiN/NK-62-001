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
public class Startups implements Components {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable= false, updatable = false)
    private Long id;

    private String name;
    private String photo;
    private String description;
    private int cores;
    private float frequencyMain;
    private float frequencyMax;
    private float cacheL2;
    private float cacheL3;
    private int technicalProcess;
    private int tdp;
    private float price;

    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Applications> applications;

    public Startups(String name, String description, int cores, float frequencyMain, float frequencyMax, float cacheL2, float cacheL3, int technicalProcess, int tdp, float price, String photo) {
        this.name = name;
        this.description = description;
        this.cores = cores;
        this.frequencyMain = frequencyMain;
        this.frequencyMax = frequencyMax;
        this.cacheL2 = cacheL2;
        this.cacheL3 = cacheL3;
        this.technicalProcess = technicalProcess;
        this.tdp = tdp;
        this.price = price;
        this.photo = photo;
    }

    public void set(String name, String description, int cores, float frequencyMain, float frequencyMax, float cacheL2, float cacheL3, int technicalProcess, int tdp, float price) {
        this.name = name;
        this.description = description;
        this.cores = cores;
        this.frequencyMain = frequencyMain;
        this.frequencyMax = frequencyMax;
        this.cacheL2 = cacheL2;
        this.cacheL3 = cacheL3;
        this.technicalProcess = technicalProcess;
        this.tdp = tdp;
        this.price = price;
    }

    @Override
    public List<String> getInfo() {
        return new ArrayList<>();
    }

    @Override
    public int getOrderingDeliveredCount() {
        return applications.stream().reduce(0, (i, application) -> {
            if (application.getStatus() == ApplicationStatus.DELIVERED) return i + 1;
            return i;
        }, Integer::sum);
    }
}
