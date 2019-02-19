package com.kalbob.app.service.service.impl;

import com.kalbob.app.common.utils.BaseServiceTestConfiguration;
import com.kalbob.app.common.utils.BaseTestConfiguration;
import com.kalbob.app.data.model.Address;
import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.repository.EmployeeRepository;
import com.kalbob.app.service.ServiceConfiguration;
import com.kalbob.app.service.service.EmployeeService;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceConfiguration.class})
@Import({BaseTestConfiguration.class, BaseServiceTestConfiguration.class})
public class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private DataFactory dataFactory;


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(getListOfEmployees(3));
        List<Employee> employeeList = employeeService.getAllEmployees();
        assertEquals(3, employeeList.size());
    }

    private Employee getEmployee() {
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        return Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .address(address)
                .build();
    }

    private List<Employee> getListOfEmployees(int n) {
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            employeeList.add(getEmployee());
        }
        return employeeList;
    }


}
