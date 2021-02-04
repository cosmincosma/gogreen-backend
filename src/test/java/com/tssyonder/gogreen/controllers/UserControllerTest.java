package com.tssyonder.gogreen.controllers;


import com.tssyonder.gogreen.dtos.UserLoginDto;
import com.tssyonder.gogreen.entities.Citizen;
import com.tssyonder.gogreen.entities.Company;
import com.tssyonder.gogreen.entities.Role;
import com.tssyonder.gogreen.entities.User;
import com.tssyonder.gogreen.services.CitizenService;
import com.tssyonder.gogreen.services.CompanyService;
import com.tssyonder.gogreen.services.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;
    @Mock
    private CitizenService citizenService;
    @Mock
    private CompanyService companyService;

    @InjectMocks
    private UserController userController;

    private User user = new User();
    private UserLoginDto userLoginDto = new UserLoginDto();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        userLoginDto.setEmail("zorilor@yahoo.com");
    }

    @Test
    public void loginTestAccepted() {

        userLoginDto.setEmail("zorilor@yahoo.com");
        userLoginDto.setPassword("string222");

        user = new User();
        user.setUserId(2L);
        user.setEmail("zorilor@yahoo.com");
        user.setPassword("iPoldW3O6nf+/HFUerl0Mg==");
        user.setRole(Role.CITIZEN);

        Citizen citizen = new Citizen();
        citizen.setCitizenId(user.getUserId());
        citizen.setCitizenAddress("str blbl");
        citizen.setCitizenPhoneNumber("0771564542");
        citizen.setFirstName("Ion");
        citizen.setLastName("Andrei");
        citizen.setLatitude("123");
        citizen.setLongitude("456");
        citizen.setUser(user);

        Company company = new Company();
        company.setCompanyId(user.getUserId());
        company.setUser(user);
        company.setUniqueCode("RO39");
        company.setLongitude("455");
        company.setDescription("descriere");
        company.setCompanyPhoneNumber("0771234567");
        company.setCompanyName("SC SRL");
        company.setCompanyAddress("str doi");
        company.setName("name");

        when(userService.getByEmail(userLoginDto.getEmail())).thenReturn(citizen.getUser());
        when(citizenService.getByUserId(2L)).thenReturn(citizen);
        when(companyService.getByUserId(2L)).thenReturn(company);

        ResponseEntity userResponseLogin = userController.login(userLoginDto);

        Assertions.assertThat(userResponseLogin).isNotNull();
        Assertions.assertThat(userResponseLogin.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loginTestFailed() {

        userLoginDto.setPassword("");

        ResponseEntity userResponseLogin = userController.login(userLoginDto);

        Assertions.assertThat(userResponseLogin.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}