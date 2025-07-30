package com.ecom.UserService.Repository;

import com.ecom.CommonEntity.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE c.cityId = :cityId")
    Optional<City> fetchCity(@Param("cityId") Long cityId);


}
