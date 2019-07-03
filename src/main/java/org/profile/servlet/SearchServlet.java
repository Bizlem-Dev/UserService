package org.profile.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.profile.service.ProfileService;
import org.profile.service.SecurityService;
import org.apache.sling.jcr.api.SlingRepository;
import org.exhibition.service.ExhibitionService;

import biz.com.service.EventService;
import biz.com.service.ProductService;
import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;

//import biz.com.app.servlet.JSONObject;
import biz.com.service.NewsService;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
		@Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/common/search" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "profile","user",
				"company", "profileDiv", "companyDiv", "group", "groupDiv",
				"checkStatus", "call" })

})
@SuppressWarnings("serial")
public class SearchServlet extends SlingAllMethodsServlet {

	@Reference(referenceInterface = ProfileService.class, name = "service")
	private ProfileService service;

	@Reference
	private SlingRepository repos;

	@Reference
	private SecurityService security_service;

	@Reference
	ExhibitionService exservice;

	@Reference
	NewsService nservice;

	@Reference
	ProductService bizservice;

	@Reference
	private EventService eventService;
	
	private ResourceBundle bundle;

	final int NUMBEROFRESULTSPERPAGE = 10;
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/rave2";
    static final String DB_URL_CS = "jdbc:mysql://localhost:3306/customerservice";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "password";

	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		try {
			if (request.getRequestPathInfo().getExtension().equals("person")) {
				Node groupNode = null;
				Session session;

				String from = null;
				String to = null;
				int t, f;
				from = request.getParameter("from");
				to = from;

				if (to != null && from != null) {
					try {
						t = Integer.parseInt(to);
						f = Integer.parseInt(from);
						f = (f - 1) * NUMBEROFRESULTSPERPAGE;
						t = t * NUMBEROFRESULTSPERPAGE;
						Node tempCmpNode = null;
						ArrayList<Node> m = new ArrayList<Node>();
						session = repos.login(new SimpleCredentials("admin",
								"admin".toCharArray()));
						groupNode = session.getRootNode().getNode("content")
								.getNode("user");
						if (groupNode.hasNodes()) {
							NodeIterator iterator = groupNode.getNodes();
							while (iterator.hasNext()) {
								tempCmpNode = iterator.nextNode();
								m.add(tempCmpNode);

							}
						}
						request.setAttribute("totalperson", m.size());
						ArrayList<Node> alist = new ArrayList<Node>();
						if (m.size() > t) {
							for (int i = f; i < t; i++) {
								alist.add(m.get(i));
							}
						} else {
							for (int i = f; i < m.size(); i++) {
								alist.add(m.get(i));
							}
						}
						request.setAttribute("person", alist);
						request.getRequestDispatcher(
								"/content/user/.userListDiv").forward(request,
								response);

					} catch (NumberFormatException e) {
						Node tempCmpNode = null;
						ArrayList<Node> m = new ArrayList<Node>();
						session = repos.login(new SimpleCredentials("admin",
								"admin".toCharArray()));
						groupNode = session.getRootNode().getNode("content")
								.getNode("user");
						if (groupNode.hasNodes()) {
							NodeIterator iterator = groupNode.getNodes();
							while (iterator.hasNext()) {
								tempCmpNode = iterator.nextNode();
								m.add(tempCmpNode);

							}
						}

						request.setAttribute("totalperson", m.size());
						ArrayList<Node> alist = new ArrayList<Node>();
						if (m.size() > NUMBEROFRESULTSPERPAGE) {
							for (int i = 0; i < NUMBEROFRESULTSPERPAGE; i++) {
								alist.add(m.get(i));

							}
						} else {
							for (int i = 0; i < m.size(); i++) {
								alist.add(m.get(i));

							}
						}

						request.setAttribute("person", alist);
						request.getRequestDispatcher(
								"/content/user/.userListDiv").forward(request,
								response);
					}

				} else {
					Node tempCmpNode = null;
					ArrayList<Node> m = new ArrayList<Node>();
					session = repos.login(new SimpleCredentials("admin",
							"admin".toCharArray()));
					groupNode = session.getRootNode().getNode("content")
							.getNode("user");
					if (groupNode.hasNodes()) {
						NodeIterator iterator = groupNode.getNodes();
						while (iterator.hasNext()) {
							tempCmpNode = iterator.nextNode();
							m.add(tempCmpNode);

						}
					}

					request.setAttribute("totalperson", m.size());
					ArrayList<Node> alist = new ArrayList<Node>();
					if (m.size() > NUMBEROFRESULTSPERPAGE) {
						for (int i = 0; i < NUMBEROFRESULTSPERPAGE; i++) {
							alist.add(m.get(i));

						}
					} else {
						for (int i = 0; i < m.size(); i++) {
							alist.add(m.get(i));

						}
					}
					request.setAttribute("person", alist);
					request.getRequestDispatcher(
							"/content/user/.searchUserList").forward(request,
							response);
				}

			} else if (request.getRequestPathInfo().getExtension()
					.equals("company")) {
				Node groupNode = null;
				Session session;

				String from = null;
				String to = null;
				int t, f;
				from = request.getParameter("from");
				to = from;

				if (to != null && from != null) {
					try {
						t = Integer.parseInt(to);
						f = Integer.parseInt(from);
						f = (f - 1) * NUMBEROFRESULTSPERPAGE;
						t = t * NUMBEROFRESULTSPERPAGE;
						Node tempCmpNode = null;
						ArrayList<Node> m = new ArrayList<Node>();
						session = repos.login(new SimpleCredentials("admin",
								"admin".toCharArray()));
						groupNode = session.getRootNode().getNode("content")
								.getNode("company");
						if (groupNode.hasNodes()) {
							NodeIterator iterator = groupNode.getNodes();
							while (iterator.hasNext()) {
								tempCmpNode = iterator.nextNode();
								m.add(tempCmpNode);

							}
						}
						request.setAttribute("total", m.size());
						ArrayList<Node> alist = new ArrayList<Node>();
						if (m.size() > t) {
							for (int i = f; i < t; i++) {
								alist.add(m.get(i));
							}
						} else {
							for (int i = f; i < m.size(); i++) {
								alist.add(m.get(i));
							}
						}
						request.setAttribute("companylist", alist);
						request.getRequestDispatcher(
								"/content/company/.companyListDiv").forward(
								request, response);
					} catch (NumberFormatException e) {
						Node tempCmpNode = null;
						ArrayList<Node> m = new ArrayList<Node>();
						session = repos.login(new SimpleCredentials("admin",
								"admin".toCharArray()));
						groupNode = session.getRootNode().getNode("content")
								.getNode("company");
						if (groupNode.hasNodes()) {
							NodeIterator iterator = groupNode.getNodes();
							while (iterator.hasNext()) {
								tempCmpNode = iterator.nextNode();
								m.add(tempCmpNode);

							}
						}

						request.setAttribute("total", m.size());
						ArrayList<Node> alist = new ArrayList<Node>();
						if (m.size() > NUMBEROFRESULTSPERPAGE) {
							for (int i = 0; i < NUMBEROFRESULTSPERPAGE; i++) {
								alist.add(m.get(i));

							}
						} else {
							for (int i = 0; i < m.size(); i++) {
								alist.add(m.get(i));

							}
						}

						request.setAttribute("companylist", alist);
						request.getRequestDispatcher(
								"/content/company/.companyListDiv").forward(
								request, response);
					}

				} else {
					Node tempCmpNode = null;
					ArrayList<Node> m = new ArrayList<Node>();
					session = repos.login(new SimpleCredentials("admin",
							"admin".toCharArray()));
					groupNode = session.getRootNode().getNode("content")
							.getNode("company");
					if (groupNode.hasNodes()) {
						NodeIterator iterator = groupNode.getNodes();
						while (iterator.hasNext()) {
							tempCmpNode = iterator.nextNode();
							m.add(tempCmpNode);

						}
					}

					request.setAttribute("total", m.size());
					ArrayList<Node> alist = new ArrayList<Node>();
					if (m.size() > NUMBEROFRESULTSPERPAGE) {
						for (int i = 0; i < NUMBEROFRESULTSPERPAGE; i++) {
							alist.add(m.get(i));

						}
					} else {
						for (int i = 0; i < m.size(); i++) {
							alist.add(m.get(i));

						}
					}
					request.setAttribute("companylist", alist);
					request.getRequestDispatcher(
							"/content/company/.companyList").forward(request,
							response);
				}

			} else if (request.getRequestPathInfo().getExtension()
					.equals("profileDiv")) {
				request.getRequestDispatcher("/content/user/*.searchDiv")
						.forward(request, response);
			} else if (request.getRequestPathInfo().getExtension()
					.equals("advProfileDiv")) {
				request.setAttribute(
						"advKey",
						service.advanceSearchProfile(
								request.getParameter("skillTag"),
								request.getParameter("skillValue"),
								request.getParameter("companyTag"),
								request.getParameter("companyValue"),
								request.getParameter("companyDur"),
								request.getParameter("addressTag"),
								request.getParameter("addressValue"),
								request.getParameter("educationTag"),
								request.getParameter("educationValue"),
								request.getParameter("educationDur"),
								request.getParameter("keyword")));

				request.getRequestDispatcher("/content/user/*.searchDiv")
						.forward(request, response);
			} else if (request.getRequestPathInfo().getExtension()
					.equals("companyDiv")) {
				request.getRequestDispatcher(
						"/content/company/*.companySearchDiv").forward(request,
						response);
			} else if (request.getRequestPathInfo().getExtension()
					.equals("group")) {
				// request.getRequestDispatcher("/content/group/*.searchGroup")
				// .forward(request, response);

				Node groupNode = null;
				Session session;

				String from = null;
				String to = null;
				int t, f;
				from = request.getParameter("from");
				to = from;

				if (to != null && from != null) {
					try {
						t = Integer.parseInt(to);
						f = Integer.parseInt(from);
						f = (f - 1) * NUMBEROFRESULTSPERPAGE;
						t = t * NUMBEROFRESULTSPERPAGE;
						Node tempCmpNode = null;
						ArrayList<Node> m = new ArrayList<Node>();
						session = repos.login(new SimpleCredentials("admin",
								"admin".toCharArray()));
						groupNode = session.getRootNode().getNode("content")
								.getNode("group");
						if (groupNode.hasNodes()) {
							NodeIterator iterator = groupNode.getNodes();
							while (iterator.hasNext()) {
								tempCmpNode = iterator.nextNode();
								m.add(tempCmpNode);

							}
						}
						request.setAttribute("total", m.size());
						ArrayList<Node> alist = new ArrayList<Node>();
						if (m.size() > t) {
							for (int i = f; i < t; i++) {
								alist.add(m.get(i));
							}
						} else {
							for (int i = f; i < m.size(); i++) {
								alist.add(m.get(i));
							}
						}
						request.setAttribute("grouplist", alist);
						request.getRequestDispatcher(
								"/content/group/.groupListDiv").forward(
								request, response);
					} catch (NumberFormatException e) {
						Node tempCmpNode = null;
						ArrayList<Node> m = new ArrayList<Node>();
						session = repos.login(new SimpleCredentials("admin",
								"admin".toCharArray()));
						groupNode = session.getRootNode().getNode("content")
								.getNode("group");
						if (groupNode.hasNodes()) {
							NodeIterator iterator = groupNode.getNodes();
							while (iterator.hasNext()) {
								tempCmpNode = iterator.nextNode();
								m.add(tempCmpNode);

							}
						}

						request.setAttribute("total", m.size());
						ArrayList<Node> alist = new ArrayList<Node>();
						if (m.size() > NUMBEROFRESULTSPERPAGE) {
							for (int i = 0; i < NUMBEROFRESULTSPERPAGE; i++) {
								alist.add(m.get(i));

							}
						} else {
							for (int i = 0; i < m.size(); i++) {
								alist.add(m.get(i));

							}
						}

						request.setAttribute("grouplist", alist);
						request.getRequestDispatcher(
								"/content/group/.groupListDiv").forward(
								request, response);
					}

				} else {
					Node tempCmpNode = null;
					ArrayList<Node> m = new ArrayList<Node>();
					session = repos.login(new SimpleCredentials("admin",
							"admin".toCharArray()));
					groupNode = session.getRootNode().getNode("content")
							.getNode("group");
					if (groupNode.hasNodes()) {
						NodeIterator iterator = groupNode.getNodes();
						while (iterator.hasNext()) {
							tempCmpNode = iterator.nextNode();
							m.add(tempCmpNode);

						}
					}

					request.setAttribute("total", m.size());
					ArrayList<Node> alist = new ArrayList<Node>();
					if (m.size() > NUMBEROFRESULTSPERPAGE) {
						for (int i = 0; i < NUMBEROFRESULTSPERPAGE; i++) {
							alist.add(m.get(i));

						}
					} else {
						for (int i = 0; i < m.size(); i++) {
							alist.add(m.get(i));

						}
					}
					request.setAttribute("grouplist", alist);
					request.getRequestDispatcher("/content/group/.groupList")
							.forward(request, response);
				}

			} else if (request.getRequestPathInfo().getExtension()
					.equals("groupDiv")) {
				request.getRequestDispatcher("/content/group/*.searchGroupDiv")
						.forward(request, response);

			} else if (request.getRequestPathInfo().getExtension()
					.equals("suggestion")) {
				response.getOutputStream()
						.print(service.friendSuggestion(
								request.getParameter("userId")).toString());
			} else if (request.getRequestPathInfo().getExtension()
					.equals("call")) {
				if (request.getParameter("param").equals("true")) {
					bundle = ResourceBundle.getBundle("server");
					String url = bundle.getString("rave.portal")
							+ "/portal/read/singleSession/"
							+ request.getParameter("userId") + ".html";
					String[] paramName = { "" };
					String[] paramValue = { "" };
					String userCheck = "offline";
					userCheck = security_service.callService(url, paramName,
							paramValue);
					request.setAttribute("userCheck", userCheck);
				}
				request.getRequestDispatcher(
						"/content/user/" + request.getParameter("userId")
								+ ".communicate").forward(request, response);

			} else if (request.getRequestPathInfo().getExtension()
					.equals("user")) {
				String userName = request.getParameter("userId").replace("@",
						"_");
//				response.getOutputStream().print("username :: "+userName);
				Node userNode = null;
				Session session;
				session = repos.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));
				userNode = session.getRootNode().getNode("content")
						.getNode("user");
				if (userNode.hasNode(userName)) {
					// return "true";
					response.getOutputStream().print("true");
				} else {
					// return "false";
					response.getOutputStream().print("false");
				}
				// request.getRequestDispatcher("/content/user/*.searchDiv").forward(
				// request, response);
			} else if (request.getRequestPathInfo().getExtension()
					.equals("checkuserservice")) {
				String userName = request.getParameter("userId").replace("@",
						"_");
				String prodCode = request.getParameter("productCode");
				char res = service.getServiceCheck(userName, prodCode);
				// response.getOutputStream().print(res);
				char yes = 'Y';
				char no = 'N';
				if (res == yes) {
					response.getOutputStream().print("false");
				} else if (res == no) {
					response.getOutputStream().print("true");
				}
				// request.getRequestDispatcher("/content/user/*.searchDiv").forward(
				// request, response);
			} else if (request.getRequestPathInfo().getExtension()
					.equals("dialingcode")) {
				try {

					String code = request.getParameter("code");
					Node currNode = null;
					Session session;

					session = repos.login(new SimpleCredentials("admin",
							"admin".toCharArray()));

					if (session.getRootNode().getNode("content")
							.hasNode("countrymaster")) {
						currNode = session.getRootNode().getNode("content")
								.getNode("countrymaster");
					}

					PrintWriter out = response.getWriter();
					if (currNode.hasNodes()) {
						out.print("<select class='form-control' id='country' name='country' style='height:35px;cursor: pointer;'>");
						NodeIterator nodeIterator = currNode.getNodes();
						while (nodeIterator.hasNext()) {
							Node removeNode = nodeIterator.nextNode();
							if (removeNode.getName().equals(code)) {

								out.print("<option selected value='"
										+ removeNode.getProperty("dialingcode")
												.getString()
										+ "'>"
										+ removeNode.getProperty("country")
												.getString() + "</option>");
							} else {
								out.print("<option value='"
										+ removeNode.getProperty("dialingcode")
												.getString()
										+ "'>"
										+ removeNode.getProperty("country")
												.getString() + "</option>");
							}
						}

						out.print("</select>");
						// out.print("</select><input style='height:25px' type='button' value='Get Selected Value' onclick='getCategoryValue()'>");
					} else {
						out.print("<b>No data found !</b>");
					}
				} catch (Exception e) {
					PrintWriter out = response.getWriter();
					out.print(e.getMessage());
				}
			} else if (request.getRequestPathInfo().getExtension()
					.equals("makenode")) {
				PrintWriter out = response.getWriter();
				Node contentNode = null, pathNode = null;
				String masterProperty = "";
				int imstProp = 0;
				try {
					Session session = repos.login(new SimpleCredentials(
							"admin", "admin".toCharArray()));
					String str = request.getParameter("path");
					contentNode = session.getRootNode().getNode("content");
					if (contentNode.hasNode(str)) {
						pathNode = contentNode.getNode(str);
					} else {
						pathNode = contentNode.addNode(str);
					}
					if (pathNode.hasProperty(str + "Count")) {
						masterProperty = pathNode.getProperty(str + "Count")
								.getString();
						imstProp = Integer.parseInt(masterProperty);
						String[] s = request.getParameter("value").split(",");
						String paramname = request.getParameter("paraname");
						// Node
						// r=session.getRootNode().getNode("content").getNode(str);
						for (int i = 0; i < s.length; i++) {
							Node p = pathNode.addNode(String.valueOf(imstProp));
							p.setProperty(paramname, s[i]);
							imstProp++;
						}
						pathNode.setProperty(str + "Count", imstProp);
					} else {

						String[] s = request.getParameter("value").split(",");
						String paramname = request.getParameter("paraname");
						int val = 0;
						// Node
						// r=session.getRootNode().getNode("content").getNode(str);
						for (int i = 0; i < s.length; i++) {
							val = i;
							Node p = pathNode.addNode(String.valueOf(i));
							p.setProperty(paramname, s[i]);
						}
						pathNode.setProperty(str + "Count", val + 1);
					}

					session.save();
					out.print("success");
				} catch (Exception e) {

					out.print("exception---" + e.getMessage());
				}
			} else if (request.getRequestPathInfo().getExtension()
					.equals("connectionedit")) {
				PrintWriter out = response.getWriter();
				Node contentNode = null, userNode = null, userFriendNode = null;
				String masterProperty = "";
				int imstProp = 0;
				try {
					String userId = request.getParameter("userId");
					String friend = request.getParameter("friend");
					String friendValue = request.getParameter("friendValue");
					Session session = repos.login(new SimpleCredentials(
							"admin", "admin".toCharArray()));

					contentNode = session.getRootNode().getNode("content")
							.getNode("user").getNode(userId);
					if (contentNode.hasNode("connection")
							&& contentNode.getNode("connection").hasNode(
									"friend")) {
						userNode = contentNode.getNode("connection").getNode(
								"friend");
						if (userNode.hasNode(friend)) {
							userFriendNode = userNode.getNode(friend);
							userFriendNode.setProperty("friendType",
									friendValue);
						}
					}
					session.save();
					out.print("success");
				} catch (Exception e) {

					out.print("exception---" + e.getMessage());
				}
			} else if (request.getRequestPathInfo().getExtension()
					.equals("connectionremove")) {
				PrintWriter out = response.getWriter();
				Node contentNode = null, userNode = null, userFriendNode = null;
				String masterProperty = "";
				int imstProp = 0;
				try {
					String userId = request.getParameter("userId");
					String friend = request.getParameter("friend");
					Session session = repos.login(new SimpleCredentials(
							"admin", "admin".toCharArray()));

					contentNode = session.getRootNode().getNode("content")
							.getNode("user").getNode(userId);
					if (contentNode.hasNode("connection")
							&& contentNode.getNode("connection").hasNode(
									"friend")) {
						userNode = contentNode.getNode("connection").getNode(
								"friend");
						if (userNode.hasNode(friend)) {
							userFriendNode = userNode.getNode(friend);
							// userFriendNode.setProperty("friendType",friendValue);
							userFriendNode.remove();
						}
					}
					session.save();
					out.print("success");
				} catch (Exception e) {

					out.print("exception---" + e.getMessage());
				}
			} else if (request.getRequestPathInfo().getExtension()
					.equals("resume")) {
				request.getRequestDispatcher("/content/user/.resumeiframe")
						.forward(request, response);
			}else if (request.getRequestPathInfo().getExtension().equals("profile")) {
	            request.getRequestDispatcher("/content/user/.search").forward(
	                    request, response);
	        } else if (request.getRequestPathInfo().getExtension()
					.equals("getquote")) {
				request.getRequestDispatcher("/content/user/.getQuoteNow")
						.forward(request, response);
			} else if (request.getRequestPathInfo().getExtension()
					.equals("fetchthroughurl")) {
				HashMap<String, Object> hmap = null;
				try {
					String url = request.getParameter("producturl");
					
					if (url != null) {
						if (url.indexOf("alibaba") != -1) {
							hmap = nservice.alibaba(url);
						} else if (url.indexOf("bizlem") != -1) {
							hmap = nservice.bzlem(url);
						} else if (url.indexOf("amazon") != -1) {
							hmap = nservice.amazon(url);
						} else if (url.indexOf("ebay") != -1) {
							hmap = nservice.ebay(url);
						}
					}
					if(hmap.get("title")==null){
						response.getWriter().print("Sorry ,please try later !");
					}else{
						ArrayList al=new ArrayList();
						al=(ArrayList)hmap.get("desc");
						String des="";
						for(int i=0;i<al.size();i++){
							des=des+al.get(i)+System.getProperty("line.separator");
						
						}
						ArrayList al1=new ArrayList();
						al1=(ArrayList)hmap.get("pic");
						String des1="";
						for(int j=0;j<al1.size();j++){
							des1=des1+al1.get(j).toString().replace("\"", "'");
						
						}
					response.getWriter().print("success888"+hmap.get("title")+"888"+des+"888"+ des1+"8888"+hmap.get("breadcum"));
					}
				} catch (Exception e) {
					
					response.getWriter().print("Sorry ,please try later !"+e.getMessage()+"inside error "+hmap.get("stage"));
				}
			} else if (request.getRequestPathInfo().getExtension()
					.equals("inrichme")) {
				String path = request.getParameter("path");
				String[] paramname = request.getParameter("propertyname")
						.split(",");
				String[] paramvalue = request.getParameter("propertyvalue")
						.split(",");

				String searchparamvalue = request
						.getParameter("searchTextvalue");
				String[] isparent = request.getParameter("isparent").split(",");
				String jsonString = exservice.eventNameSuggestion(
						searchparamvalue, paramname, isparent, path, request,
						response);
				JSONArray al = new JSONArray(jsonString);

				if (al.length() == 0) {

					Session session = repos.login(new SimpleCredentials(
							"admin", "admin".toCharArray()));
					Node rootNode = session.getRootNode();
					String[] nodes = path.split("/");
					for (int i = 1; i < nodes.length; i++) {
						if (nodes[i] != null) {
							if (rootNode.hasNode(nodes[i])) {
								rootNode = rootNode.getNode(nodes[i]);

							} else {
								rootNode = rootNode.addNode(nodes[i]);
								if (i == nodes.length - 1) {
									rootNode.setProperty("nodeCount",
											Long.valueOf("0"));
								}
							}
						}
					}
					// valid to make single node only
					Node newnode = rootNode.addNode(rootNode.getProperty(
							"nodeCount").getString());

					//
					for (int j = 0; j < paramname.length; j++) {

						if (paramname[j].equalsIgnoreCase("id")) {
							newnode.setProperty(paramname[j], rootNode
									.getProperty("nodeCount").getString());
						} else {
							newnode.setProperty(paramname[j], paramvalue[j]);
						}
					}
					rootNode.setProperty("nodeCount",
							rootNode.getProperty("nodeCount").getLong() + 1);
					session.save();
					if (request.getParameter("isExist") != null) {
						String jsonString1 = exservice.eventNameSuggestion(
								searchparamvalue, paramname, isparent, path,
								request, response);
						JSONArray al1 = new JSONArray(jsonString1);

						response.getWriter().print("success" + "~" + al1);
					} else {
						response.getWriter().print("success");
					}
				} else {
					if (request.getParameter("isExist") != null) {
						response.getWriter().print("exist" + "~" + al);
					} else {
						response.getWriter().print("exist");
					}
				}
			}else if (request.getRequestPathInfo().getExtension()
				     .equals("javamail")) {
				PrintWriter out = response.getWriter();
			    String to="tripathi.vivekk@gmail.com";//change accordingly  
			      final String user="admin";//change accordingly  
			      final String password="admin";//change accordingly  
			       
			      //1) get the session object  
			      
			      java.util.Properties properties = System.getProperties();  
			      properties.setProperty("mail.smtp.host", "localhost");  
			      properties.put("mail.smtp.auth", "true");  
			      
			      javax.mail.Session session = javax.mail.Session.getInstance(properties,  
			       new javax.mail.Authenticator() {  
			       protected PasswordAuthentication getPasswordAuthentication() {  
			       return new PasswordAuthentication(user,password);  
			       }  
			      });  
			         out.println("session==========="+session);
			      //2) compose message     
			      try{  
			        MimeMessage message = new MimeMessage(session);  
			        message.setFrom(new InternetAddress("admin@bizlem.com"));  
			        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
			        message.setSubject("Message Aleart");  
			        out.println("message==========="+message);
			        //3) create MimeBodyPart object and set your message text     
			        BodyPart messageBodyPart1 = new MimeBodyPart();  
			        messageBodyPart1.setText("This is message body");  
			        out.println("messageBodyPart1==========="+messageBodyPart1);
			        //4) create new MimeBodyPart object and set DataHandler object to this object      
//			        MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
//			      
//			        String filename = "/home/vil/rave Server/apache-rave-0.14-bin/webapps/portal/WEB-INF/classes/jdbc.properties";//change accordingly  
//			        DataSource source = new FileDataSource(filename);  
//			        messageBodyPart2.setDataHandler(new DataHandler(source));  
//			        messageBodyPart2.setFileName(filename);  
//			        out.println("messageBodyPart2==========="+messageBodyPart2);
			         
			        //5) create Multipart object and add MimeBodyPart objects to this object      
			        Multipart multipart = new MimeMultipart();  
			        multipart.addBodyPart(messageBodyPart1);  
			        //multipart.addBodyPart(messageBodyPart2);  
			      
			        //6) set the multiplart object to the message object  
			        message.setContent(multipart);  
			         
			        //7) send message  
			        Transport.send(message);  
			       
			       out.println("message sent....");  
			       }catch (MessagingException ex) {out.println("error sent...."+ex.getMessage()); }  
			     
			   }else if (request.getRequestPathInfo().getExtension().equals("recommendeduser")) {
					
					PrintWriter o=response.getWriter();
					String result = "0";
			    	Statement stmt = null;
			    	Node usersNode,userNode = null;
			    	JSONObject objMainJson = new JSONObject();
			    	try{
			    //		bundle = ResourceBundle.getBundle("serverConfig");
			    		//  Class.forName(JDBC_DRIVER);
							Connection con = (Connection) DriverManager.getConnection(
									DB_URL,
									USER,
									PASS);
					
					stmt = (Statement) con.createStatement();
					String query = "select * from person order by rand() limit 7" ;
					ResultSet rs = stmt.executeQuery(query);
					
					JSONArray objArray = new JSONArray();
					JSONObject obj = null;
					Session session = repos.login(new SimpleCredentials(
							"admin", "admin".toCharArray()));

					usersNode = session.getRootNode().getNode("content")
							.getNode("user");
					String emailId = "";
					while(rs.next()){
						//result = String.valueOf(rs.getInt("serviceId"));
						if(objArray.length() < 5){
						obj = new JSONObject();
						emailId = rs.getString("email").replace("@", "_");
						if(usersNode.hasNode(emailId)){
							obj.put("emailid", emailId);
							userNode = usersNode.getNode(emailId);
							if(userNode.hasProperty("name")){
								obj.put("firstname",userNode.getProperty("name").getString());
							}else{
								obj.put("firstname","");
							}
							if(userNode.hasProperty("lastName")){
								obj.put("lastname",userNode.getProperty("lastName").getString());
							}else{
								obj.put("lastname","");
							}
							if(userNode.hasProperty("profileImage")){
								obj.put("image",userNode.getProperty("profileImage").getString());
							}else{
								obj.put("image","");
							}
							if(userNode.hasProperty("country")){
								obj.put("country",userNode.getProperty("country").getString());
							}else{
								obj.put("country","");
							}
							if(userNode.hasProperty("city")){
								obj.put("city",userNode.getProperty("city").getString());
							}else{
								obj.put("city","");
							}
							objArray.put(obj);
						}
						}
						
					}
					objMainJson.put("recommendeduser", objArray);
					//response.getWriter().print(objMainJson.toString());
			    	}catch(Exception e){
			    		o.print(e.getMessage());
			    	}
			    	//return result;
					
					//o.print(result);
			    	response.getWriter().print(objMainJson.toString());
						// response.getOutputStream().println(request.getParameter("companyName"));

				}else if (request.getRequestPathInfo().getExtension().equals("rfqmypage")) {
					
					PrintWriter o=response.getWriter();
					String result = "0";
			    	Statement stmt = null;
			    	Node usersNode,userNode = null;
			    	JSONObject objMainJson = new JSONObject();
			    	try{
			    //		bundle = ResourceBundle.getBundle("serverConfig");
			    		//  Class.forName(JDBC_DRIVER);
							Connection con = (Connection) DriverManager.getConnection(
									DB_URL_CS,
									USER,
									PASS);
					
					stmt = (Statement) con.createStatement();
					String query = "select * from customer_service_status where customerId = '"+request.getRemoteUser() + "' and status = 'active' and service_end_date > now() and productCode = 'RFQ_Service'";
					ResultSet rs = stmt.executeQuery(query);
					if(rs.next()){
						result = String.valueOf(rs.getInt("serviceId"));
						// result = eventService.getUserPassword(request.getRemoteUser());
					}
					
					JSONArray objArray = new JSONArray();
					JSONObject obj = null;
					String search_key = request.getRemoteUser();
		   		     String currNode = "";
		   		     Session session;
		   		     Node compNodeee = null;

		   		     session = repos.login(new SimpleCredentials("admin", "admin"
		   		       .toCharArray()));

		   		     if (session.getRootNode().getNode("content")
		   		       .hasNode("company")) {
		   		      currNode = "/content/company/";
		   		   compNodeee= session.getRootNode().getNode("content")
	   		       .getNode("company");
		   		     }
					 HashMap getList = (HashMap) bizservice
				   		       .getCompanyByNode(currNode,
				   		         search_key, request, response);

				   		  //   PrintWriter out = response.getWriter();
				   		   
				   		     // Get a set of the entries
				   		      Set set = getList.entrySet();
				   		      // Get an iterator
				   		      Iterator i = set.iterator();
				   		    //  int iMain = 0;
				   		   // Display elements
				   		      if(result != "" && result != null){
				   		      if(!getList.isEmpty()){
				   		    	while(i.hasNext()) {
				   		    		obj = new JSONObject();
				      		         Map.Entry me = (Map.Entry)i.next();
				      		         String compNum = (String)me.getKey();
				      		         String compTitle = (String)me.getValue();
				      		       //if(iMain == 0){
				      		         if(compNodeee.hasNode(compNum)){
				      		        	int iNew = 0;
			      		        		int iWip = 0;
			      		        		int iResp = 0;
				      		        	Node compNode = compNodeee.getNode(compNum); 
				      		        	if(compNode.hasNode("Rfq") && compNode.getNode("Rfq").hasNodes()){
				      		        		NodeIterator ni = compNode.getNode("Rfq").getNodes();
				      		        		
				      		        		while(ni.hasNext()){
				      		        			Node tempPrdNode=ni.nextNode();
				      		  				//JSONObject obj=new JSONObject();
				      		  			    String status = tempPrdNode.getProperty("status").getString();
				      		  			    if(status.equals("new")){
				      		  			    iNew++;
				      		  			    }else if(status.equals("wip")){
				      		  			    iWip++;
				      		  			    }else if(status.equals("responded")){
				      		  			    iResp++;
				      		  			    }
				      		  				
				      		        		}
				      		        		obj.put("newcount",iNew);
				      		        		obj.put("wipcount",iWip);
				      		        		obj.put("respcount",iResp);
				      		        		
				      		        	}else{
				      		        		obj.put("newcount",iNew);
				      		        		obj.put("wipcount",iWip);
				      		        		obj.put("respcount",iResp); 
					      		         }
				      		        	//iMain++;
				      		         }
				   		    	//}
				      		       obj.put("compId",compNum);
		      		        		obj.put("compTitle",compTitle);
				      		       objArray.put(obj);  
				   		    	}
				   		      }
					objMainJson.put("rfqdetail", objArray);
					objMainJson.put("serviceId", result);
					response.getWriter().print(objMainJson.toString());
				    }else{
				    	response.getWriter().print("false");
				    }
					//response.getWriter().print(objMainJson.toString());
			    	}catch(Exception e){
			    		o.print(e.getMessage());
			    	}
			    	//return result;
					
					//o.print(result);
			    	//response.getWriter().print(objMainJson.toString());
						// response.getOutputStream().println(request.getParameter("companyName"));

				}else if (request.getRequestPathInfo().getExtension().equals("getMessageCount")) {
					Session session = null;
					Node tempPrdNode, childNode;
					int count = 0;
					String pass = "";
					String querryStr = "";
						try {
							session = repos.login(new SimpleCredentials("admin", "admin"
									.toCharArray()));

							 querryStr = "select * from [nt:base] where (contains('isread','No'))  " +
									"and from <> '"+request.getRemoteUser()+"' and ISDESCENDANTNODE('/content/user/"+request.getRemoteUser().replace("@","_")+"/messages')";

							Workspace workspace = session.getWorkspace();
							Query query = workspace.getQueryManager().createQuery(
									querryStr, Query.JCR_SQL2);
							QueryResult result = query.execute();
							NodeIterator iterator = result.getNodes();
							
							while (iterator.hasNext()) {
								iterator.nextNode();
							count++;
							}
							 pass = eventService.getUserPassword(request.getRemoteUser());
						}catch (Exception e) {
							// TODO Auto-generated catch block
							response.getWriter().print(e.getMessage());
						}
					
					response.getWriter().print(count+"~"+pass);
				}

		} catch (Exception e) {
			response.getWriter().print(e.getMessage());
		}
	}

	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		try {
			if (request.getRequestPathInfo().getExtension()
					.equals("checkStatus")) {
				bundle = ResourceBundle.getBundle("server");
				String url = bundle.getString("rave.portal")
						+ "/portal/read/singleSession/"
						+ request.getParameter("userId") + ".html";
				String[] paramName = { "" };
				String[] paramValue = { "" };
				String userCheck = "offline";
				userCheck = security_service.callService(url, paramName,
						paramValue);
				response.getOutputStream().print(userCheck);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
