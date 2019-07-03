package org.profile.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.servlet.ServletException;
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
import org.profile.service.ProfileService;
import javax.jcr.NodeIterator;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

@Component(configurationFactory = true)
@Service(ProfileService.class)
@Properties({ @Property(name = "service", value = "profileShow") })
public class ProfileServiceImpl implements ProfileService {

    @Reference
    private SlingRepository repos;

    @Reference
    private ActiveMQProducer activeMQ;

    @SuppressWarnings({ "unused", "rawtypes" })
    
    public String saveProfileNode(SlingHttpServletRequest request)
            throws ServletException, IOException {
        String userId = request.getParameter("title");
        Node node1, contentNode, userNode = null;
        Session session;
        ArrayList<Map> activityStream = new ArrayList<Map>();
        Map<String, String> map;
        StringBuilder producerEmail = new StringBuilder("");
        StringBuilder producerEmailVerification = new StringBuilder("");
        StringBuilder phoneNumber = new StringBuilder("");
        StringBuilder phoneNumberVerification = new StringBuilder("");
        ArrayList<String> phoneVerificationList = new ArrayList<String>();
        ArrayList<String> emailVerificationList = new ArrayList<String>();
        JSONArray phoneArray = new JSONArray();
        JSONArray emailArray = new JSONArray();
        JSONObject phoneObject = new JSONObject();
        JSONObject emailObject = new JSONObject();
        boolean completeStatus = false;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (!session.getRootNode().hasNode("content")) {

                contentNode = session.getRootNode().addNode("content");
            }
            if (!session.getRootNode().getNode("content").hasNode("user")) {

                userNode = session.getRootNode().getNode("content")
                        .addNode("user");
            }
            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);

            } else {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").addNode(userId);

            }

            node1.setProperty("title", request.getParameter("title"));
            node1.setProperty("address", request.getParameter("address"));
            node1.setProperty("birthDay", request.getParameter("birthDay"));
            node1.setProperty("city", request.getParameter("city"));
            if (request.getParameterMap().containsKey("primaryEmail")) {
                node1.setProperty("primaryEmail",
                        request.getParameter("primaryEmail"));
            }
            if (request.getParameterMap().containsKey("primaryMobile")) {
                node1.setProperty("primaryMobile",
                        request.getParameter("primaryMobile"));
            }
            if (request.getParameterMap().containsKey("extension")) {
                node1.setProperty("extension",
                        request.getParameter("extension"));
            }
            if (request.getParameterMap().containsKey("secondaryId")) {
                node1.setProperty("secondaryId",
                        request.getParameter("secondaryId"));
            }
            node1.setProperty("email", request.getParameterValues("email"));
            if(request.getParameter("headline") !=null){
            node1.setProperty("headline", request.getParameter("headline"));
            }else{
            	node1.setProperty("headline", "");
            }
            node1.setProperty("im", request.getParameterValues("im"));
            node1.setProperty("imType", request.getParameterValues("imType"));
            node1.setProperty("industry", request.getParameter("industry"));

            node1.setProperty("lastName", request.getParameter("lastName"));
            node1.setProperty("maidenName", request.getParameter("maidenName"));
            node1.setProperty("name", request.getParameter("name"));
            node1.setProperty("profileImage",
                    request.getParameter("profileImage"));
            node1.setProperty("number", request.getParameterValues("number"));
            node1.setProperty("numberType",
                    request.getParameterValues("numberType"));
            node1.setProperty("country", request.getParameter("country"));
            node1.setProperty("state", request.getParameter("state"));
            node1.setProperty("pinCode", request.getParameter("pinCode"));
            node1.setProperty("status", request.getParameter("status"));
            if(request.getParameter("userStatus") ==null){
            node1.setProperty("userStatus", "");
            }
            Node contact = null;
            if (node1.hasNode("ContactDetails")) {

                contact = node1.getNode("ContactDetails");
            } else {

                contact = node1.addNode("ContactDetails");
            }
            Node phone = null;
            if (contact.hasNode("Phone")) {
                phone = contact.getNode("Phone");
            } else {
                phone = contact.addNode("Phone");
            }

            if (request.getParameterValues("number")[0] != "") {

                String[] numberValue = request.getParameterValues("number");
                String[] numberTypeValue = request
                        .getParameterValues("numberType");
                NodeIterator nodeIterator = phone.getNodes();
                List<String> numberList = Arrays.asList(numberValue);
                while (nodeIterator.hasNext()) {
                    Node removeNode = nodeIterator.nextNode();
                    if (!numberList.contains(removeNode.getName())) {
                        removeNode.remove();
                    }
                }

                Node phoneValue = null;
                for (int i = 0; i < numberValue.length; i++) {
                    phoneObject = new JSONObject();
                    if (phone.hasNode(numberValue[i])) {
                        phoneValue = phone.getNode(numberValue[i]);
                        phoneNumber.append(phoneValue.getProperty("number").getString());
                        phoneNumberVerification.append(phoneValue
                                .getProperty("verifiedMobileFlag").getString());
                        phoneVerificationList.add(phoneValue.getProperty(
                                "verifiedMobileFlag").getString());
                    } else {
                        phoneValue = phone.addNode(numberValue[i]);
                        phoneValue.setProperty("number", numberValue[i]);
                        phoneValue
                                .setProperty("numberType", numberTypeValue[i]);
                        phoneValue.setProperty("verifiedMobileFlag", "no");
                        phoneNumber.append(phoneValue.getProperty("number").getString());
                        phoneNumberVerification.append(phoneValue
                                .getProperty("verifiedMobileFlag").getString());
                        phoneVerificationList.add(phoneValue.getProperty(
                                "verifiedMobileFlag").getString());
                    }

                    if (i != numberValue.length - 1) {
                        phoneNumber.append(",");
                        phoneNumberVerification.append(",");
                    }
                    try {
                        phoneObject.accumulate("num", phoneValue.getProperty("number").getString());
                        phoneObject.accumulate("numType", phoneValue.getProperty("numberType").getString());
                        phoneObject.accumulate("active", phoneValue.getProperty("verifiedMobileFlag").getString());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    phoneArray.put(phoneObject);
                }

            }
            if (request.getParameterValues("email")[0] != "") {
                int emailNumber = 0;
                String[] emailValue = request.getParameterValues("email");
                String[] emailValueNode = null;
                if (request.getParameterValues("emailValue")[0] != "") {
                    emailValueNode = request.getParameterValues("emailValue");
                    emailNumber = emailValueNode.length;
                }
                Node email = null;

              //  NodeIterator emailNodes = null;
                boolean flag = false;
                if (contact.hasNode("EmailID")) {
                    email = contact.getNode("EmailID");

                    flag = true;
                } else {
                    email = contact.addNode("EmailID");
                    email.setProperty("emailLength", "0");
                }
                
                NodeIterator emailNodes = email.getNodes();
                List<String> emailList = Arrays.asList(emailValue);
                while (emailNodes.hasNext()) {
                    Node removeNode = emailNodes.nextNode();
                    if (!emailList.contains(removeNode.getName())) {
                        removeNode.remove();
                    }
                }
                
                for (int i = 0; i < emailValue.length; i++) {
                    emailObject = new JSONObject();
                    Node emailNode = null;
                    if (email.hasNode(emailValue[i].replace("@", "_"))) {
                        emailNode = email.getNode(emailValue[i].replace("@",
                                "_"));
                        producerEmail.append(emailNode.getProperty("emailId")
                                .getString());
                        producerEmailVerification.append(emailNode
                                .getProperty("verifiedEmailFlag").getString());
                        emailVerificationList.add(emailNode
                                .getProperty("verifiedEmailFlag").getString());
                    } else {
                        if (emailValueNode != null && i < emailNumber
                                && emailValueNode[i] != null
                                && email.hasNode(emailValueNode[i])) {
                            emailNode = email.getNode(emailValueNode[i]);
                            emailNode.remove();
                        }
                        emailNode = email.addNode(emailValue[i].replace("@",
                                "_"));
                        emailNode.setProperty("emailId", emailValue[i]);
                        emailNode.setProperty("verifiedEmailFlag", "no");
                        producerEmail.append(emailNode.getProperty("emailId")
                                .getString());
                        producerEmailVerification.append(emailNode
                                .getProperty("verifiedEmailFlag").getString());
                        emailVerificationList.add(emailNode
                                .getProperty("verifiedEmailFlag").getString());
                    }
                    if (i != emailValue.length - 1) {
                        producerEmail.append(",");
                        producerEmailVerification.append(",");
                    }
                    
                    try {
                        emailObject.accumulate("id", emailNode.getProperty("emailId").getString());
                        emailObject.accumulate("active", emailNode.getProperty("verifiedEmailFlag").getString());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    emailArray.put(emailObject);
                }
            }

            if (request.getParameterValues("im")[0] != "") {
                String[] imValue = request.getParameterValues("im");
                String[] imTypeValue = request.getParameterValues("imType");
                Node im = null;
                Node imNode = null;
                if (contact.hasNode("IM")) {
                    im = contact.getNode("IM");
                } else {
                    im = contact.addNode("IM");
                }
                NodeIterator imNodes = im.getNodes();
                List<String> imList = Arrays.asList(imValue);
                while (imNodes.hasNext()) {
                    Node removeNode = imNodes.nextNode();
                    if (!imList.contains(removeNode.getName())) {
                        removeNode.remove();
                    }
                }
                for (int i = 0; i < imValue.length; i++) {
                    if (im.hasNode(imValue[i])) {
                        imNode = im.getNode(imValue[i]);

                    } else {
                        imNode = im.addNode(imValue[i]);
                    }

                    imNode.setProperty("imValue", imValue[i]);
                    imNode.setProperty("imTypeValue", imTypeValue[i]);
                }
            }

            session.save();
           
            JSONObject profileJson = new JSONObject();
            Map<String, String> property = new HashMap<String, String>();
            try{
                profileJson.accumulate("username", node1.getProperty("primaryEmail")
                        .getString());
                profileJson.accumulate("firstname", node1.getProperty("name").getString());
                profileJson.accumulate("lastname", node1.hasProperty("lastName") ? node1
                        .getProperty("lastName").getString() : "");
                profileJson.accumulate("dob", node1.hasProperty("birthDay") ? node1
                        .getProperty("birthDay").getString() : "");
                profileJson.accumulate("emails", emailArray.toString());
                profileJson.accumulate("phones", phoneArray.toString());
                JSONArray addressArray = new JSONArray();
                JSONObject addressObject = new JSONObject();
                addressObject.accumulate("title",
                        node1.hasProperty("address") ? node1.getProperty("address")
                                .getString() : "");
                addressObject.accumulate("city",
                        node1.hasProperty("city") ? node1.getProperty("city")
                                .getString() : "");
                addressObject.accumulate("state", node1.hasProperty("state") ? node1
                        .getProperty("state").getString() : "");
                addressObject.accumulate("country", node1.hasProperty("country") ? node1
                        .getProperty("country").getString() : "");
                addressObject.accumulate("pin", node1.hasProperty("pincode") ? node1
                        .getProperty("pincode").getString() : "");
                addressArray.put(addressObject);
                profileJson.accumulate("address", addressArray.toString());
                profileJson.accumulate("language", "[{\"name\": \"English\", \"default\": \"true\"}]");
                if (node1.hasProperty("state") && node1.hasProperty("country")
                        && node1.hasProperty("city")
                        && !node1.getProperty("city").getString().equals("")
                        && !node1.getProperty("state").getString().equals("")
                        && !node1.getProperty("country").getString().equals("")
                        && !phoneVerificationList.contains("no")
                        && !emailVerificationList.contains("no")) {
                    profileJson.accumulate("completeStatus", "yes");
                }else{
                    profileJson.accumulate("completeStatus", "no");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            property.put("userId", node1.getProperty("primaryEmail")
                    .getString());
            
            property.put("firstName", node1.getProperty("name").getString());
            property.put("lastName", node1.hasProperty("lastName") ? node1
                    .getProperty("lastName").getString() : "");
            property.put("city",
                    node1.hasProperty("city") ? node1.getProperty("city")
                            .getString() : "");
            property.put("state", node1.hasProperty("state") ? node1
                    .getProperty("state").getString() : "");
            property.put("country", node1.hasProperty("country") ? node1
                    .getProperty("country").getString() : "");
            property.put("pincode", node1.hasProperty("pincode") ? node1
                    .getProperty("pincode").getString() : "");
            property.put("emailId", producerEmail.toString());
            property.put("emailVerification",
                    producerEmailVerification.toString());
            property.put("phoneNumber", phoneNumber.toString());
            property.put("phoneNumberVerfication",
                    phoneNumberVerification.toString());
            if (node1.hasProperty("state") && node1.hasProperty("country")
                    && node1.hasProperty("city")
                    && !node1.getProperty("city").getString().equals("")
                    && !node1.getProperty("state").getString().equals("")
                    && !node1.getProperty("country").getString().equals("")
                    && !phoneVerificationList.contains("no")
                    && !emailVerificationList.contains("no")) {
                property.put("completeStatus", "yes");
            }else{
                property.put("completeStatus", "no");
            }
            property.put("eventId", "44");
           // activeMQ.producerCall("ProfileEdit", property, profileJson.toString());
            if (!session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {
                map = new HashMap<String, String>();
                map.put("key", "message");
                map.put("value", "has update his");
                activityStream.add(map);
                map = new HashMap<String, String>();
                map.put("key", "profileType");
                map.put("value", "Profile");
                activityStream.add(map);
                activityStream(userId, activityStream, "Profile", "");
            }
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }

        return "done";
    }
    
    
    public String saveProfileNodeBasic(SlingHttpServletRequest request)
            throws ServletException, IOException {
        String userId = request.getParameter("title");
        Node node1, contentNode, userNode = null,summary = null,info = null;
        Session session;
        ArrayList<Map> activityStream = new ArrayList<Map>();
        Map<String, String> map;
        StringBuilder producerEmail = new StringBuilder("");
        StringBuilder producerEmailVerification = new StringBuilder("");
        StringBuilder phoneNumber = new StringBuilder("");
        StringBuilder phoneNumberVerification = new StringBuilder("");
        ArrayList<String> phoneVerificationList = new ArrayList<String>();
        ArrayList<String> emailVerificationList = new ArrayList<String>();
        JSONArray phoneArray = new JSONArray();
        JSONArray socialArray = new JSONArray();
        JSONArray emailArray = new JSONArray();
        JSONObject phoneObject = new JSONObject();
        JSONObject socialObject = new JSONObject();
        JSONObject emailObject = new JSONObject();
        boolean completeStatus = false;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (!session.getRootNode().hasNode("content")) {

                contentNode = session.getRootNode().addNode("content");
            }
            if (!session.getRootNode().getNode("content").hasNode("user")) {

                userNode = session.getRootNode().getNode("content")
                        .addNode("user");
            }
            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);

            } else {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").addNode(userId);

            }
            
            if (node1.hasNode("AdditionalInformation")) {
                info = node1.getNode("AdditionalInformation");
            } else {
                info = node1.addNode("AdditionalInformation");
            }
            info.setProperty("interests",
                    request.getParameterValues("interests"));
            node1.setProperty("interests",
                    request.getParameterValues("interests"));
            node1.setProperty("title", request.getParameter("title"));
           // node1.setProperty("address", request.getParameter("address"));
            node1.setProperty("birthDay", request.getParameter("birthDay"));
         //   node1.setProperty("city", request.getParameter("city"));
            if (request.getParameterMap().containsKey("primaryEmail")) {
                node1.setProperty("primaryEmail",
                        request.getParameter("primaryEmail"));
            }
            if (request.getParameterMap().containsKey("primaryMobile")) {
                node1.setProperty("primaryMobile",
                        request.getParameter("primaryMobile"));
            }
            if (request.getParameterMap().containsKey("extension")) {
                node1.setProperty("extension",
                        request.getParameter("extension"));
            }
            if (request.getParameterMap().containsKey("secondaryId")) {
                node1.setProperty("secondaryId",
                        request.getParameter("secondaryId"));
            }
            node1.setProperty("email", request.getParameterValues("email"));
            if(request.getParameter("headline") !=null){
            node1.setProperty("headline", request.getParameter("headline"));
            }else{
            	node1.setProperty("headline", "");
            }
            node1.setProperty("im", request.getParameterValues("im"));
            node1.setProperty("imType", request.getParameterValues("imType"));
            //node1.setProperty("industry", request.getParameter("industry"));

            node1.setProperty("lastName", request.getParameter("lastName"));
            node1.setProperty("maidenName", request.getParameter("maidenName"));
            node1.setProperty("name", request.getParameter("name"));
            node1.setProperty("profileImage",
                    request.getParameter("profileImage"));
            node1.setProperty("number", request.getParameterValues("number"));
            node1.setProperty("numberType",
                    request.getParameterValues("numberType"));
           // node1.setProperty("country", request.getParameter("country"));
           // node1.setProperty("state", request.getParameter("state"));
           // node1.setProperty("pinCode", request.getParameter("pinCode"));
            node1.setProperty("status", request.getParameter("status"));
            node1.setProperty("anniversary", request.getParameter("anniversary"));
            node1.setProperty("profilesummary", request.getParameter("profilesummary"));
            node1.setProperty("website",
                    request.getParameter("userwebsite"));
            if (node1.hasNode("ProfessionalSummary")) {
                summary = node1.getNode("ProfessionalSummary");
            } else {
                summary = node1.addNode("ProfessionalSummary");
            }
            summary.setProperty("specialities",
                    request.getParameter("specialities"));
            if(request.getParameter("userStatus") ==null){
            node1.setProperty("userStatus", "");
            }
            Node contact = null;
            if (node1.hasNode("ContactDetails")) {

                contact = node1.getNode("ContactDetails");
            } else {

                contact = node1.addNode("ContactDetails");
            }
            Node phone = null;
            if (contact.hasNode("Phone")) {
                phone = contact.getNode("Phone");
            } else {
                phone = contact.addNode("Phone");
            }

            if (request.getParameterValues("number")[0] != "") {

                String[] numberValue = request.getParameterValues("number");
                String[] numberTypeValue = request
                        .getParameterValues("numberType");
                NodeIterator nodeIterator = phone.getNodes();
                List<String> numberList = Arrays.asList(numberValue);
                while (nodeIterator.hasNext()) {
                    Node removeNode = nodeIterator.nextNode();
                    if (!numberList.contains(removeNode.getName())) {
                        removeNode.remove();
                    }
                }

                Node phoneValue = null;
                for (int i = 0; i < numberValue.length; i++) {
                    phoneObject = new JSONObject();
                    if (phone.hasNode(numberValue[i])) {
                        phoneValue = phone.getNode(numberValue[i]);
                        phoneNumber.append(phoneValue.getProperty("number").getString());
                        phoneNumberVerification.append(phoneValue
                                .getProperty("verifiedMobileFlag").getString());
                        phoneVerificationList.add(phoneValue.getProperty(
                                "verifiedMobileFlag").getString());
                    } else {
                        phoneValue = phone.addNode(numberValue[i]);
                        phoneValue.setProperty("number", numberValue[i]);
                        phoneValue
                                .setProperty("numberType", numberTypeValue[i]);
                        phoneValue.setProperty("verifiedMobileFlag", "no");
                        phoneNumber.append(phoneValue.getProperty("number").getString());
                        phoneNumberVerification.append(phoneValue
                                .getProperty("verifiedMobileFlag").getString());
                        phoneVerificationList.add(phoneValue.getProperty(
                                "verifiedMobileFlag").getString());
                    }

                    if (i != numberValue.length - 1) {
                        phoneNumber.append(",");
                        phoneNumberVerification.append(",");
                    }
                    try {
                        phoneObject.accumulate("num", phoneValue.getProperty("number").getString());
                        phoneObject.accumulate("numType", phoneValue.getProperty("numberType").getString());
                        phoneObject.accumulate("active", phoneValue.getProperty("verifiedMobileFlag").getString());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    phoneArray.put(phoneObject);
                }

            }
            
            Node social = null;
            if (contact.hasNode("Social")) {
            	social = contact.getNode("Social");
            } else {
            	social = contact.addNode("Social");
            }

            if (request.getParameterValues("sociallink")[0] != "") {

                String[] socialTypeValue = request.getParameterValues("socialtype");
                String[] socialValue = request
                        .getParameterValues("sociallink");
                NodeIterator nodeIterator1 = social.getNodes();
                List<String> numberList1 = Arrays.asList(socialTypeValue);
                while (nodeIterator1.hasNext()) {
                    Node removeNode1 = nodeIterator1.nextNode();
                    if (!numberList1.contains(removeNode1.getName())) {
                        removeNode1.remove();
                    }
                }

                Node phoneValue1 = null;
                for (int i = 0; i < socialTypeValue.length; i++) {
                    socialObject = new JSONObject();
                    if (social.hasNode(socialTypeValue[i])) {
                    	phoneValue1 = social.getNode(socialTypeValue[i]);
                    	phoneValue1.setProperty("socialType", socialTypeValue[i]);
                        phoneValue1
                                .setProperty("socialLink", socialValue[i]);
                        
                    } else {
                        phoneValue1 = social.addNode(socialTypeValue[i]);
                        phoneValue1.setProperty("socialType", socialTypeValue[i]);
                        phoneValue1
                                .setProperty("socialLink", socialValue[i]);
                    }

                    try {
                    	socialObject.accumulate("socialType", phoneValue1.getProperty("socialType").getString());
                        socialObject.accumulate("socialLink", phoneValue1.getProperty("socialLink").getString());
                        //phoneObject.accumulate("active", phoneValue.getProperty("verifiedMobileFlag").getString());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    socialArray.put(socialObject);
                }

            }

            
            if (request.getParameterValues("email")[0] != "") {
                int emailNumber = 0;
                String[] emailValue = request.getParameterValues("email");
                String[] emailValueNode = null;
                if (request.getParameterValues("emailValue")[0] != "") {
                    emailValueNode = request.getParameterValues("emailValue");
                    emailNumber = emailValueNode.length;
                }
                Node email = null;

              //  NodeIterator emailNodes = null;
                boolean flag = false;
                if (contact.hasNode("EmailID")) {
                    email = contact.getNode("EmailID");

                    flag = true;
                } else {
                    email = contact.addNode("EmailID");
                    email.setProperty("emailLength", "0");
                }
                
                NodeIterator emailNodes = email.getNodes();
                List<String> emailList = Arrays.asList(emailValue);
                while (emailNodes.hasNext()) {
                    Node removeNode = emailNodes.nextNode();
                    if (!emailList.contains(removeNode.getName())) {
                        removeNode.remove();
                    }
                }
                
                for (int i = 0; i < emailValue.length; i++) {
                    emailObject = new JSONObject();
                    Node emailNode = null;
                    if (email.hasNode(emailValue[i].replace("@", "_"))) {
                        emailNode = email.getNode(emailValue[i].replace("@",
                                "_"));
                        producerEmail.append(emailNode.getProperty("emailId")
                                .getString());
                        producerEmailVerification.append(emailNode
                                .getProperty("verifiedEmailFlag").getString());
                        emailVerificationList.add(emailNode
                                .getProperty("verifiedEmailFlag").getString());
                    } else {
                        if (emailValueNode != null && i < emailNumber
                                && emailValueNode[i] != null
                                && email.hasNode(emailValueNode[i])) {
                            emailNode = email.getNode(emailValueNode[i]);
                            emailNode.remove();
                        }
                        emailNode = email.addNode(emailValue[i].replace("@",
                                "_"));
                        emailNode.setProperty("emailId", emailValue[i]);
                        emailNode.setProperty("verifiedEmailFlag", "no");
                        producerEmail.append(emailNode.getProperty("emailId")
                                .getString());
                        producerEmailVerification.append(emailNode
                                .getProperty("verifiedEmailFlag").getString());
                        emailVerificationList.add(emailNode
                                .getProperty("verifiedEmailFlag").getString());
                    }
                    if (i != emailValue.length - 1) {
                        producerEmail.append(",");
                        producerEmailVerification.append(",");
                    }
                    
                    try {
                        emailObject.accumulate("id", emailNode.getProperty("emailId").getString());
                        emailObject.accumulate("active", emailNode.getProperty("verifiedEmailFlag").getString());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    emailArray.put(emailObject);
                }
            }

            if (request.getParameterValues("im")[0] != "") {
                String[] imValue = request.getParameterValues("im");
                String[] imTypeValue = request.getParameterValues("imType");
                Node im = null;
                Node imNode = null;
                if (contact.hasNode("IM")) {
                    im = contact.getNode("IM");
                } else {
                    im = contact.addNode("IM");
                }
                NodeIterator imNodes = im.getNodes();
                List<String> imList = Arrays.asList(imValue);
                while (imNodes.hasNext()) {
                    Node removeNode = imNodes.nextNode();
                    if (!imList.contains(removeNode.getName())) {
                        removeNode.remove();
                    }
                }
                for (int i = 0; i < imValue.length; i++) {
                    if (im.hasNode(imValue[i])) {
                        imNode = im.getNode(imValue[i]);

                    } else {
                        imNode = im.addNode(imValue[i]);
                    }

                    imNode.setProperty("imValue", imValue[i]);
                    imNode.setProperty("imTypeValue", imTypeValue[i]);
                }
            }

            session.save();
           
            JSONObject profileJson = new JSONObject();
            Map<String, String> property = new HashMap<String, String>();
            try{
                profileJson.accumulate("username", node1.getProperty("primaryEmail")
                        .getString());
                profileJson.accumulate("firstname", node1.getProperty("name").getString());
                profileJson.accumulate("lastname", node1.hasProperty("lastName") ? node1
                        .getProperty("lastName").getString() : "");
                profileJson.accumulate("dob", node1.hasProperty("birthDay") ? node1
                        .getProperty("birthDay").getString() : "");
                profileJson.accumulate("emails", emailArray.toString());
                profileJson.accumulate("phones", phoneArray.toString());
                JSONArray addressArray = new JSONArray();
                JSONObject addressObject = new JSONObject();
                addressObject.accumulate("title",
                        node1.hasProperty("address") ? node1.getProperty("address")
                                .getString() : "");
                addressObject.accumulate("city",
                        node1.hasProperty("city") ? node1.getProperty("city")
                                .getString() : "");
                addressObject.accumulate("state", node1.hasProperty("state") ? node1
                        .getProperty("state").getString() : "");
                addressObject.accumulate("country", node1.hasProperty("country") ? node1
                        .getProperty("country").getString() : "");
                addressObject.accumulate("pin", node1.hasProperty("pincode") ? node1
                        .getProperty("pincode").getString() : "");
                addressArray.put(addressObject);
                profileJson.accumulate("address", addressArray.toString());
                profileJson.accumulate("language", "[{\"name\": \"English\", \"default\": \"true\"}]");
                if (node1.hasProperty("state") && node1.hasProperty("country")
                        && node1.hasProperty("city")
                        && !node1.getProperty("city").getString().equals("")
                        && !node1.getProperty("state").getString().equals("")
                        && !node1.getProperty("country").getString().equals("")
                        && !phoneVerificationList.contains("no")
                        && !emailVerificationList.contains("no")) {
                    profileJson.accumulate("completeStatus", "yes");
                }else{
                    profileJson.accumulate("completeStatus", "no");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            property.put("userId", node1.getProperty("primaryEmail")
                    .getString());
            
            property.put("firstName", node1.getProperty("name").getString());
            property.put("lastName", node1.hasProperty("lastName") ? node1
                    .getProperty("lastName").getString() : "");
            property.put("city",
                    node1.hasProperty("city") ? node1.getProperty("city")
                            .getString() : "");
            property.put("state", node1.hasProperty("state") ? node1
                    .getProperty("state").getString() : "");
            property.put("country", node1.hasProperty("country") ? node1
                    .getProperty("country").getString() : "");
            property.put("pincode", node1.hasProperty("pincode") ? node1
                    .getProperty("pincode").getString() : "");
            property.put("emailId", producerEmail.toString());
            property.put("emailVerification",
                    producerEmailVerification.toString());
            property.put("phoneNumber", phoneNumber.toString());
            property.put("phoneNumberVerfication",
                    phoneNumberVerification.toString());
            if (node1.hasProperty("state") && node1.hasProperty("country")
                    && node1.hasProperty("city")
                    && !node1.getProperty("city").getString().equals("")
                    && !node1.getProperty("state").getString().equals("")
                    && !node1.getProperty("country").getString().equals("")
                    && !phoneVerificationList.contains("no")
                    && !emailVerificationList.contains("no")) {
                property.put("completeStatus", "yes");
            }else{
                property.put("completeStatus", "no");
            }
            property.put("eventId", "44");
        //    activeMQ.producerCall("ProfileEdit", property, profileJson.toString());
            if (!session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {
                map = new HashMap<String, String>();
                map.put("key", "message");
                map.put("value", "has update his");
                activityStream.add(map);
                map = new HashMap<String, String>();
                map.put("key", "profileType");
                map.put("value", "Profile");
                activityStream.add(map);
                activityStream(userId, activityStream, "Profile", "");
            }
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }

        return "done";
    }
    
    public String saveProfileAddNode(SlingHttpServletRequest request)
            throws ServletException, IOException {
        String userId = request.getParameter("title");
        Node node1, contentNode,addrNode, userNode = null,summary = null;
        Session session;
        ArrayList<Map> activityStream = new ArrayList<Map>();
        Map<String, String> map;
        
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (!session.getRootNode().hasNode("content")) {

                contentNode = session.getRootNode().addNode("content");
            }
            if (!session.getRootNode().getNode("content").hasNode("user")) {

                userNode = session.getRootNode().getNode("content")
                        .addNode("user");
            }
            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);

            } else {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").addNode(userId);

            }
            if(node1.hasNode("Address")){
            	addrNode = node1.getNode("Address");
            }else{
            	addrNode = node1.addNode("Address");
            }
            addrNode.setProperty("address", request.getParameter("address"));
            node1.setProperty("industry", request.getParameter("industryId"));
            addrNode.setProperty("city", request.getParameter("city"));
            addrNode.setProperty("country", request.getParameter("country"));
            addrNode.setProperty("state", request.getParameter("state"));
            addrNode.setProperty("pinCode", request.getParameter("pinCode"));
            node1.setProperty("address", request.getParameter("address"));
            node1.setProperty("city", request.getParameter("city"));
            node1.setProperty("country", request.getParameter("country"));
            node1.setProperty("state", request.getParameter("state"));
            node1.setProperty("pinCode", request.getParameter("pinCode"));
            session.save();
                  } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }

        return "done";
    }

    @SuppressWarnings("rawtypes")
    public String saveSummaryNode(SlingHttpServletRequest request)
            throws ServletException, IOException {
        String userId = request.getParameter("redirect");
        Node node1 = null;
        Session session;
        ArrayList<Map> activityStream = new ArrayList<Map>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "message");
        map.put("value", "has update his");
        activityStream.add(map);
        map = new HashMap<String, String>();
        map.put("key", "profileType");
        map.put("value", "Profile");
        activityStream.add(map);
        activityStream(userId, activityStream, "Profile", "");

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            Node summary = null;

            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);

            }

            if (node1.hasNode("ProfessionalSummary")) {
                summary = node1.getNode("ProfessionalSummary");
            } else {
                summary = node1.addNode("ProfessionalSummary");
            }

            summary.setProperty("goals", request.getParameter("goals"));
            summary.setProperty("specialities",
                    request.getParameter("specialities"));
            summary.setProperty("skill", request.getParameterValues("skill"));
            summary.setProperty("level", request.getParameterValues("level"));
            summary.setProperty("skillexp", request.getParameterValues("skillexp"));
            summary.setProperty("title", request.getParameter("title"));

            if (request.getParameterValues("skill")[0] != "") {
                String[] skillValue = request.getParameterValues("skill");
                String[] levelValue = request.getParameterValues("level");
                String[] skillexpValue = request.getParameterValues("skillexp");
                Node skill = null;
                Node skillNode = null;
                if (summary.hasNode("Skill")) {
                    skill = summary.getNode("Skill");
                } else {
                    skill = summary.addNode("Skill");
                }
                for (int i = 0; i < skillValue.length; i++) {
                    if (skill.hasNode(skillValue[i])) {
                        skillNode = skill.getNode(skillValue[i]);

                    } else {
                        skillNode = skill.addNode(skillValue[i]);
                    }

                    skillNode.setProperty("skillValue", skillValue[i]);
                    skillNode.setProperty("levelValue", levelValue[i]);
                    skillNode.setProperty("skillexpValue", skillexpValue[i]);
                }
            }
            session.save();

        } catch (PathNotFoundException e) {

        } catch (RepositoryException e) {

            e.printStackTrace();
        }

        return request.getParameter("redirect");

    }

    @SuppressWarnings("rawtypes")
    public String saveExperienceNode(SlingHttpServletRequest request)
            throws ServletException, IOException {
        String userId = request.getParameter("redirect");

        String dummyField = request.getParameter("title");
     //   String company = dummyField.replaceAll("\\s+", "");
        Node node1 = null;
        Session session;
        ArrayList<Map> activityStream = new ArrayList<Map>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "message");
        map.put("value", "has update his");
        activityStream.add(map);
        map = new HashMap<String, String>();
        map.put("key", "profileType");
        map.put("value", "Experience");
        activityStream.add(map);
        activityStream(userId, activityStream, "Profile", "");
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            Node experience = null;

            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);

            }

            if (node1.hasNode("Experience")) {
                experience = node1.getNode("Experience");
            } else {
                experience = node1.addNode("Experience");
            }
            String company[] = request.getParameterValues("companyName");
            NodeIterator nodeIterator1 = experience.getNodes();
            List<String> numberList1 = Arrays.asList(company);
            while (nodeIterator1.hasNext()) {
                Node removeNode1 = nodeIterator1.nextNode();
                if (!numberList1.contains(removeNode1.getName())) {
                    removeNode1.remove();
                }
            }
           Node exNode = null;
            for (int i = 0; i < company.length; i++) {
                if (experience.hasNode(company[i].replaceAll("\\s+", ""))) {
                	exNode = experience.getNode(company[i].replaceAll("\\s+", ""));

                } else {
                	exNode = experience.addNode(company[i].replaceAll("\\s+", ""));
                }

                exNode.setProperty("companyDesc",
                        request.getParameterValues("companyDesc")[i]);
                exNode.setProperty("companyLocation",
                        request.getParameterValues("companyLocation")[i]);
                exNode.setProperty("companyName",
                        request.getParameterValues("companyName")[i]);
                exNode.setProperty("companyWebsite",
                        request.getParameterValues("companyWebsite")[i]);
                exNode.setProperty("childNode", request.getParameterValues("jobTitle")[i].replaceAll("\\s+", ""));
                exNode.setProperty("title", company[i].replaceAll("\\s+", ""));
                Node jobTitleNode = null;
            //    String job[] = 
                NodeIterator nodeIterator2 = exNode.getNodes();
                List<String> numberList2 = Arrays.asList(request.getParameterValues("jobTitle"));
                while (nodeIterator2.hasNext()) {
                    Node removeNode2 = nodeIterator2.nextNode();
                    if (!numberList2.contains(removeNode2.getName())) {
                        removeNode2.remove();
                    }
                }
                if (exNode.hasNode(request.getParameterValues("jobTitle")[i])) {
                    jobTitleNode = exNode.getNode(request.getParameterValues("jobTitle")[i].replaceAll("\\s+", ""));
                } else {
                    jobTitleNode = exNode.addNode(request.getParameterValues("jobTitle")[i].replaceAll("\\s+", ""));
                }
//                if(request.getParameterValues("currentlyWork")[i].equals("Present")){
                int workCount = 0;
                if(request.getParameterValues("endDate1")[i] == null || request.getParameterValues("endDate1")[i] == ""){
                jobTitleNode.setProperty("currentlyWork",request.getParameterValues("currentlyWork")[workCount]);
                workCount++;
                //jobTitleNode.setProperty("endDate", "");
                }else {
                	jobTitleNode.setProperty("endDate", request.getParameterValues("endDate1")[i]);
                	//jobTitleNode.setProperty("currentlyWork","");
                }
//                }
                jobTitleNode
                        .setProperty("jobDesc", request.getParameterValues("jobDesc")[i]);
                jobTitleNode.setProperty("startDate",
                        request.getParameterValues("startDate")[i]);
//                if(request.getParameterValues("currentlyWork")[i].equals("past")){
//                jobTitleNode
//                        .setProperty("endDate", request.getParameterValues("endDate1")[i]);
//                }
                jobTitleNode.setProperty("jobTitle",
                        request.getParameterValues("jobTitle")[i]);
                jobTitleNode.setProperty("title", request.getParameterValues("jobTitle")[i].replaceAll("\\s+", ""));

            }


