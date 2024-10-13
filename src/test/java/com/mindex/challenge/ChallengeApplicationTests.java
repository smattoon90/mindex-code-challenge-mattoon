package com.mindex.challenge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationRequest;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeApplicationTests {

	private String employeeUrl;
	private String employeeIdUrl;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeService employeeService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;  // ObjectMapper to convert Java objects to JSON

	@Before
	public void setup() {
		employeeUrl = "http://localhost:" + port + "/employee";
		employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
	}

	@Test
	public void contextLoads() throws JsonProcessingException {
		//John Lennon
		Employee employeeJL = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
		//Paul McCartney
		Employee employeePM = employeeRepository.findByEmployeeId("b7839309-3348-463b-a7e3-5de1c168beb3");
		//Ringo Starr
		Employee employeeRS = employeeRepository.findByEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f");
		//New Employee
		Employee employee1 = new Employee();
		employee1.setFirstName("Rome");
		employee1.setLastName("Odunze");
		employee1.setDepartment("Football");
		employee1.setPosition("WR");
		employee1.setDirectReports(Arrays.asList(employeeJL));

		Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, employee1, Employee.class).getBody();

		//Test the report structure calculations
		ReportingStructure reportingStructure = employeeService.getReport(employeeJL.getEmployeeId());
		assertEquals(reportingStructure.getNumberOfReports(),4);
		//System.out.println(employeeJL.getFirstName() + " " + employeeJL.getLastName() + " has " + + reportingStructure.getNumberOfReports() + " reports");
		reportingStructure = employeeService.getReport(employeePM.getEmployeeId());
		assertEquals(reportingStructure.getNumberOfReports(),0);
		//System.out.println(employeePM.getFirstName() + " " + employeePM.getLastName() + " has " + reportingStructure.getNumberOfReports() + " reports");
		reportingStructure = employeeService.getReport(employeeRS.getEmployeeId());
		assertEquals(reportingStructure.getNumberOfReports(),2);
		//System.out.println(employeeRS.getFirstName() + " " + employeeRS.getLastName() + " has " + + reportingStructure.getNumberOfReports() + " reports");
		reportingStructure = employeeService.getReport(createdEmployee1.getEmployeeId());
		//System.out.println(createdEmployee1.getFirstName() + " " + createdEmployee1.getLastName() + " has " + + reportingStructure.getNumberOfReports() + " reports");
		assertEquals(reportingStructure.getNumberOfReports(),5);

		//Add the compensation
		Compensation testCompensation = new Compensation();

		testCompensation.setCompensation("1000000");
		testCompensation.setEffectiveDate("10/13/24");
		System.out.println("Compensation: " + testCompensation.getEmployeeId());

		CompensationRequest compensationRequest = new CompensationRequest();
		compensationRequest.setEmployee(createdEmployee1);
		compensationRequest.setCompensation(testCompensation);

		// Convert the CompensationRequest to JSON using ObjectMapper
		String jsonRequestBody = objectMapper.writeValueAsString(compensationRequest);

		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		// POST the request and get the response
		HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
		Compensation createdCompensation = restTemplate.exchange("/compensation", HttpMethod.POST, requestEntity, Compensation.class).getBody();

		testCompensation.setEmployeeId(createdEmployee1.getEmployeeId());
		//Test applied compensation
		assertNotNull(createdCompensation.getEmployeeId());
		assertEquals(testCompensation.getCompensation(), createdCompensation.getCompensation());

	}

}
