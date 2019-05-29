package com.kalbob.app.project.service.impl;

import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.repository.ProjectRepository;
import com.kalbob.app.project.service.ProjectService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  public final ProjectRepository projectRepository;
  public final EmployeeRepository employeeRepository;

  public Optional<Project> create(Project project){
    //business validate, invoke other services, bundle transactions
    return Optional.of(projectRepository.saveAndFlush(project));
  }

  public Project findById(Long id) {//if input is null i.e. id is null should throw error from rest layer itself.
    return projectRepository.getById(id);
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
    return project.getEmployees();
  }

  public Project addEmployee(Long id, Long employeeId){
    return addEmployee(id, employeeRepository.getById(employeeId));
  }

  public Project addEmployee(Long id, Employee employee) {
    Project project = findById(id);
    return addEmployee(project, employee);
  }

  public Project addEmployee(Project project, Employee employee){
    project.addEmployee(employee);
    return projectRepository.saveAndFlush(project);
  }

  public Project addEmployee(Project project, Long employeeId){
    project.addEmployee(employeeRepository.getById(employeeId));
    return projectRepository.saveAndFlush(project);
  }

  public Project removeEmployee(Long id, Long employeeId) {
    Project project = findById(id);
    return removeEmployee(project, employeeId);
  }

  public Project removeEmployee(Project project, Long employeeId) {
    project.removeEmployee(employeeRepository.getById(employeeId));
    return projectRepository.saveAndFlush(project);
  }

  public Project addEmployeesByIds(Long id, Set<Long> employeeIds) {
    Project project = findById(id);
    employeeIds.forEach(employeeId -> addEmployee(project, employeeId));
    return findById(id);
  }

  public Project addEmployees(Long id, Set<Employee> employees) {//bulk insert stored procedure/saveAll/some spring data jpa way
    Project project = findById(id);
    employees.forEach(e -> addEmployee(project, e));
    return findById(id);
  }

  public void removeEmployees(Long id, Set<Long> employeeIds) {
    Project project = findById(id);
    employeeIds.forEach(e -> removeEmployee(project, e));
  }

  @Override
  public List<Project> getAllProjects() {
    return projectRepository.findAll();
  }
}
