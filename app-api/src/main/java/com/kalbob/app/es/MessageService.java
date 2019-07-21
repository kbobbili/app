package com.kalbob.app.es;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  Page<Message> getMessages(Map<String, String> params, Pageable pageable);
  Page<SessionAggregate> getSessionSummaries(Map<String, String> params, Pageable pageable);

}
