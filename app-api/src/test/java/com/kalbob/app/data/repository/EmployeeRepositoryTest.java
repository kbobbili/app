package com.kalbob.app.data.repository;

import com.kalbob.app.data.DataConfiguration;
import com.kalbob.app.data.model.Address;
import com.kalbob.app.data.model.AddressMother;
import com.kalbob.app.data.model.Department;
import com.kalbob.app.data.model.DepartmentMother;
import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.model.EmployeeMother;
import com.kalbob.app.data.model.Project;
import com.kalbob.app.data.model.ProjectMother;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DataConfiguration.class})
//@Import({DataTestConfiguration.class})
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

        Address address = AddressMother.complete().build();
        Employee employee = EmployeeMother.simple().build();
        Project project = ProjectMother.simple().build();
        Department department = DepartmentMother.simple().build();
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




}
