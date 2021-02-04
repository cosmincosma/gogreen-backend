package com.tssyonder.gogreen.repositories;

import com.tssyonder.gogreen.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByCompanyId(Long companyId);

    Company findByUserUserId(Long userId);

    Company findByUniqueCode(String uniqueCode);

    Company findByCompanyPhoneNumber(String companyPhoneNumber);

    @Query("from Company c join c.materialList ml where ml.materialName = :materialName")
    List<Company> findByMaterial(String materialName);

}
