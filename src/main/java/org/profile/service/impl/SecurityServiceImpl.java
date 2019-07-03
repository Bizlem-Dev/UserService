package org.profile.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import javax.jcr.Session;
import javax.jcr.Node;
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
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.SecurityService;

/**
 * The Class <code>SecurityServiceImpl</code> contains all the method
 * implementation related to Security and Privacy section. Verification of
 * Email-Id and Mobile Number is done in this section where verification code is
 * send through email or SMS and by entering verification code on UI, respective
 * mobile number and EMail Id is verified.
 * 
 * @author pranav.arya
 */
@Component(configurationFactory = true)
@Service(SecurityService.class)
@Properties({ @Property(name = "security", value = "securityService") })
public class SecurityServiceImpl implements SecurityService {

    /** Object of SlingRepository. */
    @Reference
    private SlingRepository repos;

    /** Object of ResourceBundle. */
    private ResourceBundle bundle;

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.SecurityService#sendToken(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void sendToken(String userId, String verification, String flag,
            String verificationFor, String userName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("HH-mm-ss");
        Date date = new Date();
        String message = "";
        String token = "";
        bundle = ResourceBundle.getBundle("server");
        Node node, email, contactNode, mobileNode = null;
        NodeIterator emailNodes = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            node = session.getNode(verification);

