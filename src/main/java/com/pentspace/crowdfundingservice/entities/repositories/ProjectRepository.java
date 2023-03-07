package com.pentspace.crowdfundingservice.entities.repositories;

import com.pentspace.crowdfundingservice.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
 List<Project> findByAccountId(String accountId);
}
