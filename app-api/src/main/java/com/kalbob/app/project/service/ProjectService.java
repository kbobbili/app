package com.kalbob.app.project.service;

import com.kalbob.app.project.Project;
import java.util.List;

public interface ProjectService {

  Project findById(Long id);
  List<Project> getAllProjects();
}
