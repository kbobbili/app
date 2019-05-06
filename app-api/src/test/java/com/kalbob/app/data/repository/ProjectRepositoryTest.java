package com.kalbob.app.data.repository;

import com.kalbob.app.data.DataConfiguration;
import com.kalbob.app.data.model.Project;
import com.kalbob.app.data.model.ProjectMother;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {DataConfiguration.class})
//@Import({DataTestConfiguration.class})
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeAll
    public static void beforeAll() {

    }

    @AfterAll
    public static void afterAll() {
    }

    @Test
    public void saveProject() {
        Project project = ProjectMother.complete().build();
        projectRepository.saveAndFlush(project);
        assertTrue(project.getId() != null);
    }

    @Test
    public void deleteProjectById() {
        Project project = ProjectMother.complete().build();
        project = projectRepository.saveAndFlush(project);
        assertTrue(projectRepository.findById(project.getId()).isPresent());
        projectRepository.deleteById(project.getId());
        assertTrue(!projectRepository.findById(project.getId()).isPresent());
    }

}