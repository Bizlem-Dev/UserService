package org.profile.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

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
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


@Component(immediate=true, metatype=false)
@Service(value=javax.servlet.Servlet.class)
@Properties({
	@Property(name="service.description", value="Prefix Test Servlet Minus One"),
	@Property(name="service.vendor", value="The Apache Software Foundation"),
	@Property(name="sling.servlet.paths", value={"/content/user/info"})

})
@SuppressWarnings("serial")
public class ProfileCallServlet extends SlingSafeMethodsServlet {

	@Reference
	private SlingRepository repos;

	
	
	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		PrintWriter out = response.getWriter();
			String profile=request.getParameter("id");
			Session session;
			response.setContentType("text/plain");
			
					try {
						session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
					
						@SuppressWarnings("unused")
						Node rootNode = session.getRootNode().getNode("content").getNode("user").getNode(profile);
						
						request.getRequestDispatcher("/content/user/"+profile+".profile").forward(request, response);
						
						// code commented of ehcache for profile 
					/*	InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("ehcache.xml");
						CacheManager manager = CacheManager.newInstance(inputStream1);
						Cache cache = manager.getCache("profile");
					     Element element = cache.get(request.getParameter("id"));
					     String url = "";
					     if(element == null)
					     {
					    	 System.out.println("step------- 2");
					    	 out.print("cache miss");
					    	url = "/content/user/"+profile+".profile";
					     }
					     else
					     {   
					    	 System.out.println("step------- 3");
					    	String pageData = (String)element.getObjectValue();
					    	request.setAttribute("page",pageData);
					    	if(!response.isCommitted()){
					    		url = "/content/user/"+profile+".profileCache";
					    	}else{
					    		url = "/content/user/"+profile+".profileCache";
					    	}
					     }
					     request.getRequestDispatcher(url).forward(request, response);*/
						
					} catch (PathNotFoundException e) {
						// TODO Auto-generated catch block
						out.print("PathNotFoundException==="+e.getMessage());
						//request.getRequestDispatcher("/content/user/.profile").forward(request, response);
						
					} catch (RepositoryException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						out.print("RepositoryException==="+e.getMessage());
					} 
		
			
	}
	
	
}