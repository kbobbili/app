package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.company.Company;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@ToString(exclude = {"employees", "projectManagements"})
@EqualsAndHashCode(exclude = {"employees", "projectManagements"}, callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "department")
public class Department extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  @Enumerated(EnumType.STRING)
  private DepartmentType type;

  private LocalDate startDate;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "department_head")
  public Employee departmentHead;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
  private Set<Employee> employees = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
  private Set<ProjectManagement> projectManagements = new HashSet<>();

  public Department setAddress(Address address){
    if(address != null) address.setDepartment(this);
    this.address = address;
    return this;
  }

  public Department setAddress_Internal(Address address){
    this.address = address;
    return this;
  }

  public Department removeAddress() {
    if(this.address != null) {
      this.address.setDepartment(null);
      this.address = null;
    }
    return this;
  }

  public Department setEmployees(List<Employee> employees) {
    if (employees != null) {
      employees.forEach(e -> e.setDepartment(this));
      this.employees = new HashSet<>(employees);
    }else{
      removeEmployees();
    }
    return this;
  }

  public Department addEmployee(@NonNull Employee employee) {
    if (this.employees == null) {
      this.employees = new HashSet<>();
    }
    if(!this.employees.contains(employee)){
      this.employees.add(employee);
      employee.setDepartment(this);
    }
    return this;
  }

  public Department removeEmployee(@NonNull Employee employee) {
    if (this.employees != null) {
      if (!this.employees.contains(employee))
        throw new ResourceNotFoundException();
      this.employees.remove(employee);
      employee.setDepartment(null);
    }
    return this;
  }

  public Department removeEmployees() {
    if (this.employees != null) {
      this.employees.forEach(e->e.setDepartment(null));
      this.employees = null;
    }
    return this;
  }

  public List<Employee> getEmployees() {
    if(this.employees != null) {
      return new ArrayList<>(this.employees);
    }else{
      return new ArrayList<>();
    }
  }

  public Department setCompany(Company company){
    if(company != null) company.getDepartments().add(this);
    this.company = company;
    return this;
  }

  public Department removeCompany() {
    if(company != null) {
      this.company.removeDepartment(this);
      this.company = null;
    }
    return this;
  }

  public Department setDepartmentHead(Employee departmentHead){
    if(departmentHead != null) departmentHead.setDepartment(this);
    this.departmentHead = departmentHead;
    return this;
  }

  public Department setDepartmentHead_Internal(Employee departmentHead){
    this.departmentHead = departmentHead;
    return this;
  }

  public Department removeDepartmentHead() {
    if(this.departmentHead != null) {
      this.departmentHead.setDepartment(null);
      this.departmentHead = null;
    }
    return this;
  }
  
  public Department setProjects(List<Project> projects) {
    if (projects != null) {
      this.projectManagements = projects.stream().map(p -> {
        ProjectManagement pm = new ProjectManagement()
          .setDepartment(this)
          .setProject(p)
          .setRating(3);
        p.setProjectManagement(pm);//
        return pm;
      }).collect(Collectors.toSet());

    }else{
      if(this.projectManagements != null) {
        this.projectManagements.forEach(pa -> {
          if(pa.getProject() != null) {
            pa.getProject().setProjectManagement(null);
            pa.setDepartment(null);
          }
        });
        this.projectManagements = null;
      }
    }
    return this;
  }

  public Department addProject(@NonNull Project project) {
    if(this.projectManagements == null){
      this.projectManagements = new HashSet<>();
    }
    if(this.projectManagements.stream().anyMatch(pa -> pa.getProject()!=null && pa.getProject().equals(project))) {
      throw new ResourceNotFoundException();//Duplicate Exception, Already managing that project.
    }else{
      ProjectManagement projectManagement = new ProjectManagement()
          .setDepartment_Internal(this)
          .setProject(project)
          .setRating(3);
      this.projectManagements.add(projectManagement);
      project.setProjectManagement(projectManagement);
    }
    return this;
  }

  public Department removeProject(@NonNull Project project) {
    if (this.projectManagements == null || project.getProjectManagement() == null) {
      return this;
    }
    ProjectManagement projectManagement = project.getProjectManagement();
    if(projectManagement.getDepartment() != this) throw new ResourceNotFoundException(); //association not found i.e. this department does not manage that project
    this.projectManagements.remove(projectManagement);
    project.setProjectManagement(null);
    return this;
  }

  public List<Project> getProjects() {
    if(this.projectManagements != null) {
      return this.projectManagements.stream()
          .map(ProjectManagement::getProject)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }else {
      return new ArrayList<>();
    }
  }

  public Department addProjectManagement(@NonNull ProjectManagement projectManagement) {
    if(this.projectManagements == null){
      this.projectManagements = new HashSet<>();
    }
    if(this.projectManagements.stream().noneMatch(pm -> pm.equals(projectManagement))) {
      this.projectManagements.add(projectManagement);
      projectManagement.setDepartment(this);
    }
    return this;
  }

  public Department removeProjectManagement(@NonNull ProjectManagement projectManagement) {
    if (this.projectManagements == null || projectManagement.getDepartment() == null) {
      return this;//exp not managing anything
    }
    if(!projectManagement.getDepartment().equals(this)) throw new ResourceNotFoundException(); //association not found i.e. this department does not manage that project
    this.projectManagements.remove(projectManagement);
    projectManagement.setDepartment(null);
    return this;
  }


  public static void main(String[] args){
    Department d = new Department();
    d.setType(DepartmentType.FINANCE);
    Address a = new Address();
    a.setStreet("tatum");

    Employee e1 = new Employee();
    e1.setFirstName("kal");
    Employee e2 = new Employee();
    e2.setFirstName("kal");
    Project p1 = new Project();
    p1.setName(ProjectName.RED);
    Project p2 = new Project();
    p2.setName(ProjectName.BLUE);

    /*d.setAddress(a);
    d.removeAddress();
    a.setDepartment(d);
    a.removeDepartment();
    System.out.println(d.getAddress());
    System.out.println(a.getDepartment());*/


    /*//d.setEmployees(Arrays.asList(e1, e2));
    //d.addEmployee(e1);
    //d.removeEmployee(e1);
    //e1.setDepartment(d);
    //e1.removeDepartment();
    d.removeEmployees();
    System.out.println(d.getEmployees());
    System.out.println(e1.getDepartment());*/

    /*ProjectManagement pm = new ProjectManagement();
    pm.setDepartment(d);
    pm.setProject(p1);
    if(d.getProjectManagements()!=null && !d.getProjectManagements().isEmpty())System.out.println("pm.d == d.pm.d "+pm.getDepartment().equals(new ArrayList<>(d.getProjectManagements()).get(0).getDepartment()));
    if(d.getProjectManagements()!=null && !d.getProjectManagements().isEmpty())System.out.println("pm.p == d.pm.p "+pm.getProject().equals(new ArrayList<>(d.getProjectManagements()).get(0).getProject()));
    if(d.getProjects()!=null && !d.getProjects().isEmpty()) System.out.println("d.p "+d.getProjects().contains(p1));
    if(p1.getProjectManagement()!=null && p1.getProjectManagement().getDepartment()!=null) System.out.println("p.d "+p1.getProjectManagement().getDepartment().equals(d));
    pm.removeDepartment();//Removing references
    //pm.removeProject();
    System.out.println(d);
    System.out.println(p1);
    if(d.getProjectManagements()!=null && !d.getProjectManagements().isEmpty() && new ArrayList<>(d.getProjectManagements()).get(0).getDepartment()!=null) System.out.println("pm.d == d.pm.d "+pm.getDepartment().equals(new ArrayList<>(d.getProjectManagements()).get(0).getDepartment()));
    if(d.getProjectManagements()!=null && !d.getProjectManagements().isEmpty() && new ArrayList<>(d.getProjectManagements()).get(0).getProject()!=null) System.out.println("pm.p == d.pm.p "+pm.getProject().equals(new ArrayList<>(d.getProjectManagements()).get(0).getProject()));
    if(d.getProjects()!=null && !d.getProjects().isEmpty()) System.out.println("d.p "+d.getProjects().contains(p1));
    if(p1.getProjectManagement()!=null && p1.getProjectManagement().getDepartment()!=null) System.out.println("p.d "+p1.getProjectManagement().getDepartment().equals(d));*/

    /*//d.setProjects(Arrays.asList(p1));//Sets both sides
    //d.addProject(p1);
    p1.setDepartment(d);
    if(d.getProjectManagements()!=null && !d.getProjectManagements().isEmpty())System.out.println("d == d.pm.d "+d.equals(new ArrayList<>(d.getProjectManagements()).get(0).getDepartment()));
    if(d.getProjectManagements()!=null && !d.getProjectManagements().isEmpty())System.out.println("p == d.pm.p "+p1.equals(new ArrayList<>(d.getProjectManagements()).get(0).getProject()));
    if(d.getProjects()!=null && !d.getProjects().isEmpty()) System.out.println("d.p "+d.getProjects().contains(p1));
    if(p1.getProjectManagement()!=null && p1.getProjectManagement().getDepartment()!=null) System.out.println("p.d "+p1.getProjectManagement().getDepartment().equals(d));
    d.removeProject(p1);//Removing references both sides  i.e. removeProject = removeProjectManagement from both sides of relation.
    //p1.removeDepartment();
    //d.setProjects(null);
    System.out.println(d);
    System.out.println(p1);
    if(d.getProjectManagements()!=null && !d.getProjectManagements().isEmpty())System.out.println("d == d.pm.d "+d.equals(new ArrayList<>(d.getProjectManagements()).get(0).getDepartment()));
    if(d.getProjectManagements()!=null && !d.getProjectManagements().isEmpty())System.out.println("p == d.pm.p "+p1.equals(new ArrayList<>(d.getProjectManagements()).get(0).getProject()));
    if(d.getProjects()!=null && !d.getProjects().isEmpty()) System.out.println("d.p "+d.getProjects().contains(p1));
    if(p1.getProjectManagement()!=null && p1.getProjectManagement().getDepartment()!=null) System.out.println("p.d "+p1.getProjectManagement().getDepartment().equals(d));*/


    /*ProjectAssignment pa = new ProjectAssignment();
    pa.setProject(p1);//Only sets for project & no employee is set until next line, so first println errors.
    //pa.setEmployee(e1);
    if(e1.getProjectAssignments()!=null && !e1.getProjectAssignments().isEmpty()) System.out.println(e1.getProjectAssignments().contains(new ArrayList<>(e1.getProjectAssignments()).get(0))+" e.pa "+e1.getProjectAssignments().size());
    if(p1.getProjectAssignments()!=null && !p1.getProjectAssignments().isEmpty()) System.out.println(p1.getProjectAssignments().contains(new ArrayList<>(p1.getProjectAssignments()).get(0))+" p.pa "+p1.getProjectAssignments().size());
    System.out.println(e1.getProjects().contains(p1)+" e.p "+e1.getProjects().size());
    System.out.println(p1.getEmployees().contains(e1) +" p.e "+p1.getEmployees().size());
    System.out.println("pa.e "+(pa.getEmployee() != null));
    System.out.println("pa.p "+(pa.getProject() != null));
    pa.removeEmployee();//Removing references
    //pa.removeProject();
    if(e1.getProjectAssignments()!=null && !e1.getProjectAssignments().isEmpty())System.out.println(e1.getProjectAssignments().contains(new ArrayList<>(e1.getProjectAssignments()).get(0))+" e.pa "+e1.getProjectAssignments().size());
    if(p1.getProjectAssignments()!=null && !p1.getProjectAssignments().isEmpty())System.out.println(p1.getProjectAssignments().contains(new ArrayList<>(p1.getProjectAssignments()).get(0))+" p.pa "+p1.getProjectAssignments().size());//When pa.removeEmployee() -> contains is false, bcoz project still holds the reference to pa unlike employee
    if(e1.getProjects() != null && !e1.getProjects().isEmpty())System.out.println(e1.getProjects().contains(p1)+" e.p "+e1.getProjects().size());
    if(p1.getEmployees() != null && !p1.getEmployees().isEmpty())System.out.println(p1.getEmployees().contains(e1) +" p.e "+p1.getEmployees().size());
    System.out.println("pa.e "+(pa.getEmployee() == null));
    System.out.println("pa.p "+(pa.getProject() == null));*/

    /*p1.setEmployees(Arrays.asList(e1));
    //e1.setProjects(Arrays.asList(p1));
    System.out.println(e1.getProjectAssignments().contains(new ArrayList<>(e1.getProjectAssignments()).get(0))+" e.pa "+e1.getProjectAssignments().size());
    System.out.println(p1.getProjectAssignments().contains(new ArrayList<>(p1.getProjectAssignments()).get(0))+" p.pa "+p1.getProjectAssignments().size());
    System.out.println(e1.getProjects().contains(p1)+" e.p "+e1.getProjects().size());
    System.out.println(p1.getEmployees().contains(e1) +" p.e "+p1.getEmployees().size());
    System.out.println(e1.getProjectAssignments());
    p1.removeEmployee(e1);//Changing meta-data
    //e1.removeProject(p1);
    //e1.setProjects(null);//Removing references
    if(e1.getProjectAssignments()!=null && !e1.getProjectAssignments().isEmpty())System.out.println(e1.getProjectAssignments().contains(new ArrayList<>(e1.getProjectAssignments()).get(0))+" e.pa "+e1.getProjectAssignments().size());
    if(p1.getProjectAssignments()!=null && !p1.getProjectAssignments().isEmpty())System.out.println(p1.getProjectAssignments().contains(new ArrayList<>(p1.getProjectAssignments()).get(0))+" p.pa "+p1.getProjectAssignments().size());
    if(e1.getProjects() != null && !e1.getProjects().isEmpty())System.out.println(e1.getProjects().contains(p1)+" e.p "+e1.getProjects().size());
    if(p1.getEmployees() != null && !p1.getEmployees().isEmpty())System.out.println(p1.getEmployees().contains(e1) +" p.e "+p1.getEmployees().size());
    System.out.println(e1.getProjectAssignments());*/
  }

}
