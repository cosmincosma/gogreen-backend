package com.tssyonder.gogreen.dtos.convertor;

import com.tssyonder.gogreen.dtos.*;
import com.tssyonder.gogreen.entities.*;

import java.util.Date;
import java.util.List;

public class DtoConvertor {

    public User convertUserDtoToUserEntity(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }

    public Company convertCompanyDtoToCompanyEntity(CompanyDto companyDto) {
        Company company = new Company();
        company.setCompanyPhoneNumber(companyDto.getCompanyPhoneNumber());
        company.setName(companyDto.getCompanyName());
        StringBuilder fullAddress = new StringBuilder();
        fullAddress.append(companyDto.getStreet()).append(", ").append(companyDto.getNumber()).append(", ")
                .append(companyDto.getBuilding()).append(", ")
                .append(companyDto.getEntrance()).append(", ").
                append(companyDto.getAppNumber());
        company.setCompanyAddress(fullAddress.toString());
        company.setDescription(companyDto.getDescription());
        company.setLatitude(companyDto.getLatitude());
        company.setLongitude(companyDto.getLongitude());
        company.setUniqueCode(companyDto.getUniqueCode());
        return company;
    }

    public Material convertMaterialDtoToMaterialEntity(MaterialDto materialDto) {
        Material material = new Material();
        material.setMaterialName(materialDto.getMaterialName());
        return material;
    }

    public MaterialDto convertMaterialEntityToMaterialDto(Material material) {
        MaterialDto materialDto = new MaterialDto();
        materialDto.setMaterialName(material.getMaterialName());
        return materialDto;
    }

    public Citizen convertCitizenDtoToCitizenEntity(CitizenDto citizenDto) {
        Citizen citizen = new Citizen();
        citizen.setFirstName(citizenDto.getFirstName());
        citizen.setLastName(citizenDto.getLastName());
        StringBuilder fullAddress = new StringBuilder();
        fullAddress.append(citizenDto.getStreet()).append(", ").append(citizenDto.getNumber()).append(", ")
                .append(citizenDto.getBuilding()).append(", ")
                .append(citizenDto.getEntrance()).append(", ").
                append(citizenDto.getAppNumber());
        citizen.setCitizenAddress(fullAddress.toString());
        citizen.setCitizenPhoneNumber(citizenDto.getCitizenPhoneNumber());
        citizen.setLatitude(citizenDto.getLatitude());
        citizen.setLongitude(citizenDto.getLongitude());
        return citizen;
    }

    public Request convertRequestDtoToRequestEntity(RequestDto requestDto, Material material) {
        Request request = new Request();
        Date dateRequestCreated = new Date();
        request.setDateRequestCreated(dateRequestCreated);
        request.setStatus(Status.ON_HOLD);
        request.setQuantity(requestDto.getQuantity());
        request.setUnit(requestDto.getUnit());

        request.setMaterial(material);

        return request;
    }

    public CompanyDetailsDto convertCompanyDetailsDtoToCompanyEntity(Company company, User user) {
        CompanyDetailsDto companyDetailsDto = new CompanyDetailsDto();
        companyDetailsDto.setId(user.getUserId());
        companyDetailsDto.setRole(user.getRole());
        companyDetailsDto.setCompanyName(company.getCompanyName());
        companyDetailsDto.setCompanyAddress(company.getCompanyAddress());
        companyDetailsDto.setCompanyPhoneNumber(company.getCompanyPhoneNumber());
        companyDetailsDto.setEmail(user.getEmail());
        companyDetailsDto.setDescription(company.getDescription());
        companyDetailsDto.setUniqueCode(company.getUniqueCode());
        companyDetailsDto.setLatitude(company.getLatitude());
        companyDetailsDto.setLongitude(company.getLongitude());

        return companyDetailsDto;
    }

    public CitizenDetailsDto convertCitizenDetailsDtoToCitizenEntity(Citizen citizen, User user) {
        CitizenDetailsDto citizenDetailsDto = new CitizenDetailsDto();
        citizenDetailsDto.setId(user.getUserId());
        citizenDetailsDto.setRole(user.getRole());
        citizenDetailsDto.setCitizenAddress(citizen.getCitizenAddress());
        citizenDetailsDto.setCitizenPhoneNumber(citizen.getCitizenPhoneNumber());
        citizenDetailsDto.setEmail(user.getEmail());
        citizenDetailsDto.setFirstname(citizen.getFirstName());
        citizenDetailsDto.setLastname(citizen.getLastName());
        citizenDetailsDto.setLatitude(citizen.getLatitude());
        citizenDetailsDto.setLongitude(citizen.getLongitude());

        return citizenDetailsDto;
    }

