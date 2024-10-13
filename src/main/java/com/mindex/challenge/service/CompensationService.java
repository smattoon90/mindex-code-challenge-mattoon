package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;

public interface CompensationService {
    Compensation createCompensation(Employee employee, Compensation compensation);
    Compensation readCompensation(String compensationId);
    Compensation updateCompensation(Compensation compensation);
}
