<%resource = Packages.java.util.ResourceBundle.getBundle("server");%>
<%
var login=request.getSession(true);
var loginId=login.getAttribute("loginPkId")+"";
%>
<%
// Check if we have a currentNode
var currentNodeProvided = ((typeof currentNode != "undefined") && currentNode.title != null);

function getCurrentNodeValue(prop) {
    var result = "";
    if(currentNodeProvided && currentNode[prop]) {
        result = currentNode[prop];
    }
    return result;
}
/* Global variable */

var degree="";
var showEmail="true";
var showPhone="true";
/* */

/* Following are privacy variables  */
var emailPrivacy="";
var contactPrivacy=""
var productPrivacy="";
var companyPrivacy="";
var connectionPrivacy="";
var groupPrivacy="";
var activityPrivacy="";
/*  */


var postPath = null;
var pageTitle = null;
if(currentNodeProvided) {
	postPath=request.getContextPath();
    postPath += currentNode.getPath();
    pageTitle = currentNode.title;
} else {
    postPath = request.getContextPath() + "";
    if(postPath.length > 0) {
        postPath += "/";
    }
    postPath += request.getPathInfo();
    pageTitle = "New post";
}
load("includes/header.esp");
%>
<!--
<script type="text/javascript" src="<%=request.getContextPath()%>/apps/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/apps/userjs/jquery-ui-1.8.23.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/apps/user/js/jquery.powertip.js"></script>
<script src="<%=request.getContextPath()%>/apps/user/js/html2canvas.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/apps/user/css/jquery.powertip.css" rel="stylesheet" type="text/css" />

-->
<link href="<%=request.getContextPath()%>/wro/profile.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/wro/profile.js"></script>
<%

if(currentNodeProvided && request.getParameterMap().containsKey("userId")){
if(currentNode.hasNode("Privacy") && currentNode.getNode("Privacy").hasNode("Personal")){
    var personalPrivacy=currentNode.getNode("Privacy").getNode("Personal");
     emailPrivacy=personalPrivacy.emailPrivacy?personalPrivacy.emailPrivacy:"";
     contactPrivacy=personalPrivacy.contactPrivacy?personalPrivacy.contactPrivacy:"";
     connectionPrivacy=personalPrivacy.connectionPrivacy?personalPrivacy.connectionPrivacy:"";
     groupPrivacy=personalPrivacy.groupsPrivacy?personalPrivacy.groupsPrivacy:"";
     activityPrivacy=personalPrivacy.activityPrivacy?personalPrivacy.activityPrivacy:"";
     productPrivacy=personalPrivacy.productPrivacy?personalPrivacy.productPrivacy:"";
     companyPrivacy=personalPrivacy.companyPrivacy?personalPrivacy.companyPrivacy:"";
} 
%>

    <script type="text/javascript">
        Query(document).ready(function($) {
            html2canvas({
                onrendered: function(canvas) {
                      var img = canvas.toDataURL()
                      window.open(img);
                  }
             });
        });
    </script>  
    <script>
        $.ajax({
              type: 'POST',
              url: '<%=request.getContextPath()%>/content/user/save.visit',
              data: 'userId=<%=request.getParameter("id")%>&visitor=<%=request.getParameter("userId")%>',
              success: function(a){}
              });
    </script>
<%}%>
<script>
        
       <%if(currentNodeProvided && request.getParameterMap().containsKey("userId")){%>
            jQuery(document).ready(function($) {
                $('#contentFeedbackFrame').load("<%=request.getContextPath()%>/servlet/"+
                        "feedback/content.show?contentId=<%=currentNode.getIdentifier()%>"+
                        "&userId=<%=request.getParameter('userId')%>&content=YYYYYYYY");
            });

        <%}else if(currentNodeProvided)
		{%>
            $(function() {
                    $('#contentFeedbackFrame').load("<%=request.getContextPath()%>/servlet/"+
                            "feedback/content.show?contentId=<%=currentNode.getIdentifier()%>"+
                            "&userId=<%=request.getParameter('id')%>&content=YYYYYYYY");
                            
                    $('#socialConnectId').load("<%=request.getContextPath()%>/servlet/"+
                            "/social/service.socialConnect?userId=<%=request.getParameter('id')%>");        
                            
                });
             
             $(function() {
                    $('#suggestFrame').load("<%=request.getContextPath()%>/servlet/"+
                            "suggest/content.view?suggestURL=<%=request.getRequestURL().append('?').append(request.getQueryString())%>"+
                            "&userId=<%=request.getParameter('id')%>");
                });
            $(function() {
                    $('#commFrame').load("<%=request.getContextPath()%>/servlet/"+
                            "common/search.call?param=dummy"+
                            "&userId=<%=request.getParameter('id')%>");
                });    
           <%}%>
           
        function statusR(v)   
        {
            
            if(v!='Married')
                {
                 
                    document.getElementById("anni").value="";
                    document.getElementById("anni").disabled="true";
                }
            else{
                document.getElementById("anni").disabled="";
                
            }
                
        }
        
        function searchPeople(userId){      
              window.location="<%=request.getContextPath()%>/servlet/common/search.profile?keywords=&userId="+userId;      
        }    
            
        function submitForm(v){
            
            if(confirm('Confirm Delete'))
               { 
                document.getElementById(v).submit();
                }
            else 
                {}
        }
               
        function loadContent(elementSelector, sourceUrl) {
             $(""+elementSelector+"").load("<%=request.getContextPath()%>/"+sourceUrl+"");
        }
        
   
     
        function deleteMobile(url, deleteNode, userId, obj, type){	
                var row = document.getElementById(deleteNode);	
                var parentDiv = $(obj).parent().parent().parent();
                if(type=='email'){
                    var index = $("#emailT tr").index(row);
				}else if(type=='phone'){
				    var index = $("#phone tr").index(row);
				}else if(type=='im'){
                    var index = $("#im tr").index(row);
                }					
				var data='userId='+userId+'&deleteNode='+deleteNode;
				apprise('Confirm Delete', {'confirm':true}, function(r) {
					if(r) { 
    					if(typeof(r)=='string'){
    					     alert(r);
    					}else{
    						$.ajax({
							  type: 'POST',
							  url: url,
							  data: data,
							  success: function(){
							  	row.parentNode.removeChild(row); 
                                if($(parentDiv).children().size() <= 1){
                                    if(type=='phone'){
                                        addRow5('phone');
                                    }else if(type=='email'){
                                        addRow4('emailT');
                                    }else if(type=='im'){
                                        addRow1('im');
                                    }
                                  }   
                                  if(index=='1'){
                                    if(type=='phone'){
                                          $(parentDiv).children().eq(1)
                                           .children().eq(2).prepend("<a href='javascript:void(0)' "
                                           + "onclick=\"addRow5('phone')\" id='profileEdit'>"
                                           + "<img height='20' src='<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png' /></a>");
                                    }else if(type=='email'){
                                         $(parentDiv).children().eq(1)
                                           .children().eq(1).prepend("<a href='javascript:void(0)' "
                                           + "onclick=\"addRow4('emailT')\" id='profileEdit'>"
                                           + "<img height='20' src='<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png' /></a>");
                                    }else if(type=='im'){
                                         $(parentDiv).children().eq(1)
                                           .children().eq(2).prepend("<a href='javascript:void(0)' "
                                           + "onclick=\"addRow1('im')\" id='profileEdit'>"
                                           + "<img height='20' src='<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png' /></a>");
                                    }
                                   }
        							  }
    						});
    						 }
    					}
					else{}
				});			
					
				 
								
        }
		function sentVerification(url,verification,userId,flag,verificationFor,userName){
				
				
				
				 
				if(flag=='email'){
				var data='userId='+userId+'&verification='+verification+'&flag=EmailID&userName='+userName+'&verificationFor='+verificationFor;
					apprise('Are you sure you want to verify your Email Address ?', {'confirm':true}, function(r) {
						if(r) { 
							if(typeof(r)=='string')
								{ alert(r);
								 }
							else
								{
									$.ajax({
									  type: 'POST',
									  url: url,
									  data: data,
									  success: function(){
									  	
									  }
									});	
								 }
							}
						else 
							{}
					});
					 
	       				 
	       					 
					     
					  
				        
				   } else if(flag=='mobile'){
				   
				  
				   
				   var data='userId='+userId+'&verification='+verification+'&flag=Phone&userName='+userName+'&verificationFor='+verificationFor;
				   		apprise('Send Verification Code on Selected Mobile Number', {'confirm':true}, function(r) {
						if(r) { 
							if(typeof(r)=='string')
								{ alert(r);
								 }
							else
								{
									$.ajax({
									  type: 'POST',
									  url: url,
									  data: data,
									  success: function(){
									  	submitVerification1(userId,verification);
									  }
									});	
								 }
							}
						else 
							{}
					});			        
				   
				   }
								
        }
    function submitVerification1(userId,verification){
   
    			apprise('Please Enter the Verification Code that you have recieved on your Mobile Number.', {'input':true},function(r) {
					  	if(r) { 
								if(typeof(r)=='string'){
									{ var inputs='userId='+userId+'&number='+verification+'&token='+r;
									
										$.ajax({
										  type: 'POST',
										  url: '<%=request.getContextPath()%>/servlet/security/verify.verifyTokenMobile',
										  data: inputs,
										  dataType: 'html',
										  success: function(data){
										  	if(data=='Invalid Code'){
										  		alert('Invalid Code. Please Enter the Code again.');
										  		submitVerification1(userId,verification);
										  	}
										  	else if(data=='Valid'){ 
										  	   window.location.reload();
										  /*	$('#resultsID').html("<div class='alert alert-success navbar-spacer'"+
										  	 						"style='display: block; position: fixed; top: 0px; margin: 0px 0.5%;"+
		  																	"width: 95%; padding: 8px 2%; z-index: 9999; border-radius: 4px 4px 0px 0px;'>"+
		 															"Mobile Number Has Been Verified</div>");*/
										  		
										  	}
										  }
										});	
									
									}
								}
						}
				});
									  	
	}
	
	function activateFeedback(path,check,node,identifier){
	var url=path+"/servlet/feedback/content.activate";
	var data='activateNode='+node+'&activate='+check+'&identifier='+identifier;
	
	apprise('Are you sure to perform this operation?', {'confirm':true}, function(r) {
						if(r) { 
							if(typeof(r)=='string')
								{ alert(r);
								 }
							else
								{
									$.ajax({
									  type: 'POST',
									  url: url,
									  data: data,
									  success: function(){
									  	window.location.reload();
									  }
									});	
								 }
							}
						else 
							{}
					});	
		
	}
	
