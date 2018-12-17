package com.mejner.projectmanagertool.web;

import com.mejner.projectmanagertool.domain.Project;
import com.mejner.projectmanagertool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result){

        Map<String, String> errorMap = new HashMap<>();

        for(FieldError error: result.getFieldErrors()){
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        if(result.hasErrors()){
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        projectService.saveOrUpdateProject(project);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

}
