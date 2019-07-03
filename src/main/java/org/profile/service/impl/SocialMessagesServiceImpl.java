package org.profile.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.profile.service.SocialMessagesService;
import org.profile.service.TwitterService;

/**
 * Class <code>SocialMessagesServiceImpl</code> implements an interface
 * <code>SocialMessagesService</code>. This class contains all the functionality
 * related to social messaging section.
 * <p>
 * It contains all the functionality like :
 * <ul>
 *     <li>Creation of account {@link #addSocialId}
 *      <li>Validation screenName {@link #checkScreenName}
 *      <li>Follow an user {@link #follow}
 *      <li>Post message {@link #addSocialMessage}
 *      <li>Rendering messages {@link #renderMessages}
 *      <li>Add reply to a posted message {@link #postSocialReply}
 *      <li>Add as forward or favourite to a message {@link #favNforward}
 *      <li>Set Twitter Credential {@link #setTwitterCred}
 *      <li>Get Twitter Credential {@link #getTwitterCred}
 *      <li>Set Twitter Tweets {@link #setTwitterTweet}
 *      <li>Get Limited Messages {@link #getlimitedMessage}
 * </ul>
 * 
 * @author pranav.arya
 */
@Component(configurationFactory = true)
@Service(SocialMessagesService.class)
@Properties({ @Property(name = "socialMessageService",
                value = "socialMessage") })
public class SocialMessagesServiceImpl implements SocialMessagesService {

    /** The repos. */
    @Reference
    private SlingRepository repos;

