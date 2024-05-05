package com.startups.repo;

import com.startups.model.Startups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartupsRepo extends JpaRepository<Startups, Long> {
    List<Startups> findAllByNameContaining(String name);
}
