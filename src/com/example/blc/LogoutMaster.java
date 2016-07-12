package com.example.blc;

import com.example.model.AuthenticationModel;
import com.example.model.DashboardModel;
import com.example.model.LoginModel;
import com.example.model.RatingsCommentModel;
import com.example.model.RatingsModel;
import com.example.model.RecordMobileModel;
import com.example.model.RegisterUserModel;

public class LogoutMaster {

	public LogoutMaster() {
		LoginModel.setEmp_id("");
		new LoginModel("", "");
		new AuthenticationModel("", "");
		new DashboardModel("", "", "", "", "", "");
		new RatingsCommentModel("", "", "", "");
		new RatingsModel(0.0, "", "");
		new RecordMobileModel("", "", "", "", "");
		new RegisterUserModel("", "");
		RecordMobileModel.setMobile_number("");
		RecordMobileModel.setPolicy_number("");
		RecordMobileModel.setEmail("");
		RegisterUserModel.setName("");
		RecordMobileModel.setPan_number("");
		RecordMobileModel.setDob("");
		RatingsModel.setPurpose("");
		RatingsModel.setComments("");
	}

}// class
