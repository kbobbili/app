package com.kalbob.app.data.repository;

import com.kalbob.app.data.model.Address;
import com.kalbob.app.data.model.AddressMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddressRepositoryTest extends AbstractRepositoryTest{

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void saveAddress() {
        Address address = AddressMother.complete().build();
        addressRepository.saveAndFlush(address);
        assertTrue(address.getId() != null);
    }


    @Test
    @Transactional//(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    @Rollback(false)
    public void deleteAddressById() {
        Address address = AddressMother.complete().build();
        address = addressRepository.saveAndFlush(address);
        assertTrue(addressRepository.findById(address.getId()).isPresent());
        addressRepository.deleteById(address.getId());
        assertTrue(!addressRepository.findById(address.getId()).isPresent());
    }


    @Test
    public void removeDepartment() {//Address deletes its association with department by setting department_id to null
        Address address = AddressMother.complete().build();
        address = addressRepository.saveAndFlush(address);
        assertTrue(addressRepository.findById(address.getId()).get().getDepartment()!=null);
        address.setDepartment(null);
        address = addressRepository.saveAndFlush(address);
        assertTrue(addressRepository.findById(address.getId()).get().getDepartment()==null);
    }

}