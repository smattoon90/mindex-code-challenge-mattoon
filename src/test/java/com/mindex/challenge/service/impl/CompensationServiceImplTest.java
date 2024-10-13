package com.mindex.challenge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationRequest;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private ObjectMapper objectMapper;  // ObjectMapper to convert Java objects to JSON

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateReadUpdate() throws JsonProcessingException {
        //Create the best(test) employee
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Caleb");
        testEmployee.setLastName("Williams");
        testEmployee.setDepartment("Football");
        testEmployee.setPosition("QB");

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();


        Compensation testCompensation = new Compensation();

        testCompensation.setCompensation("100000");
        testCompensation.setEffectiveDate("10/14/24");
        System.out.println("Compensation: " + testCompensation.getEmployeeId());

        CompensationRequest compensationRequest = new CompensationRequest();
        compensationRequest.setEmployee(createdEmployee);
        compensationRequest.setCompensation(testCompensation);

        // Convert the CompensationRequest to JSON using ObjectMapper
        String jsonRequestBody = objectMapper.writeValueAsString(compensationRequest);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // POST the request and get the response
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
        Compensation createdCompensation = restTemplate.exchange(compensationUrl, HttpMethod.POST, requestEntity, Compensation.class).getBody();

        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());

        assertNotNull(createdCompensation.getEmployeeId());
        assertCompensationEquivalence(testCompensation, createdCompensation);


        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployeeId()).getBody();
        assertEquals(createdCompensation.getEmployeeId(), readCompensation.getEmployeeId());
        assertCompensationEquivalence(createdCompensation, readCompensation);


        // Update checks
        readCompensation.setEffectiveDate("9/20/2024");

        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);

        Compensation updatedCompensation =
                restTemplate.exchange(compensationIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Compensation>(readCompensation, headers),
                        Compensation.class,
                        readCompensation.getEmployeeId()).getBody();

        assertCompensationEquivalence(readCompensation, updatedCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        //System.out.println("IDs " + expected.getEmployeeId() + " " + actual.getEmployeeId());
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        //System.out.println("IDs " + expected.getCompensation() + " " + actual.getCompensation());
        assertEquals(expected.getCompensation(), actual.getCompensation());
        //System.out.println("IDs " + expected.getEffectiveDate() + " " + actual.getEffectiveDate());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
