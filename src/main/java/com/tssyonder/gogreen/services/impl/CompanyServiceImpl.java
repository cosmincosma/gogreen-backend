package com.tssyonder.gogreen.services.impl;

import com.tssyonder.gogreen.entities.Company;
import com.tssyonder.gogreen.repositories.CompanyRepository;
import com.tssyonder.gogreen.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long companyId) {
        return companyRepository.findByCompanyId(companyId);
    }

    @Override
    public Company getByUserId(Long userId) {
        return companyRepository.findByUserUserId(userId);
    }

    @Override
    public Company getCompanyByUniqueCode(String uniqueCode) {
        return companyRepository.findByUniqueCode(uniqueCode);
    }

    @Override
    public Company getCompanyByPhoneNumber(String companyPhoneNumber) {
        return companyRepository.findByCompanyPhoneNumber(companyPhoneNumber);
    }

    @Override
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }
}
