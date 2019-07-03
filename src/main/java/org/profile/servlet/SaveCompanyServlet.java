package org.profile.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.ActiveMQProducer;
import org.profile.service.CompanyService;
import org.profile.service.ProfileService;
import org.profile.service.SecurityService;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Save Company Servlet"),
        @Property(name = "service.vendor", value = "VISL Company"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/company/save" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "com", "pom",
                "career", "joinReason", "deleteCareer", "mapCompany",
                "mapUser", "mapCompanyType", "mapUserType", "up", "mapProduct",
                "mapProductType", "request", "deleteUserMap", "privacy","deleteCompany" }) })
@SuppressWarnings("serial")
public class SaveCompanyServlet extends SlingAllMethodsServlet {

    @Reference
    private SlingRepository repos;

    @Reference
    private CompanyService service;

    @Reference
    private ProfileService profileService;
    
    @Reference
    private ActiveMQProducer objActiveMQProducer;

    @Reference
    SecurityService securityService;

    private ResourceBundle bundle;
    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
    	 if (request.getRequestPathInfo().getExtension().equals("savemdmcom")) {
    		 String splits = "";
    		 splits= service.saveCompanyNodeMdmService(request);
    		 String split[]=splits.split(",");
    		 String companyName = request.getParameter("companyName");
 			while(companyName.indexOf("+") != -1){
 				companyName = companyName.replace("+", " ");
 			}
    		 bundle = ResourceBundle.getBundle("server");
    		 String message = MessageFormat.format(
                     bundle.getString("sendMail.com.body"), companyName,split[2]+"&com="+split[1]);
     		
             String[] paramName = { "emailto[]", "emailfrom[]",
                     "emailsub[]", "emailmsg[]" };
             String[] paramValue = {request.getParameter("adminmailid"),
                     bundle.getString("sendMail.com.from"),
                     bundle.getString("sendMail.com.subject"), message };
             securityService.callService(bundle.getString("sendMail.address"), paramName, paramValue);
             response.getOutputStream().print("/content/company/"+split[1]);
    	 }else if (request.getRequestPathInfo().getExtension().equals("com")) {
        	String splits = "";
        	if(request.getParameter("companyNo").equals("new")){
        	  splits= service.saveCompanyNodeNew(request);
        	}else {
        		 splits= service.saveCompanyNode(request);
        	}
        	 String split[]=splits.split(",");
//        	 if (!request.getParameterValues("empName")[0].equals("")) {
//        	 response.getOutputStream().println("emp name res if ---"+request.getParameterValues("empName")[0]);
//        	 }else{
//        		 response.getOutputStream().println("emp name res else ---"+request.getParameterValues("empName")[0]);
//        	 }
        	 response.getOutputStream().println("split response--- !"+splits+"--------"+split[0]);
        	 if(split[0].equals("true")){
       	 String f=service.saveImage(request, split[1]);
       	response.getOutputStream().println("f--------------"+f);
       	 if(f.equals("success")){
       		Node rootNode = null;
       		Node userNode = null;
    		Session session;
       		try {
       			if(request.getParameter("companyNo").equals("new")){
    			session = repos.login(new SimpleCredentials("admin", "admin"
    					.toCharArray()));
    			//
    			

    			if (!session.getRootNode().hasNode("content")) {

    				session.getRootNode().addNode("content");

    			}
    			
    			if (session.getRootNode().getNode("content").hasNode("user")) {
    				rootNode = session.getRootNode().getNode("content")
    						.getNode("user");
    						
    			}
    			if(rootNode.hasNode(request.getRemoteUser().replace("@","_"))){
    				userNode = rootNode.getNode(request.getRemoteUser().replace("@","_"));
    			}
    	
    		
 
       		Map<String, String> propertyMap = new HashMap<String, String>();
    		propertyMap.put("firstname", userNode.getProperty("name").getString());
    		if(userNode.hasProperty("lastName")){
    		propertyMap.put("lastname", userNode.getProperty("lastName").getString());
    		}else{
    			propertyMap.put("lastname", "");
    		}
    		propertyMap.put("company", request.getParameter("companyName"));
    		propertyMap.put("number", userNode.getProperty("primaryMobile").getString());
    		propertyMap.put("weburl", request.getParameter("website"));
    		propertyMap.put("email", request.getRemoteUser());
    		objActiveMQProducer.producerCall("Vtiger", propertyMap, "");
    		bundle = ResourceBundle.getBundle("server");
    		String message = MessageFormat.format(
                    bundle.getString("sendMail.com.body"), request.getParameter("companyName"),split[2]+"&com="+split[1]);
    		
            String[] paramName = { "emailto[]", "emailfrom[]",
                    "emailsub[]", "emailmsg[]" };
            String[] paramValue = {request.getParameter("adminmailid"),
                    bundle.getString("sendMail.com.from"),
                    bundle.getString("sendMail.com.subject"), message };
            securityService.callService(bundle.getString("sendMail.address"), paramName, paramValue);
       		response.sendRedirect("/portal/servlet/company/show.view?compN="+split[1]);
       		}else {
       			if(request.getParameter("buttonVal").equals("Submit")){
       			response.sendRedirect("/portal/servlet/company/show.view?compN="+split[1]);
       			}else{
       				response.sendRedirect("/portal/servlet/company/show.com?id="+split[1]+"&tab="+request.getParameter("tabIdN"));	
       			}
       		}
       		} catch (PathNotFoundException e) {
    			//e.printStackTrace();
       			response.getOutputStream().println("false" + e.getMessage());
    		} catch (RepositoryException e) {
    			//e.printStackTrace();
    			response.getOutputStream().println("false" + e.getMessage());
    		}catch (Exception e) {
    			//e.printStackTrace();
    			response.getOutputStream().println("false" + e.getMessage());
    		}
       	 } else{	 
    		 response.getOutputStream().println("Sorry we are facing some issue currently ,please try laterss !"+f);
        }
       	 }else{
    		 response.getOutputStream().println("Sorry we are facing some issue currently ,please try later !"+split); 
        	 }
        	 
         
   
        } else if (request.getRequestPathInfo().getExtension().equals("career")) {
            service.saveCompanyCareer(request);
            response.sendRedirect(request.getContextPath()
                    + "/servlet/company/show.showCareer?compN="
                    + request.getParameter("companyJobName"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("joinReason")) {
            service.joiReasonCareer(request);
            response.sendRedirect(request.getContextPath()
                    + "/servlet/company/show.showCareer?compN="
                    + request.getParameter("reasonCompanyName"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteCareer")) {
            service.deleteCompanyCareer(request);
            response.sendRedirect(request.getContextPath()
                    + "/content/company/show.showCareer?compN="
                    + request.getParameter("companyJobName"));

            /*
             * request.getRequestDispatcher("/content/company/"+request.getParameter
             * ("compN")+".showCareer") .forward(request, response);
             */
        } else if (request.getRequestPathInfo().getExtension()
                .equals("mapCompany")) {
            service.mapCompany(request);
            response.sendRedirect(request.getContextPath()
                    + "/servlet/company/show.view?compN="
                    + request.getParameter("mapCompanyName").replaceAll("\\s+",
                            ""));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("mapUser")) {
            service.mapUser(request, false);
            String[] personValue = request.getParameter("mapCompanyPerson")
                    .split(",");
            for (String person : personValue) {
                System.out.println(person);

                profileService.mapCompany(person,
                        request.getParameter("mappingUserType"), null, "add",
                        request.getParameter("mapCompanyName"), "", false);
            }
            response.sendRedirect(request.getContextPath()
                    + "/servlet/company/show.view?compN="
                    + request.getParameter("mapCompanyName").replaceAll("\\s+",
                            ""));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("mapCompanyType")) {
            service.mapCompanyType(request);
            PrintWriter out = response.getWriter();
            out.println("<HTML>");
            out.println("<HEAD>");
            out.println("<SCRIPT>");
            out.println("window.close();");
            out.println("</SCRIPT>");
            out.println("</HEAD>");
            out.println("</HTML>");
        } else if (request.getRequestPathInfo().getExtension()
                .equals("mapUserType")) {
            service.mapUserType(request);
            PrintWriter out = response.getWriter();
            out.println("<HTML>");
            out.println("<HEAD>");
            out.println("<SCRIPT>");
            out.println("window.close();");
            out.println("</SCRIPT>");
            out.println("</HEAD>");
            out.println("</HTML>");

        } else if (request.getRequestPathInfo().getExtension()
                .equals("mapProductType")) {
            service.mapProductType(request);
            PrintWriter out = response.getWriter();
            out.println("<HTML>");
            out.println("<HEAD>");
            out.println("<SCRIPT>");
            out.println("window.close();");
            out.println("</SCRIPT>");
            out.println("</HEAD>");
            out.println("</HTML>");

        } else if (request.getRequestPathInfo().getExtension().equals("up")) {
            service.uploadCompanyLogo(request);
            response.sendRedirect(request.getContextPath()
                    + "/servlet/company/show.view?compN="
                    + request.getParameter("compN").replaceAll("\\s+", ""));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("mapProduct")) {
            String[] splitValue = request.getParameter("productValues").split(
                    ",");
            service.addTypes(request.getParameter("nodeName"),
                    request.getParameter("productCompanyName"), splitValue,
                    "Company");
            response.sendRedirect(request.getContextPath()
                    + "/servlet/company/show.view?compN="
                    + request.getParameter("productCompanyName").replaceAll(
                            "\\s+", ""));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("request")) {
            String companyName = request.getParameter("mapCompanyName");
            String mapType = request.getParameter("mappingUserType");
            String userId = request.getParameter("mapCompanyPerson");
            String[] values = {};
            if (request.getParameter("request").equals("accept")) {
                service.mapUser(request, false);
                profileService.mapCompany(userId, mapType, values, "true",
                        companyName, "", false);
            } else if (request.getParameter("request").equals("cancel")) {
                profileService.mapCompany(userId, mapType, values, "true",
                        companyName, "cancel", false);
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteUserMap")) {
            String[] values = {};
            service.deleteMapping(request.getParameter("nodeName"));
            if (request.getParameter("reject").equals("false")) {
                profileService.mapCompany(request.getParameter("userId"),
                        request.getParameter("mapType"), values, "true",
                        request.getParameter("companyName"), "delete", false);
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("privacy")) {
            service.setPrivacy(request);
            response.sendRedirect(request.getContextPath()
                    + "/servlet/company/show.privacy?company="
                    + request.getParameter("company"));
        }else if(request.getRequestPathInfo().getExtension()
                .equals("deleteCompany")){
        	boolean status=service.deleteCompany(request.getParameter("company"));
        	if(status){
        		PrintWriter out = response.getWriter();
            	out.print("true");            	
        	}else{
        		PrintWriter out = response.getWriter();
            	out.print("false");
            		
        	}
        	
        }else if (request.getRequestPathInfo().getExtension()
                .equals("comptoEvent")) {
           String res= service.setParticipation(request);
           PrintWriter out = response.getWriter();
       		out.print(res);
        }

    }
}
