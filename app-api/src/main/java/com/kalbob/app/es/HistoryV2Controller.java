package com.kalbob.code.es;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HistoryV2Controller {

  public final HistoryService historyService;

  @PreAuthorize("hasPermission(null,'history:c')")
  @PostMapping(value = "/sessions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Session createSession(@RequestBody Session session) {
    return historyService.createSession(session);
  }

  @PreAuthorize("hasPermission(null,'history:r')")
  @GetMapping(value = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<Session> findSessions(@RequestParam Map<String, String> params,
      Pageable pageable) {
    return historyService.getSessions(params, pageable);
  }

  @PreAuthorize("hasPermission(null,'history:r')")
  @GetMapping(value ="/results", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<JsonNode> findResults(@RequestParam Map<String, String> params,
      Pageable pageable) {
    return historyService.getResults(params, pageable);
  }

  @PreAuthorize("hasPermission(null,'history:r')")
  @GetMapping(value = "/sessions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Session getSessionById(@PathVariable String id) {
    return historyService.getSession(id);
  }

  @PreAuthorize("hasPermission(null,'history:c')")
  @PostMapping(value = "/sessions/{id}/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Session addMessage(@PathVariable String id, @RequestBody Message message) {
    return historyService.addMessage(id, message);
  }

  @PreAuthorize("hasPermission(null,'history:r')")
  @GetMapping(value = "/sessions/{id}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Message> getMessages(@PathVariable String id) {
    return historyService.getMessages(id);
  }

  @PreAuthorize("hasPermission(null,'history:c')")
  @PostMapping(value = "/messages/{id}/events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Session addEvent(@PathVariable String id, @RequestBody Event event) {
    return historyService.addEvent(id, event);
  }

  @PreAuthorize("hasPermission(null,'history:r')")
  @GetMapping(value = "/messages/{id}/events", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Event> getEvents(@PathVariable String id) {
    return historyService.getEvents(id);
  }

}