package org.profile.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
//import org.profile.service.impl.SlingHttpServletResponse;

/**
 * The Interface <code>CompanyService</code> contains the all method declaration
 * related to company profile. It contains the functionality like: Company
 * profile Creation, company career creation and deletion, search Company
 * Profile, map user with company, map product with company,map company with
 * company, upload all kind of documents,video and images related to company and
 * get present Stock information related to BSE number.
 * 
 * @author pranav.arya
 */
public interface CompanyService {

    /**
     * Saves and Edit the company Profile. Address, Point Of Contact,
     * Description and general information regarding company is saved in Company
     * Profile.
     * 
     * @param request
     *            Contains the request object which contains all the data
     *            related to Company profile
     * @return null (not using now)
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    String saveCompanyNode(SlingHttpServletRequest request)
            throws ServletException, IOException;
    
    public String setParticipation(SlingHttpServletRequest request);
    public String checkCompanyExists(String companyName);
    public String getPincodeData(String pincode);
    public String saveImage(SlingHttpServletRequest request,String companyName);
    /**
     * Save and Edit company career. Job title, Job description, No. of position
     * and experience is saved as a Job Opening.
     * 
     * @param request
     *            Contains the request object which contains all the data
     *            related to career of company
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void saveCompanyCareer(SlingHttpServletRequest request)
            throws ServletException, IOException;
    
    /**
     * Delete company career. This deletion will close the job opening and
     * marked as expired. Deleted Openings are saved in ClosedJob node.
     * 
     * @param request
     *            Contains the request object which contains the name or Id of
     *            closed Job or closed opening
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void deleteCompanyCareer(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Save the joining reasons for career.
     * 
     * @param request
     *            Contains the request object which contains the data
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void joiReasonCareer(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Search company on the basis of company name, website and company type.
     * 
     * @param keyword
     *            Contains the keyword
     * @return the array list which contains the company node
     */
    @SuppressWarnings("rawtypes")
    ArrayList searchCompany(String keyword);

    /**
     * Map user.
     * 
     * @param request
     *            Contains the request object which contains the data
     * @param delete
     *            the delete
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void mapUser(SlingHttpServletRequest request, boolean delete)
            throws ServletException, IOException;

    /**
     * Map company.
     * 
     * @param request
     *            Contains the request object which contains the data
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void mapCompany(SlingHttpServletRequest request) throws ServletException,
            IOException;

    /**
     * Map company type.
     * 
     * @param request
     *            Contains the request object which contains the data
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void mapCompanyType(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Map user type.
     * 
     * @param request
     *            Contains the request object which contains the data
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void mapUserType(SlingHttpServletRequest request) throws ServletException,
            IOException;

    /**
     * Map product type.
     * 
     * @param request
     *            Contains the request object which contains the data
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void mapProductType(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Upload company logo.
     * 
     * @param request
     *            Contains the request object which contains the data
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void uploadCompanyLogo(SlingHttpServletRequest request)
            throws ServletException, IOException;

    /**
     * Delete mapping.
     * 
     * @param nodeName
     *            the node name
     */
    void deleteMapping(String nodeName);

    /**
     * Maps the Product with User and Company on the basis of mapType variable
     * flag it will decide whether the product is to be mapped with user or
     * company.
     * 
     * @param nodeName
     *            Contains the Node name for respective company or User
     * @param companyName
     *            Contains the Company Name or userId
     * @param values
     *            Contains the product codes with comma separated
     * @param mapType
     *            It is a flag which contains whether the product is to be
     *            mapped with user or company.
     */
    void addTypes(String nodeName, String companyName, String[] values,
            String mapType);

    /**
     * Adds the content category.
     * 
     * @param category
     *            the category
     * @param type
     *            the type
     * @param company
     *            the company
     */
    void addContentCategory(String category, String type, String company);

