package com.kalbob.app.department.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kalbob.app.config.data.AbstractRepositoryTest;
import com.kalbob.app.department.Address;
import com.kalbob.app.department.AddressMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class AddressRepositoryTest extends AbstractRepositoryTest {

  @Autowired
  private AddressRepository addressRepository;

  @Test
  public void saveAddress() {
    Address address = AddressMother.complete();
    addressRepository.saveAndFlush(address);
    assertTrue(address.getId() != null);
  }


  @Test
  @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
  @Rollback(false)
  public void deleteAddressById() {
    Address address = AddressMother.complete();
    address = addressRepository.saveAndFlush(address);
    assertTrue(addressRepository.findById(address.getId()).isPresent());
    addressRepository.deleteById(address.getId());
    assertTrue(!addressRepository.findById(address.getId()).isPresent());
  }


  @Test
  public void removeDepartment() {//Address deletes its association with department by setting department_id to null
    Address address = AddressMother.complete();
    address = addressRepository.saveAndFlush(address);
    assertTrue(addressRepository.findById(address.getId()).get().getDepartment() != null);
    address.setDepartment(null);
    address = addressRepository.saveAndFlush(address);
    assertTrue(addressRepository.findById(address.getId()).get().getDepartment() == null);
  }

}