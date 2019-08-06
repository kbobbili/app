package com.kalbob.reactive.department.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kalbob.reactive.config.data.BaseRepositoryIT;
import com.kalbob.reactive.department.AddressMother;
import com.kalbob.reactive.department.Department;
import com.kalbob.reactive.department.DepartmentMother;
import com.kalbob.reactive.employee.Employee;
import com.kalbob.reactive.employee.EmployeeMother;
import com.kalbob.reactive.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class DepartmentRepositoryIT extends BaseRepositoryIT {

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Test
  public void saveDepartment() {

    Department department = DepartmentMother.simple();
    department.setAddress(AddressMother.simple());
    department.addEmployee(EmployeeMother.simple());
    department = departmentRepository.save(department);
    assertNotNull(department.getId());
    assertNotNull(department.getAddress());
    assertEquals(1, department.getEmployees().size());
  }

  @Test
  public void deleteDepartmentById() {
    Department department = DepartmentMother.simple();
    department.setAddress(AddressMother.simple());
    department.addEmployee(EmployeeMother.simple());
    department = departmentRepository.saveAndFlush(department);
    assertTrue(departmentRepository.findById(department.getId()).isPresent());
    departmentRepository.deleteById(department.getId());
    assertFalse(departmentRepository.findById(department.getId()).isPresent());
  }


  @Test
  @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
  @Rollback(false)
  public void removeAddress() {//Department deletes record from address
    Department department = DepartmentMother.simple();
    department.setAddress(AddressMother.simple());
    department.addEmployee(EmployeeMother.simple());
    department = departmentRepository.saveAndFlush(department);
    assertNotNull(departmentRepository.findById(department.getId()).get().getAddress());

    //1 with orphanRemoval = true. Note: department.setAddress(null) can also be used in case of OneToOne
    department.removeAddress();
    department = departmentRepository.saveAndFlush(department);
    assertNull(departmentRepository.findById(department.getId()).get().getAddress());

    //i'm not sure why this is not working in this transaction context.
    //2 or else no other way except, you use addressRepository to delete the address. Finish!
    //addressRepository.deleteById(departmentRepository.findById(department.getId()).get().getAddress().getId());
  }

  @Test
  @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
  @Rollback(false)
  public void removeEmployee() {//Manual delete record from employee
    Department department = DepartmentMother.simple();
    department.setAddress(AddressMother.simple());
    department.addEmployee(EmployeeMother.simple());
    department = departmentRepository.saveAndFlush(department);
    assertEquals(1, departmentRepository.findById(department.getId()).get().getEmployees()
        .size());//Use @Transactional to avoid -> org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role
    //department.removeEmployee(employee);//does not work because i don't have orphanRemoval enabled (remember, cascade doesn't work for setters)
    //department = departmentRepository.saveAndFlush(department);
    //assertTrue(departmentRepository.findById(department.getId()).get().getEmployees().size()==0);

    // so need to use employeeRepository to delete either the association (or) the complete employee record.
    department.getEmployees().get(0).setDepartment(null);
    Employee employee = employeeRepository.saveAndFlush(department.getEmployees().get(0));
    assertNull(employeeRepository.findById(employee.getId()).get().getDepartment());

    //i'm not sure why this is not working in this transaction context.
    //2 or else no other way except, you use employeeRepository to delete the employee. Finish!
     /*employeeRepository.deleteById(department.getEmployees().get(0).getId());
     assertTrue(!employeeRepository.findById(department.getEmployees().get(0).getId()).isPresent());*/
  }

}
