package com.tssyonder.gogreen.services;

import com.tssyonder.gogreen.entities.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {
    List<Company> getAllCompanies();

    Company getCompanyById(Long companyId);

    Company getByUserId(Long userId);

    Company getCompanyByUniqueCode(String uniqueCode);

    Company getCompanyByPhoneNumber(String companyPhoneNumber);

    Company saveCompany(Company company);
}
