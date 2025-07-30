package com.ecom.UserService.Repository;

import com.ecom.CommonEntity.entities.Country;
import com.ecom.CommonEntity.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StateRepository extends JpaRepository<State, Long> {

    @Query("SELECT s FROM State s WHERE s.stateId = :stateId")
    Optional<State> fetchState(@Param("stateId") Long stateId);

}
