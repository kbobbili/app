package com.kalbob.app.department.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kalbob.app.config.data.BaseRepositoryIT;
import com.kalbob.app.department.Department;
import com.kalbob.app.department.DepartmentMother;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentRepositoryIT extends BaseRepositoryIT {

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Test
  public void saveDepartment() {

    Department d = new Department();
    d.addEmployee(new Employee());

    Department department = departmentRepository.save(DepartmentMother.completeRandom());
    assertTrue(departmentRepository.findById(department.getId()).isPresent());
    assertNotNull(department.getAddress());
    assertEquals(2, department.getEmployees().size());
    //assertEquals(2, department.getProjects().size());
  }

  @Test
  public void deleteDepartmentById() {

    Department department = departmentRepository.save(DepartmentMother.complete());
    departmentRepository.deleteById(department.getId());
    assertFalse(departmentRepository.findById(department.getId()).isPresent());
  }

  /*@Test
  @Transactional
  @Rollback(false)
  public void removeAddress() {
    Department department = departmentRepository.save(DepartmentMother.simple());
    assertTrue(departmentRepository.findById(department.getId()).isPresent());
    assertNotNull(department.getAddress());

    department.removeAddress(); //orphanRemoval = true, so, NOTE: department.setAddress(null) can also be used in case of OneToOne
    department = departmentRepository.saveAndFlush(department);
    assertNull(department.getAddress());

    //2
    department.setAddress(null);
    addressRepository.deleteById(department.getAddress().getId());
  }

  *//*@Test
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
    //assertTrue(departmentRepository.findById(department.getId()).get().getReportees().size()==0);

    // so need to use employeeRepository to delete either the association (or) the complete employee record.
    department.getEmployees().get(0).setDepartment(null);
    Employee employee = employeeRepository.saveAndFlush(department.getEmployees().get(0));
    assertNull(employeeRepository.findById(employee.getId()).get().getDepartment());

    //i'm not sure why this is not working in this transaction context.
    //2 or else no other way except, you use employeeRepository to delete the employee. Finish!
     *//**//*employeeRepository.deleteById(department.getReportees().get(0).getId());
     assertTrue(!employeeRepository.findById(department.getReportees().get(0).getId()).isPresent());*//**//*
  }*/

}
