package com.tssyonder.gogreen.controllers;


import com.tssyonder.gogreen.dtos.RequestDto;
import com.tssyonder.gogreen.dtos.RequestOnHoldDto;
import com.tssyonder.gogreen.dtos.convertor.DtoConvertor;
import com.tssyonder.gogreen.entities.*;
import com.tssyonder.gogreen.services.impl.CitizenServiceImpl;
import com.tssyonder.gogreen.services.impl.CompanyServiceImpl;
import com.tssyonder.gogreen.services.impl.MaterialServiceImpl;
import com.tssyonder.gogreen.services.impl.RequestServiceImpl;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestControllerTest {

    @Mock
    private RequestServiceImpl requestService;
    @Mock
    private MaterialServiceImpl materialService;
    @Mock
    private CitizenServiceImpl citizenService;
    @Mock
    private CompanyServiceImpl companyService;
    @Mock
    private DtoConvertor dtoConvertor;

    @InjectMocks
    private RequestController requestController;

    private Request request = new Request();
    private RequestDto requestDto = new RequestDto();

    private BindingResult bindingResult = mock(BindingResult.class);
    private List<RequestDto> requests = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        requestDto = new RequestDto();
        requestDto.setUnit("kg");
        requestDto.setMaterialName("fier");
    }

    @Test
    public void saveRequestTestAccepted() throws URISyntaxException {
        Material material = new Material();
        material.setMaterialName("fier");
        Citizen citizen = new Citizen();
        citizen.setCitizenId(2l);
        requestDto.setQuantity("23");
        requests.add(requestDto);

        // arrange
        when(requestService.saveRequest(request)).thenReturn(request);
        when(materialService.getMaterialByName("fier")).thenReturn(material);
        when(citizenService.getByUserId(2l)).thenReturn(citizen);
        when(dtoConvertor.convertRequestDtoToRequestEntity(requestDto, material)).thenReturn(request);
        // act
        ResponseEntity requestResponseEntity = requestController.saveRequest(requests, citizen.getCitizenId(), bindingResult);
        // assert
        Assertions.assertThat(requestResponseEntity).isNotNull();
        Assertions.assertThat(requestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void saveRequestTestFailed() throws URISyntaxException {
        Material material = new Material();
        material.setMaterialName("fier");
        Citizen citizen = new Citizen();
        citizen.setCitizenId(2l);
        requestDto.setQuantity("502");
        requests.add(requestDto);

        // arrange
        when(dtoConvertor.convertRequestDtoToRequestEntity(requestDto, material)).thenReturn(request);
        when(bindingResult.hasErrors()).thenReturn(true);
        // act
        ResponseEntity requestResponseEntity = requestController.saveRequest(requests, citizen.getCitizenId(), bindingResult);
        // assert
        Assertions.assertThat(requestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllRequestsByCitizenIdTestAccepted() throws URISyntaxException {
        Citizen citizen = new Citizen();
        citizen.setCitizenId(2l);
        request.setId(2L);
        request.setQuantity("23");
        request.setUnit("kg");
        request.setDateRequestCreated(new Date());
        request.setStatus(Status.ON_HOLD);
        Material material = new Material();
        material.setMaterialName("fier");
        request.setMaterial(material);
        List<Request> requestList = new ArrayList<>();
        requestList.add(request);

        // arrange
        when(citizenService.getByUserId(citizen.getCitizenId())).thenReturn(citizen);
        when(requestService.getAllByUserId(citizen.getCitizenId())).thenReturn(requestList);
        // act
        ResponseEntity requestResponseEntity = requestController.getAllByUserId(citizen.getCitizenId());
        // assert
        Assertions.assertThat(requestResponseEntity).isNotNull();
        Assertions.assertThat(requestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getAllRequestsByCitizenIdTestFailed() throws URISyntaxException {
        Citizen citizen = new Citizen();
        citizen.setCitizenId(2L);

        // arrange
        when(citizenService.getCitizenById(citizen.getCitizenId())).thenReturn(null);
        // act
        ResponseEntity requestResponseEntity = requestController.getAllByUserId(2L);
        // assert
        Assertions.assertThat(requestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateRequestsOnHoldTestAccepted() throws URISyntaxException {
        Company company = new Company();
        User user = new User();
        user.setUserId(4L);
        company.setUser(user);

        request = new Request();
        request.setId(7L);

        RequestOnHoldDto requestOnHoldDto = new RequestOnHoldDto();
        requestOnHoldDto.setId(7L);
        LocalDateTime time = LocalDateTime.parse("2019-10-10 21:05:44", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        requestOnHoldDto.setDateRequestAccepted(time.atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli());

        List<RequestOnHoldDto> requestOnHoldDtoList = new ArrayList<>();
        requestOnHoldDtoList.add(requestOnHoldDto);

        // arrange
        when(companyService.getByUserId(4L)).thenReturn(company);
        when(requestService.getRequestById(7L)).thenReturn(request);
        when(requestService.saveRequest(request)).thenReturn(request);
        // act
        ResponseEntity requestResponseEntity = requestController.updateRequestsOnHold(4l, requestOnHoldDtoList);
        // assert
        Assertions.assertThat(requestResponseEntity).isNotNull();
        Assertions.assertThat(requestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateRequestOnHoldTestFailed() throws URISyntaxException {
        Company company = new Company();
        company.setCompanyId(4L);
        // arrange
        when(companyService.getByUserId(company.getCompanyId())).thenReturn(null);
        // act
        ResponseEntity requestResponseEntity = requestController.updateRequestsOnHold(4L, new ArrayList<>());
        // assert
        Assertions.assertThat(requestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @After
    public void tearDown() {
        requestDto = null;
        request = null;
    }
}
