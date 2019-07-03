package org.profile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.profile.service.LinkedInService;
import org.profile.service.TwitterService;
import org.scribe.builder.api.TwitterApi;
import org.scribe.builder.*;
//import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;

import org.scribe.builder.api.LinkedInApi;
/**
 * Class <code>TwitterServiceImpl</code> contains all the transactions related
 * to twitter. User's Authentication and verification in twitter takes place in
 * this class. Extraction of tweets and posting of message are also implemented
 * in this class.
 *
 * @author pranav.arya
 */
@Component(configurationFactory = true)
@Service(LinkedInService.class)
@Properties({ @Property(name = "linkedService", value = "linkedSet") })
public class LinkedInServiceImpl implements LinkedInService  {

    /** The Constant PROTECTED_RESOURCE_URL contains the Linkedin API URL. */
	 private static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~?format=json";

    /** bundle in an object which instantiate the property file . */
    private static ResourceBundle bundle = ResourceBundle.getBundle("server");

    /** The service is an object containing Twitter's APIKey and APISecret. */
     OAuthService service = new ServiceBuilder()
    .provider(LinkedInApi.class)
    .apiKey("75m4u19jz5vzto")
    .apiSecret("HkImphxpb1tvGUam")
    .callback("http://prod.bizlem.io:8082/portal/servlet/social/mesg.setLinkedinCodes").build();

     @Reference
 	private SlingRepository repos;
    /*
     * (non-Javadoc)
     *
     * @see org.social.service.TwitterService#getVerifier()
     */

    public String[] getVerifier() {
        String[] tokeId = {"", "", ""};
        try {

            Token requestToken = service.getRequestToken();
            String redirect_url = service.getAuthorizationUrl(requestToken);
            tokeId[0] = requestToken.getToken();
            tokeId[1] = requestToken.getSecret();
            tokeId[2] = redirect_url;
        } catch (Exception e) {
            System.out.println("exception getVerifier--" + e.getMessage());
        }
        return tokeId;
    }

    public String[] getToken(String verifierId, String token, String secret) {
        String[] tokenId = {"", "", ""};
        try {
            Token requestToken = new Token(token, secret);
            Verifier verifier = new Verifier(verifierId);
            Token accessToken = service.getAccessToken(requestToken, verifier);
            String userToken = accessToken.getToken();
            String userSecret = accessToken.getSecret();
            tokenId[0] = userToken;
            tokenId[1] = userSecret;
            tokenId[2] = "success";
        } catch (Exception e) {
            e.printStackTrace();
            tokenId[2] = "error";
        }

        return tokenId;
    }

    public String[] getAuth(String token, String secret) {
        String[] response = {"", "", "", "", ""};

        Token accessToken = new Token(token, secret);
        OAuthRequest request = new OAuthRequest(Verb.GET,
                PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        Response result = request.send();
        try {
            JSONObject jsonObj = new JSONObject(result.getBody());
            if (jsonObj.has("errors")) {
                response[3] = "error";
            } else {
                response[0] = jsonObj.getString("firstName");
                response[1] = jsonObj.getString("lastName");
                response[2] = jsonObj.getString("headline");
                JSONObject jsonObj1 = new JSONObject(jsonObj.getString("siteStandardProfileRequest"));
                response[4] = jsonObj1.getString("url");
                response[3] = "success";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            response[3] = "error";
        }
        return response;
    }
    public void setRequestToken(String token, String secret, String userId,
            String type, String flag, String socialId) {
        Node userNode, networkNode, typeNode = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userId);
            if (userNode.hasNode("NetworkProvider")) {
                networkNode = userNode.getNode("NetworkProvider");
            } else {
                networkNode = userNode.addNode("NetworkProvider");
            }

            if (networkNode.hasNode(type)) {
                typeNode = networkNode.getNode(type);
            } else {
                typeNode = networkNode.addNode(type);
            }
            if (flag.equals("setToken")) {
                typeNode.setProperty("token", token);
                typeNode.setProperty("secret", secret);
                typeNode.setProperty("socialId", socialId);
            } else if (flag.equals("setId")) {
                typeNode.setProperty("socialId", socialId);
            }
            session.save();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
    
    
}