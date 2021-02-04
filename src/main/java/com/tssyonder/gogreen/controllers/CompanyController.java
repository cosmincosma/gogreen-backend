package com.tssyonder.gogreen.controllers;


import com.tssyonder.gogreen.dtos.CollectionStationsDto;
import com.tssyonder.gogreen.dtos.CompanyDto;
import com.tssyonder.gogreen.dtos.OrdersListDto;
import com.tssyonder.gogreen.dtos.convertor.DtoConvertor;
import com.tssyonder.gogreen.entities.*;
import com.tssyonder.gogreen.services.CompanyService;
import com.tssyonder.gogreen.services.MaterialService;
import com.tssyonder.gogreen.services.RequestService;
import com.tssyonder.gogreen.services.UserService;
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
@RequestMapping(value = "/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    private Validator validator;

    public CompanyController() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @PostMapping
    public ResponseEntity saveCompany(@RequestBody @Valid CompanyDto companyDto, BindingResult bindingResult) {
        User user = new User();

        DtoConvertor dtoConvertor = new DtoConvertor();
        Company company = dtoConvertor.convertCompanyDtoToCompanyEntity(companyDto);

        List<Material> materials = new ArrayList<>();

        Map<String, String> validations = checkValidations(companyDto);

        checkUniqueCodeUnicity(companyDto, validations);
        checkPhoneNumberUnicity(companyDto, validations);
        checkEmailUnicity(companyDto, validations);

        if (!validations.isEmpty()) {
            return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
        }

        processMaterialList(companyDto, materials);
        materialService.saveAllMaterial(materials);
        company.setMaterialList(materials);

        setUserDetails(companyDto, user);
        company.setUser(user);
        User userFromDatabase = userService.saveUser(user);
        companyService.saveCompany(company);

        validations.put("user_id", userFromDatabase.getUserId().toString());
        return new ResponseEntity<>(validations, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        Map<String, String> companyNullMessage = new HashMap<>();
        companyNullMessage.put("message", Consts.USER_NOT_FOUND);
        if (company == null) {
            return new ResponseEntity<>(companyNullMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(company, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}/accepted-request")
    public ResponseEntity getAllRequestAccepted(@PathVariable Long id) {
        Company company = companyService.getByUserId(id);
        Map<String, String> companyNullMessage = new HashMap<>();
        companyNullMessage.put("message", Consts.USER_NOT_FOUND);
        if (company == null) {
            return new ResponseEntity<>(companyNullMessage, HttpStatus.NOT_FOUND);
        }

        if (requestService.getAllByCompanyUserIdAndStatus(id, Status.PROCESSING) == null || requestService.getAllByCompanyUserIdAndStatus(id, Status.PROCESSING).size() == 0) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            List<OrdersListDto> ordersList = new ArrayList<>();
            for (Request request : requestService.getAllByCompanyUserIdAndStatus(id, Status.PROCESSING)) {
                DtoConvertor dtoConvertor = new DtoConvertor();
                OrdersListDto ordersListDto = dtoConvertor.convertRequestEntityToOrdersListDto(request);
                ordersList.add(ordersListDto);
            }
            return new ResponseEntity<>(ordersList, HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity getAllCompanies() {
        List<CollectionStationsDto> collectionStationsDtoList = new ArrayList<>();
        DtoConvertor dtoConvertor = new DtoConvertor();
        for (Company company : companyService.getAllCompanies()) {
            CollectionStationsDto collectionStationsDto = dtoConvertor.convertCompanyEntityToCollectionStationsDto(company);
            collectionStationsDtoList.add(collectionStationsDto);
        }

        return new ResponseEntity(collectionStationsDtoList, HttpStatus.OK);
    }

    private void processMaterialList(CompanyDto companyDto, List<Material> materials) {
        for (String name : companyDto.getMaterialName()) {
            Material material = materialService.getMaterialByName(name);
            if (material != null) {
                materials.add(material);
            } else {
                Material materialNew = new Material();
                materialNew.setMaterialName(name);
                materials.add(materialNew);
            }
        }
    }

    private void setUserDetails(CompanyDto companyDto, User user) {
        user.setEmail(companyDto.getEmail());
        user.setPassword(companyDto.getPassword());
        user.setRole(Role.COMPANY);
    }

    private void checkEmailUnicity(CompanyDto companyDto, Map<String, String> validations) {
        if (userService.getByEmail(companyDto.getEmail()) != null) {
            validations.put("email", "This email is already used");
        }
    }

    private void checkPhoneNumberUnicity(CompanyDto companyDto, Map<String, String> validations) {
        if (companyService.getCompanyByPhoneNumber(companyDto.getCompanyPhoneNumber()) != null) {
            validations.put("companyPhoneNumber", "This phone number is already used");
        }
    }

    private void checkUniqueCodeUnicity(CompanyDto companyDto, Map<String, String> validations) {
        if (companyService.getCompanyByUniqueCode(companyDto.getUniqueCode()) != null) {
            validations.put("uniqueCode", "This UIC number is already used");
        }
    }

    private Map<String, String> checkValidations(CompanyDto companyDto) {
        Set<ConstraintViolation<CompanyDto>> violations = validator.validate(companyDto);
        Map<String, String> validations = new HashMap<>();
        for (ConstraintViolation<CompanyDto> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            if (propertyPath.startsWith("materialName[")) {
                propertyPath = "materialName";
            }
            validations.put(propertyPath, message);
        }
        return validations;
    }
}
