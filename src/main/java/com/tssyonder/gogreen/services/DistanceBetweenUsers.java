package com.tssyonder.gogreen.services;

import com.tssyonder.gogreen.entities.Citizen;
import com.tssyonder.gogreen.entities.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DistanceBetweenUsers {

    Double calculateDistanceBetweenTwoPointsInLatitudeAndLongitude(Double lat1, Double lat2, Double lat3, Double lat4);

    List<Citizen> getAllNearbyCitizensWithStatusOnHold(Double latitude, Double longitude);

    List<Company> getAllNearbyCompany(Double latitude, Double longitude);


}
