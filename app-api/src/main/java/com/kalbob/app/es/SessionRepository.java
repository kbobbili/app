package com.kalbob.app.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends ElasticsearchRepository<Session, String> {
}
