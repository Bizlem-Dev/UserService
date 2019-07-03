package org.profile.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Interface <code>GroupService</code> contains all the method declaration
 * related to Group. This contains the functionality like: Group creation, Join
 * group, Dis-connect Group, Search Group, Delete Group.
 * 
 * @author pranav.arya
 */
public interface GroupService {

    /**
     * Save group information. Group are of two type: public and private. In
     * public access type, any user can join respective group. And in private
     * access type, admin approval is necessary to join group. Group admin is
     * the creator of group.
     * 
     * @param request
     *            Contains the request Object of SlingHttpServletRequest
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Long saveGroupInfo(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Search group on the basis of groupName.
     * 
     * @param keyword
     *            Contains the keyword
     * @return the array list of Group list.
     */
    @SuppressWarnings("rawtypes")
    public ArrayList searchGroup(String keyword);

    /**
     * Adds the member to respective group. This method saves the user's request
     * in groupNode and also saves the groupName in userNode. This method is
     * also used to accept the request by group Admin
     * 
     * @param groupName
     *            Contains the groupName
     * @param userId
     *            Contains the userId of member
     * @param flag
     *            Contains the flag i.e. true or false. That determines whether
     *            the request is from user to group or group to user.
     * @param acceptPrivate
     *            Contains "yes" flag to accept the request
     */
    public void addMember(String groupName, String userId, boolean flag,
            String acceptPrivate);

    /**
     * This method is used to delete any node by passing the full path
     * 
     * @param path
     *            full path is passed to delete respective node
     */
    public boolean deletNode(String path);

    /**
     * This method is used to disconnect from Group.
     * 
     * @param groupName
     *            Contains the GroupName
     * @param userId
     *            Contains the userId of the user.
     */
    public void unJoinGroup(String groupName, String userId);
}
