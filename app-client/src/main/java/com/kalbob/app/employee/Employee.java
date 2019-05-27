package com.kalbob.app.employee;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.Department;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectAssignment;
import com.kalbob.app.task.Task;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(exclude = {"reportees","departmentHeaded","projectAssignments","tasks"})
@EqualsAndHashCode(exclude = {"reportees","departmentHeaded","projectAssignments","tasks"}, callSuper = true)
@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  private JobType jobType;

  private Double salary;

  private Double commission;

  @Column(nullable = true, updatable = true)
  private LocalDate hireDate;

  private LocalDate relievingDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id")
  //@Setter(AccessLevel.NONE) & Remove the written setter
  //bcoz i'm not cascading..i can actually remove setDepartment() & use this reference just for getting.
  private Department department;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "manager_id")
  private Employee manager;

  @OneToOne(mappedBy = "departmentHead")
  public Department departmentHeaded;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "manager", fetch = FetchType.LAZY)
  private Set<Employee> reportees = new HashSet<>();

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "employee", fetch = FetchType.LAZY)
  private Set<ProjectAssignment> projectAssignments = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee", fetch = FetchType.LAZY)
  private Set<Task> tasks = new HashSet<>();

  public Employee setDepartment(Department department) {
    if(this.department != department) {
      if(this.department != null) this.department.removeEmployee(this);
      this.department = department;
      if(department != null) department.addEmployee(this);
    }
    return this;
  }

  public Employee removeDepartment() {
    if(this.department != null){
      this.department.removeEmployee(this);
      this.department = null;
    }
    return this;
  }

  public Employee setManager(Employee manager) {
    if(this.manager != manager) {
      if(this.manager != null) this.manager.removeReportee(this);
      this.manager = manager;
      if(manager != null) manager.addReportee(this);
    }
    return this;
  }

  public Employee removeManager() {
    if(this.manager != null){
      this.manager.removeReportee(this);
      this.manager = null;
    }
    return this;
  }

  public Employee setDepartmentHeaded(Department departmentHeaded) {
    if(this.departmentHeaded != departmentHeaded){
      if(this.departmentHeaded != null) this.departmentHeaded.removeDepartmentHead();//remove existing
      this.departmentHeaded = departmentHeaded;//appoint new
      if(departmentHeaded != null) departmentHeaded.setDepartmentHead(this);//intimate new guy
    }
    return this;
  }

  public Employee removeDepartmentHeaded() {
    if(this.departmentHeaded != null){
      this.departmentHeaded.setDepartmentHead(null);//intimate existing
      this.departmentHeaded = null;//remove
    }
    return this;
  }

  public Employee setReportees(Set<Employee> reportees) {
    if (reportees != null) {
      this.reportees = new HashSet<>(reportees);
      reportees.forEach(e -> e.setManager(this));
    }else{
      if (this.reportees != null) {
        this.reportees.forEach(Employee::removeManager);
        this.reportees = null;
      }
    }
    return this;
  }

  public Employee addReportee(@NonNull Employee reportee) {
    if (this.reportees == null) {
      this.reportees = new HashSet<>();
    }
    if(!this.reportees.contains(reportee)){
      this.reportees.add(reportee);
      reportee.setManager(this);
    }
    return this;
  }

  public Employee removeReportee(@NonNull Employee reportee) {
    if (this.reportees == null) {
      return this;
    }
    if(this.reportees.contains(reportee)) {
      Employee manager = reportee.getManager();
      manager.getReportees().stream().filter(t -> t.getManager() == this)
          .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
      this.reportees.remove(reportee);
      reportee.setManager(null);
    }
    return this;
  }

  public Employee removeReportees() {
    if (this.reportees != null) {
      this.reportees.forEach(Employee::removeManager);
      this.reportees = null;
    }
    return this;
  }

  public Set<Employee> getReportees() {
    return Collections.unmodifiableSet(this.reportees);
  }

  public Employee setTasks(Set<Task> tasks) {
    if (tasks != null) {
      this.tasks = new HashSet<>(tasks);
      tasks.forEach(e -> e.setEmployee(this));
    }else{
      if (this.tasks != null) {
        this.tasks.forEach(Task::removeEmployee);
        this.tasks = null;
      }
    }
    return this;
  }

  public Employee addTask(@NonNull Task task) {
    if (this.tasks == null) {
      this.tasks = new HashSet<>();
    }
    if(!this.tasks.contains(task)){
      this.tasks.add(task);
      task.setEmployee(this);
    }
    return this;
  }

  public Employee removeTask(@NonNull Task task) {
    if (this.tasks == null) {
      return this;
    }
    if(this.tasks.contains(task)) {
      Employee employee = task.getEmployee();
      employee.getTasks().stream().filter(t -> t.getEmployee() == this)
          .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
      this.tasks.remove(task);
      task.setEmployee(null);
    }
    return this;
  }

  public Employee removeTasks() {
    if (this.tasks != null) {
      this.tasks.forEach(Task::removeEmployee);
      this.tasks = null;
    }
    return this;
  }

  public Set<Task> getTasks() {
    return Collections.unmodifiableSet(this.tasks);
  }
  
  //-------------

  public Employee setProjects(Set<Project> projects) {
    if (projects != null) {
      this.projectAssignments = projects.stream().map(p -> new ProjectAssignment()
          .setProject(p)
          .setEmployee(this)
          .setJoinedDate(LocalDateTime.now())
          .setIsCurrent(true)
      ).collect(Collectors.toSet());
    }else{
      if(this.projectAssignments != null) {
        this.projectAssignments.forEach(ProjectAssignment::removeProject);
        this.projectAssignments = null;
      }
    }
    return this;
  }

  public Employee joinProject(@NonNull Project project) {
    if(this.projectAssignments == null){
      this.projectAssignments = new HashSet<>();
    }
    if(project.getProjectAssignments() == null){
      project.setProjectAssignments(new HashSet<>());
    }
    if(this.projectAssignments.stream().noneMatch(pa -> pa.getProject() == project)) {
      ProjectAssignment projectAssignment = new ProjectAssignment()
          .setEmployee(this)
          .setProject(project)
          .setJoinedDate(LocalDateTime.now())
          .setIsCurrent(true);
      this.projectAssignments.add(projectAssignment);
      project.getProjectAssignments().add(projectAssignment);
    }
    return this;
  }

  public Employee leaveProject(@NonNull Project project) {
    if(this.projectAssignments != null && project.getProjectAssignments() != null) {
      if(this.projectAssignments.stream().anyMatch(pa -> pa.getProject() == project)) {
        ProjectAssignment projectAssignment = project.getProjectAssignments().stream()
            .filter(pa -> pa.getEmployee() == this && pa.getIsCurrent())
            .findAny().orElseThrow(ResourceNotFoundException::new);
        this.projectAssignments.remove(projectAssignment);
        projectAssignment.setLeftDate(LocalDateTime.now());
        projectAssignment.setIsCurrent(false);
        this.projectAssignments.add(projectAssignment);
      }
    }
    return this;
  }

  public Set<Project> getProjects() {
    return Collections.unmodifiableSet(this.projectAssignments.stream()
        .map(ProjectAssignment::getProject)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet()));
  }

  public Employee addProjectAssignment(ProjectAssignment projectAssignment) {
    if (this.projectAssignments == null) {
      this.projectAssignments = new HashSet<>();
    }
    if(!this.projectAssignments.contains(projectAssignment)){
      this.projectAssignments.add(projectAssignment);
      projectAssignment.setEmployee(this);
    }
    return this;
  }

  public Employee removeProjectAssignment(ProjectAssignment projectAssignment) {
    if (this.projectAssignments == null) {
      return this;
    }
    if(this.projectAssignments.contains(projectAssignment)) {
      Employee employee = projectAssignment.getEmployee();
      employee.getProjectAssignments().stream().filter(t -> t.getEmployee() == this)
          .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
      this.projectAssignments.remove(projectAssignment);
      projectAssignment.setEmployee(null);
    }
    return this;
  }
  

  public static void main(String[] args){
    Employee e = new Employee();
    e.setDepartment(null);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Employee>> violations = validator.validate(e);
    for (ConstraintViolation<Employee> violation : violations) {
      System.out.println("Field errors "+violation.getMessage());
    }
    ExecutableValidator executableValidator = factory.getValidator().forExecutables();
    Method method = null;
    try {
      method = Employee.class
          .getMethod("setDepartment", Department.class);
    } catch (NoSuchMethodException ex) {
      ex.printStackTrace();
    }
    Object[] parameterValues = {null};
    violations
        = executableValidator.validateParameters(e, method, parameterValues);
    for (ConstraintViolation<Employee> violation : violations) {
      System.out.println("Method parameter errors "+violation.getMessage());
    }
    Project p = new Project();
    p.getEmployees().add(e);
  }

}
