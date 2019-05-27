package com.kalbob.app.department.repository;

import com.kalbob.app.config.data.BaseRepository;
import com.kalbob.app.department.Address;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends BaseRepository<Address, Long> {

}
