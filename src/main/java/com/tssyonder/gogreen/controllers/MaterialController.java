package com.tssyonder.gogreen.controllers;

import com.tssyonder.gogreen.dtos.MaterialDto;
import com.tssyonder.gogreen.dtos.convertor.DtoConvertor;
import com.tssyonder.gogreen.entities.Material;
import com.tssyonder.gogreen.services.MaterialService;
import com.tssyonder.gogreen.util.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@CrossOrigin
@RequestMapping(value = "/api/materials")
@RestController
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    private Validator validator;

    public MaterialController() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @PostMapping
    public ResponseEntity saveMaterial(@RequestBody @Valid MaterialDto materialDto, BindingResult bindingResult) {
        DtoConvertor dtoConvertor = new DtoConvertor();
        Material material = dtoConvertor.convertMaterialDtoToMaterialEntity(materialDto);

        Map<String, String> validations = checkValidations(materialDto);

        checkMaterialUnicity(materialDto, validations);

        if (!validations.isEmpty()) {
            return new ResponseEntity<>(validations, HttpStatus.BAD_REQUEST);
        }

        materialService.saveMaterial(material);

        validations.put("Results", "The material will be created with succes.");
        return new ResponseEntity<>(validations, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getMaterialById(@PathVariable Long id) {
        Material material = materialService.getMaterialById(id);
        Map<String, String> materialNullMessage = new HashMap<>();
        materialNullMessage.put("message", Consts.MATERIAL_NOT_FOUND);
        if (material == null) {
            return new ResponseEntity<>(materialNullMessage, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(material, HttpStatus.OK);
        }
    }

    @GetMapping()
    public List<MaterialDto> getAllMaterials() {
        List<MaterialDto> materialDtos = new ArrayList<>();
        DtoConvertor dtoConvertor = new DtoConvertor();

        for (Material material : materialService.getAllMaterials()) {
            MaterialDto materialDto = dtoConvertor.convertMaterialEntityToMaterialDto(material);
            materialDtos.add(materialDto);
        }
        return materialDtos;
    }

    private Map<String, String> checkValidations(MaterialDto materialDto) {
        Set<ConstraintViolation<MaterialDto>> violations = validator.validate(materialDto);

        Map<String, String> validations = new HashMap<>();
        for (ConstraintViolation<MaterialDto> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validations.put(propertyPath, message);
        }
        return validations;
    }

    private void checkMaterialUnicity(MaterialDto materialDto, Map<String, String> validations) {
        if (materialService.getMaterialByName(materialDto.getMaterialName()) != null) {
            validations.put("material", "This material is already added");
        }
    }
}
