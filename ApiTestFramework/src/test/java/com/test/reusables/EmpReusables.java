package com.test.reusables;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.test.pojos.AllEmpResPOJO;
import com.test.pojos.CreateEmpPOJO;
import com.test.pojos.CreateEmpResPOJO;
import com.test.pojos.GetAllEmpPOJO;
import com.test.pojos.GetAllEmpResPOJO;
import com.test.pojos.CreateEmpResPOJO.Data;
import com.test.utilities.ApiEndpoints;
import com.test.utilities.ExtentReportUtility;
//import com.test.utilities.ExtentReportUtility;
import com.test.utilities.Log4jUtility;
import com.test.utilities.PropUtility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class EmpReusables {

	public PropUtility propUtility = new PropUtility();
	public Properties propertyFile = propUtility.loadFile("employeesproperties");

	public Log4jUtility logObj = Log4jUtility.getInstance();
	protected Logger log = logObj.getLogger();
	public ExtentReportUtility report = ExtentReportUtility.getInstance();
	public String empName = propUtility.getPropertyValue("emp.name");
	public String empSalary = propUtility.getPropertyValue("emp.salary");
	public String empAge = propUtility.getPropertyValue("emp.age");
	
	public int idToDelete;

	public Response response;
	public int idCreated;

	public String getBaseUri() {
		String baseUri = propUtility.getPropertyValue("api.baseuri");
		return baseUri;
	}

	public Response getEmpData() {

//		response = RestAssured.given().contentType(ContentType.JSON).when().get(ApiEndpoints.ALL_EMPLOYEES);
		response = RestAssured.get(ApiEndpoints.ALL_EMPLOYEES);

		return response;
	}

	public List<GetAllEmpResPOJO> getAllEmpData() {

		response = RestAssured.given().contentType(ContentType.JSON).when().get("/employees");

		GetAllEmpResPOJO allEmps = response.as(GetAllEmpResPOJO.class);
		List<GetAllEmpResPOJO> list = Arrays.asList(allEmps);

		return list;
	}

	public Response createEmpData() {

		CreateEmpPOJO createEmp = new CreateEmpPOJO();
		createEmp.setName(empName);
		createEmp.setSalary(empSalary);
		createEmp.setAge(empAge);

		response = RestAssured.given().contentType(ContentType.JSON).body(createEmp).when().post("/create");

		return response;
	}

	public int getID() {
		response = createEmpData();
		CreateEmpResPOJO emps = response.getBody().as(CreateEmpResPOJO.class);
		idCreated = emps.getData().getId();
		System.out.println(idCreated);
		return idCreated;
	}

	public Response deleteCase1() {
		idToDelete = getID();

		Response response1 = RestAssured.given().contentType(ContentType.JSON).when().delete(ApiEndpoints.DELETE_EMPLOYEEBYID,idToDelete);
//		Response response1 = RestAssured.delete("/delete/{id}", idToDelete);
		return response1;
	}

	public Response deleteEmpIdNotExists() {
		int idToDelete = 0;

		Response response1 = RestAssured.given().when().delete(ApiEndpoints.DELETE_EMPLOYEEBYID,idToDelete);
		return response1;
	}

	public Response getEmployeeByID() {
		int id = 2;
		response = RestAssured.given().contentType(ContentType.JSON).when().get(ApiEndpoints.GET_EMPLOYEEBYID,id);

		return response;
	}

	public int getStatusCode(Response response) {
		return response.getStatusCode();
	}

	public String getContentType(Response response) {
		return response.getContentType();
	}

	public void verifyStatusCode(Response response, int statusCode) {
		response.then().statusCode(statusCode);
		log.info(statusCode);
	}

	public long getResponseTimeIn(Response response, TimeUnit unit) {
		return response.getTimeIn(unit);
	}

	public String getJsonPathData(Response response, String status) {
		return response.jsonPath().getString(status);
	}

	public void getResponseBody(Response response) {
		response.body().asPrettyString();
	}

}