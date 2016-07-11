package com.example.blc;

import java.util.ArrayList;
import java.util.List;

import com.example.feedback.RatingsStats;
import com.example.model.RatingsCommentModel;

public class ParseXML {

	public static double avg_rating = 0.0;
	public static int total_count = 0, count_5_stars = 0, count_4_stars = 0,
			count_3_stars = 0, count_2_stars = 0, count_1_stars = 0;

	public String parseXmlTag(String ParentNode, String tag) {

		int start_indx = ParentNode.indexOf("<" + tag + ">");

		int end_indx = ParentNode.indexOf("</" + tag + ">",
				start_indx + tag.length());

		String content = null;

		if (start_indx >= 0 && end_indx > 0) {

			content = ParentNode.substring(start_indx + tag.length() + 2,
					end_indx);

		}

		return content;

	}

	public List<String> parseParentNode(String XML, String tag) {

		List<String> lsNode = new ArrayList<String>();

		while (XML.length() > 0) {

			int start_indx = XML.indexOf("<" + tag + ">");

			int end_indx = XML.indexOf("</" + tag + ">",
					start_indx + tag.length());

			String content = null;

			if (start_indx >= 0 && end_indx > 0) {

				content = XML
						.substring(start_indx + tag.length() + 2, end_indx);

				XML = XML.substring(end_indx + tag.length() + 3);

				lsNode.add(content);

			}

			else {

				XML = "";

			}

		}

		return lsNode;

	}

	public List<RatingsCommentModel> parseNodeElement(List<String> lsNode) {

		List<RatingsCommentModel> lsData = new ArrayList<RatingsCommentModel>();
		double sum = 0;

		for (String Node : lsNode) {

			String name = parseXmlTag(Node, "CUST_FULL_NAME");

			String app_rate = parseXmlTag(Node, "CUST_APPS_RATING");

			String rate_comm = parseXmlTag(Node, "CUST_APPS_RATING_COMMENTS");

			String cred_date = parseXmlTag(Node, "CREATEDDATE");

			RatingsCommentModel nodeVal = new RatingsCommentModel(name,
					app_rate, rate_comm, cred_date);

			lsData.add(nodeVal);

			switch (app_rate) {
			case "1":
				count_1_stars++;
				break;
			case "2":
				count_2_stars++;
				break;
			case "3":
				count_3_stars++;
				break;
			case "4":
				count_4_stars++;
				break;
			case "5":
				count_5_stars++;
				break;
			}// switch
			sum += Double.parseDouble(app_rate);
			total_count++;

		}// for

		avg_rating = sum / total_count;
		avg_rating = (double) Math.round(avg_rating * 10.00) / 10.00;
		return lsData;

	}// parseNodeElement()

}// class
