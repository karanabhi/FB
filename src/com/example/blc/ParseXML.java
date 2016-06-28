package com.example.blc;

import java.util.ArrayList;
import java.util.List;

public class ParseXML {

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

	public List<XMLHolderIssuPolicy> parseNodeElementpolicy_issu(
			List<String> lsNode) {

		List<XMLHolderIssuPolicy> lsData = new ArrayList<XMLHolderIssuPolicy>();

		for (String Node : lsNode) {

			String No = parseXmlTag(Node, "PL_PROP_NUM");

			XMLHolderIssuPolicy nodeVal = new XMLHolderIssuPolicy(No);

			lsData.add(nodeVal);

		}

		return lsData;

	}

	class XMLHolderIssuPolicy {

		private String proposal_no;

		public XMLHolderIssuPolicy(String proposal_no) {

			super();

			this.proposal_no = proposal_no;

		}

		public String getproposal_no() {

			return proposal_no;

		}

		public void setproposal_no(String proposal_no) {

			this.proposal_no = proposal_no;

		}
	}
}
