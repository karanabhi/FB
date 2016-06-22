package com.example.model;

public class DashboardModel {

	String cust_id, mob_no, pol_no, name, email, sync_status;

	public DashboardModel(String cust_id, String mob_no, String pol_no,
			String name, String email, String sync_status) {
		super();
		this.cust_id = cust_id;
		this.mob_no = mob_no;
		this.pol_no = pol_no;
		this.name = name;
		this.email = email;
		this.sync_status = sync_status;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getMob_no() {
		return mob_no;
	}

	public void setMob_no(String mob_no) {
		this.mob_no = mob_no;
	}

	public String getPol_no() {
		return pol_no;
	}

	public void setPol_no(String pol_no) {
		this.pol_no = pol_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSync_status() {
		return sync_status;
	}

	public void setSync_status(String sync_status) {
		this.sync_status = sync_status;
	}

}// class
