package com.kalbob.app.data.repository;

import com.kalbob.app.data.DatabaseTruncateService;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
/*@Transactional
@Rollback(false)*/
public class AbstractRepositoryTest {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private DatabaseTruncateService databaseTruncateService;

  @Value("${spring.profiles.active:}")
  private String profile;

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

  @Test
  public void test(){
    System.out.println("Profile: "+profile);
  }

}
