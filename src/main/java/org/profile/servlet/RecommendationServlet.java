package org.profile.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.profile.service.RecommendationService;



@Component(immediate=true, metatype=false)
@Service(value=javax.servlet.Servlet.class)
@Properties({
	@Property(name="service.description", value="Recommendation "),
	@Property(name="service.vendor", value="VisionInfoCon"),
	@Property(name="sling.servlet.paths", value={"/servlet/recommendation/show"}),
	@Property(name="sling.servlet.resourceTypes", value="sling/servlet/default"),
	@Property(name="sling.servlet.extensions", value={"send","request","accept","show","write","view" })

})
@SuppressWarnings("serial")
public class RecommendationServlet extends SlingAllMethodsServlet   {
	
	@Reference
	private RecommendationService service;
	
	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		
		
		if (request.getRequestPathInfo().getExtension().equals("send")) {
			request.getParameter("recommendType");
			request.getParameter("requestType");
			request.getParameter("userId");
			String[] person=request.getParameterValues("recommendPeople");
			for (String peopleValue : person) {
				service.sendRecommendRquest(peopleValue, request.getParameter("userId"),
						request.getParameter("requestType"),request.getParameter("recommendType"),
						request.getParameter("message"),"pending","Reciever","null","null","null");
			}
			for (String peopleValue : person) {
				service.sendRecommendRquest(request.getParameter("userId"),peopleValue,
						request.getParameter("requestType"),request.getParameter("recommendType"),
						request.getParameter("message"),"pending","Sender","null","null","null");
			}
			
			response.sendRedirect(request.getContextPath()+"/content/user/info?id="+request.getParameter("userId"));
			
		
		}
		else if (request.getRequestPathInfo().getExtension().equals("accept")) {
			
			service.sendRecommendRquest(request.getParameter("recommendTo"), request.getParameter("recommendBy"),
					request.getParameter("requestType"),request.getParameter("recommendType"),
					"null","Accepted","Sender",request.getParameter("relatonship"),
					request.getParameter("colleagueTitle"),request.getParameter("recommendDesc"));
			
			service.sendRecommendRquest(request.getParameter("recommendBy"),request.getParameter("recommendTo"),
					request.getParameter("requestType"),request.getParameter("recommendType"),
					"null","Accepted","Reciever",request.getParameter("relatonship"),
					request.getParameter("colleagueTitle"),request.getParameter("recommendDesc"));
			
			response.sendRedirect(request.getContextPath()+"/content/user/info?id="+request.getParameter("userId"));
			
		}
		
	}
	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		
		
		if (request.getRequestPathInfo().getExtension().equals("show")) {
			// request.getRequestDispatcher("/content/company/*.company").forward(request,
			// response);
			request.getRequestDispatcher(
					"/content/user/"+request.getParameter("userId")+".recommend").forward(request,response);
			// response.sendRedirect(request.getContextPath()+"/content/user/"+request.getParameter("userId")
			// +"/connection/friend.friend?userId="+request.getParameter("userId"));
		}
		
		
		
		else if (request.getRequestPathInfo().getExtension().equals("request")) {
		
			request.getRequestDispatcher(
					"/content/user/"+request.getParameter("userId")+".recommendRequests").forward(request,response);
	
		}
		
		else if (request.getRequestPathInfo().getExtension().equals("write")) {
			request.getRequestDispatcher(
					"/content/user/"+request.getParameter("userId")+".writeRecommend").forward(request,response);
		}
		
		else if (request.getRequestPathInfo().getExtension().equals("view")) {
			request.getRequestDispatcher(
					"/content/user/"+request.getParameter("userId")+".recommendation").forward(request,response);
		}
		
	}
	
	

}
