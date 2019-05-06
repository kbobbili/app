package com.kalbob.app.data.repository;

import com.kalbob.app.data.model.Project;
import com.kalbob.app.data.model.ProjectMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectRepositoryTest extends AbstractRepositoryTest{

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void saveProject() {
        Project project = ProjectMother.complete().build();
        projectRepository.saveAndFlush(project);
        assertTrue(project.getId() != null);
    }

    @Test
    @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    @Rollback(false)
    public void deleteProjectById() {
        Project project = ProjectMother.complete().build();
        project = projectRepository.saveAndFlush(project);
        assertTrue(projectRepository.findById(project.getId()).isPresent());
        projectRepository.deleteById(project.getId());
        assertTrue(!projectRepository.findById(project.getId()).isPresent());
    }

}