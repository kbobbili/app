package com.kalbob.app.employee.service.impl;

import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.employee.service.EmployeeService;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.repository.ProjectRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  public final EmployeeRepository employeeRepository;
  public final ProjectRepository projectRepository;

  public Employee findById(Long id) {
    return employeeRepository.getById(id);
  }

  public Set<Project> getProjects(Long id) {
    Employee employee = findById(id);
    //return employee.getProjects();
    return null;
  }

  public Employee joinProject(Long id, Long projectId) {
    Employee employee = findById(id);
    return joinProject(employee, projectId);
  }

  public Employee joinProject(Employee employee, Long projectId){
    Project project = projectRepository.getById(projectId);
    //employee.joinProject(project);
    return employeeRepository.saveAndFlush(employee);
  }

  public Employee joinProjects(Long id, Set<Long> projectIds) {
    Employee employee = findById(id);
    projectIds.forEach(projectId -> joinProject(employee, projectId));
    return findById(id);
  }

  public Employee leaveProject(Long id, Long projectId) {
    Employee employee = findById(id);
    return leaveProject(employee, projectId);
  }

  public Employee leaveProject(Employee employee, Long projectId){
    Project project = projectRepository.getById(projectId);
    //employee.leaveProject(project);
    return employeeRepository.saveAndFlush(employee);
  }

  public Employee leaveProjects(Long id, Set<Long> projectIds) {
    Employee employee = findById(id);
    projectIds.forEach(projectId -> leaveProject(employee, projectId));
    return findById(id);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }
}
