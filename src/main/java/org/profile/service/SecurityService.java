package org.profile.service;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * The Interface <code>SecurityService</code> contains all the method
 * declaration related to Security and Privacy section. Verification of Email-Id
 * and Mobile Number is done in this section where verification code is send
 * through email or SMS and by entering verification code on UI, respective
 * mobile number and EMail Id is verified.
 * 
 * @author pranav.arya
 */
public interface SecurityService {

    /**
     * Sends the token. If the request is for Mobile Number verification, then
     * token is sent through SMS. If request is for EMail-Id verification, then
     * email containing the URL with Token is sent on respective Email-ID. On
     * clicking the respective URL, respective EmailId is verified. Randomly
     * generated number 8 digit number is sent for mobile number verification.
     * Node Identifier is sent for Email-Id verification.
     * 
     * @param userId
     *            Contains the userId of loggedIn user
     * @param verification
     *            Contains the fullPath of verification Node. It might be
     *            MobileNumber node or EmailId node.
     * @param flag
     *            the flag determines the request i.e. whether it is for EmailID
     *            or Phone
     * @param verificationFor
     *            Contains the Mobile Number or Email ID for which the user want
     *            to verify
     * @param userName
     *            Contains the Name of loggedIn user.
     */
    void sendToken(String userId, String verification, String flag,
            String verificationFor, String userName);

    /**
     * Verify the token. This method is used to matches the verification code
     * with existing code. Flag determines whether the request is for Phone or
     * EmailId. For Mobile Number verification, token code is authenticated. And
     * for Email-Id verification, node identifier is determined.If node
     * identifier is for respective emailId then it is verified.
     * 
     * @param userId
     *            Contains the userId of the user
     * @param verificationNode
     *            Contains the Verification Node for Email-Id.
     * @param token
     *            Contains the token for Mobile No.
     * @param flag
     *            the flag determines the request i.e. whether it is for EmailID
     *            or Phone
     * @return the string, which contains whether the token is "Valid" or
     *         "Invalid"
     */
    String verifyToken(String userId, String verificationNode, String token,
            String flag);

    /**
     * Call service is a HTTPClient method which helps in invoking the RESTFull
     * web-services of method POST type.
     * 
     * @param urlStr
     *            Contains the URL of REST API
     * @param paramName
     *            Contains the array of parametrs
     * @param paramValue
     *            Conatins the Array of values
     * @return the response of WebService as a String
     */
    String callService(String urlStr, String[] paramName, String[] paramValue);

    /**
     * Sets the privacy for loggedIn user. Privacy is related to user Section.
     * It contains the Personal Privacy, Mail Frequency, Visitor's Identity. In
     * Personal Privacy: contact,email,Connection,Group,Product & Activity
     * privacy is defined on the basis of OnlyMe, Connection and EveryOne.
     * Visitor's Identity: Anonymous or With Identity, this is used in
     * "Who has Visited your Profile?" where user can visit anybody profile as
     * Anonymous user if user selects "anonymous" or with Identity if user
     * selects "Identity". Mail Frequency: Bind the mail frequency by
     * weekly,monthly,daily & yearly for newsLetter, groupsUpdates,
     * promotionalMail, activityUpdates.
     * 
     * @param request
     *            Contains the request object of privacy content
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void setPrivacy(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException,
            IOException;

    /**
     * callGetService is a HTTPClient method which helps in invoking the
     * RESTFull web-services of method GET type.
     * 
     * @param urlString
     *            the url string
     * @return the string
     */
    String callGetService(String urlString);

    /**
     * Sends the message. This method sends the SMS to number. RESTFull API is
     * invoked in this method where SMS is send to specified URL. Password and
     * Username is passed in this method for SMS authentication at webservice's
     * server side.
     * 
     * @param number
     *            Contains the mobile Number
     * @param message
     *            Contains the messagge
     * @param userName
     *            Contains the username for SMS
     * @param password
     *            Contains the password for SMS
     */
    void sendMessage(String number, String message, String userName,
            String password, String userId);

    /**
     * This method extracts the mobile number from user node. This method also
     * sets the consumption of SMS. Property "primaryMobile" contains the mobile
     * number.
     * 
     * @param userId
     *            Contains the userId
     * @param message
     *            Contains message
     * @return the string which contains the response i.e. Limit Exceed or
     *         Message Failure or Success Sent
     */
    String sendUserSMS(String userId, String message);

    /**
     * This method is used to send an Email to respective user. Consumption is
     * also set in this method.Property "primaryEmail" contains the Email Id.
     * 
     * @param userId
     *            Contains the userId
     * @param message
     *            Contains EMail body
     * @param subject
     *            Contains Email Subject
     * @param sendFrom
     *            Contains sender's email address
     * @return the string which contains the response i.e. Limit Exceed or
     *         Message Failure or Success Sent
     */
    String sendUserEmail(String userId, String message, String subject,
            String sendFrom);

    String validateEmailID(String userId, String emailId);
    StringBuilder callGetService(String urlStr, String[] paramName,
			String[] paramValue) ;
    
    void sendNotificationReminder(String userId, String verification, String flag,
            String verificationFor, String userName,String htmltext);

}
