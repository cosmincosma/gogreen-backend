package com.tssyonder.gogreen.controllers;

import com.tssyonder.gogreen.dtos.CompanyDto;
import com.tssyonder.gogreen.dtos.convertor.DtoConvertor;
import com.tssyonder.gogreen.entities.Company;
import com.tssyonder.gogreen.entities.Material;
import com.tssyonder.gogreen.entities.User;
import com.tssyonder.gogreen.exceptions.NotFoundException;
import com.tssyonder.gogreen.services.impl.CompanyServiceImpl;
import com.tssyonder.gogreen.services.impl.MaterialServiceImpl;
import com.tssyonder.gogreen.services.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompanyControllerTest {

    @Mock
    private CompanyServiceImpl companyService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private MaterialServiceImpl materialService;
    @Mock
    private DtoConvertor dtoConvertor;

    @InjectMocks
    private CompanyController companyController;

    private Company company = new Company();
    private User user = new User();
    private CompanyDto companyDto = new CompanyDto();

    private BindingResult bindingResult = mock(BindingResult.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user.setUserId(2L);

        companyDto = new CompanyDto();
        companyDto.setCompanyName("SC SRL");
        companyDto.setCompanyPhoneNumber("0771459777");
        companyDto.setDescription("description for company");
        companyDto.setLatitude("1234");
        companyDto.setLongitude("6789");
        companyDto.setUniqueCode("RO5623");
        companyDto.setPassword("asdfgyhyyujuhjkl");
        companyDto.setStreet("lapusneanu");
        companyDto.setAppNumber("45");
        companyDto.setBuilding("A1");
        companyDto.setNumber("12");
        companyDto.setEntrance("A3");

        List<String> materials = new ArrayList<>();
        materials.add("plastic");
        materials.add("fier");
        companyDto.setMaterialName(materials);
    }

    @Test
    public void saveCompanyTestAccepted() throws URISyntaxException {
        Material firstMaterial = new Material();
        Material secondMaterial = new Material();
        firstMaterial.setMaterialName("plastic");
        firstMaterial.setMaterialId(23L);
        secondMaterial.setMaterialName("fier");
        secondMaterial.setMaterialId(24L);
        List<Material> materials = new ArrayList<>();
        materials.add(firstMaterial);
        materials.add(secondMaterial);

        companyDto.setEmail("srlscc@gmail.com");

        // arrange
        when(companyService.getCompanyByUniqueCode(companyDto.getUniqueCode())).thenReturn(null);
        when(companyService.getCompanyByPhoneNumber(companyDto.getCompanyPhoneNumber())).thenReturn(null);
        when(userService.getByEmail(companyDto.getEmail())).thenReturn(null);
        when(materialService.getMaterialByName("plastic")).thenReturn(firstMaterial);
        when(materialService.getMaterialByName("fier")).thenReturn(secondMaterial);
        when(materialService.saveAllMaterial(materials)).thenReturn(materials);
        when(userService.saveUser(any(User.class))).thenReturn(user);
        when(companyService.saveCompany(company)).thenReturn(company);
        // act
        ResponseEntity companyResponseEntity = companyController.saveCompany(companyDto, bindingResult);
        // assert
        Assertions.assertThat(companyResponseEntity).isNotNull();
        Assertions.assertThat(companyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void saveCompanyTestFailed() throws URISyntaxException {
        companyDto.setEmail("scsrltgrtgtrgrtgtr");

        when(dtoConvertor.convertCompanyDtoToCompanyEntity(companyDto)).thenReturn(company);

        ResponseEntity companyResponseEntity = companyController.saveCompany(companyDto, bindingResult);

        Assertions.assertThat(companyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getCompanyByIdTestAccepted() throws NotFoundException {
        Company company = new Company();
        company.setCompanyId(2L);

        when(companyService.getCompanyById(2L)).thenReturn(company);

        ResponseEntity companyResponseEntity = companyController.getCompanyById(2L);

        Assertions.assertThat(companyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getCompanyByIdTestFailed() throws NotFoundException {
        Company company = new Company();
        company.setCompanyId(2L);

        when(companyService.getCompanyById(2L)).thenReturn(company);

        ResponseEntity companyResponseEntity = companyController.getCompanyById(null);

        Assertions.assertThat(companyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @After
    public void tearDown() {
        companyDto = null;
        company = null;
    }
}