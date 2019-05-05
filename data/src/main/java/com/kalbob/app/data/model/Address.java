package com.kalbob.app.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address extends BaseModel{
    private String aptNum;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    @OneToOne
    @JoinColumn(name = "department_id")
    /*@JoinTable(
            name = "address_department",
            joinColumns = @JoinColumn(
                    name = "address_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "department_id", referencedColumnName = "id")
    )*/
    private Department department;
}
