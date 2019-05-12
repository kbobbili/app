package com.kalbob.app.employee.repository;

import com.kalbob.app.department.DepartmentType;
import com.kalbob.app.employee.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findByDepartment_Type(DepartmentType type);

  List<Employee> findByLastNameOrderByFirstNameAsc(String lastName);
}
