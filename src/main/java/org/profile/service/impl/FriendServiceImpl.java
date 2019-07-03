package org.profile.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import org.profile.service.ActiveMQProducer;
import org.profile.service.FriendService;
import org.profile.service.ProfileService;
import org.profile.service.SecurityService;

/**
 * <code>FriendServiceImpl</code> contains the method implementation of user's
 * friend section. This contains functionality like: sending and receiving
 * friend request, disconnecting friends, import contacts from Social Networking
 * sites, upload contacts of Linkedin, Find mutual friends, Get Online friends,
 * Check if user is online or not.
 * 
 * @author pranav.arya
 * 
 */
@Component(configurationFactory = true)
@Service(FriendService.class)
@Properties({ @Property(name = "friendService", value = "friend") })
public class FriendServiceImpl implements FriendService {

    /**
     * Object of ProfileService interface
     */
    @Reference
    private ProfileService service_profile;

    /**
     * Object of SlingRepository
     */
    @Reference
    private SlingRepository repos;

    /**
     * Object of SecurityService
     */
    @Reference
    private SecurityService security_service;

    /**
     * Used to read from Property file
     */
    private ResourceBundle bundle;

    @Reference
    private SecurityService securityService;

    @Reference
    private ActiveMQProducer activeMQ;

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.FriendService#connectFriend(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public void connectFriend(String userId, String friend, String sender,
            String request, String friendMessage, String friendType) {
        Node node, connection, friendNode, lastNode = null;
        Session session;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userId);

            if (node.hasNode("connection")) {
                connection = node.getNode("connection");
            } else {
                connection = node.addNode("connection");
            }
            if (connection.hasNode("friend")) {
                friendNode = connection.getNode("friend");
            } else {
                friendNode = connection.addNode("friend");
            }
            if (friendNode.hasNode(friend)) {
                lastNode = friendNode.getNode(friend);
            } else {
                lastNode = friendNode.addNode(friend);
            }
            lastNode.setProperty("request", request);
            lastNode.setProperty("friendType", friendType);
            lastNode.setProperty("friendMessage", friendMessage);
            lastNode.setProperty("requestType", sender);
            lastNode.setProperty("requestDate", dateFormat.format(date));
            lastNode.setProperty("notify", "true");
            // lastNode.setProperty("requestPath",dateFormat.format(date));
            StringBuilder userEmail = new StringBuilder(userId);
            StringBuilder friendEmail = new StringBuilder(friend);
            if (!userEmail.toString().contains("@")) {
                int index = userEmail.toString().lastIndexOf("_");
                userEmail.setCharAt(index, '@');
            }
            if (!friendEmail.toString().contains("@")) {
                int index = friendEmail.toString().lastIndexOf("_");
                friendEmail.setCharAt(index, '@');
            }
            if (sender.equals("sender")) {
                Map<String, String> property = null;
                if (request.equals("pending")) {
                    property = new HashMap<String, String>();
                    property.put("requestFrom", userEmail.toString());
                    property.put("requestTo", friendEmail.toString());
                    property.put("relationType",
                            lastNode.hasProperty("friendType") ? lastNode
                                    .getProperty("friendType").getString()
                                    : friendType);
                    activeMQ.producerCall("ConnectionRequest", property, "");
                } else if (request.equals("accept")) {
                    property = new HashMap<String, String>();
                    property.put("acceptFor", userId.toString());
                    property.put("acceptBy", friendEmail.toString());
                    property.put("relationType",
                            lastNode.hasProperty("friendType") ? lastNode
                                    .getProperty("friendType").getString() : "");
                    activeMQ.producerCall("ConnectionAccept", property, "");
                }
            }
            session.save();

            if (request.equals("accept")) {
                ArrayList<Map> activityStream = new ArrayList<Map>();
                Map<String, String> map = new HashMap<String, String>();
                activityStream = new ArrayList<Map>();
                map.put("key", "message");
                map.put("value", "is now connected to ");
                activityStream.add(map);
                map = new HashMap<String, String>();
                map.put("key", "friend");
                map.put("value", friend);
                activityStream.add(map);
                service_profile.activityStream(userId, activityStream,
                        "Connection", "");
            }

        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.FriendService#importContacts(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @SuppressWarnings("unused")
    public void importContacts(String userName, String providerId,
            String emailNode, String email, String firstName, String lastName) {

        Node node, connection, friendNode, lastNode = null;
        Session session;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            // response.getOutputStream().print("if in");

            node = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userName);

            if (node.hasNode("ContactList")) {
                connection = node.getNode("ContactList");
            } else {
                connection = node.addNode("ContactList");
            }
            if (connection.hasNode(providerId)) {
                friendNode = connection.getNode(providerId);
            } else {
                friendNode = connection.addNode(providerId);
            }
            if (friendNode.hasNode(emailNode)) {
                lastNode = friendNode.getNode(emailNode);
            } else {
                lastNode = friendNode.addNode(emailNode);
            }

            lastNode.setProperty("importedEmail", email);
            lastNode.setProperty("importedFirstName", firstName);
            lastNode.setProperty("importedlastName", lastName);
            session.save();

        } catch (PathNotFoundException e) {

            // response.getOutputStream().print("else"+ar);
            // request.getRequestDispatcher("posts/*.profile").forward(request,
            // response);
            e.printStackTrace();
        } catch (RepositoryException e) {
            // return pl;
            // response.getOutputStream().print("elsew"+ar);
            e.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.profile.service.FriendService#uploadImportedContacts(org.apache.sling
     * .api.SlingHttpServletRequest)
     */
    @SuppressWarnings("unused")
    public String uploadImportedContacts(SlingHttpServletRequest request)
            throws ServletException, IOException {

        String userName = request.getParameter("userId");
        String extention = "";
        ArrayList<String> userList = new ArrayList<String>();
        ArrayList<String> contact = new ArrayList<String>();
        for (Entry<String, RequestParameter[]> e : request
                .getRequestParameterMap().entrySet()) {
            for (RequestParameter p : e.getValue()) {
                if (!p.isFormField()) {
                    extention = p.getFileName().substring(
                            p.getFileName().lastIndexOf("."),
                            p.getFileName().length());
                    /* if(extention.equals(".csv")){ */
                    try {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(p.getInputStream()));
                        String line = "";
                        StringTokenizer st = null;

                        int lineNumber = 0;
                        int tokenNumber = 0;

                        while ((line = br.readLine()) != null) {
                            lineNumber++;

                            // use comma as token separator
                            st = new StringTokenizer(line, ",");
                            if (request.getParameter("type") != null
                                    && request.getParameter("type").equals(
                                            "linkedin")) {
                                contact = new ArrayList<String>();
                                if (lineNumber != 1) {
                                    while (st.hasMoreTokens()) {
                                        tokenNumber++;
                                        contact.add(st.nextToken().replaceAll(
                                                "\"", ""));
                                    }
                                    String emailNode = contact.get(5)
                                            .toString().replaceAll("@", "_");
                                    addImportedContacts(userName, "linkedin",
                                            emailNode, contact);
                                }
                                tokenNumber = 0;
                            } else if (request.getParameter("type") != null
                                    && request.getParameter("type").equals(
                                            "other")) {
                                if (lineNumber != 1) {
                                    userList = new ArrayList<String>();
                                    while (st.hasMoreTokens()) {
                                        tokenNumber++;
                                        userList.add(st.nextToken().replaceAll(
                                                "\"", ""));
                                        System.out.println(userList);
                                    }
                                    if (userList.get(2) != null
                                            && !userList.get(2).equals("")) {
                                        importContacts(
                                                userName,
                                                "Others",
                                                userList.get(2)
                                                        .replace("@", "_")
                                                        .trim(), userList
                                                        .get(2).trim(),
                                                userList.get(0).trim(),
                                                userList.get(1).trim());
                                    }

                                }
                            }
                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                        return "---error---";
                    }

                }
            }
        }

        return extention;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.profile.service.FriendService#addImportedContacts(java.lang.String,
     * java.lang.String, java.lang.String, java.util.ArrayList)
     */
    @SuppressWarnings("unused")
    public void addImportedContacts(String userName, String providerId,
            String emailNode, ArrayList<String> importedValues) {

        Node node, connection, friendNode, lastNode = null;
        Session session;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        ArrayList<String> contact = importedContactValues();
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userName);

            if (node.hasNode("ContactList")) {
                connection = node.getNode("ContactList");
            } else {
                connection = node.addNode("ContactList");
            }
            if (connection.hasNode(providerId)) {
                friendNode = connection.getNode(providerId);
            } else {
                friendNode = connection.addNode(providerId);
            }
            if (friendNode.hasNode(emailNode)) {
                lastNode = friendNode.getNode(emailNode);
            } else {
                lastNode = friendNode.addNode(emailNode);
            }
            for (int i = 0; i < 59; i++) {

                lastNode.setProperty(contact.get(i), importedValues.get(i));

            }
            session.save();

        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }

    }

    /**
     * Used to add the all Column values of Linkedin Excel
     * 
     * @return the Arraylist
     */
    private ArrayList<String> importedContactValues() {

        ArrayList<String> contact = new ArrayList<String>();
        contact.add("importedTitle");
        contact.add("importedFirstName");
        contact.add("importedMiddleName");
        contact.add("importedLastName");
        contact.add("importedSuffix");
        contact.add("importedEmail");
        contact.add("importedE-mail2Address");
        contact.add("importedE-mail3Address");
        contact.add("importedBusinessStreet");
        contact.add("importedBusinessStreet2");
        contact.add("importedBusinessStreet3");
        contact.add("importedBusinessCity");
        contact.add("importedBusinessState");
        contact.add("importedBusinessPostalCode");
        contact.add("importedBusinessCountry");
        contact.add("importedHomeStreet");
        contact.add("importedHomeStreet2");
        contact.add("importedHomeStreet3");
        contact.add("importedHomeCity");
        contact.add("importedHomeState");
        contact.add("importedHomePostalCode");
        contact.add("importedHomeCountry");
        contact.add("importedOtherStreet");
        contact.add("importedOtherStreet2");
        contact.add("importedOtherStreet3");
        contact.add("importedOtherCity");
        contact.add("importedOtherState");
        contact.add("importedOtherPostalCode");
        contact.add("importedOtherCountry");
        contact.add("importedCompany");
        contact.add("importedDepartment");
        contact.add("importedJobTitle");
        contact.add("importedAssistantPhone");
        contact.add("importedBusinessFax");
        contact.add("importedBusinessPhone");
        contact.add("importedBusinessPhone2");
        contact.add("importedCallback");
        contact.add("importedCarPhone");
        contact.add("importedCompanyMainPhone");
        contact.add("importedHomeFax");
        contact.add("importedHomePhone");
        contact.add("importedHomePhone2");
        contact.add("importedISDN");
        contact.add("importedMobilePhone");
        contact.add("importedOtherFax");
        contact.add("importedOtherPhone");
        contact.add("importedPager");
        contact.add("importedPrimaryPhone");
        contact.add("importedRadioPhone");
        contact.add("importedTTY-TDDPhone");
        contact.add("importedTelex");
        contact.add("importedAssistantName");
        contact.add("importedBirthday");
        contact.add("importedManagerName");
        contact.add("importedNotes");
        contact.add("importedOtherAddressPOBox");
        contact.add("importedSpouse");
        contact.add("importedWebPage");
        contact.add("importedPersonalWebPage");

        return contact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.FriendService#mutualFriend(java.lang.String,
     * java.lang.String)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ArrayList mutualFriend(String userId, String friendId) {
        Node node = null, friendNode = null;
        NodeIterator userFriendNodes = null, friendFriendNodes = null;
        Session session;
        ArrayList userFriendList = new ArrayList();
        ArrayList friendFriendList = new ArrayList();
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {

                node = session.getRootNode().getNode("content").getNode("user")
                        .getNode(userId);
            }

            if (node.hasNode("connection")
                    && node.getNode("connection").hasNode("friend")
                    && node.getNode("connection").getNode("friend").hasNodes()) {

                userFriendNodes = node.getNode("connection").getNode("friend")
                        .getNodes();
                while (userFriendNodes.hasNext()) {

                    userFriendList.add(userFriendNodes.nextNode().getName());
                }
            }

            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(friendId)) {

                friendNode = session.getRootNode().getNode("content")
                        .getNode("user").getNode(friendId);
            }

            if (friendNode.hasNode("connection")
                    && friendNode.getNode("connection").hasNode("friend")
                    && friendNode.getNode("connection").getNode("friend")
                            .hasNodes()) {

                friendFriendNodes = friendNode.getNode("connection")
                        .getNode("friend").getNodes();
                while (friendFriendNodes.hasNext()) {

                    friendFriendList
                            .add(friendFriendNodes.nextNode().getName());
                }

                friendFriendList.retainAll(userFriendList);
            }

        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return friendFriendList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.FriendService#onlineFriend(java.lang.String)
     */
    public ArrayList<String> onlineFriend(String userId) {
        bundle = ResourceBundle.getBundle("server");
        String url = bundle.getString("rave.portal") + "/portal/read/session/"
                + userId + ".html";
        String[] paramName = { "" };
        String[] paramValue = { "" };
        ArrayList<String> onlineUsers = new ArrayList<String>();
        String online = security_service
                .callService(url, paramName, paramValue);
        if (!online.equals("invalid") && (!StringUtils.isEmpty(online))) {
            for (String users : (online.replace("[", "").replace("]", ""))
                    .split(",")) {
                String seperated = users.replaceAll("\\s+", "");
                onlineUsers.add(seperated);
            }
        }

        return onlineUsers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.FriendService#onlineCheck(java.lang.String)
     */
    public String onlineCheck(String userId) {
        bundle = ResourceBundle.getBundle("server");
        String url = bundle.getString("rave.portal") + "/portal/read/session/"
                + userId + ".html";
        String[] paramName = { "" };
        String[] paramValue = { "" };
        ArrayList<String> onlineUsers = new ArrayList<String>();
        String online = security_service
                .callService(url, paramName, paramValue);
        if (!online.equals("invalid") && (!StringUtils.isEmpty(online))) {
            for (String users : (online.replace("[", "").replace("]", ""))
                    .split(",")) {
                String seperated = users.replaceAll("\\s+", "");
                onlineUsers.add(seperated);
            }
            if (onlineUsers.contains(userId)) {
                return "true";
            }
        }

        return "false";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.FriendService#disConnectFriend(java.lang.String,
     * java.lang.String)
     */
    public void disConnectFriend(String userId, String friend) {
        Session session;
        Node node, connection = null, friendNode = null, lastNode = null;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userId);

            if (node.hasNode("connection")) {
                connection = node.getNode("connection");
            }
            if (connection.hasNode("friend")) {
                friendNode = connection.getNode("friend");
            }
            if (friendNode.hasNode(friend)) {
                lastNode = friendNode.getNode(friend);
            }
            lastNode.remove();
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
     * @see
     * org.profile.service.FriendService#importContactList(java.lang.String,
     * java.lang.String)
     */
    public void importContactList(String userId, String contactList) {
        try {
            JSONArray jarray = new JSONArray(contactList);
            System.out.println(jarray.length());
            if (jarray != null && jarray.length() > 0) {
                for (int i = 0; i < jarray.length(); i++) {
                    try {
                        JSONObject json = jarray.getJSONObject(i);

                        if (json.has("providerId") && json.has("email")) {
                            importContacts(
                                    userId,
                                    json.getString("providerId"),
                                    json.getString("email")
                                            .replaceAll("@", "_"),
                                    json.getString("email"),
                                    json.has("firstName") ? json
                                            .getString("firstName") : "",
                                    json.has("lastName") ? json
                                            .getString("lastName") : "");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendInvite(String userList, final String userId,
            final String providerId, String messageBody) {
        final String[] list = userList.split(",");
        final String[] paramName = { "emailto[]", "emailfrom[]", "emailsub[]",
                "emailmsg[]" };
        bundle = ResourceBundle.getBundle("server");
        final String url = bundle.getString("sendMail.address");
        Runnable task = new Runnable() {
            public void run() {
                Session session = null;
                Node rootNode, contactNode = null, invitee = null;
                String message = "";
                try {

                    session = repos.login(new SimpleCredentials("admin",
                            "admin".toCharArray()));
                    rootNode = session.getNode("/content/user/" + userId);
                    if (rootNode.hasNode("ContactList")
                            && rootNode.getNode("ContactList").hasNode(
                                    providerId)) {
                        contactNode = rootNode.getNode("ContactList").getNode(
                                providerId);
                        message = MessageFormat
                                .format(bundle.getString("invitationMail.body"),
                                        (rootNode.hasProperty("name") ? rootNode
                                                .getProperty("name")
                                                .getString() : "")
                                                + " "
                                                + (rootNode
                                                        .hasProperty("lastName") ? rootNode
                                                        .getProperty("lastName")
                                                        .getString()
                                                        : ""),
                                        rootNode.hasProperty("headline") ? rootNode
                                                .getProperty("headline")
                                                .getString() : "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String[] paramValue = { "", bundle.getString("sendMail.from"),
                        "You are invited", message };
                for (int i = 0; i < list.length; i++) {
                    paramValue[0] = list[i].trim();
                    securityService.callService(url, paramName, paramValue);
                    try {
                        if (contactNode.hasNode(list[i].replace("@", "_")
                                .trim())) {
                            invitee = contactNode.getNode(list[i].replace("@",
                                    "_").trim());
                            invitee.setProperty("invited", "true");
                            invitee.setProperty("status", "pending");
                        }

                        session.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(task, userId + "Send Invite to contacts").start();

    }

    @SuppressWarnings("deprecation")
    public String friendRequestSchedular() {
        Session session = null;
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            Node rootNode = session.getNode("/content/user");
            String query2 = "select * from nt:base where invited='true' "
                    + "and jcr:path LIKE '/content/user/%/ContactList/%/%'";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager().createQuery(query2, Query.SQL);

            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();
            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                if (rootNode.hasNode(node.getName())) {
                    json = new JSONObject();
                    connectFriend(node.getParent().getParent().getParent()
                            .getName(), node.getName(), "sender", "pending",
                            "Invited through " + node.getParent().getName(),
                            "Friend");
                    connectFriend(node.getName(), node.getParent().getParent()
                            .getParent().getName(), "reciever", "pending",
                            "Invited through " + node.getParent().getName(),
                            "Friend");
                    json.accumulate("RequestFrom", node.getParent().getParent()
                            .getParent().getName());
                    json.accumulate("RequestTo", node.getName());
                    jarray.put(json);

                    node.remove();
                }
            }
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }

        return jarray.toString();
    }

    public void accpetInvitation(String identifier) {
        Session session = null;
        Node friendNode, userNode, connection;
        String userId = "";
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            friendNode = session.getNodeByIdentifier(identifier);
            userId = friendNode.getParent().getParent().getParent().getName();
            userNode = session.getNode("/content/user/" + userId);
            if (userNode.hasNode("connection")
                    && userNode.getNode("connection").hasNode("friend")
                    && userNode.getNode("connection").getNode("friend")
                            .hasNode(userId)) {
                connection = userNode.getNode("connection").getNode("friend")
                        .getNode(userId);
                friendNode.setProperty("request", "accpet");
                connection.setProperty("request", "accpet");
            }

            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public String friendInvitationStatus(String userId, String friendId) {
        Session session = null;
        JSONObject json = new JSONObject();
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            String query2 = "select * from nt:base where contains(status,'*') "
                    + "and importedEmail='" + friendId
                    + "' and jcr:path LIKE '/content/user/"
                    + userId.replace("@", "_") + "/ContactList/%/"
                    + friendId.replace("@", "_") + "'";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager().createQuery(query2, Query.SQL);

            QueryResult qr = query.execute();
            NodeIterator iterator = qr.getNodes();
            if (iterator.getSize()==0) {
                json.accumulate("status", "notfound");
            }
            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                if (node.hasProperty("status")) {
                    json.accumulate("status", node.getProperty("status")
                            .getString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }
        return json.toString();
    }

    @SuppressWarnings("deprecation")
    public void updateInvitationStatus(String userId, String friendId,
            String status) {
        Session session = null;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            String query2 = "select * from nt:base where importedEmail='"
                    + friendId + "' and jcr:path LIKE '/content/user/"
                    + userId.replace("@", "_") + "/ContactList/%/"
                    + friendId.replace("@", "_") + "'";
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager().createQuery(query2, Query.SQL);

            QueryResult qr = query.execute();
            NodeIterator iterator = qr.getNodes();
            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                node.setProperty("status", status);
            }
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }

    }

}
