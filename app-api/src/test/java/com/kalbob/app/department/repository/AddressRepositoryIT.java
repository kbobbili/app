package com.kalbob.app.department.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kalbob.app.config.data.BaseRepositoryIT;
import com.kalbob.app.department.Address;
import com.kalbob.app.department.AddressMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class AddressRepositoryIT extends BaseRepositoryIT {

  @Autowired
  private AddressRepository addressRepository;

  @Test
  public void saveAddress() {
    Address address = AddressMother.complete();
    addressRepository.saveAndFlush(address);
    assertNotNull(address.getId());
  }


  @Test
  @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
  @Rollback(false)
  public void deleteAddressById() {
    Address address = AddressMother.complete();
    address = addressRepository.saveAndFlush(address);
    assertTrue(addressRepository.findById(address.getId()).isPresent());
    addressRepository.deleteById(address.getId());
    assertFalse(addressRepository.findById(address.getId()).isPresent());
  }


  @Test
  public void removeDepartment() {//Address deletes its association with department by setting department_id to null
    Address address = AddressMother.complete();
    address = addressRepository.saveAndFlush(address);
    assertNotNull(addressRepository.findById(address.getId()).get().getDepartment());
    address.setDepartment(null);
    address = addressRepository.saveAndFlush(address);
    assertNull(addressRepository.findById(address.getId()).get().getDepartment());
  }

}