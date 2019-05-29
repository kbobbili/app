package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.company.Company;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectAssignment;
import com.kalbob.app.project.ProjectName;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    this.address = address;
    if(address != null) address.setDepartment(this);
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
      employees.forEach(this::addEmployee);
    }else{
      removeEmployees();
    }
    return this;
  }

  public void addEmployee(Employee employee) {
    employee.setDepartment(this);
  }
  public void removeEmployee(Employee employee) {
    employee.setDepartment(null);
  }

  public void addEmployee_Internal(Employee employee) {
    if(this.employees == null) this.employees = new HashSet<>();
    this.employees.add(employee);
  }
  public void removeEmployee_Internal(Employee employee) {
    if(this.employees == null) this.employees = new HashSet<>();
    this.employees.remove(employee);
  }

  public Department removeEmployees() {
    if (this.employees == null) {
      throw new ResourceNotFoundException();//no employees
    }
    this.employees.forEach(e->e.setDepartment(null));
    this.employees = null;
    return this;
  }

  public Set<Employee> getEmployees() {
    return this.employees;
  }

  public Department setCompany(Company company){
    this.company = company;
    if(company != null) company.getDepartments().add(this);
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
    this.departmentHead = departmentHead;
    if(departmentHead != null) departmentHead.setDepartment(this);
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

  public Department setProjectManagements(List<ProjectManagement> projectManagements) {
    if (projectManagements != null) {
      this.projectManagements = new HashSet<>(projectManagements);
      projectManagements.forEach(e -> e.setDepartment(this));
    }else{
      removeProjectManagements();
    }
    return this;
  }

  public Set<ProjectManagement> getProjectManagements() {
    return this.projectManagements;
  }

  public Department removeProjectManagements() {
    if (this.projectManagements != null) {
      this.projectManagements.forEach(e->e.setDepartment(null));
      this.projectManagements = null;
    }
    return this;
  }

  public Department addProjectManagement(@NonNull ProjectManagement projectManagement) {
    if(this.projectManagements == null){
      this.projectManagements = new HashSet<>();
    }
    if(this.projectManagements.stream().noneMatch(pm -> pm.equals(projectManagement))) {
      projectManagement.setDepartment(this);
      this.projectManagements.add(projectManagement);
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
    e2.setFirstName("bob");
    Project p1 = new Project();
    p1.setName(ProjectName.RED);
    Project p2 = new Project();
    p2.setName(ProjectName.BLUE);

    //OnetoOne
    /*d.setAddress(a);
    //a.setDepartment(d);
    printOneToOne(d, a);
    System.out.println("----------------------------------------------------------------------------------");
    //d.removeAddress();
    //a.removeDepartment();
    printOneToOne(d, a);*/


    //OneToMany
    /*d.setEmployees(Arrays.asList(e1));
    //d.addEmployee(e1);
    //e1.setDepartment(d);
    printOneToMany(d, e1);
    System.out.println("----------------------------------------------------------------------------------");
    //d.removeEmployees();
    d.removeEmployee(e1);
    //e1.removeDepartment();
    printOneToMany(d, e1);*/

    //OneToMany Join Table
    ProjectManagement pm = new ProjectManagement();
    pm.setDepartment(d);
    pm.setProject(p1);
    printOneToManyJoinTable(pm, d);
    System.out.println("----------------------------------------------------------------------------------");
    pm.removeDepartment();
    //pm.removeProject();
    printOneToManyJoinTable(pm, d);

    //ManyToMany
    /*ProjectAssignment pa = new ProjectAssignment();
    pa.setProject(p1);
    pa.setEmployee(e1);
    printManyToManyJoinTable(pa, p1, e1);
    System.out.println("----------------------------------------------------------------------------------");
    pa.removeProject();//Actual delete of pa
    pa.removeEmployee();
    printManyToManyJoinTable(pa, p1, e1);*/
  }

  private static void printOneToOne(Department d, Address a) {
    System.out.println("d "+d);
    if(d.getAddress() != null) System.out.print("d.getAddress() != null |");
    if(a.getDepartment() != null) System.out.print("a.getDepartment() != null ");
    System.out.println();
    if(d.getAddress() != null) System.out.println("d.getAddress().equals(a) "+d.getAddress().equals(a));
    if(a.getDepartment() != null) System.out.println("a.getDepartment().equals(d)"+a.getDepartment().equals(d));
  }

  private static void printOneToMany(Department d, Employee e1){
    System.out.println("d "+d);
    if(d.getEmployees() != null) System.out.print("d.getEmployees() != null |");
    if(e1.getDepartment() != null) System.out.print("e1.getDepartment() != null |");
    System.out.println();
    if(d.getEmployees() != null) System.out.println("d.getEmployees().contains(e1) "+d.getEmployees().contains(e1));
    if(e1.getDepartment() != null) System.out.println("e1.getDepartment().equals(d) "+e1.getDepartment().equals(d));
  }

  private static void printOneToManyJoinTable(ProjectManagement pm, Department d){
    System.out.println("pm "+pm);
    if(pm.getDepartment() != null) System.out.print("pm.getDepartment() != null |");
    if(d.getProjectManagements() != null) System.out.print("d.getProjectManagements() != null |");
    if (d.getProjectManagements() != null && !d.getProjectManagements().isEmpty()
        && d.getProjectManagements().iterator().next().getDepartment() != null) {
      System.out.print("d.getProjectManagements().getDepartment() != null ");
    }
    System.out.println();
    if(pm.getDepartment() != null && d.getProjectManagements() != null && !d.getProjectManagements().isEmpty() && d.getProjectManagements().iterator().next().getDepartment() != null) System.out.println("pm.getDepartment() == d.getProjectManagements().getDepartment() "+pm.getDepartment().equals(d.getProjectManagements().iterator().next().getDepartment()));
    if(pm.getProject() != null && d.getProjectManagements() != null && !d.getProjectManagements().isEmpty() && d.getProjectManagements().iterator().next().getProject() != null)System.out.println("pm.getProject() == d.getProjectManagements().getProject() "+pm.getProject().equals(d.getProjectManagements().iterator().next().getProject()));
  }

  private static void printManyToManyJoinTable(ProjectAssignment pa, Project p1, Employee e1){
    System.out.println("pa "+pa);
    if(pa.getProject() != null) System.out.print("pa.getProject() != null |");
    if(p1.getProjectAssignments() != null) System.out.print("p1.getProjectAssignments() != null |");
    if (p1.getProjectAssignments() != null && !p1.getProjectAssignments().isEmpty()
        && p1.getProjectAssignments().iterator().next().getProject() != null) {
      System.out.print("p1.getProjectAssignments().getProject() != null ");
    }
    System.out.println();
    if(pa.getEmployee() != null) System.out.print("pa.getEmployee() != null |");
    if(e1.getProjectAssignments() != null) System.out.print("e1.getProjectAssignments() != null |");
    if (e1.getProjectAssignments() != null && !e1.getProjectAssignments().isEmpty()
        && e1.getProjectAssignments().iterator().next().getEmployee() != null) {
      System.out.print("e1.getProjectAssignments().getEmployee() != null ");
    }
    System.out.println();
    if(pa.getProject()!= null && e1.getProjectAssignments()!=null && !e1.getProjectAssignments().isEmpty() && e1.getProjectAssignments().iterator().next().getProject()!=null) System.out.println("pa.getProject() == e1.getProjectAssignments().getProject() "+pa.getProject().equals(e1.getProjectAssignments().iterator().next().getProject()));
    if(pa.getEmployee()!= null && e1.getProjectAssignments()!=null && !e1.getProjectAssignments().isEmpty() && e1.getProjectAssignments().iterator().next().getEmployee()!=null)System.out.println("pa.getEmployee() == e1.getProjectAssignments().getEmployee() "+pa.getEmployee().equals(e1.getProjectAssignments().iterator().next().getEmployee()));
    if(pa.getProject()!= null && p1.getProjectAssignments()!=null && !p1.getProjectAssignments().isEmpty() && p1.getProjectAssignments().iterator().next().getProject()!=null) System.out.println("pa.getEmployee() == p1.getProjectAssignments().getProject() "+pa.getProject().equals(p1.getProjectAssignments().iterator().next().getProject()));
    if(pa.getEmployee()!= null && p1.getProjectAssignments()!=null && !p1.getProjectAssignments().isEmpty() && p1.getProjectAssignments().iterator().next().getEmployee()!=null)System.out.println("pa.getEmployee() == p1.getProjectAssignments().getEmployee() "+pa.getEmployee().equals(p1.getProjectAssignments().iterator().next().getEmployee()));
  }


}
