package org.profile.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Friend "),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "sling.servlet.paths", value = { "/service/servlet/publish" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "connect",
                "request", "accept", "show", "visitor", "view", "up",
                "importedContact", "addOther", "friendSuggest", "mutualFriend",
                "activity", "remove", "invite", "search", "friendConnect" })

})
@SuppressWarnings("serial")
public class PublishServlet extends SlingAllMethodsServlet {

	//DataBaseConnection dbc=new DataBaseConnection();
		static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	    static final String DB_URL = "jdbc:mysql://localhost:3306/publish";

	    //  Database credentials
	    static final String USER = "root";
	    static final String PASS = "password";
	
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

        }

    }

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {

        if (request.getRequestPathInfo().getExtension().equals("getcountry")) {
        	
        	Connection conn = null;
  		  PrintWriter out = response.getWriter();
  		        try {
  		        	JSONObject json = new JSONObject();
  		        	JSONArray jarray = new JSONArray();
  		       	 conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);                        
  		        	//oraganization  details 	  
  		               String sql = "select distinct country,country_code from publishupload";
  					//   idstream`, `stream_name
  		               String strCountry;
  					   String strCountryCode;
  		         		               
  		               Statement stmtstream=conn.createStatement();
  		               
  		               ResultSet rs=stmtstream.executeQuery(sql);
  		               
  		             JSONObject objData = null;
  		                   while(rs.next()){
  		                       objData = new JSONObject();
  		                	 strCountry=  rs.getString("country");
  		                	strCountryCode=rs.getString("country_code");
  		                	objData.put("country",strCountry);
  		                	objData.put("country_code",strCountryCode);
  		                	jarray.put(objData);
  		                	
  		   		        	}
  		                   json.put("countrylist",jarray);
  		   		        	
  		          out.print(json.toString());
  		        }
  		        catch (Exception e) {
  		        	out.println(e.getMessage());
  		          e.printStackTrace();
  		         }
  		        finally {
  		          try
  		          {
  		        	  conn.close();
  		          }
  		           
  		            catch (Exception e) {
  		              e.printStackTrace();
  		            }
  		    }
            
        } else if (request.getRequestPathInfo().getExtension().equals("getlang")) {
        	  
        	Connection conn = null;
    		  PrintWriter out = response.getWriter();
    		        try {
    		        	JSONObject json = new JSONObject();
    		        	JSONArray jarray = new JSONArray();
    		       	 conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);                        
    		        	//oraganization  details 	  
    		               String sql = "SELECT distinct language,language_code FROM `publishupload` where country_code ="+ "'"+request.getParameter("countrycode")+"'";
    					//   idstream`, `stream_name
    		               String strLang;
    					   String strLangCode;
    		         		               
    		               Statement stmtstream=conn.createStatement();
    		               
    		               ResultSet rs=stmtstream.executeQuery(sql);
    		               
    		             JSONObject objData = null;
    		                   while(rs.next()){
    		                       objData = new JSONObject();
    		                	 strLang=  rs.getString("language");
    		                	strLangCode=rs.getString("language_code");
    		                	objData.put("lang",strLang);
    		                	objData.put("lang_code",strLangCode);
    		                	jarray.put(objData);
    		                	
    		   		        	}
    		                   json.put("languagelist",jarray);
    		   		        	
    		          out.print(json.toString());
    		        }
    		        catch (Exception e) {
    		        	out.println(e.getMessage());
    		          e.printStackTrace();
    		         }
    		        finally {
    		          try
    		          {
    		        	  conn.close();
    		          }
    		           
    		            catch (Exception e) {
    		              e.printStackTrace();
    		            }
    		    }
        	
        }else if (request.getRequestPathInfo().getExtension().equals("getdata")) {
      	  
      	Connection conn = null;
  		  PrintWriter out = response.getWriter();
  		        try {
  		        	JSONObject json = new JSONObject();
  		        	JSONArray jarray = new JSONArray();
  		       	 conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);                        
  		        	//oraganization  details 	  
  		               String sql = "SELECT name,sling_url,destination_url FROM `publishupload` where country_code ="+ "'"+request.getParameter("countrycode")+"'" + "and language_code =" + "'"+request.getParameter("langcode")+"'";
  					//   idstream`, `stream_name
  		               String strSlingURL;
  					   String strName;
  					 String strDestURL;
  		         		               
  		               Statement stmtstream=conn.createStatement();
  		               
  		               ResultSet rs=stmtstream.executeQuery(sql);
  		               
  		             JSONObject objData = null;
  		                   while(rs.next()){
  		                       objData = new JSONObject();
  		                     strName=  rs.getString("name");
  		                   strSlingURL=rs.getString("sling_url");
  		                 strDestURL=rs.getString("destination_url");
  		                	objData.put("name",strName);
  		                	objData.put("sling_url",strSlingURL);
  		                	objData.put("dest_url",strDestURL);
  		                	jarray.put(objData);
  		                	
  		   		        	}
  		                   json.put("data",jarray);
  		   		        	
  		          out.print(json.toString());
  		        }
  		        catch (Exception e) {
  		        	out.println(e.getMessage());
  		          e.printStackTrace();
  		         }
  		        finally {
  		          try
  		          {
  		        	  conn.close();
  		          }
  		           
  		            catch (Exception e) {
  		              e.printStackTrace();
  		            }
  		    }
      	
      }else if (request.getRequestPathInfo().getExtension().equals("view")) {
            
        	request.getRequestDispatcher("/content/group/.publish").forward(request,response);

        }else if (request.getRequestPathInfo().getExtension().equals("readrss")) {
        	PrintWriter out = response.getWriter();
        	  Document htmlFile1 = null;
        	 try {
        		 JSONObject json = new JSONObject();
		         JSONArray jarray = new JSONArray();
		         JSONObject objData = null;
		         htmlFile1 = Jsoup.parse(new URL("http://prod.bizlem.io/index/en/home/home.html"), 120000);
		         /*JSONObject objData = null;
		         objData = new JSONObject();
		         objData.put("id","1");
               	 objData.put("iframeurl",htmlFile1.getElementById("rssFeedHide").val());
               	jarray.put(objData);
               	 objData = new JSONObject();
               	 objData.put("id","2");
               	 objData.put("iframeurl",htmlFile1.getElementById("rssFeedHide1").val());
               	 jarray.put(objData);*/
		         int length = htmlFile1.getElementsByAttributeValue("name", "newsurl").size();
		            for(int i=0;i<length;i++){
		            	objData = new JSONObject();
		            	objData.put("id",i);
		            	objData.put("newsurl",htmlFile1.getElementsByAttributeValue("name", "newsurl").get(i).val());
		            	jarray.put(objData);
		            }
               	 json.put("data",jarray);
		         out.print(json);
		         //out.print("htmlFile1---"+htmlFile1.getElementById("rssFeedHide1").val());
        	 } catch (Exception e) {
        		 out.print("Exception--"+e.getMessage());
           }
        	//request.getRequestDispatcher("/content/group/.publish").forward(request,response);

        }else if (request.getRequestPathInfo().getExtension().equals("writeIndexHtml")) {
        	PrintWriter out = response.getWriter();
      	  Document htmlFile1 = null;
      	 try {
      		     //htmlFile1 = Jsoup.parse(new URL("http://prod.bizlem.io/index/en/home/home.html"), 120000);
      		   htmlFile1 = Jsoup.connect("http://prod.bizlem.io/index/en/home/home.html")
      				    .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
      				   	.header("Accept-Encoding", "gzip, deflate")
      				    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
      				    .timeout(600000)
      				    .get();
      		     //out.print("1--"+htmlFile1);
      		   htmlFile1.getElementById("rssFeedIf").attr("src", "http://prod.bizlem.io/index/en/home/iframe/0/0news.html");
      		 //out.print("2");
               //System.out.println(htmlFile1.getElementById("rssFeedIf").attr("src"));
               String data = htmlFile1.html();
              
              // Files.write(Paths.get("/home/vil/rave%20Server/apache-rave-0.14-bin/webapps/ROOT/index/en/home/iframe/0/0news.html"), data.getBytes(), StandardOpenOption.CREATE);
              
              File file = new File("/home/vil/rave Server/apache-rave-0.14-bin/webapps/ROOT/index/en/home/home.html");
               FileWriter fileWriter = new FileWriter(file);
   			   fileWriter.write(data);
   			   fileWriter.flush();
   			   fileWriter.close(); 
                 out.print("true");
		         //out.print("htmlFile1---"+htmlFile1.getElementById("rssFeedHide1").val());
      	 } catch (Exception e) {
      		 out.print("Exception--"+e.getMessage());
         }
        }
  }
}