</script>
<script language="javascript">
    var counts;
    function addRow4(tableID) {
        var table = document.getElementById(tableID);
        var rowCount = table.rows.length;
        var row = table.insertRow(rowCount);
        counts = rowCount - 1;

        var cell1 = row.insertCell(0);
        var skill = document.createElement("input");
        skill.type = "text";
        skill.name = "email";
        skill.id = "jsp_" + counts + "__skill";
        skill.setAttribute("class", "");
        cell1.appendChild(skill);

        var cell2 = row.insertCell(1);
        var skill = document.createElement("label");
        skill.id = "jsp_" + counts + "__skill";
        skill.setAttribute("class", "");
        cell2.appendChild(skill);

        var cell3 = row.insertCell(2);
        var rate = document.createElement("label");
        rate.id = "jsp_" + counts + "__skill";
        rate.setAttribute("class", "");
        cell3.appendChild(skill);

    }
    function addRow1(tableID) {

        var table = document.getElementById(tableID);
        var rowCount = table.rows.length;

        var row = table.insertRow(rowCount);
        counts = rowCount - 1;

        var cell3 = row.insertCell(0);
        var rate = document.createElement("input");
        rate.type = "text";
        rate.name = "im";
        rate.setAttribute("class", "");
        rate.id = "jsp_" + counts + "__rate";
        cell3.appendChild(rate);

        var cell2 = row.insertCell(1);  
        var selector = document.createElement('select');
        selector.id = 'selBidReceivedIsPM';
        selector.name = 'imType';
        cell2.appendChild(selector);
        var option = '';
        var skill = "Yahoo,GTalk,Skype,Window Live Messenger";
        var token = skill.split(","); 
        option = document.createElement('option');
        for(var i = 0;i<token.length;i++){  
        option = document.createElement('option');
        option.value = token[i];
        option.appendChild(document.createTextNode(token[i]));
        selector.appendChild(option);
        }

        
        
        var cell1 = row.insertCell(2);
        var skill = document.createElement("label");
        skill.id = "jsp_" + counts + "__skill";
        skill.setAttribute("class", "");
        cell1.appendChild(skill);
    }
    function addRow5(tableID) {
        var table = document.getElementById(tableID);
        var rowCount = table.rows.length;
        var row = table.insertRow(rowCount);
        counts = rowCount - 1;

        

        var cell2 = row.insertCell(0);
        var skill = document.createElement("input");
        skill.type = "text";
        skill.name = "number";
        skill.id = "jsp_" + counts + "__skill";
        skill.setAttribute("class", "");
        cell2.appendChild(skill);

        
        
        var cell2 = row.insertCell(1);  
        var selector = document.createElement('select');
        selector.id = 'selBidReceivedIsPM';
        selector.name = 'numberType';
        cell2.appendChild(selector);
        var option = '';
        var skill = "Home,Office,Mobile";
        var token = skill.split(",");        
        for(var i = 0;i<token.length;i++){  
        option = document.createElement('option');
        option.value = token[i];
        option.appendChild(document.createTextNode(token[i]));
        selector.appendChild(option);
        }
        
        var cell1 = row.insertCell(2);
        var skill = document.createElement("label");
        skill.id = "jsp_" + counts + "__skill";
        skill.setAttribute("class", "");
        cell1.appendChild(skill);
    }

    function deleteRow(guestList) {
        try {
            //alert("1");
            var table = document.getElementById(guestList);
            var rowCount = table.rows.length;
            if (rowCount > 2) {
                //alert("2");
                table.deleteRow(rowCount - 1);
            }
        } catch (e) {
            alert(e);
        }
    }
</script>





<script>

	$(function() {
			$('.north').powerTip({placement: 'n'});
		});   
