<%
var currentNodeProvided = ((typeof currentNode != "undefined") && currentNode.title != null);
%>
<%load("../user/includes/header.esp");%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/wro/companyEdit.css" />
<script src="<%=request.getContextPath()%>/wro/companyEdit.js" type="text/javascript"></script>
<script>
	$(function() {
			 $( "#tab2" ).tabs();
	});
</script>
<script type="text/javascript">
    function deleteUserMap(node,userId,mapType,v,reject){
        var url='<%=request.getContextPath()%>/servlet/company/save.deleteUserMap';
        var data='userId='+userId+'&mapType='+mapType+'&companyName=<%=currentNodeProvided?currentNode.getName():""%>&nodeName='+node+'&reject='+reject;
        if(confirm('Confirm Delete'))
            {
                $.ajax({
                      type: 'POST',
                      url: url,
                      data: data,
                      success: function(){
                        $(v).parent('li').remove();
                      }
                  }); 
             }else{} 
    }
	function openPopUpCompany(theURL, title, heightInPX, widthInPX,values)
       { var selectedValues;
        var uMatch = navigator.userAgent.match("Chrome");
           if(uMatch=='Chrome'){
          window.open(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
           }else{
            selectedValues= window.showModalDialog(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
           }
    	   
    	   document.getElementById("mappingCompanyTypeID").value=values;
    	    document.getElementById("selectedCompany").value=selectedValues;
    	    document.getElementById("formCompany").submit();
      }
  function openPopUpUser(theURL, title, heightInPX, widthInPX,values)
   {var selectedValues;
   var uMatch = navigator.userAgent.match("Chrome");
           if(uMatch=='Chrome'){
            selectedValues= window.open(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
           }else{
            selectedValues= window.showModalDialog(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
           }
	  
	   document.getElementById("mappingUserTypeID").value=values;
	    document.getElementById("selectedPerson").value=selectedValues;
	    document.getElementById("formUser").submit();
  }
  
  function openPopUpCommon(theURL, title, heightInPX, widthInPX)
   { var selectedValues;
    var uMatch = navigator.userAgent.match("Chrome");
           if(uMatch=='Chrome'){
           selectedValues= window.open(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
           }else{
            selectedValues= window.showModalDialog(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
           }
	  
	   /*document.getElementById("mappingCompanyTypeID").value=values;
	    document.getElementById("selectedCompany").value=selectedValues;
	    document.getElementById("formCompany").submit();*/ 
   }
  function openPopUpProduct(theURL, title, heightInPX, widthInPX,values)
    { var selectedValues;
    var uMatch = navigator.userAgent.match("Chrome");
           if(uMatch=='Chrome'){
         selectedValues= window.open(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
           }else{
           selectedValues= window.showModalDialog(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
           }
     
       document.getElementById("productTypeID").value=selectedValues;
        document.getElementById("nodeNameID").value=values;
        document.getElementById("formProduct").submit(); 
     }
    $(function() {
		$("#tree").treeview({
			collapsed: true,
			animated: "medium",
			control:"#sidetreecontrol",
			persist: "location"
		});
    });
</script>
<script>
	function addBranch(){
	var top_level_div = document.getElementById('branchDiv');
	var count = top_level_div.getElementsByTagName('tbody').length;
	var id='Branch'+(count)
	var newdiv = document.createElement('tbody');
	newdiv.id=id;
		newdiv.innerHTML ="<tr><td colspan='2'><label><u><strong></strong></u></label></td>"+
		 		"<td align='right'><a href='javascript:void(0)' class='btn btn-warning' onclick=\"removeBranch(\'"+id+"\')\" >Remove Branch</a></td></tr>"+
				"<input type='hidden' name='branchName' value='Branch"+(count)+"' />"+
				"<tr><td colspan='3' width='50%' ><label>Address Type :</label><input type='text' name='addressType' /></td></tr>"+
				 "<tr><td width='33%' ><label>Address :</label><input type='text' name='address' /></td>"+
				 "<td width='33%' ><label>City :</label><input type='text' name='city' value='' /></td>"+
				 "<td width='33%' ><label>Postal Code :</label><input type='text' name='postalCode'  /></td></tr>"+
				 "<tr><td width='33%' ><label>Land Line Number :</label><input type='text' name='workNumber' /></td>"+
				 "<td width='33%' ><label>Mobile Number :</label><input type='text' name='mobileNumber' /></td>"+
				 "<td width='33%' ><label>Contact Email ID :</label><input type='text' name='contactEmailId' /></td></tr>";
				 
		
         
          document.getElementById("branchDiv").appendChild(newdiv);
	
	}
	function removeBranch(divName){
	

	var d = document.getElementById('branchDiv');
	  var olddiv = document.getElementById(divName);
	  d.removeChild(olddiv);
	}

</script>
<script>
	var counts;
	function addRow(tableID) {
		
		
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		counts = rowCount - 1;
		

		
		var cell2 = row.insertCell(0);	
		var rate = document.createElement("input");
		rate.type = "text";
		rate.name = "contactTypeCompany";
		rate.setAttribute("class", "");
		rate.id = "jsp_" + counts
		+ "__rate";
		cell2.appendChild(rate);
		
		
		var cell3 = row.insertCell(1);
		var rate = document.createElement("input");
		rate.type = "text";
		rate.name = "nameContactTypeCompany";
		rate.setAttribute("class", "");
		rate.id = "jsp_" + counts
		+ "__rate";
		cell3.appendChild(rate);
		
		var cell4 = row.insertCell(2);
		var rate = document.createElement("input");
		rate.type = "text";
		rate.name = "contactContactTypeCompany";
		rate.setAttribute("class", "");
		rate.id = "jsp_" + counts
		+ "__rate";
		cell4.appendChild(rate);
		
		var cell1 = row.insertCell(3);
		var skill = document.createElement("input");
		skill.type = "text";
		skill.name = "detailContactTypeCompany";
		skill.setAttribute("class", "");
		skill.id = "jsp_" + counts
		+ "__rate";
		cell1.appendChild(skill);
	}

	function deleteRow(guestList) {
		try {
			var table = document.getElementById(guestList);
			var rowCount = table.rows.length;
			if (rowCount > 1) {
				table.deleteRow(rowCount - 1);
			}
		} catch (e) {
			alert(e);
		}
	}
	
	function showNhide(show,hide){
	
		document.getElementById(show).style.display="block";
		document.getElementById(hide).style.display="none";
	}

function CheckForm(){

var a=document.getElementsByName("companyName")[0].value;

var b=document.getElementsByName("website")[0].value;
var c=document.getElementsByName("industries")[0].value;
var d=document.getElementsByName("description")[0].value;
var e=document.getElementsByName("employeesNum")[0].value;
var g=document.getElementsByName("type")[0].value;
var h=document.getElementsByName("turnOver")[0].value;
var i=document.getElementsByName("founded")[0].value;
var j=document.getElementsByName("stateSalesTax")[0].value;
var k=document.getElementsByName("centralSalesTax")[0].value;
var l=document.getElementsByName("panNumber")[0].value;
var m=document.getElementsByName("BSENumber")[0].value;
var n=document.getElementsByName("creatorEmailId")[0].value;
var o=document.getElementsByName("creatorTitle")[0].value;
var p=document.getElementsByName("creatorjoiningDate")[0].value;
var q=document.getElementsByName("addressType")[0].value;
var r=document.getElementsByName("address")[0].value;
var s=document.getElementsByName("city")[0].value;
var t=document.getElementsByName("postalCode")[0].value;//should be number
var u=document.getElementsByName("workNumber")[0].value;//should be number
var v=document.getElementsByName("mobileNumber")[0].value;//should be number
var w=document.getElementsByName("contactEmailId")[0].value;//should bevalid email id validation required
var x=document.getElementsByName("contactTypeCompany")[0].value;
var y=document.getElementsByName("nameContactTypeCompany")[0].value;//
var z=document.getElementsByName("contactContactTypeCompany")[0].value;
var aa=document.getElementsByName("detailContactTypeCompany")[0].value;

if(a.length>0 & b.length>0 & c.length>0 & d.length>0 & e.length>0 & g.length>0 & h.length>0){
	if(i.length>0 & j.length>0 & k.length>0 & l.length> 0 & m.length>0 & n.length> 0 & o.length>0 & p.length>0){
			if(q.length>0 & r.length>0 & s.length>0 & t.length> 0 & u.length>0 & v.length> 0 & w.length>0 & x.length>0){
					if(y.length>0 & z.length>0 & aa.length>0){
									
									document.getElementById("companyadding").submit();
								
									}
								else
								{
							alert("Please fill complete form !");
							}	
					
					}
					else
				{
				alert("Please fill complete form !");
				}	
			}
		else
		{
		alert("Please fill complete form !");
	}	
}else{
		alert("Please fill complete form !");
	}
}
</script>
<%load("../user/includes/menu.esp");%>
 
<%
var basicInfoNode;
if(currentNodeProvided && currentNode.hasNode("BasicInfo")){
basicInfoNode=currentNode.getNode("BasicInfo");
}

%>

 <div  class="section-left">           
	<fieldset class="row-fluid" id="userProfilePrimaryData">
		<div class="profile-user-thumb">
			<% if( currentNodeProvided && basicInfoNode.hasProperty("companyLogo")){
               %>
               <img src="<%=basicInfoNode.companyLogo?basicInfoNode.companyLogo:request.getContextPath()+"/apps/user/css/images/photo.gif"%>" height="165px" width="125px" />
               <%}
               else{  %>
             <img src="<%=request.getContextPath()%>/apps/user/css/images/photo.gif" /> 
             <%}%>
	         <%if(currentNodeProvided){%>                
				<div align="center"><a rel="facebox" href="<%=request.getContextPath()%>/content/company/*.uploadLogo?compN=<%=request.getParameter("id")%>&type=companyLogo" class="btn btn-primary">Change Picture</a></div>
			<%}%>
			</div>
			
			<!--
			<table>
			 <tr><td><br /><a href="#"><b>< Major Export ></b></a></td></tr>
			<tr><td><a href="#"><b>< Major Import ></b></a></td></tr>
			<tr><td><a href="#"><b>< Major Purchase ></b></a></td></tr>
			<tr><td><a href="#"><b>< Major Sell ></b></a></td></tr>
			<tr><td><a href="#"><b>< Major Raw Material ></b></a></td></tr>
			<tr><td><a href="#"><b>< Major Capital Equipment Used ></b></a></td></tr>
			<tr><td><a href="#"><b>< Inquiries ></b></a></td></tr> 
			
		
			<%if(currentNodeProvided){%>
			<tr><td><a href="<%=request.getContextPath()%>/servlet/company/show.showCareer?compN=<%=currentNode.getName()%>">
									<b>< Career ></b></a></td></tr>
			<%}else{%>
			<tr><td><a href="#"><b>< Career ></b></a></td></tr>
			
			<%}%>
			
			
			</table>
			-->
		
			

<div class="profile-info-visible" style="width:77%;">
 
<div>
<%if(currentNodeProvided){%>
 	<a href="#" onclick="showNhide('editForm','relationship')" class="btn btn-info">Edit Company Profile</a>
 	<a href="#" onclick="showNhide('relationship','editForm')"  class="btn btn-info">Edit Company Relationship</a>
<%}%>
 </div>	 <br>
 	
 	
 
 	<div id="editForm" >
		<form method="POST" action="<%=request.getContextPath()%>/servlet/company/save.com" id="companyadding">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="form">
		
		 <tr>
		
		 <td width="33%"  ><label>Company Name </label><input type="text" name="companyName" 
		 								<%if(currentNodeProvided){%>
		 									value="<%=basicInfoNode.companyName?basicInfoNode.companyName:""%>" /></td>
		 								<%}else{%>
		 										value="" /></td>
		 								<%}%>
		 									
		 <td width="33%" ><label>WebSite URL </label><input type="text" name="website" 
		 							<%if(currentNodeProvided){%>
		 							value="<%=basicInfoNode.website?basicInfoNode.website:""%>"
		 							<%}else{%>
		 								value=""
		 							<%}%>
		 							 /></td>
		 							 
		  <td width="33%" ><label>Industries </label><input type="text" name="industries" 
									 <%if(currentNodeProvided){%>
		 								value="<%=basicInfoNode.industries?basicInfoNode.industries:""%>" 
		 								<%}else{%>
		 								value=""
		 								<%}%>
		 								
		 								/></td></tr>
		 															 
		 <tr><td colspan="3" ><label>Description </label><textarea style="height:150px;" name="description"><%if(currentNodeProvided){%><%=basicInfoNode.description?basicInfoNode.description:""%><%}%></textarea></td></tr>
		
		 <tr><td width="33%" ><label>No. of Employee </label><input type="text" name="employeesNum"
		 						<%if(currentNodeProvided){%> 
		 							value="<%=basicInfoNode.employeesNum?basicInfoNode.employeesNum:""%>" 
		 						<%}else{%>
		 								value=""
		 							<%}%>
		 						/></td>
		 						
		 						
		<td width="33%" ><label>Type </label><input type="text" name="type" 
		 								<%if(currentNodeProvided){%>
		 									value="<%=basicInfoNode.type?basicInfoNode.type:""%>" />
		 								<%}else{%>
		 									value="" />
		 								<%}%>
		 								</td>
		 <td width="33%" ><label>Yearly TurnOver</label><input type="text" name="turnOver" 
		 							  <%if(currentNodeProvided){%>
		 								value="<%=basicInfoNode.turnOver?basicInfoNode.turnOver:""%>"
		 								<%}else{%>
		 								value=""
		 							<%}%>
		 								
		 								 /></td></tr>
		 <tr><td width="33%" ><label>Founded </label><input type="text" name="founded" 
		 							  <%if(currentNodeProvided){%>
		 								value="<%=basicInfoNode.founded?basicInfoNode.founded:""%>" 
		 								<%}else{%>
		 								value=""
		 								<%}%>
		 								/></td>
		 	<td width="33%" ><label>State Sales Tax Number</label><input type="text" name="stateSalesTax" 
		 							  <%if(currentNodeProvided){%>
		 								value="<%=basicInfoNode.stateSalesTax?basicInfoNode.stateSalesTax:""%>" 
		 								<%}else{%>
		 								value=""
		 								<%}%>
		 								/></td>
		 	<td width="33%" ><label>Central Sales Tax Number</label><input type="text" name="centralSalesTax" 
		 							  <%if(currentNodeProvided){%>
		 								value="<%=basicInfoNode.centralSalesTax?basicInfoNode.centralSalesTax:""%>" 
		 								<%}else{%>
		 								value=""
		 								<%}%>
		 								/></td></tr>
		 	<tr><td width="33%" ><label>PAN No.</label><input type="text" name="panNumber" 
		 							  <%if(currentNodeProvided){%>
		 								value="<%=basicInfoNode.panNumber?basicInfoNode.panNumber:""%>" 
		 								<%}else{%>
		 								value=""
		 								<%}%>
		 								/></td>
		 		<td width="33%" ><label>BSE No.</label><input type="text" name="BSENumber" 
                                      <%if(currentNodeProvided){%>
                                        value="<%=basicInfoNode.BSENumber?basicInfoNode.BSENumber:""%>" 
                                        <%}else{%>
                                        value=""
                                        <%}%>
                                        /></td>
		 		<td width="33%" ><!--<label>PAN Card </label> -->
		 		<%if(currentNodeProvided){%>
		 		<label class="note"><a rel="facebox" href="<%=request.getContextPath()%>/content/company/*.uploadLogo?compN=<%=request.getParameter("id")%>&type=panCard">Click to attach the Copy of PAN Card</a></label>
		 		<%}%>
		 		</td>
		      </tr>
		<tr><td width="33%" ><label>Creator Email Id</label>
		<input type="text" name="creatorEmailId" disabled="true"  value="<%=request.getRemoteUser()%>" />
        </td>
		<td width="33%" ><label>Your Title </label><input type="text" name="creatorTitle"
								 <%if(currentNodeProvided){%>
									value="<%=basicInfoNode.creatorTitle?basicInfoNode.creatorTitle:""%>"
								<%}else{%>
		 								value=""
		 							<%}%>	
									
									/></td>
		<td width="33%" ><label>Joining Date </label><input type="text" name="creatorjoiningDate"
									<%if(currentNodeProvided){%> 
									value="<%=basicInfoNode.creatorjoiningDate?basicInfoNode.creatorjoiningDate:""%>"
									<%}else{%>
		 								value=""
		 							<%}%>
									/></td></tr>
		<tr><td colspan="3">
		                  <div class="separator"></div></td></tr>
		</table>
		<table width="100%" id="branchDiv" border="0" cellspacing="0" cellpadding="0" class="form">

		<% 	  if(currentNodeProvided){
			 		var addressNode;
				 	if(currentNode.hasNode("Address") && currentNode.getNode("Address").hasNodes()){
				 		addressNode=currentNode.getNode("Address").getNodes();
				 		%>
				 		
				 		
			 				
				 		<%
				 		for(i in addressNode){
				 		%><tbody id="<%=addressNode[i].branchName?addressNode[i].branchName:""%>">
				 			<tr><td colspan="2"><label><u><strong><%=addressNode[i].addressType?addressNode[i].addressType:""%></strong></u></label></td>
		 						<td align="right"><%
		 						
			 						if(addressNode[i].getProperty("branchName").getString()=='HeadQuater'){
									%>
										<a href="javascript:void(0)" class="btn btn-info" onclick="addBranch()">Add Branch</a>
									<%
									}else if(addressNode[i].getProperty("branchName").getString()!='HeadQuater'){
									%>
									<a href='javascript:void(0)' class="btn btn-warning" onclick="removeBranch('<%=addressNode[i].branchName?addressNode[i].branchName:""%>')" >Remove Branch</a>
									
									<%
									
								}%>
								</td>
		 					</tr>
				 			
				 			 <tr><td colspan="3"><input type="hidden" name="branchName"
				 			 			 value="<%=addressNode[i].branchName?addressNode[i].branchName:""%>" /></td></tr>
				 			 <tr><td colspan="3" width="50%" ><label>Address Type: </label><input type="text" name="addressType"
				 			 		 value="<%=addressNode[i].addressType?addressNode[i].addressType:""%>" /></td></tr>
							 <tr><td width="33%" ><label>Address </label><input type="text" name="address" 
							 			value="<%=addressNode[i].branchAddress?addressNode[i].branchAddress:""%>" /></td>
							 	 <td width="33%" ><label>City :</label><input type="text" name="city" 
							 				value="<%=addressNode[i].branchCity?addressNode[i].branchCity:""%>" /></td>
							 	<td width="33%" ><label>Postal Code :</label><input type="text" name="postalCode" 
							 				value="<%=addressNode[i].branchPostalCode?addressNode[i].branchPostalCode:""%>" /></td></tr>
							 <tr><td width="33%" ><label>Land Line Number :</label><input type="text" name="workNumber" 
							 				value="<%=addressNode[i].branchWorkNumber?addressNode[i].branchWorkNumber:""%>"/></td>
							 	<td width="33%" ><label>Mobile Number :</label><input name="mobileNumber" type="text" 
							 				value="<%=addressNode[i].branchMobileNumber?addressNode[i].branchMobileNumber:""%>" /></td>
								<td width="33%" ><label>Contact Email Id :</label><input type="text" name="contactEmailId" 
							 				value="<%=addressNode[i].branchContactEmailId?addressNode[i].branchContactEmailId:""%>" /></td></tr>
							 				
							
							</tbody>
							
							 
				 		
				 		<%	
				 		}%>
				 
				 		<%	
				 	}
			 		
			 		
			 		
			 	}else{
			 	%>
			 		<tbody id="HeadQuater">
			 		<tr><td colspan="2"><label><u><strong>HeadQuater</strong></u></label></td>
		 						<td align="right"><a href="javascript:void(0)" class="btn btn-info" onclick="addBranch()">Add Branch</a></td></tr>
				 	 <tr><td colspan="3"><input type="hidden" name="branchName" value="HeadQuater" /></td></tr>
				 	 <tr><td colspan="3" width="50%" ><label>Address Type: </label><input type="text" name="addressType" value="" /></td></tr>
					<tr><td width="33%" ><label>Address :</label><input type="text" name="address" value="" /></td>
						<td width="33%" ><label>City :</label><input type="text" name="city" value="" /></td>
					 	<td width="33%" ><label>Postal Code :</label><input type="text" name="postalCode" value="" /></td></tr>
					<tr><td width="33%" ><label>Land Line Number :</label><input type="text" name="workNumber" value=""/></td>
					 	<td width="33%" ><label>Mobile Number :</label><input name="mobileNumber" type="text" value="" /></td>
					 	<td width="33%" ><label>Contact Email Id :</label><input type="text" name="contactEmailId" value="" /></td></tr>
					 
					</tbody>
				  	
			 	<%
			 	}
		 %>
		 
	
		</table>
		<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="form">
		 <tr><td colspan="5">
		           <div class="separator"></div> 
		
		<tr><td colspan="3"><label><u>Point Of Contact</u></label><br /></td>
										<td align="right"><a id="profileEdit" onclick="addRow('contactTypeList')" href="javascript:void(0)">
		                                                            <img height="20" align="middle" src="/sling/apps/user/css/images/icon_add_2.png" title="Add Row" alt="ADD">
		                                                        </a>
		                                                        <a id="profileEdit" onclick="deleteRow('contactTypeList')" href="javascript:void(0)">
		                                                            <img height="20" align="middle" src="/sling/apps/user/css/images/icon_remove_2.png" title="Delete Row" alt="Delete">
		                                                        </a>
		                                  </td>
		 </tr>
		
		 <tr>
		 		<td width="20%" ><label>Contact For:</label></td>
		 		<td width="20%" ><label>Name</label></td>
		 		<td width="20%" ><label>Contact</label></td>
		 		<td width="20%" ><label>Contact Details</label></td>
		 		
		<tbody id="contactTypeList" >
		  <%
		             var contactNode;                                   
		          if( currentNodeProvided && currentNode.hasNode("PointOfContact") && currentNode.getNode("PointOfContact").hasNodes() ){
		          										contactNode=currentNode.getNode("PointOfContact").getNodes();
		                                                for(i in contactNode)
		                                                {%>
		                                               
		                                                  <tr>
		                                                      <td><input type="text" name="contactTypeCompany" 
		                                                      			value="<%=contactNode[i].contactTypeCompany?contactNode[i].contactTypeCompany:""%>"/></td>
															<td><input type="text" name="nameContactTypeCompany" 
																	value="<%=contactNode[i].nameContactTypeCompany?contactNode[i].nameContactTypeCompany:""%>"/></td>
															<td><input type="text" name="contactContactTypeCompany" 
																		value="<%=contactNode[i].contactContactTypeCompany?contactNode[i].contactContactTypeCompany:""%>"/></td>
															<td><input type="text" name="detailContactTypeCompany" 
																		value="<%=contactNode[i].detailContactTypeCompany?contactNode[i].detailContactTypeCompany:""%>"/></td>
		
		                                                       <% if(i==0)
		                                                        {
		                                                        %>
		                                                        <td><a id="profileEdit" onclick="addRow('contactTypeList')" href="javascript:void(0)">
		                                                            <img height="20" align="middle" src="/sling/apps/user/css/images/icon_add_2.png" title="Add Row" alt="ADD">
		                                                        </a>
		                                                        <a id="profileEdit" onclick="deleteRow('contactTypeList')" href="javascript:void(0)">
		                                                            <img height="20" align="middle" src="/sling/apps/user/css/images/icon_remove_2.png" title="Delete Row" alt="Delete">
		                                                        </a>
		                                                        </td>
		
		                                                    
		                                                        
		                                                        <%} %>
		                                                      </tr>
		                                                <%}}
		                                                else{ %>
		
		                                                <tr>
		
		                                                    <td><input type="text" name="contactTypeCompany" placeholder="SELL/PURCHASE/OTHER" value=""/></td>
															<td><input type="text" name="nameContactTypeCompany" value=""/></td>
															<td><input type="text" name="contactContactTypeCompany" value=""/></td>
															<td><input type="text" name="detailContactTypeCompany" value=""/></td>
		
		                                                   
		
		                                                   
		     
		                                                </tr>
		                                                <%}%>
		</tbody>
		</tr>
		
		 <tr>
		   <td width="150px">
		       <%if(!request.getRemoteUser().equals("anonymous")){%>
		          <input type="button" value="Save" class="btn btn-primary" style="font-size: 12px !important;" onclick="CheckForm()"/>
		       <%}%>
		       <%if(currentNodeProvided){%>
		          <a class="btn btn-danger" style="font-size: 12px !important;"
		              href="<%=request.getContextPath()%>/servlet/company/show.view?compN=<%=currentNode.getName()%>" >Cancel</a>
		       <%}%>
		   </td>
		 </tr>
	   </table>
     </form>
	</div>

	
	<div id="relationship" class="content" style="display:none;" > 
	<%if(currentNodeProvided){%>
      <h2>Edit Relationship</h2><br><br>
   
      <ul class="easyui-tree"  data-options="animate:true,state:'closed'">  
      		<li>  
                <span>With Other Company   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                				<a class="btn btn-primary" onclick="openPopUpCommon('<%=request.getContextPath()%>/content/company/<%=request.getParameter("id")%>.mapType?mapType=company&compN=<%=request.getParameter("id")%>', 'Map CompanyType', 200, 300)" 
                    					href="javascript:void(0)">ADD</a> </span>
                
                
                <ul>  
                    
                   <% 
					    if(currentNode.hasNode("Relationship")){
					    var relationNode=currentNode.getNode("Relationship");
     
                    	if(relationNode.hasNode("Company")){
                    		var relationTypeNode=relationNode.getNode("Company").getNodes();
                    		for(i in relationTypeNode){
                    		%>
                    		<li data-options="state:'closed'">  
                    			<span><%=relationTypeNode[i].getName()%>   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    						<a onclick="openPopUpCompany('<%=request.getContextPath()%>/servlet/common/search.company?keywords=', 'Search Company', 800, 1000,'<%=relationTypeNode[i].getName()%>')" 
                    															href="javascript:void(0)"><img height="14" align="middle" 
                    																src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png"
                    																 title="Add Row" alt="ADD" /></a></span>
                    			<ul>	
                    		
                    		<%	var relationCompanyNode=relationTypeNode[i].getNodes();
                    			for(j in relationCompanyNode){
                    			%>
		                    			<li>
		                    			<a href="<%=request.getContextPath()%>/servlet/company/show.view?compN=<%=relationCompanyNode[j].getName()%>"><%=relationCompanyNode[j].getName()%></a>
		                    			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    			<a href="<%=request.getContextPath()%>/servlet/company/show.deleteMap?nodeName=<%=relationCompanyNode[j]%>&compN=<%=request.getParameter("id")%>">
		                    			<img height="14" align="middle"
		                    						 src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png"
		                    						  title="Delete Row" alt="Delete" />
		                    			</a>
		                    			</li>
                    			<%
                    			 }%>
                    			</ul>
                    		  </li>	
                    		<%}
                    	}}
                    
                    %>
                         
                         
                    
                </ul>  
            </li> 
            <li>  
                <span>Agent/Employer Etc.   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                					<a class="btn btn-primary" onclick="openPopUpCommon('<%=request.getContextPath()%>/content/company/<%=request.getParameter("id")%>.mapType?mapType=user&compN=<%=request.getParameter("id")%>', 'Map UserType', 200, 300)" 
                    					href="javascript:void(0)">ADD</a> </span>
                
                <ul>  
                    
                   <%
                   		 
				      	if(currentNode.hasNode("Relationship")){
				      	var relationNode=currentNode.getNode("Relationship");
     
                    	if(relationNode.hasNode("User")){
                    		var relationTypeNode=relationNode.getNode("User").getNodes();
                    		for(i in relationTypeNode){
                    		%>
                    		<li data-options="state:'closed'">  
                    			<span><%=relationTypeNode[i].getName()%>    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    						<a onclick="openPopUpUser('<%=request.getContextPath()%>/servlet/common/search.profile?keywords=&userId=newUserId', 'Search User', 800, 1000,'<%=relationTypeNode[i].getName()%>')" 
                    															href="javascript:void(0)"><img height="14" align="middle" 
                    																src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png"
                    																 title="Add Row" alt="ADD" /></a></span>
                    			<ul>	
                    		
                    		<%	var relationUserNode=relationTypeNode[i].getNodes();
                    			for(j in relationUserNode){
                    			%>
		                    			<li>
		                    			<a href="<%=request.getContextPath()%>/content/user/info?id=<%=relationUserNode[j].getName()%>">
		                    			<% var userNode=currentNode.session.getRootNode().getNode("content/user/").getNode(relationUserNode[j].getName());%>
		                    				<%=userNode.name%> <%=userNode.lastName%></a>
		                    			<%if(relationUserNode[j].hasProperty("rejected") && relationUserNode[j].rejected=='true'){%>
		                    			       (Rejected By User)
		                    			<%}%>
		                    			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    			<a href="javascript:void(0);" onclick="deleteUserMap('<%=relationUserNode[j]%>',
		                    			                                                     '<%=userNode.getName()%>',
		                    			                                          '<%=relationTypeNode[i].getName()%>',this,
		                    			                                          '<%=relationUserNode[j].rejected%>')">
		                    			<img height="14" align="middle"
		                    						 src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png"
		                    						  title="Delete Row" alt="Delete" />
		                    			</a>
		                    			</li>
                    			<%
                    			 }%>
                    			</ul>
                    		  </li>	
                    		<%}
                    	}}
                    
                    %>
                         
                         
                    
                </ul>  
            </li> 
            <li>  
                <span>Products   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a class="btn btn-primary" onclick="openPopUpCommon('<%=request.getContextPath()%>/content/company/<%=request.getParameter("id")%>.mapType?mapType=product&compN=<%=request.getParameter("id")%>', 'Map ProductType', 200, 300)" 
                                        href="javascript:void(0)">ADD</a> </span>                
                <ul>  
                    
                   <%
                         
                        if(currentNode.hasNode("Relationship")){
                        var relationNode=currentNode.getNode("Relationship");
     
                        if(relationNode.hasNode("Product")){
                            var relationTypeNode=relationNode.getNode("Product").getNodes();
                            for(i in relationTypeNode){
                            %>
                            <li data-options="state:'closed'">  
                                <span><%=relationTypeNode[i].getName()%>    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a onclick="openPopUpProduct('<%=request.getContextPath()%>/content/category.searchfn', 'Product Mapping', 800, 1000,'<%=relationTypeNode[i].getName()%>')" 
                                                                                href="javascript:void(0)"><img height="14" align="middle" 
                                                                                    src="<%=request.getContextPath()%>/apps/user/css/images/icon_add_2.png"
                                                                                     title="Add Row" alt="ADD" /></a></span>
                                <ul>    
                            
                            <%  var relationProductNode=relationTypeNode[i].getNodes();
                                for(j in relationProductNode){
                                %>
                                        <li>
                                       
                                        <% var productNode=currentNode.session.getNode(relationProductNode[j].productPath);%>
                                        <%=productNode.name%>
                                        
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <a href="<%=request.getContextPath()%>/servlet/company/show.deleteMap?nodeName=<%=relationProductNode[j]%>&compN=<%=request.getParameter("id")%>">
                                        <img height="14" align="middle"
                                                     src="<%=request.getContextPath()%>/apps/user/css/images/icon_remove_2.png"
                                                      title="Delete Row" alt="Delete" />
                                        </a>
                                        </li>
                           <%}%></ul>
                           <%
                             }}}%>
               </ul> 
            </li>
        </ul>
	<%}%>
   </div>
	
  </div>
 </fieldset>
	
</div>
<form method="POST" id="formCompany" action="<%=request.getContextPath()%>/servlet/company/save.mapCompany">
<input type="hidden" id="mappingCompanyTypeID" name="mappingCompanyType"  />
<input type="hidden" id="selectedCompany" name="mappingName"  />
<input type="hidden" name="mapCompanyName"  value="<%=request.getParameter("id")%>" />		
</form>
<form method="POST" id="formUser" action="<%=request.getContextPath()%>/servlet/company/save.mapUser">
<input type="hidden" id="mappingUserTypeID" name="mappingUserType"  />
<input type="hidden" id="selectedPerson" name="mapCompanyPerson"  />
<input type="hidden" name="mapCompanyName"  value="<%=request.getParameter("id")%>" />		
</form>
<form method="POST" id="formProduct" action="<%=request.getContextPath()%>/servlet/company/save.mapProduct">
<input type="hidden" id="productTypeID" name="productValues" />
<input type="hidden" id="nodeNameID" name="nodeName"  />
<input type="hidden" name="productCompanyName"  value="<%=request.getParameter("id")%>" />      
</form>