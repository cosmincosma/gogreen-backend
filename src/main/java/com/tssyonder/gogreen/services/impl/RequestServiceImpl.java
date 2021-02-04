package com.tssyonder.gogreen.services.impl;

import com.tssyonder.gogreen.entities.Request;
import com.tssyonder.gogreen.entities.Status;
import com.tssyonder.gogreen.repositories.RequestRepository;
import com.tssyonder.gogreen.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    RequestRepository requestRepository;

    @Override
    public Request getRequestById(Long requestId) {
        return requestRepository.findRequestById(requestId);
    }

    @Override
    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }

    @Override
    public List<Request> getAllByCitizenId(Long id) {
        return requestRepository.findAllByCitizenCitizenId(id);
    }

    @Override
    public List<Request> getAllByUserId(Long id) {
        return requestRepository.findAllByCitizenUserUserId(id);
    }

    @Override
    public List<Request> getAllByCompanyUserIdAndStatus(Long id, Status status) {
        return requestRepository.findAllByCompanyUserUserIdAndAndStatus(id, status);
    }

    @Override
    public List<Request> getAllByCitizenUserIdAndStatus(Long id, Status status) {
        return requestRepository.findAllByCitizenUserUserIdAndStatus(id, status);
    }

    @Override
    public List<Request> getAllByUserIdOrderByQuantity(Long id) {
        return requestRepository.findAllByCitizenUserUserIdOrderByQuantity(id);
    }
}