</script>
<script type="text/javascript">
    function showNHide() {
        //alert("dgfdf");
        document.getElementById("personProfileContent").style.display = "none";
        document.getElementById("editable").style.display = "";
    }
    
</script>
</head>

<body>
<div class="container-fluid navbar-spacer">
    <div id="pageContent"  >
     <div  class="section-right">Right</div>
            
                <div id="personProfileContent" class="section-left">
                    <fieldset class="row-fluid" id="userProfilePrimaryData">
                        <div class="profile-user-thumb">
                       <% if(currentNodeProvided && currentNode.hasProperty("profileImage")){
                           %>
                           <img src="<%=getCurrentNodeValue("profileImage")%>" height="165px" width="125px" />
                           <%}
                           else{  %>
                         <img src="<%=request.getContextPath()%>/apps/user/css/images/photo.gif" /> 
                         <%}%>
                        </div>
                        
                       
                        
                            <!-- Display user info-->
                            <div class="profile-info-visible" style="width: 480px !important;">
                                <h2>
                               <%if(currentNodeProvided){%>
                                    <%=currentNode.name? currentNode.name: ""%>&nbsp;<%=currentNode.maidenName? currentNode.maidenName: ""%>&nbsp;<%=currentNode.lastName? currentNode.lastName: ""%>
                                 <%}%>
                                    <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
                                        <a href="javascript:void(0)" id="profileEdit" onclick="showNHide()">
                                            <img alt="Edit" title="Edit" src="<%=request.getContextPath()%>/apps/user/css/images/icon_edit.png" height="14" align="middle">
                                        </a>
                                   <%}%> 
                                 <%if(request.getParameterMap().containsKey("userId") 
                                 		&& request.getParameterMap().containsKey("friendId")){%>   
											 <%
											 	if(currentNode.hasNode("connection") 
											 			&& currentNode.getNode("connection").hasNode("friend")
											 			&& currentNode.getNode("connection").getNode("friend")
											 					.hasNode(request.getParameter("userId"))
											 			&& currentNode.getNode("connection").getNode("friend")
											 			   .getNode(request.getParameter("userId")).getProperty("request")=='accept'){
											 			   degree='first';
											 			%> <span class="friend-degree">
											 					1<sup>st</sup>
											 				 </span>
											 			<%
											 			}else if(request.getParameter("userId").equals(request.getParameter("friendId"))){
											 			%>
											 			 	<span class="friend-degree">
											 					You
											 				 </span>
											 			<%
											 			} else if(mutualFriend.size()>0){						 														 			
											 			%>
											 				 <span class="friend-degree">
											 					2<sup>nd</sup>
											 				  </span>
											 				
											 			<%
											 			}%>
											 	
										
									<%}%>
                                </h2>
                                    
                                <h3>
                                    <span id="givenName">
                                    <%=getCurrentNodeValue("headline")%>
                                    </span>&nbsp;
                                </h3>
                                
<ul>
<li class="spe">
<%=getCurrentNodeValue("industry")%> &nbsp;&nbsp;|&nbsp;&nbsp; <%=getCurrentNodeValue("city")%>
</li>
<li>
<span class="icon birth">Born on:</span><%=getCurrentNodeValue("birthDay")%>
<span class="icon anniversary">
<%if(getCurrentNodeValue("status")=="Married"){%>
Married on:</span><%=getCurrentNodeValue("status")%>
<%}else{
out.println(getCurrentNodeValue("status"));
}
%>

</li>
<li style="white-space: nowrap;">

<span class="icon email">Email:</span>


<%if(privacy(emailPrivacy)=='true' && currentNodeProvided && currentNode.hasNode("ContactDetails") 
                   && currentNode.getNode("ContactDetails").hasNode("EmailID")){
	 var emailValue=currentNode.getNode("ContactDetails/EmailID").getNodes();

	 for(i in emailValue){
	 %>
	 	<%=emailValue[i].emailId%>
	<% 	
	 	if(emailValue[i].verifiedEmailFlag=='no'){
	 	%>
	 	<%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
    	 	<button class="north"  title="Click to verify <%=emailValue[i].emailId%>"
    	 			 style="height:16px;width:22px;border:none; background: none repeat scroll 0 0 transparent;" 
    	 			 	onclick="sentVerification('<%=request.getContextPath()%>/servlet/security/verify.sendToken',
    	 	    		'<%=emailValue[i]%>','<%=request.getParameter("id")%>','email',
    	 	    		'<%=emailValue[i].emailId%>','<%=currentNode.name?currentNode.name:""%>')" >
    	 		<span class="unverify"></span></button>
    	 <%}else{%>
    	        <button style="height:16px;width:22px;border:none; background: none repeat scroll 0 0 transparent;"
                      ><span class="unverify"></span></button>
    	 <%}%>		
	 	<%	 	
	 	}else{
	 	%>
	 	<%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
	 	     <button class="north"  title="Email is Verified"
	 			 style="height:16px;width:22px;border:none; background: none repeat scroll 0 0 transparent;">
	 			 <span class="verify"></span></button>
	 	<%}else{%>
	 	         <button style="height:16px;width:22px;border:none; background: none repeat scroll 0 0 transparent;"
	 	              ><span class="verify"></span></button>
	 	<%}%>		 
	 		<%
	 	}
	 }
}

%>
</li>
<li>                            
<span class="icon im">IM:</span><%=getCurrentNodeValue("im")[0]%><em>&nbsp;(<%=getCurrentNodeValue("imType")[0]%>)</em>

</li>
<li style="width:440px;white-space: nowrap;">                                
<span class="icon number">Contact:</span>

<%if( privacy(contactPrivacy)=='true' && currentNodeProvided && currentNode.hasNode("ContactDetails") 
                        && currentNode.getNode("ContactDetails").hasNode("Phone")){
                    
		 var phoneValue1=currentNode.getNode("ContactDetails");
	  var phoneValue2=phoneValue1.getNode("Phone");	
	  var phoneValue=phoneValue2.getNodes();
	
	 for(i in phoneValue)
	 { 
	
	 %>
	 <%=phoneValue[i].number%><em>&nbsp;(<%=phoneValue[i].numberType%>)</em>
	 <%
	 	if(phoneValue[i].numberType=='Mobile'){
	 		if(phoneValue[i].verifiedMobileFlag=='no'){
	 		%>
	 		<%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
    	 		<button class="north"  title="Click to verify <%=phoneValue[i].number%>"
    	 			 style="height:16px;width:22px;border:none; background: none repeat scroll 0 0 transparent;" 
    	 			 	 onclick="sentVerification('<%=request.getContextPath()%>/servlet/security/verify.sendToken',
    	 	    		'<%=phoneValue[i]%>','<%=request.getParameter("id")%>','mobile',
    	 	    		'<%=phoneValue[i].number%>','<%=currentNode.name?currentNode.name:""%>')" >
    	 					<span class="unverify"></span></button>
    	 	<%}else{
    	 	          <button style="height:16px;width:22px;border:none; background: none repeat scroll 0 0 transparent;"
                      ><span class="unverify"></span></button>
    	 	}%>				

	 		<%	 	
		 	}else{
		 	%>
		 	  <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
		 		<button class="north"  title="Mobile Number is Verified"
	 			 style="height:16px;width:22px;border:none; background: none repeat scroll 0 0 transparent;"
	 			  ><span class="verify"></span></button>
	 		  <%}else{%>
	 		         <button style="height:16px;width:22px;border:none; background: none repeat scroll 0 0 transparent;"
                      ><span class="verify"></span></button>      
	 		  <%}%>
	 		<%
	 		}
	 	}
	 }
}
%>



