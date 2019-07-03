package org.profile.service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.apache.sling.api.SlingHttpServletRequest;

public interface EventServiceUser {

	public ArrayList<Node> getEvent(String searchKeyword);
	public ArrayList<Node> getDefaultEvent();
	public String callService(String urlStr, String[] paramName,
            String[] paramValue);
	public String sendToken(String userId[]);
	public String callGetService(String urlString);
	public List getCustomerDetails(SlingHttpServletRequest RE,String customerId, PrintWriter out) ;
	public String getChatUrl();
	public String getSipmlUrl();
	public String getSipmlExtn(String userName);
	public String getUserChatPassword(String uid);
}
