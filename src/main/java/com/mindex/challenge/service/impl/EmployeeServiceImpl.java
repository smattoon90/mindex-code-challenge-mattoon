package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure getReport(String id) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        ReportingStructure report = new ReportingStructure();
        report.setEmployee(employee);
        report.setNumberOfReports(sumReports(employee));
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        return report;
    }

    //Using recursive method to iterate and sum total reports under employee
    private int sumReports(Employee employee) {

        //Check for zero direct reports
        if (employee == null || employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            //System.out.println("Detected no reports ");
            return 0;
        }

        int totalReports = employee.getDirectReports().size();
        //System.out.println("initial " + totalReports);

        //iterate through list of direct reports
        for(Employee underling : employee.getDirectReports()){
            Employee report = employeeRepository.findByEmployeeId(underling.getEmployeeId());
            totalReports += sumReports(report);
        }
        return totalReports;
    }
}
