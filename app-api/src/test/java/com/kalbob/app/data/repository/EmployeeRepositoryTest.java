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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DataConfiguration.class})
//@Import({DataTestConfiguration.class})
public class EmployeeRepositoryTest {

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
        Department department = DepartmentMother.simple().build();
        department.assignAddress(AddressMother.simple().build());
        department.addEmployee(EmployeeMother.simple().build());
        department = departmentRepository.save(department);
        assertTrue(department.getId() != null);
        assertTrue(department.getAddress() != null);
        assertTrue(department.getEmployees().size() == 1);
    }

    @Test
    public void saveEmployee() {
        Employee employee = EmployeeMother.complete().build();
        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employee.getId() != null);
    }

    @Test
    public void saveProject() {
        Project project = ProjectMother.complete().build();
        projectRepository.saveAndFlush(project);
        assertTrue(project.getId() != null);
    }


    @Test
    public void saveAddress() {
        Address address = AddressMother.complete().build();
        addressRepository.saveAndFlush(address);
        assertTrue(address.getId() != null);
    }

    @Test
    public void deleteDepartmentById() {
        Department department = DepartmentMother.simple().build();
        department.assignAddress(AddressMother.simple().build());
        department.addEmployee(EmployeeMother.simple().build());
        department = departmentRepository.saveAndFlush(department);
        assertTrue(departmentRepository.findById(department.getId()).isPresent());
        departmentRepository.deleteById(department.getId());
        assertTrue(!departmentRepository.findById(department.getId()).isPresent());
    }

    @Test
    public void deleteEmployeeById() {
        Employee employee = EmployeeMother.complete().build();
        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employeeRepository.findById(employee.getId()).isPresent());
        employeeRepository.deleteById(employee.getId());
        assertTrue(!employeeRepository.findById(employee.getId()).isPresent());
    }

    @Test
    public void deleteProjectById() {
        Project project = ProjectMother.complete().build();
        project = projectRepository.saveAndFlush(project);
        assertTrue(projectRepository.findById(project.getId()).isPresent());
        projectRepository.deleteById(project.getId());
        assertTrue(!projectRepository.findById(project.getId()).isPresent());
    }
    

    @Test
    public void deleteAddressById() {
        Address address = AddressMother.complete().build();
        address = addressRepository.saveAndFlush(address);
        assertTrue(addressRepository.findById(address.getId()).isPresent());
        addressRepository.deleteById(address.getId());
        assertTrue(!addressRepository.findById(address.getId()).isPresent());
    }

    @Test
    public void removeAddress() {//Department deletes record from address
        Department department = DepartmentMother.simple().build();
        department.assignAddress(AddressMother.simple().build());
        department.addEmployee(EmployeeMother.simple().build());
        department = departmentRepository.saveAndFlush(department);
        assertTrue(departmentRepository.findById(department.getId()).get().getAddress()!=null);
        department.removeAddress();//with orphanRemoval = true. Note: department.setAddress(null) can also be used in case of OneToOne
        department = departmentRepository.saveAndFlush(department);
        assertTrue(departmentRepository.findById(department.getId()).get().getAddress()==null);

        //or no other way except, you use addressRepository to delete the address.
        //addressRepository.deleteById(departmentRepository.findById(department.getId()).get().getAddress().getId()); Finish!
    }

    @Test
    @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    @Rollback(false)
    public void removeEmployee() {//Manual delete record from employee
        Department department = DepartmentMother.simple().build();
        department.assignAddress(AddressMother.simple().build());
        Employee employee = EmployeeMother.simple().build();
        department.addEmployee(EmployeeMother.simple().build());
        department = departmentRepository.saveAndFlush(department);
        assertTrue(departmentRepository.findById(department.getId()).get().getEmployees().size()==1);//Use @Transactional to avoid -> org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role
        //department.removeEmployee(employee);//does not work because i don't have orphanRemoval enabled (remember, cascade doesn't work for setters)
        //department = departmentRepository.saveAndFlush(department);
        //assertTrue(departmentRepository.findById(department.getId()).get().getEmployees().size()==0);

        // so need to use employeeRepository to delete either the association (or) the complete employee record.
        employee.setDepartment(null);
        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employeeRepository.findById(employee.getId()).get().getDepartment()==null);
        employeeRepository.deleteById(departmentRepository.findById(department.getId()).get().getEmployees().get(0).getId());
        assertTrue(!employeeRepository.findById(employee.getId()).isPresent());
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
        employee.removeProject(employeeRepository.findById(employee.getId()).get().getProjects().get(0));
        //employee.setProjects(null);//works without the concept of orphan removal, because both are owning sides
        employee = employeeRepository.saveAndFlush(employee);
        assertTrue(employeeRepository.findById(employee.getId()).get().getProjects().size()==0);
    }

    @Test
    public void removeDepartmentFromAddress() {//Address deletes its association with department by setting department_id to null
        Address address = AddressMother.complete().build();
        address = addressRepository.saveAndFlush(address);
        assertTrue(addressRepository.findById(address.getId()).get().getDepartment()!=null);
        address.setDepartment(null);
        address = addressRepository.saveAndFlush(address);
        assertTrue(addressRepository.findById(address.getId()).get().getDepartment()==null);
    }

}
