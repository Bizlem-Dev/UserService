package org.profile.service.impl;

import java.beans.Statement;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.EventServiceUser;
import org.profile.service.UserMailService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Component(configurationFactory = true)
@Service(EventServiceUser.class)
@Properties({ @Property(name = "userEventService", value = "eventService") })
public class EventServiceUserImpl implements EventServiceUser {

	@Reference
	private SlingRepository repo;

	private ResourceBundle bundle;
	Session session = null;
	
	private String ISSUE = "NSA";
	public List getCustomerDetails(SlingHttpServletRequest request,String customerId, PrintWriter out) {
		bundle = ResourceBundle.getBundle("serverConfig");
		InputStream inputXml = null;
		String content = "";
		String serviceUrl="";
		String url="";
		 Map<String, String> map=null;
		List customerDeatilList=null;
		 String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		 String bannerService = "Home_Left,Home_Right,Home_Top,Home_bottom";
		 String bannerService = bundle.getString("bannerServices");
		    String[] bannerListArray = bannerService.split(",");
		    ArrayList<String> bannerList = new ArrayList<String>(
		            Arrays.asList(bannerListArray));
		    Date endDate = new Date();
			endDate.setDate(endDate.getDate()-1);
			Date startDate = new Date(); 
			String url1="";
			
		try {
			session=repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if(session.getNode("/content/ip").hasProperty("auctionService")){
				serviceUrl=session.getNode("/content/ip").getProperty("auctionService").getString();
			 }
		   url=serviceUrl+"/services/Auctions_WSDL/getCustomerServiceStatus?customerId="+customerId;
			customerDeatilList=new ArrayList();
//			out.println(url);
			inputXml = new URL(url).openConnection().getInputStream();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputXml);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("ns:return");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				url1="";
				org.w3c.dom.Node nNode = nList.item(temp);
				if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					map=new HashMap<String, String>();
					Element eElement = (Element) nNode;
					//map.put("orderId", eElement.getElementsByTagName("ax21:orderId").item(0).getFirstChild().getNodeValue());
					map.put("productCode", eElement.getElementsByTagName("ax21:productCode").item(0).getFirstChild().getNodeValue());
					//map.put("configId", eElement.getElementsByTagName("ax21:configId").item(0).getFirstChild().getNodeValue());
					map.put("consumptionQuantity", eElement.getElementsByTagName("ax21:consumptionQuantity").item(0).getFirstChild().getNodeValue());
//					map.put("customerId", eElement.getElementsByTagName("ax21:customerId").item(0).getFirstChild().getNodeValue());
//					map.put("customerName", eElement.getElementsByTagName("ax21:customerName").item(0).getFirstChild().getNodeValue());
					map.put("lastConsumptionDate", eElement.getElementsByTagName("ax21:lastConsumptionDate").item(0).getFirstChild().getNodeValue());
					//map.put("productDescription", eElement.getElementsByTagName("ax21:productDescription").item(0).getFirstChild().getNodeValue());
					map.put("productDescription", eElement.getElementsByTagName("ax21:productCode").item(0).getFirstChild().getNodeValue());
					map.put("quantity", eElement.getElementsByTagName("ax21:quantity").item(0).getFirstChild().getNodeValue());
//					map.put("rfq", eElement.getElementsByTagName("ax21:rfq").item(0).getFirstChild().getNodeValue());
					map.put("serviceEndDate", eElement.getElementsByTagName("ax21:serviceEndDate").item(0).getFirstChild().getNodeValue());
					map.put("serviceStartDate", eElement.getElementsByTagName("ax21:serviceStartDate").item(0).getFirstChild().getNodeValue());
					map.put("status", eElement.getElementsByTagName("ax21:status").item(0).getFirstChild().getNodeValue());
					map.put("serviceId", eElement.getElementsByTagName("ax21:serviceId").item(0).getFirstChild().getNodeValue());
					
					String productCode=eElement.getElementsByTagName("ax21:productCode").item(0).getFirstChild().getNodeValue();
					
//					if(eElement.hasAttribute("ax21:orderId")){
//					    map.put("orderId", eElement.getElementsByTagName("ax21:orderId").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("orderId", "");
//					}
//					if(eElement.hasAttribute("ax21:productCode")){
//					map.put("productCode", eElement.getElementsByTagName("ax21:productCode").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("productCode", "");
//					}
//					if(eElement.hasAttribute("ax21:configId")){
//					map.put("configId", eElement.getElementsByTagName("ax21:configId").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("configId", "");
//					}
//					if(eElement.hasAttribute("ax21:consumptionQuantity")){
//					map.put("consumptionQuantity", eElement.getElementsByTagName("ax21:consumptionQuantity").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("consumptionQuantity", "");
//					}
//					if(eElement.hasAttribute("ax21:customerId")){
//					map.put("customerId", eElement.getElementsByTagName("ax21:customerId").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("customerId", "");
//					}
//					if(eElement.hasAttribute("ax21:customerName")){
//					map.put("customerName", eElement.getElementsByTagName("ax21:customerName").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("customerName", "");
//					}
//					if(eElement.hasAttribute("ax21:lastConsumptionDate")){
//					map.put("lastConsumptionDate", eElement.getElementsByTagName("ax21:lastConsumptionDate").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("lastConsumptionDate", "");
//					}
//					if(eElement.hasAttribute("ax21:productDescription")){
//					map.put("productDescription", eElement.getElementsByTagName("ax21:productDescription").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("productDescription", "");
//					}
//					if(eElement.hasAttribute("ax21:quantity")){
//					map.put("quantity", eElement.getElementsByTagName("ax21:quantity").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("quantity", "");
//					}
//					if(eElement.hasAttribute("ax21:rfq")){
//					map.put("rfq", eElement.getElementsByTagName("ax21:rfq").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("rfq", "");
//					}
//					if(eElement.hasAttribute("ax21:serviceEndDate")){
//					map.put("serviceEndDate", eElement.getElementsByTagName("ax21:serviceEndDate").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("serviceEndDate", "");
//					}
//					if(eElement.hasAttribute("ax21:serviceStartDate")){
//					map.put("serviceStartDate", eElement.getElementsByTagName("ax21:serviceStartDate").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("serviceStartDate", "");
//					}
//					if(eElement.hasAttribute("ax21:status")){
//					map.put("status", eElement.getElementsByTagName("ax21:status").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("status", "");
//					}
//					if(eElement.hasAttribute("ax21:serviceId")){
//					map.put("serviceId", eElement.getElementsByTagName("ax21:serviceId").item(0).getFirstChild().getNodeValue());
//					}else{
//						map.put("serviceId", "");
//					}
					
//						String sdate=eElement.getElementsByTagName("ax21:serviceStartDate").item(0).getFirstChild().getNodeValue();
//						String edate=eElement.getElementsByTagName("ax21:serviceEndDate").item(0).getFirstChild().getNodeValue();
					
					//if(startDate.toString().compareTo(sdate) > 0 &&  (endDate.toString().compareTo(edate) < 0)){  
					   //   String endServiceDate = new SimpleDateFormat("yyyy-MM-dd").format(edate);
						  
						String protocol = bundle.getString("services");
				        String a[] = protocol.split(",");
				        List<String> service = new ArrayList<String>();
				        for (String s : a) {
				            service.add(s);
				        }
//				        out.println(service.size());
				        double quantity=Double.valueOf(eElement.getElementsByTagName("ax21:quantity").item(0).getFirstChild().getNodeValue().toString());
				        long longquantity=(long)quantity;
						
					if (productCode.equalsIgnoreCase(service.get(0)) || productCode.equalsIgnoreCase(service.get(1))) {
						url1 = bundle.getString("chatnvchat") + "&val=" + longquantity + "&productCode=" + productCode
								+ "&serviceId=" + eElement.getElementsByTagName("ax21:serviceId").item(0)
										.getFirstChild().getNodeValue().toString();

					} else if (productCode.equalsIgnoreCase(service.get(13))) {

						url1 = bundle.getString("PaidAdd") + "?startdate=" + currentDate + "&enddate="
								+ eElement.getElementsByTagName("ax21:serviceEndDate").item(0).getFirstChild()
										.getNodeValue()
								+ "&username=" + customerId + "&serviceId="
								+ eElement.getElementsByTagName("ax21:serviceId").item(0).getFirstChild().getNodeValue()
										.toString();

					} else if (productCode.equalsIgnoreCase(service.get(14))) {
						url1 = bundle.getString("SponsoredAdd") + "?startdate=" + currentDate + "&enddate="
								+ eElement.getElementsByTagName("ax21:serviceEndDate").item(0).getFirstChild()
										.getNodeValue()
								+ "&username=" + customerId + "&serviceId="
								+ eElement.getElementsByTagName("ax21:serviceId").item(0).getFirstChild().getNodeValue()
										.toString();

					} else if (bannerList.contains(productCode)) {
						url1 = bundle.getString("bannerAdd") + "?startdate=" + currentDate + "&enddate="
								+ eElement.getElementsByTagName("ax21:serviceEndDate").item(0).getFirstChild()
										.getNodeValue()
								+ "&username=" + customerId + "&zonename=" + productCode + "&serviceId="
								+ eElement.getElementsByTagName("ax21:serviceId").item(0).getFirstChild().getNodeValue()
										.toString();

					} else if (productCode.equalsIgnoreCase(service.get(18))) {

						url1 = bundle.getString("webmail") + "&val=" + longquantity + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(2))
							|| productCode.equalsIgnoreCase(service.get(16))
							|| productCode.equalsIgnoreCase(service.get(11))) {
						url1 = bundle.getString("emailnsmsncall") + "&productCode=" + productCode;
					} else if (productCode.equalsIgnoreCase(service.get(3))) {

						url1 = bundle.getString("dcamp") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(4))) {

						url1 = bundle.getString("dlime") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(5))) {

						url1 = bundle.getString("dADHome") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(6))) {

						url1 = bundle.getString("dWebConf") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(7))) {

						url1 = bundle.getString("dChatLog") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(8))) {

						url1 = bundle.getString("d360_Deg") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(9))) {

						url1 = bundle.getString("dOnlineExam") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(10))) {

						url1 = bundle.getString("dcs") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(12))) {

						url1 = bundle.getString("dMailPlatform") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(15))) {

						url1 = bundle.getString("dcallMB") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(17))) {

						url1 = bundle.getString("dhelpdesk") + "&productCode=" + productCode;

					} else if (productCode.equalsIgnoreCase(service.get(19))) {
						url1 = bundle.getString("mdmservice") + "&val=" + longquantity + "&productCode=" + productCode
								+ "&serviceId=" + eElement.getElementsByTagName("ax21:serviceId").item(0)
										.getFirstChild().getNodeValue().toString();

					} else if (productCode.equalsIgnoreCase(service.get(20))) {
						url1 = bundle.getString("livechat") + "&val=" + longquantity + "&productCode=" + productCode
								+ "&serviceId=" + eElement.getElementsByTagName("ax21:serviceId").item(0)
										.getFirstChild().getNodeValue().toString();

					} else if (productCode.equalsIgnoreCase(service.get(21))) {
						url1 = bundle.getString("rfqservice") + "?serviceId="
								+ eElement.getElementsByTagName("ax21:serviceId").item(0).getFirstChild().getNodeValue()
										.toString();

					}else if (productCode.toLowerCase().indexOf(service.get(22)) != -1) {
						url1 = bundle.getString("provisioning_d") + "&val=" + longquantity + "&productCode=" + productCode
								+ "&serviceId=" + eElement.getElementsByTagName("ax21:serviceId").item(0)
								.getFirstChild().getNodeValue().toString();
					}
																			     
					   ///}
					map.put("url", url1);
					customerDeatilList.add(map);
				}

			}
			
		} catch (Exception e) {
			out.println(e.getMessage().toString());
			
		}finally{
//			session.logout();
			
		}
		return customerDeatilList;
	     
}
	public ArrayList<Node> getEvent(String searchKey) {
		ArrayList<Node> pb = null;

		Node tempPrdNode, childNode;

		if (!searchKey.trim().equals("")) {
			try {

				pb = new ArrayList<Node>();
				session = repo.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));

				String querryStr = "select * from [nt:unstructured] where  ISDESCENDANTNODE('/content/exhibition/') and selected_category like '"
						+ searchKey + "%'";
				Workspace workspace = session.getWorkspace();
				Query query = workspace.getQueryManager().createQuery(
						querryStr, Query.JCR_SQL2);
				QueryResult result = query.execute();
				NodeIterator iterator = result.getNodes();

				while (iterator.hasNext()) {

					tempPrdNode = iterator.nextNode();

					pb.add(tempPrdNode);

				}

			} catch (LoginException e) {

			} catch (RepositoryException e) {
				// TODO Auto-generated catch block

			} catch (Exception e) {
				// TODO Auto-generated catch block

			}
		}
		return pb;
	}

	public ArrayList<Node> getDefaultEvent() {

		ArrayList<Node> pb = null;
		Node tempPrdNode, childNode;

		try {

			pb = new ArrayList<Node>();
			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			String querryStr = "select * from [nt:unstructured] where  ISDESCENDANTNODE('/content/news/') and newsType  like";
			Workspace workspace = session.getWorkspace();
			Query query = workspace.getQueryManager().createQuery(querryStr,
					Query.JCR_SQL2);
			QueryResult result = query.execute();
			NodeIterator iterator = result.getNodes();

			while (iterator.hasNext()) {

				tempPrdNode = iterator.nextNode();

				pb.add(tempPrdNode);

			}

		} catch (LoginException e) {

		} catch (RepositoryException e) {
			// TODO Auto-generated catch block

		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

		return pb;
	}

	public String callService(String urlStr, String[] paramName,
			String[] paramValue) {

		StringBuilder response = new StringBuilder();
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			// Create the form content
			OutputStream out = conn.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");
			for (int i = 0; i < paramName.length; i++) {
				writer.write(paramName[i]);
				writer.write("=");
				writer.write(URLEncoder.encode(paramValue[i], "UTF-8"));
				writer.write("&");
			}
			writer.close();
			out.close();
			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}
			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();

			conn.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.append(e.getMessage());

		}
		return response.toString();
	}

	 public String sendToken(String values[]) {
		  String result = "";
		  String url = "";
		String email;
		Node node,  contactNode, mobileNode = null;
		NodeIterator emailNodes = null;
		Session session;

		  try {
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   DateFormat dateFormat2 = new SimpleDateFormat("HH-mm-ss");
		   Date date = new Date();
		   String message = "";
		   String token = "This is mail for testiomg contact supplier";
		   
		   session = repo.login(new SimpleCredentials("admin", "admin"
		     .toCharArray()));
		   if (session.getRootNode().getNode("content/company")
		     .hasNode(values[4])) {
		    Node supplierNode=session.getRootNode().getNode("content/company").getNode(values[4]);
		    if(supplierNode.hasNode("BasicInfo") && supplierNode.getNode("BasicInfo").hasProperty("creatorEmailId")){
		      email=supplierNode.getNode("BasicInfo").getProperty("creatorEmailId").getString();
		    }else{
		      email="";
		    }
		    bundle = ResourceBundle.getBundle("server");
		    /*String response = callGetService(bundle
		      .getString("service.consumption")
		      + "&userId="
		      + email
		      + "&quantity=1&productCode=email");
		    boolean accessFlag = false;
		    JSONObject json;

		    json = new JSONObject(response);
		    accessFlag = json.getBoolean("accessFlag");*/
		    if (true) {

		     
		     //message = MessageFormat
		     //  .format(bundle.getString("sendMail.body"),
		      //   values[0], token);
		     
		     message = "<html><body><table width='100%' border='0' cellspacing='0' cellpadding='0' style='border-radius:0px '0px  10px 10px; border:1px solid #4096EE; background-color:#ffffff;'><tr><td align='center' valign='top'><table width='100%' border='0' align='center' cellpadding='5' cellspacing='5' style='border-top:6px solid #4096EE;'><tr><td align='left' valign='middle' style='font-family:Arial, Helvetica, sans-serif; color:#4e4e4e; font-size:13px; padding-right:10px;'><div style='font-size:24px; color:#4096EE;'>Dear "+email+", </div><p>You have recieved a new Buying Request for <strong>"+values[6]+"</strong></p><p>Quantity : "+values[5]+"</p><p>Description : "+values[3]+"</p><p>Sender Emailid : "+values[0]+"</p><p>product link : <a href='http://prod.bizlem.io:8082/portal/servlet/service/productselection.brief?pid="+values[7]+"' >http://prod.bizlem.io:8082/portal/servlet/service/productselection.brief?pid="+values[7]+"</a></p><tr><td align='left' valign='middle' style='font-family:Arial, Helvetica, sans-serif; color:#4e4e4e; font-size:13px;'></tr><tr><td align='left' valign='middle' style='font-family:Arial, Helvetica, sans-serif; color:#333; font-size:13px;'><span style='color:#333; font-size:12px; font-family:Arial, Helvetica, sans-serif;'>With Regards,<br /><strong>Administrator</strong></span></tr></table></td></tr></table></body></html>";
		     url = bundle.getString("sendMail.address");
		     String[] paramName = { "emailto[]", "emailfrom[]",
		       "emailsub[]", "emailmsg[]" };
		     String[] paramValue = { email,
		       bundle.getString("sendMail.from"),
		       "Enquiry Received", message };
		     result = callService(url, paramName, paramValue);
		    } else {
		     result = "SE";

		    }
		   }
		  } catch (Exception e) {
		   // e.printStackTrace();
		  }
		  return result;
		 }

	public String callGetService(String urlString) {

		URL url;
		HttpURLConnection urlConn;
		DataOutputStream printout;
		DataInputStream input;
		StringBuilder sb = new StringBuilder();
		try {
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			conn.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return sb.toString();
	}
	public String getChatUrl() {
		String p="bizlem.com:8078";
		try {
			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			p=session.getRootNode().getNode("content").getNode("ip").getProperty("chatUrl").getString();		
			
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p=e.getMessage();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p=e.getMessage();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		p=e.getMessage();
		}
		return p;
	}
	
	public String getSipmlUrl() {
		String p="";
		try {
			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			p=session.getRootNode().getNode("content").getNode("ip").getProperty("sipmlUrl").getString();		
			
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p=e.getMessage();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p=e.getMessage();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		p=e.getMessage();
		}
		return p;
	}
	
	public String getSipmlExtn(String userName) {
		String p="";
		try {
			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			p=session.getRootNode().getNode("content").getNode("user").getNode(userName).getProperty("extension").getString();		
			
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p=e.getMessage();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p=e.getMessage();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		p=e.getMessage();
		}
		return p;
	}
	
	public String getUserChatPassword(String uid) {
		Connection conn = null;  
		String password="";
		String entityId="";
		ResultSet rs=null;
		try {
			conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/rave2?" +
			        "user=root&password=password");
			String sql="select encrypt_password,entity_id from person where username='"+uid+"'";
			java.sql.Statement smt=conn.createStatement();
			rs=smt.executeQuery(sql);
		
			while(rs.next()){
			password=rs.getString("encrypt_password");
			entityId=rs.getString("entity_id");
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//password="fail"+e.getMessage();
		}
		return password+"-@#@-"+entityId;

	}
}