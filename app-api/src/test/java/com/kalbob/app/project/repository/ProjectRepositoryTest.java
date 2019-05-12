package com.kalbob.app.project.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kalbob.app.config.data.AbstractRepositoryTest;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class ProjectRepositoryTest extends AbstractRepositoryTest {

  @Autowired
  private ProjectRepository projectRepository;

  @Test
  public void saveProject() {
    Project project = ProjectMother.complete();
    projectRepository.saveAndFlush(project);
    assertTrue(project.getId() != null);
  }

  @Test
  @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
  @Rollback(false)
  public void deleteProjectById() {
    Project project = ProjectMother.complete();
    project = projectRepository.saveAndFlush(project);
    assertTrue(projectRepository.findById(project.getId()).isPresent());
    projectRepository.deleteById(project.getId());
    assertTrue(!projectRepository.findById(project.getId()).isPresent());
  }

}