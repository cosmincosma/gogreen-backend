package com.tssyonder.gogreen.controllers;

import com.tssyonder.gogreen.dtos.*;
import com.tssyonder.gogreen.dtos.convertor.DtoConvertor;
import com.tssyonder.gogreen.entities.*;
import com.tssyonder.gogreen.services.*;
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
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DistanceBetweenUsers distanceBetweenUsers;

    @Autowired
    private RequestService requestService;

    @Autowired
    private MaterialService materialService;

    private Validator validator;

    public UserController() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @PostMapping
    public ResponseEntity saveUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        DtoConvertor dtoConvertor = new DtoConvertor();
        User user = dtoConvertor.convertUserDtoToUserEntity(userDto);

        Map<String, String> validations = checkValidations(userDto);

        if (!validations.isEmpty()) {
            return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);

        validations.put("Results", "The user will be created with succes.");
        return new ResponseEntity<>(validations, HttpStatus.OK);
    }


    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody(required = false) UserLoginDto userLoginDto) {

        if (checkLoginFields(userLoginDto))
            return new ResponseEntity<>(Consts.LOGIN_ERROR, HttpStatus.BAD_REQUEST);

        User user = userService.getByEmail(userLoginDto.getEmail());

        return checkUserExistence(userLoginDto, user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            Map<String, String> userNullMessage = new HashMap<>();
            userNullMessage.put("message", Consts.USER_NOT_FOUND);
            return new ResponseEntity<>(userNullMessage, HttpStatus.OK);
        }

        ResponseEntity userDetailsDto = returnUserDetailsByRole(user);

        if (userDetailsDto != null)
            return userDetailsDto;
        else
            return new ResponseEntity<>("Bad request.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserMapDetailsDto> userMapDetailsDtos = new ArrayList<>();

        setUserMapDetails(users, userMapDetailsDtos);

        return new ResponseEntity<>(userMapDetailsDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/nearby-users-for-citizen/{id}")
    public ResponseEntity getNearbyUsersForCitizen(@PathVariable Long id) {
        Citizen citizen = citizenService.getByUserId(id);
        if (checkCitizenExistance(citizen)) return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        Double citizenLatitude = Double.parseDouble(citizen.getLatitude());
        Double citizenLongitude = Double.parseDouble(citizen.getLongitude());
        List<CitizenNearbyDto> citizenNearbyDtos = setNearbyCitizens(citizenLatitude, citizenLongitude);
        List<CompanyDetailsHoverDto> companyDetailsHoverDtos = setNearbyCompanies(citizenLatitude, citizenLongitude);

        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(companyDetailsHoverDtos);
        combinedList.addAll(citizenNearbyDtos);

        return new ResponseEntity<>(combinedList, HttpStatus.OK);
    }

    @GetMapping(value = "/nearby-users-for-company/{id}")
    public ResponseEntity getNearbyUsersForCompany(@PathVariable Long id) {
        Company company = companyService.getByUserId(id);
        if (checkCompanyExistance(company)) return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        Double companyLatitude = Double.parseDouble(company.getLatitude());
        Double companyLongitude = Double.parseDouble(company.getLongitude());
        List<CitizenNearbyDto> citizenNearbyDtos = setNearbyCitizens(companyLatitude, companyLongitude);
        List<CompanyDetailsHoverDto> companyDetailsHoverDtos = setNearbyCompanies(companyLatitude, companyLongitude);

        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(companyDetailsHoverDtos);
        combinedList.addAll(citizenNearbyDtos);

        return new ResponseEntity<>(combinedList, HttpStatus.OK);
    }

    @GetMapping(value = "/nearby-users-after-moving")
    public ResponseEntity getNearbyUsersForAfterMapMoving(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Long id) {
        Map<String, String> message = new HashMap<>();
        message.put("error", "Invalid latitude and longitude.");
        if (checkLatitudeAndLongitude(latitude, longitude))
            return new ResponseEntity<>(message, HttpStatus.OK);
        List<CompanyDetailsHoverDto> companyDetailsHoverDtoList = setNearbyCompanies(latitude, longitude);
        List<CitizenNearbyDto> citizenNearbyDtos = setNearbyCitizens(latitude, longitude);

        removeUserWithLocationLoggedIn(id, companyDetailsHoverDtoList, citizenNearbyDtos);

        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(companyDetailsHoverDtoList);
        combinedList.addAll(citizenNearbyDtos);

        return new ResponseEntity<>(combinedList, HttpStatus.OK);
    }

    @GetMapping(value = "/companies/hover-info")
    public ResponseEntity getAllCompaniesWithHoverInfo() {
        List<CompanyDetailsHoverDto> companyDetailsHoverDtoList = new ArrayList<>();
        DtoConvertor dtoConvertor = new DtoConvertor();
        setCompaniesHoverInfos(companyDetailsHoverDtoList, dtoConvertor);
        return new ResponseEntity(companyDetailsHoverDtoList, HttpStatus.OK);
    }

    private void setUserMapDetails(List<User> users, List<UserMapDetailsDto> userMapDetailsDtos) {
        for (User user : users) {
            UserMapDetailsDto userMapDetailsDto = new UserMapDetailsDto();
            userMapDetailsDto.setId(user.getUserId());
            userMapDetailsDto.setRole(user.getRole());
            if (user.getRole() == Role.CITIZEN) {
                Citizen citizen = citizenService.getByUserId(user.getUserId());
                userMapDetailsDto.setLatitude(citizen.getLatitude());
                userMapDetailsDto.setLongitude(citizen.getLongitude());
            } else if (user.getRole() == Role.COMPANY) {
                Company company = companyService.getByUserId(user.getUserId());
                userMapDetailsDto.setLatitude(company.getLatitude());
                userMapDetailsDto.setLongitude(company.getLongitude());
            }
            userMapDetailsDtos.add(userMapDetailsDto);
        }
    }

    private ResponseEntity returnUserDetailsByRole(User user) {
        DtoConvertor dtoConvertor = new DtoConvertor();
        if (user.getRole() == Role.CITIZEN) {
            Citizen citizen = citizenService.getByUserId(user.getUserId());
            CitizenDetailsDto citizenDetailsDto = dtoConvertor.convertCitizenDetailsDtoToCitizenEntity(citizen, user);

            return new ResponseEntity<>(citizenDetailsDto, HttpStatus.OK);
        } else if (user.getRole() == Role.COMPANY) {
            Company company = companyService.getByUserId(user.getUserId());
            CompanyDetailsDto companyDetailsDto = dtoConvertor.convertCompanyDetailsDtoToCompanyEntity(company, user);

            return new ResponseEntity<>(companyDetailsDto, HttpStatus.OK);
        }
        return null;
    }

    private ResponseEntity checkUserExistence(UserLoginDto userLoginDto, User user) {
        if (user == null) {
            return new ResponseEntity<>(Consts.LOGIN_ERROR, HttpStatus.BAD_REQUEST);
        }
        if (validate(userLoginDto, user.getEmail(), user.getPassword())) {
            ResponseEntity citizenDetailsDto = returnUserDetailsByRole(user);
            if (citizenDetailsDto != null)
                return citizenDetailsDto;
        }
        return new ResponseEntity<>(Consts.LOGIN_ERROR, HttpStatus.BAD_REQUEST);
    }

    private boolean checkLoginFields(UserLoginDto userLoginDto) {
        if (userLoginDto == null) {
            return true;
        } else if (userLoginDto.getEmail() == null || userLoginDto.getEmail().isEmpty()) {
            return true;
        } else {
            return userLoginDto.getPassword() == null || userLoginDto.getPassword().isEmpty();
        }
    }

    private Map<String, String> checkValidations(UserDto userDto) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        Map<String, String> validations = new HashMap<>();
        for (ConstraintViolation<UserDto> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validations.put(propertyPath, message);
        }
        return validations;
    }

    private boolean validate(UserLoginDto userLoginDto, String email, String password) {
        String encryptPwd = EncryptDecrypt.encrypt(userLoginDto.getPassword(), Consts.SECRET_KEY);
        return userLoginDto.getEmail().equals(email) && encryptPwd.equals(password);
    }

    private List<CitizenNearbyDto> setNearbyCitizens(Double latitude, Double longitude) {
        List<Citizen> nearbyCitizens = distanceBetweenUsers.getAllNearbyCitizensWithStatusOnHold(latitude, longitude);
        List<CitizenNearbyDto> citizenNearbyDtoList = new ArrayList<>();
        DtoConvertor dtoConvertor = new DtoConvertor();

        for (Citizen citizen : nearbyCitizens) {
            CitizenNearbyDto citizenNearbyDto = dtoConvertor.convertCitizenEntityToCitizenNearbyDto(citizen);
            User user = userService.getByCitizenId(citizen.getCitizenId());

            List<String> materials = new ArrayList<>();

            for (Request request : requestService.getAllByCitizenId(citizen.getCitizenId())) {
                Material material = request.getMaterial();
                if (!materials.contains(material.getMaterialName())) {
                    materials.add(material.getMaterialName());
                }
            }

            citizenNearbyDto.setCitizenRequestsMaterials(materials);
            citizenNearbyDto.setId(user.getUserId());
            citizenNearbyDtoList.add(citizenNearbyDto);
        }
        return citizenNearbyDtoList;
    }

    private List<CompanyDetailsHoverDto> setNearbyCompanies(Double latitude, Double longitude) {
        List<Company> nearbyCompany = distanceBetweenUsers.getAllNearbyCompany(latitude, longitude);
        List<CompanyDetailsHoverDto> companyDetailsHoverDtos = new ArrayList<>();
        DtoConvertor dtoConvertor = new DtoConvertor();

        for (Company company : nearbyCompany) {
            List<String> materials = new ArrayList<>();
            for (Material material : company.getMaterialList()) {

                if (materialService.getMaterialByName(material.getMaterialName()) != null && !materials.contains(material.getMaterialName())) {
                    materials.add(material.getMaterialName());
                }
            }
            CompanyDetailsHoverDto companyDetailsHoverDto = dtoConvertor.convertCompanyEntityToCompanyDetailsHoverDto(company, materials);
            User user = userService.getByCompanyId(company.getCompanyId());
            companyDetailsHoverDto.setId(user.getUserId());
            companyDetailsHoverDtos.add(companyDetailsHoverDto);
        }
        return companyDetailsHoverDtos;
    }

    private void setCompaniesHoverInfos(List<CompanyDetailsHoverDto> companyDetailsHoverDtoList, DtoConvertor dtoConvertor) {
        for (Company company : companyService.getAllCompanies()) {

            List<String> materials = new ArrayList<>();
            for (Material material : company.getMaterialList()) {
                if (materialService.getMaterialByName(material.getMaterialName()) != null && !materials.contains(material.getMaterialName())) {
                    materials.add(material.getMaterialName());
                }
            }
            CompanyDetailsHoverDto companyDetailsHoverDto = dtoConvertor.convertCompanyEntityToCompanyDetailsHoverDto(company, materials);
            User user = userService.getByCompanyId(company.getCompanyId());
            companyDetailsHoverDto.setId(user.getUserId());
            companyDetailsHoverDtoList.add(companyDetailsHoverDto);
        }
    }

    private boolean checkLatitudeAndLongitude(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return true;
        }
        return false;
    }

    private boolean checkCitizenExistance(Citizen citizen) {
        if (citizen == null) {
            return true;
        }
        return false;
    }

    private boolean checkCompanyExistance(Company company) {
        if (company == null) {
            return true;
        }
        return false;
    }

    private void removeUserWithLocationLoggedIn(@RequestParam Long id, List<CompanyDetailsHoverDto> companyDetailsHoverDtoList, List<CitizenNearbyDto> citizenNearbyDtos) {
        Iterator companiesIterator = companyDetailsHoverDtoList.iterator();
        CompanyDetailsHoverDto companyDetailsHoverDto;
        while (companiesIterator.hasNext()) {
            companyDetailsHoverDto = (CompanyDetailsHoverDto) companiesIterator.next();
            if (companyDetailsHoverDto.getId().equals(id)) {
                companiesIterator.remove();
            }
        }

        Iterator citizensIterator = citizenNearbyDtos.iterator();
        CitizenNearbyDto citizenNearbyDto;
        while (citizensIterator.hasNext()) {
            citizenNearbyDto = (CitizenNearbyDto) citizensIterator.next();
            if (citizenNearbyDto.getId().equals(id)) {
                citizensIterator.remove();
            }
        }
    }
}