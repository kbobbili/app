package com.kalbob.app.config.data;

import java.io.Serializable;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class BaseRepositoryImpl<T, ID extends Serializable>
    extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

  public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  @Override
  public T getById(ID id) {
    Optional<T> entity = findById(id);
    return entity.orElseThrow(ResourceNotFoundException::new);
  }
}