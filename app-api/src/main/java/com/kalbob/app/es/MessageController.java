package com.kalbob.app.es;


import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

  public final MessageService messageService;

  @PostMapping("/messages")
  public void save(@RequestBody Message message) {
    messageService.createMessage(message);
  }

  @PostMapping("/sessions")
  public void save(@RequestBody Session session) {
    messageService.createSession(session);
  }

  @GetMapping("/messages")
  public Page<Message> messages(@RequestParam Map<String, String> params,
      Pageable pageable) {
    return messageService.getMessages(params, pageable);
  }

  @GetMapping("/sessions")
  public Page<SessionAggregate> sessionSummaries(@RequestParam Map<String, String> params,
      Pageable pageable) {
    return messageService.getSessionSummaries(params, pageable);
  }

}