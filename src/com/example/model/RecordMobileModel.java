package com.example.model;

public class RecordMobileModel {

	String policy_number, dob, pan_number, mobile_number, email;

	public RecordMobileModel(String policy_number, String dob,
			String pan_number, String mobile_number, String email) {
		super();
		this.policy_number = policy_number;
		this.dob = dob;
		this.pan_number = pan_number;
		this.mobile_number = mobile_number;
		this.email = email;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPolicy_number() {
		return policy_number;
	}

	public void setPolicy_number(String policy_number) {
		this.policy_number = policy_number;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPan_number() {
		return pan_number;
	}

	public void setPan_number(String pan_number) {
		this.pan_number = pan_number;
	}

}// class
