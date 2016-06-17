package com.example.model;

public class AuthenticationModel {

	static String mobile_number, policy_number;

	public AuthenticationModel(String mobile_number, String policy_number) {

		AuthenticationModel.mobile_number = mobile_number;
		AuthenticationModel.policy_number = policy_number;

	}// mobile_number Constructor

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		AuthenticationModel.mobile_number = mobile_number;
	}

	public String getPolicy_number() {
		return policy_number;
	}

	public void setPolicy_number(String policy_number) {
		AuthenticationModel.policy_number = policy_number;
	}

}// class