    /** The twitter_service. */
    @Reference
    private TwitterService twitter_service;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#checkScreenName(java.lang.String
     * )
     */
    public boolean checkScreenName(String screenName) {
        Node socialNode, socialId = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (!session.getRootNode().getNode("content")
                    .hasNode("socialMessenger")) {
                return true;
            } else {
                socialNode = session.getRootNode().getNode("content")
                        .getNode("socialMessenger");
            }
            if (!socialNode.hasNode("socialMessagingId")) {
                return true;
            } else {
                socialId = socialNode.getNode("socialMessagingId");
            }
            if (socialId.hasNode(screenName)) {
                return false;
            } else {
                return true;
            }
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#addSocialId(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unused")
    public String addSocialId(String screenName, String userId,
            String contactNumber, String aboutMe) {
        Node node, socialNode, userNode, nameNode, socialId = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (session.getRootNode().getNode("content")
                    .hasNode("socialMessenger")) {
                socialNode = session.getRootNode().getNode("content")
                        .getNode("socialMessenger");
            } else {
                socialNode = session.getRootNode().getNode("content")
                        .addNode("socialMessenger");
            }

            if (socialNode.hasNode("socialMessagingId")) {
                socialId = socialNode.getNode("socialMessagingId");
            } else {
                socialId = socialNode.addNode("socialMessagingId");
            }

            if (socialId.hasNode(screenName)) {
                userNode = socialId.getNode(screenName);
            } else {
                userNode = socialId.addNode(screenName);
            }

            nameNode = session.getNode(userId);
            userNode.setProperty("firstName", nameNode.getProperty("name")
                    .getString());
            if (userNode.hasProperty("lastName")) {
                userNode.setProperty("lastName",
                        nameNode.getProperty("lastName").getString());
            }
            userNode.setProperty("userId", userId);
            userNode.setProperty("aboutMe", aboutMe);
            userNode.setProperty("contactNumber", contactNumber);
            userNode.setProperty("sling:resourceType", "socialMessages");
            /* Add ScreenName in Person Node of a user */
            nameNode.setProperty("socialMessagingId", screenName);
            session.save();
            return nameNode.getName();
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.social.service.SocialMessagesService#follow(java.lang.String,
     * java.lang.String, java.lang.String, boolean)
     */
    public void follow(String fromScreenName, String toScreenName, String flag,
            boolean remove) {
        Node node, socialNode, userNode, followNode = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            socialNode = session.getRootNode().getNode("content")
                    .getNode("socialMessenger").getNode("socialMessagingId");
            if (socialNode.hasNode(fromScreenName)) {
                userNode = socialNode.getNode(fromScreenName);
            } else {
                userNode = socialNode.addNode(fromScreenName);
            }

            if (userNode.hasNode(flag)) {
                node = userNode.getNode(flag);
            } else {
                node = userNode.addNode(flag);
            }

            if (node.hasNode(toScreenName)) {
                followNode = node.getNode(toScreenName);
                if (remove) {
                    followNode.remove();
                }
            } else {
                followNode = node.addNode(toScreenName);
                followNode.setProperty("userId",
                        socialNode.getNode(toScreenName).getProperty("userId")
                                .getString());
            }

            session.save();
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#addSocialMessage(java.lang.
     * String, java.lang.String, boolean, java.lang.String)
     */
    public void addSocialMessage(String socialMessageId, String message,String tags,
            boolean tweetFlag, String twitterId) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        // DateFormat dateFormat2 = new SimpleDateFormat("HH-MM-SS");
        Node messageNode = null, socialUserNode = null, socialNode, userNode = null,tagNode;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            socialNode = session.getRootNode().getNode("content")
                    .getNode("socialMessenger");
            if (socialNode.hasNode("SocialMessages")) {
                messageNode = socialNode.getNode("SocialMessages");
            } else {
                messageNode = socialNode.addNode("SocialMessages");
                messageNode.setProperty("messageLength", 0);
            }

            if (socialNode.hasNode("socialMessagingId")
                    && socialNode.getNode("socialMessagingId").hasNode(
                            socialMessageId)) {
                socialUserNode = socialNode.getNode("socialMessagingId")
                        .getNode(socialMessageId);
            }
            userNode = messageNode.addNode(socialMessageId
                    + (messageNode.getProperty("messageLength").getLong() + 1));
            messageNode.setProperty("messageLength",
                    messageNode.getProperty("messageLength").getLong() + 1);
            // userNode = messageNode.addNode(socialMessageId + (lengthNode +
            // 1));
            // changed above 4 lines for increment on the basis of static number
            
         /*   String tag[]=tags.split(",");
            
        
            for(int i=0;i<tag.length;i++)
            {
              	message=message.replace("#"+tag[i],"<a  style='color: #00FFFF;' href='"+contextPath+"/servlet/social/messages.messageResult?param="+tag[ i ]+"&searchType=hash' >"+"#"+tag[i]+"</a>");	
           
            	
            }*/
            
            
            
            
            userNode.setProperty("message", message);
            userNode.setProperty("socialMessagingId", socialMessageId);
            if (twitterId != null) {
                userNode.setProperty("tweetId", twitterId);
            }
            userNode.setProperty("createdOn", dateFormat.format(cal.getTime()));
            
          
            if( userNode.hasNode("Tags")){
                tagNode =  userNode.getNode("Tags");  
            }else{
                tagNode =  userNode.addNode("Tags");
            }
            tagNode.setProperty("tags", tags.split(","));
        
      
            
            session.save();
            if (tweetFlag && socialUserNode.hasProperty("twitterToken")) {
                twitter_service
                        .sendMessage(socialUserNode.getProperty("twitterToken")
                                .getString(),
                                socialUserNode.getProperty("twitterSecret")
                                        .getString(), message);
            }
            favNforward(userNode.getPath(), socialMessageId, "Forward",
                    "forwardId", false);
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#renderMessages(java.lang.String)
     */
    public ArrayList<Node> renderMessages(String socialMessagingId) {
        ArrayList<Node> messageNode = new ArrayList<Node>();
        ArrayList<String> followingNode = new ArrayList<String>();
        Session session;
        Node socialNode = null;
        NodeIterator following;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            socialNode = session.getRootNode().getNode("content")
                    .getNode("socialMessenger");
            if (socialNode.hasNode("socialMessagingId")
                    && socialNode.getNode("socialMessagingId").hasNode(
                            socialMessagingId)
                    && socialNode.getNode("socialMessagingId")
                            .getNode(socialMessagingId).hasNode("Following")
                    && socialNode.getNode("socialMessagingId")
                            .getNode(socialMessagingId).getNode("Following")
                            .hasNodes()) {

                following = socialNode.getNode("socialMessagingId")
                        .getNode(socialMessagingId).getNode("Following")
                        .getNodes();

                while (following.hasNext()) {
                    followingNode.add(following.nextNode().getName());

                }

            }

            if (socialNode.hasNode("SocialMessages")
                    && socialNode.getNode("SocialMessages").hasNodes()) {
                NodeIterator message;
                message = socialNode.getNode("SocialMessages").getNodes();
                while (message.hasNext()) {
                    Node node = message.nextNode();
                    if (followingNode.contains(node.getProperty(
                            "socialMessagingId").getString())) {
                        messageNode.add(node);
                    }
                }
            }

        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }
        return messageNode;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#postSocialReply(java.lang.String
     * , java.lang.String, java.lang.String)
     */
    public void postSocialReply(String socialMessagingId, String message,
            String messageNode) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Session session;
        Node socialNode, replyNode, node;
        int replyLength = 0;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            socialNode = session.getNode(messageNode);
            if (socialNode.hasNode("Reply")) {
                replyNode = socialNode.getNode("Reply");
            } else {
                replyNode = socialNode.addNode("Reply");
            }
            if (replyNode.hasNodes()) {
                replyLength = (int) replyNode.getNodes().getSize();
            }
            node = replyNode.addNode(replyLength + "");
            node.setProperty("reply", message);
            node.setProperty("replyById", socialMessagingId);
            node.setProperty("replyCreatedOn",
                    dateFormat.format(cal.getTime()));

            session.save();
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }
    }

    /* Method is for Favourite and forward message */
    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#favNforward(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public void favNforward(String messageId, String socialMessagingId,
            String nodeType, String propertyType, boolean saveMessage) {
        Session session;
        Node socialNode, favouriteNode, messageNode, socialMessage,
                messageFavNode, userFavNode = null;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            socialNode = session.getNode("/content/socialMessenger/"
                    + "socialMessagingId/" + socialMessagingId);
            socialMessage = session.getNode(messageId);
            if (saveMessage) {
                if (socialMessage.hasNode(nodeType)) {
                    messageFavNode = socialMessage.getNode(nodeType);
                } else {
                    messageFavNode = socialMessage.addNode(nodeType);
                }

                if (messageFavNode.hasNode(socialMessagingId)) {
                    userFavNode = messageFavNode.getNode(socialMessagingId);
                    userFavNode.remove();
                } else {
                    userFavNode = messageFavNode.addNode(socialMessagingId);
                    userFavNode.setProperty("userMessagingId",
                            socialMessagingId);
                }

            }
            if (socialNode.hasNode(nodeType)) {
                favouriteNode = socialNode.getNode(nodeType);
            } else {
                favouriteNode = socialNode.addNode(nodeType);
            }
            if (favouriteNode.hasNode(socialMessage.getName())) {
                messageNode = favouriteNode.getNode(socialMessage.getName());
                messageNode.remove();
            } else {
                messageNode = favouriteNode.addNode(socialMessage.getName());
                messageNode.setProperty(propertyType, messageId);
            }

            session.save();
        } catch (PathNotFoundException e) {

            e.printStackTrace();
        } catch (RepositoryException e) {

            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#setTwitterCred(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public int setTwitterCred(String userId, String token, String secret,
            String twitterScreenName, String twitterId) {
        Session session;
        Node userNode, socialNode, networkNode, twitterNode;
        String screenName = "";

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content/user")
                    .getNode(userId);
            if (userNode.hasNode("NetworkProvider")) {
                networkNode = userNode.getNode("NetworkProvider");
            } else {
                networkNode = userNode.addNode("NetworkProvider");
            }

            if (networkNode.hasNode("Twitter")) {
                twitterNode = networkNode.getNode("Twitter");
            } else {
                twitterNode = networkNode.addNode("Twitter");
            }

            twitterNode.setProperty("token", token);
            twitterNode.setProperty("secret", secret);
            twitterNode.setProperty("screenName", twitterScreenName);
            twitterNode.setProperty("socialId", twitterId);

            if (userNode.hasProperty("socialMessagingId")) {
                screenName = userNode.getProperty("socialMessagingId")
                        .getString();

                if (session.getRootNode().getNode("content")
                        .getNode("socialMessenger")
                        .getNode("socialMessagingId").hasNode(screenName)) {
                    socialNode = session.getRootNode().getNode("content")
                            .getNode("socialMessenger")
                            .getNode("socialMessagingId").getNode(screenName);
                    socialNode.setProperty("twitterToken", token);
                    socialNode.setProperty("twitterSecret", secret);
                    socialNode.setProperty("twitterScreenName",
                            twitterScreenName);
                    socialNode.setProperty("twitterId", twitterId);

                }

            }

            session.save();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#getTwitterCred(java.lang.String)
     */
    public String[] getTwitterCred(String userId,String type) {
        String socialId = "";
        String[] value = {"", "" };
        Node userNode, networkNode, typeNode = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content/user")
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
            if (typeNode.hasProperty("token") && typeNode.hasProperty("secret")) {
            	socialId = typeNode.getProperty("socialId")
                        .getString();
                    value[0] = typeNode.getProperty("token").getString();
                    value[1] = typeNode.getProperty("secret").getString();
                }else {
                	value[0] = "error";
                }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.social.service.SocialMessagesService#setTwitterTweet(java.lang.String
     * , java.lang.String, java.lang.String)
     */
    public void setTwitterTweet(String tweets, String tweetId, String userId) {

        Node userNode;
        Session session;
        String socialMessageId = "";
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content").getNode("user")
                    .getNode(userId);
            if (userNode.hasProperty("socialMessagingId")) {
                socialMessageId = userNode.getProperty("socialMessagingId")
                        .getString();
            }
            String[] tweetVal = tweets.split("!@#%&,");
            String[] tweetIdVal = tweetId.split(",");
            for (int i = 0; i < tweetVal.length; i++) {
                addSocialMessage(socialMessageId, tweetVal[i],null, false, tweetIdVal[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.social.service.SocialMessagesService#getlimitedMessage(int, int,
     * java.lang.String, java.lang.String)
     */
    public String getlimitedMessage(int startlimit, int limit,
            String socialMessagingId, String app) {
        ArrayList<Node> messages = renderMessages(socialMessagingId);

        JSONObject json = new JSONObject();
        JSONArray jArr = new JSONArray();
        Node usersocialNode, userNode, socialMesgId, replyNode,
                replySocialNode, userReply;
        NodeIterator replyNodes;
        Session session = null;
        StringBuilder html = new StringBuilder("");
        try {
            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            socialMesgId = session
                    .getNode("/content/socialMessenger/socialMessagingId/"
                            + socialMessagingId);
            for (int i = (messages.size() - (startlimit + 1)); (i >= ((messages
                    .size() - startlimit) - 20) && i >= 0); i--) {
                json = new JSONObject();
                json.accumulate("key", i);
                html = new StringBuilder("");
                usersocialNode = session
                        .getNode("/content/socialMessenger/socialMessagingId/"
                                + messages.get(i)
                                        .getProperty("socialMessagingId")
                                        .getString());
                userNode = session.getNode(usersocialNode.getProperty("userId")
                        .getString());
                html.append("<li><ol class='icon-holder'>");
                if (messages.get(i).hasNode("Forward")
                        && messages.get(i).getNode("Forward")
                                .hasNode(socialMessagingId)) {
                    html.append("<li class='icon-forward'></li>");
                } else {
                    html.append("<li class='icon-forward'"
                            + " style='display:none;'></li>");
                }
                if (messages.get(i).hasNode("Favourite")
                        && messages.get(i).getNode("Favourite")
                                .hasNode(socialMessagingId)) {
                    html.append("<li class='icon-favorite'></li>");
                } else {
                    html.append("<li class='icon-favorite'"
                            + " style='display:none;'></li>");
                }

                html.append("</ol><div class='profile-picture'>");
                if (userNode.hasProperty("profileImage")) {
                    html.append("<img src='"
                            + userNode.getProperty("profileImage").getString()
                            + "'   />");
                } else {
                    html.append("<img src='" + app
                            + "/apps/user/css/images/photo.gif'  />");
                }
                html.append("</div>");
                html.append("<div class='profile-message'>");
                html.append("<h2><a href='javascript:void(0);'>");
                html.append(userNode.getProperty("name").getString() + " ");
                if (userNode.hasProperty("lastName")) {
                    html.append(userNode.getProperty("lastName").getString());
                }

                html.append("<span> @" + usersocialNode.getName()
                        + "</span></a>");
                html.append("</h2>");
                html.append("<h3>"
                        + messages.get(i).getProperty("message").getString()
                        + "</h3>");
                html.append("<div class='links'><a href='javascript:void(0);'"
                        + " class='expand'><span></span>expand</a>"
                        + "<a href='javascript:void(0);' class='reply'>"
                        + "<span></span>reply</a>"
                        + "<a href='javascript:void(0);' onclick=\"forward('"
                        + app
                        + "','"
                        + messages.get(i).getPath()
                        + "','"
                        + socialMessagingId
                        + "');\" class='forward'><span></span>forward</a>"
                        + "<a href='javascript:void(0);' onclick=\"forward('"
                        + app
                        + "','"
                        + messages.get(i).getPath()
                        + "','"
                        + socialMessagingId
                        + "');\" class='favorite' ><span></span>favorite</a>"
                        + "<a href='javascript:void(0);' class='email' "
                        + "onclick=\"sendMail(this,'"
                        + socialMesgId.getProperty("userId")
                        + "','"
                        + userNode.getName()
                        + "');\"><span></span>email</a>"
                        + "</div></div>");
                html.append("<ul class='message-reply'>");
                html.append("<li><div class='message-reply-text'><textarea "
                        + "name='textfield4' class='textCount' maxlength='140'"
                        + " id='postReply" + i + "' >@"
                        + usersocialNode.getName() + "</textarea>");
                html.append("<div class='message-reply-buttons'><div "
                        + "class='btn-submit-message'><input type='button' "
                        + "value='Submit' onclick=\"postReply('"
                        + app
                        + "','"
                        + socialMessagingId
                        + "','"
                        + messages.get(i).getPath()
                        + "','"
                        + i
                        + "')\" class='btn btn-info btn-small' />"
                        + "</div></div></div><div class='message-reply-text'>"
                        + "</div></li>");
                if (messages.get(i).hasNode("Reply")
                        && messages.get(i).getNode("Reply").hasNodes()) {
                    replyNodes = messages.get(i).getNode("Reply").getNodes();
                    while (replyNodes.hasNext()) {
                        replyNode = replyNodes.nextNode();
                        replySocialNode = usersocialNode.getParent().getNode(
                                replyNode.getProperty("replyById").getString());
                        userReply = session.getNode(replySocialNode
                                .getProperty("userId").getString());
                        html.append("<li><div class='profile-picture'>");
                        if (userReply.hasProperty("profileImage")) {
                            html.append("<img src='"
                                    + userReply.getProperty("profileImage")
                                            .getString() + "'  />");
                        } else {
                            html.append("<img src='" + app
                                    + "/apps/user/css/images/photo.gif' />");
                        }
                        html.append("</div><div class='profile-message'>"
                                + "<h2><a href='javascript:void(0);'>"
                                + "<span>"
                                + userReply.getProperty("name").getString()
                                + " ");
                        if (userReply.hasProperty("lastName")) {
                            html.append(userReply.getProperty("lastName")
                                    .getString());
                        }
                        html.append(" @" + replySocialNode.getName()
                                + "</span></a></h2>" + "<h3>"
                                + replyNode.getProperty("reply").getString()
                                + "</h3></div></li>");
                    }
                }
                html.append("</ul></li>");
                json.accumulate("value", html.toString());
                jArr.put(json);
                /*
                 * '<%=messages.get(i)%>', '<%=currentNode.getName()%>')"
                 * class="forward"><span></span>forward</a> <a
                 * href="javascript:void(0);"
                 * onclick="favourite('<%=request.getContextPath()%>',
                 * '<%=messages.get(i)%>', '<%=currentNode.getName()%>')"
                 * class="favorite"><span></span>favorite</a> <a
                 * href="javascript:void(0);" class="email" onclick=
                 * "sendMail(this,'<%=currentNode.userId%>',
                 * '<%=userNode.getName()%>');"
                 * ><span></span>email</a> </div> </div>
                 */

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jArr.toString();
    }

	public ArrayList<String> searchTag(String parameter) {
		 

		        Session session = null;
		        ArrayList<String> tagList = new ArrayList<String>();
		        try {
		            session = repos.login(new SimpleCredentials("admin", "admin"
		                    .toCharArray()));
		            String jcrQuery = "select excerpt(.) from nt:unstructured where contains(*, '"
                    + parameter
                    + "*')  and jcr:path LIKE '/content/socialMessenger/SocialMessages/%/Tags'";
		            Workspace workspace = session.getWorkspace();
		            Query query;
		            query = workspace.getQueryManager()
		                    .createQuery(jcrQuery, Query.SQL);
		            QueryResult qr = query.execute();
		            RowIterator rit = qr.getRows();
		            while (rit.hasNext()) {
		                Row row = rit.nextRow();
		                Value[] values = row.getValues();
		                Document doc = Jsoup.parse(values[0].getString());
		                Elements strongTag = doc.select("strong");
		                for (int i = 0; i < strongTag.size(); i++) {
		                    if (!tagList.contains(strongTag.get(i).text())) {
		                        tagList.add(strongTag.get(i).text());
		                    }
		                }

		            }
		            System.out.println(tagList);

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return tagList;
		
	}


	public  ArrayList<Node> messageResults(String parameter, String searchType) {
		  Session session = null;
	        ArrayList<Node> list = new ArrayList<Node>();
	  
	        try {
	            session = repos.login(new SimpleCredentials("admin", "admin"
	                    .toCharArray()));
	            String jcrQuery="";
	            Query query;
	            Workspace workspace = session.getWorkspace();
	           
	            if(searchType.equals("hash"))
	            {
	           jcrQuery = "select * from nt:unstructured where tags='"
	                    + parameter
	                    + "'  and jcr:path LIKE '/content/socialMessenger/SocialMessages/%/Tags'";
	           query = workspace.getQueryManager()
	                    .createQuery(jcrQuery, Query.SQL);
	            }
	            else
	            {
	            	 jcrQuery =	"select * from [nt:unstructured] where ISDESCENDANTNODE('/content/socialMessenger/SocialMessages/')"
	                        + "and (contains('message','*" + parameter + "*'))";
	            	  query = workspace.getQueryManager().createQuery(jcrQuery,
	  	                    Query.JCR_SQL2);
	            }
	           
	          
	            QueryResult qr = query.execute();

	            NodeIterator iterator = qr.getNodes();
	          
	             

	            while (iterator.hasNext()) {
	                Node node = iterator.nextNode();
	                if(searchType.equals("hash"))
		            {
	                list.add(node.getParent());
		            }
	                else
	                {
	                	 list.add(node);
	                }
	          
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return list;
	}

	public String randomUser(String userId,String contextPath) {
		Node userNode, socialNode;
		Session session = null;
		ArrayList<Node> list = new ArrayList<Node>();

		ArrayList<String> followingList = new ArrayList<String>();
		JSONObject json = new JSONObject();
		JSONArray jArr = new JSONArray();
		String socialId = "";
		StringBuilder htmlString = new StringBuilder("");
		try {
			
			session = repos.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			Node contentNode = session.getNode("/content");
			if (contentNode.hasNode("socialMessenger")
					&& contentNode.getNode("socialMessenger").hasNodes()
					&& contentNode.getNode("socialMessenger").hasNode(
							"socialMessagingId")
					&& contentNode.getNode("socialMessenger")
							.getNode("socialMessagingId").hasNodes()) {

				if (contentNode.hasNode("user")
						&& contentNode.getNode("user").hasNodes()
						&& contentNode.getNode("user").getNode(userId)
								.hasProperty("socialMessagingId")) {
					socialId = contentNode.getNode("user").getNode(userId)
							.getProperty("socialMessagingId").getString();
				}

				socialNode = session
						.getNode("/content/socialMessenger/socialMessagingId/"
								+ socialId);

				if (socialNode.hasNode("Following")
						&& socialNode.getNode("Following").hasNodes()) {

					NodeIterator followingNodes = socialNode.getNode(
							"Following").getNodes();
					while (followingNodes.hasNext()) {
						Node followNode = followingNodes.nextNode();
						followingList.add(followNode.getName().toString());
					}

				}
				followingList.add(socialId);
                     
				NodeIterator iterator = socialNode.getParent().getNodes();
				while (iterator.hasNext()) {
					Node node = iterator.nextNode();
					if (!followingList.contains(node.getName().toString())) {
						list.add(node);
					}

				}
				
				  
				  
				for (int i = 0; i < list.size(); i++)
				{
					json = new JSONObject(); 
					htmlString = new StringBuilder("");
				  htmlString.append("<li>");
				  htmlString.append("<div class='profile-picture'>");
				  	Node profileNode=list.get(i).getProperty("userId").getNode();
				  	if(profileNode.hasProperty("profileImage"))
				  			{
				  		 htmlString.append("<img src='"+profileNode.getProperty("profileImage")+"' border='0' /></div>");
				  			}
				  	else
				  	{
				  		 htmlString.append("<img src='/sling/apps/socialMessages/images/profile_pic.gif' border='0' /></div>");	
				  	}
				  
				  
				  
				 htmlString.append("<div class='profile-message follow-message'>");
				  htmlString.append("<h2><a href='#'>"+list.get(i).getProperty("firstName").getString()+"</a><span class='user-name'>@"+list.get(i).getName()+"</span></h2><span class='follow-mess-close' onclick=\"removeUser(this);\"></span>");
				     htmlString.append("<a class='btn btn-active btn-follow' href='#' onclick=\"followFriend('"
                                    + socialId
                                    + "','"+list.get(i).getName()+"',this);\">Follow</a>");
				    htmlString.append("</div>");
				   htmlString.append("</li>");
				   json.accumulate("user", htmlString.toString());
				   jArr.put(json);
				  
				}
		
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return jArr.toString();

	}
	
	

}