</li>
<li>            
<span class="icon address">Address:</span><%=getCurrentNodeValue("address")%>
</li>
</ul>
                </div>
<div class="social-media">
<%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
<span style="float:left; font-family: 'Dosis',serif !important; font-size:12px; font-weight:bold; color:#006699; margin-right:10px; line-height:14px;">Import profile using: </span>
	<a target="_blank" href="<%=resource.getString("rave.portal")%>/portal/socialauthProfile.html?id=facebook"><img src="<%=request.getContextPath()%>/apps/user/css/images/facebook_icon.png" height="16" alt="Facebook" title="Facebook" border="0"></img></a>
<!--<td><a href="../socialauthProfile.html?id=twitter"><img src="css/images/twitter_icon.png" height="16" alt="Twitter" title="Twitter" border="0"></img></a></td> -->
<!-- <td><a href="socialauthProfile.html?id=google"><img src="css/images/gmail-icon.jpg" height="16" alt="Gmail" title="Gmail" border="0"></img></a></td> -->
	<a target="_blank" href="<%=resource.getString("rave.portal")%>/portal/socialauthProfile.html?id=yahoo"><img src="<%=request.getContextPath()%>/apps/user/css/images/yahoomail_icon.jpg" height="16" alt="YahooMail" title="YahooMail" border="0"></img></a>
	<a target="_blank" href="<%=resource.getString("rave.portal")%>/portal/socialauthProfile.html?id=hotmail"><img src="<%=request.getContextPath()%>/apps/user/css/images/hotmail.jpeg" height="16" alt="HotMail" title="HotMail" border="0"></img></a>
	<a target="_blank" href="<%=resource.getString("rave.portal")%>/portal/socialauthProfile.html?id=linkedin"><img src="<%=request.getContextPath()%>/apps/user/css/images/linkedin.gif" height="16" alt="Linked In" title="Linked In" border="0"></img></a><br />
 <span style="float:left; font-family: 'Dosis',serif !important; font-size:12px; font-weight:bold; color:#006699; margin-right:10px; line-height:14px;">Import Contact using: </span>
	<a target="_blank" href="<%=resource.getString("rave.portal")%>/portal/socialauthContact.html?id=googleplus"><img src="<%=request.getContextPath()%>/apps/user/css/images/gmail-icon.jpg" height="12" alt="Google" title="Google" border="0"></img></a>
	<a target="_blank" href="<%=resource.getString("rave.portal")%>/portal/socialauthContact.html?id=yahoo"><img src="<%=request.getContextPath()%>/apps/user/css/images/yahoomail_icon.jpg" height="16" alt="YahooMail" title="YahooMail" border="0"></img></a>
	<!--a href="<%=resource.getString("rave.portal")%>/portal/socialauthContact.html?id=hotmail"><img src="<%=request.getContextPath()%>/apps/user/css/images/hotmail.jpeg" height="16" alt="HotMail" title="HotMail" border="0"></img></a-->
	<a rel="facebox" href="<%=request.getContextPath()%>/content/user/*.uploadContacts?userId=<%=request.getParameter("id")%>"><img src="<%=request.getContextPath()%>/apps/user/css/images/linkedin.gif" height="16" alt="Linked In" title="Linked In" border="0"></img></a>
<!-- <a href="<%=resource.getString("rave.portal")%>/portal/importProfile.jsp">Click Here To Import Profile</a> -->
<%}%>
<%if(request.getParameterMap().containsKey("friendId") && request.getParameterMap().containsKey("userId")){
if(!request.getParameter("userId").equals(request.getParameter("id"))){
%>
 <a rel='facebox' href="<%=request.getContextPath()%>/servlet/friend/show.mutualFriend?userId=<%=request.getParameter("userId")%>&friendId=<%=request.getParameter("friendId")%>"><%=mutualFriend.size()%></a> Mutual Friend
<%}}%>
<br />

<%if(currentNodeProvided){%>
   <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%> Activate Content Feedback <%}%>
    <%if(currentNodeProvided && currentNode.hasProperty("activateFeedback") && currentNode.activateFeedback=='true'){%>
      <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>  
        <input type="checkbox" id="activateFeedback" checked onchange="activateFeedback('<%=request.getContextPath()%>',this.checked,'<%=currentNode%>','<%=currentNode.getIdentifier()%>')" />
      <%}%> 
        <div id="contentFeedbackFrame"></div>
    <%}else{%>
      <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
        <input type="checkbox" id="activateFeedback" onchange="activateFeedback('<%=request.getContextPath()%>',this.checked,'<%=currentNode%>','<%=currentNode.getIdentifier()%>')" />
      <%}%> 
    <%}%>
<%}%>
 <div id="suggestFrame"></div>
 <div id="commFrame"></div>
</div>                      
                    </fieldset>
                </div>   
               
         

