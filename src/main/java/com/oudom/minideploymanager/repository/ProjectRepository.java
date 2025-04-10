package com.oudom.minideploymanager.repository;

import com.oudom.minideploymanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
