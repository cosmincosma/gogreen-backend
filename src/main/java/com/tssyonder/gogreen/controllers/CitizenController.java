package com.tssyonder.gogreen.controllers;

import com.tssyonder.gogreen.dtos.CitizenDto;
import com.tssyonder.gogreen.dtos.convertor.DtoConvertor;
import com.tssyonder.gogreen.entities.Citizen;
import com.tssyonder.gogreen.entities.Role;
import com.tssyonder.gogreen.entities.User;
import com.tssyonder.gogreen.services.CitizenService;
import com.tssyonder.gogreen.services.UserService;
import com.tssyonder.gogreen.util.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/citizens")
public class CitizenController {

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private UserService userService;

    private Validator validator;

    public CitizenController() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @PostMapping
    public ResponseEntity saveCitizen(@RequestBody @Valid CitizenDto citizenDto, BindingResult bindingResult) {
        User user = new User();

        DtoConvertor dtoConvertor = new DtoConvertor();
        Citizen citizen = dtoConvertor.convertCitizenDtoToCitizenEntity(citizenDto);

        Map<String, String> validations = checkValidations(citizenDto);

        checkPhoneNumberUnicity(citizenDto, validations);
        checkEmailUnicity(citizenDto, validations);

        if (!validations.isEmpty()) {
            return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
        }

        setUserDetails(citizenDto, user);
        citizen.setUser(user);

        User userFromDatabase = userService.saveUser(user);
        citizenService.saveCitizen(citizen);

        validations.put("user_id", userFromDatabase.getUserId().toString());
        return new ResponseEntity<>(validations, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getCitizenById(@PathVariable Long id) {
        Citizen citizen = citizenService.getCitizenById(id);
        if (citizen == null) {
            Map<String, String> citizenNullMessage = new HashMap<>();
            citizenNullMessage.put("message", Consts.CITIZEN_NOT_FOUND);
            return new ResponseEntity<>(citizenNullMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(citizen, HttpStatus.OK);
        }
    }

    private Map<String, String> checkValidations(CitizenDto citizenDto) {
        Set<ConstraintViolation<CitizenDto>> violations = validator.validate(citizenDto);

        Map<String, String> validations = new HashMap<>();
        for (ConstraintViolation<CitizenDto> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validations.put(propertyPath, message);
        }
        return validations;
    }

    private void setUserDetails(CitizenDto citizenDto, User user) {
        user.setEmail(citizenDto.getEmail());
        user.setPassword(citizenDto.getPassword());
        user.setRole(Role.CITIZEN);
    }

    private void checkEmailUnicity(CitizenDto citizenDto, Map<String, String> validations) {
        if (userService.getByEmail(citizenDto.getEmail()) != null) {
            validations.put("email", "This email is already used");
        }
    }

    private void checkPhoneNumberUnicity(CitizenDto citizenDto, Map<String, String> validations) {
        if (citizenService.getCitizenByPhoneNumber(citizenDto.getCitizenPhoneNumber()) != null) {
            validations.put("citizenPhoneNumber", "This phone number is already used");
        }
    }
}
