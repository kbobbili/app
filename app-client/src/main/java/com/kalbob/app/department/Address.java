package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "address")
public class Address extends BaseEntity {

  private String aptNum;
  private String street;
  private String city;
  private String state;
  private String zipCode;
  @OneToOne(fetch = FetchType.EAGER, mappedBy = "address")
  private Department department;

  public Address setDepartment(Department department) {
    if(department != null) {
      department.setAddressUnidirectional(this);
    }else{
      if(this.department != null){
        this.department.setAddressUnidirectional(null);
      }
    }
    this.department = department;
    return this;
  }

  public void removeDepartment() {
    if(this.department != null){
      this.department.setAddress(null);
    }
    this.department = null;
  }
}
