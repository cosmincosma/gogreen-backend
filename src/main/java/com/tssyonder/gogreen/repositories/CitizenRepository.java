package com.tssyonder.gogreen.repositories;

import com.tssyonder.gogreen.entities.Citizen;
import com.tssyonder.gogreen.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Citizen findByCitizenId(Long citizenId);

    Citizen findByUserUserId(Long userId);

    Citizen findByCitizenPhoneNumber(String citizenPhoneNumber);

    List<Citizen> findByCitizenAddress(String citizenAddress);

    @Query("select distinct c from Citizen c join c.requestList r where r.status = :status")
    List<Citizen> findAllCitizensByRequestStatus(Status status);

}
