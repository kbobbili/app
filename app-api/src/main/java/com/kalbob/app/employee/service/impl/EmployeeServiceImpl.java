package com.kalbob.app.employee.service.impl;

import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectAssignment;
import com.kalbob.app.project.service.EmployeeService;
import com.kalbob.app.project.service.ProjectService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  public final EmployeeRepository employeeRepository;
  public final ProjectService projectService;

  public Employee findById(Long id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    return employee.orElseThrow(ResourceNotFoundException::new);
  }

  public List<Project> getProjects(Long id) {
    Employee employee = findById(id);
    if (employee.getProjectAssignments() != null) {
      return employee.getProjectAssignments().stream()
          .map(ProjectAssignment::getProject)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  public Employee joinProject(Long id, Long projectId) {
    Employee employee = findById(id);
    Project project = projectService.findById(projectId);
    ProjectAssignment projectAssignment = new ProjectAssignment()
        .setEmployee(employee)
        .setProject(project)
        .setJoinedDate(LocalDateTime.now())
        .setIsCurrent(true)
        ;
    if (employee.getProjectAssignments() != null) {
      employee.getProjectAssignments().add(projectAssignment);
      if (project.getProjectAssignments() != null) {
        project.getProjectAssignments().add(projectAssignment);
      }
    }
    return employeeRepository.saveAndFlush(employee);
  }

  public Employee joinProjects(Long id, List<Long> projectIds) {
    projectIds.forEach(projectId -> joinProject(id, projectId));
    return findById(id);
  }

  public Employee leaveProject(Long id, Long projectId) {
    Employee employee = findById(id);
    Project project = projectService.findById(projectId);
    if (employee.getProjectAssignments() != null) {
      ProjectAssignment projectAssignment = project.getProjectAssignments().stream()
          .filter(pa -> pa.getEmployee() == employee && pa.getIsCurrent())
          .findAny().orElseThrow(ResourceNotFoundException::new);
      employee.getProjectAssignments().remove(projectAssignment);
      projectAssignment.setLeftDate(LocalDateTime.now());
      projectAssignment.setIsCurrent(false);
      employee.getProjectAssignments().add(projectAssignment);
    }
    return employeeRepository.saveAndFlush(employee);
  }

  public Employee leaveProjects(Long id, List<Long> projectIds) {
    projectIds.forEach(projectId -> leaveProject(id, projectId));
    return findById(id);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }
}
