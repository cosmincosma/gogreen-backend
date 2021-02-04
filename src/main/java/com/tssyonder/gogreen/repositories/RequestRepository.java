package com.tssyonder.gogreen.repositories;

import com.tssyonder.gogreen.entities.Request;
import com.tssyonder.gogreen.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Request findRequestById(Long requestId);

    List<Request> findAllByCitizenCitizenId(Long citizenId);

    List<Request> findAllByCitizenUserUserId(Long userId);

    List<Request> findAllByCompanyUserUserIdAndAndStatus(Long userId, Status status);

    List<Request> findAllByCitizenUserUserIdAndStatus(Long userId, Status status);

    List<Request> findAllByCitizenUserUserIdOrderByQuantity(Long citizenId);
}
