package org.profile.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.BundleContext;
import org.profile.service.CompanyService;
import org.social.service.TwitterService;

import biz.com.service.ProductService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
        @Property(name = "service.vendor", value = "The Apache Software Foundation"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/company/show" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "com", "view",
                "career", "showCareer", "joinReason", "deleteMap", "redirect",
                "getInfo", "document", "image", "video", "privacy",
                "setTwitterToken", "getVerifier", "companySocial", "getStock",
                "setSocialConnect"})

})
@SuppressWarnings("serial")
public class CallCompanyServlet extends SlingAllMethodsServlet {

    BundleContext context;

    @Reference(referenceInterface = CompanyService.class, name = "service")
    private CompanyService service;

    @Reference
    private TwitterService twitterService;

    @Reference
    private SlingRepository repos;
    
    @Reference
	private ProductService product;

    @SuppressWarnings("unused")
    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {

        Session session;
        if (request.getRequestPathInfo().getExtension().equals("com")) {
            String companyName = request.getParameter("id");
            try {
                session = repos.login(new SimpleCredentials("admin", "admin"
                        .toCharArray()));

                
if(companyName.equals("new")){
	Node rootNode = session.getRootNode().getNode("content")
            .getNode("company");
	request.getRequestDispatcher(
            "/content/company/.createcompany")
            .forward(request, response);
}else{
	Node rootNode = session.getRootNode().getNode("content")
            .getNode("company").getNode(companyName);
	request.getRequestDispatcher(
            "/content/company/"+companyName+".company")
            .forward(request, response);
}
                
            } catch (PathNotFoundException e) {
                // TODO Auto-generated catch block
                request.getRequestDispatcher("/content/company/*.company")
                        .forward(request, response);

            } catch (RepositoryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (request.getRequestPathInfo().getExtension().equals("pincodedetail")) {
            
        	String strResult = service.getPincodeData(request.getParameter("pincode"));
        	response.getOutputStream().print(strResult);
        }else if (request.getRequestPathInfo().getExtension().equals("getSupplrCount")) {
        	String strResult = service.getSupplrData(request.getParameter("prdname"));
        	response.getOutputStream().print(strResult);
		} else if (request.getRequestPathInfo().getExtension()
                .equals("showCareer")) {

            request.getRequestDispatcher(
                    "/content/company/" + request.getParameter("compN")
                            + ".showCareer").forward(request, response);
        } else if (request.getRequestPathInfo().getExtension().equals("career")) {
            String companyName = request.getParameter("compN");
            try {
                session = repos.login(new SimpleCredentials("admin", "admin"
                        .toCharArray()));

                Node rootNode = session.getRootNode().getNode("content")
                        .getNode("company").getNode(companyName)
                        .getNode("Career/Job")
                        .getNode(request.getParameter("jobN"));

                request.getRequestDispatcher(
                        "/content/company/" + request.getParameter("compN")
                                + "/Career/Job/" + request.getParameter("jobN")
                                + ".career").forward(request, response);
                // request.getRequestDispatcher("/content/company/"+companyName+".company").forward(request,
                // response);
            } catch (PathNotFoundException e) {
                // TODO Auto-generated catch block
                request.getRequestDispatcher(
                        "/content/company/" + request.getParameter("compN")
                                + ".career").forward(request, response);

                // request.getRequestDispatcher("/content/company/*.company").forward(request,
                // response);

            } catch (RepositoryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if(request.getRequestPathInfo().getExtension()
                .equals("invite")){
        
         String compid=request.getParameter("compid");
         String compname=request.getParameter("compname");
         String url="http://prod.bizlem.io:8078/ServiceLogging/userPanel?type=invitecompany&compname="+compname+"&compid="+compid;
         
         request.setAttribute("compid", compid);
         request.setAttribute("compname", compname);
         request.setAttribute("url", url);
         
         request.getRequestDispatcher(
     "/content/products/.paidsearch").forward(
     request, response);
        
        
        
        
    }
        
        
        
        
        
        else if (request.getRequestPathInfo().getExtension()
                .equals("eventExistence")) {
        	try{
        	session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
        	String companyName = request.getParameter("compN");
        	String eventId = request.getParameter("eventId");
        	if(session.getRootNode().getNode("content").hasNode("company")){
            Node cmpNode = session.getRootNode().getNode("content")
                    .getNode("company");
            if(cmpNode.hasNode(companyName)){
            	Node compNode = cmpNode.getNode(companyName);
            	if(compNode.hasNode("event") && compNode.getNode("event").hasNodes()){
            		Node eventsNode = compNode.getNode("event");
            		if(eventsNode.hasNode(eventId)){
            			response.getOutputStream().print("true"+"~"+"Participated");
            		}else{
            			response.getOutputStream().print("false"+"~"+"Not Participated");
            		}
//            		NodeIterator nodeIterator = compNode.getNode("event").getNodes();
//                    while (nodeIterator.hasNext()) {
//                        Node removeNode = nodeIterator.nextNode();
//                        if (removeNode.getName().equals(eventId)) {
//                        	response.getOutputStream().print("true"+"~"+"Participated");
//                        }else{
//                       	
//                        }
//                    }
                    
            	}
            	else{
        			response.getOutputStream().print("false"+"~"+"Not Participated");
        		}
            }else{
    			response.getOutputStream().print("false"+"~"+"Not Participated");
    		}
        	}else{
    			response.getOutputStream().print("false"+"~"+"Not Participated");
    		}
        	
        } catch (PathNotFoundException e) {
            // TODO Auto-generated catch block
                      // request.getRequestDispatcher("/content/company/*.company").forward(request,
            // response);
        	response.getOutputStream().print("RepositoryException------"+e.getMessage());

        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.getOutputStream().print("RepositoryException------"+e.getMessage());
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.getOutputStream().print("Exception------"+e.getMessage());
        }


        }else if (request.getRequestPathInfo().getExtension()
                .equals("joinReason")) {
            request.getRequestDispatcher(
                    "/content/company/" + request.getParameter("compN")
                            + ".joinReason").forward(request, response);
        }else if (request.getRequestPathInfo().getExtension()
                .equals("checkcompany")) {
           String companyName = request.getParameter("companyName");
           String data = service.checkCompanyExists(companyName);
           response.getOutputStream().print(data);
        } else if (request.getRequestPathInfo().getExtension().equals("view")) {
        	String company = request.getParameter("compN");
           // request.getRequestDispatcher("/content/company/" + request.getParameter("compN")+ ".companyView").forward(request, response);
        /*	InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("ehcache.xml");
			CacheManager manager = CacheManager.newInstance(inputStream1);
			Cache cache = manager.getCache("company");
		     Element element = cache.get(request.getParameter("compN"));
		     String url = "";
		     if(element == null)
		     {
		    	 System.out.println("step------- 2");
		    	// out.print("cache miss");
		    	url = "/content/company/"+company+".companyView";
		     }
		     else
		     {   
		    	 System.out.println("step------- 3");
		    	String pageData = (String)element.getObjectValue();
		    	request.setAttribute("page",pageData);
		    	if(!response.isCommitted()){
		    		url = "/content/company/"+company+".companyViewCache";
		    	}else{
		    		url = "/content/company/"+company+".companyViewCache";
		    	}
		     }*/
        	String url = "/content/company/"+company+".companyView";
		     request.getRequestDispatcher(url).forward(request, response);	
        	
        	
        } else if (request.getRequestPathInfo().getExtension()
                .equals("deleteMap")) {
            service.deleteMapping(request.getParameter("nodeName"));
            response.sendRedirect(request.getContextPath()
                    + "/servlet/company/show.com?id="
                    + request.getParameter("compN"));

        } else if (request.getRequestPathInfo().getExtension().equals("video")) {
            request.getRequestDispatcher(
                    "/content/company/" + request.getParameter("comp")
                            + ".videoGallery").forward(request, response);
        } else if (request.getRequestPathInfo().getExtension().equals("image")) {
            request.getRequestDispatcher(
                    "/content/company/" + request.getParameter("comp")
                            + ".pictureGallery").forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("document")) {
            request.getRequestDispatcher(
                    "/content/company/" + request.getParameter("comp")
                            + ".documentGallery").forward(request, response);
        } else if (request.getRequestPathInfo().getExtension().equals("stock")) {
            response.getOutputStream().print(
                    service.getStockDemo(request.getParameter("bse")) + "");
        } else if (request.getRequestPathInfo().getExtension()
                .equals("privacy")) {
            request.getRequestDispatcher(
                    "/content/company/" + request.getParameter("company")
                            + ".privacy").forward(request, response);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getVerifier")) {
            String[] result = service.getVerifier();
            request.getSession(true).setAttribute("companyTwitter", result[1]);
            request.getSession(true).setAttribute("companyName",
                    request.getParameter("company"));
            response.sendRedirect(result[2]);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("setTwitterToken")) {
            String[] result = twitterService.getToken(
                    request.getParameter("oauth_verifier"),
                    request.getParameter("oauth_token"),
                    request.getSession(true).getAttribute("companyTwitter")
                            + "");
            request.getSession(true).removeAttribute("companyTwitter");
            if (result[2].equals("success")) {
                String[] response_body = twitterService.getAuth(result[0],
                        result[1]);
                if (response_body[3].equals("success")) {
                    int p = service.setCompanyTwitterCred(
                            request.getSession(true)
                                    .getAttribute("companyName") + "",
                            response_body[0], response_body[1],
                            response_body[2], response_body[4]);
                    request.getSession(true).removeAttribute("companyName");
                    if (p == 1) {
                        response.getOutputStream()
                                .print("<html><head><script>window.close();</script></head></html>");
                    } else {
                        response.getOutputStream().print("error");
                    }
                } else {
                    response.getOutputStream().print("error");
                }
            } else {
                response.getOutputStream().print("error");
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("companySocial")) {
            Map<String, String> socialMap = new HashMap<String, String>();
            socialMap = service.getTwitterData(request.getParameter("company"));

            if (socialMap == null) {
                socialMap = new HashMap<String, String>();
                request.setAttribute("twitter", socialMap);
            } else {
                request.setAttribute("twitter", socialMap);
            }
            request.getRequestDispatcher(
                    "/content/company/" + request.getParameter("company")
                            + ".socialConnect").forward(request, response);
        }else if (request.getRequestPathInfo().getExtension()
                .equals("mycompany")) {
        	if(request.getRemoteUser()!=null && !request.getRemoteUser().equals("anonymous")){
        		String currNode="";
        		ArrayList<Node> al=new ArrayList<Node>();
        		   try {
					session = repos.login(new SimpleCredentials("admin", "admin"
						       .toCharArray()));
					 if (session.getRootNode().getNode("content")
				   		       .hasNode("company")) {
				   		      currNode = "/content/company/";
				   		     }

	            		     HashMap getList = (HashMap) product
	            		    		 .getCompanyByNode(currNode,
	  	            		    		   request.getRemoteUser(), request, response);

	            		      Set set = getList.entrySet();
	            		    
	            		      Iterator i = set.iterator();
	            		      
	            		      while(i.hasNext()) {
	            	   		         Map.Entry me = (Map.Entry)i.next();	            	   		         
	            	   		         al.add(session.getRootNode().getNode("content").getNode("company").getNode(me.getKey().toString()));
	            		      }
	            		      	request.setAttribute("companylist", al);
	            		      	request.getRequestDispatcher(
	                                    "/content/company/.mycompanyList").forward(request, response);
	               			
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            		   
    			
        		
    			}else{
    			response.getWriter().print("----------");	
    				
    			}
        	
        }else if (request.getRequestPathInfo().getExtension().equals("addSling")) {
        
        	try{
        		String workgroupID = request.getParameter("workgroupID");
        		String workgroupDisplayName = request.getParameter("workgroupDisplayName");
        		String productCode = request.getParameter("productCode");
        		String companyId = request.getParameter("companyId");
        		String serviceId = request.getParameter("serviceId");
        		
        		PrintWriter out =response.getWriter();
        		//out.println(workgroupID+"~"+workgroupDisplayName+"~"+productCode+"~"+companyId+"~"+serviceId);
        		
        		session = repos.login(new SimpleCredentials("admin", "admin"
         		       .toCharArray()));
     		 if (session.getRootNode().getNode("content").hasNode("company")) {
     		 
     			 Node currNode =session.getRootNode().getNode("content").getNode("company");
     			//out.println(currNode);   
           		    if(currNode.hasNode(companyId)){
           		    	//out.println("compantnode");
           		    	if(currNode.getNode(companyId).hasNode("livechat")){
           		    		//out.println("has livechat");
           		    		//String productID=currNode.getNode(companyId).getNode("livechat").getProperty("productCode").getString();
           		    		Node setnode =currNode.getNode(companyId).getNode("livechat");
           		    	    setnode.setProperty("productCode", productCode);
           		    		setnode.setProperty("workgroupID", workgroupID);
           		    		setnode.setProperty("workgroupDisplayName", workgroupDisplayName);
           		    		setnode.setProperty("serviceId", serviceId);
           		    		session.save();
           		    		//out.println("add property");
           		    		
           		    	}else {
           		    		out.println("check livechat");
           		    		Node setnode =currNode.getNode(companyId);
           		    		setnode.addNode("livechat");
           		    		//out.println("addNode livechat");
           		    		setnode.setProperty("productCode", productCode);
           		    		setnode.setProperty("workgroupID", workgroupID);
           		    		setnode.setProperty("workgroupDisplayName", workgroupDisplayName);
           		    		setnode.setProperty("serviceId", serviceId);
           		    		session.save();
           		    		//out.println("set property");
           		    		}
           		    	
           		    }else{
           		    	//out.println("companyId nor found");
           		    	}
         		     }
     		
        	}catch(Exception e){
        		response.getWriter().print("error   "+e.getMessage());
        		
        	}
        	
        	
        }else if (request.getRequestPathInfo().getExtension().equals("addcallMBToSling")) {
            
            try{
             String productCode = request.getParameter("productCode");
             String companyId = request.getParameter("companyId");
             String serviceId = request.getParameter("serviceId");
             String extens_queue = request.getParameter("ext_queue");
             
             PrintWriter out =response.getWriter();
             //out.println(workgroupID+"~"+workgroupDisplayName+"~"+productCode+"~"+companyId+"~"+serviceId);
             
             session = repos.login(new SimpleCredentials("admin", "admin"
                     .toCharArray()));
           if (session.getRootNode().getNode("content").hasNode("company")) {
           
            Node currNode =session.getRootNode().getNode("content").getNode("company");
           //out.println(currNode);   
                    if(currNode.hasNode(companyId)){
                     //out.println("compantnode");
                    	if(!currNode.getNode(companyId).hasProperty("extnqueue")){
                    		currNode.getNode(companyId).setProperty("extnqueue", extens_queue);	
                    	}
                     if(currNode.getNode(companyId).hasNode("callMB")){
                      //out.println("has livechat");
                      //String productID=currNode.getNode(companyId).getNode("livechat").getProperty("productCode").getString();
                      Node setnode =currNode.getNode(companyId).getNode("callMB");
                         setnode.setProperty("productCode", productCode);
                      setnode.setProperty("serviceId", serviceId);
                      session.save();
                      //out.println("add property");
                      
                     }else {
                      out.println("check callMB");
                      Node setnode =currNode.getNode(companyId);
                      setnode.addNode("callMB");
                      //out.println("addNode livechat");
                      setnode.setProperty("productCode", productCode);
                      setnode.setProperty("serviceId", serviceId);
                      session.save();
                      //out.println("set property");
                      }
                     
                    }else{
                     //out.println("companyId nor found");
                     }
                   }
          
            }catch(Exception e){
             response.getWriter().print("error   "+e.getMessage());
             
            }
            
            
           }else if (request.getRequestPathInfo().getExtension().equals("getExtension")) {
        	   String userext = "";
               try{
                String userList = request.getParameter("users");
                session = repos.login(new SimpleCredentials("admin", "admin"
                        .toCharArray()));
                Node user = null;
                
                int iCount=0;
                String userListArr[] = userList.split(",");
                for(int i=0;i<userListArr.length;i++){
                	if (session.getRootNode().getNode("content").hasNode("user") && session.getRootNode().getNode("content").getNode("user")
                			.hasNode(userListArr[i].replace("@", "_"))) {
                		user = session.getRootNode().getNode("content").getNode("user").getNode(userListArr[i].replace("@", "_"));
                		if(user.hasProperty("extension")){
                			if(iCount == 0){
                			userext = userext + user.getProperty("extension").getString();
                			iCount++;
                			}else{
                				userext = userext + "," + user.getProperty("extension").getString();
                			}
                		}
                     }
                }
             
               }catch(Exception e){
                response.getWriter().print("error   "+e.getMessage());
                
               }
               
               response.getWriter().print(userext);
              }else if (request.getRequestPathInfo().getExtension().equals("comp")) {
     		try{
     			Node currentnode1;
      		     String search_key = request.getParameter("userId");
      		   String service_Id = request.getParameter("serviceId");
      		     String currNode = "";
      		     

      		     session = repos.login(new SimpleCredentials("admin", "admin"
      		       .toCharArray()));

      		     if (session.getRootNode().getNode("content")
      		       .hasNode("company")) {
      		      currNode = "/content/company/";
      		       currentnode1=session.getRootNode().getNode("content")
      	      		       .getNode("company");
      		     HashMap getList = (HashMap) product
      		       .getCompanyByNode(currNode,
      		         search_key, request, response);

      		     PrintWriter out = response.getWriter();

      		     /*
      		      * out.println(getList.size()); out.println(currNode);
      		      * out.print(search_key);
      		      */
      		  // Get a set of the entries
      		      Set set = getList.entrySet();
      		      // Get an iterator
      		      Iterator i = set.iterator();
      		   // Display elements
      		      
      		      if(!getList.isEmpty()){
      		    	  if(set.size() > 1){
      		    		 Set set1 =null;
                   out.print("<select   class='form-control' id='companyId' name='companyId' style='height:30px'>");

//      		    	while(i.hasNext()) {
//
//         		         Map.Entry me = (Map.Entry)i.next();
//         		        HashMap m1 = new HashMap();
//         		       String compNode = me.getKey().toString();
//         		       
//         		      if(currentnode1.hasNode(compNode) && currentnode1.getNode(compNode).hasNode("livechat")){
//       		        	Node n = currentnode1.getNode(compNode).getNode("livechat");
//       		         if(service_Id.equals(n.getProperty("serviceId").getString())){
//       		        	 m1.put(me.getKey(), me.getValue());
//       		         }}
//         		     set1 = m1.entrySet();
//      		    	}
         		     int iCount = 0;
         		     while(i.hasNext()) {
         		         Map.Entry me = (Map.Entry)i.next();
         		       String compNode = me.getKey().toString();
         		       //if(set1.size()>=1){
         		         if(currentnode1.hasNode(compNode) && currentnode1.getNode(compNode).hasNode("livechat")){
         		        
         		        	Node n = currentnode1.getNode(compNode).getNode("livechat");
         		         if(service_Id.equals(n.getProperty("serviceId").getString())){
         		        	 
         		         out.print("<option   value='" + me.getKey() + "' selected >"+me.getValue()+"</option>");
         		         break;
         		         }else{
         		        	 
         		        	 if(iCount == 0){
         		        		 iCount++;
         		        	out.print("<option   value='" + me.getKey() + "' selected >"+me.getValue()+"</option>");
         		        	 }else{
         		        		out.print("<option   value='" + me.getKey() + "' >"+me.getValue()+"</option>");
         		        	 }	 
         		         }
         		         }else{
         		        	 if(iCount == 0){
         		        		 iCount++;
         		        	out.print("<option   value='" + me.getKey() + "' selected >"+me.getValue()+"</option>");
         		        	 }else{
         		        		out.print("<option   value='" + me.getKey() + "' >"+me.getValue()+"</option>");
         		        	 }
         		         }
//         		      }else{
//       		        	out.print("<option value='" + me.getKey() + "' >"+me.getValue()+"</option>");
//       		         }
         		       
      		    	}
         		      out.print("</select>");
      		    	  
      		    	  }else{
      		    	out.print("<select readonly class='form-control' id='companyId' name='companyId' style='height:30px'>");
      		    	out.print("</select>");
      		    	  }
      		      
      		    	  
      		      //out.print("</select><input style='height:25px' type='button' value='Get Selected Value' onclick='getCategoryValue()'>");
      		      }else {
      		      out.print("<b>Company Not found. please create company !</b>");
      		     }}
      		}catch (Exception e) {
      		     PrintWriter out = response.getWriter();
      		     out.print(e.getMessage());
      		    }
           }else if (request.getRequestPathInfo().getExtension().equals("verifycomp")) {
   			String status="";
   			String companytnc; 
   			String compid=request.getParameter("compid");
   			PrintWriter out = response.getWriter();

   			Node rootnode;
   			Node comp;
   			try{
   			session = repos.login(new SimpleCredentials("admin", "admin".toCharArray()));
   			 rootnode=session.getRootNode().getNode("content").getNode("company");
   			if(rootnode.hasNode(compid)&&rootnode.getNode(compid).hasNode("BasicInfo")&&rootnode.getNode(compid).getNode("BasicInfo").hasProperty("companytnc"))
   			{
   				companytnc =rootnode.getNode(compid).getNode("BasicInfo").getProperty("companytnc").getString();
   				if(companytnc.equals("accept")){
   					status="true";
   				}else{
   				 comp=rootnode.getNode(compid).getNode("BasicInfo");
   				 comp.setProperty("companytnc", "accept");
   				 status="true";
   				}
   				
   			}else{
   				rootnode=session.getRootNode().getNode("content").getNode("company");
   						comp=rootnode.getNode(compid).getNode("BasicInfo");
   				comp.setProperty("companytnc", "accept");
   				 status="true";
   			}
   			session.save();
   			out.print(status);
   			}catch(Exception e){}
   			
           }

    }

    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension().equals("deleteMap")) {
            service.deleteMapping(request.getParameter("nodeName"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("addCategory")) {
            service.addContentCategory(request.getParameter("category"),
                    request.getParameter("type"),
                    request.getParameter("company"));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("addPicture")) {
            service.allFileUpload(request.getRemoteUser().replaceAll("@", "_"),
                    request.getParameter("category"), request,
                    request.getParameter("company"), "Picture");
        } else if (request.getRequestPathInfo().getExtension()
                .equals("addVideo")) {
            String status=service.videoUpload(request.getRemoteUser().replaceAll("@", "_")
                    + "", request, request.getParameter("category"),
                    request.getParameter("company"));
           if(status.equals("true")){
        	   response.sendRedirect(request.getContextPath()
                       + "/servlet/company/show.video?comp="
                       + request.getParameter("company"));
           }else{
            response.getOutputStream().println("Sorry we are facing some issue ,please try later"+status);
           }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("addDocument")) {
            service.allFileUpload(request.getRemoteUser().replaceAll("@", "_"),
                    request.getParameter("category"), request,
                    request.getParameter("company"), "Document");
        } else if (request.getRequestPathInfo().getExtension()
                .equals("download")) {
            Map<Object, Object> map = service.downloadNode(request
                    .getParameter("path"));
            response.setContentType("text/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + map.get("fileName"));
            InputStream is = (InputStream) map.get("stream");
            int read = 0;
            byte[] bytes = new byte[1024];
            OutputStream os = response.getOutputStream();
            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
            os.flush();
            os.close();
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getInfo")) {
            response.getOutputStream().print(
                    service.getInfo(request.getParameter("value"),
                            request.getParameter("type"),
                            request.getContextPath()));
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getStock")) {
            response.getOutputStream().print(
                    service.getStock(request.getParameter("company")) + "");
        } else if (request.getRequestPathInfo().getExtension()
                .equals("setSocialConnect")) {
            service.setCompanySocialCred(request.getParameter("company"),
                    request.getParameter("name"), request.getParameter("url"),
                    request.getParameter("type"));
        }else if (request.getRequestPathInfo().getExtension()
                .equals("savebuyingreq")) {
        	if(request.getParameter("prdname") != null && request.getParameter("prdname") != ""){
            String res = service.saveBuyingReq(request,response);
            if(res!=null && res.equals("true")){
            	  response.sendRedirect(request.getContextPath()+ "/servlet/common/search.getquote?id="+ res);  	
            }else{
            	  response.sendRedirect(request.getContextPath()+ "/servlet/common/search.getquote?id=false"+res);
//            response.sendRedirect(request.getContextPath()+ "/servlet/common/search.getquote");
            }
        	}else{
        		  response.sendRedirect(request.getContextPath()+ "/servlet/common/search.getquote");
        	}
       }

    }
}
