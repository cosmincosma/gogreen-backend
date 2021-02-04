package com.tssyonder.gogreen.services.impl;

import com.tssyonder.gogreen.entities.Citizen;
import com.tssyonder.gogreen.entities.Company;
import com.tssyonder.gogreen.entities.Status;
import com.tssyonder.gogreen.repositories.CitizenRepository;
import com.tssyonder.gogreen.repositories.CompanyRepository;
import com.tssyonder.gogreen.services.DistanceBetweenUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistanceBetweenUsersImpl implements DistanceBetweenUsers {

    private final static Double RADIUS = 3d;

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Double calculateDistanceBetweenTwoPointsInLatitudeAndLongitude(Double firstPointLatitude, Double firstPointLongitude, Double secondPointLatitude,
                                                                          Double secondPointLongitude) {
        Double earthRadius = 6372.8;

        double dLat = Math.toRadians(secondPointLatitude - firstPointLatitude);
        double dLon = Math.toRadians(secondPointLongitude - firstPointLongitude);

        firstPointLatitude = Math.toRadians(firstPointLatitude);
        secondPointLatitude = Math.toRadians(secondPointLatitude);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(firstPointLatitude) * Math.cos(secondPointLatitude);
        double c = 2 * Math.asin(Math.sqrt(a));
        return earthRadius * c;

    }

    @Override
    public List<Citizen> getAllNearbyCitizensWithStatusOnHold(Double latitude, Double longitude) {
        List<Citizen> nearbyCitizens = new ArrayList<>();
        for (Citizen citizen : citizenRepository.findAllCitizensByRequestStatus(Status.ON_HOLD)) {

            Double latitudeCitizen = Double.parseDouble(citizen.getLatitude());
            Double longitudeCitizen = Double.parseDouble(citizen.getLongitude());

            if (calculateDistanceBetweenTwoPointsInLatitudeAndLongitude(latitude, longitude, latitudeCitizen, longitudeCitizen) < RADIUS && calculateDistanceBetweenTwoPointsInLatitudeAndLongitude(latitude, longitude, latitudeCitizen, longitudeCitizen) != 0) {
                nearbyCitizens.add(citizen);
            }
        }
        return nearbyCitizens;
    }

    @Override
    public List<Company> getAllNearbyCompany(Double latitude, Double longitude) {
        List<Company> nearbyCompanies = new ArrayList<>();
        for (Company company : companyRepository.findAll()) {

            Double latitudeCompany = Double.parseDouble(company.getLatitude());
            Double longitudeCompany = Double.parseDouble(company.getLongitude());

            if (calculateDistanceBetweenTwoPointsInLatitudeAndLongitude(latitude, longitude, latitudeCompany, longitudeCompany) < RADIUS && calculateDistanceBetweenTwoPointsInLatitudeAndLongitude(latitude, longitude, latitudeCompany, longitudeCompany) != 0) {
                nearbyCompanies.add(company);
            }
        }
        return nearbyCompanies;
    }
}