package com.example.blc;

import com.example.model.*;

public class AuthenticationMaster {

	public boolean checkMobileNumber(AuthenticationModel am) {

		/*
		 * call web service to check mobile number
		 */

		if (am.getMobile_number().equals("111")) {

			/*
			 * since mobile number is correct return true else return false
			 */
			return true;
		}
		return false;
	}// checkMobileNumber()

	public boolean validatePolicyNumber() {

		/*
		 * call web service to validate policy number associated with the user
		 * entered mobile number
		 */

		return true;
	}// validatePolicyNumber

	public boolean checkPolicyNumber(AuthenticationModel am) {
		return true;
	}// checkPolicyNumber()

	public boolean validateMobileNumber() {
		return true;
	}// validateMobileNumber()

}// class
