package com.techelevator.model;

import java.math.BigDecimal;

public class TaxResponseDto {
    private BigDecimal salesTax;
    private String lastUpdated;

    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(BigDecimal salesTax) {
        this.salesTax = salesTax;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