//            if (request.getParameterValues("im")[0] != "") {
//                String[] imValue = request.getParameterValues("im");
//                String[] imTypeValue = request.getParameterValues("imType");
//                Node im = null;
//                Node imNode = null;
//                if (contact.hasNode("IM")) {
//                    im = contact.getNode("IM");
//                } else {
//                    im = contact.addNode("IM");
//                }
//                NodeIterator imNodes = im.getNodes();
//                List<String> imList = Arrays.asList(imValue);
//                while (imNodes.hasNext()) {
//                    Node removeNode = imNodes.nextNode();
//                    if (!imList.contains(removeNode.getName())) {
//                        removeNode.remove();
//                    }
//                }
//                for (int i = 0; i < imValue.length; i++) {
//                    if (im.hasNode(imValue[i])) {
//                        imNode = im.getNode(imValue[i]);
//
//                    } else {
//                        imNode = im.addNode(imValue[i]);
//                    }
//
//                    imNode.setProperty("imValue", imValue[i]);
//                    imNode.setProperty("imTypeValue", imTypeValue[i]);
//                }
//            }

            session.save();

        } catch (PathNotFoundException e) {

        } catch (RepositoryException e) {

            e.printStackTrace();
        }

        return request.getParameter("redirect");

    }

    @SuppressWarnings("rawtypes")
    public String saveEducationNode(SlingHttpServletRequest request)
            throws ServletException, IOException {

        String userId = request.getParameter("redirect");
        String dummyField = request.getParameter("title");
        String degree = dummyField.replaceAll("\\s+", "");
        Node node1 = null;
        Session session;
        ArrayList<Map> activityStream = new ArrayList<Map>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "message");
        map.put("value", "has update his");
        activityStream.add(map);
        map = new HashMap<String, String>();
        map.put("key", "profileType");
        map.put("value", "Education");
        activityStream.add(map);
        activityStream(userId, activityStream, "Profile", "");
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            Node education = null;

            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);

            }

            if (node1.hasNode("Education")) {
                education = node1.getNode("Education");
            } else {
                education = node1.addNode("Education");
            }

