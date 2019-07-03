package org.profile.service;

// TODO: Auto-generated Javadoc
/**
 * The Interface <code>RecommendationService</code> contains all the method
 * declaration related to Recommendation. Recommendation is done for education,
 * experience and skill for respective user. Recommendation request is sent to
 * respective user where invited user can recommend by writing any message.
 * 
 * @author pranav.arya
 */
public interface RecommendationService {

    /**
     * Send and save recommend request to invited users. Here In Recommendation
     * node, Sender or Receiver is saved which helps in determine who sends the
     * request or to whome the request has been sent. Variable "flag"
     * helps in determine whether the request is from sender or for receiver.
     * 
     * @param recommendTo
     *            Contains the userId of invited user
     * @param userId
     *            Contains the userId loggedIn user
     * @param requestType
     *            Contains the Title of Recommendation(eg. Student At ABC)
     * @param recommendType
     *            Contains the full Path of recommend Node i.e. for which recommendation is done
     * @param message
     *            Contains the message for recommend request
     * @param pending
     *            Contains the status flag i.e. Pending or accepted
     * @param flag
     *            Contains the flag i.e. sender or receiver
     * @param relatonship
     *            Contains the Relationship between sender and receiver for Recommendation Title
     * @param colleagueTitle
     *            Contains the receiver's title or Position for Recommendation Title
     * @param description
     *            Contains the description of recommendation
     */
    public void sendRecommendRquest(String recommendTo, String userId,
            String requestType, String recommendType, String message,
            String pending, String flag, String relatonship,
            String colleagueTitle, String description);

}
