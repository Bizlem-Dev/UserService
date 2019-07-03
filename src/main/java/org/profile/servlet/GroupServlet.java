package org.profile.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

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
import org.common.service.FileUploadService;
import org.profile.service.GroupService;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Group Service"),
        @Property(name = "service.vendor", value = "VisionInfoCon"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/group/show" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "save", "view",
                "viewGroup", "addMember", "acceptRequest", "upload",
                "addedGroup" ,"delete"}) })
@SuppressWarnings("serial")
public class GroupServlet extends SlingAllMethodsServlet {

    @Reference
    private GroupService service;

    @Reference
    private SlingRepository repos;

    @Reference
    private FileUploadService uploadService;

    final int NUMBEROFRESULTSPERPAGE = 6;
    
    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {

        if (request.getRequestPathInfo().getExtension().equals("save")) {
            String group = request.getParameter("groupName").replaceAll("\\s+",
                    "");
            Long n=service.saveGroupInfo(request,response);
            response.sendRedirect(request.getContextPath()
                    + "/servlet/group/show.viewGroup?id=" + n);

        } else if (request.getRequestPathInfo().getExtension()
                .equals("delete")) {

           boolean status= service.deletNode(request.getParameter("groupName"));       
        if(status){
        	PrintWriter out=response.getWriter();
        	out.print("true");
        }else{
        	PrintWriter out=response.getWriter();
        	out.print("false");
        }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("addMember")) {

            service.addMember(request.getParameter("groupName"),
                    request.getParameter("userId"), true,
                    request.getParameter("accept"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("acceptRequest")) {

            service.addMember(request.getParameter("groupName"),
                    request.getParameter("userId"), false, "no");

        } else if (request.getRequestPathInfo().getExtension().equals("upload")) {
            service.deletNode("/content/group/"
                    + request.getParameter("groupName") + "/picture");
            uploadService.uploadFile(request.getParameter("filePath"),
                    request.getParameter("fileName"), request);
            response.sendRedirect(request.getContextPath()
                    + "/servlet/group/show.view?id="
                    + request.getParameter("groupName"));

        } else if (request.getRequestPathInfo().getExtension()
                .equals("unjoin")) {

            service.unJoinGroup(request.getParameter("groupName"),
                    request.getParameter("userId"));

        }
    }

    @SuppressWarnings("unused")
    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
    	try{
        if (request.getRequestPathInfo().getExtension().equals("view")) {

            Session session;
            String group = request.getParameter("id");
            Node rootNode = null;
            try {
                session = repos.login(new SimpleCredentials("admin", "admin"
                        .toCharArray()));

                rootNode = session.getRootNode().getNode("content")
                        .getNode("group").getNode(group);
if(group.equals("new")){
                request.getRequestDispatcher(
                        "/content/group/.group").forward(request,
                        response);
}else{
	 request.getRequestDispatcher(
             "/content/group/" + group + ".group").forward(request,
             response);
}       } catch (PathNotFoundException e) {
                // TODO Auto-generated catch block
                request.getRequestDispatcher("/content/group/.group").forward(
                        request, response);

            } catch (RepositoryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (request.getRequestPathInfo().getExtension()
                .equals("viewGroup")) {
        	Node groupNode= null;
            Session session;
            String group = request.getParameter("id");
        	String from = null;
			String to = null;
			int t, f;
			from = request.getParameter("from");
			to =from;
			  
			if (to != null && from != null) {
				try {
					t = Integer.parseInt(to);
					f = Integer.parseInt(from);
					f=(f-1)*NUMBEROFRESULTSPERPAGE;
					t=t*NUMBEROFRESULTSPERPAGE;
					Node tempGrpNode = null;
					ArrayList<Node> m = new ArrayList<Node>();
					  session = repos.login(new SimpleCredentials("admin", "admin"
			                    .toCharArray()));
			            groupNode = session.getRootNode().getNode("content").getNode("group")
			                    .getNode(group);
			            if (groupNode.hasNode("Member") && groupNode.getNode("Member").hasNodes()) {
			            	NodeIterator iterator = groupNode.getNode("Member").getNodes();
			            	while (iterator.hasNext()){
			            	tempGrpNode = iterator.nextNode();
			                m.add(tempGrpNode);
			               
			            	}
			            }
			            request.setAttribute("total",m.size());
					ArrayList<Node> alist = new ArrayList<Node>();
					if(m.size()>t){
						for (int i = f; i < t; i++) {
							alist.add(m.get(i));
						}
					}else{
					for (int i = f; i < m.size(); i++) {
						alist.add(m.get(i));
					}
					}
					request.setAttribute("groupmemberlist", alist);
					 request.getRequestDispatcher(
			                    "/content/group/.progroupView").forward(request,
			                    response);
				} catch (NumberFormatException e) {
					Node tempGrpNode = null;
					ArrayList<Node> m = new ArrayList<Node>();
					  session = repos.login(new SimpleCredentials("admin", "admin"
			                    .toCharArray()));
			            groupNode = session.getRootNode().getNode("content").getNode("group")
			                    .getNode(group);
			            if (groupNode.hasNode("Member") && groupNode.getNode("Member").hasNodes()) {
			            	NodeIterator iterator = groupNode.getNode("Member").getNodes();
			            	while (iterator.hasNext()){
			            	tempGrpNode = iterator.nextNode();
			                m.add(tempGrpNode);
			                
			            	}
			            }
			            request.setAttribute("total",m.size());
					ArrayList<Node> alist = new ArrayList<Node>();
					if (m.size() > NUMBEROFRESULTSPERPAGE) {
						for (int i = 0; i < NUMBEROFRESULTSPERPAGE; i++) {
							alist.add(m.get(i));

						}
					} else {
						for (int i = 0; i < m.size(); i++) {
							alist.add(m.get(i));

						}
					}

					request.setAttribute("groupmemberlist", alist);
					 request.getRequestDispatcher(
			                    "/content/group/" + group + ".progroupView").forward(request,
			                    response);
				}

			} else {
				Node tempGrpNode = null;
				ArrayList<Node> m = new ArrayList<Node>();
				  session = repos.login(new SimpleCredentials("admin", "admin"
		                    .toCharArray()));
		            groupNode = session.getRootNode().getNode("content").getNode("group")
		                    .getNode(group);
		            if (groupNode.hasNode("Member") && groupNode.getNode("Member").hasNodes()) {
		            	NodeIterator iterator = groupNode.getNode("Member").getNodes();
		            	while (iterator.hasNext()){
		            	tempGrpNode = iterator.nextNode();
		                m.add(tempGrpNode);
		               }
		            }
		            request.setAttribute("total",m.size());
				ArrayList<Node> alist = new ArrayList<Node>();
				if (m.size() > NUMBEROFRESULTSPERPAGE) {
					for (int i = 0; i < NUMBEROFRESULTSPERPAGE; i++) {
						alist.add(m.get(i));

					}
				} else {
					for (int i = 0; i < m.size(); i++) {
						alist.add(m.get(i));

					}
				}
				request.setAttribute("groupmemberlist", alist);
				 request.getRequestDispatcher(
		                    "/content/group/" + group + ".groupView").forward(request,
		                    response);
			}

        } else if (request.getRequestPathInfo().getExtension()
                .equals("addedGroup")) {
            String user = request.getParameter("userId");
            request.getRequestDispatcher(
                    "/content/user/" + user + ".groupsAdded").forward(request,
                    response);

        } if (request.getRequestPathInfo().getExtension()
                .equals("addToGroup")) {
        String groupid=request.getParameter("groupid");
        String idetity=request.getParameter("identifier");
        String email=request.getParameter("useremail");
        	if(groupid!=null && idetity!=null && email!=null){
        		if(!groupid.equals("") && !idetity.equals("") && !email.equals("")){
        			Session  session = repos.login(new SimpleCredentials("admin", "admin"
                            .toCharArray()));
        			Node group=session.getRootNode().getNode("content").getNode("group");
        			if(group.hasNode(groupid) && group.getNode(groupid).hasNode("invities")){
        				group=group.getNode(groupid).getNode("invities");
        				if(group.hasNode(email.replace("@", "_"))){
        					String identy=group.getNode(email).getProperty("identifier").getString();
        					if(identy.equals(idetity)){
        						service.addMember(groupid,
        								email, true,
        								"yes");
        						  response.sendRedirect(request.getContextPath()
        				                    + "/servlet/group/show.viewGroup?id=" + groupid);
        					}else{
                				response.getWriter().print("Sorry this is not valid link 5!");
                			}
        					
        				}else{
            				response.getWriter().print("Sorry this is not valid link 4!");
            			}
        			}else{
        				response.getWriter().print("Sorry this is not valid link 3!");
        			}
        		}else{
        			response.getWriter().print("Sorry this is not valid link 2!");	
        		}
        	
        	}else{
        		response.getWriter().print("Sorry this is not valid link !1");
        	}
        }
    }catch(Exception e){
    	response.getWriter().print("Sorry this is not valid link !");
    	}
    }

}
