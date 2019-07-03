package org.profile.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.profile.service.CompanyService;
import org.profile.service.ProfileService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.InputStream;


@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Save Profile"),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "sling.servlet.paths", value = { "/content/user/save" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "pro", "edu",
                "su", "ex", "info", "up", "visit", "deleteEdu", "deleteEx",
                "deleteEmail", "deleteMobile", "mapCompany", "deleteIM" }) })
@SuppressWarnings("serial")
public class SaveProfileServlet extends SlingAllMethodsServlet {

    @Reference(referenceInterface = ProfileService.class, name = "service")
    private ProfileService service;

    @Reference
    private CompanyService companyService;

    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
    	PrintWriter o = response.getWriter();
        if (request.getRequestPathInfo().getExtension().equals("pro")) {
        	 //String[] emailValue = request.getParameterValues("email");
             
            
        	service.saveProfileNode(request);
        	
        	try{
    			  
       		 String userId = request.getParameter("title");
       		 userId = userId.replaceAll("_", "@");
 		   InputStream inputXml = null;
    	String	url = "http://prod.bizlem.io:8180/UserValidation/services/UserValidation/assignUserOpenfire?assignedPerson="+userId;
// 			customerDeatilList = new ArrayList<HashMap<String, String>>();
 			inputXml = new URL(url).openConnection().getInputStream();

 			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
 					.newInstance();
 			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
 			Document doc = dBuilder.parse(inputXml);
 			doc.getDocumentElement().normalize();
 			NodeList nList1 = doc.getElementsByTagName("ns:assignUserOpenfireResponse");
 	        org.w3c.dom.Node nNode = nList1.item(0);
 	       Element eElement = (Element) nNode;
// 	        System.out.println(""+eElement.getElementsByTagName("ns:return").item(0).getTextContent());
 	String openfireresult = eElement.getElementsByTagName("ns:return").item(0).getFirstChild().getNodeValue();


 		 } 
 		 catch(Exception e){}
        	
        	response.sendRedirect(request.getContextPath()
                    + "/content/user/user.profileEdit?id="
                    + request.getRemoteUser().replace("@", "_"));
            // response.getOutputStream().print(service.saveProfileNode(request));
           // response.sendRedirect(request.getContextPath()+"/content/user/info?id="
                    //+ request.getParameter("title"));
            //response.getWriter().print(emailValue.length);
        }else if (request.getRequestPathInfo().getExtension().equals("basic")) {
       	 //String[] emailValue = request.getParameterValues("email");
            
            
       	service.saveProfileNodeBasic(request);
       	response.sendRedirect(request.getContextPath()
                   + "/content/user/user.profileEdit?id="+ request.getRemoteUser().replace("@", "_")+"&tab=2");
           // response.getOutputStream().print(service.saveProfileNode(request));
          // response.sendRedirect(request.getContextPath()+"/content/user/info?id="
                   //+ request.getParameter("title"));
           //response.getWriter().print(emailValue.length);
       } else if (request.getRequestPathInfo().getExtension().equals("add")) {
       	 //String[] emailValue = request.getParameterValues("email");
            
            
       	service.saveProfileAddNode(request);
       	response.sendRedirect(request.getContextPath()
                   + "/content/user/info?id="
                   + request.getRemoteUser().replace("@", "_"));
           // response.getOutputStream().print(service.saveProfileNode(request));
          // response.sendRedirect(request.getContextPath()+"/content/user/info?id="
                   //+ request.getParameter("title"));
           //response.getWriter().print(emailValue.length);
       } else if (request.getRequestPathInfo().getExtension().equals("su")) {

            service.saveSummaryNode(request);
            //response.sendRedirect(request.getContextPath()
              //      + "/content/user/info?id="
                //    + request.getParameter("redirect"));
            response.sendRedirect(request.getContextPath()
                    + "/content/user/user.profileEdit?id="
                    + request.getRemoteUser().replace("@", "_")+"&tab=4");
            
        } else if (request.getRequestPathInfo().getExtension().equals("edu")) {

            service.saveEducationNode(request);
            //response.sendRedirect(request.getContextPath()
              //      + "/content/user/info?id="
                //    + request.getParameter("redirect"));
            response.sendRedirect(request.getContextPath()
                    + "/content/user/user.profileEdit?id="
                    + request.getRemoteUser().replace("@", "_")+"&tab=3");
        } else if (request.getRequestPathInfo().getExtension().equals("info")) {

            service.saveInfoNode(request);
            response.sendRedirect(request.getContextPath()
                    + "/content/user/info?id="
                    + request.getParameter("redirect"));
        } else if (request.getRequestPathInfo().getExtension().equals("ex")) {

            service.saveExperienceNode(request);
            //response.sendRedirect(request.getContextPath()
              //      + "/content/user/info?id="
                //    + request.getParameter("redirect"));
            
            response.sendRedirect(request.getContextPath()
                    + "/content/user/user.profileEdit?id="
                    + request.getRemoteUser().replace("@", "_")+"&tab=5");
        } else if (request.getRequestPathInfo().getExtension().equals("up")) {

            service.uploadProfilePic(request);
            response.sendRedirect(request.getContextPath()
                    + "/content/user/info?id=" + request.getParameter("userId"));

        }

