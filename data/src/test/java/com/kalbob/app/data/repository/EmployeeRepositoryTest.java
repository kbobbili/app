package com.kalbob.app.data.repository;

import com.kalbob.app.data.config.DataConfiguration;
import com.kalbob.app.data.model.Address;
import com.kalbob.app.data.model.Department;
import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.model.Project;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DataConfiguration.class})
//@Import({BaseDataTestConfiguration.class})
public class EmployeeRepositoryTest {

    @Autowired
    private DataFactory dataFactory;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void saveDepartment() {
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        Employee employee = Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
        Project project = Project.builder()
                .name("Project RED")
                .build();
        Department department = Department.builder()
                .name("R&D")
                .build();
        department.assignAddress(address);
        employee.addProject(project);//or project.addEmployee(employee);
        department.addEmployee(employee);
        Department savedDepartment = departmentRepository.save(department);
        System.out.println(savedDepartment + "\n" + savedDepartment.getAddress() + "\n" + savedDepartment.getEmployees() + "\n" + savedDepartment.getEmployees().get(0).getProjects());
        Employee savedEmployee = employeeRepository.findAll().get(0);
        System.out.println(savedEmployee.getDepartment() + "\n" + savedEmployee.getProjects());
        Project savedProject = projectRepository.findAll().get(0);
        System.out.println(savedProject.getEmployees());
        Address savedAddress = addressRepository.findAll().get(0);
        System.out.println(savedAddress.getDepartment());
    }

    @Test
    public void saveEmployee() {
        Employee employee = Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
        Project project = Project.builder()
                .name("Project RED")
                .build();
        employee.addProject(project);//or project.addEmployee(employee);
        employeeRepository.saveAndFlush(employee);
    }

    @Test
    public void saveProject() {
        Employee employee = Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
        Project project = Project.builder()
                .name("Project RED")
                .build();
        project.addEmployee(employee);//or employee.addProject(project);
        projectRepository.saveAndFlush(project);
    }


    @Test
    public void saveAddress() {
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        addressRepository.saveAndFlush(address);
    }

    @Test
    public void deleteEmployeeWithRelationships() {
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        Employee employee = Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
        Project project = Project.builder()
                .name("Project RED")
                .build();
        Department department = Department.builder()
                .name("R&D")
                .build();
        department.assignAddress(address);
        employee.addProject(project);//or project.addEmployee(employee);
        department.addEmployee(employee);
        departmentRepository.save(department);
        //employeeRepository.delete(department.getEmployees().get(0));//delete(emp) needs @Transactional, @Modifying in a service class to work when emp has relations
        //employeeRepository.deleteById(department.getEmployees().get(0).getId());//even deleteById won't work if to-Many is there.
        Employee emp = employeeRepository.findAll().get(0);//If you want to delete the employee --> findById, then set projects = null, save, & then deleteById
        emp.setProjects(null);
        employeeRepository.deleteById(emp.getId());
    }

    @Test
    public void deleteAllEmployeesNoProjects() {//ManyToOne (emp->dept), deletes employees
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        Employee employee = Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
        Project project = Project.builder()
                .name("Project RED")
                .build();
        Department department = Department.builder()
                .name("R&D")
                .build();
        department.assignAddress(address);
        //employee.addProject(project);//or project.addEmployee(employee);
        department.addEmployee(employee);
        departmentRepository.save(department);
        employeeRepository.deleteAll();
    }

    @Test
    public void deleteAllEmployeesWhenProjectsItWont() {//ManyToMany, No error but won't delete
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        Employee employee = Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
        Project project = Project.builder()
                .name("Project RED")
                .build();
        Department department = Department.builder()
                .name("R&D")
                .build();
        department.assignAddress(address);
        employee.addProject(project);//or project.addEmployee(employee);
        department.addEmployee(employee);
        departmentRepository.save(department);
        employeeRepository.deleteAll();
    }

    //Better to write query methods for this case
    @Test
    public void deleteAllProjectsOfEmployee() {//ManyToMany So, manually delete
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        Employee employee = Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
        Project project = Project.builder()
                .name("Project RED")
                .build();
        Department department = Department.builder()
                .name("R&D")
                .build();
        department.assignAddress(address);
        employee.addProject(project);//or project.addEmployee(employee);
        department.addEmployee(employee);
        departmentRepository.save(department);
        employeeRepository.save(employee);
        Employee emp = employeeRepository.findAll().get(0);//If you want to delete the employee --> findById, then set projects = null, save, & then deleteById
        emp.setProjects(null);
        employeeRepository.delete(emp);//save, if you just want to remove the association
        //employeeRepository.deleteById(emp.getId()); //if you want to delete the employee, then take the id in the rest call
    }

    @Test
    public void deleteAllEmployeesWithProjects() {//ManyToMany So, manually delete
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        Employee employee = Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
        Project project = Project.builder()
                .name("Project RED")
                .build();
        Department department = Department.builder()
                .name("R&D")
                .build();
        department.assignAddress(address);
        employee.addProject(project);//or project.addEmployee(employee);
        department.addEmployee(employee);
        departmentRepository.save(department);
        List<Employee> emps = employeeRepository.findAll();
        for(Employee emp: emps){
            emp.setProjects(null);
        }
        employeeRepository.deleteAll(); //Won't delete
        //Below 2 stmts work
        employeeRepository.saveAll(emps);
        employeeRepository.deleteAll();
    }


}
