package org.profile.servlet;

import java.io.IOException;
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
import org.profile.service.SecurityService;
import org.profile.service.UserMailService;
import javax.jcr.SimpleCredentials;
import org.apache.sling.jcr.api.SlingRepository;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Verification Service"),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/security/verify" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = {
                "verifyTokenMobile", "sendToken", "verifyToken", "privacy",
                "save", "get", "sendSMS", "sendMail", "checkCred",
                "saveMailCred", "mailNumb", "getMailNumb", "validateEmail" }) })
@SuppressWarnings("serial")
public class SecurityServlet extends SlingAllMethodsServlet {

    @Reference
    SecurityService service;

    private ResourceBundle bundle;

    @Reference
    private UserMailService mail_service;
    
    @Reference
    private SlingRepository repos;

    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {

        if (request.getRequestPathInfo().getExtension().equals("sendToken")) {
            service.sendToken(request.getParameter("userId"),
                    request.getParameter("verification"),
                    request.getParameter("flag"),
                    request.getParameter("verificationFor"),
                    request.getParameter("userName"));
            //response.getOutputStream().print("Valid---"+request.getParameter("userId"));
        }else if (request.getRequestPathInfo().getExtension().equals("sendNotificationReminder")) {
            service.sendNotificationReminder(request.getParameter("userId"),
                    request.getParameter("verification"),
                    request.getParameter("flag"),
                    request.getParameter("verificationFor"),
                    request.getParameter("userName"),request.getParameter("htmltext"));
            //return request.getParameter("userId");
        } else if (request.getRequestPathInfo().getExtension()
                .equals("verifyTokenMobile")) {

            String code = service.verifyToken(request.getParameter("userId"),
                    request.getParameter("number"),
                    request.getParameter("token"), "Phone");
            if (code.equals("Invalid")) {
                response.getOutputStream().print("Invalid Code");
            } else if (code.equals("Valid")) {
                response.getOutputStream().print("Valid");
            }

        } else if (request.getRequestPathInfo().getExtension().equals("save")) {
            service.setPrivacy(request,response);
            response.sendRedirect(request.getContextPath()
                    + "/servlet/security/verify.privacy?userId="
                    + request.getRemoteUser().replace("@", "_"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("sendSMS")) {
            response.getOutputStream().print(
                    service.sendUserSMS(request.getParameter("userId"),
                            request.getParameter("message")));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("sendMail")) {
            response.getOutputStream().print(
                    service.sendUserEmail(request.getParameter("emailTo"),
                            request.getParameter("message"),
                            request.getParameter("subject"),request.getParameter("emailFrom")));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("checkCred")) {
            String result_value = "";
            if (request.getParameter("host").equals("Gmail")) {
                result_value = mail_service.getAuthenticationIMAP(
                        request.getParameter("userName"),
                        request.getParameter("password"), "pop.gmail.com");
            } else if (request.getParameter("host").equals("Yahoo")) {
                result_value = mail_service.getAuthenticationPOP(
                        request.getParameter("userName"),
                        request.getParameter("password"), "pop.mail.yahoo.com");
            } else if (request.getParameter("host").equals("Hotmail")) {
                result_value = mail_service.getAuthenticationPOP(
                        request.getParameter("userName"),
                        request.getParameter("password"), "pop3.live.com");
            } else if (request.getParameter("host").equals("SocialAware")) {
                result_value = mail_service.getAuthenticationPOP(
                        request.getParameter("userName"),
                        request.getParameter("password"), "");
            }

            response.getOutputStream().print(result_value);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("saveMailCred")) {
            mail_service.setCredential(request.getParameter("userName"),
                    request.getParameter("password"), request
                            .getParameter("host"), request.getRemoteUser()
                            .replaceAll("@", "_"));
            response.sendRedirect(request.getContextPath()
                    + "/servlet/security/verify.mailNumb?userId="
                    + request.getRemoteUser().replaceAll("@", "_"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getMailNumb")) {
            String mailNum = mail_service.getEmailNumber(request
                    .getParameter("providerId"), request.getRemoteUser()
                    .replaceAll("@", "_"))[0];
            response.getOutputStream().print(mailNum);

        }

    }

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {

        if (request.getRequestPathInfo().getExtension().equals("verifyToken")) {
            bundle = ResourceBundle.getBundle("server");
            // String token=request.getParameter("token");
            // String userId=token.substring(0,token.lastIndexOf("$"));
            /*
             * String
             * emailAddress=request.getParameter("token").substring((request
             * .getParameter("token").lastIndexOf("$")+1),
             * request.getParameter("token").lastIndexOf("*"));
             */
            String userId = service.verifyToken("", "",
                    request.getParameter("token"), "EmailID");

            response.sendRedirect(bundle.getString("rave.portal")
                    + "/portal/profile/getProfile.html?id=" + userId);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("privacy")) {
        	if(request.getRemoteUser()!=null && !request.getRemoteUser().equals("anonymous")){
        		 request.getRequestDispatcher(
                         "/content/user/" + request.getRemoteUser().replace("@", "_")).forward(
                         request, response);
        	}else{
        		response.sendRedirect(request.getContextPath()+"/login");
        	}
           

        } else if (request.getRequestPathInfo().getExtension().equals("get")) {
            String result = service.callGetService(request
                    .getParameter("service"));
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().print(result);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("mailNumb")) {
            /*
             * String gmailNum=mail_service.getEmailNumber("Gmail",
             * "pran3bull_gmail.com")[0]; String
             * yahooNum=mail_service.getEmailNumber("Yahoo",
             * "pran3bull_gmail.com")[0]; String
             * hotmailNum=mail_service.getEmailNumber("Hotmail",
             * "pran3bull_gmail.com")[0]; request.setAttribute("gmailNum",
             * gmailNum); request.setAttribute("yahooNum", yahooNum);
             * request.setAttribute("hotmailNum", hotmailNum);
             */
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".userMail").forward(request, response);

        } else if (request.getRequestPathInfo().getExtension().equals("validateEmail")) {
            service.validateEmailID(request.getRemoteUser(),
                    request.getParameter("email"));
            bundle = ResourceBundle.getBundle("server");
            response.sendRedirect(bundle.getString("rave.portal")
                    + "/portal/profile/getProfile.html?id=" 
                    + request.getRemoteUser().replace("@", "_"));
        } else if (request.getRequestPathInfo().getExtension().equals("validatecom")) {
        	
        	try{javax.jcr.Session session = repos.login(new SimpleCredentials("admin", "admin"
                     .toCharArray()));
        	String token="";
            bundle = ResourceBundle.getBundle("server");
            javax.jcr.Node node=session.getRootNode().getNode("content").getNode("company");
            response.getWriter().println("--------------10000");
            if(node.hasNode(request.getParameter("com"))){
            	response.getWriter().print("--------------");
            	token=node.getNode(request.getParameter("com")).getProperty("token").getString();
            	response.getWriter().println("--------------1"+token);
            	response.getWriter().println("[[[[--------------]]"+request.getParameter("token"));
            	if(token!=null && token.equals(request.getParameter("token"))){
            		
            		node.getNode(request.getParameter("com")).setProperty("token","Yes");
            	}
            }response.getWriter().println("--------------2");
            response.sendRedirect(bundle.getString("viewcom")+request.getParameter("com"));
            session.save();
        }catch(Exception e){
        	response.getWriter().print("Sorry this link is not valid or not available !");
        }
        	}

    }

}
