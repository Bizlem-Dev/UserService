

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
