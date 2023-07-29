package com.test.scripts;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.test.pojos.AllEmpResPOJO;
import com.test.pojos.CreateEmpResPOJO;
import com.test.pojos.GetAllEmpPOJO;
import com.test.pojos.GetAllEmpResPOJO;
import com.test.pojos.GetAllEmpResPOJO.Data;
import com.test.reusables.EmpReusables;
//import com.test.utilities.ExtentReportUtility;
import com.test.utilities.Log4jUtility;
import com.test.utilities.PropUtility;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EmpAPIScripts extends EmpReusables {
	public PropUtility propUtility = new PropUtility();
	public Properties propertyFile = propUtility.loadFile("employeesproperties");

	public Log4jUtility logObj = Log4jUtility.getInstance();
	protected Logger log = logObj.getLogger();

	public String empName = propUtility.getPropertyValue("emp.name");
	public String empSalary = propUtility.getPropertyValue("emp.salary");
	public String empAge = propUtility.getPropertyValue("emp.age");

	public String getEmpName = propUtility.getPropertyValue("Empname");
	public String getEmpSal = propUtility.getPropertyValue("Empsalary");
	public String getEmpAge = propUtility.getPropertyValue("Empage");

	public int idCreated;

	@BeforeMethod
	public void init() {
		RestAssured.baseURI = getBaseUri();
	}

	@Test(priority = 1)
	public void getAllEmployees() {
		Response res = getEmpData();
		res.prettyPrint();
		AllEmpResPOJO emps = res.getBody().as(AllEmpResPOJO.class);
		verifyStatusCode(res, 200);
		String Status = emps.getStatus();
		Assert.assertEquals(Status, "success");

		List<List<GetAllEmpPOJO>> empData = Arrays.asList(emps.getData());
		List<Integer> totalCount = Arrays.asList((emps.getData().size()));
		log.info("Status Message is verified as success");
		report.logTestInfo("Status Message is verified as success");
		System.out.println("Total Count = " + totalCount);

		log.info("All Employees data and total record count is retrieved");
		report.logTestInfo("All Employees data and total record count is retrieved");
	}

	@Test
	public void createEmployeeRec() {
		Response res = createEmpData();
		res.prettyPrint();

		CreateEmpResPOJO emps = res.getBody().as(CreateEmpResPOJO.class);
		String status = getJsonPathData(res, "status");

		Assert.assertEquals(status, "success");
		log.info("Request status =" + status);
		verifyStatusCode(res, 200);
		log.info("Request status code is 200");
		String name = emps.getData().getName();
		String salary = emps.getData().getSalary();
		String age = emps.getData().getAge();

		Assert.assertEquals(empName, name);
		log.info("Name matched as given in the request");
		report.logTestInfo("Name matched as given in the request");

		Assert.assertEquals(empSalary, salary);
		log.info("Salary matched as given in the request");
		report.logTestInfo("Salary matched as given in the request");

		Assert.assertEquals(empAge, age);
		log.info("Age matched as given in the request");
		report.logTestInfo("Age matched as given in the request");

		idCreated = emps.getData().getId();
		log.info("Employee ID = " + idCreated);

	}

	@Test
	public void deleteByID() {

		Response res = deleteCase1();
		res.prettyPrint();
		String status = getJsonPathData(res, "status");
		Assert.assertEquals(status, "success");
		log.info("Request status =" + status);

		report.logTestInfo("Request status is success");
		verifyStatusCode(res, 200);
		log.info("Request status code is 200");
		report.logTestInfo("Request status code is 200");
		String message = getJsonPathData(res, "message");
		log.info("Message = " + message);
		report.logTestInfo("Message = " + message);
	}

	@Test
	public void deleteIDNotExists() {

		Response res = deleteEmpIdNotExists();
		res.prettyPrint();
		String status = getJsonPathData(res, "status");
		Assert.assertEquals(status, "error");
		log.info("Request status =" + status);
		report.logTestInfo("Request status is error");
		verifyStatusCode(res, 400);
		log.info("Request status code is 400");
		report.logTestInfo("Request status code is 400");
		String message = res.body().jsonPath().get("message");
		System.out.println("Message=" + message);

		log.info("Message = " + message);
		report.logTestInfo("Message=" + message);
	}

	@Test
	public void getEmployee() {

		Response res = getEmployeeByID();
		res.prettyPrint();

		int statusCode = res.statusCode();
		log.info("Status code : " + statusCode);
		Assert.assertEquals(statusCode, 200);

		String status = res.body().jsonPath().get("status");
		Assert.assertEquals(status, "success");

		String content = res.contentType();
		log.info("Content is : " + content);
		Assert.assertEquals(content, "application/json");

		String empName = res.body().jsonPath().getString("employee_name");
		Assert.assertTrue(empName.equalsIgnoreCase(getEmpName));

		String empsalary = res.body().jsonPath().getString("employee_salary");
		Assert.assertEquals(empsalary, getEmpSal);

		String empAge = res.body().jsonPath().getString("employee_age");
		Assert.assertEquals(empAge, getEmpAge);
	}

}