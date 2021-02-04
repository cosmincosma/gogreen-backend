package com.tssyonder.gogreen.services;

import com.tssyonder.gogreen.entities.Citizen;
import org.springframework.stereotype.Service;

@Service
public interface CitizenService {
    Citizen getCitizenById(Long citizenId);

    Citizen getByUserId(Long userId);

    Citizen getCitizenByPhoneNumber(String citizenPhoneNumber);

    Citizen saveCitizen(Citizen citizen);
}
