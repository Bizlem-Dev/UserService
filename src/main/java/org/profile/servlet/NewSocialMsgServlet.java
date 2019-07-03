package org.profile.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
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
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.FacebookService;
import org.profile.service.LinkedInService;
import org.profile.service.SocialMessagesService;
import org.profile.service.TwitterService;



import com.loginService.dwr.SocialMessagingService;

/**
 * <code>SocialMessagesServlet</code> contains all the request and
 * response related to SocialMessaging section.
 */
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Social Messages "),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "sling.servlet.paths",
                            value = { "/servlet/social/mesg" }),
        @Property(name = "sling.servlet.resourceTypes",
                            value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "view", "check",
                "add", "follow", "addMessage", "favourite", "forward",
                "unfollow", "socialMessage", "following", "follower",
                "favourite", "message", "sendMail", "oauthSet", "setTwitterId",
                "getTweets", "setTweets","tagSearch","searchResult","randomUser" })

})
@SuppressWarnings("serial")
public class NewSocialMsgServlet extends SlingAllMethodsServlet {

    /** Reference to <code>SocialMessagesService</code>. */
    @Reference
    private SocialMessagesService service;

    /** Reference to <code>TwitterService</code>. */
    @Reference
    private LinkedInService lik_service;
    
    @Reference
    private TwitterService twitter_service;
    @Reference
    private FacebookService fb_service;
    

    /** Reference to <code>SlingRepository</code>. */
    @Reference
    private SlingRepository repos;

    
    
    @Override
    protected final void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException,
            IOException {
 if (request.getRequestPathInfo().getExtension()
                .equals("oauthSet")) {
            String[] result = twitter_service.getVerifier();
            /* request.setAttribute("token", result[0] + ""); */
            request.getSession(true).setAttribute("twitterSecret", result[1]);
            /*
             * request.setAttribute("secret", result[1]);
             * request.setAttribute("redirectUrl", result[2]);
             */
            response.sendRedirect(result[2]);
            /*
             * request.getRequestDispatcher(
             * "/content/socialMessages/*.oauthTwitter").forward(request,
             * response);
             */
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getTweets")) {
        	
            String[] value = service.getTwitterCred(request
                    .getParameter("userId"),"Twitter");
            if(value[0] == "error"){
            	response.getOutputStream().print("false");
            }else {
            ArrayList<Map<String, String>> tweets = twitter_service.getTweets(
                    value[0], value[1]);
            request.setAttribute("tweets", tweets);
            request.getRequestDispatcher(
                    "/content/user/*.twitterExtract").forward(request,
                    response);
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("setTwitterId")) {
            String[] result = twitter_service
                    .getToken(
                            request.getParameter("oauth_verifier"),
                            request.getParameter("oauth_token"),
                            request.getSession(true).getAttribute(
                                    "twitterSecret")
                                    + "");
            request.getSession(true).removeAttribute("twitterSecret");
            if (result[2].equals("success")) {
                String[] response_body = twitter_service.getAuth(result[0],
                        result[1]);
                if (response_body[3].equals("success")) {
                    int p = service.setTwitterCred(request.getRemoteUser()
                            .replaceAll("@", "_"), response_body[0],
                            response_body[1], response_body[2],
                            response_body[4]);
                    if (p == 1) {
                        response.getOutputStream()
                                .print("<html><head><script>window.close();"
                                        + "</script></head></html>");
                    } else {
                        response.getOutputStream().print("p error");
                    }
                } else {
                    response.getOutputStream().print(" response body error");
                }
            } else {
                response.getOutputStream().print("result error");
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("Linkedin")) {
        	 
        	String s[] = lik_service.getVerifier();
        	request.getSession(true).setAttribute("secret", s[1]);
        	 response.sendRedirect(s[2]);

        }
        else if (request.getRequestPathInfo().getExtension()
                .equals("setLinkedinCodes")) {
        	String[] result = lik_service.getToken(
                    request.getParameter("oauth_verifier"),
                    request.getParameter("oauth_token"),
                    request.getSession(true).getAttribute(
                            "secret")
                    + "");
        	 String userId = request.getRemoteUser().replace("@", "_");
        	lik_service.setRequestToken(result[0],
                   result[1], userId, "Linkedin", "setToken", "");
        	 response.getOutputStream().print("<html><head><script>window.close();</script></head></html>");

        }else if (request.getRequestPathInfo().getExtension()
                .equals("getLinkedinCodes")) {
        	
        	String userId = request.getRemoteUser().replace("@", "_");
        	
        	try{Session session = repos.login(new SimpleCredentials("admin", "admin"
                         .toCharArray()));
               Node g= session.getRootNode().getNode("content").getNode("user").getNode(userId);
               if(g.hasNode("NetworkProvider") && g.getNode("NetworkProvider").hasNode("Linkedin")){            	
            	   String t=g.getNode("NetworkProvider").getNode("Linkedin").getProperty("token").getString();
            	   String c=g.getNode("NetworkProvider").getNode("Linkedin").getProperty("secret").getString();
            	   String []res= lik_service.getAuth(t,c);
            	  	
            	  request.setAttribute("linked",res);
            	  	 request.getRequestDispatcher(
                             "/content/user/*.linkedExtract").forward(request,
                             response);
               }else{
            	   response.getOutputStream().print("false");
            	   
               }
        	}catch(Exception e){
        		
        	}	
        }else if (request.getRequestPathInfo().getExtension()
                .equals("facebook")) {
        	 
        	String url = fb_service.getFbAuthUrl();
            response.sendRedirect(url);

        } else if (request.getRequestPathInfo().getExtension()
                .equals("setFacebookToken")) {
        	
        	String[] value = fb_service.getFbAccessToken((request
                    .getParameter("code")));
            String userId = request.getRemoteUser().replace("@", "_");
            fb_service.setRequestToken(value[0],value[1], userId, "Facebook", "setToken", "");
            response.getOutputStream().print("<html><head><script>window.close();</script></head></html>");

        }else if (request.getRequestPathInfo().getExtension()
                .equals("getFacebookCodes")) {
        	
        	String userId = request.getRemoteUser().replace("@", "_");
        	
        	try{Session session = repos.login(new SimpleCredentials("admin", "admin"
                         .toCharArray()));
               Node g= session.getRootNode().getNode("content").getNode("user").getNode(userId);
               if(g.hasNode("NetworkProvider") && g.getNode("NetworkProvider").hasNode("Facebook")){            	
            	   String t=g.getNode("NetworkProvider").getNode("Facebook").getProperty("token").getString();
            	   String c=g.getNode("NetworkProvider").getNode("Facebook").getProperty("secret").getString();
            	   String []res= fb_service.getFbAuth(t, c);
            	  	
            	  request.setAttribute("fb",res);
            	  	 request.getRequestDispatcher(
                             "/content/user/*.FbExtract").forward(request,
                             response);
               }else{
            	   response.getOutputStream().print("false");
            	   
               }
        	}catch(Exception e){
        		
        	}	
        }else if (request.getRequestPathInfo().getExtension()
                .equals("blog")) {
        	
            request.getRequestDispatcher(
                    "/content/user/.blogiframe").forward(request,
                    response);
            }else if (request.getRequestPathInfo().getExtension()
                    .equals("wiki")) {
            	
                request.getRequestDispatcher(
                        "/content/user/.wikiiframe").forward(request,
                        response);
                }
        

        
    }

}
