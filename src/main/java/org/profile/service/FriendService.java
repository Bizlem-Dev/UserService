package org.profile.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Interface <code>FriendService</code> contains the method declaration related
 * to user's friend section. This contains functionality like: sending and
 * receiving friend request, disconnecting friends, import contacts from Social
 * Networking sites, upload contacts of Linkedin, Find mutual friends, Get
 * Online friends, Check if user is online or not.
 * 
 * @author pranav.arya
 */
public interface FriendService {

    /**
     * This method is used to send and receive friend request from loggedIn
     * user.
     * 
     * @param userId
     *            Contains the userId of loggedIn users
     * @param friend
     *            Contains the userId of
     * @param sender
     *            Contains the flag whether the
     * @param request
     *            Contains the request flag i.e. accepted or pending
     * @param friendMessage
     *            Contains the message of loggedIn user
     * @param friendType
     *            Contains the type of friend i.e colleague, friend or alumni
     */
    void connectFriend(String userId, String friend, String sender,
            String request, String friendMessage, String friendType);

    /**
     * Import contacts from different Social Networking sites.
     * 
     * @param userName
     *            Contains the userId of loggedIn users
     * @param providerId
     *            Contains the Social Networking providerId.
     * @param emailNode
     *            Contains the emailNode's value
     * @param email
     *            Contains the emailId of contact
     * @param firstName
     *            Contains the first name of contact
     * @param lastName
     *            Contains the last name of contact
     */
    void importContacts(String userName, String providerId, String emailNode,
            String email, String firstName, String lastName);

    /**
     * Upload Contacts from Excel. It is being used for Linkedin contact import.
     * Particular format is defined by Linkedin. On the basis of column index,
     * data is imported.
     * 
     * @param request
     *            Contains the request object
     * @return the string which contains the extension of file i.e. csv or xls
     *         (Not using now)
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    String uploadImportedContacts(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Adds the imported contacts. It is being specifically used for Linkedin
     * Import contact.
     * 
     * @param userName
     *            Contains the userId of loggedIn users
     * @param providerId
     *            Contains the Social Networking providerId i.e. Linkedin
     * @param emailNode
     *            Contains the emailNode's value
     * @param importedValues
     *            It is an ArrayList which contains all values
     */
    void addImportedContacts(String userName, String providerId,
            String emailNode, ArrayList<String> importedValues);

    /**
     * This method is used to determine the mutual friend with the loggedIn
     * user.
     * 
     * @param userId
     *            Contains the userId of loggedIn users
     * @param friendId
     *            Contains the userId of visitor's profile
     * @return the array list of mutual friends
     */
    @SuppressWarnings("rawtypes")
    ArrayList mutualFriend(String userId, String friendId);

    /**
     * This method is used to determine the online friends.
     * 
     * @param userId
     *            Contains the userId of loggedIn users
     * @return the array list of online friends
     */
    ArrayList<String> onlineFriend(String userId);

    /**
     * This method is used to dis-connect with friend.
     * 
     * @param userId
     *            Contains the userId of loggedIn users
     * @param friend
     *            Contains the userId of connected friend
     */
    void disConnectFriend(String userId, String friend);

    /**
     * This method is used to determine whether the user is online or not.
     * UserId is passed as a parameter to determine whether this userId is
     * online or not.
     * 
     * @param userId
     *            the user id
     * @return the string
     */
    String onlineCheck(String userId);

    /**
     * This method is used to iterate imported contacts from different social
     * networking sites
     * 
     * @param userId
     *            Contains userId of user
     * @param contactList
     *            Contains contact list in json format
     */
    void importContactList(String userId, String contactList);

    /**
     * This method is used to send Registration invitation to selected userList
     * 
     * @param userList
     *            Contains the list of selected user
     * @param userId
     *            Contains the logged in userId
     * @param providerId
     *            Contains the providerId i.e. google, yahoo etc
     * @param messageBody
     *            Contains the message body
     */
    void sendInvite(String userList, final String userId,
            final String providerId, String messageBody);

    /**
     * This method contains the scheduler for sending friend request to newly
     * registered user.
     * 
     * @return the list of users which gets connected
     */
    String friendRequestSchedular();

    /**
     * This method returns the status of registration invitation
     * 
     * @param userId
     *            Contains the loggedIn userId
     * @param friendId
     *            Contains the friend userId
     * @return the json string which contains the status of invitation
     */
    String friendInvitationStatus(String userId, String friendId);

    /**
     * This method is used to update the registration invitation status 
     * @param userId
     *            Contains the loggedIn userId
     * @param friendId
     *            Contains the friend userId
     * @param status
     *            Contains the updated status
     */
    void updateInvitationStatus(String userId, String friendId, String status);
}
