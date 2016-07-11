package com.example.model;

public class RatingsCommentModel {

	String cust_name = "", app_rate = "", app_comment = "", created_date = "";

	public RatingsCommentModel(String cust_name, String app_rate,
			String app_comment, String created_date) {
		this.cust_name = cust_name;
		this.app_rate = app_rate;
		this.app_comment = app_comment;
		this.created_date = created_date;
	}// constructor

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public String getApp_rate() {
		return app_rate;
	}

	public void setApp_rate(String app_rate) {
		this.app_rate = app_rate;
	}

	public String getApp_comment() {
		return app_comment;
	}

	public void setApp_comment(String app_comment) {
		this.app_comment = app_comment;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

}// class
