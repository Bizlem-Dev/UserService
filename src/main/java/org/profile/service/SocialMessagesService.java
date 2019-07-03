package org.profile.service;

import java.util.ArrayList;

import javax.jcr.Node;

/**
 * <code>SocialMessagesService</code> is the interface which contains all the
 * method declaration related to Social Messaging. Here all the operations are
 * performed through socialMessageId which is unique for logged in user.
 * 
 * @author pranav.arya
 */
public interface SocialMessagesService {

    /**
     * Check whether the screen name exists or not.
     *
     * @param screenName
     *            Contains the screenName which is an input through user
     * @return true, if screenName is unique i.e. doesn't exists in JCR
     *         repository
     */
    boolean checkScreenName(String screenName);

    /**
     * On the basis of userId, screenName is created. ScreenName is used for all
     * purpose in this entire module. ScreenName is unique for Social Messaging
     * module.
     *
     * @param screenName
     *            Screen name i.e. unique for user also known as socialMessageId
     * @param userId
     *            UserId of the user
     * @param contactNumber
     *            ContactNumber of the User. Not using this field till now.
     * @param aboutMe
     *            Brief description about the user.
     * @return the userId of the particular user.
     */
    String addSocialId(String screenName, String userId, String contactNumber,
            String aboutMe);

    /**
     * This method is used for two functionality i.e. Follow and UnFollow. On
     * the basis of Follower and Following flag, user is followed. If the
     * removeFlag is true, Follower and Following of particular user is removed
     *
     * @param fromScreenName
     *            Contains the screenName of user who is following another user
     * @param toScreenName
     *            Contains the screenName of user who is followed by another
     *            user.
     * @param flag
     *            Contains the Follower and Following flag.
     * @param remove
     *            Contains the remove flag
     */
    void follow(String fromScreenName, String toScreenName, String flag,
            boolean remove);

    /**
     * Message is posted for particular screenName. On the basis of twitterFlag,
     * message is also posted in Twitter If twitterFloag is true, then message
     * is posted on Twitter else it is only posted on socialMessenger. On Post a
     * message by user, DWR operation is also performed where through reverse
     * ajax all the followed user can see his/her post. This method is also used
     * for posting the extracted tweets from twitter after mapping of twitter
     * account with socialMessaging section.
     *
     * @param socialMessageId
     *            socialMessageId of user which is unique
     * @param message
     *            Contains the message.
     * @param tweetFlag
     *            Flag to post the message on twitter i.e. true or false
     * @param twitterId
     *            If the message is extracted from twitter then it contains the
     *            twitterId which is unique otherwise it remains null
     * @param contextPath 
     */
    void addSocialMessage(String socialMessageId, String message,String tags,
            boolean tweetFlag, String twitterId);

    /**
     * Render the messages of followed user for particular socialMessageId.
     *
     * @param socialMessagingId
     *            socialMessageId of user which is unique
     * @return the arrayList of messages having type <code>Node</code>
     */
    ArrayList<Node> renderMessages(String socialMessagingId);

    /**
     * Post reply for particular message.
     *
     * @param socialMessagingId
     *            socialMessageId of user which is unique
     * @param message
     *            contains the reply of user for particular post
     * @param messageNode
     *            contains the path of post where reply has to be saved
     */
    void postSocialReply(String socialMessagingId, String message,
            String messageNode);

    /**
     * Make the post of another user as favourite or forward for logged in user.
     * When logged in user click the favourite button for post, then mapping of
     * user is created with that particular message. When logged in user click
     * the forward button for post, then path of same posted message of another
     * user is copied in logged in user.
     *
     * @param messageId
     *            contains the path of post i.e. messageId
     * @param socialMessagingId
     *            socialMessageId of user which is unique
     * @param nodeType
     *            it is a flag which determines the request is either for
     *            forward or for favourite
     * @param propertyType
     *            contains the key for property. It will be either forwardId or
     *            favMessageId
     * @param saveMessage
     *            This flag determines whether user mapping has to be done with
     *            posted message or not. It is either true or false.
     */
    void favNforward(String messageId, String socialMessagingId,
            String nodeType, String propertyType, boolean saveMessage);

    /**
     * Sets the twitter credentials i.e. Twitter's user token, user secret,
     * screen name and twitter id for particular loggedIn user.
     *
     * @param userId
     *            Contains the userId of loggedIn user
     * @param token
     *            Contains the user token of Twitter
     * @param secret
     *            Contains the user secret of Twitter
     * @param twitterScreenName
     *            Contains the screenName of Twitter
     * @param twitterId
     *            Contains the twitterId which is unique
     * @return the int i.e. 0 or 1 if any exception occures then it return 0
     *         otherwise it will return 1
     */
    int setTwitterCred(String userId, String token, String secret,
            String twitterScreenName, String twitterId);

    /**
     * Gets the twitter credentials from userNode for loggedIn user.
     *
     * @param userId
     *            Contains the userId of loggedIn user
     * @return an array of twitter credential which contains the twitter user
     *         token at index 0 and twitter user's secret at index 1
     */
    String[] getTwitterCred(String userId,String type);

    /**
     * Sets the twitter tweets in social messaging section after the extraction
     * of tweets from Twitter. After the selection of tweets from a user, an
     * string is passed as a parameter which contains all the tweets separated
     * by !@#%&, delimiter and tweetId are separated by comma(,) delimiter.
     *
     * @param tweets
     *            Contains all the selected tweets separated by delimiter i.e.
     *            !@#%&,
     * @param tweetId
     *            Contains all the selected tweetId separated by delimiter i.e.
     *            comma(,)
     * @param userId
     *            Contains the userId of loggedIn user
     */
    void setTwitterTweet(String tweets, String tweetId, String userId);

    /**
     * Gets the limited message of followers at the main page.
     *
     * @param startlimit
     *            contains the beginIndex of arraylist of messages
     * @param limit
     *            contains the limit of messages
     * @param socialMessagingId
     *            socialMessageId of user which is unique
     * @param app
     *            contains the contextPath of an application
     * @return the limited message
     */
    String getlimitedMessage(int startlimit, int limit,
            String socialMessagingId, String app);

	ArrayList<String> searchTag(String parameter);

	/*ArrayList<String> allTags();*/

	ArrayList<Node> messageResults(String parameter, String searchType);

	String randomUser(String userId,String contextPath);
}
