package org.profile.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.SocialNetworkService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

// TODO: Auto-generated Javadoc
/**
 * The Class <code>SocialNetworkServiceImpl</code> contains all method
 * implementations related to Social networking data extraction. Authentication,
 * Authorization and data extraction from different Social Networking sites are
 * taken place in this section. Social networking sites includes : Linkedin,
 * Twitter, Facebook.
 */
@Component(configurationFactory = true)
@Service(SocialNetworkService.class)
@Properties({ @Property(name = "socialService", value = "socialNetwork") })
public class SocialNetworkServiceImpl implements SocialNetworkService {

    /** Object of SlingRepository. */
    @Reference
    private SlingRepository repos;

    /** Object of ResourceBundle. */
    private static ResourceBundle bundle = ResourceBundle.getBundle("server");

    /** Object of OAuthService. */
    private OAuthService service = null;

    /** Declaration of MAP. */
    private Map<String, String> map = null;

    
    /*
     * (non-Javadoc)
     *
     * @see org.profile.service.SocialNetworkService#getLinkedinToken()
     */
    public Map<String, String> getLinkedinToken() {
        map = new HashMap<String, String>();
        try {
            service = new ServiceBuilder()
                    .provider(LinkedInApi.class)
                              .apiKey("75m4u19jz5vzto")
            .apiSecret("HkImphxpb1tvGUam")
                    .callback(
                        bundle.getString("sling.serverSpec")
                                + "/servlet/social/service.setLinkedinToken")
                    .build();
            Token requestToken = service.getRequestToken();
            map.put("token", requestToken.getToken());
            map.put("secret", requestToken.getSecret());
            String url = service.getAuthorizationUrl(requestToken);
            map.put("requestURL", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.profile.service.SocialNetworkService#authorizeLinkedinToken(java.
     * lang.String, java.lang.String, java.lang.String)
     */
    public Map<String, String> authorizeLinkedinToken(String token,
            String secret, String verifier) {
        service = new ServiceBuilder()
                .provider(LinkedInApi.class)
                .apiKey(bundle.getString("linkedin.key"))
                .apiSecret(bundle.getString("linkedin.secret"))
                .callback(
                        bundle.getString("sling.serverSpec")
                                + "/servlet/social/service.setLinkedinToken")
                .build();
        map = new HashMap<String, String>();
        Token requestToken = new Token(token, secret);
        Verifier verifierIN = new Verifier(verifier);
        Token accessToken = service.getAccessToken(requestToken, verifierIN);
        map.put("token", accessToken.getToken());
        map.put("secret", accessToken.getSecret());
        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.profile.service.SocialNetworkService#getLinkedinData(java.lang.String
     * )
     */
    public Map<String, String> getLinkedinData(String userId) {
        map = new HashMap<String, String>();
        String response = "";
        OAuthService localService = new ServiceBuilder()
                .provider(LinkedInApi.class)
                .apiKey(bundle.getString("linkedin.key"))
                .apiSecret(bundle.getString("linkedin.secret"))
                .callback(
                        bundle.getString("sling.serverSpec")
                                + "/servlet/social/service.setLinkedinToken")
                .build();
        map = getRequestToken(userId, "Linkedin");
        if (map == null) {
            return null;
        }
        response = getOutput(
            map.get("token"),
            map.get("secret"),
            "http://api.linkedin.com/v1/people/~:(id,connections:(id))"
            + "?format=json",
            localService);

        try {
            map = new HashMap<String, String>();
            JSONObject json = new JSONObject(response);
            map.put("contact",
                    json.getJSONObject("connections").getString("_total"));
            map.put("linkedinId", json.getString("id"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.profile.service.SocialNetworkService#getFacebookToken()
     */
    public Map<String, String> getFacebookToken() {
        map = new HashMap<String, String>();
        try {
            service = new ServiceBuilder()
                    .provider(FacebookApi.class)
                    .apiKey("1627299000856281")
                    .apiSecret("9e51b9e54551a60b8306c943820b7b3f")
                    .callback(
                        bundle.getString("sling.serverSpec")
                                + "/servlet/social/service.setFacebookToken")
                    .build();
            Token requestToken = null;
            String url = service.getAuthorizationUrl(requestToken);
            map.put("requestURL", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.profile.service.SocialNetworkService#authorizeFacebookToken(java.
     * lang.String)
     */
    public Map<String, String> authorizeFacebookToken(String verifier) {
        service = new ServiceBuilder()
                .provider(FacebookApi.class)
                .apiKey(bundle.getString("facebook.key"))
                .apiSecret(bundle.getString("facebook.secret"))
                .callback(
                        bundle.getString("sling.serverSpec")
                                + "/servlet/social/service.setFacebookToken")
                .build();
        map = new HashMap<String, String>();
        Token requestToken = null;
        Verifier verifierFB = new Verifier(verifier);
        Token accessToken = service.getAccessToken(requestToken, verifierFB);
        map.put("token", accessToken.getToken());
        map.put("secret", accessToken.getSecret());
        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.profile.service.SocialNetworkService#getFacebookData(java.lang.String
     * , java.lang.String)
     */
    public Map<String, String> getFacebookData(String userId, String type) {
        map = new HashMap<String, String>();
        String response = "";
        OAuthService localService = new ServiceBuilder()
                .provider(FacebookApi.class)
                .apiKey(bundle.getString("facebook.key"))
                .apiSecret(bundle.getString("facebook.secret"))
                .callback(
                        bundle.getString("sling.serverSpec")
                                + "/servlet/social/service.setFacebookToken")
                .build();
        String socialId = "";
        map = getRequestToken(userId, "Facebook");
        if (map == null) {
            return null;
        }
        socialId = map.get("socialId");
        JSONObject json;
        if (type.equals("friend")) {
            response = getOutput(map.get("token"), map.get("secret"),
                    "https://graph.facebook.com/me/friends/", localService);
            try {
                map = new HashMap<String, String>();
                json = new JSONObject(response);
                map.put("contact", json.getJSONArray("data").length() + "");
                map.put("facebookId", socialId);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (type.equals("profile")) {
            response = getOutput(map.get("token"), map.get("secret"),
                    "https://graph.facebook.com/me?fields=id", localService);

            try {
                json = new JSONObject(response);
                setRequestToken("", "", userId, "Facebook", "setId",
                        json.getString("id"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.profile.service.SocialNetworkService#getTwitterData(java.lang.String)
     */
    public Map<String, String> getTwitterData(String userId) {
        map = new HashMap<String, String>();
        String response = "";
        OAuthService localService = new ServiceBuilder()
                .provider(TwitterApi.class)
                .apiKey(bundle.getString("twitter.key"))
                .apiSecret(bundle.getString("twitter.secret"))
                .callback(
                        bundle.getString("sling.serverSpec")
                                + "/servlet/social/service.setTwitterToken")
                .build();
        String socialId = "";
        map = getRequestToken(userId, "Twitter");
        if (map == null) {
            return null;
        }
        socialId = map.get("socialId");
        response = getOutput(map.get("token"), map.get("secret"),
                "https://api.twitter.com/1.1/users/lookup.json?user_id="
                        + socialId, localService);
        map = new HashMap<String, String>();
        try {
            JSONArray jsonArr = new JSONArray(response);
            map.put("follower_count",
                    jsonArr.getJSONObject(0).get("followers_count") + "");
            map.put("following_count",
                    jsonArr.getJSONObject(0).get("friends_count") + "");
            map.put("screen_name", jsonArr.getJSONObject(0).get("screen_name")
                    + "");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.profile.service.SocialNetworkService#getOutput(java.lang.String,
     * java.lang.String, java.lang.String, org.scribe.oauth.OAuthService)
     */
    public String getOutput(String token, String secret, String url,
            OAuthService localService) {
        String output = "";
        try {
            Token accessToken = new Token(token, secret);
            OAuthRequest request = new OAuthRequest(Verb.GET, url);
            localService.signRequest(accessToken, request);
            Response response = request.send();
            output = response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.profile.service.SocialNetworkService#setRequestToken(java.lang.String
     * , java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
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
     * org.profile.service.SocialNetworkService#getRequestToken(java.lang.String
     * , java.lang.String)
     */
    public Map<String, String> getRequestToken(String userId, String type) {
        map = new HashMap<String, String>();
        Node userNode, networkNode, typeNode = null;
        Session session;
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            userNode = session.getRootNode().getNode("content/user/" + userId);
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
            if (typeNode.hasProperty("token") && typeNode.hasProperty("secret"))
            {
                map.put("token", typeNode.getProperty("token").getString());
                map.put("secret", typeNode.getProperty("secret").getString());
                map.put("socialId", typeNode.getProperty("socialId")
                        .getString());
            } else {
                return null;
            }
        } catch (PathNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (RepositoryException e) {

            e.printStackTrace();
            return null;
        }

        return map;
    }
    public String callService(String urlStr, String[] paramName,
            String[] paramValue) {
    	
    	
        StringBuilder sb = new StringBuilder();
        URL url;
        try {
        
            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            // Create the form content
            OutputStream out = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            for (int i = 0; i < paramName.length; i++) {
                writer.write(paramName[i]);
                writer.write("=");
                writer.write(URLEncoder.encode(paramValue[i], "UTF-8"));
                writer.write("&");
            }
            writer.close();
            out.close();
            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }
            // Buffer the result into a string
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            conn.disconnect();
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (Exception e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
        	return e.getMessage();

        }
        return sb.toString();
    }
    public String MailToFriend(SlingHttpServletRequest request){
//    	String from=request.getParameter("from");
//    	String message=request.getParameter("message");
//    	String subject=request.getParameter("subject");
//       	String to=request.getParameter("to");
//    	  String url = bundle.getString("sendMail.address");	
//    	  String[] paramName = { "emailto[]", "emailfrom[]",
//                  "emailsub[]", "emailmsg[]" };
//    	  String[] paramValue = {to,
//    			  from,
//                  subject, message };
    	///+"%%_%%"+callService(url, paramName, paramValue)
    	if(request.getParameter("changestate")!=null){
    	return  changeState(request);
    	}else{
    		return  SaveToReceiverNode(request);	
    	}
    }
    
    public String SaveToReceiverNode(SlingHttpServletRequest request){
    	
    	 Node userNode, networkNode, typeNode = null;
         Session session;
         String result="";
         try {

             session = repos.login(new SimpleCredentials("admin", "admin"
                     .toCharArray()));
             Node usersender=session.getRootNode().getNode("content").getNode("user").getNode(request.getParameter("from").replace("@", "_"));
             Node userreceiver= session.getRootNode().getNode("content").getNode("user").getNode(request.getParameter("to").replace("@", "_"));
             if(!userreceiver.hasNode("messages")){
            	 userreceiver=userreceiver.addNode("messages");
            	 
             }else{
            	 userreceiver= userreceiver.getNode("messages");
             }
             
             if(!usersender.hasNode("messages")){
            	 usersender=usersender.addNode("messages");
            	
             }else{
            	 usersender= usersender.getNode("messages");
             }
            

             if(!usersender.hasNode(request.getParameter("to").replace("@", "_"))){
            	 usersender=usersender.addNode(request.getParameter("to").replace("@", "_"));
            	 usersender.setProperty("scount",Long.valueOf("0"));
            	 usersender.setProperty("isread", "Yes");
            	 if(request.getParameter("subject")!=null){
            		 usersender.setProperty("subject", request.getParameter("subject"));	 
            	 }
            	 
            	 usersender.setProperty("date",  new Date().toString());
             }else{
            	 usersender= usersender.getNode(request.getParameter("to").replace("@", "_"));
            	 usersender.setProperty("isread", "Yes");
            	 if(request.getParameter("subject")!=null){
            		 usersender.setProperty("subject", request.getParameter("subject"));	 
            	 }
            	 usersender.setProperty("date",  new Date().toString()); 
             }
             
             if(!userreceiver.hasNode(request.getParameter("from").replace("@", "_"))){
            	 userreceiver=userreceiver.addNode(request.getParameter("from").replace("@", "_"));
            	 userreceiver.setProperty("scount",Long.valueOf("0"));
            	 userreceiver.setProperty("isread", "No");
            	  if(request.getParameter("subject")!=null){
            		 userreceiver.setProperty("subject", request.getParameter("subject"));	 
            	 }
            	 
            	 userreceiver.setProperty("date",  new Date().toString());
             }else{
            	 userreceiver= userreceiver.getNode(request.getParameter("from").replace("@", "_"));
           	  if(request.getParameter("subject")!=null){
         		 userreceiver.setProperty("subject", request.getParameter("subject"));	 
         	 }
         	 userreceiver.setProperty("date",  new Date().toString());
            	 userreceiver.setProperty("isread", "No");
             
             }
             
           
            	   String k= usersender.getProperty("scount").getString(); 
            	 
            	   long l=Long.valueOf(k);//usersender.getProperty("scount").getLong();            
                   usersender.setProperty("scount",l+1);
                   usersender= usersender.addNode(String.valueOf(l+1));
                   String k1=userreceiver.getProperty("scount").getString();
                   long l1=Long.valueOf(k1);//userreceiver.getProperty("scount").getLong();            
                   userreceiver.setProperty("scount",l1+1);
                   userreceiver= userreceiver.addNode(String.valueOf(l1+1));
                   usersender.setProperty("from", request.getParameter("from"));
                   usersender.setProperty("to", request.getParameter("to"));
                  
                   usersender.setProperty("isread", "Yes");
                   usersender.setProperty("subject", request.getParameter("subject"));
                   usersender.setProperty("message", request.getParameter("message"));
                   usersender.setProperty("date", new Date().toString());
                   usersender.setProperty("rcount", Long.valueOf("0"));
                   userreceiver.setProperty("from", request.getParameter("from"));
                   userreceiver.setProperty("to", request.getParameter("to"));
                   userreceiver.setProperty("message", request.getParameter("message"));
                   userreceiver.setProperty("date", new Date().toString());
                   userreceiver.setProperty("subject", request.getParameter("subject"));
                   userreceiver.setProperty("rcount", Long.valueOf("0"));
                   userreceiver.setProperty("isread", "No");
             
             session.save();    
         
         }catch(Exception e){
        	return result+ e.getMessage();
             }
         return "success";
    }
    
    public String geJsonMessage(SlingHttpServletRequest request){
    	
         Session session;
         JSONArray ar=new JSONArray();
         try {

             session = repos.login(new SimpleCredentials("admin", "admin"
                     .toCharArray()));
             Node messageNode=session.getRootNode().getNode("content").getNode("user").getNode(request.getParameter("loginuser").replace("@", "_")).getNode("messages").getNode(request.getParameter("messageid"));
            
             if(messageNode.hasNodes()){
            	 	 NodeIterator itr=messageNode.getNodes();
            	 while(itr.hasNext()){
            		 Node d=itr.nextNode();
            		 JSONObject obj=new JSONObject();		 
            		 obj.put("from",session.getRootNode().getNode("content").getNode("user").getNode(d.getProperty("from").getString().replace("@", "_")).getProperty("name").getString());
            		 obj.put("message",d.getProperty("message").getString());
            		 obj.put("date", d.getProperty("date").getString());
            		 obj.put("mailfrom", d.getProperty("from").getString());
            		 obj.put("mailto", d.getProperty("to").getString());
            		 obj.put("subject", d.getProperty("subject").getString());
            		 ar.put(obj);
            	 }
            	 
             }
         }catch(Exception e){
            return e.getMessage();	 
             }
    	
    	return ar.toString();
    }
    public String changeState(SlingHttpServletRequest request){
    	 Session session;
         
         try {

             session = repos.login(new SimpleCredentials("admin", "admin"
                     .toCharArray()));
             String path[]=request.getParameter("messageid").split("/");
             Node f=session.getRootNode().getNode("content").getNode("user").getNode(request.getParameter("loginuser").replace("@", "_")).getNode("messages").getNode(path[0].replace("@", "_"));
             f.setProperty("isread", "Yes");
             if(f.hasNodes()){
            	 NodeIterator itr=f.getNodes();
                 while(itr.hasNext()){
                	 Node g=itr.nextNode();
                	 g.setProperty("isread", "Yes");
                 } 
             }
             
             session.save();
             return "success";
         }catch(Exception e){
        	 return e.getMessage();
         }
	
    }
    
    public String replayPost(SlingHttpServletRequest request){
    	
    	 Session session;
        String result="";
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            Node usersender=session.getRootNode().getNode("content").getNode("user").getNode(request.getParameter("from").replace("@", "_"));
            Node userreceiver= session.getRootNode().getNode("content").getNode("user").getNode(request.getParameter("messageid").replace("@", "_"));
            result=result+"stage1 clear";
            if(!userreceiver.hasNode("messages")){
           	 userreceiver=userreceiver.addNode("messages");
           	 
            }else{
           	 userreceiver= userreceiver.getNode("messages");
            }
            
            if(!usersender.hasNode("messages")){
           	 usersender=usersender.addNode("messages");
           	
            }else{
           	 usersender= usersender.getNode("messages");
            }
            result=result+"stage2 clear";

            if(!usersender.hasNode(request.getParameter("messageid").replace("@", "_"))){
           	 usersender=usersender.addNode(request.getParameter("messageid").replace("@", "_"));
           	 usersender.setProperty("scount",Long.valueOf("0"));
           	 usersender.setProperty("isread", "Yes");
           	 if(request.getParameter("subject")!=null){
           		 usersender.setProperty("subject", "Re:"+request.getParameter("subject"));	 
           	 }
           	 
           	 usersender.setProperty("date",  new Date().toString());
            }else{
           	 usersender= usersender.getNode(request.getParameter("messageid").replace("@", "_"));
           	 usersender.setProperty("isread", "Yes");
           	 if(request.getParameter("subject")!=null){
           		 usersender.setProperty("subject", "Re:"+request.getParameter("subject"));	 
           	 }
           	 usersender.setProperty("date",  new Date().toString()); 
            }
            result=result+"stage3 clear";
            if(!userreceiver.hasNode(request.getParameter("from").replace("@", "_"))){
           	 userreceiver=userreceiver.addNode(request.getParameter("from").replace("@", "_"));
           	 userreceiver.setProperty("scount",Long.valueOf("0"));
           	 userreceiver.setProperty("isread", "No");
           	  if(request.getParameter("subject")!=null){
           		 userreceiver.setProperty("subject", "Re:"+request.getParameter("subject"));	 
           	 }
           	 
           	 userreceiver.setProperty("date",  new Date().toString());
            }else{
           	 userreceiver= userreceiver.getNode(request.getParameter("from").replace("@", "_"));
          	  if(request.getParameter("subject")!=null){
        		 userreceiver.setProperty("subject", "Re:"+request.getParameter("subject"));	 
        	 }
        	 userreceiver.setProperty("date",  new Date().toString());
           	 userreceiver.setProperty("isread", "No");
            
            }
            result=result+"stage4clear";
            if(request.getParameter("messageid")!=null){
           	 result=result+request.getParameter("messageid");
           	 
           	 String l=usersender.getProperty("scount").getString();;
           	 long k=Long.valueOf(l);
           	 usersender.setProperty("scount",k+1);
           	 usersender=usersender.addNode(String.valueOf(k+1));
           	 usersender.setProperty("message",  request.getParameter("message"));
           	 usersender.setProperty("date",  new Date().toString());
           	 usersender.setProperty("from", request.getParameter("from"));
             usersender.setProperty("to", request.getParameter("messageid"));
             usersender.setProperty("isread", "Yes");
             usersender.setProperty("subject", "Re:"+request.getParameter("subject"));
           	 
           	 userreceiver.setProperty("scount",k+1);
           	 userreceiver=userreceiver.addNode(String.valueOf(k+1));
           	 userreceiver.setProperty("message",  request.getParameter("message"));
           	 userreceiver.setProperty("date",  new Date().toString());
           	 userreceiver.setProperty("from",   request.getParameter("from"));
             userreceiver.setProperty("to",  request.getParameter("messageid"));
             userreceiver.setProperty("isread", "No");
             userreceiver.setProperty("subject", "Re:"+request.getParameter("subject"));
            
            }
            session.save();    
        
        }catch(Exception e){
       	return result="errrrooorr"+result+e.getMessage();
            }
    	return result;
    }
}
