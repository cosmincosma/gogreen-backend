package com.tssyonder.gogreen.controllers;

import com.tssyonder.gogreen.dtos.CitizenDto;
import com.tssyonder.gogreen.entities.Citizen;
import com.tssyonder.gogreen.entities.User;
import com.tssyonder.gogreen.exceptions.NotFoundException;
import com.tssyonder.gogreen.services.impl.*;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CitizenControllerTest {

    @Mock
    DistanceBetweenUsersImpl distanceBetweenUsers;
    @Mock
    private CitizenServiceImpl citizenService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private CompanyServiceImpl companyService;
    @Mock
    private RequestServiceImpl requestService;
    @InjectMocks
    private CitizenController citizenController;

    private Citizen citizen = new Citizen();
    private User user = new User();
    private CitizenDto citizenDto = new CitizenDto();

    private BindingResult bindingResult = mock(BindingResult.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user.setUserId(2L);

        citizenDto = new CitizenDto();
        citizenDto.setFirstName("Cosmin");
        citizenDto.setLastName("Cosma");
        citizenDto.setLatitude("12");
        citizenDto.setLongitude("67");
        citizenDto.setPassword("asdfghjkl");
        citizenDto.setStreet("Lapusneanu");
        citizenDto.setAppNumber("45");
        citizenDto.setBuilding("A1");
        citizenDto.setNumber("12");
        citizenDto.setCitizenPhoneNumber("0751452855");
        citizenDto.setEntrance("A3");
    }

    @Test
    public void saveCitizenTestAccepted() throws URISyntaxException {

        citizenDto.setEmail("scsrl@gmail.com");

        // arrange
        when(citizenService.getCitizenByPhoneNumber(citizenDto.getCitizenPhoneNumber())).thenReturn(null);
        when(userService.getByEmail(citizenDto.getEmail())).thenReturn(null);
        when(userService.saveUser(any(User.class))).thenReturn(user);
        when(citizenService.saveCitizen(citizen)).thenReturn(citizen);
        // act
        ResponseEntity citizenResponseEntity = citizenController.saveCitizen(citizenDto, bindingResult);
        // assert
        Assertions.assertThat(citizenResponseEntity).isNotNull();
        Assertions.assertThat(citizenResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void saveCitizenTestFailed() throws URISyntaxException {
        citizenDto.setEmail("scsrltgrtgtrgrtgtr");

        when(bindingResult.hasErrors()).thenReturn(true); // if-ul

        ResponseEntity citizenResponseEntity = citizenController.saveCitizen(citizenDto, bindingResult);

        Assertions.assertThat(citizenResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getCitizenByIdTestAccepted() throws NotFoundException {
        Citizen citizen = new Citizen();
        citizen.setCitizenId(2L);

        when(citizenService.getCitizenById(2L)).thenReturn(citizen);

        ResponseEntity citizenResponseEntity = citizenController.getCitizenById(2L);

        Assertions.assertThat(citizenResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getCitizenByIdTestFailed() throws NotFoundException {
        Citizen citizen = new Citizen();
        citizen.setCitizenId(2L);

        when(citizenService.getCitizenById(2L)).thenReturn(null);

        ResponseEntity citizenResponseEntity = citizenController.getCitizenById(null);

        Assertions.assertThat(citizenResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @After
    public void tearDown() {
        citizenDto = null;
        citizen = null;
    }
}