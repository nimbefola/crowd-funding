package com.pentspace.crowdfundingservice.endpoints;

import com.pentspace.crowdfundingservice.entities.Project;
import com.pentspace.crowdfundingservice.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "project")
public class ProjectEndpoint {
    @Autowired
    ProjectService projectService;

    @PostMapping( consumes = "application/json", produces = "application/json")
    public ResponseEntity<Project> create(@RequestBody Project project){
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @GetMapping(path = "{projectId}", produces = "application/json")
    public ResponseEntity<Project> get(@PathVariable ("projectId") String projectId){
        return ResponseEntity.ok(projectService.getById(projectId));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Project>> getAll(){
        return ResponseEntity.ok(projectService.getAll());
    }

    @PostMapping(path = "/fund", produces = "application/json")
    public ResponseEntity<String> fund(@RequestParam("projectId") String projectId, @RequestParam("accountId") String accountId,@RequestParam("amount") String amount){
        return ResponseEntity.ok(projectService.fundProject(projectId, accountId, amount));
    }

    @GetMapping(path = "account/{id}", produces = "application/json")
    public ResponseEntity<List<Project>> getByAccountId(@PathVariable ("id") String id){
        return ResponseEntity.ok(projectService.getByAccountId(id));
    }

    @PostMapping(path = "/picture/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> uploadProjectPicture(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(projectService.uploadProjectPicture(id, file),HttpStatus.OK);
    }

    @PutMapping(path = "/status/update", produces = "application/json")
    public ResponseEntity<Project> updateProjectStatus(@RequestParam("id") String id, @RequestParam("status") String status) {
        return new ResponseEntity<>(projectService.updateStatus(id, status), HttpStatus.OK);
    }

}
