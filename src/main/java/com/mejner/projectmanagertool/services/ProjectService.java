package com.mejner.projectmanagertool.services;

import com.mejner.projectmanagertool.domain.Backlog;
import com.mejner.projectmanagertool.domain.Project;
import com.mejner.projectmanagertool.domain.User;
import com.mejner.projectmanagertool.exceptions.ProjectIdException;
import com.mejner.projectmanagertool.exceptions.ProjectNotFoundException;
import com.mejner.projectmanagertool.repositories.BacklogRepository;
import com.mejner.projectmanagertool.repositories.ProjectRepository;
import com.mejner.projectmanagertool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){

        if(project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("Nie znaleziono projektu na Twoim koncie");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("Projekt o ID: '" + project.getProjectIdentifier() + "' nie moze zostać uaktualniony, ponieważ projekt nie istnieje");
            }
        }

        try{

            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }catch (Exception e){
            /*check id projectId is unique, if not, throw exception*/
            throw new ProjectIdException("Projekt o ID '" + project.getProjectIdentifier().toUpperCase() + "' juz istnieje");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        /*If project does not exist, throw exception*/
        if(project==null){
            throw new ProjectIdException("Projekt o ID '" + projectId.toUpperCase() + "' nie istnieje");
        }

//        System.out.println("PLeadr: " + project.getProjectLeader() + " -- username : " + username);
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Nie znaleziono projektu na tym koncie");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        Iterable<Project> allProjects = projectRepository.findAllByProjectLeader(username);

        return allProjects;
    }

    public void deleteProjectByIdentifier(String projectId, String username){

        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }

}
