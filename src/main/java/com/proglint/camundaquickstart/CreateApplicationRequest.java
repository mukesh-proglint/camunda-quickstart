package com.proglint.camundaquickstart;

import java.math.BigDecimal;

public class CreateApplicationRequest {
    private String carType;
    private BigDecimal sumInsured;

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }
}