        else if (request.getRequestPathInfo().getExtension().equals("visit")) {

            service.profileVisit(request.getParameter("userId"),
                    request.getParameter("visitor"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteEdu")) {

            service.profileEducationDelete(request);
            response.sendRedirect(request.getContextPath()
                    + "/content/user/info?id="
                    + request.getParameter("redirect"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteEx")) {

            service.profileExperienceDelete(request);
            response.sendRedirect(request.getContextPath()
                    + "/content/user/info?id="
                    + request.getParameter("redirect"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteEmail")) {

            service.profileEmailNMobileDelete(request.getParameter("userId"),
                    "EmailID", request.getParameter("deleteNode"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteMobile")) {

            service.profileEmailNMobileDelete(request.getParameter("userId"),
                    "Phone", request.getParameter("deleteNode"));

        }else if (request.getRequestPathInfo().getExtension()
                .equals("deleteIM")) {

            service.profileEmailNMobileDelete(request.getParameter("userId"),
                    "IM", request.getParameter("deleteNode"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteMobile")) {

            service.profileEmailNMobileDelete(request.getParameter("userId"),
                    "Phone", request.getParameter("deleteNode"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("addType")) {
            String[] splitValue = request.getParameter("productValues").split(
                    ",");
            companyService
                    .addTypes(request.getParameter("nodeName"),
                            request.getParameter("productUserName"),
                            splitValue, "User");
            response.sendRedirect(request.getContextPath()
                    + "/content/user/info?id="
                    + request.getParameter("productUserName"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("mapCompany")) {

            service.mapCompany(request.getParameter("userId"),
                    request.getParameter("mapType"),
                    request.getParameterValues("companyValues"), "false", "",
                    "", Boolean.parseBoolean(request.getParameter("validate")));
            response.sendRedirect(request.getContextPath()
                    + "/content/user/info?id=" + request.getParameter("userId"));
        } else if (request.getRequestPathInfo().getExtension().equals("delete")) {
            companyService.deleteMapping(request.getParameter("nodeName"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteCompanyMap")) {
            companyService.deleteMapping(request.getParameter("nodeName"));
            if (request.getParameter("verified").equals("true")) {
                companyService.mapUser(request, true);
            } else if (request.getParameter("verified").equals("pending")) {
                service.mapCompanyRequest(
                        request.getParameter("mapCompanyName"),
                        request.getParameter("mappingUserType"),
                        request.getParameter("userId"), true);
            }
        } else if (request.getRequestPathInfo().getExtension().equals("setUserStatus")) {
            //service.setUserStatus("pran3bull_gmail.com", request.getParameter("userStatus")); HardCOded Email wrong chhages are below
        	service.setUserStatus(request.getParameter("userid").replace("@","_"), request.getParameter("userStatus"));

            o.print(request.getParameter("userid").replace("@","_"));
        }
        
        

    }

}