    /**
     * Video upload.
     * 
     * @param userId
     *            the user id
     * @param request
     *            the request
     * @param category
     *            the category
     * @param company
     *            the company
     * @return the string
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    String videoUpload(String userId, SlingHttpServletRequest request,
            String category, String company) throws ServletException,
            IOException;

    /**
     * This method is used to upload all kind of images and document for
     * respective category.
     * 
     * @param userId
     *            Contains the userId of respective user
     * @param category
     *            Contains the category
     * @param request
     *            Contains the request object which contains the data i.e.
     *            images or documents
     * @param company
     *            Contains the companyId
     * @param type
     *            Contains the type i.e. image or Document
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void allFileUpload(String userId, String category,
            SlingHttpServletRequest request, String company, String type)
            throws ServletException, IOException;

    /**
     * It is used to download the content or document.
     * 
     * @param path
     *            Contains the full path of document Node
     * @return the map which contain Input Stream and File name
     */
    Map<Object, Object> downloadNode(String path);

    /**
     * Gets the stock Detail for BSE. On the basis of BSE number, stock
     * information is provided.
     * 
     * @param id
     *            Contains the Company Id
     * @return Json string which contains stock price and % change
     */
    String getStock(String id);

    /**
     * Gets the stock demo (Not using Now).
     * 
     * @param BSENumber
     *            the bSE number
     * @return the stock demo
     */
    String getStockDemo(String BSENumber);

    /**
     * Gets the info.
     * 
     * @param value
     *            the value
     * @param type
     *            the type
     * @param contextPath
     *            the context path
     * @return the info
     */
    String getInfo(String value, String type, String contextPath);

    /**
     * Sets the privacy for Company Profile. Privacy include : contact
     * Privacy,images Privacy ,video Privacy, product Privacy, user
     * RelationPrivacy, company Relation Privacy, company Document
     * Privacy,company News Privacy.
     * 
     * 
     * @param request
     *            Contains the request object which contains the privacy data
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void setPrivacy(SlingHttpServletRequest request) throws ServletException,
            IOException;

    /**
     * Gets the Redirect URL for Twitter which is further used to obtain the
     * verifier Code.
     * 
     * @return an array which contains the Request URL, Request Token and
     *         Request Secret
     */
    String[] getVerifier();

    /**
     * Sets the company twitter credentials i.e. Access Token and Access Secret
     * is saved for respective company Id.
     * 
     * @param company
     *            Contains the companyId
     * @param token
     *            Contains the Access Token
     * @param secret
     *            Contains the Access Secret
     * @param twitterScreenName
     *            Contains the Screen name
     * @param twitterId
     *            Contains the twitter Id
     * @return flag 0 (fail) or 1 (success) as integer
     */
    int setCompanyTwitterCred(String company, String token, String secret,
            String twitterScreenName, String twitterId);

    /**
     * Gets the Follower, Following and screen name from Twitter through REST
     * API. Access token and Access Secret is necessary for fetching the data
     * through twitter.
     * 
     * @param companyId
     *            Contains the Company ID
     * @return an array of follower count, following count and screen name.
     */
    Map<String, String> getTwitterData(String companyId);

    /**
     * For Social connect with respective company name and URL is enter by
     * administrator of the company. So that, user can join these company of
     * different social networks(Linkedin and Facebook).
     * 
     * @param company
     *            Contains the CompanyId
     * @param name
     *            Contains the Name of Company
     * @param url
     *            Contains the URL of Company
     * @param type
     *            Contains the type of Social Network i.e. Facebook or Linkedin
     */
    void setCompanySocialCred(String company, String name, String url,
            String type);
    
     boolean deleteCompany(String node);
     public String saveCompanyNodeNew(SlingHttpServletRequest request)
 			throws ServletException, IOException;
     public String saveCompanyNodeMdmService(SlingHttpServletRequest request)
  			throws ServletException, IOException;
     public String getSupplrData(String prodName);
     
     public String saveBuyingReq(SlingHttpServletRequest request,SlingHttpServletResponse response);

}
