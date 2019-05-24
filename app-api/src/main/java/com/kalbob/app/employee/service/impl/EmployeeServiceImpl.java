package com.kalbob.app.employee.service.impl;

import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.employee.service.EmployeeService;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectAssignment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  public final EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(
      EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

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

  public void joinProject(Long id, Project project) {
    Employee employee = findById(id);
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
    employeeRepository.saveAndFlush(employee);
  }

  public void leaveProject(Long id, Project project) {
    Employee employee = findById(id);
    if (employee.getProjectAssignments() != null) {
      Optional<ProjectAssignment> projectAssignment = employee.getProjectAssignments().stream()
          .filter(pa -> pa.getProject() == project && pa.getIsCurrent())
          .findAny();
      projectAssignment.map(pa -> {
        employee.getProjectAssignments().remove(pa);
        pa.setLeftDate(LocalDateTime.now());
        pa.setIsCurrent(false);
        employee.getProjectAssignments().add(pa);
        return employee;
      }).orElseThrow(ResourceNotFoundException::new);
    }
    employeeRepository.saveAndFlush(employee);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }
}
