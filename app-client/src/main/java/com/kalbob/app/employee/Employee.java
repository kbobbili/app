package com.kalbob.app.employee;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.Department;
import com.kalbob.app.project.ProjectAssignment;
import com.kalbob.app.task.Task;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@EqualsAndHashCode(exclude = {"reportees","departmentHeaded","projectAssignments","tasks"}, callSuper = false)
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

  public Employee setDepartment(Department department){
    if(department != null) department.getEmployees().add(this);
    this.department = department;
    return this;
  }

  public Employee removeDepartment() {
    if(department != null) {
      this.department.removeEmployee(this);
      this.department = null;
    }
    return this;
  }

  public Employee setDepartmentHeaded(Department departmentHeaded){
    if(departmentHeaded != null) departmentHeaded.setDepartmentHead_Internal(this);
    this.departmentHeaded = departmentHeaded;
    return this;
  }

  public Employee removeDepartmentHeaded() {
    if(departmentHeaded != null) {
      this.departmentHeaded.setDepartmentHead(null);
      this.departmentHeaded = null;
    }
    return this;
  }

  public Employee setManager(Employee manager){
    if(manager != null) manager.getReportees().add(this);
    this.manager = manager;
    return this;
  }

  public Employee removeManager() {
    if(manager != null) {
      this.manager.removeReportee(this);
      this.manager = null;
    }
    return this;
  }

  public Employee setReportees(List<Employee> reportees) {
    if (reportees != null) {
      reportees.forEach(e -> e.setManager(this));
      this.reportees = new HashSet<>(reportees);
    }else{
      removeReportees();
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
    if (this.reportees != null) {
      if (!this.reportees.contains(reportee))
        throw new ResourceNotFoundException();
      this.reportees.remove(reportee);
      reportee.setManager(null);
    }
    return this;
  }

  public Employee removeReportees() {
    if (this.reportees != null) {
      this.reportees.forEach(e->e.setManager(null));
      this.reportees = null;
    }
    return this;
  }

  public Set<Employee> getReportees() {
    return this.reportees;
  }

  public Employee setTasks(List<Task> tasks) {
    if (tasks != null) {
      tasks.forEach(e -> e.setEmployee(this));
      this.tasks = new HashSet<>(tasks);
    }else{
      removeTasks();
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
    if (this.tasks != null) {
      if (!this.tasks.contains(task))
        throw new ResourceNotFoundException();
      this.tasks.remove(task);
      task.setEmployee(null);
    }
    return this;
  }

  public Employee removeTasks() {
    if (this.tasks != null) {
      this.tasks.forEach(e->e.setEmployee(null));
      this.tasks = null;
    }
    return this;
  }

  public Set<Task> getTasks() {
    return this.tasks;
  }

  /*public Employee setProjects(List<Project> projects) {
    if (projects != null) {
      this.projectAssignments = projects.stream().map(p -> new ProjectAssignment()
          .setEmployee_Internal(this)
          .setProject(p)
          .setJoinedDate(LocalDateTime.now())
          .setIsCurrent(true)
      ).collect(Collectors.toSet());
      projects.forEach(p -> p.setProjectAssignments(projectAssignments));
    }else{
      if(this.projectAssignments != null) {
        this.projectAssignments.forEach(pa ->  {
          pa.getEmployee().setProjectAssignments(null);
          pa.getProject().setProjectAssignments(null);});
        this.projectAssignments = null;
      }
    }
    return this;
  }

  public Employee addProject(@NonNull Project project) {
    if(this.projectAssignments == null){
      this.projectAssignments = new HashSet<>();
    }
    if(project.getProjectAssignments() == null){
      project.setProjectAssignments(new HashSet<>());
    }
    if(this.projectAssignments.stream().anyMatch(pa -> pa.getProject() != null && pa.getProject().equals(project))) {
      throw new ResourceNotFoundException();//Duplicate Exception
    }else{
      ProjectAssignment projectAssignment = new ProjectAssignment()
          .setEmployee_Internal(this)
          .setProject(project)
          .setJoinedDate(LocalDateTime.now())
          .setIsCurrent(true);
      this.projectAssignments.add(projectAssignment);
      project.getProjectAssignments().add(projectAssignment);
    }
    return this;
  }

  public Employee removeProject(@NonNull Project project) {
    if(this.projectAssignments != null && project.getProjectAssignments() != null) {
      if(this.projectAssignments.stream().anyMatch(pa -> pa.getProject() != null && pa.getProject().equals(project))) {
        ProjectAssignment projectAssignment = project.getProjectAssignments().stream()
            .filter(pa -> pa.getEmployee() == this )
            .findAny().orElseThrow(ResourceNotFoundException::new);
        this.projectAssignments.remove(projectAssignment);
        project.getProjectAssignments().remove(projectAssignment);
        projectAssignment.setLeftDate(LocalDateTime.now());
        projectAssignment.setIsCurrent(false);
        this.projectAssignments.add(projectAssignment);
        project.getProjectAssignments().add(projectAssignment);
      }else{
        throw new ResourceNotFoundException();
      }
    }
    return this;
  }

  public List<Project> getProjects() {
    if(this.projectAssignments != null) {
      return this.projectAssignments.stream()
          .map(ProjectAssignment::getProject)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }else {
      return new ArrayList<>();
    }
  }*/

  public Employee setProjectAssignments(List<ProjectAssignment> projectAssignments) {
    if (projectAssignments != null) {
      projectAssignments.forEach(e -> e.setEmployee(this));
      this.projectAssignments = new HashSet<>(projectAssignments);
    }else{
      removeProjectAssignments();
    }
    return this;
  }

  public Set<ProjectAssignment> getProjectAssignments() {
    return this.projectAssignments;
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
    if (this.projectAssignments != null) {
      if (!this.projectAssignments.contains(projectAssignment))
        throw new ResourceNotFoundException();
      this.projectAssignments.remove(projectAssignment);
      projectAssignment.removeEmployee();
      //
      /*projectAssignment.setLeftDate(LocalDateTime.now());//meta-data or projectAssignment.setEmployee(null);
      projectAssignment.setIsCurrent(false);
      this.projectAssignments.add(projectAssignment);*/
    }
    return this;
  }

  public Employee removeProjectAssignments() {
    if (this.projectAssignments != null) {
      this.projectAssignments.forEach(e->e.setEmployee(null));
      this.projectAssignments = null;
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
  }

}
