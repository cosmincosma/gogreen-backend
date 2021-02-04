package com.tssyonder.gogreen.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RequestDto {

    @NotBlank(message = "Required")
    private String unit;

    @NotBlank(message = "Required")
    @Pattern(message = "Only numbers between 0,1 and 500.", regexp = "^(([1-4][0-9][0-9],[0-9]*)|([1-4][0-9][0-9])|(100(\\.[0]{1,2})?|[1-9][0-9](\\,[0-9]*)?)|([0],[1-9][0-9]*)|10|([1][0],[0-9]*)|500|([1],[0-9]*)|1|[1-9]|([1-9],[0-9]*))$")
    private String quantity;

    @NotBlank(message = "Required")
    private String materialName;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
