package com.kalbob.app.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseTruncateService {

    @Autowired
    private EntityManager entityManager;
    private List<String> tableNames;

    @Transactional
    @Rollback(false)
    public void truncate() {
        Metamodel metamodel = entityManager.getMetamodel();
        tableNames = metamodel.getManagedTypes().stream()
                .filter(m -> m.getJavaType().getAnnotation(Table.class)!=null)
                .map(m -> m.getJavaType().getAnnotation(Table.class).name())
                .collect(Collectors.toList());
        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        tableNames.forEach(tableName ->
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate());
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}