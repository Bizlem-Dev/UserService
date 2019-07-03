package org.profile.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.profile.service.SocialNetworkService;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Fetching Data From Social Network "),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/social/service" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = {
                "getLinkedinToken", "setLinkedinToken", "getLinkedinData",
                "getFacebookToken", "setFacebookToken", "getFacebookData" })

})
@SuppressWarnings("serial")
public class SocialNetworkServlet extends SlingAllMethodsServlet {

    @Reference
    private SocialNetworkService service;

    private Map<String, String> socialMap = null;

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension()
                .equals("getLinkedinToken")) {
            socialMap = new HashMap<String, String>();
            socialMap = service.getLinkedinToken();
            request.getSession(true).setAttribute("secret",
                    socialMap.get("secret"));
            response.sendRedirect(socialMap.get("requestURL"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("setLinkedinToken")) {
            socialMap = new HashMap<String, String>();

            socialMap = service.authorizeLinkedinToken(
                    request.getParameter("oauth_token"),
                    request.getSession(true).getAttribute("secret") + "",
                    request.getParameter("oauth_verifier"));
            String userId = request.getRemoteUser().replace("@", "_");
            service.setRequestToken(socialMap.get("token"),
                    socialMap.get("secret"), userId, "Linkedin", "setToken", "");
            request.getSession(true).removeAttribute("secret");
            response.getOutputStream().print("<html><head><script>window.close();</script></head></html>");
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getLinkedinData1")) {
            socialMap = new HashMap<String, String>();
            String userId = request.getRemoteUser().replace("@", "_");
            socialMap = service.getLinkedinData(userId);
            response.getOutputStream().print(
                    socialMap.get("contact") + "---"
                            + socialMap.get("linkedinId"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("getFacebookToken")) {
            socialMap = new HashMap<String, String>();
            socialMap = service.getFacebookToken();
            response.sendRedirect(socialMap.get("requestURL"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("setFacebookToken")) {
            socialMap = new HashMap<String, String>();
            //Map<String, String> map = new HashMap<String, String>();
            socialMap = service.authorizeFacebookToken(request
                    .getParameter("code"));
            String userId = request.getRemoteUser().replace("@", "_");
            service.setRequestToken(socialMap.get("token"),
                    socialMap.get("secret"), userId, "Facebook", "setToken", "");
            response.getOutputStream().print("<html><head><script>window.close();</script></head></html>");

        } else if (request.getRequestPathInfo().getExtension()
                .equals("getFacebookData1")) {
            socialMap = new HashMap<String, String>();
            String userId = request.getParameter("userId");
            socialMap = service.getFacebookData(userId, "friend");
            response.getOutputStream().print(
                    socialMap.get("contact") + "---"
                            + socialMap.get("facebookId"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("getTwitterData1")) {
            socialMap = new HashMap<String, String>();
            String userId = request.getParameter("userId");
            socialMap = service.getTwitterData(userId);
            response.getOutputStream().print(
                    socialMap.get("follower_count") + "---"
                            + socialMap.get("following_count"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("socialConnect")) {
            socialMap = new HashMap<String, String>();
            socialMap = service.getLinkedinData(request.getParameter("userId"));
            if(socialMap==null){
                socialMap = new HashMap<String, String>();
                request.setAttribute("linkedin", socialMap);
            }else{
                request.setAttribute("linkedin", socialMap);
            }
            
            socialMap = new HashMap<String, String>();
            socialMap = service.getFacebookData(request.getParameter("userId"),
                    "friend");
            if(socialMap==null){
                socialMap = new HashMap<String, String>();
                request.setAttribute("facebook", socialMap);
            }else{
                request.setAttribute("facebook", socialMap);
            }
            
            socialMap = new HashMap<String, String>();
            socialMap = service.getTwitterData(request.getParameter("userId"));
            if(socialMap==null){
                socialMap = new HashMap<String, String>();
                request.setAttribute("twitter", socialMap);
            }else{
                request.setAttribute("twitter", socialMap);
            }
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".socialConnect").forward(request, response);
        }else if (request.getRequestPathInfo().getExtension()
                .equals("messages")) {
        	 request.getRequestDispatcher(
                     "/content/user/" + request.getParameter("userId").replace("@", "_")
                             + ".messages").forward(request, response);
        }

    }

    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension()
                .equals("getLinkedinData")) {
            socialMap = new HashMap<String, String>();
            String userId = request.getParameter("userId");
            socialMap = service.getLinkedinData(userId);
            // response.getOutputStream().print(socialMap.get("contact")+"---"+socialMap.get("linkedinId"));
            response.getOutputStream().print(
                    "{\"count\":" + socialMap.get("contact")
                            + ",\"linkedinId\":\""
                            + socialMap.get("linkedinId") + "\"}");
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getFacebookData")) {
            socialMap = new HashMap<String, String>();
            String userId = request.getParameter("userId");
            socialMap = service.getFacebookData(userId, "friend");
            response.getOutputStream().print(
                    "{\"count\":" + socialMap.get("contact")
                            + ",\"facebookId\":\""
                            + socialMap.get("facebookId") + "\"}");

        } else if (request.getRequestPathInfo().getExtension()
                .equals("getTwitterData")) {
            socialMap = new HashMap<String, String>();
            String userId = request.getParameter("userId");

            socialMap = service.getTwitterData(userId);
            response.getOutputStream().print(
                    "{\"follower_count\":" + socialMap.get("follower_count")
                            + ",\"twitterId\":\""
                            + socialMap.get("screen_name")
                            + "\",\"following_count\":"
                            + socialMap.get("following_count") + "}");

        }else if (request.getRequestPathInfo().getExtension()
                .equals("sendMailToFriend")) {
                response.getWriter().print(service.MailToFriend(request));
        	
        }else if (request.getRequestPathInfo().getExtension()
                .equals("getmessages")) {
                
        	
        	response.getWriter().print(service.geJsonMessage(request));
        	
        }else if (request.getRequestPathInfo().getExtension()
                .equals("changestate")) {
                
        	
        	response.getWriter().print(service.MailToFriend(request));
        	
        }else if (request.getRequestPathInfo().getExtension()
                .equals("replystate")) {
                
        	
        	response.getWriter().print(service.replayPost(request));
        	
        }
    }

}
