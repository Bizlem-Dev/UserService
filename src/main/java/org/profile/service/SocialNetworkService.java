package org.profile.service;

import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.scribe.oauth.OAuthService;

/**
 * The Interface <code>SocialNetworkService</code> contains all method
 * declaration related to Social networking data extraction. Authentication,
 * Authorization and data extraction from different Social Networking sites are
 * taken place in this section.
 * 
 * @author pranav.arya
 */
public interface SocialNetworkService {

    /**
     * Gets the linkedin token. This method provides an URL which helps in
     * authenticating with Linkedin.
     * 
     * @return the Map which contains requestURL, request Token and request
     *         Secret.
     */
    Map<String, String> getLinkedinToken();

    /**
     * Authorizes the linkedin token. A verifier code is provided by linkedin
     * with request token and request secret which helps in providing the Access
     * Token and Access Secret for LoggedIn User in Linkedin. Access Token and
     * Access secret acts as a password in Linkedin for respective user.
     * 
     * @param token
     *            Contains request Token
     * @param secret
     *            Contains request Secret
     * @param verifier
     *            Contains the verifier Code
     * @return the map which contains Access Token and Access Secret
     */
    Map<String, String> authorizeLinkedinToken(String token, String secret,
            String verifier);

    /**
     * It is common method which helps in getting the response from Different
     * Social Networking sites. REST API, Access Token, Access Secret and object
     * of OAuthService for respective Social Network is provided to extract the
     * output (these parameters are mandatory).
     * 
     * @param token
     *            Contains Access Token
     * @param secret
     *            Contains Access Secret
     * @param url
     *            Contains REST API
     * @param localService
     *            Contains the object of OAuthService
     * @return the output or response as a string
     */
    String getOutput(String token, String secret, String url,
            OAuthService localService);

    /**
     * Access Token and Access Secret is saved in userNode for respective Social
     * Network. So, this method is used to get the Access Token and Access
     * Secret for respective user
     * 
     * @param userId
     *            Contains the userId for respective user
     * @param type
     *            Contains the type of Social Network i.e. Linkedin,Facebook
     *            etc.
     * @return the map which contains Access Token,Access Secret and SocialId
     *         i.e. Linkedin, Twitter etc.
     */
    Map<String, String> getRequestToken(String userId, String type);

    /**
     * Sets the Access token. This method is used to sets the Access Token and
     * Access Secret for respective user
     * 
     * @param token
     *            Contains Access Token
     * @param secret
     *            Contains Access secret
     * @param userId
     *            Contains the userId of respective user
     * @param type
     *            Contains the Social Network type i.e. whether the request is
     *            for linkedin, facebook or twitter.
     * @param flag
     *            It is flag which contains "setToken" or "setId" which helps in
     *            determine whether the request is to set socialId only or to
     *            set token with social Id
     * @param socialId
     *            Contains the socialId i.e. unique ID of facebook, twitter &
     *            linkedin
     */
    void setRequestToken(String token, String secret, String userId,
            String type, String flag, String socialId);

    /**
     * Gets the linkedin data for specific REST API. This method is used to
     * extract number of connection of respective user.
     * 
     * @param userId
     *            Contains the userId of respective user
     * @return the linkedin data in Map which contains connection number and
     *         LinkedinId
     */
    Map<String, String> getLinkedinData(String userId);

    /**
     * Authorizes the facebook token. A verifier code is provided by facebook
     * with request token and request secret which helps in providing the Access
     * Token and Access Secret for LoggedIn User in facebook. Access Token and
     * Access secret acts as a password in facebook for respective user.
     * 
     * @param verifier
     *            Contains the verifier code
     * @return the map which contains Access Token and Access Secret
     */
    Map<String, String> authorizeFacebookToken(String verifier);

    /**
     * Gets the facebook token. This method provides an URL which helps in
     * authenticating with Facebook.
     * 
     * @return the Map which contains requestURL, request Token and request
     *         Secret.
     */
    Map<String, String> getFacebookToken();

    /**
     * Gets the facebook data for specific REST API. This method is used to
     * extract number of connection and Profile date of respective user .
     * 
     * @param userId
     *            Contains the userId of respective user
     * @param type
     *            Flag to determine whether the request is to extract frien or
     *            profile data
     * @return the facebook data in Map which contains connection number and
     *         FacebookId
     */
    Map<String, String> getFacebookData(String userId, String type);

    /**
     * Gets the twitter data for specific REST API. This method is used to
     * extract number of followers, following and Profile date of respective
     * user .
     * 
     * @param userId
     *            Contains the userId of respective user
     * @return the twitter data in Map which contains followers, following and
     *         Twitter's screen name
     */
    Map<String, String> getTwitterData(String userId);
    
    String MailToFriend(SlingHttpServletRequest request);
    String geJsonMessage(SlingHttpServletRequest request);
    String replayPost(SlingHttpServletRequest request);
    
}
