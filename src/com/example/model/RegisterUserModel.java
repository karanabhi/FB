package com.example.model;

public class RegisterUserModel {

	String name, mobile_number;

	public RegisterUserModel(String name, String mobile_number) {
		this.mobile_number = mobile_number;
		this.name = name;

	}// constructor

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

}// class