</div>



    <div id="editable" style="display:none;" class="section-left">

                <div id="personProfileContent">
                    <fieldset class="row-fluid" id="userProfilePrimaryData">
                        <div class="profile-user-thumb">
                             <% if(currentNodeProvided && currentNode.hasProperty("profileImage")){
	                           %>
	                           <img src="<%=getCurrentNodeValue("profileImage")%>" height="165px" width="125px" />
	                           <%}
	                           else{  %>
	                         <img src="<%=request.getContextPath()%>/apps/user/css/images/photo.gif" /> 
	                         <%}%>
                                
                            <div align="center"><a rel="facebox" href="<%=postPath%>/attachments/*.upload?userId=<%=request.getParameter("id")%>" class="btn btn-primary">Change Picture</a></div>
                            <!-- <div align="center"><a href="#" class="btn btn-warning">Remove Picture</a></div> -->
                        </div>

                        

                        <!-- Display user info-->
                        
                            <div class="profile-info-visible" style="width:77%;">
                                <form  method="POST" action="<%=request.getContextPath() %>/content/user/save.pro">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="form">
                                        <tr>
                                            <%if(!currentNodeProvided){
                                                %>
                                                    <input type="hidden" name="profileImage"
                                                         value="<%=request.getContextPath()%>/apps/user/css/images/photo.gif" />         
                                            <%}else{%>
                                        	<input type="hidden" name="profileImage" value="<%=getCurrentNodeValue("profileImage")%>" />
                                            <%}%>
                                            <td width="33%"><label>First Name</label>
                                            <%if(currentNodeProvided){%>
                                            <input type="hidden" name="title" value="<%=getCurrentNodeValue("title")%>" />
                                            <%}else {%>
                                            <input type="hidden" name="title" value="<%=request.getParameter("id")%>" />
                                            <%}%>
                                            <input type="text" name="name"  value="<%= getCurrentNodeValue("name") %>" title="First Name" /></td>
                                            <td width="33%"><label>Middle Name</label><input type="text" name="maidenName" value="<%= getCurrentNodeValue("maidenName") %>" title="Middle Name" /></td>
                                            <td width="33%"><label>Last Name</label><input type="text" name="lastName" value="<%= getCurrentNodeValue("lastName") %>" title="Last Name" /></td>
                                        </tr>
                                        
                                        <tr>
                                            <td colspan="3"><label>Profile HeadLine</label><textarea  name="headline" rows="1"><%= getCurrentNodeValue("headline") %></textarea></td>
                                        </tr>
                                        <tr>
                                            <td><label>Maritual Status</label>
                                            <select name="status"  onchange="statusR(this.value)" >
                                            <%
                                                var statusVal=["Single","In a Relationship","Engaged","Married","Divorced","In an Open Relationship"]
                                               for(i in statusVal) {
                                               if(getCurrentNodeValue("status")==statusVal[i]){
                                               %>
                                                   <option value="<%=statusVal[i]%>" selected="selected" ><%=statusVal[i]%></option>
                                               <%}
                                                   else{
                                               %>
                                                <option value="<%=statusVal[i]%>"><%=statusVal[i]%></option>   
  
                                                    <%  }} %>
                                                    </select>
                                           </td>

                                        
                                            <td><label>Birthday</label><input type="text" name="birthDay"
                                                    value="<%= getCurrentNodeValue("birthDay") %>" /></td>
                                            <td><label>Marriage Anniversary</label>
                                          <%  if(getCurrentNodeValue("anniversary")=="Married"){
                                          %>
                                            <input name="anniversary" type="text" value="<%= getCurrentNodeValue("anniversary") %>"  id="anni"  />
                                          <%
                                              }else{
                                              %>
                                              <input name="anniversary" type="text" value="<%= getCurrentNodeValue("anniversary") %>" disabled="true" id="anni"  />
                                                          
                                           <%}  
                                          %>      
                                                   
                                                   
                                                    </td>
                                        
                                    <tr><td colspan="3">
                                    <div class="separator"></div>
                                    
                                    <table id="emailT" width="100%" border="0" cellspacing="0" cellpadding="0">
                                            
                                            
                                            <tr>
                                                <td width="33%"><label>Email</label></td>
                                                <td width="33%">&nbsp;</td>
                                                <td width="33%">&nbsp;</td>
                                            </tr>
                                            
                                             
                                            <%
                                                
                                                if( currentNodeProvided && currentNode.hasNode("ContactDetails")
                                                	 && currentNode.getNode("ContactDetails").hasNode("EmailID")
                                                	  && currentNode.getNode("ContactDetails").getNode("EmailID").hasNodes() ){
	                                                
		                                                var emailValue=currentNode.getNode("ContactDetails/EmailID").getNodes();
		                                                
		                                               var countEmail=0;
		                                                for(i in emailValue)
		                                                {countEmail++;
		                                                %>
		                                                  <tr id="<%=emailValue[i].getName()%>">
		                                                      <td >                                                    
		                                                        <input name="email" type="text" value="<%= emailValue[i].emailId %>" /> 
		                                                        <input name="emailValue" type="hidden" value="<%= emailValue[i].getName() %>" /> 
		                                                        </td>
		                                                        <td class="addClass">
		                                                       <% if(countEmail==1)
		                                                        {
		                                                        %>
		                                                       <a id="profileEdit" onclick="addRow4('emailT')" href="javascript:void(0)">
		                                                            <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png" title="Add Row" alt="ADD">
		                                                        </a>
		                                                        <%} %>
		                                                        <a id="profileEdit"
		                                                        	 onclick="deleteMobile('<%=request.getContextPath()%>/content/user/save.deleteEmail','<%= emailValue[i].getName() %>','<%=request.getParameter("id")%>',this, 'email')" href="javascript:void(0)">
		                                                            <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png" title="Delete Row" alt="Delete">
		                                                        </a>
		                                                        </td>
		
		                                                    
		                                                        
		                                                        
		                                                      </tr>
		                                                <%}}
		                                                else{ %>
		
		                                                <tr>
		
		                                                    <td><input name="email" type="text" value="" /></td>
		                                                    <input name="emailValue" type="hidden" value="" />
		
		                                                    <td><a id="profileEdit" onclick="addRow4('emailT')" href="javascript:void(0)">
		                                                            <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png" title="Add Row" alt="ADD">
		                                                        </a>
		                                                        <a id="profileEdit" onclick="deleteRow('emailT')" href="javascript:void(0)">
		                                                            <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png" title="Delete Row" alt="Delete">
		                                                        </a>
		                                                        </td>
		
		                                                    <td></td>
		     
		                                                </tr>
		                                                <%}%>
                                      
                                            
                                        </table>
                                    </td></tr>
                                     <tr><td colspan="3">
                                    <div class="separator"></div>
                                    
                                    <table id="phone" width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td width="33%"><label>Phone Number</label></td>
                                                <td width="33%"><label>Number Type</label></td>
                                                <td width="33%">&nbsp;</td>
                                            </tr>
                                            <%
                                                if(currentNodeProvided && currentNode.hasNode("ContactDetails")
                                                 && currentNode.getNode("ContactDetails").hasNode("Phone")
                                                 && currentNode.getNode("ContactDetails").getNode("Phone").hasNodes()){
                                                var numberValue=currentNode.getNode("ContactDetails/Phone").getNodes();
                                              	 var countPhone=0;
                                                
                                                for(j in numberValue)
                                                {countPhone++;
                                             %>
                                                 <tr id="<%=numberValue[j].number%>">

                                                    <td><input name="number" type="text" value="<%=numberValue[j].number %>" /></td>
                                                    
                                                     <td>      
                                                          <select name="numberType">
                                            <%
                                                var phoneVal=["Mobile","Home","Office"];
                                               for(i in phoneVal) {
                                               if(phoneVal[i]==numberValue[j].numberType){
                                               out.println(numberValue[j].numberType);
                                               %>
                                                   <option value="<%=phoneVal[i]%>" selected="selected" ><%=phoneVal[i]%></option>
                                               <%}
                                                   else{
                                               %>
                                                <option value="<%=phoneVal[i]%>"><%=phoneVal[i]%></option>   
  
                                                    <%  }} %>
                                                         </select>
                                                     
                                                     </td>
                                                     <td class="addClass">
                                                  <%
                                                      if(countPhone==1){
                                                  %>
                                                                                                        
                                                            <a id="profileEdit" onclick="addRow5('phone')" href="javascript:void(0)">
                                                                <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png" title="Add Row" alt="ADD">
                                                            </a>
                                                    <%}%>
                                                     <a id="profileEdit" onclick="deleteMobile('<%=request.getContextPath()%>/content/user/save.deleteMobile','<%=numberValue[j].number%>','<%=request.getParameter("id")%>',this,'phone')" href="javascript:void(0)">
                                                                <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png" title="Delete Row" alt="Delete">
                                                            </a> </td>


                                                </tr>
                                             <%}} else {%>
                                            
                                            
                                            
                                            
                                                <tr>

                                                    <td><input name="number" type="text" value="" /></td>
                                                    
                                                     <td>      
                                                          <select name="numberType">
                                            <%
                                                var phoneVal=["Mobile","Home","Office"];
                                               for(i in phoneVal) {
                                               
                                               %>
                                                <option value="<%=phoneVal[i]%>"><%=phoneVal[i]%></option>   
  
                                                    <%  } %>
                                                         </select>
                                                     
                                                     </td>
                                                  
                                                      <td>                                                  
                                                            <a id="profileEdit" onclick="addRow5('phone')" href="javascript:void(0)">
                                                                <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png" title="Add Row" alt="ADD">
                                                            </a>
                                                            <a id="profileEdit" onclick="deleteRow('phone')" href="javascript:void(0)">
                                                                <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png" title="Delete Row" alt="Delete">
                                                            </a>
                                                            
                                                            
                                                      </td>



                                                </tr>
                                                <%}%>
                                              </table>  </td></tr>

                                          <tr><td colspan="3">
                                             
                                        <div class="separator"></div>
                                        <table id="im" width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td width="33%"><label>IM Id</label></td>
                                                <td width="33%"><label>IM Type</label></td>
                                                <td width="33%">&nbsp;</td>
                                            </tr>
                                            <%
                                                 if(currentNodeProvided && currentNode.hasNode("ContactDetails")
                                                 && currentNode.getNode("ContactDetails").hasNode("IM")
                                                 && currentNode.getNode("ContactDetails").getNode("IM").hasNodes()){
                                                 
                                                var imValue=currentNode.getNode("ContactDetails").getNode("IM").getNodes();
                                                 var countim=0;
                                                for(j in imValue){
                                                                                               
                                                countim++;
                                             %>
                                                 <tr id="<%=imValue[j].imValue%>">

                                                    <td><input name="im" type="text" value="<%=imValue[j].imValue%>" /></td>
                                                     <td>      
                                                         <select name="imType">
                                                           <%
                                                            var imVal=["Yahoo","GTalk","Skype","Window Live Messenger"];
                                                            for(i in imVal) {
                                                             if( imValue[j].imTypeValue==imVal[i]){%>
                                                                 <option value="<%=imVal[i]%>" selected="selected" ><%=imVal[i]%></option>
                                                             <%}else{%>
                                                                <option value="<%=imVal[i]%>"><%=imVal[i]%></option>   
                                                             <%  }} %>
                                                         </select>
                                                     </td>
                                                     <td>
                                                        <%if(countim==1){%>
                                                            <a id="profileEdit" onclick="addRow1('im')" href="javascript:void(0)">
                                                                <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png" title="Add Row" alt="ADD">
                                                            </a>
                                                        <%}%>
                                                            <a id="profileEdit" onclick="deleteMobile('<%=request.getContextPath()%>/content/user/save.deleteIM','<%=imValue[j].getName()%>','<%=request.getParameter("id")%>',this,'im')" href="javascript:void(0)">
                                                                <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png" title="Delete Row" alt="Delete">
                                                            </a>
                                                     </td>
                                                 </tr>
                                             <%}} else {%>
                                                 <tr>
                                                    <td><input name="im" type="text" value="" /></td>
                                                    <td>      
                                                     <select name="imType"   >
                                                      <%var imVal=["Yahoo","GTalk","Skype","Window Live Messenger"]
                                                       for(i in imVal) {%>
                                                           <option value="<%=imVal[i]%>"  ><%=imVal[i]%></option>
                                                       <%}%>
                                                     </select>
                                                    </td>
                                                     <td>                                                  
                                                            <a id="profileEdit" onclick="addRow1('im')" href="javascript:void(0)">
                                                                <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png" title="Add Row" alt="ADD">
                                                            </a>
                                                            <a id="profileEdit" onclick="deleteRow('im')" href="javascript:void(0)">
                                                                <img height="20" align="middle" src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png" title="Delete Row" alt="Delete">
                                                            </a>
                                                      </td>
                                                </tr>
                                                <%}%>
                                           </table></td></tr> 
                                            
                                            <tr><td colspan="3">
                                        <div class="separator"></div>
                                        
                                        
                                        <tr>
                                            <td><label>Current Industry</label><input name="industry" type="text" 
                                                    value="<%= getCurrentNodeValue("industry") %>" /></td>
                                        
                                            <td><label>Current Address</label><input name="address" type="text" 
                                                    value="<%= getCurrentNodeValue("address") %>" /></td>
                                        
                                            <td><label>Current City</label><input type="text" name="city" value="<%= getCurrentNodeValue("city") %>" /></td>
                                        </tr>
                                        
                                        
                                        <tr>
                                            <td><label>State</label><input name="state" type="text" 
                                                    value="<%= getCurrentNodeValue("state") %>" /></td>
                                            <td><label>Country</label><input name="country" type="text" 
                                                    value="<%= getCurrentNodeValue("country") %>" /></td>
                                            <td><label>Pin Code</label><input name="pinCode" type="text" 
                                                    value="<%= getCurrentNodeValue("pinCode") %>" /></td>
                                        </tr>
                                        
                                        <tr>
                                            <td></td>
                                            <td colspan="2"></td>
                                        </tr>
                                        <tr>
                                        
                                            <td>
                                            <%if(currentNodeProvided){%>
                                            
                                            <input name="redirect" type="hidden" value="<%=postPath%>.profile"/> 
                                            <%}else{%>
                                                     <input name="redirect" type="hidden" value="<%=request.getContextPath()%>/content/profile/posts/<%=request.getParameter("id")%>.profile"/> 
                                            
                                            <%}%>
                                                <input type="submit" class="btn btn-primary"
                                                value="Save" />
                                                <a class="btn btn-warning" href="<%=request.getContextPath()%>/content/user/info?id=<%=request.getParameter("id")%>" id="cancelEdit">Cancel</a></td>
                                        </tr>
                                        </td></tr>
                                    </table>
                                    
                                </form>
                            </div>
                    
                    
                    </fieldset>
                </div>


    </div>

