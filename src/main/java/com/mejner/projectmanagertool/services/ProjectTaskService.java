package com.mejner.projectmanagertool.services;

import com.mejner.projectmanagertool.domain.Backlog;
import com.mejner.projectmanagertool.domain.Project;
import com.mejner.projectmanagertool.domain.ProjectTask;
import com.mejner.projectmanagertool.exceptions.ProjectNotFoundException;
import com.mejner.projectmanagertool.repositories.BacklogRepository;
import com.mejner.projectmanagertool.repositories.ProjectRepository;
import com.mejner.projectmanagertool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        try{
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

            projectTask.setBacklog(backlog);

            //sequence is projectidentifier-PTSequence, for example ABCD-1 ABCD-2
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            //add Sequence to projectTask
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);

            projectTask.setProjectIdentifier(projectIdentifier);

            //SET initial priority when is null
            if(projectTask.getPriority() == 0 || projectTask.getPriority() == null){
                projectTask.setPriority(3);
            }

            //SET initial status when is null
            if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("DO ZROBIENIA");
            }

            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Projekt nie został znaleziony");
        }

    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);

        if(project == null){
            throw new ProjectNotFoundException("Projekt o ID: '" + id + "' nie istnieje");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTBySequence(String backlog_id, String pt_id){
        //Check if backlog exist
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog == null){
            throw new ProjectNotFoundException("Projekt o ID: '" + backlog_id + "' nie istnieje");
        }

        //Check if project task exist
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask == null){
            throw new ProjectNotFoundException("Zadanie projektu " + pt_id + " nie istnieje");
        }
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Zadanie projektu " + pt_id + " nie istnieje w projekcie: " + backlog_id);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){
        ProjectTask projectTask = findPTBySequence(backlog_id, pt_id);
        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTBySequence(backlog_id, pt_id);

        projectTaskRepository.delete(projectTask);
    }
}