    public RequestsListDto convertRequestsListEntityToRequestsListDto(Request request) {
        RequestsListDto requestsListDto = new RequestsListDto();

        requestsListDto.setId(request.getId());
        requestsListDto.setDateRequestCreated(request.getDateRequestCreated());
        requestsListDto.setQuantity(request.getQuantity());
        requestsListDto.setStatus(request.getStatus());
        requestsListDto.setUnit(request.getUnit());
        requestsListDto.setMaterialName(request.getMaterial().getMaterialName());

        return requestsListDto;
    }

    public CitizenNearbyDto convertCitizenEntityToCitizenNearbyDto(Citizen citizen) {
        CitizenNearbyDto citizenNearbyDto = new CitizenNearbyDto();
        citizenNearbyDto.setName(citizen.getFirstName() + " " + citizen.getLastName());
        citizenNearbyDto.setLongitude(citizen.getLongitude());
        citizenNearbyDto.setLatitude(citizen.getLatitude());
        citizenNearbyDto.setRole(citizen.getUser().getRole());

        return citizenNearbyDto;
    }


    public RequestsListDto convertRequestEntityToRequestsListDtoAccepted(Request request) {
        RequestsListDto requestsListDto = this.convertRequestsListEntityToRequestsListDto(request);
        requestsListDto.setId(request.getId());
        requestsListDto.setDateRequestAccepted(request.getDateRequestAccepted());
        requestsListDto.setCompanyName(request.getCompany().getCompanyName());
        return requestsListDto;
    }

    public CompanyDetailsHoverDto convertCompanyEntityToCompanyDetailsHoverDto(Company company, List<String> materials) {
        CompanyDetailsHoverDto companyDetailsHoverDto = new CompanyDetailsHoverDto();
        companyDetailsHoverDto.setAddress(company.getCompanyAddress());
        companyDetailsHoverDto.setCompanyName(company.getName());
        companyDetailsHoverDto.setCompanyPhoneNumber(company.getCompanyPhoneNumber());
        companyDetailsHoverDto.setEmail(company.getUser().getEmail());
        companyDetailsHoverDto.setMaterialList(materials);
        companyDetailsHoverDto.setLatitude(company.getLatitude());
        companyDetailsHoverDto.setLongitude(company.getLongitude());
        companyDetailsHoverDto.setRole(company.getUser().getRole());

        return companyDetailsHoverDto;
    }

    public RequestsListDto convertRequestEntityToRequestsListDtoCompleted(Request request) {
        RequestsListDto requestsListDto = this.convertRequestsListEntityToRequestsListDto(request);
        requestsListDto.setId(request.getId());
        requestsListDto.setDateRequestAccepted(request.getDateRequestAccepted());
        requestsListDto.setCompanyName(request.getCompany().getCompanyName());
        return requestsListDto;
    }

    public OrdersListDto convertRequestEntityToOrdersListDto(Request request) {
        OrdersListDto ordersListDto = new OrdersListDto();

        ordersListDto.setId(request.getId());
        ordersListDto.setDateCreated(request.getDateRequestCreated());
        ordersListDto.setDateCollection(request.getDateRequestAccepted());
        ordersListDto.setAddress(request.getCitizen().getCitizenAddress());
        ordersListDto.setPhoneNumber(request.getCitizen().getCitizenPhoneNumber());
        ordersListDto.setMaterialName(request.getMaterial().getMaterialName());
        ordersListDto.setQuantity(request.getQuantity());
        ordersListDto.setCitizenName(request.getCitizen().getFirstName() + " " + request.getCitizen().getLastName());
        ordersListDto.setUnit(request.getUnit());

        return ordersListDto;
    }

    public CollectionStationsDto convertCompanyEntityToCollectionStationsDto(Company company) {
        CollectionStationsDto collectionStationsDto = new CollectionStationsDto();

        collectionStationsDto.setName(company.getName());
        collectionStationsDto.setLatitude(company.getLatitude());
        collectionStationsDto.setLongitude(company.getLongitude());

        return collectionStationsDto;
    }
}