<div id="ui-tab-common" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
  <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
    <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#">Network Details</a></li>
  </ul>
  <div class="ui-tabs-panel ui-widget-content ui-corner-bottom">
    <div class="profile-info-visible">
      <h3>  
      		<%
			  var countFriend=0;
			  var countRecommend=0;
			  if(currentNodeProvided && currentNode.hasNode("connection")){
				  	var connectionNode=currentNode.getNode("connection").getNode("friend").getNodes();
				  	for(i in connectionNode){
				  		if(connectionNode[i].request=='accept' ){
				  			countFriend++;
				  		}
				  	}
				 }
			   if(currentNodeProvided && currentNode.hasNode("Recommendation")){
			   		if(currentNode.getNode("Recommendation").hasNode("Reciever")){
					  	var recommendNode=currentNode.getNode("Recommendation").getNode("Reciever").getNodes();
					  	for(i in recommendNode){
					  		var recommendJobNode=recommendNode[i].getNodes();
					  		for( j in recommendJobNode){
					  			
						  		if(recommendJobNode[j].pending=='pending'){
						  			countRecommend++;
						  		}
					  		}
					  	}
					 }
				}
			  
			  %>
			  <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%> 
    		     <a href="javascript:void(0);" onclick="searchPeople('<%=request.getParameter("id")%>')">
    		      Sent Connection Request </a>
    		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		      
    		      <%if(currentNodeProvided  && privacy(connectionPrivacy)=='true'){%>
        		      <label>
        		          <a rel="facebox"
        		               href="<%=request.getContextPath()%>/servlet/friend/show.view?<%=request.getParameter("id")%>&delete" >
        		                     Connections ( <%=countFriend%> )</a>
        		      </label>
        		   <%}%>
        		<%}else{%>   
            		<%if(currentNodeProvided  && privacy(connectionPrivacy)=='true'){%>                 
                              <a rel="facebox"
                                   href="<%=request.getContextPath()%>/servlet/friend/show.view?<%=request.getParameter("id")%>&delete" >
                                         Connections ( <%=countFriend%> )</a>
                       <%}%>
        		<%}%>

	  </h3>
	  
	
	  <h3>	
	  <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
	  	<a  href="<%=request.getContextPath()%>/servlet/recommendation/show.show?userId=<%=
	  	        request.getParameter("id")%>">Sent Recommendation Request </a>
	  	<%}%>        
		      <!--  <a rel="facebox" href="<%=request.getContextPath()%>/servlet/recommendation/show.request?userId=<%=request.getParameter("id")%>"
		       							 class="link">Recommendation Request ( <%=countRecommend%> )</a> -->
		</h3>
		<h3>
		<%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
		  <a  href="<%=request.getContextPath()%>/servlet/common/search.group?keywords=&userId=<%=request.getParameter("id")%>">
				Sent Group Request</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <%if(currentNodeProvided  && privacy(groupPrivacy)=='true'){%>
                  <label>
                      <a rel="facebox"
                           href="<%=request.getContextPath()%>/servlet/group/show.addedGroup?userId=<%=request.getParameter("id")%>&delete" >
                                 View Group </a>
                  </label>
               <%}%>
           <%}else{%>
                <%if(currentNodeProvided  && privacy(groupPrivacy)=='true'){%>                 
                      <a rel="facebox"
                           href="<%=request.getContextPath()%>/servlet/group/show.addedGroup?userId=<%=request.getParameter("id")%>&delete" >
                                 View Group </a>
               <%}%>
           <%}%>    
         </h3>
         <%if(privacy(productPrivacy)=='true'){%>
            <h3><a rel="facebox" href="<%=request.getContextPath()%>/content/user/<%=currentNode.getName()%>.productMap"
                     >Tagged Products</a></h3>
         <%}%>
         <%if(privacy(companyPrivacy)=='true'){%>
            <h3><a rel="facebox" href="<%=request.getContextPath()%>/content/user/<%=currentNode.getName()%>.mapCompany" 
                    >Tagged Company</a></h3>
         <%}%>
          <h3><a href='#importContactId' rel='facebox'
                    >Imported Contacts</a></h3>   
    </div>
  </div>
