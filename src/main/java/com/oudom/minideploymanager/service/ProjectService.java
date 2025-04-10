package com.oudom.minideploymanager.service;

import com.oudom.minideploymanager.model.Project;
import com.oudom.minideploymanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project addProject(Project project) {
        project.setDeploymentStatus("Pending");
        project.setDeployedUrl("");
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Optional<Project> existingProject = projectRepository.findById(id);
        if (existingProject.isEmpty()) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        Project project = existingProject.get();
        project.setName(projectDetails.getName());
        project.setGitUrl(projectDetails.getGitUrl());
        project.setDeploymentStatus(projectDetails.getDeploymentStatus());
        project.setDeployedUrl(projectDetails.getDeployedUrl());
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    public Project getProjectById(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        return projectOptional.orElse(null);
    }

    public String getDeploymentStatus(long id) {
        Optional<Project> projectOpt = projectRepository.findById((long) id);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            return "Status: " + project.getDeploymentStatus() + ", URL: " + project.getDeployedUrl();
        } else {
            return "Project not found.";
        }
    }

    public boolean isGitUrlValid(String gitUrl) {
        try {
            URL url = new URL(gitUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD"); // Use HEAD request to only check if it's reachable
            connection.setConnectTimeout(5000); // Timeout in 5 seconds
            connection.setReadTimeout(5000);    // Timeout in 5 seconds

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK; // 200 OK means it's valid
        } catch (Exception e) {
            // Return false if there's any error (invalid URL or network issue)
            return false;
        }
    }

    public String triggerDeployment(long id) {
        Optional<Project> projectOpt = projectRepository.findById((long) id);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();

            // Validate the Git URL
            if (!isGitUrlValid(project.getGitUrl())) {
                return "❌ Invalid Git URL. Unable to trigger deployment.";
            }

            // Simulate deployment success if URL is valid
            project.setDeploymentStatus("Deployed");
            String fakeUrl = "https://deploys.fake/" + project.getName().replaceAll("\\s+", "-").toLowerCase();
            project.setDeployedUrl(fakeUrl);

            projectRepository.save(project);
            return "✅ Deployment triggered successfully!";
        } else {
            return "Project not found.";
        }
    }



}











