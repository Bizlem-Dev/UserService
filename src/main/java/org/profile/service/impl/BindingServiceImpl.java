package org.profile.service.impl;

import java.util.ArrayList;
import javax.script.Bindings;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.scripting.api.BindingsValuesProvider;
import org.profile.service.CompanyService;
import org.profile.service.FriendService;
import org.profile.service.GroupService;
import org.profile.service.ProfileService;
import org.social.service.SocialMessagesService;

@Component(immediate = true, metatype = false)
@Service
@Properties({
        @Property(name = "service.description", value = "ESP Binding Values "),
        @Property(name = "service.vendor", value = "The Apache Software Foundation"),
        @Property(name = "javax.script.name", value = "any") })
public class BindingServiceImpl implements BindingsValuesProvider {

    @Reference
    private ProfileService service;

    @Reference
    private CompanyService companyService;

    @Reference
    private GroupService groupService;

    @Reference
    private FriendService friendService;

    @Reference
    private SocialMessagesService messageService;

    @SuppressWarnings("unused")
    public void addBindings(Bindings bindings) {

        SlingHttpServletRequest request = (SlingHttpServletRequest) bindings
                .get("request");
        ArrayList<String> a = new ArrayList<String>();
        if (request.getPathInfo().equals("/servlet/common/search.profileDiv")) {
            if(request.getParameterMap().containsKey("flag") 
                      && request.getParameter("flag").equals("follow")){
                bindings.put("key",
                    service.searchProfile(request.getParameter("keywords"),"follow"));
            }else{
                bindings.put("key",
                    service.searchProfile(request.getParameter("keywords"),""));
            }    
        }
        if (request.getPathInfo().equals("/servlet/common/search.companyDiv")) {
            bindings.put("companyKey", companyService.searchCompany(request
                    .getParameter("keywords")));
        }
        if (request.getPathInfo().equals("/servlet/common/search.groupDiv")) {
            bindings.put("groupKey",
                    groupService.searchGroup(request.getParameter("keywords")));
        }
        if (request.getParameterMap().containsKey("userId")
                && request.getParameterMap().containsKey("friendId")) {
            bindings.put("mutualFriend", friendService.mutualFriend(
                    request.getParameter("userId"),
                    request.getParameter("friendId")));
            
        }
        /*if (request.getPathInfo().equals("/servlet/friend/show.view")) {
           
            bindings.put("online",
                    friendService.onlineFriend(request.getParameterMap().keySet().toArray()[0]+""));
        }*/
        if (request.getPathInfo().equals("/servlet/social/messages.viewMessages")) {
            bindings.put("messages", messageService.renderMessages(request
                    .getParameter("screenName")));
        }
      /*  if (request.getPathInfo().equals("/servlet/common/search.call") && 
                request.getParameter("param").equals("true") && 
                request.getParameterMap().containsKey("userId")) {
            bindings.put("onlineCheck",
                    friendService.onlineCheck(request.getParameter("userId")));
        }*/
        if (request.getPathInfo().equals("/servlet/friend/show.invite") 
                       && request.getParameterMap().containsKey("conference")) {
            bindings.put("conference",
                    friendService.onlineFriend(request.getParameter("userId")));
        }
        /*bindings.put("sessionPerson",service.setSession(session1));*/
    }

}
