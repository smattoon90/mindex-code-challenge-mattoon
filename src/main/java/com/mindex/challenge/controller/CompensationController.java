package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationRequest;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/compensation")
    public Compensation createCompensation(@RequestBody CompensationRequest request) {
        LOG.debug("Received compensation create request for [{}]", request);

        Employee employee = request.getEmployee();
        Compensation compensation = request.getCompensation();

        return compensationService.createCompensation(employee, compensation);
    }

    @GetMapping("/compensation/{id}")
    public Compensation readCompensation(@PathVariable String id) {
        LOG.debug("Received compensation create request for id [{}]", id);

        return compensationService.readCompensation(id);
    }

    @PutMapping("/compensation/{id}")
    public Compensation updateCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received compensation update request for id [{}] and compensation [{}]", id, compensation);

        compensation.setEmployeeId(id);
        return compensationService.updateCompensation(compensation);
    }
}
