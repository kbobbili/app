package com.kalbob.app.data.repository;

import com.kalbob.app.data.DataConfiguration;
import com.kalbob.app.data.DatabaseTruncateService;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {DataConfiguration.class})
//@Import({DataTestConfiguration.class})
public class AbstractRepositoryTest {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private DatabaseTruncateService databaseTruncateService;

  @BeforeAll
  public static void beforeAll() {
  }

  @AfterAll
  public static void afterAll() {
  }

  @BeforeEach
  public void beforeEach() throws Exception {
  }

  @AfterEach
  public void afterEach() throws Exception {
  }

  public void clearDB() {
    databaseTruncateService.truncate();
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

}
