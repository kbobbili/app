package com.kalbob.app.task.repository;

import com.kalbob.app.config.data.BaseRepository;
import com.kalbob.app.task.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends BaseRepository<Task, Long> {

}
