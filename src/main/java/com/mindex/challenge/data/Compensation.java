package com.mindex.challenge.data;

public class Compensation {
    private String employeeId;
    private String compensation;
    private String effectiveDate;

    public Compensation(){}

    //Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    public String getCompensation() {
        return compensation;
    }
    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }
    public String getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

}
