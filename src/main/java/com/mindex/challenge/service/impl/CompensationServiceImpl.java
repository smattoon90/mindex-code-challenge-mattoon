package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation createCompensation(Employee employee, Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        compensation.setEmployeeId(employee.getEmployeeId());
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation readCompensation(String id) {
        LOG.debug("Creating compensation with id [{}]", id);

        Compensation compensation = compensationRepository.findByEmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }

    @Override
    public Compensation updateCompensation(Compensation compensation) {
        LOG.debug("Updating compensation [{}]", compensation);

        return compensationRepository.save(compensation);
    }
}