//            Node eduNode = null;
//            if (education.hasNode(degree)) {
//                eduNode = education.getNode(degree);
//            } else {
//                eduNode = education.addNode(degree);
//            }
//            
            String school[] = request.getParameterValues("school");
            String degree1[] = request.getParameterValues("idDegree");
            NodeIterator nodeIterator1 = education.getNodes();
            List<String> numberList1 = Arrays.asList(school+"_"+degree1);
            while (nodeIterator1.hasNext()) {
                Node removeNode1 = nodeIterator1.nextNode();
                if (!numberList1.contains(removeNode1.getName())) {
                    removeNode1.remove();
                }
            }
           Node exNode = null;
            for (int i = 0; i < school.length; i++) {
                if (education.hasNode(school[i].replaceAll("\\s+", "")+"_"+degree1[i])) {
                	exNode = education.getNode(school[i].replaceAll("\\s+", "")+"_"+degree1[i]);

                } else {
                	exNode = education.addNode(school[i].replaceAll("\\s+", "")+"_"+degree1[i]);
                }

                exNode.setProperty("activity",
                        request.getParameterValues("activityId")[i]);
                exNode.setProperty("additionalNotes",
                        request.getParameterValues("additionalNotes")[i]);
                exNode.setProperty("school",
                        request.getParameterValues("schoolId")[i]);
                exNode.setProperty("degree",
                        request.getParameterValues("idDegree")[i]);
                exNode.setProperty("endDate",
                        request.getParameterValues("endDate")[i]);
                exNode.setProperty("toDate",
                        request.getParameterValues("toDate")[i]);
                exNode.setProperty("toMonth",
                        request.getParameterValues("toMonth")[i]);
                exNode.setProperty("endMonth",
                        request.getParameterValues("endMonth")[i]);
                exNode.setProperty("title", school[i].replaceAll("\\s+", ""));

            }
