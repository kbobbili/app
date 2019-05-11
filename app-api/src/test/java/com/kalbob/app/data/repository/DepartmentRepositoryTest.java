package com.kalbob.app.data.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kalbob.app.data.model.AddressMother;
import com.kalbob.app.data.model.Department;
import com.kalbob.app.data.model.DepartmentMother;
import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.model.EmployeeMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class DepartmentRepositoryTest extends AbstractRepositoryTest {

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
    assertTrue(department.getId() != null);
    assertTrue(department.getAddress() != null);
    assertTrue(department.getEmployees().size() == 1);
  }

  @Test
  public void deleteDepartmentById() {
    Department department = DepartmentMother.simple();
    department.setAddress(AddressMother.simple());
    department.addEmployee(EmployeeMother.simple());
    department = departmentRepository.saveAndFlush(department);
    assertTrue(departmentRepository.findById(department.getId()).isPresent());
    departmentRepository.deleteById(department.getId());
    assertTrue(!departmentRepository.findById(department.getId()).isPresent());
  }


  @Test
  @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
  @Rollback(false)
  public void removeAddress() {//Department deletes record from address
    Department department = DepartmentMother.simple();
    department.setAddress(AddressMother.simple());
    department.addEmployee(EmployeeMother.simple());
    department = departmentRepository.saveAndFlush(department);
    assertTrue(departmentRepository.findById(department.getId()).get().getAddress() != null);

    //1 with orphanRemoval = true. Note: department.setAddress(null) can also be used in case of OneToOne
    department.removeAddress();
    department = departmentRepository.saveAndFlush(department);
    assertTrue(departmentRepository.findById(department.getId()).get().getAddress() == null);

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
    assertTrue(departmentRepository.findById(department.getId()).get().getEmployees().size()
        == 1);//Use @Transactional to avoid -> org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role
    //department.removeEmployee(employee);//does not work because i don't have orphanRemoval enabled (remember, cascade doesn't work for setters)
    //department = departmentRepository.saveAndFlush(department);
    //assertTrue(departmentRepository.findById(department.getId()).get().getEmployees().size()==0);

    // so need to use employeeRepository to delete either the association (or) the complete employee record.
    department.getEmployees().get(0).setDepartment(null);
    Employee employee = employeeRepository.saveAndFlush(department.getEmployees().get(0));
    assertTrue(employeeRepository.findById(employee.getId()).get().getDepartment() == null);

    //i'm not sure why this is not working in this transaction context.
    //2 or else no other way except, you use employeeRepository to delete the employee. Finish!
     /*employeeRepository.deleteById(department.getEmployees().get(0).getId());
     assertTrue(!employeeRepository.findById(department.getEmployees().get(0).getId()).isPresent());*/
  }

}
