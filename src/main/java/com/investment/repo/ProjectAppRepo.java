package com.investment.repo;

import com.investment.model.ProjectApp;
import com.investment.model.enums.ProjectAppStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAppRepo extends JpaRepository<ProjectApp, Long> {
    List<ProjectApp> findAllByStatus(ProjectAppStatus status);
}