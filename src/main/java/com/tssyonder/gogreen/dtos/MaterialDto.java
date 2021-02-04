package com.tssyonder.gogreen.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MaterialDto {

    @NotNull(message = "This field cannot be empty!")
    @Pattern(message = "Material name does not respect the field validation rules", regexp = "[a-zA-Z- ]*$")
    @Size(min = 1, max = 20, message = "Material name does not respect the field validation rules")
    private String materialName;

    public MaterialDto(String materialName) {
        this.materialName = materialName;
    }

    public MaterialDto() {
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