            if (flag.equals("EmailID")) {
                token = node.getIdentifier();
                message = MessageFormat.format(
                        bundle.getString("sendMail.body"), userName, token);
                String url = bundle.getString("sendMail.address");
                String[] paramName = { "emailto[]", "emailfrom[]",
                        "emailsub[]", "emailmsg[]" };
                String[] paramValue = { verificationFor,
                        bundle.getString("sendMail.from"),
                        bundle.getString("sendMail.subject"), message };
                callService(url, paramName, paramValue);
                node.setProperty("emailToken", token);

            } else if (flag.equals("Phone")) {
                token = gen8DigitToken();
                message = MessageFormat.format(
                        bundle.getString("sendSms.body"), token);
                sendMessage(verificationFor, message,
                        bundle.getString("sendSms.username"),
                        bundle.getString("sendSms.password"), userId);
                node.setProperty("mobileToken", token);

            }
            session.save();
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }

    }

    
    // akhilesh changes
    
    @SuppressWarnings("unused")
    public void sendNotificationReminder(String userId, String verification, String flag,
            String verificationFor, String userName, String htmltext) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("HH-mm-ss");
        Date date = new Date();
        String message = "";
        String token = "";
        bundle = ResourceBundle.getBundle("server");
        Node node, email, contactNode, mobileNode = null;
        NodeIterator emailNodes = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
          //  node = session.getNode(verification);

            if (flag.equals("ConnectionEmailID")) {
          //      token = node.getIdentifier();
                //message = MessageFormat.format(htmltext, userName, token);
                message = htmltext;
                String url = bundle.getString("sendMail.address");
                String[] paramName = { "emailto[]", "emailfrom[]",
                        "emailsub[]", "emailmsg[]" };
                String[] paramValue = { verificationFor,
                        bundle.getString("sendMail.from")," Notification pending Reguest", message };
                callService(url, paramName, paramValue);
              //  node.setProperty("emailToken", token);
                
            }else if(flag.equals("visitor")){
            	message = htmltext;
                String url = bundle.getString("sendMail.address");
                String[] paramName = { "emailto[]", "emailfrom[]",
                        "emailsub[]", "emailmsg[]" };
                String[] paramValue = { verificationFor,
                        bundle.getString("sendMail.from"),"People are looking at your Bizlem profile", message };
                callService(url, paramName, paramValue);
            }else if(flag.equals("accountsummary")){
            	message = htmltext;
                String url = bundle.getString("sendMail.address");
                String[] paramName = { "emailto[]", "emailfrom[]",
                        "emailsub[]", "emailmsg[]" };
                String[] paramValue = { verificationFor,
                        bundle.getString("sendMail.from"),"Service Account Summmary", message };
                callService(url, paramName, paramValue);
            }
            else if(flag.equals("serviceexpiry")){
            	message = htmltext;
                String url = bundle.getString("sendMail.address");
                String[] paramName = { "emailto[]", "emailfrom[]",
                        "emailsub[]", "emailmsg[]" };
                String[] paramValue = { verificationFor,
                        bundle.getString("sendMail.from"),"Service Expiry Reminder", message };
                callService(url, paramName, paramValue);
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
     * @see org.profile.service.SecurityService#verifyToken(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public String verifyToken(String userId, String verificationNode,
            String token, String flag) {
        Node email, mobileNode = null;
        Session session;
        String code = "Invalid";
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (flag.equals("EmailID")) {
                email = session.getNodeByIdentifier(token);
                email.setProperty("verifiedEmailFlag", "yes");
                session.save();
                return email.getParent().getParent().getParent().getName();
            } else if (flag.equals("Phone")) {

                mobileNode = session.getNode(verificationNode);
                if (mobileNode.getProperty("mobileToken").getString()
                        .equals(token)) {
                    code = "Valid";
                    mobileNode.setProperty("verifiedMobileFlag", "yes");
                }
            }
            session.save();
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return code;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.SecurityService#callService(java.lang.String,
     * java.lang.String[], java.lang.String[])
     */
    public String callService(String urlStr, String[] paramName,
            String[] paramValue) {

        StringBuilder response = new StringBuilder();
        URL url;
        try {
            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            // Create the form content
            OutputStream out = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            for (int i = 0; i < paramName.length; i++) {
                writer.write(paramName[i]);
                writer.write("=");
                writer.write(URLEncoder.encode(paramValue[i], "UTF-8"));
                writer.write("&");
            }
            writer.close();
            out.close();
            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }
            // Buffer the result into a string
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();

            conn.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return response.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.SecurityService#sendMessage(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void sendMessage(String number, String message, String userName,
            String password, String userId) {

        URL url;
        HttpURLConnection urlConn;
        DataOutputStream printout;
        DataInputStream input;
        bundle = ResourceBundle.getBundle("server");
        String content = bundle.getString("sendSms.address");
        content += "&u=" + userName;
        content += "&p=" + password;
        content += "&to=0" + number;
        content += "&msg=" + message;
        content += "&eusr=" + userId;
        try {
            url = new URL(content);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            conn.disconnect();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.SecurityService#callGetService(java.lang.String)
     */
    @SuppressWarnings("unused")
    public String callGetService(String urlString) {

        URL url;
        HttpURLConnection urlConn;
        DataOutputStream printout;
        DataInputStream input;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            conn.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.SecurityService#setPrivacy(org.apache.sling.api.
     * SlingHttpServletRequest)
     */
    public void setPrivacy(SlingHttpServletRequest request,SlingHttpServletResponse response)
            throws ServletException, IOException {
        Session session;
        String userId = request.getRemoteUser().replace("@", "_");
        Node node, privacyNode, personalNode, identityNode, mailNode = null;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            node = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userId);

            if (node.hasNode("Privacy")) {
                privacyNode = node.getNode("Privacy");
            } else {
                privacyNode = node.addNode("Privacy");
            }

            if (privacyNode.hasNode("Personal")) {
                personalNode = privacyNode.getNode("Personal");
            } else {
                personalNode = privacyNode.addNode("Personal");
            }

            personalNode.setProperty("contactPrivacy",
                    request.getParameter("contactPrivacy"));
            personalNode.setProperty("emailPrivacy",
                    request.getParameter("emailPrivacy"));
            personalNode.setProperty("galleryPrivacy",
                    request.getParameter("galleryPrivacy"));
            personalNode.setProperty("photostoryPrivacy",
                    request.getParameter("photostoryPrivacy"));
            personalNode.setProperty("connectionPrivacy",
                    request.getParameter("connectionPrivacy"));
            personalNode.setProperty("activityPrivacy",
                    request.getParameter("activityPrivacy"));
            personalNode.setProperty("groupsPrivacy",
                    request.getParameter("groupsPrivacy"));
            personalNode.setProperty("productPrivacy",
                    request.getParameter("productPrivacy"));
            personalNode.setProperty("companyPrivacy",
                    request.getParameter("companyPrivacy"));

            if (privacyNode.hasNode("Identity")) {
                identityNode = privacyNode.getNode("Identity");
            } else {
                identityNode = privacyNode.addNode("Identity");
            }
            identityNode.setProperty("visitorPrivacy",
                    request.getParameter("visitorPrivacy"));

            if (privacyNode.hasNode("MailFrequency")) {
                mailNode = privacyNode.getNode("MailFrequency");
            } else {
                mailNode = privacyNode.addNode("MailFrequency");
            }
            mailNode.setProperty("newsLetter",
                    request.getParameter("newsLetter"));
            mailNode.setProperty("groupsUpdates",
                    request.getParameter("groupsUpdates"));
            mailNode.setProperty("promotionalMail",
                    request.getParameter("promotionalMail"));
            mailNode.setProperty("activityUpdates",
                    request.getParameter("activityUpdates"));

            session.save();
        } catch (PathNotFoundException e) {
        	response.getOutputStream().print("PathNotfound---"+e.getMessage());
            e.printStackTrace();
        } catch (RepositoryException e) {
        	response.getOutputStream().print("Respository---"+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
        	response.getOutputStream().print("Exception---"+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gen8 digit token.
     * 
     * @return the string
     */
    private String gen8DigitToken() {
        String strToken = "";
        try {
            Random randomPassword = new Random();
            int i = randomPassword.nextInt(10);
            int j = randomPassword.nextInt(100);
            int k = randomPassword.nextInt(100);
            int l = randomPassword.nextInt(10);
            int m = randomPassword.nextInt(10);

            strToken = "" + i + j + k + l + m;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strToken;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.SecurityService#sendUserSMS(java.lang.String,
     * java.lang.String)
     */
    public String sendUserSMS(String userId, String message) {
        Session session = null;
        Node userNode = null;
        try {
            String mobileNumber = "";
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userId);
            bundle = ResourceBundle.getBundle("server");
            if (userNode.hasProperty("primaryMobile")) {
                mobileNumber = userNode.getProperty("primaryMobile")
                        .getString();
                if (userNode.getProperty("serviceCheck").getString().charAt(0) == 'Y'
                        && userNode.hasProperty("smsUserID")
                        && !userNode.getProperty("smsPassword").getString()
                                .equals("")) {

                    String response = callGetService(bundle
                            .getString("service.consumption")
                            + "&userId="
                            + userNode.getProperty("primaryEmail").getString()
                            + "&quantity=1&productCode=sms");
                    boolean accessFlag = false;
                    JSONObject json = new JSONObject(response);
                    accessFlag = json.getBoolean("accessFlag");
                    if (accessFlag) {
                        sendMessage(mobileNumber, message, userNode
                                .getProperty("smsUserID").getString(), userNode
                                .getProperty("smsPassword").getString(), userId);
                        return "Message Sent SuccessFully";
                    } else {
                        return "Limit Exceeded";
                    }
                } else {
                    return "Not Provisioned";
                }
            } else {
                return "Mobile Number does not Exist";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Message Sent Failure";
        } finally {
            session.logout();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.profile.service.SecurityService#sendUserEmail(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public String sendUserEmail(String userId, String message, String subject,
            String sendFrom) {

        Session session = null;
        Node userNode = null;
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userId.replace("@", "_"));
            bundle = ResourceBundle.getBundle("server");
            if (userNode.getProperty("serviceCheck").getString().charAt(1) == 'Y') {

                String response = callGetService(bundle
                        .getString("service.consumption")
                        + "&userId="
                        + userId
                        + "&quantity=1&productCode=email");
                boolean accessFlag = false;
                JSONObject json = new JSONObject(response);
                accessFlag = json.getBoolean("accessFlag");
                if (accessFlag) {
                    String url = bundle.getString("sendMail.address");
                    String[] paramName = { "emailto[]", "emailfrom[]",
                            "emailsub[]", "emailmsg[]" };
                    String[] paramValue = { userId,
                            bundle.getString("sendMail.from"), subject, message };
                    callService(url, paramName, paramValue);
                    return "Mail Sent SuccessFully";
                } else {
                    return "Limit Exceeded";
                }
            } else {
                return "Not Provisioned";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Mail Sent Failed";
        } finally {
            session.logout();
        }

    }
    
    public String validateEmailID(String userId, String emailId){
        Node email, userNode, contentNode = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            contentNode = session.getNode("/content/user");
            if(contentNode.hasNode(userId.replace("@", "_"))){
                userNode = contentNode.getNode(userId.replace("@", "_"));
                if(userNode.hasNode("ContactDetails") 
                        && userNode.getNode("ContactDetails").hasNode("EmailID")
                        && userNode.getNode("ContactDetails")
                            .getNode("EmailID").hasNode(emailId.replace("@", "_"))){
                    email = userNode.getNode("ContactDetails")
                            .getNode("EmailID").getNode(emailId.replace("@", "_"));
                            email.setProperty("verifiedEmailFlag", "yes");
                }
            }
            session.save();
        } catch (PathNotFoundException e) {
           
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return "done";
    } 
    
    
    public StringBuilder callGetService(String urlStr, String[] paramName,
			String[] paramValue) {
		URL url;
		StringBuilder requestString = new StringBuilder(urlStr);
		StringBuilder sb = new StringBuilder();
		InputStream stream=null;
		try {
			url = new URL(requestString.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			stream=conn.getInputStream();
			 BufferedReader rd = new BufferedReader(new InputStreamReader(
						stream));
				
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
	                        conn.disconnect();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return sb;
	}

  
}
