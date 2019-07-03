package org.profile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.ProfileService;
import org.profile.service.RecommendationService;

/**
 * The Class <code>RecommendationServiceImpl</code> contains all the method
 * implementation related to Recommendation. Recommendation is done for
 * education, experience and skill for respective user. Recommendation request
 * is sent to respective user where invited user can recommend by writing any
 * message.
 * 
 * @author pranav.arya
 * 
 */
@Component(configurationFactory = true)
@Service(RecommendationService.class)
@Properties({ @Property(name = "recommend", value = "recommendService") })
public class RecommendationServiceImpl implements RecommendationService {

    /**
     * Contains Object of SlingRepository
     */
    @Reference
    private SlingRepository repos;

    /**
     * Contains Object of ProfileService
     */
    @Reference
    private ProfileService service_profile;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.profile.service.RecommendationService#sendRecommendRquest(java.lang
     * .String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public void sendRecommendRquest(String recommendTo, String userId,
            String requestType, String recommendType, String message,
            String pending, String flag, String relatonship,
            String colleagueTitle, String description)

    {

        Node node, recommendNode, userNode, requestTypeNode, flagNode = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));

            node = session.getRootNode().getNode("content").getNode("user")
                    .getNode(recommendTo);

            if (node.hasNode("Recommendation")) {
                recommendNode = node.getNode("Recommendation");
            } else {
                recommendNode = node.addNode("Recommendation");
            }
            if (recommendNode.hasNode(flag)) {
                flagNode = recommendNode.getNode(flag);
            } else {
                flagNode = recommendNode.addNode(flag);
            }

            if (flagNode.hasNode(userId)) {
                userNode = flagNode.getNode(userId);
            } else {
                userNode = flagNode.addNode(userId);
            }

            if (userNode.hasNode(requestType)) {
                requestTypeNode = userNode.getNode(requestType);
            } else {
                requestTypeNode = userNode.addNode(requestType);
            }

            requestTypeNode.setProperty("recommendType", recommendType);
            requestTypeNode.setProperty("userId", userId);
            requestTypeNode.setProperty("requestType", requestType);
            requestTypeNode.setProperty("message", message);
            requestTypeNode.setProperty("pending", pending);
            requestTypeNode.setProperty("recommendFlag", flag);
            requestTypeNode.setProperty("relatonship", relatonship);
            requestTypeNode.setProperty("colleagueTitle", colleagueTitle);
            requestTypeNode.setProperty("recommendDesc", description);
            session.save();

            if (pending.equals("Accepted") && flag.equals("Sender")) {
                ArrayList<Map> activityStream = new ArrayList<Map>();
                Map<String, String> map = new HashMap<String, String>();
                activityStream = new ArrayList<Map>();
                map.put("key", "message");
                map.put("value", "is endorsed for: ");
                activityStream.add(map);
                map = new HashMap<String, String>();
                map.put("key", "recommendType");
                map.put("value", requestType);
                activityStream.add(map);
                map = new HashMap<String, String>();
                map.put("key", "requested");
                map.put("value", "by ");
                activityStream.add(map);
                map = new HashMap<String, String>();
                map.put("key", "recommend");
                map.put("value", userId);
                activityStream.add(map);
                service_profile.activityStream(recommendTo, activityStream,
                        "Recommendation", "");

            }

        } catch (PathNotFoundException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }

}
