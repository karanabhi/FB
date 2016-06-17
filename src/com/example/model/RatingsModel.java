package com.example.model;

public class RatingsModel {

	static String comments, purpose;
	static double ratings;

	public RatingsModel(double ratings, String comments, String purpose) {
		RatingsModel.comments = comments;
		RatingsModel.ratings = ratings;
		RatingsModel.purpose = purpose;
	}// constructor

	public static String getPurpose() {
		return purpose;
	}

	public static void setPurpose(String purpose) {
		RatingsModel.purpose = purpose;
	}

	public static String getComments() {
		return comments;
	}

	public static void setComments(String comments) {
		RatingsModel.comments = comments;
	}

	public static double getRatings() {
		return ratings;
	}

	public static void setRatings(double ratings) {
		RatingsModel.ratings = ratings;
	}

}// class
