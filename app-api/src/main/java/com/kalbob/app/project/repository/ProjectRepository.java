package com.kalbob.app.project.repository;

import com.kalbob.app.config.data.BaseRepository;
import com.kalbob.app.project.Project;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends BaseRepository<Project, Long> {

}
