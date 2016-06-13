package com.example.model;

public class RatingsModel {

	String comments;
	double ratings;

	public RatingsModel(double ratings, String comments) {
		this.comments = comments;
		this.ratings = ratings;
	}// constructor

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public double getRatings() {
		return ratings;
	}

	public void setRatings(double ratings) {
		this.ratings = ratings;
	}

}// class
