package com.kalbob.reactive.project.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kalbob.reactive.config.data.BaseRepositoryIT;
import com.kalbob.reactive.project.Project;
import com.kalbob.reactive.project.ProjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class ProjectRepositoryIT extends BaseRepositoryIT {

  @Autowired
  private ProjectRepository projectRepository;

  @Test
  public void saveProject() {
    Project project = ProjectMother.complete();
    projectRepository.saveAndFlush(project);
    assertNotNull(project.getId());
  }

  @Test
  @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
  @Rollback(false)
  public void deleteProjectById() {
    Project project = ProjectMother.complete();
    project = projectRepository.saveAndFlush(project);
    assertTrue(projectRepository.findById(project.getId()).isPresent());
    projectRepository.deleteById(project.getId());
    assertFalse(projectRepository.findById(project.getId()).isPresent());
  }

}