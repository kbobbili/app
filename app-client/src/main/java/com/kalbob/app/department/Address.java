package com.kalbob.code.department;

import com.kalbob.code.BaseEntity;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "address")
public class Address extends BaseEntity {

  private String aptNum;
  private String street;
  private String city;
  private String state;
  private String zipCode;
  @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
  @JoinColumn(name = "department_id")
  private Department department;
}
