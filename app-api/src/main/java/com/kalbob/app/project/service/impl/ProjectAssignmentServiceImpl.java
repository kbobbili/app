package com.kalbob.app.project.service.impl;

import com.kalbob.app.project.ProjectAssignment;
import com.kalbob.app.project.repository.ProjectAssignmentRepository;
import com.kalbob.app.project.service.EmployeeService;
import com.kalbob.app.project.service.ProjectAssignmentService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectAssignmentServiceImpl implements ProjectAssignmentService {

  public final ProjectAssignmentRepository projectAssignmentRepository;
  public final EmployeeService employeeService;

  public ProjectAssignment create(ProjectAssignment projectAssignment){
    //business validate, invoke other services, bundle transactions
    return projectAssignmentRepository.saveAndFlush(projectAssignment);
  }

  public ProjectAssignment findById(Long id) {
    Optional<ProjectAssignment> projectAssignment = projectAssignmentRepository.findById(id);
    return projectAssignment.orElseThrow(ResourceNotFoundException::new);
  }

  public ProjectAssignment update(Long id, ProjectAssignment projectAssignment){
    ProjectAssignment existingProjectAssignment = findById(id);
    //merge, business validate, invoke other services, bundle transactions
    return projectAssignmentRepository.saveAndFlush(existingProjectAssignment);
  }

  public Optional<ProjectAssignment> delete(Long id, Boolean isSoftDelete) {
    ProjectAssignment projectAssignment = findById(id);
    if(isSoftDelete){
      projectAssignment.setLeftDate(LocalDateTime.now());
      projectAssignment.setIsCurrent(false);
      return Optional.of(projectAssignmentRepository.saveAndFlush(projectAssignment));
    }else {
      projectAssignmentRepository.deleteById(id);
      return Optional.empty();
    }
  }
  
}