</div>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
            <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
                <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
                    <a href="#summary" >Professional
                        Summary
                    </a>
                </li>  
<div id="summary" class="profile-info-visible ui-tabs-panel ui-widget-content ui-corner-bottom">

 <div id="skillList">       
<h3>Professional Summary &nbsp;
<%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
    <%if(currentNodeProvided){
        if(currentNode.hasNode("ProfessionalSummary")){
        %>
        <a href="javascript:loadContent('#summary','<%=currentNode+'/ProfessionalSummary.summary'%>');"><img alt="Edit" title="Edit" src="<%=request.getContextPath()%>/apps/user/css/images/icon_edit.png" height="14" align="middle"></a></h3>    
        <%}else{%>
        <a href="javascript:loadContent('#summary','<%=currentNode+'/*.summary'%>');"><img alt="Edit" title="Edit" src="<%=request.getContextPath()%>/apps/user/css/images/icon_edit.png" height="14" align="middle"></a></h3>    
    <%}}%>
<%}%>
<ul>
<li>
<span>Skill & Expertise:</span>
				<%if(currentNodeProvided && currentNode.hasNode("ProfessionalSummary") &&currentNode.getNode("ProfessionalSummary").getProperty("skill").getDefinition().isMultiple()  ){%>
					<%	var skillValue=currentNode.getNode("ProfessionalSummary").skill;
						var ratingValue=currentNode.getNode("ProfessionalSummary").level;
						for(i in skillValue)
						{
							if(i!=0)
							{out.println(",");
							}
							out.println(skillValue[i]);%><em>(Rating is <%=ratingValue[i]%>/10)</em><%
						}
					%>
		   			
					<%} else if(currentNodeProvided && currentNode.hasNode("ProfessionalSummary")){ %>
					<%=currentNode.getNode("ProfessionalSummary").skill? currentNode.getNode("ProfessionalSummary").skill: ""%>
						<em>(Rating is <%if(currentNodeProvided && currentNode.hasNode("ProfessionalSummary")){%>
								<%=currentNode.getNode("ProfessionalSummary").level? currentNode.getNode("ProfessionalSummary").level: ""%><%}%>/10)</em>
						<%}%>
</li>
<li>
<span>Professional Experience:</span><%if(currentNodeProvided && currentNode.hasNode("ProfessionalSummary")){%><%=currentNode.getNode("ProfessionalSummary").goals? currentNode.getNode("ProfessionalSummary").goals: ""%><%}%>
</li>
<li>
<span>Specialities:</span><%if(currentNodeProvided && currentNode.hasNode("ProfessionalSummary")){%><%=currentNode.getNode("ProfessionalSummary").specialities? currentNode.getNode("ProfessionalSummary").specialities: ""%><%}%>
</li>
</ul>
</div>
</div>
</ul>
</div>
        
<div class="ui-tabs ui-widget ui-widget-content ui-corner-all" id="ui-tab-common">
  <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
    <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#">Social Network Connect</a></li>
  </ul>
  <div class="ui-tabs-panel ui-widget-content ui-corner-bottom">
    <div class="profile-info-visible">
       <div id="socialConnectId"></div>   
    </div>
  </div>
</div>

<div id="tabs1" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
     <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
         <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
             <a href="#experience" >Experience</a>
         </li>
     </ul>     
    <div id="experience" class="profile-info-visible ui-tabs-panel ui-widget-content ui-corner-bottom">
