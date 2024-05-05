package com.startups.repo;

import com.startups.model.Applications;
import com.startups.model.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationsRepo extends JpaRepository<Applications, Long> {

    List<Applications> findAllByStatusAndOwner_Id(ApplicationStatus status, Long id);

    List<Applications> findAllByStatus(ApplicationStatus status);


}
