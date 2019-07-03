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
import org.profile.service.ProfileService;
import com.loginService.dwr.LoginService;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Group Service"),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/session/status" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "login",
                "logout","msg" }) })
@SuppressWarnings("serial")
public class LoginDWRServlet extends SlingAllMethodsServlet {

    @Reference
    private ProfileService profile_service;
    
    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        LoginService login_service = new LoginService();
        if (request.getRequestPathInfo().getExtension().equals("login")) {
            login_service.login(request.getParameter("userId"));
            String value = profile_service
                    .getServiceParam(request.getParameter("param"), 
                            request.getParameter("userId"));
            request.getSession(true).setAttribute("lll",request.getRemoteUser());
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(value);
            
        } else if (request.getRequestPathInfo().getExtension().equals("logout")) {
            login_service.logout(request.getParameter("userId"));
        }
       
    }
    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
    	request.getSession(true).setAttribute("lll",request.getRemoteUser());
        if (request.getRequestPathInfo().getExtension().equals("msg")) {
            ResourceBundle bundle;
            bundle=ResourceBundle.getBundle("server");
          //  SocialMessagingService msg=new SocialMessagingService();
           // msg.socialMessage(request.getParameter("userId"));
            response.getOutputStream().print(bundle.getString("sling.serverSpec"));
        } else if (request.getRequestPathInfo().getExtension().equals("login")) {
            LoginService login_service = new LoginService();
            login_service.login(request.getParameter("userId"));
            String value = profile_service
                    .getServiceParam(request.getParameter("param"), 
                            request.getParameter("userId"));
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(value);
        }
        
    }

}
