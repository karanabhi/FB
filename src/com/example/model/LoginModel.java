package com.example.model;

public class LoginModel {

	static String emp_id;
	String emp_password;

	public LoginModel(String emp_id, String emp_password) {
		LoginModel.emp_id = emp_id;
		this.emp_password = emp_password;
	}// constructor

	public static String getEmp_id() {
		return emp_id;
	}

	public static void setEmp_id(String emp_id) {
		LoginModel.emp_id = emp_id;
	}

	public String getEmp_password() {
		return emp_password;
	}

	public void setEmp_password(String emp_password) {
		this.emp_password = emp_password;
	}

}// class
