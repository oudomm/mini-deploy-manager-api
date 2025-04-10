package com.oudom.minideploymanager.controller;

import com.oudom.minideploymanager.model.Project;
import com.oudom.minideploymanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Get all projects", description = "Fetch all the projects stored in the system.")
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Add a new project", description = "Create a new project by providing its name and Git URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody @Parameter(description = "Details of the new project to add") Project project) {
        Project createdProject = projectService.addProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a project by ID", description = "Fetch a project by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(
            @PathVariable @Parameter(description = "ID of the project to retrieve") Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Update a project", description = "Update the details of an existing project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,
                                                 @RequestBody @Parameter(description = "Updated project details") Project projectDetails) {
        Project updatedProject = projectService.updateProject(id, projectDetails);
        if (updatedProject == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updatedProject);
    }

    @Operation(summary = "Delete a project", description = "Delete a project by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Project deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with ID: " + id);
        }
    }

    @Operation(summary = "Get deployment status", description = "Retrieve the deployment status for a project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deployment status fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{id}/deploy")
    public ResponseEntity<String> getDeploymentStatus(
            @PathVariable @Parameter(description = "ID of the project to retrieve deployment status") int id) {
        String status = projectService.getDeploymentStatus(id);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }
        return ResponseEntity.ok(status);
    }

    @Operation(summary = "Trigger a deployment", description = "Simulate a deployment trigger for a project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deployment triggered successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PostMapping("/{id}/deploy")
    public ResponseEntity<String> triggerDeployment(@PathVariable int id) {
        String response = projectService.triggerDeployment(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }
        return ResponseEntity.ok(response);
    }
}
