package com.mejner.projectmanagertool.services;

import com.mejner.projectmanagertool.domain.Backlog;
import com.mejner.projectmanagertool.domain.ProjectTask;
import com.mejner.projectmanagertool.repositories.BacklogRepository;
import com.mejner.projectmanagertool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

        projectTask.setBacklog(backlog);

        //sequence is projectidentifier-PTSequence, for example ABCD-1 ABCD-2
        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;

        //add Sequence to projectTask
        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);

        projectTask.setProjectIdentifier(projectIdentifier);

        //SET initial priority when is null
        if(projectTask.getPriority() == 0 || projectTask.getPriority() == null){
            projectTask.setPriority(3);
        }

        //SET initial status when is null
        if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }
}
