package com.mejner.projectmanagertool.services;

import com.mejner.projectmanagertool.domain.Project;
import com.mejner.projectmanagertool.exceptions.ProjectIdException;
import com.mejner.projectmanagertool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        }catch (Exception e){
            /*check id projectId is unique, if not, throw exception*/
            throw new ProjectIdException("Projekt o ID '" + project.getProjectIdentifier().toUpperCase() + "' juz istnieje");
        }
    }

    public Project findProjectByIdentifier(String projectId){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        /*If project does not exist, throw exception*/
        if(project==null){
            throw new ProjectIdException("Projekt o ID '" + projectId.toUpperCase() + "' nie istnieje");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(){
        Iterable<Project> allProjects = projectRepository.findAll();

        return allProjects;
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null){
            throw new ProjectIdException("Nie można usunąć, projekt o ID '" + projectId.toUpperCase() + "' nie istnieje");
        }
        projectRepository.delete(project);
    }

}
