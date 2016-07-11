package com.example.model;

public class RecordMobileModel {

	static String policy_number = "", dob = "", pan_number = "",
			mobile_number = "", email = "";

	public RecordMobileModel(String policy_number, String dob,
			String pan_number, String mobile_number, String email) {
		RecordMobileModel.policy_number = policy_number;
		RecordMobileModel.dob = dob;
		RecordMobileModel.pan_number = pan_number;
		RecordMobileModel.mobile_number = mobile_number;
		RecordMobileModel.email = email;
	}

	public static String getMobile_number() {
		return mobile_number;
	}

	public static void setMobile_number(String mobile_number) {
		RecordMobileModel.mobile_number = mobile_number;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		RecordMobileModel.email = email;
	}

	public static String getPolicy_number() {
		return policy_number;
	}

	public static void setPolicy_number(String policy_number) {
		RecordMobileModel.policy_number = policy_number;
	}

	public static String getDob() {
		return dob;
	}

	public static void setDob(String dob) {
		RecordMobileModel.dob = dob;
	}

	public static String getPan_number() {
		return pan_number;
	}

	public static void setPan_number(String pan_number) {
		RecordMobileModel.pan_number = pan_number;
	}

}// class
