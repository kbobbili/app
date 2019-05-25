package com.kalbob.app.employee.service.impl;

import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.employee.service.EmployeeService;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.service.ProjectService;
import java.util.List;
import java.util.Optional;
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
    return employee.getProjects();
  }

  public Employee joinProject(Long id, Long projectId) {
    Employee employee = findById(id);
    return joinProject(employee, projectId);
  }

  public Employee joinProject(Employee employee, Long projectId){
    Project project = projectService.findById(projectId);
    employee.joinProject(project);
    return employeeRepository.saveAndFlush(employee);
  }

  public Employee joinProjects(Long id, List<Long> projectIds) {
    Employee employee = findById(id);
    projectIds.forEach(projectId -> joinProject(employee, projectId));
    return findById(id);
  }

  public Employee leaveProject(Long id, Long projectId) {
    Employee employee = findById(id);
    return leaveProject(employee, projectId);
  }

  public Employee leaveProject(Employee employee, Long projectId){
    Project project = projectService.findById(projectId);
    employee.leaveProject(project);
    return employeeRepository.saveAndFlush(employee);
  }

  public Employee leaveProjects(Long id, List<Long> projectIds) {
    Employee employee = findById(id);
    projectIds.forEach(projectId -> leaveProject(employee, projectId));
    return findById(id);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }
}
