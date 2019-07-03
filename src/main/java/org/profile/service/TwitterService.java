package org.profile.service;

import java.util.ArrayList;
import java.util.Map;

/**
 * <code>TwitterService</code> is an interface which contains all methods
 * related to twitter transactions.
 *
 * @author pranav.arya
 */
public interface TwitterService {

    /**
     * This method generates the verifier for twitter which is an url containing
     * Twitter domain's address.
     *
     * @return an array which contains Token at index 0, Secret at index 1 and
     *         url with verifier at index 2.
     */
    String[] getVerifier();

    /**
     * After verification this method will generate an Access token and Access
     * secret for particular loggedIn user in Twitter.
     *
     * @param verifierId
     *            Contains the verifier Id
     * @param token
     *            Contains the requestToken
     * @param secret
     *            Contains the requestSecret
     * @return an array which contains Access Token at index 0, Access Secret at
     *         index 1 and flag for success or failure at index 2
     */
    String[] getToken(String verifierId, String token, String secret);

    /**
     * This method is used to send a message to twitter on the basis of Access
     * Token and Access Secret.
     *
     * @param token
     *            Contains the Access Token
     * @param secret
     *            Contains the Access Secret
     * @param message
     *            Contains the message
     * @return the flag which contains either done on successful post or error
     *         whenever the submition fails
     */
    String sendMessage(String token, String secret, String message);

    /**
     * Gets the authentication data through Twitter.
     *
     * @param token
     *            Contains the Access Token
     * @param secret
     *            Contains the Access Secret
     * @return an array which contains Access Token at index 0, Access Secret at
     *         index 1, flag for success or error at index 2, screenName of
     *         Twitter is at index 3, twitter unique Id is at index 4
     */
    String[] getAuth(String token, String secret);

    /**
     * Gets the top 10 tweets from Twitter.
     *
     * @param token
     *            Contains the Access Token
     * @param secret
     *            Contains the Access Secret
     * @return the ArrayList of type MAP<String,String> containing top 10 tweets
     *         of an user. ArrayList contains the Map of profileImage,
     *         screenName, tweets, tweetId, firstName
     */
//    String getTweets(String token, String secret);
    ArrayList<Map<String, String>> getTweets(String token,
            String secret) ;
}
