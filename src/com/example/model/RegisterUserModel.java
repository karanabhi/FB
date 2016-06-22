package com.example.model;

public class RegisterUserModel {

	static String name = "", mobile_number;

	public RegisterUserModel(String name, String mobile_number) {
		RegisterUserModel.mobile_number = mobile_number;
		RegisterUserModel.name = name;

	}// constructor

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		RegisterUserModel.name = name;
	}

	public static String getMobile_number() {
		return mobile_number;
	}

	public static void setMobile_number(String mobile_number) {
		RegisterUserModel.mobile_number = mobile_number;
	}

}// class
