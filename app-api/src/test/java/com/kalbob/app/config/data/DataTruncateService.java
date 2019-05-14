package com.kalbob.app.config.data;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.Metamodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Rollback(false)
public class DataTruncateService {

  @Autowired
  private EntityManager entityManager;

  public void truncate() {
    Metamodel metamodel = entityManager.getMetamodel();
    List<String> tableNames = metamodel.getManagedTypes().stream()
        .filter(m -> m.getJavaType().getAnnotation(Table.class) != null)
        .map(m -> m.getJavaType().getAnnotation(Table.class).name())
        .collect(Collectors.toList());
    entityManager.flush();
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
    tableNames.forEach(tableName ->
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate());
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
  }
}