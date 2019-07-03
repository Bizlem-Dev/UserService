package org.profile.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.ActiveMQProducer;
import org.profile.service.FriendService;
import org.profile.service.ProfileService;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Friend "),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/friend/show" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "connect",
                "request", "accept", "show", "visitor", "view", "up",
                "importedContact", "addOther", "friendSuggest", "mutualFriend",
                "activity", "remove", "invite", "search", "friendConnect" })

})
@SuppressWarnings("serial")
public class FriendServlet extends SlingAllMethodsServlet {

    @Reference
    private FriendService service;

    @Reference
    private ProfileService profile_service;

    @Reference
    private SlingRepository repos;

    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension().equals("connect")) {

            service.connectFriend(request.getParameter("userId"),
                    request.getParameter("friend"), "sender", "pending",
                    request.getParameter("friendMessage"),
                    request.getParameter("friendType"));
            service.connectFriend(request.getParameter("friend"),
                    request.getParameter("userId"), "reciever", "pending",
                    request.getParameter("friendMessage"),
                    request.getParameter("friendType"));

        }else if (request.getRequestPathInfo().getExtension().equals("ehcache")) {
			PrintWriter out = response.getWriter(); 
			
			System.out.println("step------- 2.1");
			String page1 = request.getParameter("vpage");
			 
			String el1 = new String(request.getParameter("id"));
			String el2 = new String(page1);
			InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("ehcache.xml");
			CacheManager manager = CacheManager.newInstance(inputStream1);
			//out.print("true");
			Cache cache = manager.getCache("profile");
			cache.put(new Element(el1, el2));
			//Element element = cache.get(request.getParameter("id"));
			//request.setAttribute("page",el2);
			// request.getRequestDispatcher("/content/user/"+el1+".profileCache").forward(request, response);
			out.print("success--"+cache+"-------");
		}else if (request.getRequestPathInfo().getExtension().equals("ehcachecompany")) {
			PrintWriter out = response.getWriter(); 
			
			System.out.println("step------- 2.1");
			String page1 = request.getParameter("vpage");
			 
			String el1 = new String(request.getParameter("id"));
			String el2 = new String(page1);
			InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("ehcache.xml");
			CacheManager manager = CacheManager.newInstance(inputStream1);
			//out.print("true");
			Cache cache = manager.getCache("company");
			cache.put(new Element(el1, el2));
			//Element element = cache.get(request.getParameter("id"));
			//request.setAttribute("page",el2);
			// request.getRequestDispatcher("/content/user/"+el1+".profileCache").forward(request, response);
			out.print("success--"+cache+"-------");
		}else if (request.getRequestPathInfo().getExtension().equals("ehcacheproduct")) {
			PrintWriter out = response.getWriter(); 
			
			System.out.println("step------- 2.1");
			String page1 = request.getParameter("vpage");
			 
			String el1 = new String(request.getParameter("id"));
			String el2 = new String(page1);
			InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("ehcache.xml");
			CacheManager manager = CacheManager.newInstance(inputStream1);
			//out.print("true");
			Cache cache = manager.getCache("product");
			cache.put(new Element(el1, el2));
			//Element element = cache.get(request.getParameter("id"));
			//request.setAttribute("page",el2);
			// request.getRequestDispatcher("/content/user/"+el1+".profileCache").forward(request, response);
			out.print("success--"+cache+"-------");
		} else if (request.getRequestPathInfo().getExtension().equals("accept")) {
            service.connectFriend(request.getParameter("userId"),
                    request.getParameter("friend"), "reciever", "accept",
                    request.getParameter("friendMessage"),
                    request.getParameter("friendType"));
            service.connectFriend(request.getParameter("friend"),
                    request.getParameter("userId"), "sender", "accept",
                    request.getParameter("friendMessage"),
                    request.getParameter("friendType"));
            // response.sendRedirect("/sling/content/user/info?id="+request.getParameter("title"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("contactsImport")) {
            service.importContactList(request.getParameter("userId"),
                    request.getParameter("contactList"));
        } else if (request.getRequestPathInfo().getExtension().equals("up")) {
            service.uploadImportedContacts(request);
            if (request.getParameter("type") != null
                    && request.getParameter("type").equals("linkedin")) {
                response.sendRedirect(request.getContextPath()
                        + "/content/user/info?id="
                        + request.getParameter("userId"));
            } else if (request.getParameter("type") != null
                    && request.getParameter("type").equals("other")) {
                response.sendRedirect(request.getContextPath()
                        + "/servlet/friend/show.importedContact?userId="
                        + request.getParameter("userId"));
            }

        } else if (request.getRequestPathInfo().getExtension()
                .equals("addOther")) {
            String emailNode = request.getParameter("emailId").replaceAll("@",
                    "_");
            service.importContacts(request.getParameter("userId"), "Others",
                    emailNode.trim(), request.getParameter("emailId"),
                    request.getParameter("firstName"),
                    request.getParameter("lastName"));
            response.sendRedirect(request.getContextPath()
                    + "/servlet/friend/show.importedContact?userId="
                    + request.getParameter("userId"));

        } else if (request.getRequestPathInfo().getExtension().equals("remove")) {
            service.disConnectFriend(request.getParameter("userId"),
                    request.getParameter("friend"));
            service.disConnectFriend(request.getParameter("friend"),
                    request.getParameter("userId"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("userInvite")) {
            service.sendInvite(request.getParameter("userList"),
                    request.getParameter("userId"),
                    request.getParameter("providerId"),
                    request.getParameter("body"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("checkStatus")) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().print(
                    service.friendInvitationStatus(
                            request.getParameter("userId"),
                            request.getParameter("friendId")));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("updateStatus")) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            service.updateInvitationStatus(request.getParameter("userId"),
                    request.getParameter("friendId"),
                    request.getParameter("status"));
        }

    }

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {

        if (request.getRequestPathInfo().getExtension().equals("request")) {
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + "/connection/friend.friend").forward(request,
                    response);
        } else if (request.getRequestPathInfo().getExtension().equals("show")) {
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + "/connection/friend.connection").forward(request,
                    response);

        } else if (request.getRequestPathInfo().getExtension()
                .equals("visitor")) {
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".visitor").forward(request, response);
        } else if (request.getRequestPathInfo().getExtension().equals("view")) {
            request.setAttribute(
                    "online",
                    service.onlineFriend(request.getParameterMap().keySet()
                            .toArray()[0]
                            + ""));
            request.getRequestDispatcher(
                    "/content/user/"
                            + request.getParameterMap().keySet().toArray()[0]
                            + ".connection").forward(request, response);

        } else if (request.getRequestPathInfo().getExtension()
                .equals("importedContact")) {
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".importedContact").forward(request, response);

        } else if (request.getRequestPathInfo().getExtension()
                .equals("friendSuggest")) {
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".friendSuggestion").forward(request, response);

        } else if (request.getRequestPathInfo().getExtension()
                .equals("mutualFriend")) {
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".mutualFriend").forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("activity")) {
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".activityWidget").forward(request, response);

        } else if (request.getRequestPathInfo().getExtension().equals("invite")) {
            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".invite").forward(request, response);

        } else if (request.getRequestPathInfo().getExtension().equals("visit")) {
            profile_service.profileVisit("pran3bull_gmail.com",
                    request.getParameter("id"));

        } else if (request.getRequestPathInfo().getExtension().equals("search")) {
            response.getOutputStream().print(
                    profile_service.advanceSearchProfile(
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
                            request.getParameter("keyword"))
                            + "");

        } else if (request.getRequestPathInfo().getExtension()
                .equals("friendConnect")) {
            response.getOutputStream().print(service.friendRequestSchedular());

        } else if (request.getRequestPathInfo().getExtension().equals("active")) {
            // activeMQ.accountCreationProducer("UserCreationRave", "sdasd");

        }else if (request.getRequestPathInfo().getExtension().equals("notifyOff")) {
           try{
        	if(request.getRemoteUser()!=null && !request.getRemoteUser().equals("anonymous")){
         	   Session  session = repos.login(new SimpleCredentials("admin", "admin"
                        .toCharArray()));
         	   Node rootNode=session.getRootNode().getNode("content").getNode("user").getNode(request.getRemoteUser().replace("@","_"));
         	   if(rootNode.hasNode("connection") && rootNode.getNode("connection").hasNode("friend") && rootNode.getNode("connection").getNode("friend").hasNodes()){
         		NodeIterator itr=rootNode.getNode("connection").getNode("friend").getNodes();
         		while(itr.hasNext()){
         			Node i=itr.nextNode();
         			if(i.hasProperty("notify") && i.hasProperty("request") && i.getProperty("request").getString().equals("pending")){
         				i.setProperty("notify","false");
         			}
         		 }
         	   }else{
         			 response.getWriter().print("in allowed");
         	   }
        	   session.save();
            }else{
            	 response.getWriter().print("not allowed");
            }
           }  catch (PathNotFoundException e) {
              response.getWriter().print("Ext");
           } catch (RepositoryException e) {
        	   response.getWriter().print("Ext");
           } catch (Exception e) {
        	   response.getWriter().print("Ext");
           }
           //response.getWriter().print("allowed");
    }
  }
}