//            eduNode.setProperty("activity", request.getParameter("activityId"));
//            eduNode.setProperty("additionalNotes",
//                    request.getParameter("additionalNotes"));
//            eduNode.setProperty("degree", request.getParameter("idDegree"));
//            eduNode.setProperty("endDate", request.getParameter("endDate"));
//            eduNode.setProperty("toDate", request.getParameter("toDate"));
////            eduNode.setProperty("grade", request.getParameter("grade"));
//            eduNode.setProperty("school", request.getParameter("schoolId"));
////            eduNode.setProperty("board", request.getParameter("boardId"));
////            eduNode.setProperty("location", request.getParameter("location"));
//            eduNode.setProperty("title", request.getParameter("title"));
//            eduNode.setProperty("toMonth", request.getParameter("toMonth"));
//            eduNode.setProperty("endMonth", request.getParameter("endMonth"));

            session.save();

        } catch (PathNotFoundException e) {

        } catch (RepositoryException e) {

            e.printStackTrace();
        }

        return request.getParameter("redirect");

    }

    public String saveInfoNode(SlingHttpServletRequest request)
            throws ServletException, IOException {

        String userId = request.getParameter("redirect");
        Node node1 = null;
        Session session;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            Node info = null;

            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {

                node1 = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);

            }

            if (node1.hasNode("AdditionalInformation")) {
                info = node1.getNode("AdditionalInformation");
            } else {
                info = node1.addNode("AdditionalInformation");
            }

            info.setProperty("websites", request.getParameterValues("websites"));
            info.setProperty("webType", request.getParameterValues("webType"));
            info.setProperty("groupsAssociations",
                    request.getParameter("groupsAssociations"));
            info.setProperty("interests",
                    request.getParameterValues("interests"));
            info.setProperty("honorsAwards",
                    request.getParameterValues("honorsAwards"));
            info.setProperty("title", request.getParameter("title"));
            if (request.getParameterValues("websites")[0] != "") {
                String[] webValue = request.getParameterValues("websites");
                String[] webtypeValue = request.getParameterValues("webType");
                Node url = null;
                Node urllNode = null;
                if (info.hasNode("URL")) {
                    url = info.getNode("URL");
                } else {
                    url = info.addNode("URL");
                }
                for (int i = 0; i < webValue.length; i++) {
                    if (url.hasNode(webValue[i])) {
                        urllNode = url.getNode(webValue[i]);

                    } else {
                        urllNode = url.addNode(webValue[i]);
                    }

                    urllNode.setProperty("websitesValue", webValue[i]);
                    urllNode.setProperty("webTypeValue", webtypeValue[i]);
                }
            }
            session.save();

        } catch (PathNotFoundException e) {

        } catch (RepositoryException e) {

            e.printStackTrace();
        }

        return request.getParameter("redirect");

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ArrayList searchProfile(String firstName, String flag) {
        ArrayList searchPeople = new ArrayList();
        try {
            Session session = repos.login(new SimpleCredentials("admin",
                    "admin".toCharArray()));

            Workspace workspace = session.getWorkspace();
            Query query;
            if (flag.equals("follow")) {
                query = workspace.getQueryManager().createQuery(

                        "select * from [nt:unstructured] where  (contains('name', '*"
                                + firstName + "*') OR  contains('address', '*"
                                + firstName + "*') OR  "
                                + "contains('industry', '*" + firstName
                                + "*') OR  contains('email', '*" + firstName
                                + "*') OR  contains('city', '*" + firstName
                                + "*'))  and"
                                + " socialMessagingId IS NOT NULL "
                                + "and ISDESCENDANTNODE('/content/user')",
                        Query.JCR_SQL2);
            } else {
                query = workspace.getQueryManager().createQuery(

                        "select * from [nt:unstructured] where  (contains('name', '*"
                                + firstName + "*') OR  contains('address', '*"
                                + firstName + "*') OR  "
                                + "contains('industry', '*" + firstName
                                + "*') OR  contains('email', '*" + firstName
                                + "*') OR  contains('city', '*" + firstName
                                + "*')) "
                                + "and ISDESCENDANTNODE('/content/user')",
                        Query.JCR_SQL2);
            }
            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();

            while (iterator.hasNext()) {

                searchPeople.add(iterator.nextNode());
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        return searchPeople;
    }

    @SuppressWarnings({ "deprecation", "unused" })
    public String uploadProfilePic(SlingHttpServletRequest request)
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        Node root, userNode = null, fileNewNode, pictureNode, fileNode, jcrNode = null;

        try {
            Session session = repos.login(new SimpleCredentials("admin",
                    "admin".toCharArray()));

            if (session.getRootNode().getNode("content").getNode("user")
                    .hasNode(userId)) {
                userNode = session.getRootNode().getNode("content")
                        .getNode("user").getNode(userId);
            }
            if (userNode.hasNode("picture")) {

                pictureNode = userNode.getNode("picture");
            } else {
                pictureNode = userNode.addNode("picture");
            }
            if (pictureNode.hasNode("profilePic")) {

                fileNode = pictureNode.getNode("profilePic");
                fileNode.remove();
            }
            fileNewNode = pictureNode.addNode("profilePic", "nt:file");

            jcrNode = fileNewNode.addNode("jcr:content", "nt:resource");

            for (Entry<String, RequestParameter[]> e : request
                    .getRequestParameterMap().entrySet()) {
                for (RequestParameter p : e.getValue()) {
                    if (!p.isFormField()) {
                        jcrNode.setProperty("jcr:data", p.getInputStream());
                    }
                }
            }

            jcrNode.setProperty("jcr:lastModified", Calendar.getInstance());
            jcrNode.setProperty("jcr:mimeType", "image/jpg");
            userNode.setProperty("profileImage", request.getContextPath()
                    + "/content/user/" + request.getParameter("userId")
                    + "/picture/profilePic");
            session.save();

        } catch (PathNotFoundException e) {
            return "1";

        } catch (RepositoryException e) {
            return "2";

        } catch (FileNotFoundException e) {
            return "";

        }

        return "3";

    }

    public String profileVisit(String username, String visitor) {

        Node node, userNode, visitorNode, dateNode, timeNode = null;
        Session session;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("HH-mm-ss");
        DateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getRootNode().getNode("content").getNode("user")
                    .getNode(username);

            if (node.hasNode("Visitor")) {
                visitorNode = node.getNode("Visitor");
            } else {
                visitorNode = node.addNode("Visitor");
            }
            ArrayList<String> visitorList = new ArrayList<String>();
            if (visitorNode.hasProperty("visitorName")) {
                visitorList.add(visitor);
                Value[] visitorVal = visitorNode.getProperty("visitorName")
                        .getValues();
                for (int i = 0; i < visitorVal.length && i < 9; i++) {
                    if (!visitorVal[i].getString().equals(visitor)) {
                        visitorList.add(visitorVal[i].getString());
                    }
                }
            } else {
                visitorList.add(visitor);
            }

            String[] visitorArr = new String[visitorList.size()];
            visitorArr = visitorList.toArray(visitorArr);
            visitorNode.setProperty("visitorName", visitorArr);
            if (visitorNode.hasNode(visitor)) {
                userNode = visitorNode.getNode(visitor);
            } else {
                userNode = visitorNode.addNode(visitor);
            }

            if (userNode.hasNode(dateFormat.format(date))) {
                dateNode = userNode.getNode(dateFormat.format(date));
            } else {
                dateNode = userNode.addNode(dateFormat.format(date));
            }
            if (dateNode.hasNode(dateFormat2.format(date))) {
                timeNode = dateNode.getNode(dateFormat2.format(date));
            } else {
                timeNode = dateNode.addNode(dateFormat2.format(date));
            }
            timeNode.setProperty("date", dateFormat.format(date));
            timeNode.setProperty("time", dateFormat3.format(date));
            timeNode.setProperty("visitor", visitor);
            session.save();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void profileEducationDelete(SlingHttpServletRequest request)
            throws ServletException, IOException {
        String education = request.getParameter("operation");
        Node node1 = null;
        Session session;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            if (session.getRootNode().hasNode(education.replaceFirst("/", ""))) {

                node1 = session.getRootNode().getNode(
                        education.replaceFirst("/", ""));

            }
            node1.remove();
            session.save();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }

    public void profileExperienceDelete(SlingHttpServletRequest request)
            throws ServletException, IOException {
        String experience = request.getParameter("operation");

        Node node1 = null;
        Session session;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            if (session.getRootNode().hasNode(experience.replaceFirst("/", ""))) {

                node1 = session.getRootNode().getNode(
                        experience.replaceFirst("/", ""));

            }
            node1.remove();
            session.save();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }

    public void profileEmailNMobileDelete(String userName, String deleteNode,
            String childNode) {
        Node node, contactNode = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            if (session.getRootNode().hasNode("content/user/" + userName)) {
                node = session.getRootNode()
                        .getNode("content/user/" + userName);
                if (node.hasNode("ContactDetails")) {
                    contactNode = node.getNode("ContactDetails")
                            .getNode(deleteNode).getNode(childNode);
                }
            }
            contactNode.remove();
            session.save();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    public ArrayList friendSuggestion(String userId) {

        /* Search servlet */
        ArrayList friendPeople = new ArrayList();
        ArrayList eduPeople = new ArrayList();
        ArrayList exPeople = new ArrayList();
        Node userNode, eduNode, experienceNode, connectionNode, userFriendNode = null;
        NodeIterator education, experience, connection, userFriendNodes = null;
        NodeIterator educationIterate, experienceIterate = null;
        Query query;
        QueryResult qr;
        NodeIterator iterator;

        try {
            Session session = repos.login(new SimpleCredentials("admin",
                    "admin".toCharArray()));
            Workspace workspace = session.getWorkspace();
            userNode = session.getRootNode().getNode("content/user")
                    .getNode(userId);

            if (userNode.hasNode("Education")
                    && userNode.getNode("Education").hasNodes()) {
                education = userNode.getNode("Education").getNodes();

                while (education.hasNext()) {
                    eduNode = education.nextNode();
                    query = workspace.getQueryManager().createQuery(
                            "select * from [nt:unstructured] where  (contains('school', '*"
                                    + eduNode.getProperty("school").getString()
                                    + "*')" + " and contains('degree', '*"
                                    + eduNode.getProperty("degree").getString()
                                    + "*')) "
                                    + "and ISDESCENDANTNODE('/content/user')",
                            Query.JCR_SQL2);

                    qr = query.execute();
                    educationIterate = qr.getNodes();
                    while (educationIterate.hasNext()) {
                        Node userEduNode = educationIterate.nextNode()
                                .getParent().getParent();
                        if (!userEduNode.getName().equals(userId)) {
                            eduPeople.add(userEduNode.getName());
                        }

                    }
                }
            }

            if (userNode.hasNode("Experience")
                    && userNode.getNode("Experience").hasNodes()) {
                experience = userNode.getNode("Experience").getNodes();

                while (experience.hasNext()) {
                    experienceNode = experience.nextNode();

                    query = workspace.getQueryManager().createQuery(
                            "select * from [nt:unstructured] where  (contains('companyName', '*"
                                    + experienceNode.getProperty("companyName")
                                            .getString()
                                    + "*')"
                                    + " or contains('childNode', '*"
                                    + experienceNode.getProperty("childNode")
                                            .getString() + "*')) "
                                    + "and ISDESCENDANTNODE('/content/user')",
                            Query.JCR_SQL2);
                    qr = query.execute();
                    experienceIterate = qr.getNodes();

                    while (experienceIterate.hasNext()) {

                        Node userExpNode = experienceIterate.nextNode()
                                .getParent().getParent();

                        if (!userExpNode.getName().equals(userId)) {

                            exPeople.add(userExpNode.getName());
                        }

                    }

                }
            }

            if (userNode.hasNode("connection")
                    && userNode.getNode("connection").hasNode("friend")
                    && userNode.getNode("connection").getNode("friend")
                            .hasNodes()) {
                connection = userNode.getNode("connection").getNode("friend")
                        .getNodes();

                while (connection.hasNext()) {
                    connectionNode = connection.nextNode();

                    userFriendNode = session.getRootNode().getNode("content")
                            .getNode("user").getNode(connectionNode.getName());
                    userFriendNodes = userFriendNode.getNode("connection")
                            .getNode("friend").getNodes();
                    friendPeople.add(connectionNode.getName());

                    while (userFriendNodes.hasNext()) {
                        Node friendNode = userFriendNodes.nextNode();
                        if (!friendNode.getName().equals(userId)) {

                            friendPeople.add(friendNode.getName());

                        }

                    }
                }
            }
            exPeople.addAll(eduPeople);
            if (exPeople.size() <= 1) {
                friendPeople.addAll(exPeople);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        return exPeople;
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    private ArrayList removeDuplicates(ArrayList list) {
        HashSet set = new HashSet(list);
        list.clear();
        list.addAll(set);

        return list;
    }

    @SuppressWarnings({ "unused", "rawtypes" })
    public void activityStream(String userName, ArrayList<Map> properties,
            String activityTypeNode, String activityNode) {
        Session session;
        Node userNode, activity, activityType, dateNode, lastNode = null;
        NodeIterator activityNumber = null;
        String count = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userName);

            if (userNode.hasNode("Activity")) {
                activity = userNode.getNode("Activity");
            } else {
                activity = userNode.addNode("Activity");
            }

            if (activity.hasNode(dateFormat.format(date))) {
                dateNode = activity.getNode(dateFormat.format(date));
            } else {
                dateNode = activity.addNode(dateFormat.format(date));
            }

            if (dateNode.hasNode(activityTypeNode)) {
                activityType = dateNode.getNode(activityTypeNode);
            } else {
                activityType = dateNode.addNode(activityTypeNode);
            }
            if (activityType.hasNodes()) {
                activityNumber = activityType.getNodes();
                while (activityNumber.hasNext()) {
                    count = activityNumber.nextNode().getName();
                }
                lastNode = activityType.addNode((Integer.parseInt(count) + 1)
                        + "");
            } else {
                lastNode = activityType.addNode("1");
            }
            for (int i = 0; i < properties.size(); i++) {

                Map<String, String> map = new HashMap<String, String>();
                lastNode.setProperty(properties.get(i).get("key") + "",
                        properties.get(i).get("value") + "");
            }
            session.save();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void mapCompany(String userId, String mapType, String[] values,
            String confirm, String confirmCompany, String confirmType,
            boolean verify) {

        Session session;
        Node userNode, node, mapNode, company;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userId);
            if (userNode.hasNode("Company")) {
                node = userNode.getNode("Company");
            } else {
                node = userNode.addNode("Company");
            }

            if (node.hasNode(mapType)) {
                mapNode = node.getNode(mapType);
            } else {
                mapNode = node.addNode(mapType);
            }
            if (confirm.equals("true")) {
                if (confirmType.equals("delete")) {
                    company = mapNode.getNode(confirmCompany);
                    company.setProperty("verified", "false");
                } else if (confirmType.equals("cancel")) {
                    company = mapNode.getNode(confirmCompany);
                    company.setProperty("verified", "false");
                    mapCompanyRequest(confirmCompany, mapType, userId, true);
                } else {
                    company = mapNode.getNode(confirmCompany);
                    company.setProperty("verified", "true");
                    mapCompanyRequest(confirmCompany, mapType, userId, true);
                }

            } else if (confirm.equals("add")) {
                if (mapNode.hasNode(confirmCompany)) {
                    company = mapNode.getNode(confirmCompany);
                } else {
                    company = mapNode.addNode(confirmCompany);
                    company.setProperty("verified", "true");

                }

            } else {
                for (String companyName : values) {
                    if (mapNode.hasNode(companyName)) {
                        company = mapNode.getNode(companyName);
                    } else {
                        company = mapNode.addNode(companyName);
                        if (verify) {
                            mapCompanyRequest(companyName, mapType, userId,
                                    false);
                            company.setProperty("verified", "pending");
                        }
                    }
                }
            }
            session.save();
        } catch (Exception e) {
        }
    }

    public void mapCompanyRequest(String companyName, String mapType,
            String userId, boolean delete) {
        Node companyNode, relationNode, requestNode, mapNode, personNode;
        Session session = null;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            companyNode = session.getNode("/content/company/" + companyName);
            if (companyNode.hasNode("Relationship")) {
                relationNode = companyNode.getNode("Relationship");
            } else {
                relationNode = companyNode.addNode("Relationship");
            }
            if (relationNode.hasNode("UserRequest")) {
                requestNode = relationNode.getNode("UserRequest");
            } else {
                requestNode = relationNode.addNode("UserRequest");
            }
            if (delete) {
                requestNode.setProperty("request",
                        requestNode.getProperty("request").getLong() - 1);
            }
            if (requestNode.hasNode(mapType)) {
                mapNode = requestNode.getNode(mapType);

            } else {
                mapNode = requestNode.addNode(mapType);
            }

            if (mapNode.hasNode(userId)) {
                personNode = mapNode.getNode(userId);
                if (delete) {

                    personNode.remove();
                }
            } else {
                personNode = mapNode.addNode(userId);
                if (!delete) {
                    if (requestNode.hasProperty("request")) {
                        requestNode.setProperty("request", requestNode
                                .getProperty("request").getLong() + 1);
                    } else {
                        requestNode.setProperty("request", "1");
                    }
                }
            }
            session.save();

        } catch (Exception e) {
        }
        /*
         * finally{ session.logout(); }
         */
    }

    public void deleteCompanyMap(String userId, String nodeName) {

    }

    public String serviceCheck(String userId, String key, String value) {
        Session session = null;
        Node userNode = null;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getNode("/content/user/" + userId);
            //String service = "NNNNNNNNNNNNNNNNN";    // earlier
            String service = "NNNNNNNNNNNNNNNNNN";    // added N at last for doctiger by mohit
            StringBuffer serviceBuffer = new StringBuffer(service);
            if (userNode.hasProperty("serviceCheck")) {
                service = userNode.getProperty("serviceCheck").getString();

                serviceBuffer = new StringBuffer(service);
            }
            if (key.equals("sms")) {
                serviceBuffer.setCharAt(0, value.charAt(0));
            } else if (key.equals("email")) {
                serviceBuffer.setCharAt(1, value.charAt(0));
            } else if (key.equals("Chat")) {
                serviceBuffer.setCharAt(2, value.charAt(0));
            } else if (key.equals("VChat")) {
                serviceBuffer.setCharAt(3, value.charAt(0));
            } else if (key.equals("call")) {
                serviceBuffer.setCharAt(4, value.charAt(0));
            } else if (key.equals("camp") || key.equals("mm")) {
                serviceBuffer.setCharAt(5, value.charAt(0));
            } else if (key.equals("lime")) {
                serviceBuffer.setCharAt(6, value.charAt(0));
            } else if (key.equals("ox")) {
                serviceBuffer.setCharAt(7, value.charAt(0));
            } else if (key.equals("wc")) {
                serviceBuffer.setCharAt(8, value.charAt(0));
            } else if (key.equals("mm")) {
                serviceBuffer.setCharAt(9, value.charAt(0));
            } 
//            else if (key.equals("log")) {
//                serviceBuffer.setCharAt(10, value.charAt(0));
//            }
            else if (key.equals("live_chat")) {
                serviceBuffer.setCharAt(10, value.charAt(0));
            }
            else if (key.equals("deg3")) {
                serviceBuffer.setCharAt(11, value.charAt(0));
            } else if (key.equals("exam")) {
                serviceBuffer.setCharAt(12, value.charAt(0));
            } else if (key.equals("cs")) {
                serviceBuffer.setCharAt(13, value.charAt(0));
            } else if (key.equals("mp")) {
                serviceBuffer.setCharAt(14, value.charAt(0));
            } else if (key.equals("callMB")) {
                serviceBuffer.setCharAt(15, value.charAt(0));
            } else if (key.equals("helpdesk")) {
                serviceBuffer.setCharAt(16, value.charAt(0));
            } else if (key.equals("Doctiger")) {
                serviceBuffer.setCharAt(17, value.charAt(0));
            }
            
            // 184.72.240.222
            userNode.setProperty("serviceCheck", serviceBuffer.toString());

            session.save();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        } finally {
            session.logout();
        }

    }

    public char getServiceCheck(String userId, String key) {
        Session session = null;
        Node userNode = null;
        char value = 0;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getNode("/content/user/" + userId);
            String service = "NNNNNNNNNNNNNNNNN";
            if (userNode.hasProperty("serviceCheck")) {
                service = userNode.getProperty("serviceCheck").getString();

            }
            if (key.equals("sms")) {
                value = service.charAt(0);
            } else if (key.equals("email")) {
                value = service.charAt(1);
            } else if (key.equals("Chat")) {
                value = service.charAt(2);
            } else if (key.equals("VChat")) {
                value = service.charAt(3);
            } else if (key.equals("call")) {
                value = service.charAt(4);
            } else if (key.equals("camp") || key.equals("mm")) {
                value = service.charAt(5);
            } else if (key.equals("lime")) {
                value = service.charAt(6);
            } else if (key.equals("ox")) {
                value = service.charAt(7);
            } else if (key.equals("wc")) {
                value = service.charAt(8);
            } else if (key.equals("mm")) {
                value = service.charAt(9);
            } //else if (key.equals("log")) {
            else if (key.equals("live_chat")) {        /// previous it was log change on 17-02-2017
                value = service.charAt(10);
            } else if (key.equals("deg3")) {
                value = service.charAt(11);
            } else if (key.equals("exam")) {
                value = service.charAt(12);
            } else if (key.equals("cs")) {
                value = service.charAt(13);
            } else if (key.equals("mp")) {
                value = service.charAt(14);
            } else if (key.equals("callMB")) {
                value = service.charAt(15);
            } else if (key.equals("helpdesk")) {
                value = service.charAt(16);
            }

        } catch (Exception e) {
            value = 'N';
        }

        finally {
            session.logout();
        }

        return value;
    }

    public String setMessageCredential(String userId, String msgUser,
            String msgPassword) {
        Session session = null;
        Node userNode = null;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getNode("/content/user/" + userId);
            userNode.setProperty("smsUserID", msgUser);
            userNode.setProperty("smsPassword", msgPassword);

            session.save();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        } finally {
            session.logout();
        }
    }

    public String getServiceParam(String param, String userId) {

        StringBuilder result = new StringBuilder("");
        try {

            String[] paramKey = param.split(",");

            for (String key : paramKey) {

                result.append(getServiceCheck(userId, key));

            }

        } catch (Exception e) {

            return "failure";
        }

        return result.toString();
    }

    @SuppressWarnings("rawtypes")
    public void setUserStatus(String userId, String userStatus) {
        Session session = null;
        Node userNode = null;
        ArrayList<Map> activityStream = new ArrayList<Map>();
        Map<String, String> map;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getNode("/content/user/" + userId);
            userNode.setProperty("userStatus", userStatus);

            session.save();
            map = new HashMap<String, String>();
            map.put("key", "message");
            map.put("value", "has update his");
            activityStream.add(map);
            map = new HashMap<String, String>();
            map.put("key", "profileType");
            map.put("value", "Status : " + userStatus);
            activityStream.add(map);
            activityStream(userId, activityStream, "Status", "");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            session.logout();
        }

    }

    public ArrayList<Node> advanceSearchProfile(String skillTag,
            String skillValue, String companyTag, String companyValue,
            String companyDur, String addressTag, String addressValue,
            String educationTag, String educationValue, String educationDur,
            String keyword) {
        ArrayList<Node> searchPeople = new ArrayList<Node>();
        /* final long startTime = System.currentTimeMillis(); */
        try {
            Session session = repos.login(new SimpleCredentials("admin",
                    "admin".toCharArray()));
            String query2 = "select * from [nt:unstructured] As parent  ";
            if (companyTag.equals("1")) {
                query2 += "INNER JOIN [nt:unstructured] AS expChild ON ISCHILDNODE(expChild,parent) "
                        + "INNER JOIN [nt:unstructured] as exp ON ISCHILDNODE(exp,expChild) ";
                if (!companyDur.equals("")) {
                    query2 += "INNER JOIN [nt:unstructured] AS expDurChild ON ISCHILDNODE(expDurChild,exp) ";
                }
            }
            if (educationTag.equals("1")) {
                query2 += "INNER JOIN [nt:unstructured] AS eduChild ON ISCHILDNODE(eduChild,parent) "
                        + "INNER JOIN [nt:unstructured] as edu ON ISCHILDNODE(edu,eduChild) ";
            }

            if (skillTag.equals("1")) {
                query2 += "INNER JOIN [nt:unstructured] AS skill ON ISCHILDNODE(skill,parent) ";
            }
            String clause = "where ((contains(parent.'name', '*" + keyword
                    + "*') OR " + "contains(parent.'industry', '*" + keyword
                    + "*') OR  contains(parent.'email', '*" + keyword
                    + "*') OR  contains(parent.'city', '*" + keyword
                    + "*')) and ISDESCENDANTNODE(parent,'/content/user/')) ";
            if (companyTag.equals("1")) {
                if (!companyValue.equals("")) {
                    clause += "and (contains(exp.'companyName','*"
                            + companyValue + "*') ";
                }
                if (!companyDur.equals("")) {
                    clause += "and (contains(expDurChild.'startDate','*"
                            + companyDur
                            + "*') or contains(expDurChild.'endDate','*"
                            + companyDur + "*')) ";
                }
                if (!companyValue.equals("")) {
                    clause += ") ";
                }
            }
            if (educationTag.equals("1")) {
                if (!educationValue.equals("")) {
                    clause += "and (contains(edu.'school','*" + educationValue
                            + "*') ";
                }

                if (!educationDur.equals("")) {
                    clause += "and (contains(edu.'toDate','*" + educationDur
                            + "*') or contains(edu.'endDate','*" + educationDur
                            + "*')) ";
                }
                if (!educationValue.equals("")) {
                    clause += ") ";
                }
            }
            if (skillTag.equals("1")) {
                clause += "and contains(skill.'skill','*" + skillValue + "*') ";
            }
            if (addressTag.equals("1")) {
                clause += "and contains(parent.'address','*" + addressValue
                        + "*') ";
            }
            Workspace workspace = session.getWorkspace();
            Query query;
            query = workspace.getQueryManager().createQuery(query2 + clause,
                    Query.JCR_SQL2);

            QueryResult qr = query.execute();

            NodeIterator iterator = qr.getNodes();
            ArrayList<String> userArr = new ArrayList<String>();
            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                if (!userArr.contains(node.getName())) {
                    userArr.add(node.getName());
                    searchPeople.add(node);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * final long endTime = System.currentTimeMillis();
         * 
         * System.out.println("Total execution time: " + (endTime - startTime));
         * 
         * System.out.println("-->" + searchPeople);
         */
        return searchPeople;
    }
   
}
