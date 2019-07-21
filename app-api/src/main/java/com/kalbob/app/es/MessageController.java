package com.kalbob.app.es;


import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

  public final MessageService messageService;

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