package com.kalbob.app.data.repository;

import com.kalbob.app.data.model.DepartmentType;
import com.kalbob.app.data.model.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findByDepartment_Type(DepartmentType type);

  List<Employee> findByLastNameOrderByFirstNameAsc(String lastName);
}