<%
if(currentNodeProvided   && currentNode.hasNode("Experience")){
var nodes = currentNode.getNode("Experience").getNodes();
        for(i in nodes) { 
%>                      
<form id="<%=nodes[i]%>" method="POST" action="<%=request.getContextPath()%>/content/user/save.deleteEx">
<h3>
 
<%=nodes[i].getNode(nodes[i].childNode).jobTitle ? nodes[i].getNode(nodes[i].childNode).jobTitle : ""%>
<em>at
<%=nodes[i].companyName? nodes[i].companyName : ""%></em>
<%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
    <a  href="javascript:loadContent('#experience','<%=nodes[i]%>.experience');"><img alt="Edit" title="Edit" src="<%=request.getContextPath()%>/apps/user/css/images/icon_edit.png" height="14" align="middle"></a>
    <a href="#"  onclick="submitForm('<%=nodes[i]%>')"><img alt="Remove" title="Remove" src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove.png" height="14" align="middle"></a>
<%}%>
                          <input name="redirect" type="hidden" value="<%=currentNode.getName()%>"/> 
                          <input name="operation" type="hidden" value="<%=nodes[i]%>"/> 
                         
                         </h3>
<ul>

<li>
<span>From:</span><%=nodes[i].getNode(nodes[i].childNode).startDate ? nodes[i].getNode(nodes[i].childNode).startDate : ""%> &nbsp;|&nbsp; <span>To:</span><%=nodes[i].getNode(nodes[i].childNode).endDate ? nodes[i].getNode(nodes[i].childNode).endDate: ""%><%=nodes[i].getNode(nodes[i].childNode).currentlyWork ? nodes[i].getNode(nodes[i].childNode).currentlyWork: ""%>
</li>
<li><span>Work Location:</span><%=nodes[i].companyLocation ? nodes[i].companyLocation : ""%></li>
<li><span>Working as:</span><%=nodes[i].getNode(nodes[i].childNode).jobDesc? nodes[i].getNode(nodes[i].childNode).jobDesc: ""%></li>
</ul>
</form>




<div class="separator"></div>
<%}}%>
    <div  class="profile-info-visible" >
    <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
        <%if(currentNodeProvided){%>
        <a  href="javascript:loadContent('#experience','<%=currentNode%>/experience/*.experience');" class="btn btn-primary">Add Experience</a>
        <%}%>
    <%}%>    
    </div>
</div>  
</div>
       
<div id="tabs2" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
  <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
      <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
        <a href="#education">Education</a>
      </li>
  </ul>
  <div id="education" class="profile-info-visible ui-tabs-panel ui-widget-content ui-corner-bottom">
       <%
         if(currentNodeProvided  && currentNode.hasNode("Education")){
         var nodes = currentNode.getNode("Education").getNodes();
                for(i in nodes) { 
       %>
       <form method="POST" id="<%=nodes[i]%>" action="<%=request.getContextPath()%>/content/user/save.deleteEdu">
            <h3>
                <%=nodes[i].degree? nodes[i].degree: ""%>
                <em>from <%=nodes[i].school ? nodes[i].school : ""%></em>
                <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
                    <a  href="javascript:loadContent('#education','<%=nodes[i]%>.education');"><img alt="Edit" title="Edit" src="<%=request.getContextPath()%>/apps/user/css/images/icon_edit.png" height="14" align="middle"></a>
                    <a href="#"  onclick="submitForm('<%=nodes[i]%>')"><img alt="Remove" title="Remove" src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove.png" height="14" align="middle"></a>
                <%}%>
                <input name="redirect" type="hidden" value="<%=currentNode.getName()%>"/> 
                <input name="operation" type="hidden" value="<%=nodes[i]%>"/> 
            </h3>
            <ul>
                <li>
                    <span>From:</span><%=nodes[i].toDate ? nodes[i].toDate : ""%> &nbsp;|&nbsp; <span>To:</span>
                    <%=nodes[i].endDate ? nodes[i].endDate : ""%>
                </li>
                <li>
                    <span>Grade/Percentage:</span><%=nodes[i].grade ? nodes[i].grade: ""%>
                </li>
            </ul>
        </form>
        <div class="separator"></div>
      <%}}%>
      <div class="profile-info-visible">
           <%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
              <%if(currentNodeProvided){%>
               <a  href="javascript:loadContent('#education','<%=currentNode%>/Education/*.education');" class="btn btn-primary">Add Education</a>
              <%}%>
           <%}%>
      </div>
  </div>
</div>
        
        <div id="tabs3" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
            <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
                
                <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
                    <a href="#info" >Additional Information</a>
                </li>
            </ul>
                   <div id="info" class="profile-info-visible ui-tabs-panel ui-widget-content ui-corner-bottom">
<h3>Additional Info &nbsp;


<%if(request.getRemoteUser().replace("@","_").equals(request.getParameter("id"))){%>
    <%if(currentNodeProvided ){
        if(currentNode.hasNode("AdditionalInformation")){
        %>
        <a  href="javascript:loadContent('#info','<%=currentNode%>/AdditionalInformation.info');"><img alt="Edit" title="Edit" src="<%=request.getContextPath()%>/apps/user/css/images/icon_edit.png" height="14" align="middle"></a>
        <%}else{%>
         <a href="javascript:loadContent('#info','<%=currentNode%>/*.info');"><img alt="Edit" title="Edit" src="<%=request.getContextPath()%>/apps/user/css/images/icon_edit.png" height="14" align="middle"></a>
     <%}}%>
<%}%>
</h3>
<ul>
<li>
<span>Websites:</span><%if(currentNodeProvided  && currentNode.hasNode("AdditionalInformation")){%><%=currentNode.getNode("AdditionalInformation").websites? currentNode.getNode("AdditionalInformation").websites: ""%><%}%>
</li>
<li>
<span>Groups:</span>

	<%	var groupNode;
		if(currentNodeProvided  && currentNode.hasNode("Groups") && currentNode.getNode("Groups").hasNode("GroupsAdded") 
			&& currentNode.getNode("Groups").getNode("GroupsAdded").hasNodes() ){
				groupNode=currentNode.getNode("Groups").getNode("GroupsAdded").getNodes();
				for(i in groupNode){
                        out.println(groupNode[i].getName());
                }
				
		}%>
			

</li>
<li>
<span>Interests:</span><%if(currentNodeProvided  && currentNode.hasNode("AdditionalInformation")){%><%=currentNode.getNode("AdditionalInformation").interests? currentNode.getNode("AdditionalInformation").interests: ""%><%}%>
</li>
<li>
<span>Awards:</span><%if(currentNodeProvided  && currentNode.hasNode("AdditionalInformation")){%><%=currentNode.getNode("AdditionalInformation").honorsAwards? currentNode.getNode("AdditionalInformation").honorsAwards: ""%><%}%>
</li>

</div>
            </ul>

        </div>
      
      
 </div>
 <%if(request.getParameterMap().containsKey("verified")){
 	if(request.getParameter("verified")=='true'){%>
		<div class="alert alert-success navbar-spacer" 	style="display: block; position: fixed; top: 0px; margin: 0px 0.5%;
		  width: 95%; padding: 8px 2%; z-index: 9999; border-radius: 4px 4px 0px 0px;">
		 	Email Address Has Been Verified</div>
<%}else if(request.getParameter("verified")=='mobile'){
%>
		<div class="alert alert-success navbar-spacer" 	style="display: block; position: fixed; top: 0px; margin: 0px 0.5%;
		  width: 95%; padding: 8px 2%; z-index: 9999; border-radius: 4px 4px 0px 0px;">
		 	Mobile Number Has Been Verified</div>

<%}
}%>

<%
function privacy(privacyVar){
var show="true";
if(currentNodeProvided && request.getParameterMap().containsKey("userId")){
    if(privacyVar=='connection' && degree=='first'){
           show="true";
        }else if(privacyVar=='onlyMe'){
                show="false";
        }else if(privacyVar=='everyOne'){
                show="true";
        }else if(privacyVar=='connection' && degree!='first'){
               show="false";
     }
 }
    return show;
}

%>
<div id="canvas"></div>
<div id="importContactId" style="display:none;">
    <iframe src="<%=request.getContextPath()%>/servlet/friend/show.importedContact?userId=<%=currentNode.getName()%>" width="400" height="326" frameborder="0" ></iframe>
</div>