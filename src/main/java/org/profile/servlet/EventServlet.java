package org.profile.servlet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.http.HttpService;
import org.profile.service.EventServiceUser;
import org.profile.service.RSSReaderService;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import biz.com.service.EventService;
import org.apache.sling.jcr.api.SlingRepository;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
        @Property(name = "service.vendor", value = "The Apache Software Foundation"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/service/event" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "userevent",
                "sendMail", "conference", "getService", "upload","profileEdit" })

})
@SuppressWarnings("serial")
public class EventServlet extends SlingAllMethodsServlet {

    @Reference
    private EventServiceUser service;
    
    @Reference
    private EventService eventService;
    
	@Reference
    private RSSReaderService reader;
	
	@Reference
	 private SlingRepository repos;
	
    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        
    }

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension().equals("userevent")) {
       if(eventService!=null){
    	   
//    	   dwr.getEduMessage();
 //   	 request.setAttribute("userevent",eventService.getEventList(request,"Pump","selected_category"));        	
       	 request.setAttribute("hotproductsearch",eventService.getHotProductSearchText(request));
		   request.setAttribute("hotsellersearch",eventService.getHotSellerSearchText(request));
		   HttpSession  session=request.getSession(true);
		   session.setAttribute("chatUrl", service.getChatUrl());
		 /*  String pass=service.getUserChatPassword(request.getRemoteUser());
		   
		   	String []data=pass.split("-@#@-");
		   	if(data[0]!=null && !data[0].equals("fail")){
		   session.setAttribute("chatUrl", service.getChatUrl());
		   session.setAttribute("chatId", "u"+data[1]);
		   session.setAttribute("chatEncpass", data[0]);
		   session.setAttribute("sipmlUrl", service.getSipmlUrl());
		   session.setAttribute("sipmlExtn", service.getSipmlExtn(request.getRemoteUser().replaceAll("@", "_")));
		 
		   request.getRequestDispatcher(
                   "/content/user/.welcome_new").forward(request, response);
           
		   	}*/
		   	request.getRequestDispatcher(
	                   "/content/user/.welcome_new").forward(request, response);
       }else{
    	   response.getWriter().print(response+"----"+eventService);
       }	
        	
        }else if (request.getRequestPathInfo().getExtension().equals("usereventnew")) {
            if(eventService!=null){
         	   
//         	   dwr.getEduMessage();
      //   	 request.setAttribute("userevent",eventService.getEventList(request,"Pump","selected_category"));        	
            	 request.setAttribute("hotproductsearch",eventService.getHotProductSearchText(request));
     		   request.setAttribute("hotsellersearch",eventService.getHotSellerSearchText(request));
     		   HttpSession  session=request.getSession(true);
     		   session.setAttribute("chatUrl", service.getChatUrl());
     		   String pass=service.getUserChatPassword(request.getRemoteUser());
     		   	String []data=pass.split("-@#@-");
     		   	if(data[0]!=null && !data[0].equals("fail")){
     		   session.setAttribute("chatUrl", service.getChatUrl());
     		   session.setAttribute("chatId", "u"+data[1]);
     		   session.setAttribute("chatEncpass", data[0]);
     		   session.setAttribute("sipmlUrl", service.getSipmlUrl());
     		   session.setAttribute("sipmlExtn", service.getSipmlExtn(request.getRemoteUser().replaceAll("@", "_")));
     		 
     		   request.getRequestDispatcher(
                        "/content/user/.welcome").forward(request, response);
                
     		   	}
            }else{
         	   response.getWriter().print(response+"----"+eventService);
            }	
             	
             }else if(request.getRequestPathInfo().getExtension().equals("sendMail")){
          
                  try{
                  
                  String value[]=new String[8];
                   value[0]=request.getParameter("email");
                   if(request.getParameter("name") != null){
                   value[1]=request.getParameter("name");
                   }else{
                    value[1]= "NA";
                   }
                   value[2]=request.getParameter("company");
                   value[3]=request.getParameter("msg");
                   value[4]=request.getParameter("supId");
                   if(request.getParameter("productquantity") != null){
                   value[5]=request.getParameter("productquantity");
                   }else{ value[5]= "NA";}
                   if(request.getParameter("productname") != null){
                   value[6]=request.getParameter("productname");
                   }else{
                    value[6]= "NA";
                   }
                   value[7]=request.getParameter("pid");
                   
                  String result=service.sendToken(value);
                  
                  response.getWriter().print(result);
                  
                  }catch(Exception e){
                   response.getWriter().print(e.getMessage()); 
                  }
                 
                 }else if(request.getRequestPathInfo().getExtension().equals("sendMailRfqResponse")){
        	try{
        		//PrintWriter out=response.getWriter();
     			String value[]=new String[7];
        	 value[0]=request.getParameter("email");
        	 value[1]=request.getParameter("name");
        	 value[2]=request.getParameter("company");
        	 value[3]=request.getParameter("msg");
        	 value[4]=request.getParameter("supId");
        	 if(request.getParameter("productname") != null){
        	 value[5]=request.getParameter("productname");
        	 }else{
        		 value[5]= "NA";
        	 }
        	 if(request.getParameter("productquantity") != null){
        	 value[6]=request.getParameter("productquantity");
        	 }else{
        		 value[6]="NA";
        	 }
//        	 Node node1=null;
//        	 Node rfqNode=null;
//        	 String[] pname;
//        	 String[] pquantity;
//        	 if(session.getRootNode().getNode("content").getNode("company").getNode(request.getParameter("supId")).hasNode("Rfq")){
//        	  node1=session.getRootNode().getNode("content").getNode("company").getNode(request.getParameter("supId")).getNode("Rfq");
//        	  String R=node1.getProperty("rfqcount").getString();
//        	  rfqNode=node1.getNode(R);
//        	  NodeIterator itr =rfqNode.getNodes();
//        	  while(itr.hasNext()){
//        	  
//        		pname=itr.nextNode().getProperty("productName ").getString();  
//        		pquantity=itr.nextNode().getProperty("productpQuantity").getString();
//        		
//        	  }
//        	  
//        	 }
        	 
        	 
        	 
        	 
        	String result=service.sendToken(value);
        	
        	response.getWriter().print(result);
        	
        	}catch(Exception e){
        		response.getWriter().print(e.getMessage());	
        	}
        }else  if (request.getRequestPathInfo().getExtension().equals("dashboard")) {
        	String userId=request.getRemoteUser();
        	List servicesList=service.getCustomerDetails(request,userId, response.getWriter());
        	 request.setAttribute("services", servicesList);
        	 
        	request.getRequestDispatcher(
                    "/content/user/.dashBoard").forward(request, response);

        }else  if (request.getRequestPathInfo().getExtension().equals("dashboardview")) {
        	String userId=request.getRemoteUser();
        	List servicesList=service.getCustomerDetails(request,userId, response.getWriter());
        	 //request.setAttribute("services", servicesList);
        	 
        	if(servicesList.size()>0){
        		response.getWriter().print("true");
        	}else if(servicesList.size() == 0){
        		response.getWriter().print("false");	
        	}

        }else if(request.getRequestPathInfo().getExtension().equals("news")){
		String url1="http://34.193.219.25:8983/solr/product/select?q=+"+request.getParameter("search")+"&wt=json&indent=true&facet=true&&facet=true&facet.field=v_keyword1&facet.field=v_keyword2";
        	String s = reader.readJsonFromUrl(url1);
        	request.setAttribute("solarnews", s);
            String url = "http://www.businessinsider.in/rss_section_feeds/21807169.cms";
            
         
           
            ArrayList al = reader.writeFeed(url);
            request.setAttribute("feed", al);

        	request.getRequestDispatcher(
                    "/content/news/.newsdescription").forward(request, response);
        }else if(request.getRequestPathInfo().getExtension().equals("profileEdit")){
        	String profile=request.getParameter("id");
        	if(request.getRemoteUser()!=null && request.getRemoteUser().replace("@", "_").equals(profile)){
        		request.getRequestDispatcher("/content/user/"+profile+".profileEdit").forward(request, response);	
        	}else{
        		//request.getRequestDispatcher("/content/user/"+profile+".profileEdit").forward(request, response);
        		response.getWriter().print("you are not authorised to change other profile .."+request.getRemoteUser());
        	}
        	
        	
        }else if(request.getRequestPathInfo().getExtension().equals("calender")){
        	request.getRequestDispatcher("/content/user/.calender").forward(request, response);	
        }else if(request.getRequestPathInfo().getExtension().equals("personalcontact")){
            String user =request.getParameter("user");
            Node rootNode = null;
      Session session;
      String status="";
      JSONObject objMainJson = new JSONObject();
      JSONArray objArray = new JSONArray();
     JSONObject obj = null;
      try{
            // PrintWriter out=response.getWriter();
           session = repos.login(new SimpleCredentials("admin", "admin"
             .toCharArray()));
           rootNode=session.getRootNode().getNode("content").getNode("user").getNode(user);
           if(rootNode.hasNode("ContactList") && rootNode.getNode("ContactList").hasNodes()){
            //obj.put("status","true"); 
            
            NodeIterator I1 =rootNode.getNode("ContactList").getNodes();
            while(I1.hasNext()){
             Node temp = I1.nextNode();
             if(temp.hasNodes()){
              
              NodeIterator I2 =temp.getNodes();
               while(I2.hasNext()){
               obj = new JSONObject();
               String email=I2.nextNode().getProperty("importedEmail").getString();
               obj.put("email", email);
               objArray.put(obj); 
               }
             }
            }
            objMainJson.put("data", objArray);
            response.getWriter().print(objMainJson.toString());
             }else{
              response.getWriter().print("false");
             // obj.put("status","false") ;
             // objArray.put(obj);
               }
           
           
      }catch(Exception e){
       response.getWriter().print("error====" + e.getMessage());
      }
            
           }
    }
}
