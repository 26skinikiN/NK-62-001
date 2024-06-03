package com.investment.model;

import com.investment.model.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails, Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String username;
    private String password;

    private String fio;
    private boolean theme = true;
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<>();
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ProjectApp> projectApps = new ArrayList<>();
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<ProjectApp> projectAppsAdmin = new ArrayList<>();

    public AppUser(String username, String password, String fio) {
        this.username = username;
        this.password = passwordEncoder().encode(password);
        this.fio = fio;
    }

    public void setPassword(String password) {
        this.password = passwordEncoder().encode(password);
    }

    public boolean checkApps(Long id) {
        for (ProjectApp app : projectApps) {
            if (app.getId().equals(id)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}