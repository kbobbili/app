package com.kalbob.app.data.repository;

import com.kalbob.app.data.BaseDataTestConfiguration;
import com.kalbob.app.data.DataConfiguration;
import com.kalbob.app.data.factory.EmployeeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DataConfiguration.class})
@Import({BaseDataTestConfiguration.class})
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeFactory employeeFactory;

    @Before
    public void setUp() throws Exception {
        employeeRepository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void save() {
        employeeFactory.getListOfEmployees(3).forEach(e -> employeeRepository.saveAndFlush(e));
        assertEquals(3, employeeRepository.findAll().size());
    }


}
