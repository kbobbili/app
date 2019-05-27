package com.kalbob.app.employee.repository;

import com.kalbob.app.config.data.BaseRepository;
import com.kalbob.app.department.DepartmentType;
import com.kalbob.app.employee.Employee;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee, Long> {

  List<Employee> findByDepartment_Type(DepartmentType type);

  List<Employee> findByLastNameOrderByFirstNameAsc(String lastName);
}
