package com.kalbob.app.es;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {
  private String userId;
  private Boolean isActive;
  private Map<String, Object> info = new HashMap<>();
}