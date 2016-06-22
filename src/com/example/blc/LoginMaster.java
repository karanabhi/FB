package com.example.blc;

import com.example.model.*;

public class LoginMaster {

	public boolean validateEmployee(LoginModel lm) {

		if (LoginModel.getEmp_id().equals("admin")
				&& LoginModel.getEmp_password().equals("admin")) {

			return true;
		}
		return false;
	}// validateEmployee()

}// class