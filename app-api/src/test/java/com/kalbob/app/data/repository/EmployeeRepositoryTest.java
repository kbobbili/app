package com.kalbob.app.data.repository;

import com.kalbob.app.data.DataConfiguration;
import com.kalbob.app.data.model.Department;
import com.kalbob.app.data.model.DepartmentMother;
import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.model.EmployeeMother;
import com.kalbob.app.data.model.Project;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {DataConfiguration.class})
/*@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)*/
//@Import({DataTestConfiguration.class})
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeAll
    public static void beforeAll() throws Exception {
    }

    @AfterAll
    public static void afterAll() throws Exception {
    }

    @BeforeEach
    public void beforeEach() throws Exception {
    }

    @AfterEach
    public void afterEach() throws Exception {
    }


    @Test
    public void saveEmployee() {
        Employee employee = EmployeeMother.complete().build();
        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employee.getId() != null);
    }

    @Test
    public void deleteEmployeeById() {
        Employee employee = EmployeeMother.simple().build();
        employee.setDepartment(DepartmentMother.simple().build());
        employee = employeeRepository.saveAndFlush(employee);
        //assertTrue(employeeRepository.findById(employee.getId()).isPresent());
        employeeRepository.deleteById(employee.getId());
        //assertTrue(!employeeRepository.findById(employee.getId()).isPresent());
    }

    //Rest all cases are straight-forward, because they are all owning sides.
    //In Employee -> removing Department, Project
    //In Project -> removing Employee
    //In Address -> removing Department

    @Test
    public void removeDepartment() {//Employee deletes its association with department by setting department_id to null
        Employee employee = EmployeeMother.complete().build();
        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employeeRepository.findById(employee.getId()).get().getDepartment()!=null);
        employee.setDepartment(null);
        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employeeRepository.findById(employee.getId()).get().getDepartment()==null);
    }

    @Test
    public void removeProject() {//Employee deletes its association with department by setting department_id to null
        Employee employee = EmployeeMother.complete().build();
        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employeeRepository.findById(employee.getId()).get().getProjects().size()!=0);

        Project project = employeeRepository.findById(employee.getId()).get().getProjects().get(0);
        employee.removeProject(project);
        project.removeEmployee(employee);
        //employee.setProjects(null);//works without the concept of orphan removal, because both are owning sides

        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employeeRepository.findById(employee.getId()).get().getProjects().size()==0);
    }


    @Test
    public void findByDepartment_Name(){

        Employee employee1 = EmployeeMother.completeRandom().build();
        Department department = DepartmentMother.simple().build();
        employee1.setDepartment(department);

        Employee employee2 = EmployeeMother.completeRandom().build();

        Employee employee3 = EmployeeMother.completeRandom().build();
        employee3.setDepartment(department);

        List<Employee> employees = employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3));
        assertTrue(employeeRepository.findByDepartment_NameIgnoreCase(department.getName()).size()==2);
    }

}
