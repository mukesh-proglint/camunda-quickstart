package com.proglint.camundaquickstart;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Application {
    @Id
    @GeneratedValue
    private Long id;
    private String carType;
    private BigDecimal sumInsured;
    @ManyToOne
    private Underwriter underwriter;

    public Application() {
    }

    public Application(String carType, BigDecimal sumInsured) {
        this.carType = carType;
        this.sumInsured = sumInsured;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Underwriter getUnderwriter() {
        return underwriter;
    }

    public void setUnderwriter(Underwriter underwriter) {
        this.underwriter = underwriter;
    }
}
