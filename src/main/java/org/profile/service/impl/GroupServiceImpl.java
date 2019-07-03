package org.profile.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.GroupService;
import org.profile.service.ProfileService;

import org.apache.sling.api.request.RequestParameter;
import javax.jcr.Node;

/**
 * The Class <code>GroupServiceImpl</code> contains the functionality like:
 * Group creation, Join group, Dis-connect Group, Search Group, Delete Group.
 * 
 * @author pranav.arya
 */
@Component(configurationFactory = true)
@Service(GroupService.class)
@Properties({ @Property(name = "group", value = "groupService") })
public class GroupServiceImpl implements GroupService {

    /** The repos is an object of SlingRepository */
    @Reference
    private SlingRepository repos;

    /** The service_profile is an object of ProfileService */
    @Reference
    private ProfileService service_profile;

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.GroupService#saveGroupInfo(org.apache.sling.api.
     * SlingHttpServletRequest)
     */
    public Long saveGroupInfo(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String group = request.getParameter("groupid");
        String id="";
        Node node=null, groupNode=null,picNode=null,jcrNode=null;
        Session session;
        long a=0;
        DateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy");
        Date date = new Date();

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            if (session.getRootNode().getNode("content").hasNode("group")) {
                groupNode = session.getRootNode().getNode("content")
                        .getNode("group");
            } else {
                groupNode = session.getRootNode().getNode("content")
                        .addNode("group");
                groupNode.setProperty("groupCount", 0);
            }
           
	if(!group.equals("new")){
		node = groupNode.getNode(group);
                a=Long.parseLong(group);
           }else{
        	  // response.getWriter().print("Enter--");
        	Long s=groupNode.getProperty("groupCount").getLong();
        	String f=Long.toString(s);
            	node = groupNode.addNode(f);
                node.setProperty("groupDate", dateFormat.format(date)); 
                a=s;
                node.setProperty("groupid", s+1);
                groupNode.setProperty("groupCount", s+1);
               // response.getWriter().print("Exit----))-"+s);
            }
    
		   //  a=groupNode.getProperty("groupCount").getLong();
            node.setProperty("groupName", request.getParameter("groupName"));
            node.setProperty("groupType", request.getParameter("groupType"));
            node.setProperty("groupSummary",
                    request.getParameter("groupSummary"));
            node.setProperty("groupDescription",
                    request.getParameter("groupDescription"));
            node.setProperty("groupWebsite",
                    request.getParameter("groupWebsite"));
            node.setProperty("groupOwnerEmail",
                    request.getParameter("groupOwnerEmail"));
            node.setProperty("groupAccess", request.getParameter("groupAccess"));
            node.setProperty("groupOwnerEmail", request.getRemoteUser().replace("@","_"));
			if (request.getParameter("file") != null
					&& !request.getParameter("file").equals("")) {
				RequestParameter[] ap = request.getRequestParameterMap()
						.get("file");
				for (int i = 0; i < ap.length; i++) {
					
					String filenam = ap[i].getFileName();
					//response.getWriter().print("inside file----)))-"+group);
					if(filenam !=null && !filenam.equals("")){
					if (node.hasNodes()) {
						picNode=node.getNode("picture");
						picNode.remove();
						picNode = node.addNode("picture", "nt:file");
						jcrNode = picNode.addNode("jcr:content","nt:resource");

						jcrNode.setProperty("jcr:data",
								ap[i].getInputStream());

						jcrNode.setProperty("jcr:mimeType",
								"image/jpeg");
					} else {
						picNode = node.addNode("picture", "nt:file");
						jcrNode = picNode.addNode("jcr:content",
								"nt:resource");

						jcrNode.setProperty("jcr:data",
								ap[i].getInputStream());

						jcrNode.setProperty("jcr:mimeType",
								"image/jpeg");
					}
					}
				}

			}
			//response.getWriter().print("----))))-"+group);
            session.save();

        } catch (PathNotFoundException e) {
        	response.getWriter().print("Path----)))-"+e.getMessage());
            e.printStackTrace();
        } catch (RepositoryException e) {
        	response.getWriter().print("Rrespo----)))-"+e.getMessage());
            e.printStackTrace();
        }
    return a;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.GroupService#searchGroup(java.lang.String)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ArrayList searchGroup(String keyword) {
        ArrayList groupList = new ArrayList();
        try {
            Session session = repos.login(new SimpleCredentials("admin",
                    "admin".toCharArray()));

            Workspace workspace = session.getWorkspace();

            Query query = workspace.getQueryManager().createQuery(

                    "select * from [nt:unstructured] where  (contains('groupName', '*"
                            + keyword + "*')) "
                            + "and ISDESCENDANTNODE('/content/group')",
                    Query.JCR_SQL2);

            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();

            while (iterator.hasNext()) {

                groupList.add(iterator.nextNode());
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        return groupList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.GroupService#addMember(java.lang.String,
     * java.lang.String, boolean, java.lang.String)
     */
    @SuppressWarnings({ "unused", "rawtypes" })
    public void addMember(String groupName, String userId, boolean flag,
            String acceptPrivate) {

        Node node, groupNode = null, memberNode, groupChildNode, userChildNode, groupsAddedNode, userGroupNode, requestNode, requestGroupNode, requestAddedGroup, requestAddedMemeber, userNode = null;
        Session session;
        ArrayList<Map> activityStream = new ArrayList<Map>();
        Map<String, String> map = new HashMap<String, String>();

        try {
            activityStream = new ArrayList<Map>();
            map.put("key", "message");
            map.put("value", "has joined : ");
            activityStream.add(map);
            map = new HashMap<String, String>();
            map.put("key", "groupName");
            map.put("value", groupName);
            activityStream.add(map);

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            if (session.getRootNode().getNode("content").getNode("group")
                    .hasNode(groupName)) {
                groupNode = session.getRootNode().getNode("content")
                        .getNode("group").getNode(groupName);
            }

            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {
                userNode = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);
            }

            /* Group Node */

            if (groupNode.hasNode("Member")) {
                memberNode = groupNode.getNode("Member");
            } else {
                memberNode = groupNode.addNode("Member");
            }

            if (memberNode.hasNode(userId)) {
                groupChildNode = memberNode.getNode(userId);
            } else {
                groupChildNode = memberNode.addNode(userId);
            }

            /* User Node */
            if (userNode.hasNode("Groups")) {
                userGroupNode = userNode.getNode("Groups");
            } else {
                userGroupNode = userNode.addNode("Groups");
            }

            if (userGroupNode.hasNode("GroupsAdded")) {
                groupsAddedNode = userGroupNode.getNode("GroupsAdded");
            } else {
                groupsAddedNode = userGroupNode.addNode("GroupsAdded");
            }

            if (groupsAddedNode.hasNode(groupName)) {
                userChildNode = groupsAddedNode.getNode(groupName);
            } else {
                userChildNode = groupsAddedNode.addNode(groupName);
            }

            if (flag
                    && groupNode.getProperty("groupAccess").getString()
                            .equals("private")) {

                userChildNode.setProperty("groupRequest", "pending");
                groupChildNode.setProperty("groupRequest", "pending");
                String groupCreator = groupNode.getProperty("groupOwnerEmail")
                        .getString();

                if (session.getRootNode().getNode("content").getNode("user")
                        .getNode(groupCreator).hasNode("Groups")) {

                    requestNode = session.getRootNode().getNode("content")
                            .getNode("user").getNode(groupCreator)
                            .getNode("Groups");
                } else {

                    requestNode = session.getRootNode().getNode("content")
                            .getNode("user").getNode(groupCreator)
                            .addNode("Groups");
                }

                if (requestNode.hasNode("GroupRequests")) {
                    requestGroupNode = requestNode.getNode("GroupRequests");
                } else {
                    requestGroupNode = requestNode.addNode("GroupRequests");
                }

                if (requestGroupNode.hasNode(groupName)) {
                    requestAddedGroup = requestGroupNode.getNode(groupName);
                } else {
                    requestAddedGroup = requestGroupNode.addNode(groupName);
                }

                if (requestAddedGroup.hasNode(userId)) {
                    requestAddedMemeber = requestAddedGroup.getNode(userId);
                } else {
                    requestAddedMemeber = requestAddedGroup.addNode(userId);
                }
                if (acceptPrivate.equals("yes")) {
                    requestAddedMemeber.remove();
                    userChildNode.setProperty("groupRequest", "accepted");
                    groupChildNode.setProperty("groupRequest", "accepted");
                    service_profile.activityStream(userId, activityStream,
                            "Group", "");
                }

            } else {
                userChildNode.setProperty("groupRequest", "accepted");
                groupChildNode.setProperty("groupRequest", "accepted");
                service_profile.activityStream(userId, activityStream, "Group",
                        "");

            }

            session.save();
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.GroupService#deletNode(java.lang.String)
     */
    public boolean deletNode(String group) {
        Session session;
        Node pictureNode = null;
		Node groupNode;
		boolean status=false;
        try {
		session = repos.login(new SimpleCredentials("admin", "admin"
						.toCharArray()));
				groupNode = session.getRootNode().getNode("content/group");
						
				if (groupNode.hasNode(group)) {
					groupNode.getNode(group).remove();
					status=true;
				} 
			
				session.save();
            
        } catch (PathNotFoundException e) {

//            e.printStackTrace();
            status=false;
        } catch (RepositoryException e) {
        	 status=false;
//            e.printStackTrace();
        }
        return status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.GroupService#unJoinGroup(java.lang.String,
     * java.lang.String)
     */
    public void unJoinGroup(String groupName, String userId) {
        Node groupNode = null, memberNode, groupChildNode, userChildNode, groupsAddedNode = null, userGroupNode, userNode = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            if (session.getRootNode().getNode("content").getNode("group")
                    .hasNode(groupName)) {
                groupNode = session.getRootNode().getNode("content")
                        .getNode("group").getNode(groupName);
            }

            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {
                userNode = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);
            }

            /* Group Node */

            if (groupNode.hasNode("Member")) {
                memberNode = groupNode.getNode("Member");
            } else {
                memberNode = groupNode.addNode("Member");
            }

            if (memberNode.hasNode(userId)) {
                groupChildNode = memberNode.getNode(userId);
                groupChildNode.remove();
            }

            /* User Node */
            if (userNode.hasNode("Groups")) {
                userGroupNode = userNode.getNode("Groups");
            } else {
                userGroupNode = userNode.addNode("Groups");
            }

            if (userGroupNode.hasNode("GroupsAdded")) {
                groupsAddedNode = userGroupNode.getNode("GroupsAdded");
            }

            if (groupsAddedNode.hasNode(groupName)) {
                userChildNode = groupsAddedNode.getNode(groupName);
                userChildNode.remove();
            }

            session.save();
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }

    }
}
