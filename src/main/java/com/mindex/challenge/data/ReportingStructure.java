package com.mindex.challenge.data;

import com.mindex.challenge.dao.EmployeeRepository;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure(Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    //Default constructor
    public ReportingStructure() {}

    //Get method
    public Employee getEmployee() {
        return employee;
    }

    //Set method
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    //Get method
    public int getNumberOfReports() {
        return numberOfReports;
    }

    //Set number of direct + nested reports
    public void setNumberOfReports(int reports) {
        this.numberOfReports = reports;
    }



}
