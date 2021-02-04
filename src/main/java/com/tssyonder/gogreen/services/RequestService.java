package com.tssyonder.gogreen.services;

import com.tssyonder.gogreen.entities.Request;
import com.tssyonder.gogreen.entities.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {
    Request getRequestById(Long requestId);

    Request saveRequest(Request request);

    void deleteRequest(Long id);

    List<Request> getAllByCitizenId(Long id);

    List<Request> getAllByUserId(Long id);

    List<Request> getAllByCompanyUserIdAndStatus(Long id, Status status);

    List<Request> getAllByCitizenUserIdAndStatus(Long id, Status status);

    List<Request> getAllByUserIdOrderByQuantity(Long id);
}
