package org.profile.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.jcr.api.SlingRepository;
import org.gallery.service.GalleryService;
import org.profile.service.CompanyService;
import org.profile.service.SecurityService;
import org.profile.service.SocialNetworkService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

// TODO: Auto-generated Javadoc
/**
 * The Class <code>CompanyServiceImpl</code> contains the all the method
 * implementation related to company profile. It contains the functionality
 * like: Company profile Creation, company career creation and deletion, search
 * Company Profile, map user with company, map product with company,map company
 * with company, upload all kind of documents,video and images related to
 * company and get present Stock information related to BSE number.
 * 
 * @author pranav.arya
 */
@Component(configurationFactory = true)
@Service(CompanyService.class)
@Properties({ @Property(name = "companyService", value = "company") })
public class CompanyServiceImpl implements CompanyService {

	/** The repos. */
	@Reference
	private SlingRepository repos;

	/** The bundle. */
	private ResourceBundle bundle = ResourceBundle.getBundle("server");

	/** The gallery_service. */
	@Reference
	private GalleryService gallery_service;

	/** The social network service. */
	@Reference
	private SocialNetworkService socialNetworkService;
	final String VIDEOEXTENSION[] = { ".3g2", ".3gp", ".asf", ".asx", ".avi",
			".flv", ".mov", ".mp4", ".mpg", ".rm", ".swf", ".vob", ".wmv" };
	/** The security_service. */
	@Reference
	private SecurityService security_service;
	private static final int THRESHOLD_SIZE = 1024 * 1024 * 1; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 15; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 15; // 50MB

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#saveCompanyNode(org.apache.sling.api
	 * .SlingHttpServletRequest)
	 */
	@SuppressWarnings({ "unused" })
	public String saveCompanyNode(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Node node = null;
		String comNodeame = "";
		Node rootNode = null, vm,sc,scChild, mt, pr, pictureNode, fileNewNode, jcrNode, fileNode, basicNode, addressNode, branchNode, contactNode, contactTypeNode = null;
		Session session;
		String status = "false";
		Node taxNode = null;
		try {
			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			//

			if (!session.getRootNode().hasNode("content")) {

				session.getRootNode().addNode("content");

			}

			if (session.getRootNode().getNode("content").hasNode("company")) {
				rootNode = session.getRootNode().getNode("content")
						.getNode("company");

			} else {
				rootNode = session.getRootNode().getNode("content")
						.addNode("company");
				rootNode.setProperty("companyNumber", 0);
			}

			if (request.getParameter("companyNo").equals("new")) {
				comNodeame = Long.toString(rootNode
						.getProperty("companyNumber").getLong() + 1);

				node = rootNode.addNode(comNodeame);
				node.setProperty("companyNumber",
						rootNode.getProperty("companyNumber").getLong() + 1);
				rootNode.setProperty("companyNumber",
						rootNode.getProperty("companyNumber").getLong() + 1);

			} else {
				// comNodeame=Long.toString(rootNode.getProperty("companyNumber").getLong());
				node = rootNode.getNode(request.getParameter("companyNo"));
				comNodeame = request.getParameter("companyNo");
			}

			// if(node.hasNode("vissionmission")){
			// vm=node.getNode("vissionmission");
			// vm.setProperty("vision",request.getParameter("vision"));
			// vm.setProperty("mission",request.getParameter("mission"));
			// }else{
			// vm=node.addNode("vissionmission");
			// vm.setProperty("vision",request.getParameter("vision"));
			// vm.setProperty("mission",request.getParameter("mission"));
			// }

			if (node.hasNode("taxation")) {
				vm = node.getNode("taxation");
				vm.setProperty("turnOver", request.getParameter("turnOver"));
				vm.setProperty("BSENumber", request.getParameter("BSENumber"));
				vm.setProperty("founded", request.getParameter("founded"));
				vm.setProperty("stateSalesTax",
						request.getParameter("stateSalesTax"));
				vm.setProperty("centralSalesTax",
						request.getParameter("centralSalesTax"));
				vm.setProperty("panNumber", request.getParameter("panNumber"));
				vm.setProperty("foundedPlace",
						request.getParameter("foundedPlace"));
			} else {
				vm = node.addNode("taxation");
				vm.setProperty("turnOver", request.getParameter("turnOver"));
				vm.setProperty("BSENumber", request.getParameter("BSENumber"));
				vm.setProperty("founded", request.getParameter("founded"));
				vm.setProperty("stateSalesTax",
						request.getParameter("stateSalesTax"));
				vm.setProperty("centralSalesTax",
						request.getParameter("centralSalesTax"));
				vm.setProperty("panNumber", request.getParameter("panNumber"));
				vm.setProperty("foundedPlace",
						request.getParameter("foundedPlace"));
			}
			if (!request.getParameterValues("secdocurl")[0].equals("")) {
			if (node.hasNode("securedoc")) {
				sc = node.getNode("securedoc");
			} else {
				sc = node.addNode("securedoc");
			}
			String[] secFilename = request.getParameterValues("secdocfilename");
			String[] secUrl = request.getParameterValues("secdocurl");
			if(sc.hasNodes()){
			String[] secUrl1=new String[secUrl.length];
			for(int k = 0; k < secUrl.length; k++){
				if(secUrl[k].indexOf("#") != -1){
	                //System.out.println(secUrl[i].substring(s.lastIndexOf("/")+1, s.lastIndexOf("#")));
					secUrl1[k] = secUrl[k].substring(secUrl[k].lastIndexOf("/")+1, secUrl[k].lastIndexOf("#"));
	            }else{
	                //System.out.println(s.substring(s.lastIndexOf("/")+1, s.length()));
	            	secUrl1[k] = secUrl[k].substring(secUrl[k].lastIndexOf("/")+1, secUrl[k].length());
	            }
			}
			NodeIterator imNodes = sc.getNodes();
			List<String> imList = Arrays.asList(secUrl1);
			while (imNodes.hasNext()) {
				Node removeNode = imNodes.nextNode();
				if (!imList.contains(removeNode.getName())) {
					removeNode.remove();
				}
			}
			}
			for (int i = 0; i < secUrl.length; i++) {
				String subStringFile = "";
				if(secUrl[i].indexOf("#") != -1){
	                //System.out.println(secUrl[i].substring(s.lastIndexOf("/")+1, s.lastIndexOf("#")));
					subStringFile = secUrl[i].substring(secUrl[i].lastIndexOf("/")+1, secUrl[i].lastIndexOf("#"));
	            }else{
	                //System.out.println(s.substring(s.lastIndexOf("/")+1, s.length()));
	            	subStringFile = secUrl[i].substring(secUrl[i].lastIndexOf("/")+1, secUrl[i].length());
	            }
				if (sc.hasNode(subStringFile)) {
					scChild = sc.getNode(subStringFile);

				} else {
					scChild = sc.addNode(subStringFile);
				}

				scChild.setProperty("secfilename", secFilename[i]);
				scChild.setProperty("securl", secUrl[i]);
				
			}
			}
			
			if (!request.getParameterValues("empName")[0].equals("")) {
				String[] empName = request.getParameterValues("empName");
				String[] empEmail = request.getParameterValues("empEmail");
				String[] empDesignation = request
						.getParameterValues("empDesignation");
				String[] empDescription = request
						.getParameterValues("empDescription");
				String[] empAccess = request.getParameterValues("empAccess");
				// Node im = null;
				Node imNode = null;
				if (node.hasNode("managementteam")) {
					mt = node.getNode("managementteam");
					// mt.setProperty("managementteam",request.getParameter("management"));

				} else {

					mt = node.addNode("managementteam");
					// mt.setProperty("managementteam",request.getParameter("management"));

				}
				NodeIterator imNodes = mt.getNodes();
				List<String> imList = Arrays.asList(empEmail);
				while (imNodes.hasNext()) {
					Node removeNode = imNodes.nextNode();
					if (!imList.contains(removeNode.getName())) {
						removeNode.remove();
					}
				}
				for (int i = 0; i < empEmail.length; i++) {
					if (mt.hasNode(empEmail[i])) {
						imNode = mt.getNode(empEmail[i].replace("@", "_"));

					} else {
						imNode = mt.addNode(empEmail[i].replace("@", "_"));
					}

					imNode.setProperty("empname", empName[i]);
					imNode.setProperty("empemail", empEmail[i]);
					imNode.setProperty("empdesignation", empDesignation[i]);
					imNode.setProperty("empdescription", empDescription[i]);
					imNode.setProperty("empAccess", empAccess[i]);
					ResourceBundle bundle = ResourceBundle.getBundle("server");
					String serviceUrl = bundle.getString("userServiceUrl");
					String url = "";
					InputStream inputXml = null;
					url = serviceUrl
							+ "/services/UserValidation/raveUserExistence?userId="
							+ empEmail[i];
					// customerDeatilList = new ArrayList<HashMap<String,
					// String>>();
					inputXml = new URL(url).openConnection().getInputStream();

					DocumentBuilderFactory dbFactory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(inputXml);
					doc.getDocumentElement().normalize();
					// NodeList nList = doc.getElementsByTagName("ns:return");
					// String userresult = nList.toString();
					NodeList nList1 = doc
							.getElementsByTagName("ns:raveUserExistenceResponse");
					org.w3c.dom.Node nNode = nList1.item(0);
					Element eElement = (Element) nNode;
					// System.out.println(""+eElement.getElementsByTagName("ns:return").item(0).getTextContent());
					String userresult = eElement
							.getElementsByTagName("ns:return").item(0)
							.getFirstChild().getNodeValue();

				}
			}else{
				Node imNode = null;
				if (node.hasNode("managementteam")) {
					mt = node.getNode("managementteam");
					// mt.setProperty("managementteam",request.getParameter("management"));
					if(mt.hasNodes()){
					NodeIterator imNodes = mt.getNodes();
					//List<String> imList = Arrays.asList(empEmail);
					while (imNodes.hasNext()) {
						Node removeNode = imNodes.nextNode();
						//if (!imList.contains(removeNode.getName())) {
							removeNode.remove();
						//}
					}
					}
				
				}
				
			}
			
			

			/*
			 * if(node.hasNode("product")){ pr=node.getNode("product");
			 * 
			 * 
			 * }else{
			 * 
			 * pr=node.addNode("product");
			 * 
			 * }
			 */

			if (node.hasNode("BasicInfo")) {
				basicNode = node.getNode("BasicInfo");
			} else {
				basicNode = node.addNode("BasicInfo");
			}
			node.setProperty("title", request.getParameter("companyName")
					.replaceAll("\\s+", ""));
			basicNode.setProperty("title", request.getParameter("companyName")
					.replaceAll("\\s+", ""));
			basicNode.setProperty("companyName",
					request.getParameter("companyName"));
			/*if (!request.getParameter("agree").equals("")) {
				basicNode.setProperty("companytnc", "accept");
			} else {
				basicNode.setProperty("companytnc", "accept");
			}*/
			basicNode.setProperty("website", request.getParameter("website"));
			// basicNode.setProperty("reason", request.getParameter("website"));
			basicNode.setProperty("description",
					request.getParameter("description"));
			basicNode.setProperty("industries",
					request.getParameter("industries"));
			basicNode.setProperty("employeesNum",
					request.getParameter("employeesNum"));
			basicNode.setProperty("type", request.getParameter("type"));
			basicNode.setProperty("creatorEmailId", request.getRemoteUser());
			basicNode.setProperty("creatorjoiningDate",
					request.getParameter("creatorjoiningDate"));
			basicNode.setProperty("aboutcom", request.getParameter("aboutcom"));
			basicNode.setProperty("creatorTitle",
					request.getParameter("creatorTitle"));
			basicNode.setProperty("vision", request.getParameter("vision"));
			basicNode.setProperty("mission", request.getParameter("mission"));
			basicNode.setProperty("fburl", request.getParameter("fburl"));
			basicNode.setProperty("twurl", request.getParameter("twurl"));
			basicNode.setProperty("googleurl",
					request.getParameter("googleurl"));
			basicNode.setProperty("pinurl", request.getParameter("pinurl"));

			if (node.hasNode("Address")) {
				addressNode = node.getNode("Address");
			} else {
				addressNode = node.addNode("Address");
			}

			String[] branchName = request.getParameterValues("branchName");
			String[] branchAddress = request.getParameterValues("address");
			String[] branchCity = request.getParameterValues("city");
			String[] branchDistrict = request.getParameterValues("district");
			String[] branchState = request.getParameterValues("state");
			String[] branchCountry = request.getParameterValues("country");
			String[] branchPostalCode = request
					.getParameterValues("postalCode");
			String[] branchWorkNumber = request
					.getParameterValues("workNumber");
			String[] branchMobileNumber = request
					.getParameterValues("mobileNumber");
			String[] branchContactEmailId = request
					.getParameterValues("contactEmailId");
			String[] addressType = request.getParameterValues("addressType");
			for (int i = 0; i < branchName.length; i++) {
				if (branchName[i] != null && !branchName[i].equals("")) {
					if (addressNode.hasNode(branchName[i])) {
						branchNode = addressNode.getNode(branchName[i]);
					} else {
						branchNode = addressNode.addNode(branchName[i]);
					}
					branchNode.setProperty("branchName", branchName[i]);
					branchNode.setProperty("addressType", addressType[i]);
					branchNode.setProperty("branchAddress", branchAddress[i]);
					branchNode.setProperty("branchCity", branchCity[i]);
					branchNode.setProperty("branchDistrict", branchDistrict[i]);
					branchNode.setProperty("branchState", branchState[i]);
					branchNode.setProperty("branchCountry", branchCountry[i]);
					branchNode.setProperty("branchPostalCode",
							branchPostalCode[i]);
					branchNode.setProperty("branchWorkNumber",
							branchWorkNumber[i]);
					branchNode.setProperty("branchMobileNumber",
							branchMobileNumber[i]);
					branchNode.setProperty("branchContactEmailId",
							branchContactEmailId[i]);
				}
			}
			if (node.hasNode("PointOfContact")) {
				contactNode = node.getNode("PointOfContact");
			} else {
				contactNode = node.addNode("PointOfContact");
			}
			String[] contactTypeFor = request
					.getParameterValues("contactTypeCompany");
			String[] contactTypeName = request
					.getParameterValues("nameContactTypeCompany");
			String[] contactTypeContact = request
					.getParameterValues("contactContactTypeCompany");
			String[] contactTypeDetail = request
					.getParameterValues("detailContactTypeCompany");
			for (int i = 0; i < contactTypeFor.length; i++) {
				if (contactTypeFor[i] != null && !contactTypeFor[i].equals("")) {
					if (contactNode.hasNode(contactTypeFor[i])) {
						contactTypeNode = contactNode
								.getNode(contactTypeFor[i]);
					} else {
						contactTypeNode = contactNode
								.addNode(contactTypeFor[i]);
					}

					contactTypeNode.setProperty("contactTypeCompany",
							contactTypeFor[i]);
					contactTypeNode.setProperty("nameContactTypeCompany",
							contactTypeName[i]);
					contactTypeNode.setProperty("contactContactTypeCompany",
							contactTypeContact[i]);
					contactTypeNode.setProperty("detailContactTypeCompany",
							contactTypeDetail[i]);
				}
			}

			session.save();
			status = "true";

		} catch (PathNotFoundException e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		} catch (RepositoryException e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		} catch (Exception e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		}

		return status + "," + comNodeame;

	}

	public String saveCompanyNodeNew(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Node node = null;
		String comNodeame = "";
		Node rootNode, relationNode, companyNode, typeNode = null, vm, mt, pr, pictureNode, fileNewNode, jcrNode, fileNode, basicNode, addressNode, branchNode, contactNode, contactTypeNode = null;
		Session session;
		String status = "false";
		Node taxNode = null;
		String ident = "";
		try {
			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			//

			if (!session.getRootNode().hasNode("content")) {

				session.getRootNode().addNode("content");

			}

			if (session.getRootNode().getNode("content").hasNode("company")) {
				rootNode = session.getRootNode().getNode("content")
						.getNode("company");

			} else {
				rootNode = session.getRootNode().getNode("content")
						.addNode("company");
				rootNode.setProperty("companyNumber", 0);
			}

			if (request.getParameter("companyNo").equals("new")) {
				comNodeame = Long.toString(rootNode
						.getProperty("companyNumber").getLong() + 1);

				node = rootNode.addNode(comNodeame);
				node.setProperty("companyNumber",
						rootNode.getProperty("companyNumber").getLong() + 1);
				rootNode.setProperty("companyNumber",
						rootNode.getProperty("companyNumber").getLong() + 1);
				ident = node.getIdentifier();
				node.setProperty("token", ident);

			} else {
				// comNodeame=Long.toString(rootNode.getProperty("companyNumber").getLong());
				node = rootNode.getNode(request.getParameter("companyNo"));
				comNodeame = request.getParameter("companyNo");
			}

			// if(node.hasNode("vissionmission")){
			// vm=node.getNode("vissionmission");
			// vm.setProperty("vision",request.getParameter("vision"));
			// vm.setProperty("mission",request.getParameter("mission"));
			// }else{
			// vm=node.addNode("vissionmission");
			// vm.setProperty("vision",request.getParameter("vision"));
			// vm.setProperty("mission",request.getParameter("mission"));
			// }

			if (node.hasNode("CompRelation")) {
				relationNode = node.getNode("CompRelation");
			} else {
				relationNode = node.addNode("CompRelation");
			}
			if (relationNode.hasNode("Company")) {
				companyNode = relationNode.getNode("Company");
			} else {
				companyNode = relationNode.addNode("Company");
			}
			if (companyNode.hasNode(request.getParameter("mappingCompanyType"))) {
				typeNode = companyNode.getNode(request
						.getParameter("mappingCompanyType"));
			} else {
				typeNode = companyNode.addNode(request
						.getParameter("mappingCompanyType"));

				typeNode.setProperty("designation",
						request.getParameter("mappingDesignationType"));
				typeNode.setProperty("officialmailid",
						request.getParameter("offmailid"));
			}

			/*
			 * if(node.hasNode("product")){ pr=node.getNode("product");
			 * 
			 * 
			 * }else{
			 * 
			 * pr=node.addNode("product");
			 * 
			 * }
			 */

			if (node.hasNode("BasicInfo")) {
				basicNode = node.getNode("BasicInfo");
			} else {
				basicNode = node.addNode("BasicInfo");
			}
			node.setProperty("adminid", request.getParameter("adminmailid"));
			node.setProperty("title", request.getParameter("companyName")
					.replaceAll("\\s+", ""));
			basicNode.setProperty("title", request.getParameter("companyName")
					.replaceAll("\\s+", ""));
			basicNode.setProperty("companyName",
					request.getParameter("companyName"));
			basicNode.setProperty("offemailid",
					request.getParameter("emailidcompany"));
			if (!request.getParameter("agree").equals("")) {
				basicNode.setProperty("companytnc", "accept");
			} else {
				basicNode.setProperty("companytnc", "accept");
			}
			basicNode.setProperty("website", request.getParameter("website"));
			basicNode.setProperty("reason", request.getParameter("offReason"));
			basicNode.setProperty("industries",
					request.getParameter("industries"));
			basicNode.setProperty("creatorEmailId", request.getRemoteUser());

			session.save();
			status = "true";

		} catch (PathNotFoundException e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		} catch (RepositoryException e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		} catch (Exception e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		}

		return status + "," + comNodeame + "," + ident;

	}
	
	public String saveCompanyNodeMdmService(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Node node = null;
		String comNodeame = "";
		Node rootNode, relationNode, companyNode, typeNode = null, vm, mt, pr, pictureNode, fileNewNode, jcrNode, fileNode, basicNode, addressNode, branchNode, contactNode, contactTypeNode = null;
		Session session;
		String status = "false";
		Node taxNode = null;
		String ident = "";
		try {
			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			//

			if (!session.getRootNode().hasNode("content")) {

				session.getRootNode().addNode("content");

			}

			if (session.getRootNode().getNode("content").hasNode("company")) {
				rootNode = session.getRootNode().getNode("content")
						.getNode("company");

			} else {
				rootNode = session.getRootNode().getNode("content")
						.addNode("company");
				rootNode.setProperty("companyNumber", 0);
			}

				comNodeame = Long.toString(rootNode
						.getProperty("companyNumber").getLong() + 1);

				node = rootNode.addNode(comNodeame);
				node.setProperty("companyNumber",
						rootNode.getProperty("companyNumber").getLong() + 1);
				rootNode.setProperty("companyNumber",
						rootNode.getProperty("companyNumber").getLong() + 1);
				ident = node.getIdentifier();
				node.setProperty("token", ident);

			if (node.hasNode("BasicInfo")) {
				basicNode = node.getNode("BasicInfo");
			} else {
				basicNode = node.addNode("BasicInfo");
			}
			String companyName = request.getParameter("companyName");
			while(companyName.indexOf("+") != -1){
				companyName = companyName.replace("+", " ");
			}
			node.setProperty("adminid", request.getParameter("adminmailid"));
			node.setProperty("title", companyName
					.replaceAll("\\s+", ""));
			basicNode.setProperty("companyName",
					companyName);
			basicNode.setProperty("offemailid",
					request.getParameter("emailidcompany"));
			if (!request.getParameter("agree").equals("")) {
				basicNode.setProperty("companytnc", "accept");
			} else {
				basicNode.setProperty("companytnc", "accept");
			}
			basicNode.setProperty("website", request.getParameter("website"));
			basicNode.setProperty("reason", request.getParameter("offReason"));
			basicNode.setProperty("industries",
					request.getParameter("industries"));
			basicNode.setProperty("contactnumber",
					request.getParameter("contact"));
			basicNode.setProperty("creatorEmailId", request.getParameter("userId"));

			session.save();
			status = "true";

		} catch (PathNotFoundException e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		} catch (RepositoryException e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		} catch (Exception e) {
			// e.printStackTrace();
			status = "false" + e.getMessage();
		}

		return status + "," + comNodeame + "," + ident;

	}
	
	public String checkCompanyExists(String companyName){
		String querryStr="";
		Session session =null;
		Node tempPrdNode = null,childNode;
		JSONArray ar=new JSONArray();
		querryStr="select [companyName] from [nt:unstructured] where   contains('companyName','*"+companyName+"*') and ISDESCENDANTNODE('/content/company/')";
		JSONObject objMain = null;
		try {
			session=repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Workspace workspace = session.getWorkspace();		
			Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
			QueryResult result = query.execute();
			NodeIterator iterator=result.getNodes();
//			ar.put(" isParent size is "+isParent.length);
//			ar.put(" paramNameToSearch  size is "+paramNameToSearch.length);
			Node tempo,rootNode;
			 objMain=new JSONObject();
			while(iterator.hasNext()){
				tempPrdNode=iterator.nextNode();
				JSONObject obj=new JSONObject();
				tempo=tempPrdNode;
				//rootNode = session.getRootNode().getNode("content").getNode("company").getNode(tempo.getParent().getName());
				obj.put("companyPath", "/content/company/"+tempo.getParent().getName());
				ar.put(obj);
				}
			objMain.put("array",ar);
			
		} catch (PathNotFoundException e) {
			ar.put(e.getMessage()+"path ecps");
			e.printStackTrace();
		} catch (RepositoryException e) {ar.put(e.getMessage());
			e.printStackTrace();
			ar.put(e.getMessage()+"repo ecps");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			ar.put(e.getMessage()+"expo");
		}
		finally{
			session.logout();
			
		}
    	return objMain.toString();
	}

	public String setParticipation(SlingHttpServletRequest request) {
		String state = "";

		Node root, companyNode = null, rootNode, eventNode = null, eventNode1 = null, exhibitionNode = null, exhibitionNode1 = null, rootNodeEx = null;
		Node event, event1 = null, event2, event3 = null;
		try {
			Session session = repos.login(new SimpleCredentials("admin",
					"admin".toCharArray()));
			rootNode = session.getRootNode().getNode("content")
					.getNode("company");
			String companyName = request.getParameter("compN");
			String eventId = request.getParameter("eventId");
			if (rootNode.hasNode(companyName)) {
				companyNode = rootNode.getNode(companyName);
				if (companyNode.hasNode("event")) {
					eventNode = companyNode.getNode("event");
					if (eventNode.hasNode(eventId)) {
						eventNode1 = eventNode.getNode(eventId);
					} else {
						eventNode1 = eventNode.addNode(eventId);
					}
				} else {
					eventNode = companyNode.addNode("event");
					eventNode1 = eventNode.addNode(eventId);
				}
				exhibitionNode = session.getRootNode().getNode("content")
						.getNode("exhibition").getNode(eventId);
				eventNode1.setProperty("eventId", eventId);
				eventNode1.setProperty("eventname",
						exhibitionNode.getProperty("eventName").getString());
			}
			rootNodeEx = session.getRootNode().getNode("content")
					.getNode("exhibition");
			if (rootNodeEx.hasNode(eventId)) {
				event = rootNodeEx.getNode(eventId);
				if (event.hasNode("com")) {
					event1 = event.getNode("com");
					if (event1.hasNode(companyName)) {
						event2 = event1.getNode(companyName);
					} else {
						event2 = event1.addNode(companyName);
					}
				} else {
					event1 = event.addNode("com");
					event2 = event1.addNode(companyName);
				}
				exhibitionNode1 = session.getRootNode().getNode("content")
						.getNode("company").getNode(companyName)
						.getNode("BasicInfo");
				event2.setProperty("id", companyName);
				event2.setProperty("comName",
						exhibitionNode1.getProperty("companyName").getString());
			}
			session.save();
			state = "success";
		} catch (PathNotFoundException e) {
			state = e.getMessage() + "1";
		} catch (RepositoryException e) {
			state = e.getMessage() + "2";
		} catch (Exception e) {

			state = e.getMessage() + "4";
		}
		return state;
	}

	public String saveImage(SlingHttpServletRequest request, String comNodeame) {
		String state = "";

		Node root, companyNode = null, rootNode, node, companyBasicNode = null, fileNewNode, pictureNode, fileNode, jcrNode = null;

		try {
			Session session = repos.login(new SimpleCredentials("admin",
					"admin".toCharArray()));

			rootNode = session.getRootNode().getNode("content")
					.getNode("company");
			if (session.getRootNode().getNode("content").getNode("company")
					.hasNode(comNodeame)) {
				companyNode = session.getRootNode().getNode("content")
						.getNode("company").getNode(comNodeame);
			} else {

				companyNode = session.getRootNode().getNode("content")
						.getNode("company").addNode(comNodeame);
			}
			if (companyNode.hasNode("BasicInfo")) {
				companyBasicNode = companyNode.getNode("BasicInfo");
			} else {
				companyBasicNode = companyNode.addNode("BasicInfo");
			}
			if (companyNode.hasNode("picture")) {

				pictureNode = companyNode.getNode("picture");
			} else {
				pictureNode = companyNode.addNode("picture");
			}

			if (request.getParameter("file") != null
					&& !request.getParameter("file").equals("")) {
				RequestParameter[] ap = request.getRequestParameterMap().get(
						"file");
				for (int i = 0; i < ap.length; i++) {

					String filenam = ap[i].getFileName();
					// response.getWriter().print("inside file----)))-"+group);
					if (filenam != null && !filenam.equals("")) {
						if (pictureNode.hasNode("companyLogo")) {
							fileNewNode = pictureNode.getNode("companyLogo");
							// picNode=node.getNode("picture");
							fileNewNode.remove();
							fileNewNode = pictureNode.addNode("companyLogo",
									"nt:file");
							jcrNode = fileNewNode.addNode("jcr:content",
									"nt:resource");

							jcrNode.setProperty("jcr:data",
									ap[i].getInputStream());

							jcrNode.setProperty("jcr:mimeType", "image/jpeg");
							jcrNode.setProperty("jcr:lastModified",
									Calendar.getInstance());
							companyBasicNode.setProperty("companyLogo",
									"/content/company/" + comNodeame
											+ "/picture/" + "companyLogo");
						} else {
							fileNewNode = pictureNode.addNode("companyLogo",
									"nt:file");
							jcrNode = fileNewNode.addNode("jcr:content",
									"nt:resource");

							jcrNode.setProperty("jcr:data",
									ap[i].getInputStream());

							jcrNode.setProperty("jcr:mimeType", "image/jpeg");
							jcrNode.setProperty("jcr:lastModified",
									Calendar.getInstance());
							companyBasicNode.setProperty("companyLogo",
									"/content/company/" + comNodeame
											+ "/picture/" + "companyLogo");
						}
					}
				}

			}

			session.save();
			state = "success";
		} catch (PathNotFoundException e) {
			state = e.getMessage() + "1";
		} catch (RepositoryException e) {
			state = e.getMessage() + "2";
		} catch (FileNotFoundException e) {
			state = e.getMessage() + "3";

		} catch (Exception e) {

			state = e.getMessage() + "4";
		}
		return state;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#saveCompanyCareer(org.apache.sling
	 * .api.SlingHttpServletRequest)
	 */
	public void saveCompanyCareer(SlingHttpServletRequest request)
			throws ServletException, IOException {

		Node rootNode, careerNode, careerNode2, jobNode = null;
		Session session;
		DateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy");
		Date date = new Date();
		String compnayNode = request.getParameter("companyJobName");
		String dummyField = request.getParameter("companyJobTitle");
		String jobName = dummyField.replaceAll("\\s+", "");
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			rootNode = session.getRootNode().getNode("content")
					.getNode("company/" + compnayNode);
			if (rootNode.hasNode("Career")) {
				careerNode = rootNode.getNode("Career");
			} else {
				careerNode = rootNode.addNode("Career");
			}
			if (careerNode.hasNode("Job")) {
				careerNode2 = careerNode.getNode("Job");
			} else {
				careerNode2 = careerNode.addNode("Job");
			}
			if (careerNode2.hasNode(jobName)) {
				jobNode = careerNode2.getNode(jobName);
			} else {
				jobNode = careerNode2.addNode(jobName);
			}
			jobNode.setProperty("companyJobTitle",
					request.getParameter("companyJobTitle"));
			jobNode.setProperty("title", jobName);
			jobNode.setProperty("companyJobPosition",
					request.getParameter("companyJobPosition"));
			jobNode.setProperty("companyJobExperience",
					request.getParameter("companyJobExperience"));
			jobNode.setProperty("companyJobQualification",
					request.getParameter("companyJobQualification"));
			jobNode.setProperty("companyJobDescription",
					request.getParameter("companyJobDescription"));
			jobNode.setProperty("companyJobResponseTo",
					request.getParameter("companyJobResponseTo"));
			jobNode.setProperty("companyJobResponseEmail",
					request.getParameter("companyJobResponseEmail"));
			jobNode.setProperty("companyJobNumbPosition",
					request.getParameter("companyJobNumbPosition"));
			jobNode.setProperty("postingDate", dateFormat.format(date));

			session.save();
		} catch (PathNotFoundException e) {

			e.printStackTrace();
		} catch (RepositoryException e) {

			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#deleteCompanyCareer(org.apache.sling
	 * .api.SlingHttpServletRequest)
	 */
	public void deleteCompanyCareer(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Node rootNode, careerNode, careerNode2, jobNode, closedJobNode, newJobNode = null;
		Session session;
		DateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy");
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String compnayNode = request.getParameter("companyJobName");
		String jobName = request.getParameter("companyJobTitle");
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			rootNode = session.getRootNode().getNode("content")
					.getNode("company/" + compnayNode);
			if (rootNode.hasNode("Career")) {
				careerNode = rootNode.getNode("Career");
			} else {
				careerNode = rootNode.addNode("Career");
			}
			if (careerNode.hasNode("Job")) {
				careerNode2 = careerNode.getNode("Job");
			} else {
				careerNode2 = careerNode.addNode("Job");
			}
			if (careerNode2.hasNode(jobName.replaceAll("\\s+", ""))) {
				jobNode = careerNode2.getNode(jobName.replaceAll("\\s+", ""));
			} else {
				jobNode = careerNode2.addNode(jobName.replaceAll("\\s+", ""));
			}
			jobNode.remove();
			if (careerNode.hasNode("ClosedJob")) {
				careerNode2 = careerNode.getNode("ClosedJob");
			} else {
				careerNode2 = careerNode.addNode("ClosedJob");
			}
			if (careerNode2.hasNode(dateFormat2.format(date))) {
				closedJobNode = careerNode2.getNode(dateFormat2.format(date));
			} else {
				closedJobNode = careerNode2.addNode(dateFormat2.format(date));
			}
			if (closedJobNode.hasNode(jobName.replaceAll(" ", ""))) {
				newJobNode = closedJobNode.getNode(jobName.replaceAll("\\s+",
						""));
			} else {
				newJobNode = closedJobNode.addNode(jobName.replaceAll("\\s+",
						""));
			}
			newJobNode.setProperty("companyJobTitle",
					request.getParameter("companyJobTitle"));
			newJobNode.setProperty("title", jobName.replaceAll("\\s+", ""));
			newJobNode.setProperty("companyJobPosition",
					request.getParameter("companyJobPosition"));
			newJobNode.setProperty("companyJobExperience",
					request.getParameter("companyJobExperience"));
			newJobNode.setProperty("companyJobQualification",
					request.getParameter("companyJobQualification"));
			newJobNode.setProperty("companyJobDescription",
					request.getParameter("companyJobDescription"));
			newJobNode.setProperty("companyJobResponseTo",
					request.getParameter("companyJobResponseTo"));
			newJobNode.setProperty("companyJobResponseEmail",
					request.getParameter("companyJobResponseEmail"));
			newJobNode.setProperty("companyJobNumbPosition",
					request.getParameter("companyJobNumbPosition"));
			newJobNode.setProperty("closingDate", dateFormat.format(date));

			session.save();
		} catch (Exception e) {
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#joiReasonCareer(org.apache.sling.api
	 * .SlingHttpServletRequest)
	 */
	public void joiReasonCareer(SlingHttpServletRequest request)
			throws ServletException, IOException {

		Node rootNode, careerNode, jobNode = null;
		Session session;

		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			rootNode = session
					.getRootNode()
					.getNode("content")
					.getNode(
							"company/"
									+ request.getParameter("reasonCompanyName")
											.replaceAll("\\s+", ""));
			if (rootNode.hasNode("Career")) {
				careerNode = rootNode.getNode("Career");
			} else {
				careerNode = rootNode.addNode("Career");
			}
			if (careerNode.hasNode("ReasonToJoin")) {
				jobNode = careerNode.getNode("ReasonToJoin");
			} else {
				jobNode = careerNode.addNode("ReasonToJoin");
			}

			jobNode.setProperty("reasonToJoin",
					request.getParameterValues("companyJobReason"));
			jobNode.setProperty("reasonCompanyName",
					request.getParameter("reasonCompanyName"));
			session.save();

		} catch (PathNotFoundException e) {

			e.printStackTrace();
		} catch (RepositoryException e) {

			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#searchCompany(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList searchCompany(String keyword) {
		ArrayList companyList = new ArrayList();
		try {
			Session session = repos.login(new SimpleCredentials("admin",
					"admin".toCharArray()));

			Workspace workspace = session.getWorkspace();

			Query query = workspace.getQueryManager().createQuery(

					"select * from [nt:unstructured] where  (contains('companyName', '*"
							+ keyword + "*') OR  contains('website', '*"
							+ keyword + "*') OR  " + "contains('type', '*"
							+ keyword
							+ "*')) and ISDESCENDANTNODE('/content/company')",
					Query.JCR_SQL2);

			QueryResult qr = query.execute();

			NodeIterator iterator = qr.getNodes();

			while (iterator.hasNext()) {

				companyList.add(iterator.nextNode());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return companyList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#mapUser(org.apache.sling.api.
	 * SlingHttpServletRequest, boolean)
	 */
	public void mapUser(SlingHttpServletRequest request, boolean delete)
			throws ServletException, IOException {
		Node rootNode, relationNode, userNode, typeNode, personNode = null;
		Session session;

		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			rootNode = session
					.getRootNode()
					.getNode("content")
					.getNode(
							"company/"
									+ request.getParameter("mapCompanyName")
											.replaceAll("\\s+", ""));
			if (rootNode.hasNode("Relationship")) {
				relationNode = rootNode.getNode("Relationship");
			} else {
				relationNode = rootNode.addNode("Relationship");
			}
			if (relationNode.hasNode("User")) {
				userNode = relationNode.getNode("User");
			} else {
				userNode = relationNode.addNode("User");
			}
			if (userNode.hasNode(request.getParameter("mappingUserType"))) {
				typeNode = userNode.getNode(request
						.getParameter("mappingUserType"));
			} else {
				typeNode = userNode.addNode(request
						.getParameter("mappingUserType"));
			}
			if (delete) {
				if (typeNode.hasNode(request.getParameter("userId"))) {
					personNode = typeNode.getNode(request
							.getParameter("userId"));
					personNode.setProperty("rejected", "true");
				}
				session.save();
			} else {
				String[] personValue = request.getParameter("mapCompanyPerson")
						.split(",");
				for (String person : personValue) {

					if (typeNode.hasNode(person)) {
						personNode = typeNode.getNode(person);
					} else {
						personNode = typeNode.addNode(person);
					}

					personNode.setProperty("mappingUserType",
							request.getParameter("mappingUserType"));
					personNode.setProperty("mapCompanyName",
							request.getParameter("mapCompanyName"));
					personNode.setProperty("mapCompanyPerson", person);
					personNode.setProperty("selectedPersonPath",
							"content/user/" + person);
					personNode.setProperty("rejected", "false");
					session.save();

				}
			}
		} catch (PathNotFoundException e) {

			e.printStackTrace();
		} catch (RepositoryException e) {

			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#mapCompany(org.apache.sling.api.
	 * SlingHttpServletRequest)
	 */
	public void mapCompany(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Node rootNode, relationNode, companyNode, typeNode, companyNameNode = null;
		Session session;

		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			rootNode = session
					.getRootNode()
					.getNode("content")
					.getNode(
							"company/"
									+ request.getParameter("mapCompanyName")
											.replaceAll("\\s+", ""));
			if (rootNode.hasNode("Relationship")) {
				relationNode = rootNode.getNode("Relationship");
			} else {
				relationNode = rootNode.addNode("Relationship");
			}
			if (relationNode.hasNode("Company")) {
				companyNode = relationNode.getNode("Company");
			} else {
				companyNode = relationNode.addNode("Company");
			}
			if (companyNode.hasNode(request.getParameter("mappingCompanyType"))) {
				typeNode = companyNode.getNode(request
						.getParameter("mappingCompanyType"));
			} else {
				typeNode = companyNode.addNode(request
						.getParameter("mappingCompanyType"));
			}

			String[] companyValue = request.getParameter("mappingName").split(
					",");
			for (String company : companyValue) {

				if (typeNode.hasNode(company)) {
					companyNameNode = typeNode.getNode(company);
				} else {
					companyNameNode = typeNode.addNode(company);
				}

				companyNameNode.setProperty("mappingUserType",
						request.getParameter("mappingCompanyType"));
				companyNameNode.setProperty("mapCompanyName",
						request.getParameter("mapCompanyName"));
				companyNameNode.setProperty("mappingName", company);
				companyNameNode.setProperty("selectedCompanyPath",
						"content/user/" + company);
				session.save();
			}
		} catch (PathNotFoundException e) {

			e.printStackTrace();
		} catch (RepositoryException e) {

			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#mapCompanyType(org.apache.sling.api
	 * .SlingHttpServletRequest)
	 */
	@SuppressWarnings("unused")
	public void mapCompanyType(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Node rootNode, relationNode, companyNode, typeNode = null;
		Session session;

		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			rootNode = session
					.getRootNode()
					.getNode("content")
					.getNode(
							"company/"
									+ request.getParameter("mapCompanyName")
											.replaceAll("\\s+", ""));
			if (rootNode.hasNode("Relationship")) {
				relationNode = rootNode.getNode("Relationship");
			} else {
				relationNode = rootNode.addNode("Relationship");
			}
			if (relationNode.hasNode("Company")) {
				companyNode = relationNode.getNode("Company");
			} else {
				companyNode = relationNode.addNode("Company");
			}

			String[] companyValues = request
					.getParameterValues("companyMapType");
			for (String companyType : companyValues) {
				if (companyNode.hasNode(companyType)) {
					typeNode = companyNode.getNode(companyType);
				} else {
					typeNode = companyNode.addNode(companyType);
				}

			}

			session.save();

		} catch (PathNotFoundException e) {

			e.printStackTrace();
		} catch (RepositoryException e) {

			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#mapProductType(org.apache.sling.api
	 * .SlingHttpServletRequest)
	 */
	@SuppressWarnings("unused")
	public void mapProductType(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Node rootNode, relationNode, companyNode, typeNode;
		Session session;

		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			rootNode = session
					.getRootNode()
					.getNode("content")
					.getNode(
							"company/"
									+ request.getParameter("mapCompanyName")
											.replaceAll("\\s+", ""));
			if (rootNode.hasNode("Relationship")) {
				relationNode = rootNode.getNode("Relationship");
			} else {
				relationNode = rootNode.addNode("Relationship");
			}
			if (relationNode.hasNode("Product")) {
				companyNode = relationNode.getNode("Product");
			} else {
				companyNode = relationNode.addNode("Product");
			}

			String[] productValues = request
					.getParameterValues("mapProductType");
			for (String productType : productValues) {
				if (companyNode.hasNode(productType)) {
					typeNode = companyNode.getNode(productType);
				} else {
					typeNode = companyNode.addNode(productType);
				}

			}

			session.save();

		} catch (PathNotFoundException e) {

			e.printStackTrace();
		} catch (RepositoryException e) {

			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#mapUserType(org.apache.sling.api.
	 * SlingHttpServletRequest)
	 */
	@SuppressWarnings("unused")
	public void mapUserType(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Node rootNode, relationNode, userNode, typeNode = null;
		Session session;

		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			rootNode = session
					.getRootNode()
					.getNode("content")
					.getNode(
							"company/"
									+ request.getParameter("mapCompanyName")
											.replaceAll("\\s+", ""));
			if (rootNode.hasNode("Relationship")) {
				relationNode = rootNode.getNode("Relationship");
			} else {
				relationNode = rootNode.addNode("Relationship");
			}
			if (relationNode.hasNode("User")) {
				userNode = relationNode.getNode("User");
			} else {
				userNode = relationNode.addNode("User");
			}
			String[] userValues = request.getParameterValues("mapUserType");
			for (String userType : userValues) {
				if (userNode.hasNode(userType)) {
					typeNode = userNode.getNode(userType);
				} else {
					typeNode = userNode.addNode(userType);
				}

			}

			session.save();

		} catch (PathNotFoundException e) {

			e.printStackTrace();
		} catch (RepositoryException e) {

			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#uploadCompanyLogo(org.apache.sling
	 * .api.SlingHttpServletRequest)
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public void uploadCompanyLogo(SlingHttpServletRequest request)
			throws ServletException, IOException {
		String companyName = request.getParameter("compN");
		Node root, companyNode = null, companyBasicNode, fileNewNode, pictureNode, fileNode, jcrNode = null;

		try {
			Session session = repos.login(new SimpleCredentials("admin",
					"admin".toCharArray()));

			if (session.getRootNode().getNode("content").getNode("company")
					.hasNode(companyName)) {
				companyNode = session.getRootNode().getNode("content")
						.getNode("company").getNode(companyName);
			}
			if (companyNode.hasNode("BasicInfo")) {
				companyBasicNode = companyNode.getNode("BasicInfo");
			} else {
				companyBasicNode = companyNode.addNode("BasicInfo");

			}
			if (companyNode.hasNode("picture")) {

				pictureNode = companyNode.getNode("picture");
			} else {
				pictureNode = companyNode.addNode("picture");
			}
			if (pictureNode.hasNode(request.getParameter("type"))) {

				fileNode = pictureNode.getNode(request.getParameter("type"));
				fileNode.remove();
			}
			fileNewNode = pictureNode.addNode(request.getParameter("type"),
					"nt:file");

			jcrNode = fileNewNode.addNode("jcr:content", "nt:resource");
			for (Entry<String, RequestParameter[]> e : request
					.getRequestParameterMap().entrySet()) {
				for (RequestParameter p : e.getValue()) {
					if (!p.isFormField()) {

						p.getInputStream();
						jcrNode.setProperty("jcr:data", p.getInputStream());
					}
				}
			}

			jcrNode.setProperty("jcr:lastModified", Calendar.getInstance());
			jcrNode.setProperty("jcr:mimeType", "image/jpg");
			companyBasicNode.setProperty(
					request.getParameter("type"),
					request.getContextPath() + "/content/company/"
							+ request.getParameter("compN") + "/picture/"
							+ request.getParameter("type"));
			session.save();

		} catch (PathNotFoundException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#deleteMapping(java.lang.String)
	 */
	public void deleteMapping(String nodeName) {
		Node node = null;
		try {
			Session session = repos.login(new SimpleCredentials("admin",
					"admin".toCharArray()));

			node = session.getNode(nodeName);
			node.remove();
			session.save();

		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#addTypes(java.lang.String,
	 * java.lang.String, java.lang.String[], java.lang.String)
	 */
	public void addTypes(String nodeName, String name, String[] values,
			String mapType) {
		Node relationNode = null, node, mapNode, companyNode, productNode, typeNode = null;
		Session session;

		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			if (mapType.equals("Company")) {
				companyNode = session.getRootNode().getNode("content")
						.getNode("company/" + name.replaceAll("\\s+", ""));
				if (companyNode.hasNode("Relationship")) {
					relationNode = companyNode.getNode("Relationship");
				} else {
					relationNode = companyNode.addNode("Relationship");
				}

			} else if (mapType.equals("User")) {
				relationNode = session.getRootNode().getNode("content")
						.getNode("user/" + name.replaceAll("\\s+", ""));
			}
			if (relationNode.hasNode("Product")) {
				mapNode = relationNode.getNode("Product");
			} else {
				mapNode = relationNode.addNode("Product");
			}
			if (mapNode.hasNode(nodeName)) {
				node = mapNode.getNode(nodeName);
			} else {
				node = mapNode.addNode(nodeName);
			}
			for (String type : values) {
				productNode = session.getNode(type);
				if (node.hasNode("_"
						+ productNode.getProperty("id").getString())) {
					typeNode = node.getNode("_"
							+ productNode.getProperty("id").getString());
				} else {
					typeNode = node.addNode("_"
							+ productNode.getProperty("id").getString());
				}
				typeNode.setProperty("productPath", type);
				typeNode.setProperty("productName",
						productNode.getProperty("name").getString());
			}
			session.save();
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#addContentCategory(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void addContentCategory(String category, String type, String company) {
		Node node, imageNode, categoryNode, typeNode = null;
		Session session = null;
		String categoryVal = category.replaceAll("\\s+", "").trim();
		try {
			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			node = session.getNode("/content/company/" + company);
			if (node.hasNode("media")) {
				typeNode = node.getNode("media");
			} else {
				typeNode = node.addNode("media");
			}
			if (typeNode.hasNode(type)) {
				imageNode = typeNode.getNode(type);
			} else {
				imageNode = typeNode.addNode(type);
			}
			if (imageNode.hasNode(categoryVal)) {
				categoryNode = imageNode.getNode(categoryVal);
			} else {
				categoryNode = imageNode.addNode(categoryVal);
				categoryNode.setProperty("categoryName", category);
			}

			session.save();

		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			session.logout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#videoUpload(java.lang.String,
	 * org.apache.sling.api.SlingHttpServletRequest, java.lang.String,
	 * java.lang.String)
	 */

	@SuppressWarnings({ "deprecation" })
	public String videoUpload(String userId, SlingHttpServletRequest request,
			String category, String company) throws ServletException,
			IOException {
		String path = request.getSession().getServletContext().getRealPath("/")
				+ "/temp/";
		Session session;
		String status = "false";
		String response = null;
		Node filePathNode, fileNode, jcrNode, companyNode, mediaNode, videoNode, categoryNode = null;
		String randomNumber = gallery_service.generateRandomNumber();
		DateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy HH:mm");
		Date date = new Date();
		File files = new File(path);
		if (!files.exists()) {
			files.mkdirs();
		}
		try {
			byte[] data = request.getParameter("myFile").getBytes("ISO-8859-1");
			if (data.length > 0) {
				File file = new File(path, randomNumber + ".avi");
				FileOutputStream fout = new FileOutputStream(file);
				fout.write(data);
				fout.close();

			}

			response = gallery_service.convert(randomNumber + ".avi", path);
			if (response.equals("success")) {
				session = repos.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));

				companyNode = session.getNode("/content/company/" + company);
				if (companyNode.hasNode("media")) {
					mediaNode = companyNode.getNode("media");
				} else {
					mediaNode = companyNode.addNode("media");
				}
				if (mediaNode.hasNode("Video")) {
					videoNode = mediaNode.getNode("Video");
				} else {
					videoNode = mediaNode.addNode("Video");

				}
				if (videoNode.hasNode(category)) {
					categoryNode = videoNode.getNode(category.replaceAll(
							"\\s+", ""));
				} else {
					categoryNode = videoNode.addNode(category.replaceAll(
							"\\s+", ""));
					categoryNode.setProperty("categoryName", category);
				}

				filePathNode = categoryNode.addNode(randomNumber);
				filePathNode.setProperty("photoDate", dateFormat.format(date));
				filePathNode.setProperty("userId", userId);
				fileNode = filePathNode.addNode(randomNumber + ".avi.webm",
						"nt:file");
				jcrNode = fileNode.addNode("jcr:content", "nt:resource");

				jcrNode.setProperty("jcr:data", new FileInputStream(path
						+ randomNumber + ".avi.webm"));
				jcrNode.setProperty("jcr:lastModified", Calendar.getInstance());
				jcrNode.setProperty("jcr:mimeType", "video/webm");
				filePathNode.setProperty("videoName",
						request.getParameter("videoName"));
				filePathNode.setProperty("videoDesc",
						request.getParameter("videoDesc"));

				ArrayList<String> latestVideoList = new ArrayList<String>();
				if (videoNode.hasProperty("latestVideo")) {
					latestVideoList.add(fileNode.getPath());
					Value[] videoVal = videoNode.getProperty("latestVideo")
							.getValues();
					for (int i = 0; i < videoVal.length && i < 9; i++) {
						latestVideoList.add(videoVal[i].getString());
					}
				} else {
					latestVideoList.add(fileNode.getPath());
				}
				if (latestVideoList != null && latestVideoList.size() > 0) {
					String[] latestVideoArr = new String[latestVideoList.size()];
					latestVideoArr = latestVideoList.toArray(latestVideoArr);
					videoNode.setProperty("latestVideo", latestVideoArr);

				}

				session.save();
				status = "true";
			}
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			status = "false" + e.getMessage();
			// e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			status = "false" + e.getMessage();
			// e.printStackTrace();
		} catch (Exception e) {
			status = "false" + e.getMessage() + "<br>"
					+ e.getLocalizedMessage() + "<br>" + e.getCause();
		} finally {
			// File file = new File(path + randomNumber + ".webm");
			// file.delete();
			File file2 = new File(path + randomNumber + ".avi");
			file2.delete();

		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#allFileUpload(java.lang.String,
	 * java.lang.String, org.apache.sling.api.SlingHttpServletRequest,
	 * java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	public void allFileUpload(String userId, String category,
			SlingHttpServletRequest request, String company, String type)
			throws ServletException, IOException {

		Session session;
		Node companyNode, mediaNode, pictureNode, categoryNode, fileNode, thumbnalNode, thumbnalNodeJcrNode, picNode, jcrNode = null;
		String path = request.getSession().getServletContext().getRealPath("/")
				+ "/temp/";
		String categoryV = category.replaceAll("\\s+", "").trim();
		DateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy HH:mm");
		Date date = new Date();
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));

			companyNode = session.getNode("/content/company/" + company);
			if (companyNode.hasNode("media")) {
				mediaNode = companyNode.getNode("media");
			} else {
				mediaNode = companyNode.addNode("media");
			}
			if (mediaNode.hasNode(type)) {
				pictureNode = mediaNode.getNode(type);
			} else {
				pictureNode = mediaNode.addNode(type);
			}
			if (pictureNode.hasNode(categoryV)) {
				categoryNode = pictureNode.getNode(categoryV);
			} else {
				categoryNode = pictureNode.addNode(categoryV);
				categoryNode.setProperty("categoryName", category);
			}

			String mimeType = "";

			for (Entry<String, RequestParameter[]> e : request
					.getRequestParameterMap().entrySet()) {
				for (RequestParameter p : e.getValue()) {
					if (!p.isFormField()) {
						mimeType = p.getContentType();
						if (mimeType == null) {
							mimeType = "application/octet-stream";
						}
						String randomNumber = gallery_service
								.generateRandomNumber();
						fileNode = categoryNode.addNode(randomNumber);
						fileNode.setProperty("photoDate",
								dateFormat.format(date));
						fileNode.setProperty("userId", userId);
						if (type.equals("Picture")) {
							picNode = fileNode.addNode("xOp", "nt:file");
						} else {
							picNode = fileNode.addNode(p.getFileName(),
									"nt:file");
						}
						jcrNode = picNode.addNode("jcr:content", "nt:resource");

						jcrNode.setProperty("jcr:data", p.getInputStream());
						jcrNode.setProperty("jcr:lastModified",
								Calendar.getInstance());
						jcrNode.setProperty("jcr:mimeType", mimeType);
						if (type.equals("Picture")) {
							gallery_service.generateThumbnail(path,
									randomNumber, p.getInputStream(), 150);
							File fileThumbnail = new File(path + randomNumber
									+ ".jpg");
							InputStream thumbnailStream = new FileInputStream(
									fileThumbnail);
							thumbnalNode = fileNode.addNode("x150", "nt:file");
							thumbnalNodeJcrNode = thumbnalNode.addNode(
									"jcr:content", "nt:resource");

							thumbnalNodeJcrNode.setProperty("jcr:data",
									thumbnailStream);
							thumbnalNodeJcrNode.setProperty("jcr:lastModified",
									Calendar.getInstance());
							thumbnalNodeJcrNode.setProperty("jcr:mimeType",
									mimeType);
							fileThumbnail.delete();
						}

						ArrayList<String> latestImageList = new ArrayList<String>();
						if (pictureNode.hasProperty("latest")) {
							latestImageList.add(fileNode.getPath());
							Value[] videoVal = pictureNode
									.getProperty("latest").getValues();
							for (int i = 0; i < videoVal.length && i < 9; i++) {
								latestImageList.add(videoVal[i].getString());
							}
						} else {
							latestImageList.add(fileNode.getPath());
						}
						String[] latestImageArr = new String[latestImageList
								.size()];
						latestImageArr = latestImageList
								.toArray(latestImageArr);
						pictureNode.setProperty("latest", latestImageArr);

					}
				}
			}
			session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#downloadNode(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	public Map<Object, Object> downloadNode(String path) {
		Session session;
		Map<Object, Object> map = new HashMap<Object, Object>();
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			Node node = session.getNode(path).getNode("jcr:content");

			InputStream in = node.getProperty("jcr:data").getStream();
			map.put("stream", in);
			map.put("fileName", node.getParent().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#getStock(java.lang.String)
	 */
	public String getStock(String id) {

		JSONObject json = new JSONObject();
		Session session;
		Node companyNode, basicNode = null;
		String company = id.replaceAll("\\s+", "");
		try {
			String BSENumber = "";
			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			companyNode = session.getNode("/content/company/" + company);
			if (companyNode.hasNode("BasicInfo")) {
				basicNode = companyNode.getNode("BasicInfo");
				if (basicNode.hasProperty("BSENumber")
						&& !basicNode.getProperty("BSENumber").getString()
								.equals("")) {
					BSENumber = basicNode.getProperty("BSENumber").getString();
					String result = security_service
							.callGetService("http://finance.google.com/finance/info?client=ig&q="
									+ BSENumber);
					JSONArray po = new JSONArray(result.replace("//", ""));
					json.accumulate("rate", po.getJSONObject(0).getString("l"));
					String amount = po.getJSONObject(0).getString("c");
					json.accumulate("change", amount);
					if (amount.contains("-")) {
						json.accumulate("variation", "neg");
					} else if (amount.contains("+")) {
						json.accumulate("variation", "pos");
					}
					json.accumulate("changeP",
							po.getJSONObject(0).getString("cp"));
					json.accumulate("time", po.getJSONObject(0).getString("lt")
							.replace("GMT+5:30", ""));
					json.accumulate("status", "success");
				}
			}

		} catch (Exception e) {
			return null;
		}
		return json.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#getStockDemo(java.lang.String)
	 */
	public String getStockDemo(String BSENumber) {

		JSONObject json = new JSONObject();
		try {

			String result = security_service
					.callGetService("http://finance.google.com/finance/info?client=ig&q="
							+ BSENumber);
			JSONArray po = new JSONArray(result.replace("//", ""));
			json.accumulate("rate", po.getJSONObject(0).getString("l_cur"));
			json.accumulate("change", po.getJSONObject(0).getString("c"));
			json.accumulate("changeP", po.getJSONObject(0).getString("cp"));
			json.accumulate("status", "success");
		} catch (Exception e) {
			return null;
		}
		return json.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#getInfo(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public String getInfo(String value, String type, String contextPath) {
		Session session;
		Node node = null;
		String result = "";
		try {
			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			if (type.equals("user")) {
				node = session.getNode("/content/user/" + value);
				if (node.hasProperty("profileImage")) {
					result = "<div style='height=100px;' ><img src='"
							+ node.getProperty("profileImage").getString()
							+ "' style='float:left;' height='70px' width='60px' /></div>";
				} else {
					result = "<div style='height=100px;'><img src='"
							+ contextPath
							+ "/apps/user/css/images/photo.gif"
							+ "' style='float:left;' height='70px' width='60px' /></div>";
				}
				result += "<div style='display:block;font-size:13px;padding:0 0 0 2px;'>";
				result += "<span style='color:#77B6F1;'>Name: </span>"
						+ node.getProperty("name").getString();
				if (node.hasProperty("lastName")) {
					result += " " + node.getProperty("lastName").getString();
				} else {
					result += " ";
				}
				if (node.hasProperty("birthDay")) {
					result += "<br/><span style='color:#77B6F1;'>DOB: </span>"
							+ node.getProperty("birthDay").getString();
				} else {
					result += "<br/><span style='color:#77B6F1;'>DOB: </span>";
				}
				if (node.hasProperty("city")) {
					result += "<br/><span style='color:#77B6F1;'>City: </span>"
							+ node.getProperty("city").getString();
				} else {
					result += "<br/><span style='color:#77B6F1;'>City: </span>";
				}
				result += "</div>";

			} else if (type.equals("company")) {
				node = session.getNode("/content/company/" + value
						+ "/BasicInfo");
				if (node.hasProperty("companyLogo")) {
					result = "<div style='height=140px;' ><img src='"
							+ node.getProperty("companyLogo").getString()
							+ "' style='float:left;' height='60px' width='100px' /></div>";
				} else {
					result = "<div><img src='"
							+ contextPath
							+ "/apps/user/css/images/photo.gif"
							+ "' style='float:left;' height='60px' width='100px' /></div>";
				}
				result += "<div style='display:block;font-size:10px;line-height:14px;padding:0 0 0 2px;'>";
				result += "<span style='color:#77B6F1;'>Name: </span>"
						+ node.getProperty("companyName").getString();
				result += "<br/>";
				if (node.hasProperty("description")) {
					result += "<span style='color:#77B6F1;'>Description: </span>"
							+ node.getProperty("description").getString()
									.substring(0, 120) + "...";
				} else {
					result += "<span style='color:#77B6F1;'>Description: </span>";
				}
				result += "</div>";

			} else if (type.equals("product")) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#setPrivacy(org.apache.sling.api.
	 * SlingHttpServletRequest)
	 */
	public void setPrivacy(SlingHttpServletRequest request)
			throws ServletException, IOException {
		Session session;
		String companyId = request.getParameter("company");
		Node node, privacyNode = null;
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			node = session.getRootNode().getNode("content").getNode("company")
					.getNode(companyId);

			if (node.hasNode("Privacy")) {
				privacyNode = node.getNode("Privacy");
			} else {
				privacyNode = node.addNode("Privacy");
			}

			privacyNode.setProperty("contactPrivacy",
					request.getParameter("contactPrivacy"));
			privacyNode.setProperty("imagesPrivacy",
					request.getParameter("imagesPrivacy"));
			privacyNode.setProperty("videoPrivacy",
					request.getParameter("videoPrivacy"));
			privacyNode.setProperty("productPrivacy",
					request.getParameter("productPrivacy"));
			privacyNode.setProperty("userRelationPrivacy",
					request.getParameter("userRelationPrivacy"));
			privacyNode.setProperty("compRelationPrivacy",
					request.getParameter("compRelationPrivacy"));
			privacyNode.setProperty("comDocsPrivacy",
					request.getParameter("comDocsPrivacy"));
			privacyNode.setProperty("secureDocsPrivacy",
					request.getParameter("secureDocsPrivacy"));
			privacyNode.setProperty("comTaxPrivacy",
					request.getParameter("comTaxPrivacy"));
			privacyNode.setProperty("comNewsPrivacy",
					request.getParameter("comNewsPrivacy"));

			session.save();
		} catch (PathNotFoundException e) {

			e.printStackTrace();
		} catch (RepositoryException e) {

			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#getVerifier()
	 */
	public String[] getVerifier() {
		ResourceBundle bundle = ResourceBundle.getBundle("server");
		OAuthService service = new ServiceBuilder()
				.provider(TwitterApi.class)
				.apiKey(bundle.getString("twitter.key"))
				.apiSecret(bundle.getString("twitter.secret"))
				.callback(
						bundle.getString("sling.serverSpec")
								+ "/servlet/company/show.setTwitterToken")
				.build();
		String[] tokenId = { "", "", "" };
		Token requestToken = service.getRequestToken();
		String redirect_url = service.getAuthorizationUrl(requestToken);
		tokenId[0] = requestToken.getToken();
		tokenId[1] = requestToken.getSecret();
		tokenId[2] = redirect_url;
		return tokenId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#setCompanyTwitterCred(java.lang.String
	 * , java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int setCompanyTwitterCred(String company, String token,
			String secret, String twitterScreenName, String twitterId) {
		Session session;
		Node companyNode, socialNode, networkNode, twitterNode;
		String screenName = "";

		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			companyNode = session.getRootNode().getNode("content/company")
					.getNode(company);
			if (companyNode.hasNode("NetworkProvider")) {
				networkNode = companyNode.getNode("NetworkProvider");
			} else {
				networkNode = companyNode.addNode("NetworkProvider");
			}

			if (networkNode.hasNode("Twitter")) {
				twitterNode = networkNode.getNode("Twitter");
			} else {
				twitterNode = networkNode.addNode("Twitter");
			}

			twitterNode.setProperty("token", token);
			twitterNode.setProperty("secret", secret);
			twitterNode.setProperty("screenName", twitterScreenName);
			twitterNode.setProperty("socialId", twitterId);

			if (companyNode.getNode("BasicInfo").hasProperty(
					"socialMessagingId")) {
				screenName = companyNode.getNode("BasicInfo")
						.getProperty("socialMessagingId").getString();

				if (session.getRootNode().getNode("content")
						.getNode("socialMessenger")
						.getNode("socialMessagingId").hasNode(screenName)) {
					socialNode = session.getRootNode().getNode("content")
							.getNode("socialMessenger")
							.getNode("socialMessagingId").getNode(screenName);
					socialNode.setProperty("twitterToken", token);
					socialNode.setProperty("twitterSecret", secret);
					socialNode.setProperty("twitterScreenName",
							twitterScreenName);
					socialNode.setProperty("twitterId", twitterId);

				}

			}

			session.save();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.profile.service.CompanyService#getTwitterData(java.lang.String)
	 */
	public Map<String, String> getTwitterData(String companyId) {
		Map<String, String> map = new HashMap<String, String>();
		String response = "";
		OAuthService localService = new ServiceBuilder()
				.provider(TwitterApi.class)
				.apiKey(bundle.getString("twitter.key"))
				.apiSecret(bundle.getString("twitter.secret")).build();
		String socialId = "";
		map = getSocialRequestToken(companyId, "Twitter");
		if (map == null) {
			return null;
		}
		socialId = map.get("socialId");
		response = socialNetworkService.getOutput(map.get("token"),
				map.get("secret"),
				"https://api.twitter.com/1.1/users/lookup.json?user_id="
						+ socialId, localService);
		map = new HashMap<String, String>();
		try {
			JSONArray jsonArr = new JSONArray(response);
			System.out.println(jsonArr.getJSONObject(0).get("followers_count"));
			map.put("follower_count",
					jsonArr.getJSONObject(0).get("followers_count") + "");
			map.put("following_count",
					jsonArr.getJSONObject(0).get("friends_count") + "");
			map.put("screen_name", jsonArr.getJSONObject(0).get("screen_name")
					+ "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return map;
	}

	/**
	 * Gets the social request token.
	 * 
	 * @param company
	 *            the company
	 * @param type
	 *            the type
	 * @return the social request token
	 */
	public Map<String, String> getSocialRequestToken(String company, String type) {
		Map<String, String> map = new HashMap<String, String>();
		Node companyNode, networkNode, typeNode = null;
		Session session;
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			companyNode = session.getRootNode().getNode(
					"content/company/" + company);
			if (companyNode.hasNode("NetworkProvider")) {
				networkNode = companyNode.getNode("NetworkProvider");
			} else {
				networkNode = companyNode.addNode("NetworkProvider");
			}

			if (networkNode.hasNode(type)) {
				typeNode = networkNode.getNode(type);
			} else {
				typeNode = networkNode.addNode(type);
			}
			map.put("token", typeNode.getProperty("token").getString());
			map.put("secret", typeNode.getProperty("secret").getString());
			map.put("socialId", typeNode.getProperty("socialId").getString());
		} catch (PathNotFoundException e) {

			return null;
		} catch (RepositoryException e) {

			return null;
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.profile.service.CompanyService#setCompanySocialCred(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setCompanySocialCred(String company, String name, String url,
			String type) {
		Session session;
		Node companyNode, networkNode, twitterNode;
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			companyNode = session.getRootNode().getNode("content/company")
					.getNode(company);
			if (companyNode.hasNode("NetworkProvider")) {
				networkNode = companyNode.getNode("NetworkProvider");
			} else {
				networkNode = companyNode.addNode("NetworkProvider");
			}

			if (networkNode.hasNode(type)) {
				twitterNode = networkNode.getNode(type);
			} else {
				twitterNode = networkNode.addNode(type);
			}

			twitterNode.setProperty("name", name);
			twitterNode.setProperty("url", url);
			session.save();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public boolean deleteCompany(String company) {

		Session session;
		boolean status = false;
		Node companyNode, companyNode1, networkNode, twitterNode;
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			companyNode = session.getRootNode().getNode("content/company");

			if (companyNode.hasNode(company)) {
				companyNode1 = companyNode.getNode(company);
				companyNode1.remove();
				status = true;
			}

			session.save();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return status;

	}

	public String saveBuyingReq(SlingHttpServletRequest request,
			SlingHttpServletResponse response) {
		Session session;
		String status = "true";
		Node prodNode, compNode, tempPrdNode, tempCompNode, tempCompNode1, tempCompNode2;
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			prodNode = session.getRootNode()
					.getNode("content/product/products");
			compNode = session.getRootNode().getNode("content/company");
			String queryString=request.getParameter("prdname");
			if(queryString!=null && queryString.indexOf("'")!=-1){
				queryString=queryString.replace("'", "");
			}
			//String querryStr = "select * from [nt:unstructured] where  ISDESCENDANTNODE('/content/product/products/') and name  like '"
			//		+  queryString+ "%'";
			String querryStr = "select * from [nt:unstructured] where (contains('name','*"
					+ queryString
					+ "*'))  and ISDESCENDANTNODE('/content/product/products/')";
			Workspace workspace = session.getWorkspace();
			Query query = workspace.getQueryManager().createQuery(querryStr,
					Query.JCR_SQL2);
			QueryResult result = query.execute();
			NodeIterator iterator = result.getNodes();
			int suppCount = 0;
			//String tempComp[] = new String[compid.length];
    	    // String tempRFQCount = "";
    	    //int tempCompCount = 0;
			// create an empty array list 
			   ArrayList<String> arrlist = new ArrayList<String>();
			   
			while (iterator.hasNext()) {
				tempPrdNode = iterator.nextNode();
				if (tempPrdNode.hasNode("sup")
						&& tempPrdNode.getNode("sup").hasNodes()) {
					NodeIterator iterator2 = tempPrdNode.getNode("sup")
							.getNodes();
					while (iterator2.hasNext()) {
						String comp = iterator2.nextNode().getProperty("supid")
								.getString();

						if (compNode.hasNode(comp)) {
							Node tempcompNode = compNode.getNode(comp);
							/*if (tempcompNode.hasNode("buyingrequest")) {
								tempCompNode1 = tempcompNode
										.getNode("buyingrequest");
								String nodeValue = tempCompNode1.getProperty(
										"nodeCount").getString();
								tempCompNode2 = tempCompNode1
										.addNode(nodeValue);
								tempCompNode1.setProperty("nodeCount",
										Integer.parseInt(nodeValue) + 1);
								tempCompNode2.setProperty("name",
										request.getParameter("prdname"));
								tempCompNode2.setProperty("estquantity",
										request.getParameter("ordquant"));
								tempCompNode2.setProperty("piece",
										request.getParameter("piece"));
								tempCompNode2.setProperty("detail",
										request.getParameter("detailSpec"));
								Node user=session.getRootNode().getNode("content").getNode("user").getNode(request.getRemoteUser().replace("@", "_"));
								tempCompNode2.setProperty("sendername", user.getProperty("name").getString());
								tempCompNode2.setProperty("senderemail", request.getRemoteUser().replace("@", "_"));
								if (request.getParameter("producturl") == null) {
									tempCompNode2.setProperty("fob",
											request.getParameter("fob"));
									tempCompNode2.setProperty("pup",
											request.getParameter("pup"));
									tempCompNode2.setProperty("currency",
											request.getParameter("price"));
									tempCompNode2.setProperty("port",
											request.getParameter("dp"));
									tempCompNode2.setProperty("paymentterms",
											request.getParameter("pterms"));
									tempCompNode2.setProperty("customer",
											request.getRemoteUser());
									RequestParameter[] ap = request
											.getRequestParameterMap().get(
													"filequote");

									if (request.getParameter("filequote") != null) {

										String fileType = "";

										for (int i = 0; i < ap.length; i++) {
											if (ap[i].getFileName() != null
													&& ap[i].getFileName()
															.trim() != "") {

												String filenam = ap[i]
														.getFileName();

												fileType = "";

												Node fileNode = tempCompNode2
														.addNode("0", "nt:file");

												Node jcrNode = fileNode
														.addNode("jcr:content",
																"nt:resource");

												jcrNode.setProperty("jcr:data",
														ap[i].getInputStream());

												jcrNode.setProperty(
														"jcr:mimeType",
														"text/plain");

												tempCompNode2.setProperty(
														"fileType", fileType);
												tempCompNode2
														.setProperty(
																"imgpath",
																"/content/company/"
																		+ comp
																		+ "/buyingrequest/"
																		+ nodeValue
																		+ "/0/");
											}
										}
									}
								} else {
									tempCompNode2.setProperty("fob", "");
									tempCompNode2.setProperty("pup", "");
									tempCompNode2.setProperty("currency", "");
									tempCompNode2.setProperty("port", "");
									tempCompNode2.setProperty("paymentterms",
											"");
									tempCompNode2.setProperty("customer", "");

								}
							} else {
								tempCompNode1 = tempcompNode
										.addNode("buyingrequest");
								tempCompNode2 = tempCompNode1.addNode(String
										.valueOf(suppCount));
								tempCompNode1.setProperty("nodeCount",
										suppCount + 1);
								tempCompNode2.setProperty("name",
										request.getParameter("prdname"));
								tempCompNode2.setProperty("estquantity",
										request.getParameter("ordquant"));
								tempCompNode2.setProperty("piece",
										request.getParameter("piece"));
								tempCompNode2.setProperty("detail",
										request.getParameter("detailSpec"));
								Node user=session.getRootNode().getNode("content").getNode("user").getNode(request.getRemoteUser().replace("@", "_"));
								tempCompNode2.setProperty("sendername", user.getProperty("name").getString());
								tempCompNode2.setProperty("senderemail", request.getRemoteUser().replace("@", "_"));
								
								if (request.getParameter("producturl") == null) {
									tempCompNode2.setProperty("fob",
											request.getParameter("fob"));
									tempCompNode2.setProperty("pup",
											request.getParameter("pup"));
									tempCompNode2.setProperty("currency",
											request.getParameter("price"));
									tempCompNode2.setProperty("port",
											request.getParameter("dp"));
									tempCompNode2.setProperty("paymentterms",
											request.getParameter("pterms"));
									tempCompNode2.setProperty("customer",
											request.getRemoteUser());
									RequestParameter[] ap = request
											.getRequestParameterMap().get(
													"filequote");

									if (request.getParameter("filequote") != null) {

										String fileType = "";

										for (int i = 0; i < ap.length; i++) {
											if (ap[i].getFileName() != null
													&& ap[i].getFileName()
															.trim() != "") {

												String filenam = ap[i]
														.getFileName();

												fileType = "";

												Node fileNode = tempCompNode2
														.addNode("0", "nt:file");

												Node jcrNode = fileNode
														.addNode("jcr:content",
																"nt:resource");

												jcrNode.setProperty("jcr:data",
														ap[i].getInputStream());

												jcrNode.setProperty(
														"jcr:mimeType",
														"text/plain");

												tempCompNode2.setProperty(
														"fileType", fileType);
												tempCompNode2
														.setProperty(
																"imgpath",
																"/content/company/"
																		+ comp
																		+ "/buyingrequest/"
																		+ suppCount
																		+ "/0/");
											}
										}
									}
								} else {
									tempCompNode2.setProperty("fob", "");
									tempCompNode2.setProperty("pup", "");
									tempCompNode2.setProperty("currency", "");
									tempCompNode2.setProperty("port", "");
									tempCompNode2.setProperty("paymentterms",
											"");
									tempCompNode2.setProperty("customer", "");

								}
							}*/
							
							
							// save in rfq node of company starts here
							String imgpath = "";
							if(tempPrdNode.hasNode("media") && tempPrdNode.getNode("media").hasNode("images") && tempPrdNode.getNode("media").getNode("images").hasNodes()){
								NodeIterator imagNode = tempPrdNode.getNode("media").getNode("images").getNodes();
								if(imagNode.hasNext()){
									imgpath =imagNode.nextNode().getProperty("imgpath").getString();
								}
							}
							
							if (tempcompNode.hasNode("Rfq")) {
								tempCompNode1 = tempcompNode.getNode("Rfq");
		        	    	       Node workNode = null;
		        	    	       Node pNode = null;
		        	    	       long count = tempCompNode1.getProperty("rfqcount").getLong();
		        	    	       // Convert String Array to List
		        	    	      // List<String> list = Arrays.asList(tempComp);

		        	    	       if (arrlist.contains(tempcompNode.getName())) {
		        	    	        workNode = tempCompNode1.getNode(String.valueOf(count));
		        	    	        pNode = workNode.addNode(tempPrdNode.getName());
		        	    	       } else {
		        	    	        // long c=Long.parseLong(count);
		        	    	        long count1 = count + 1;
		        	    	        workNode = tempCompNode1.addNode(String.valueOf(count1));
		        	    	        tempCompNode1.setProperty("rfqcount", count1);
		        	    	        workNode.setProperty("userid", request.getRemoteUser());
		        	    	        workNode.setProperty("status", "new");
		        	    	        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		        	    	        Date rfqdate = new Date();
		        	    	        workNode.setProperty("CreationDate", df.format(rfqdate).toString());
		        	    	        workNode.setProperty("rfqNo", String.valueOf(count1));
		        	    	        pNode = workNode.addNode(tempPrdNode.getName());
		        	    	        //tempComp[tempCompCount] = compid[i];
		        	    	        arrlist.add(tempcompNode.getName());
		        	    	       }
		        	    	       // session.save();
		        	    	       // Node workNode =
		        	    	       // rfqNode.getNode(String.valueOf(count1));

		        	    	       // session.save();
		        	    	       // Node pNode=workNode.getNode(pid[i]);
		        	    	       pNode.setProperty("productid", tempPrdNode.getName());
		        	    	       pNode.setProperty("productName", tempPrdNode.getProperty("name").getString());
		        	    	       pNode.setProperty("productImage", imgpath);
		        	    	       pNode.setProperty("productpQuantity", request.getParameter("ordquant"));
		        	    	       pNode.setProperty("productDescription", request.getParameter("detailSpec"));

		        	    	      } else {
		        	    	    	  tempCompNode1 = tempcompNode.addNode("Rfq");
		        	    	    	  tempCompNode1.setProperty("rfqcount", 1);

		        	    	       Node workNode = tempCompNode1.addNode("1");
		        	    	       // rfqNode.setProperty("rfqcount", count1);
		        	    	       // session.save();
		        	    	       // Node workNode =
		        	    	       // rfqNode.getNode(String.valueOf(count1));
		        	    	       workNode.setProperty("userid", request.getRemoteUser());
		        	    	       workNode.setProperty("status", "new");
		        	    	       SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		        	    	       Date rfqdate = new Date();
		        	    	       workNode.setProperty("CreationDate", df.format(rfqdate).toString());
		        	    	       workNode.setProperty("rfqNo", "1");

		        	    	       Node pNode = workNode.addNode(tempPrdNode.getName());
		        	    	       // session.save();
		        	    	       // Node pNode=workNode.getNode(pid[i]);
		        	    	       pNode.setProperty("productid", tempPrdNode.getName());
		        	    	       pNode.setProperty("productName", tempPrdNode.getProperty("name").getString());
		        	    	       pNode.setProperty("productImage", imgpath);
		        	    	       pNode.setProperty("productpQuantity", request.getParameter("ordquant"));
		        	    	       pNode.setProperty("productDescription", request.getParameter("detailSpec"));
		        	    	       arrlist.add(tempcompNode.getName());
		        	    	      }
		        	    	    //  tempCompCount++;
							
							
						}
					}
				}
			}
			
			 String htmlText=null;
	    	    
	    	    //String htmldata[]=new String[tempComp.length];
	    	    String htmldata=null;
	    	    String compid1=null;
	    	    String pid1=null ;
	    	    String pname1 =null;
	    	    String pquantity1 =null;
	    	    String pdescription1=null ;
	    	    Node compMailNode = session.getRootNode().getNode("content").getNode("company");
	    	    
	    	    for(int j=0; j<arrlist.size(); j++){
	    	     
	    	     //if(arrlist.get(j) != null && arrlist.get(j) != ""){
	    	      htmldata="";
	    	     Node comp = compMailNode.getNode(arrlist.get(j).toString());

	    	     String compcreator=comp.getNode("BasicInfo").getProperty("creatorEmailId").getString(); // basicinfo node and get creatorEmailId
	    	     
	    	     String cout= String.valueOf(comp.getNode("Rfq").getProperty("rfqcount").getLong());
	    	     NodeIterator it=comp.getNode("Rfq").getNode(cout).getNodes();
	    	     //int co=0;
	    	     while (it.hasNext()) {
	    	      
	    	      Node tempNode=it.nextNode();
	    	       pid1 =tempNode.getProperty("productid").getString();

	    	          pname1=tempNode.getProperty("productName").getString();

	    	          pquantity1=tempNode.getProperty("productpQuantity").getString();

	    	       pdescription1=tempNode.getProperty("productDescription").getString();

	    	       /*if(tempNode.hasNode("attachment")&&tempNode.getNode("attachment").hasNodes()){
	    	        NodeIterator itr =tempNode.getNode("attachment").getNodes(); 
	    	        while(itr.hasNext())
	    	        {
	    	         attachment1[co]=itr.nextNode().getProperty("docpath").getString();
	    	         break;
	    	        }}*/
	    	       //co++;
	    	 htmldata=htmldata+"<p>Product Name : <strong>"+pname1+"</strong></p><p>Quantity : "+pquantity1+"</p><p>Buyer Description : "+pdescription1+"</p><p>Product Link : <a href='http://prod.bizlem.io:8082/portal/servlet/service/productselection.brief?pid="+pid1+"'>http://prod.bizlem.io:8082/portal/servlet/service/productselection.brief?pid="+pid1+"</a></p>"; 
	    	     
	    	     }
	    	     
	    	htmlText="<html><body><table width='100%' border='0' cellspacing='0' cellpadding='0' style='border-radius:0px '0px  10px 10px; border:1px solid #4096EE; background-color:#ffffff;'><tr><td align='center' valign='top'><table width='100%' border='0' align='center' cellpadding='5' cellspacing='5' style='border-top:6px solid #4096EE;'><tr><td align='left' valign='middle' style='font-family:Arial, Helvetica, sans-serif; color:#4e4e4e; font-size:13px; padding-right:10px;'><div style='font-size:24px; color:#4096EE;'>Dear "+compcreator+", </div><p>You have recieved a new frq for following Products</p><br/>"+htmldata+"<tr><td align='left' valign='middle' style='font-family:Arial, Helvetica, sans-serif; color:#4e4e4e; font-size:13px;'></tr><tr><td align='left' valign='middle' style='font-family:Arial, Helvetica, sans-serif; color:#333; font-size:13px;'><span style='color:#333; font-size:12px; font-family:Arial, Helvetica, sans-serif;'>With Regards,<br /><strong>Administrator</strong></span></tr></table></td></tr></table></body></html>";

	    	String[] checkparamKey = {"email","msg"};
	    	String[] checkparamValue = {compcreator,htmlText};
	    	 callPostService("http://prod.bizlem.io:8082/portal/servlet/service/productselection.sendMailRfqSeller", checkparamKey, checkparamValue);
	    	   
	    	     //}
	    	    }
			
			if (iterator.getSize() <= 0) {
				Node admin = session.getRootNode().getNode("content")
						.getNode("user").getNode("admin");
				if (admin.hasNode("buyingrequest")) {
				
						Node tempCompNode3 = admin
								.getNode("buyingrequest");
						String nodeValue = tempCompNode3.getProperty(
								"nodeCount").getString();
						tempCompNode3.setProperty("nodeCount",
								Integer.parseInt(nodeValue) + 1);
						
						tempCompNode3 = tempCompNode3
								.addNode(nodeValue);
						
						tempCompNode3.setProperty("name",
								request.getParameter("prdname"));
						tempCompNode3.setProperty("estquantity",
								request.getParameter("ordquant"));
						tempCompNode3.setProperty("piece",
								request.getParameter("piece"));
						tempCompNode3.setProperty("detail",
								request.getParameter("detailSpec"));
						
						tempCompNode3.setProperty("image",
								request.getParameter("prdimg"));
						tempCompNode3.setProperty("cat",
								request.getParameter("prdcat"));
						Node user=session.getRootNode().getNode("content").getNode("user").getNode(request.getRemoteUser().replace("@", "_"));
						tempCompNode3.setProperty("sendername", user.getProperty("name").getString());
						tempCompNode3.setProperty("senderemail", request.getRemoteUser().replace("@", "_"));
						
				} else {

					Node tempCompNode3 = admin
							.addNode("buyingrequest");

					tempCompNode3.setProperty("nodeCount",
							Integer.parseInt("0"));
					
					tempCompNode3 = tempCompNode3
							.addNode("0");
					
					tempCompNode3.setProperty("name",
							request.getParameter("prdname"));
					tempCompNode3.setProperty("estquantity",
							request.getParameter("ordquant"));
					tempCompNode3.setProperty("piece",
							request.getParameter("piece"));
					tempCompNode3.setProperty("detail",
							request.getParameter("detailSpec"));
					
					tempCompNode3.setProperty("image",
							request.getParameter("prdimg"));
					tempCompNode3.setProperty("cat",
							request.getParameter("prdcat"));
					Node user=session.getRootNode().getNode("content").getNode("user").getNode(request.getRemoteUser().replace("@", "_"));
					tempCompNode3.setProperty("sendername", user.getProperty("name").getString());
					tempCompNode3.setProperty("senderemail", request.getRemoteUser().replace("@", "_"));
				}
			}
			// status = String.valueOf(suppCount);
			status = "true";
			session.save();
		} catch (Exception e) {
			// e.printStackTrace();
			return e.getMessage();
		}
		return status;

	}

	public String getSupplrData(String prodName) {

		Session session;
		String status = "0";
		Node prodNode, compNode, tempPrdNode, tempCompNode;
		try {

			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			prodNode = session.getRootNode()
					.getNode("content/product/products");
			compNode = session.getRootNode().getNode("content/company");

			String querryStr = "select * from [nt:unstructured] where  ISDESCENDANTNODE('/content/product/products/') and name  like '"
					+ prodName + "%'";
			Workspace workspace = session.getWorkspace();
			Query query = workspace.getQueryManager().createQuery(querryStr,
					Query.JCR_SQL2);
			QueryResult result = query.execute();
			NodeIterator iterator = result.getNodes();
			int suppCount = 0;
			while (iterator.hasNext()) {
				tempPrdNode = iterator.nextNode();
				String prodNodeVal = tempPrdNode.getName();
				if (compNode.hasNodes()) {
					NodeIterator iterator1 = compNode.getNodes();
					while (iterator1.hasNext()) {
						tempCompNode = iterator1.nextNode();
						if (tempCompNode.hasNode("product")
								&& tempCompNode.getNode("product").hasNode(
										prodNodeVal)) {
							suppCount++;
						}
					}
				}
				// suppCount++;
			}
			status = String.valueOf(suppCount);

			session.save();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return status;

	}

	public String getPincodeData(String pincode) {
		Session session;
		boolean status = false;
		Node pinNode, pinNode1, companyNode, country, state, district, citycode;
		JSONObject json = new JSONObject();
		try {
			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			pinNode = session.getRootNode().getNode("content/pincode");

			if (pinNode.hasNode(pincode)) {
				pinNode1 = pinNode.getNode(pincode);
				companyNode = session.getRootNode().getNode("content/address");
				if (pinNode1.hasProperty("countrycode")
						&& pinNode1.hasProperty("statecode")
						&& pinNode1.hasProperty("districtcode")
						&& pinNode1.hasProperty("citycode")) {
					// json.accumulate("countrycode",
					// pinNode1.getProperty("countrycode").getString());
					String strCountryCode = pinNode1.getProperty("countrycode")
							.getString();
					String strSatetCode = pinNode1.getProperty("statecode")
							.getString();
					String strDistrictCode = pinNode1.getProperty(
							"districtcode").getString();
					String strCityCode = pinNode1.getProperty("citycode")
							.getString();
					if (companyNode.hasNode(strCountryCode)) {
						country = companyNode.getNode(strCountryCode);
						if (country.hasNode(strSatetCode)) {
							state = country.getNode(strSatetCode);
							if (state.hasNode(strDistrictCode)) {
								district = state.getNode(strDistrictCode);
								if (district.hasNode(strCityCode)) {
									citycode = district.getNode(strCityCode);
									json.accumulate("country", country
											.getProperty("countryname")
											.getString());
									json.accumulate("state",
											state.getProperty("statename")
													.getString());
									json.accumulate("district", district
											.getProperty("distatename")
											.getString());
									json.accumulate("city", citycode
											.getProperty("cityname")
											.getString());
									json.accumulate("status", "success");
									return "true" + "~" + json.toString();
								}
							}
						}

					}
				}

			}

		} catch (Exception e) {
			return null;
		}
		return "false" + "~" + "false";
	}
	
	public String callPostService(String urlStr, String[] paramName,
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

}
