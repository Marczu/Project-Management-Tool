package com.mejner.projectmanagertool.services;

import com.mejner.projectmanagertool.domain.Project;
import com.mejner.projectmanagertool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        return projectRepository.save(project);
    }
}
