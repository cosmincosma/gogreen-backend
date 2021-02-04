package com.tssyonder.gogreen.services.impl;

import com.tssyonder.gogreen.entities.Citizen;
import com.tssyonder.gogreen.repositories.CitizenRepository;
import com.tssyonder.gogreen.repositories.RequestRepository;
import com.tssyonder.gogreen.services.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CitizenServiceImpl implements CitizenService {

    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    RequestRepository requestRepository;

    @Override
    public Citizen saveCitizen(Citizen citizen) {
        return citizenRepository.save(citizen);
    }

    @Override
    public Citizen getCitizenById(Long citizenId) {
        return citizenRepository.findByCitizenId(citizenId);
    }

    @Override
    public Citizen getByUserId(Long userId) {
        return citizenRepository.findByUserUserId(userId);
    }

    @Override
    public Citizen getCitizenByPhoneNumber(String citizenPhoneNumber) {
        return citizenRepository.findByCitizenPhoneNumber(citizenPhoneNumber);
    }
}
