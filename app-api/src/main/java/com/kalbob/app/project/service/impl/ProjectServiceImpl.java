package com.kalbob.app.project.service.impl;

import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectAssignment;
import com.kalbob.app.project.repository.ProjectRepository;
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
public class ProjectServiceImpl implements ProjectService {

  public final ProjectRepository projectRepository;
  public final EmployeeService employeeService;

  public Optional<Project> create(Project project){
    //business validate, invoke other services, bundle transactions
    return Optional.of(projectRepository.saveAndFlush(project)).orElseThrow(null);//exp
  }

  public Project findById(Long id) {//if input is null i.e. id is null should throw error from rest layer itself.
    Optional<Project> project = projectRepository.findById(id);
    return project.orElseThrow(ResourceNotFoundException::new);
  }

  public Project update(Long id, Project project){
    Project existingProject = findById(id);
    //merge, business validate, invoke other services, bundle transactions
    return projectRepository.saveAndFlush(existingProject);
  }

  public void delete(Long id) {
    findById(id);
    projectRepository.deleteById(id);
  }

  public List<Employee> getEmployees(Long id) {
    Project project = findById(id);
    if (project.getProjectAssignments() != null) {
      return project.getProjectAssignments().stream()
          .map(ProjectAssignment::getEmployee)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  public Project addEmployee(Long id, Employee employee) {
    Project project = findById(id);
    ProjectAssignment projectAssignment = new ProjectAssignment()
        .setEmployee(employee)
        .setProject(project)
        .setJoinedDate(LocalDateTime.now())
        .setIsCurrent(true)
        ;
    if (project.getProjectAssignments() != null) {
      project.getProjectAssignments().add(projectAssignment);
      if (employee.getProjectAssignments() != null) {
        employee.getProjectAssignments().add(projectAssignment);
      }
    }
    return projectRepository.saveAndFlush(project);
  }

  public void addEmployee(Long id, Long employeeId){
    addEmployee(id, employeeService.findById(employeeId));
  }

  public Project removeEmployee(Long id, Long employeeId) {
    //REPLACE with methods from projectAssignmentService
    Project project = findById(id);
    Employee employee = employeeService.findById(employeeId);
    if (project.getProjectAssignments() != null) {
      ProjectAssignment projectAssignment = project.getProjectAssignments().stream()
          .filter(pa -> pa.getEmployee() == employee && pa.getIsCurrent())
          .findAny().orElseThrow(ResourceNotFoundException::new);//message customize
      project.getProjectAssignments().remove(projectAssignment);
      projectAssignment.setLeftDate(LocalDateTime.now());
      projectAssignment.setIsCurrent(false);
      project.getProjectAssignments().add(projectAssignment);
    }
    return projectRepository.saveAndFlush(project);
  }

  public Project addEmployeesByIds(Long id, List<Long> employeeIds) {
    employeeIds.forEach(employeeId -> addEmployee(id, employeeId));
    return findById(id);
  }

  public Project addEmployees(Long id, List<Employee> employees) {
    employees.forEach(e->this.addEmployee(id, e));
    return findById(id);
  }

  public void removeEmployees(Long id, List<Long> employeeIds) {
    employeeIds.forEach(e->this.removeEmployee(id, e));
  }

  @Override
  public List<Project> getAllProjects() {
    return projectRepository.findAll();
  }
}
