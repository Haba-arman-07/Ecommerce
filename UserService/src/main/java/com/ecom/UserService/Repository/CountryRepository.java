package com.ecom.UserService.Repository;

import com.ecom.CommonEntity.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {


}
