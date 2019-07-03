package org.profile.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.w3c.dom.Element;

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
public interface RSSReaderService {

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
    ArrayList writeFeed(String url);    
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
    
    String getValue(Element parent, String nodeName);
    
    String readJsonFromUrl(String url);
    }
