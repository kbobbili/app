package com.kalbob.app.data.repository;

import com.kalbob.app.data.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartment_NameIgnoreCase(String deptName);

    List<Employee> findByLastNameOrderByFirstNameAsc(String lastName);
}
