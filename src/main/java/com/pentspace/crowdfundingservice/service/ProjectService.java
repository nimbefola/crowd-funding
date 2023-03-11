package com.pentspace.crowdfundingservice.service;

import com.pentspace.crowdfundingservice.dto.FundProjectDTO;
import com.pentspace.crowdfundingservice.entities.Project;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project);
    String fundProject(FundProjectDTO fundProjectDTO);
    Project getById(String id);
    List<Project> getByAccountId(String id);
    List<Project> getAll();
    Project updateStatus(String projectId, String status);
    Project uploadProjectPicture(String id, MultipartFile multipartFile);
}
