package com.kalbob.app.data.repository;

import com.kalbob.app.common.utils.BaseDataTestConfiguration;
import com.kalbob.app.common.utils.BaseTestConfiguration;
import com.kalbob.app.data.DataConfiguration;
import com.kalbob.app.data.model.Address;
import com.kalbob.app.data.model.Employee;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DataConfiguration.class})
@Import({BaseTestConfiguration.class, BaseDataTestConfiguration.class})
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DataFactory dataFactory;


    @Before
    public void setUp() throws Exception {
        employeeRepository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void save() {
        getListOfEmployees(3).forEach(e -> employeeRepository.saveAndFlush(e));
        assertEquals(3, employeeRepository.findAll().size());
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
