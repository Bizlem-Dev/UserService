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
import org.profile.service.TwitterService;
import org.scribe.builder.api.TwitterApi;
import org.scribe.builder.*;
//import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;



/**
 * Class <code>TwitterServiceImpl</code> contains all the transactions related
 * to twitter. User's Authentication and verification in twitter takes place in
 * this class. Extraction of tweets and posting of message are also implemented
 * in this class.
 *
 * @author pranav.arya
 */
@Component(configurationFactory = true)
@Service(TwitterService.class)
@Properties({ @Property(name = "twitterService", value = "twitterSet") })
public class TwitterServiceImpl implements TwitterService  {

    /** The Constant PROTECTED_RESOURCE_URL contains the Twitter API URL. */
    private static final String PROTECTED_RESOURCE_URL
            = "https://api.twitter.com/1.1/statuses/update.json";

    /** bundle in an object which instantiate the property file . */
    private static ResourceBundle bundle = ResourceBundle.getBundle("server");

    /** The service is an object containing Twitter's APIKey and APISecret. */
    private static OAuthService service = new ServiceBuilder()
            .provider(TwitterApi.SSL.class)
            .apiKey(bundle.getString("twitter.key"))
            .apiSecret(bundle.getString("twitter.secret"))
            .callback(
                    bundle.getString("sling.serverSpec")
                            + "/servlet/social/mesg.setTwitterId").build();


    /*
     * (non-Javadoc)
     *
     * @see org.social.service.TwitterService#getVerifier()
     */
    public String[] getVerifier() {
        String[] tokeId = {"", "", "" };
        Token requestToken = service.getRequestToken();
        String redirect_url = service.getAuthorizationUrl(requestToken);
        tokeId[0] = requestToken.getToken();
        tokeId[1] = requestToken.getSecret();
        tokeId[2] = redirect_url;
        return tokeId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.social.service.TwitterService#getToken(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String[] getToken(String verifierId, String token, String secret) {
        String[] tokenId = {"", "", "" };
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

    /*
     * (non-Javadoc)
     *
     * @see org.social.service.TwitterService#sendMessage(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String sendMessage(String token, String secret, String message) {
        Token accessToken = new Token(token, secret);
        OAuthRequest request = new OAuthRequest(Verb.POST,
                PROTECTED_RESOURCE_URL);
        request.addBodyParameter("status", message);
        service.signRequest(accessToken, request);
        Response response = request.send();
        try {
            JSONObject jsonObj = new JSONObject(response.getBody());
            if (jsonObj.has("errors")) {
                return "error";
            } else {
                return "done";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.social.service.TwitterService#getAuth(java.lang.String,
     * java.lang.String)
     */
    public String[] getAuth(String token, String secret) {
        String[] response = {"", "", "", "", "" };
        response[0] = token;
        response[1] = secret;
        Token accessToken = new Token(token, secret);
        OAuthRequest request = new OAuthRequest(Verb.GET,
                "https://api.twitter.com/1.1/account/verify_credentials.json");
        service.signRequest(accessToken, request);
        Response result = request.send();
        try {
            JSONObject jsonObj = new JSONObject(result.getBody());
            if (jsonObj.has("errors")) {
                response[3] = "error";
            } else {
                response[2] = jsonObj.getString("screen_name");
                response[4] = jsonObj.getString("id");
                response[3] = "success";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            response[3] = "error";
        }
        return response;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.social.service.TwitterService#getTweets(java.lang.String,
     * java.lang.String)
     */
    /* public String getTweets(String token,
            String secret) {
        JSONArray array
                = new JSONArray();
        Map<String, String> map = null;
        JSONObject obj = null;
        Token accessToken = new Token(token, secret);
        OAuthRequest request = new OAuthRequest(Verb.GET,
                "https://api.twitter.com/1.1/statuses/user_timeline.json"
                + "?exclude_replies=true&include_rts"
                + "=false&count=10");
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("bxfbx" + response.getBody());
        try {
            JSONArray jsonArr = new JSONArray(response.getBody());
            for (int i = 0; i < jsonArr.length(); i++) {

                map = new HashMap<String, String>();
                obj = new JSONObject();
                JSONObject jsonObj = (JSONObject) jsonArr.get(i);
                JSONObject jsonUserObj = (JSONObject) jsonObj.get("user");
                obj.put("tweet", jsonObj.getString("text"));
                obj.put("tweet_id", jsonObj.getString("id"));

                obj.put("twitter_profileImage",
                        jsonUserObj.getString("profile_image_url"));
                obj.put("twitter_name", jsonUserObj.getString("name"));
                obj.put("twitter_screenName",
                        jsonUserObj.getString("screen_name"));
                array.put(obj);
                System.out.println("array--" + array);
            }
        } catch (Exception e) {
            
        }
        return array.toString();
    }*/
     public ArrayList<Map<String, String>> getTweets(String token,
             String secret) {
     ArrayList<Map<String, String>> array =
                 new ArrayList<Map<String, String>>();
     Map<String, String> map = null;
     Token accessToken = new Token(token, secret);
     OAuthRequest request = new OAuthRequest(Verb.GET,
             "https://api.twitter.com/1.1/statuses/user_timeline.json"
                     + "?exclude_replies=true&include_rts"
                     + "=false&count=10");
     service.signRequest(accessToken, request);
     Response response = request.send();
     try {
         JSONArray jsonArr = new JSONArray(response.getBody());
         for (int i = 0; i < jsonArr.length(); i++) {
             map = new HashMap<String, String>();
             JSONObject jsonObj = (JSONObject) jsonArr.get(i);
             JSONObject jsonUserObj = (JSONObject) jsonObj.get("user");
             map.put("tweet", jsonObj.getString("text"));
             map.put("tweet_id", jsonObj.getString("id"));

             map.put("twitter_profileImage",
                     jsonUserObj.getString("profile_image_url"));
             map.put("twitter_name", jsonUserObj.getString("name"));
             map.put("twitter_screenName",
                     jsonUserObj.getString("screen_name"));
             array.add(map);
         }

     } catch (JSONException e) {
         map = new HashMap<String, String>();
         map.put("error", e.getMessage());
         array.add(map);
     }
     return array;
 }
}