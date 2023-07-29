package com.test.pojos;

import java.util.ArrayList;
import java.util.List;

public class AllEmpResPOJO {
	
	private String status;
	
	List<GetAllEmpPOJO> data;
	public List<GetAllEmpPOJO> getData() {
		return data;
	}
	public void setData(List<GetAllEmpPOJO> data) {
		this.data = data;
	}
	private String message;
	

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
