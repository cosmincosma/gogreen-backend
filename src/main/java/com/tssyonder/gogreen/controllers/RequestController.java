package com.tssyonder.gogreen.controllers;

import com.tssyonder.gogreen.dtos.RequestAcceptedDto;
import com.tssyonder.gogreen.dtos.RequestDto;
import com.tssyonder.gogreen.dtos.RequestOnHoldDto;
import com.tssyonder.gogreen.dtos.RequestsListDto;
import com.tssyonder.gogreen.dtos.convertor.DtoConvertor;
import com.tssyonder.gogreen.entities.*;
import com.tssyonder.gogreen.services.CitizenService;
import com.tssyonder.gogreen.services.CompanyService;
import com.tssyonder.gogreen.services.MaterialService;
import com.tssyonder.gogreen.services.RequestService;
import com.tssyonder.gogreen.util.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private CompanyService companyService;

    private Validator validator;

    public RequestController() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity saveRequest(@RequestBody @Valid List<RequestDto> requestDtoList, @PathVariable Long id, BindingResult bindingResult) {
        DtoConvertor dtoConvertor = new DtoConvertor();
        Map<String, String> validations = new HashMap<>();

        checkRequestFields(requestDtoList, validations);

        if (!validations.isEmpty()) {
            return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
        }

        Map<String, String> citizenNullMessage = new HashMap<>();
        citizenNullMessage.put("message", Consts.USER_NOT_FOUND);
        if (saveRequest(requestDtoList, id, dtoConvertor))

            return new ResponseEntity<>(citizenNullMessage, HttpStatus.NOT_FOUND);

        validations.put("Results", "The requests will be created with succes.");
        return new ResponseEntity<>(validations, HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity getRequestById(@PathVariable Long id) {
        Request request = requestService.getRequestById(id);
        if (request == null) {
            Map<String, String> requestNullMessage = new HashMap<>();
            requestNullMessage.put("message", Consts.REQUEST_NOT_FOUND);
            return new ResponseEntity<>(requestNullMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(request, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        Request request = requestService.getRequestById(id);
        if (request == null) {
            Map<String, String> requestNullMessage = new HashMap<>();
            requestNullMessage.put("message", Consts.REQUEST_NOT_FOUND);
            return new ResponseEntity<>(requestNullMessage, HttpStatus.NOT_FOUND);
        } else {
            requestService.deleteRequest(id);
            Map<String, String> requestDeletedMessage = new HashMap<>();
            requestDeletedMessage.put("message", Consts.REQUEST_DELETED);
            return new ResponseEntity<>(requestDeletedMessage, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/citizens/{id}/requests")
    public ResponseEntity getAllByUserId(@PathVariable Long id) {
        List<RequestsListDto> requestsListDtos = new ArrayList<>();
        DtoConvertor dtoConvertor = new DtoConvertor();

        Citizen citizen = citizenService.getByUserId(id);
        if (citizen == null) {
            Map<String, String> citizenNullMessage = new HashMap<>();
            citizenNullMessage.put("message", Consts.USER_NOT_FOUND);
            return new ResponseEntity<>(citizenNullMessage, HttpStatus.NOT_FOUND);
        }

        return verifyRequests(id, requestsListDtos, dtoConvertor);
    }

    @GetMapping(value = "/citizens/{id}/requests-onhold")
    public ResponseEntity getAllByUserIdAndStatusOnHold(@PathVariable Long id) {
        List<RequestsListDto> requestsListDtos = new ArrayList<>();
        DtoConvertor dtoConvertor = new DtoConvertor();
        Citizen citizen = citizenService.getByUserId(id);
        if (citizen == null) {
            Map<String, String> citizenNullMessage = new HashMap<>();
            citizenNullMessage.put("message", Consts.USER_NOT_FOUND);
            return new ResponseEntity<>(citizenNullMessage, HttpStatus.NOT_FOUND);
        }

        return verifyRequestsOnHold(id, requestsListDtos, dtoConvertor);
    }

    @GetMapping(value = "/citizens/{id}/requests-ordered-by-quantity")
    public ResponseEntity getAllByUserIdOrderByQuantity(@PathVariable Long id) {
        List<RequestsListDto> requestsListDtos = new ArrayList<>();
        DtoConvertor dtoConvertor = new DtoConvertor();
        Citizen citizen = citizenService.getByUserId(id);
        if (citizen == null) {
            Map<String, String> citizenNullMessage = new HashMap<>();
            citizenNullMessage.put("message", Consts.USER_NOT_FOUND);
            return new ResponseEntity<>(citizenNullMessage, HttpStatus.NOT_FOUND);
        }

        return verifyAndGetRequestsOrderedByQuantity(id, requestsListDtos, dtoConvertor);
    }

    @PatchMapping(value = "/{id}/accept")
    public ResponseEntity updateRequestsOnHold(@PathVariable Long id, @RequestBody @Valid List<RequestOnHoldDto> requestOnHoldDtoList) {
        Company company = companyService.getByUserId(id);

        Map<String, String> validations = new HashMap<>();
        if (company == null) {
            Map<String, String> companyNullMessage = new HashMap<>();
            companyNullMessage.put("message", Consts.USER_NOT_FOUND);
            return new ResponseEntity<>(companyNullMessage, HttpStatus.NOT_FOUND);
        } else {
            checkRequestsOnHoldFields(requestOnHoldDtoList, validations);

            if (!validations.isEmpty()) {
                return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
            }
            return updateRequestOnHoldMethod(requestOnHoldDtoList, company);
        }
    }

    @PatchMapping(value = "/{id}/finish")
    public ResponseEntity updateRequestsAccepted(@RequestBody @Valid RequestAcceptedDto requestAcceptedDto) {

        Map<String, String> validations = new HashMap<>();

        checkRequestsAcceptedFields(requestAcceptedDto, validations);

        if (!validations.isEmpty()) {
            return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
        }

        return updateRequestAcceptedMethod(requestAcceptedDto);
    }

    private ResponseEntity verifyAndGetRequestsOrderedByQuantity(Long id, List<RequestsListDto> requestsListDtos, DtoConvertor dtoConvertor) {
        List<Request> requestList = requestService.getAllByUserIdOrderByQuantity(id);
        if (requestList == null || requestList.size() == 0) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            for (Request request : requestList) {
                RequestsListDto requestsListDto = dtoConvertor.convertRequestsListEntityToRequestsListDto(request);
                requestsListDtos.add(requestsListDto);
            }
        }
        return new ResponseEntity<>(requestsListDtos, HttpStatus.OK);
    }

    private boolean saveRequest(List<RequestDto> requestDtoList, Long id, DtoConvertor dtoConvertor) {
        Request request;
        Citizen citizen = citizenService.getByUserId(id);

        if (citizen == null) {
            return true;
        }
        for (RequestDto requestDto : requestDtoList) {
            Material material = materialService.getMaterialByName(requestDto.getMaterialName());
            request = dtoConvertor.convertRequestDtoToRequestEntity(requestDto, material);
            request.setCitizen(citizen);
            requestService.saveRequest(request);
        }
        return false;
    }

    private void checkRequestFields(List<RequestDto> requestDtoList, Map<String, String> validations) {
        for (int i = 0; i < requestDtoList.size(); i++) {
            RequestDto requestDto = requestDtoList.get(i);
            Set<ConstraintViolation<RequestDto>> violations = validator.validate(requestDto);

            if (materialService.getMaterialByName(requestDtoList.get(i).getMaterialName()) == null) {
                validations.put("materialName" + i, Consts.MATERIAL_NOT_FOUND);
            }

            for (ConstraintViolation<RequestDto> violation : violations) {
                String propertyPath = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                validations.put(propertyPath + i, message);
            }
        }
    }

    private ResponseEntity verifyRequests(Long id, List<RequestsListDto> requestsListDtos, DtoConvertor dtoConvertor) {
        List<Request> requestList = requestService.getAllByUserId(id);
        if (requestList == null || requestList.size() == 0) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            for (Request request : requestList) {
                if (request.getStatus() == Status.ON_HOLD) {
                    RequestsListDto requestsListDto = dtoConvertor.convertRequestsListEntityToRequestsListDto(request);
                    requestsListDtos.add(requestsListDto);
                } else if (request.getStatus() == Status.PROCESSING) {
                    RequestsListDto requestsListDto = dtoConvertor.convertRequestEntityToRequestsListDtoAccepted(request);
                    requestsListDtos.add(requestsListDto);
                } else if (request.getStatus() == Status.COMPLETED) {
                    RequestsListDto requestsListDto = dtoConvertor.convertRequestEntityToRequestsListDtoCompleted(request);
                    requestsListDtos.add(requestsListDto);
                }
            }
            return new ResponseEntity<>(requestsListDtos, HttpStatus.OK);
        }
    }

    private ResponseEntity verifyRequestsOnHold(Long id, List<RequestsListDto> requestsListDtos, DtoConvertor dtoConvertor) {
        List<Request> requestList = requestService.getAllByCitizenUserIdAndStatus(id, Status.ON_HOLD);
        if (requestList == null || requestList.size() == 0) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            for (Request request : requestList) {
                RequestsListDto requestsListDto = dtoConvertor.convertRequestsListEntityToRequestsListDto(request);
                requestsListDtos.add(requestsListDto);
            }
        }
        return new ResponseEntity<>(requestsListDtos, HttpStatus.OK);
    }

    private ResponseEntity updateRequestOnHoldMethod(List<RequestOnHoldDto> requestOnHoldDtoList, Company company) {
        Map<String, String> dateError = new HashMap<>();
        for (RequestOnHoldDto requestOnHoldDto : requestOnHoldDtoList) {
            Request requestFromDb = requestService.getRequestById(requestOnHoldDto.getId());
            if (requestFromDb == null) {
                Map<String, String> requestNullMessage = new HashMap<>();
                requestNullMessage.put("message", Consts.REQUEST_NOT_FOUND);
                return new ResponseEntity<>(requestNullMessage, HttpStatus.NOT_FOUND);
            } else {
                requestFromDb.setStatus(Status.PROCESSING);
                Date dateAccepted = new Date(requestOnHoldDto.getDateRequestAccepted());
                Date currentDate = new Date();

                dateError.put("dateRequestAccepted" + requestOnHoldDto.getId(), Consts.INVALID_DATE);

                if (dateAccepted.before(currentDate)) {
                    return new ResponseEntity<>(dateError, HttpStatus.BAD_REQUEST);
                }

                requestFromDb.setDateRequestAccepted(dateAccepted);
                requestFromDb.setCompany(company);

                requestService.saveRequest(requestFromDb);
            }
        }
        Map<String, String> requestUpdatedMessage = new HashMap<>();
        requestUpdatedMessage.put("message", Consts.REQUEST_UPDATED);
        return new ResponseEntity<>(requestUpdatedMessage, HttpStatus.OK);
    }

    private void checkRequestsOnHoldFields(List<RequestOnHoldDto> requestOnHoldDtoList, Map<String, String> validations) {
        for (int i = 0; i < requestOnHoldDtoList.size(); i++) {
            RequestOnHoldDto requestOnHoldDto = requestOnHoldDtoList.get(i);
            Set<ConstraintViolation<RequestOnHoldDto>> violations = validator.validate(requestOnHoldDto);

            for (ConstraintViolation<RequestOnHoldDto> violation : violations) {
                String propertyPath = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                validations.put(propertyPath + requestOnHoldDto.getId(), message);
            }
        }
    }

    private void checkRequestsAcceptedFields(RequestAcceptedDto requestAcceptedDto, Map<String, String> validations) {
        Set<ConstraintViolation<RequestAcceptedDto>> violations = validator.validate(requestAcceptedDto);

        for (ConstraintViolation<RequestAcceptedDto> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validations.put(propertyPath, message);
        }
    }

    private ResponseEntity updateRequestAcceptedMethod(RequestAcceptedDto requestAcceptedDto) {
        Request requestFromDb = requestService.getRequestById(requestAcceptedDto.getId());
        if (requestFromDb == null) {
            Map<String, String> requestNullMessage = new HashMap<>();
            requestNullMessage.put("message", Consts.REQUEST_NOT_FOUND);
            return new ResponseEntity<>(requestNullMessage, HttpStatus.NOT_FOUND);
        } else {
            requestFromDb.setStatus(requestAcceptedDto.getStatus());
            requestService.saveRequest(requestFromDb);
        }
        Map<String, String> requestUpdatedMessage = new HashMap<>();
        requestUpdatedMessage.put("message", Consts.REQUEST_UPDATED);
        return new ResponseEntity<>(requestUpdatedMessage, HttpStatus.OK);
    }
}