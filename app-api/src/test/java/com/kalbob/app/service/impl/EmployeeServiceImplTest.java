package com.kalbob.app.service.impl;

import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.repository.EmployeeRepository;
import com.kalbob.app.rest.client.EmployeeService;
import com.kalbob.app.service.ServiceConfiguration;
import com.kalbob.app.util.EmployeeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceConfiguration.class})
//@Import({ServiceTestConfiguration.class})
public class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeFactory employeeFactory;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(employeeFactory.getListOfEmployeesWithIds(3));
        List<Employee> employeeList = employeeService.getAllEmployees();
        assertEquals(3, employeeList.size());
    }

}
