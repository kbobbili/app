package com.kalbob.app.data.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProjectAssignmentKey implements Serializable {
    private Long projectId;
    private Long employeeId;
}