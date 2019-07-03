package org.profile.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.jcr.Node;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * The Interface ProfileService.
 */
public interface ProfileService {

    /**
     * Save profile node.
     *
     * @param request the request
     * @return the string
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    String saveProfileNode(SlingHttpServletRequest request)
            throws ServletException, IOException;
    
    String saveProfileNodeBasic(SlingHttpServletRequest request)
            throws ServletException, IOException;

    
    String saveProfileAddNode(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Save summary node.
     *
     * @param request the request
     * @return the string
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    String saveSummaryNode(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Save experience node.
     *
     * @param request the request
     * @return the string
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    String saveExperienceNode(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Save education node.
     *
     * @param request the request
     * @return the string
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    String saveEducationNode(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Save info node.
     *
     * @param request the request
     * @return the string
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    String saveInfoNode(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Search profile.
     *
     * @param firstName the first name
     * @param flag the flag
     * @return the array list
     */
    @SuppressWarnings("rawtypes")
    ArrayList searchProfile(String firstName, String flag);

    /**
     * Upload profile pic.
     *
     * @param request the request
     * @return the string
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    String uploadProfilePic(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Profile visit.
     *
     * @param username the username
     * @param visitor the visitor
     * @return the string
     */
    String profileVisit(String username, String visitor);

    /**
     * Profile education delete.
     *
     * @param request the request
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void profileEducationDelete(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Profile experience delete.
     *
     * @param request the request
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void profileExperienceDelete(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Profile email n mobile delete.
     *
     * @param userName the user name
     * @param deleteNode the delete node
     * @param childNode the child node
     */
    void profileEmailNMobileDelete(String userName, String deleteNode,
            String childNode);

    /**
     * Friend suggestion.
     *
     * @param userId the user id
     * @return the array list
     */
    @SuppressWarnings("rawtypes")
    ArrayList friendSuggestion(String userId);

    /**
     * Activity stream.
     *
     * @param userName the user name
     * @param properties the properties
     * @param activityTypeNode the activity type node
     * @param activityNode the activity node
     */
    @SuppressWarnings("rawtypes")
    void activityStream(String userName, ArrayList<Map> properties,
            String activityTypeNode, String activityNode);

    /**
     * Map company.
     *
     * @param userId the user id
     * @param mapType the map type
     * @param values the values
     * @param confirm the confirm
     * @param confirmCompany the confirm company
     * @param confirmType the confirm type
     * @param verify the verify
     */
    void mapCompany(String userId, String mapType, String[] values,
            String confirm, String confirmCompany, String confirmType,
            boolean verify);

    /**
     * Map company request.
     *
     * @param companyName the company name
     * @param mapType the map type
     * @param userId the user id
     * @param delete the delete
     */
    void mapCompanyRequest(String companyName, String mapType, String userId,
            boolean delete);

    /**
     * Service check.
     *
     * @param userId the user id
     * @param key the key
     * @param value the value
     * @return the string
     */
    String serviceCheck(String userId, String key, String value);

    /**
     * Gets the service check.
     *
     * @param userId the user id
     * @param key the key
     * @return the service check
     */
    char getServiceCheck(String userId, String key);

    /**
     * Sets the message credential.
     *
     * @param userId the user id
     * @param msgUser the msg user
     * @param msgPassword the msg password
     * @return the string
     */
    String setMessageCredential(String userId, String msgUser,
            String msgPassword);

    /**
     * Gets the service param.
     *
     * @param param the param
     * @param userId the user id
     * @return the service param
     */
    String getServiceParam(String param, String userId);

    /**
     * Sets the user status.
     *
     * @param userId the user id
     * @param userStatus the user status
     */
    void setUserStatus(String userId, String userStatus);

    /**
     * Advance search profile.
     *
     * @param skillTag the skill tag
     * @param SkillValue the skill value
     * @param companyTag the company tag
     * @param companyValue the company value
     * @param companyDur the company dur
     * @param addressTag the address tag
     * @param addressValue the address value
     * @param educationTag the education tag
     * @param educationValue the education value
     * @param educationDur the education dur
     * @param keyword the keyword
     * @return the array list
     */
    ArrayList<Node> advanceSearchProfile(String skillTag, String SkillValue,
            String companyTag, String companyValue, String companyDur,
            String addressTag, String addressValue, String educationTag,
            String educationValue, String educationDur, String keyword);
}
