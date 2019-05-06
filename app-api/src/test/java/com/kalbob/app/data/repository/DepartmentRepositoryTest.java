package com.kalbob.app.data.repository;

import com.kalbob.app.data.DataConfiguration;
import com.kalbob.app.data.model.AddressMother;
import com.kalbob.app.data.model.Department;
import com.kalbob.app.data.model.DepartmentMother;
import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.model.EmployeeMother;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {DataConfiguration.class})
//@Import({DataTestConfiguration.class})
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeAll
    public static void beforeAll() {

    }

    @AfterAll
    public static void afterAll() {
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

}
