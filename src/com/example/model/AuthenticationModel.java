package com.example.model;

public class AuthenticationModel {

	String mobile_number, policy_number;

	public AuthenticationModel(String number, int status) {
		if (status == 1) {
			this.mobile_number = number;
		} else if (status == 2) {
			this.policy_number = number;
		}

	}// mobile_number Constructor

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getPolicy_number() {
		return policy_number;
	}

	public void setPolicy_number(String policy_number) {
		this.policy_number = policy_number;
	}

}// class
