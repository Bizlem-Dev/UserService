package org.profile.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.profile.service.EventServiceUser;
import org.profile.service.RSSReaderService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.JSONArray;
@Component(configurationFactory = true)
@Service(RSSReaderService.class)
@Properties({ @Property(name = "userEventService", value = "eventService") })
public class RSSReaderServiceImpl implements RSSReaderService  {


	  
	    public ArrayList writeFeed(String url)  {
	        ArrayList al = new ArrayList();
	        try {
	        	URL rssURL = new URL(url);
	            
	            //out.print(rssURL+"");
	            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	            Document doc = builder.parse(rssURL.openStream());

	            NodeList items = doc.getElementsByTagName("item");
//	          NodeList description = doc.getElementsByTagName("item");  
//	            if(items.getLength() > 5){
//	               items = 5; 
//	            }
	            for (int i = 0; i < items.getLength(); i++) {
	                Element item = (Element) items.item(i);
	                StringBuilder br = new StringBuilder();
	                if (item != null) {

	                    br.append("<li>");
	                    br.append("<a href='"+getValue(item, "link")+"' target='_blank'>"+getValue(item, "title")+"</a>");
	                    br.append("</li>");
//	            out.print("<h2>");
//	            out.println(getValue(item, "title"));
//	            out.print("</h2>");
	                    //System.out.println(getValue(item, "description")+System.lineSeparator());  
//	            out.println(getValue(item, "pubDate"));  
//	            out.print("<br>");  
	                    al.add(br.toString());
	                }	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return al;
	    }

	    public String getValue(Element parent, String nodeName) {
	        try {
	            // int f=parent.getElementsByTagName(nodeName).getLength();
	            // S//ystem.out.println("----------"+f);
//	          for(int a=0;a<f;a++){
	            // System.out.println(parent.getElementsByTagName(nodeName).item(a).getFirstChild().getNodeValue()); 
//	          }
	            return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
	        } catch (NullPointerException e) {
	            return null;
	        }
	    }
	    private  String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
		  }
	    public  String readJsonFromUrl(String url){
		    InputStream is=null;
		    JSONObject json=null;
		    JSONObject json1=null;
			try {
				is = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = this.readAll(rd);
		       json = new JSONObject(jsonText);
		       String s = json.getString("response");
               //    System.out.println("s--"+s);
		       JSONObject   objjson = new JSONObject(s);
                   String docs = objjson.getString("docs");
                 //  System.out.println("s--"+docs);
                   JSONArray array = new JSONArray(docs);
	       JSONObject json2=new JSONObject();
	       for(int i = 0 ; i<array.length();i++){
	    	   json2.put("newsid",array.getJSONObject(i).getString("id"));
                       
	       }
			return json2.toString();	
		    } catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return e.getMessage();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return e.getMessage();
			}finally {
		      try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }   
				
			
		  }


}
