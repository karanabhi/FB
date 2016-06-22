package com.example.model;

public class LoginModel {

	static String emp_id, emp_password;

	public LoginModel(String emp_id, String emp_password) {
		LoginModel.emp_id = emp_id;
		LoginModel.emp_password = emp_password;
	}// constructor

	public static String getEmp_id() {
		return emp_id;
	}

	public static void setEmp_id(String emp_id) {
		LoginModel.emp_id = emp_id;
	}

	public static String getEmp_password() {
		return emp_password;
	}

	public static void setEmp_password(String emp_password) {
		LoginModel.emp_password = emp_password;
	}

}// class
