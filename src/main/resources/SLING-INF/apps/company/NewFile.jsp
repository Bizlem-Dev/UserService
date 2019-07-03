<!DOCTYPE html>
<%
	var currentNodeProvided = (typeof currentNode != "undefined");
var user=currentNode.session.getRootNode().getNode("content/user/");
/* Following are privacy variables  */
var contactPrivacy="";
var imagesPrivacy=""
var videoPrivacy="";
var productPrivacy="";
var userRelationPrivacy="";
var compRelationPrivacy="";
var comDocsPrivacy="";
var comNewsPrivacy="";
/*  */
var connectionCom = "false";
if(currentNodeProvided && request.getRemoteUser()!='anonymous'){
    if(currentNode.hasNode("Privacy")){
        var privacyNode = currentNode.getNode("Privacy");
         contactPrivacy=privacyNode.contactPrivacy?privacyNode.contactPrivacy:"";
         imagesPrivacy=privacyNode.imagesPrivacy?privacyNode.imagesPrivacy:"";
         videoPrivacy=privacyNode.videoPrivacy?privacyNode.videoPrivacy:"";
         productPrivacy=privacyNode.productPrivacy?privacyNode.productPrivacy:"";
         userRelationPrivacy=privacyNode.userRelationPrivacy?privacyNode.userRelationPrivacy:"";
         compRelationPrivacy=privacyNode.compRelationPrivacy?privacyNode.compRelationPrivacy:"";
         comDocsPrivacy=privacyNode.comDocsPrivacy?privacyNode.comDocsPrivacy:"";
         comNewsPrivacy=privacyNode.comNewsPrivacy?privacyNode.comNewsPrivacy:"";
    }
}
%>
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/wro/companyView.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/wro/companyView.css"
	type="text/css" />
<script type="text/javascript">      
    $(document).ready(function() {
        $('.tTip').tinyTips('light', '<%=request.getContextPath()%>');         
    });
</script>
<script>
    $(function() {
        $('#contentFeedbackFrame').load("<%=request.getContextPath()%>/servlet/"+
                "feedback/content.show?contentId=<%=currentNode.getIdentifier()%>"+
                "&userId=<%=request.getRemoteUser().replace("@","_")%>&content=YYYYYYYY");
                
        $('#socialConnectId').load("<%=request.getContextPath()%>/servlet/company/"+
                    "show.companySocial?company=<%=currentNode.getName()%>");                 
        
        $("#iframe_close").click(function(){
            $(this).parent().parent().hide();
        });
    });
    $(function(){
        $(".content-sub-tab > li:first-child").addClass("selected");
        $("#relationDivId").children('div:first').show();
        $("#relationDivId").children('div:first').children('ul').children('li:first-child').addClass("selected");
        $("#relationDivId").children('div:first').children('div:first').show();
        
    });
    (function($){
        $(window).load(function(){
            $(".content-scroll").mCustomScrollbar({
                scrollButtons:{
                    enable:true
                }
            });
            $("#docId").mCustomScrollbar({
                scrollButtons:{
                    enable:true
                }
            });
            $("#newsId").mCustomScrollbar({
                scrollButtons:{
                    enable:true
                }
            });
            $("#carousel").mCustomScrollbar({
                 horizontalScroll:true,
                 scrollButtons:{
                    enable:true                
                 }
            });
            $("#videoScrollId").mCustomScrollbar({
                 horizontalScroll:true,
                 scrollButtons:{
                    enable:true                
                 }
            });
            
        });
    })(jQuery);
    
    function downloadFile(filePath){
        $("#downloadId").val(filePath);
        $("#downFormId").submit();
    }
    function getDelete(){alert("haha");
    	 var choice = confirm("Are you sure you want to delete this group !");

    	    if (choice) {
    	    	document.getElementById('delcom').href="/servlet/company/show.com?id="<%=request.getParameter("compN")%>;
    	    	$.ajax({
    	            url:'<%=request.getContextPath()%>/servlet/company/show.deleteCompany',
    	            type:'POST',
    	            data:'company=<%=currentNode.getName()%>',
    	            success:function(data){
    	            	alert(data);
    	                if(data=="true"){
    	                	window.location.href ="<%=request.getContextPath()%>/servlet/common/search.company?keywords=&paramKey=true";
    	                }else {
    	                alert("Groupt is not deleted .");  
    	                  }
    	                }
    	               	            
    	        });
    	    }
    }
    function getStock(){
        $.ajax({
            url:'<%=request.getContextPath()%>/servlet/company/show.getStock',
            type:'POST',
            data:'company=<%=currentNode.getName()%>',
            success:function(data){
                if(data!='null'){
                  var obj = JSON.parse(data);
                  if(obj.variation=='neg'){
                  var response="";
                     response = "<div class='bse-stock-container'><div class='bse-stock-time' >"
                        +"<div class='bse-stock-status' >LIVE</div><div class='bse-stock-mark' >"
                        +"<strong >BSE</strong></div><span class='bse-stock-date' >"+obj.time
                        +"</span></div><div  class='bse-stock-rate'><strong>"+obj.rate+"</strong>"
                        +"<div  class='bse-stock-rate-diffrence'><strong>"+obj.change+"</strong>"
                        +"("+obj.changeP+"%)</div></div></div>";
                  
                  }else if(obj.variation=='pos'){
                       response = "<div class='bse-stock-container'><div class='bse-stock-time' >"
                        +"<div class='bse-stock-status' >LIVE</div><div class='bse-stock-mark' >"
                        +"<strong >BSE</strong></div><span class='bse-stock-date' >"+obj.time
                        +"</span></div><div  class='bse-stock-rate green'><strong>"+obj.rate+"</strong>"
                        +"<div  class='bse-stock-rate-diffrence green'><strong>"+obj.change+"</strong>"
                        +"("+obj.changeP+"%)</div></div></div>";
                  
                  }
                   $("#shareId").html(response);
                }
             
            }
        });
    }
    getStock();
    function showRel(id,obj){
        $(".content-sub-link-data").hide();
        $(".content-sub-link").children("li").removeClass("selected");
        $(obj).parent('li').addClass("selected");
       document.getElementById(id).style.display='block';
    }
    
    function showRelMain(value){
       $(".content-sub-tab > li").removeClass("selected");
       $(".content-sub-link-data").hide();
       $(".content-sub-link").children("li").removeClass("selected");
       $("#"+value+"").addClass("selected");
       $(".dummy_class").hide();
        if(value=='product'){
            $("#productId").show();
            $("#productId").children('ul').children('li:first-child').addClass("selected");
            var text1 = $("#productId").children('ul').children('li:first-child').children('a').text().replace(/ /g,"");
            $("#p"+text1+"").show();
        }else if(value=='person'){
            $("#personId").show();
            $("#personId").children('ul').children('li:first-child').addClass("selected");
            var text2 = $("#personId").children('ul').children('li:first-child').children('a').text().replace(/ /g,"");
            $("#u"+text2+"").show();
        }else if(value=='company'){
            $("#companyId").show();
            $("#companyId").children('ul').children('li:first-child').addClass("selected");
            var text3 = $("#companyId").children('ul').children('li:first-child').children('a').text().replace(/ /g,"");
            $("#c"+text3+"").show();
        }
        
    }
</script>
<script type="text/javascript">
function openPopUpCompany(theURL, title, heightInPX, widthInPX,values)
   {
	   var selectedValues= window.showModalDialog(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
	   document.getElementById("mappingCompanyTypeID").value=values;
	    document.getElementById("selectedCompany").value=selectedValues;
	    document.getElementById("formCompany").submit();
  }
  function openPopUpUser(theURL, title, heightInPX, widthInPX,values)
   {
	   var selectedValues= window.showModalDialog(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
	   document.getElementById("mappingUserTypeID").value=values;
	    document.getElementById("selectedPerson").value=selectedValues;
	    document.getElementById("formUser").submit();
  }
  
  function openPopUpCommon(theURL, title, heightInPX, widthInPX)
   {
	   var selectedValues= window.showModalDialog(theURL, title, 'dialogHeight=' + heightInPX + 'px;dialogWidth='+ widthInPX + 'px');
	  /* document.getElementById("mappingCompanyTypeID").value=values;
	    document.getElementById("selectedCompany").value=selectedValues;
	    document.getElementById("formCompany").submit(); */
  }
  function accpetRequest(userId,mapType){
    var data='mapCompanyPerson='+userId+'&mappingUserType='+mapType+
                '&mapCompanyName=<%=request.getParameter("compN")%>'+
                '&request=accept';
    var url='<%=request.getContextPath()%>/servlet/company/save.request'
    callAjax(url,data);
  }
  
  function cancelRequest(userId,mapType){
    var data='mapCompanyPerson='+userId+'&mappingUserType='+mapType+
                '&mapCompanyName=<%=request.getParameter("compN")%>'+
                '&request=cancel';
    var url='<%=request.getContextPath()%>/servlet/company/save.request'
    callAjax(url,data);
  }
  function callAjax(url,data){
        $.ajax({
                  type: 'POST',
                  url: url,
                  data: data,
                  success: function(){  
                    window.location.reload();               
                  }
            });
  }
  
  function showRequest(){
    $("#request").show();
  }
  
  function showPrivacy(){
    $('#privacyDivId').load('<%=request.getContextPath()%>/servlet/company/show.privacy?company=<%=currentNode.getName()%>');
    $("#privacyId").show();
  }
</script>

</head>
<body>
	<%
		var companyNode=currentNode.getNode("BasicInfo");
	%>
	<div class="container-fluid company-profile" style="min-height: 537px;">
		<h1 class="spacing-left" style="float: left; width: 69%; clear: left;">
			<div class="company-icon">
				<%
					if(companyNode.hasProperty("companyLogo")){
				%>
				<img
					src="<%=companyNode.companyLogo?companyNode.companyLogo:request.getContextPath()+"/apps/user/css/images/photo.gif"%>" />
				<%
					}
				    	   else{
				%>
				<img
					src="<%=request.getContextPath()%>/apps/user/css/images/photo.gif" />
				<%
					}
				%>
			</div>
			<%=companyNode.companyName?companyNode.companyName:""%>
			<div class="company-edit-icon">
				<%
					if(request.getRemoteUser().equals(companyNode.creatorEmailId+"")){
				%>

				<a	href="<%=request.getContextPath()%>/servlet/company/show.com?id=<%=request.getParameter("compN")%>">
					<img alt="Edit" title="Edit"
					src="<%=request.getContextPath()%>/apps/user/css/images/icon_edit.png"
					height="14" />
				</a> <a href="javascript:void(0);" onclick="getDelete()" id="delCom">
					<img alt="Delete" title="Delete"
					src="<%=request.getContextPath()%>/apps/user/css/images/delete.png"
					height="14" />
				</a>
				<%
					if(currentNode.hasNode("Relationship")){
				%>
				<a href="#request" onclick="showRequest()">User Request <%=currentNode.getNode("Relationship").hasNode("UserRequest")?
                                currentNode.getNode("Relationship").getNode("UserRequest").request:"0"%></a>
				<%
					}
				%>
				<a href="javascript:void(0);" onclick="showPrivacy()">Privacy</a>
				<%
					}
				%>
			</div>

			<br /> <a
				href="http://<%=companyNode.website?companyNode.website:""%>"
				target="_blank" class="sub-link"><%=companyNode.website?companyNode.website:""%></a>
			<br>
			<div id="shareId" style="margin-left: 126px;" onload=""></div>
		</h1>
		<div id="contentFeedbackFrame" style="float: left;"></div>
		<hr />
		<div class="container-right">
			<div class="content">
				<h2>Company Details</h2>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="35%"><label>Industries</label></td>
						<td><%=companyNode.industries?companyNode.industries:""%></td>
					</tr>
					<tr>
						<td><label>No. of Employee</label></td>
						<td><%=companyNode.employeesNum?companyNode.employeesNum:""%></td>
					</tr>
					<tr>
						<td><label>Type</label></td>
						<td><%=companyNode.type?companyNode.type:""%></td>
					</tr>
					<tr>
						<td><label>Turn Over</label></td>
						<td><%=companyNode.turnOver?companyNode.turnOver:""%></td>
					</tr>
					<tr>
						<td><label>SST No.</label></td>
						<td><%=companyNode.stateSalesTax?companyNode.stateSalesTax:""%></td>
					</tr>
					<tr>
						<td><label>CST No.</label></td>
						<td><%=companyNode.centralSalesTax?companyNode.centralSalesTax:""%></td>
					</tr>
					<tr>
						<td><label>PAN No.</label></td>
						<td><%=companyNode.panNumber?companyNode.panNumber:""%></td>
					</tr>
					<tr>
						<td><label><a rel="facebox" href="#panCopy">PAN
									Card Copy</a></label></td>
						<td id="panCopy" style="display: none"><img
							src="<%=companyNode.panCard?companyNode.panCard:""%>" width="700"
							height="400" /></td>
					</tr>
					<tr>
						<td><label>Founded On</label></td>
						<td><%=companyNode.founded?companyNode.founded:""%></td>
					</tr>
				</table>
			</div>
			<!--
    <div class="content"> 
      <h2>Our Business</h2>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="35%" valign="top"><label>Major Import</label></td>
          <td></td>
        </tr>
        <tr>
          <td><label>Major Export</label></td>
          <td></td>
        </tr>
        <tr>
          <td><label>Major Purchase</label></td>
          <td></td>
        </tr>
        <tr>
          <td><label>Major Sell</label></td>
          <td></td>
        </tr>
        <tr>
          <td><label>Major Raw Material</label></td>
          <td></td>
        </tr>
        <tr>
          <td><label>Major Capital Equipment Used</label></td>
          <td></td>
        </tr>
        
        <tr>
          <td><label>Services</label></td>
          <td></td>
        </tr>    
      </table>
    </div>-->
			<div class="container-right">
				<div class="content" style="width: 257px;">
					<h2>Career with Us</h2>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="70%" valign="top"><label>No. of Job
									Openings</label></td>
							<td>
								<%
									if(currentNode.hasNode("Career") && currentNode.getNode("Career").hasNode("Job") 
								                            && currentNode.getNode("Career").getNode("Job").hasNodes() ){
								%> <a
								href="<%=request.getContextPath()%>/servlet/company/show.showCareer?compN=<%=currentNode.getName()%>"><%=currentNode.getNode("Career").getNode("Job").getNodes().length%></a>
								<%
									}else{
								%> <a
								href="<%=request.getContextPath()%>/servlet/company/show.showCareer?compN=<%=currentNode.getName()%>">0</a>
								<%
									}
								%>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="container-right">
				<div class="content">
					<h2>Social Connect</h2>
					<div id="socialConnectId"></div>
				</div>
			</div>
			<%
				var masterNode=currentNode.session.getNode("/content/master/company");
			    var relCompanyNode=masterNode.getNode("company");
			    var relUser=masterNode.getNode("user");
			    var relProduct=masterNode.getNode("product");
			%>
			<div class="content" id="relationDivId">
				<h2>
					Relationship
					<ul class="content-sub-tab">
						<%
							if(privacy(userRelationPrivacy)=='true'){
						%>
						<li class="selected" id="person"><a
							href="javascript:void(0);" onclick="showRelMain('person');">Person</a></li>
						<%
							}if(privacy(compRelationPrivacy)=='true'){
						%>
						<li id="company"><a href="javascript:void(0);"
							onclick="showRelMain('company');">Company</a></li>
						<%
							}if(privacy(productPrivacy)=='true'){
						%>
						<li id="product"><a href="javascript:void(0);"
							onclick="showRelMain('product');">Products</a></li>
						<%
							}
						%>
					</ul>
				</h2>
				<%
					if(privacy(userRelationPrivacy)=='true'){
				%>
				<div id="personId" class="dummy_class">
					<ul class="content-sub-link">
						<%
							var userType=relUser.userType;
						                    for(index in userType){
						%>
						<li><a href="javascript:void(0);"
							onclick="showRel('u<%=(userType[index]).replace(/ /g,"")%>',this)"><%=userType[index]%></a></li>
						<%
							}
						%>
					</ul>
					<%
						if(currentNode.hasNode("Relationship")){
					                    relationNode=currentNode.getNode("Relationship");
					                    if(relationNode.hasNode("User")){
					                        var relationTypeNode=relationNode.getNode("User").getNodes()
					                        for(i in relationTypeNode){
					                            var relationUserNode=relationTypeNode[i].getNodes();
					                            var countRelationUser=relationUserNode.length;
					                            var countUser=0;
					%>
					<div id="u<%=relationTypeNode[i].getName()%>"
						style="display: none;" class="content-sub-link-data">
						<%
							for(j in relationUserNode){
						                                    countUser++;
						                                        if((request.getRemoteUser().replace("@","_")+"")==(relationUserNode[j].getName()+"")){
						                                            connectionCom = "true";
						                                        }
						%>
						<%
							var userNode=currentNode.session.getRootNode().getNode("content/user/").getNode(relationUserNode[j].getName());
						%>
						<a title="<%=userNode.getName()%>" lang="user" class="tTip"
							href="<%=request.getContextPath()%>/content/user/info?id=<%=relationUserNode[j].getName()%>">
							<%=userNode.name%> <%=userNode.lastName?userNode.lastName:""%></a>
						<%
							if(countUser!=countRelationUser){
						                                          out.println(",");
						                                    }
						                                 }
						%>
					</div>
					<!-- content-sub-link-data Class-->

					<%
						}
					                    }
					                }
					%>
				</div>
				<!-- Person Id -->
				<script>
            $(function(){
                $("#personId").children('ul').children('li:first-child').addClass("selected");
                var text2 = $("#personId").children('ul').children('li:first-child').children('a').text().replace(/ /g,"");
                $("#u"+text2+"").show();
            });
        </script>
				<%
					}
				%>
				<%
					if(privacy(compRelationPrivacy)=='true'){
				%>
				<div id="companyId" style="display: none;" class="dummy_class">
					<ul class="content-sub-link">
						<%
							var companyType=relCompanyNode.companyType;
						                    for(index in companyType){
						%>
						<li><a href="javascript:void(0);"
							onclick="showRel('c<%=(companyType[index]).replace(/ /g,"")%>',this)"><%=companyType[index]%></a></li>
						<%
							}
						%>
					</ul>
					<%
						if(currentNode.hasNode("Relationship")){
					                var relationNode=currentNode.getNode("Relationship");
					                    if(relationNode.hasNode("Company")){
					                        var relationTypeNode=relationNode.getNode("Company").getNodes();
					                        for(i in relationTypeNode){
					                            var relationCompanyNode=relationTypeNode[i].getNodes();
					                            var countRelationCompany=relationCompanyNode.length;
					                            var countCompany=0;
					%>
					<div id="c<%=relationTypeNode[i].getName()%>"
						class="content-sub-link-data" style="display: none;">
						<%
							for(j in relationCompanyNode){
						                                countCompany++;
						%>
						<a title="<%=relationCompanyNode[j].getName()%>" lang="company"
							class="tTip"
							href="<%=request.getContextPath()%>/servlet/company/show.view?compN=<%=relationCompanyNode[j].getName()%>"><%=relationCompanyNode[j].getName()%></a>
						<%
							if(countCompany!=countRelationCompany){
						                                      out.println(",");
						                                }
						                             }
						%>
					</div>
					<!-- content-sub-link-data Class-->
					<%
						}
					                    }
					                }
					%>
				</div>
				<!-- Company Id -->
				<%
					}
				%>
				<%
					if(privacy(productPrivacy)=='true'){
				%>
				<div id="productId" style="display: none;" class="dummy_class">
					<ul class="content-sub-link">
						<%
							var productType=relProduct.productType;
						                    for(index in productType){
						%>
						<li><a href="javascript:void(0);"
							onclick="showRel('p<%=(productType[index]).replace(/ /g,"")%>',this)"><%=productType[index]%></a></li>
						<%
							}
						%>
					</ul>
					<%
						if(currentNode.hasNode("Relationship")){
					                    relationNode=currentNode.getNode("Relationship");
					                    if(relationNode.hasNode("Product")){
					                        var relationTypeNode=relationNode.getNode("Product").getNodes()
					                        for(i in relationTypeNode){
					                            var relationProductNode=relationTypeNode[i].getNodes();
					                            var countRelationProduct=relationProductNode.length;
					                            var countProduct=0;
					%>
					<div id="p<%=relationTypeNode[i].getName()%>"
						style="display: none;" class="content-sub-link-data">
						<%
							for(j in relationProductNode){
						                                    countUser++;
						%>
						<%=relationProductNode[j].productName%>
						<%
							if(countUser!=countRelationUser){
						                                          out.println(",");
						                                    }
						                                 }
						%>
					</div>
					<!-- content-sub-link-data Class-->

					<%
						}
					                    }
					                }
					%>

				</div>
				<!-- Product Id -->
				<%
					}
				%>
			</div>
			<!-- Content Div -->

		</div>
		<!-- Right Container Div -->
		<div class="container-left" style="overflow: hidden;">
			<div class="content spacing-left content-scroll"
				style="max-height: 200px; overflow: hidden;">
				<p class="company-detail"><%=companyNode.description?companyNode.description:""%></p>
			</div>
		</div>

		<div class="container-left">
			<hr class="highlight" />
			<div class="content spacing-left">
				<ul class="address">

					<%
						var addressNode=currentNode.getNode("Address").getNodes();
						for(i in addressNode){
					%>

					<li><span><%=addressNode[i].addressType?addressNode[i].addressType:""%></span>
						<%=addressNode[i].branchAddress?addressNode[i].branchAddress:""%><br />
						<%=addressNode[i].branchCity?addressNode[i].branchCity:""%> - <%=addressNode[i].branchPostalCode?addressNode[i].branchPostalCode:""%><br />
						Tel: <%=addressNode[i].branchWorkNumber?addressNode[i].branchWorkNumber:""%><br />
						Mobile: <%=addressNode[i].branchMobileNumber?addressNode[i].branchMobileNumber:""%><br />
						Contact EmailId: <%=addressNode[i].branchContactEmailId?addressNode[i].branchContactEmailId:""%>
					</li>
					<%
						}
					%>

				</ul>
			</div>
		</div>
		<%
			if(privacy(imagesPrivacy)=='true'){
		%>
		<div class="container-left image_containe_box">
			<div class="image_container_heading">
				Image Gallery<span><a target="_blank"
					href="<%=request.getContextPath()%>/servlet/company/show.image?comp=<%=companyNode.companyName?companyNode.companyName:""%>">More
						Images</a></span>
			</div>
			<div id="carousel" class="image_container"
				style="background: none; border: none; height: 137px; width: 100%;">
				<div class="content">
					<%
						if(currentNode.hasNode("media") && currentNode.getNode("media").hasNode("Picture")
					                            && currentNode.getNode("media").getNode("Picture").hasNodes()
					                            && currentNode.getNode("media").getNode("Picture").hasProperty("latest")){
					                        var imageCatNodes = currentNode.getNode("media").getNode("Picture").getProperty("latest").getValues();
					                        for(var i=0;i<imageCatNodes.length;i++){
					%>
					<div style="display: inline-table;">
						<img class="image_scroll" height="100"
							src="<%=request.getContextPath()%><%=imageCatNodes[i].getString()+"/x150"%>" />
					</div>
					<%
						}
					                    }
					%>
				</div>
			</div>
		</div>
		<%
			}
		%>
		<br />
		<%
			if(privacy(videoPrivacy)=='true'){
		%>
		<div class="container-left image_containe_box">
			<div class="image_container_heading">
				Video Gallery<span><a target="_blank"
					href="<%=request.getContextPath()%>/servlet/company/show.video?comp=<%=companyNode.companyName?companyNode.companyName:""%>">More
						Videos</a></span>
			</div>
			<div id="videoScrollId" class="image_container"
				style="background: none; border: none; height: 137px; width: 100%;">
				<div class="content">
					<%
						if(currentNode.hasNode("media") && currentNode.getNode("media").hasNode("Video")
					                    && currentNode.getNode("media").getNode("Video").hasNodes() 
					                    && currentNode.getNode("media").getNode("Video").hasProperty("latestVideo")){
					                var videoCatNodes = currentNode.getNode("media").getNode("Video").getProperty("latestVideo").getValues();
					                for(i in videoCatNodes){
					%>
					<div style="display: inline-table;">
						<video width="175" height="118" controls>
							<source
								src="<%=request.getContextPath()+videoCatNodes[i].getString()%>"
								type="video/webm">
                               Your browser does not support the video tag.
                            
						</video>
					</div>
					<%
						}
					            }
					%>
				</div>
			</div>
		</div>
		<%
			}
		%>
		<div class="container-full image_containe_box">
			<div class="image_container_heading">Product Catalog</div>
			<img src="<%=request.getContextPath()%>/apps/company/hot-rolled.jpg"
				height="100px" style="float: left; padding: 5px;"></img> Hot-rolled

			Our extensive range of hot-rolled strip steels are designed to offer
			both standard and specialist performance requirements such as
			forming, bending, deep drawing, laser cutting and welding. Our
			structural range includes Durbar® floor plate for construction
			applications and we can also provide high carbon grades to meet
			demanding wear and fatigue performance.
		</div>


		<div class="container-full">
			<%
				if(privacy(comNewsPrivacy)=='true'){
			%>
			<div class="company-news">
				<h2>Company News</h2>
				<ul id="newsId" style="max-height: 200px;">
					<%
						try{
					                var contentNode = currentNode.session.getNode("/content/contentFeedback/"+currentNode.getIdentifier());
					                if(contentNode.hasNode("Notice") &&  contentNode.getNode("Notice").hasNodes()){
					                var newsNode = contentNode.getNode("Notice").getNodes();
					                for(i in newsNode){
					%>
					<li><%=newsNode[i].titleName%> <a target="_blank"
						style="float: right;"
						href="<%=request.getContextPath()%>/servlet/feedback/content.viewNotice?contentId=<%=contentNode.getName()%>&noticeId=<%=newsNode[i].getName()%>">Detail</a>
					</li>
					<%
						}
					            }}catch(e){
					            	
					            }
					%>
				</ul>
			</div>
			<%
				}
			%>

			<%
				if(privacy(comDocsPrivacy)=='true'){
			%>
			<div class="company-docs">
				<h2>
					Company Documents <a target="_blank"
						style="float: right; font-size: 11px; padding: 3px; color: white;"
						href="<%=request.getContextPath()%>/servlet/company/show.document?comp=<%=companyNode.companyName?companyNode.companyName:""%>">+
						Add More</a>
				</h2>
				<ul id="docId" style="max-height: 200px;">
					<%
						if(currentNode.hasNode("media") && currentNode.getNode("media").hasNode("Document")
					                && currentNode.getNode("media").getNode("Document").hasNodes()){
					                var documentCatNodes = currentNode.getNode("media").getNode("Document").getNodes();
					                for(i in documentCatNodes){
					                    var docNodes = documentCatNodes[i].getNodes();
					                    for(k in docNodes){
					%>
					<li><%=docNodes[k].getNodes()[0].getName()%> <a
						target="_blank" style="float: right; padding: 0 0 0 5px;"
						href="javascript:void(0);"
						onclick="downloadFile('<%=docNodes[k].getNodes()[0]%>')">Download</a>
						<a target="_blank" style="float: right;"
						href="https://docs.google.com/viewer?url=http://<%=request.getServerName()+":"+request.getServerPort()+request.getContextPath()+docNodes[k].getNodes()[0]%>">View</a>
					</li>
					<%
						}
					                }
					            }
					%>
				</ul>
			</div>
			<%
				}
			%>
		</div>
		<%
			if(privacy(contactPrivacy)=='true'){
		%>
		<div class="container-full">
			<%
				if(currentNode.hasNode("PointOfContact")){
			%>
			<div class="content spacing-left" style="float: left;">
				<h3>Point of Contact Detail</h3>
				<ul class="address-2">
					<%
						var contactNode=currentNode.getNode("PointOfContact").getNodes();
						for(i in contactNode){
					%>
					<li><span><%=contactNode[i].contactTypeCompany?contactNode[i].contactTypeCompany:""%></span>
						Name: <%=contactNode[i].nameContactTypeCompany?contactNode[i].nameContactTypeCompany:""%><br />
						Tel: <%=contactNode[i].contactContactTypeCompany?contactNode[i].contactContactTypeCompany:""%><br />
						Details: <%=contactNode[i].detailContactTypeCompany?contactNode[i].detailContactTypeCompany:""%><br />
					</li>
					<%
						}
					%>
				</ul>
			</div>
			<%
				}
			%>
			<div class="footer-note">
				Creator by
				<%=companyNode.creatorEmailId?companyNode.creatorEmailId:""%>, DOJ
				<%=companyNode.creatorjoiningDate?companyNode.creatorjoiningDate:""%></div>

		</div>
	</div>
	<%
		}
	%>
	<div style="display: none;">
		<form
			action="<%=request.getContextPath()%>/servlet/company/show.download"
			method="POST" id="downFormId">
			<input type="hidden" value="" id="downloadId" name="path" />
		</form>
	</div>
	<div id="privacyId" style="display: none;">
		<div class="cred_bg-disable"></div>
		<div class="iframe_div" style="height: 389px;">
			<div style="font-weight: bold; font-size: 18px;">Set Your
				Privacy</div>
			<a href="javascript:void(0);" class="iframe_close"></a>
			<div id="privacyDivId"></div>
		</div>
	</div>
	<div id="request" style="display: none;">
		<div class="cred_bg-disable"></div>
		<div class="iframe_div">
			<div style="font-weight: bold; font-size: 18px;">User Request</div>
			<a href="javascript:void(0);" class="iframe_close"></a>
			<ul style="padding: 0px;">
				<%
					if(currentNode.hasNode("Relationship") 
				               && currentNode.getNode("Relationship").hasNode("UserRequest")){
				            var requestNodes=currentNode.getNode("Relationship")
				                                .getNode("UserRequest").getNodes();
				             for(i in requestNodes){
				                var mapType=requestNodes[i].getNodes();
				%>

				<div class="section">
					<div class="header">
						<%=requestNodes[i].getName()%>
					</div>
					<ul>
						<%
							for(k in mapType){
						                        var userName=mapType[k].getName();
						                        var personNode=user.getNode(userName);
						%>
						<li><%=personNode.name%> <%=personNode.lastName?personNode.lastName:""%>
							<input type="button"
							style="float: right !important; margin: -2px 0 0 6px;"
							class="custom-button custom-button-danger"
							onclick="cancelRequest('<%=userName%>',
                                        '<%=requestNodes[i].getName()%>')"
							value="Cancel" />&nbsp; <input type="button"
							style="float: right !important; margin: -2px 0 0 6px;"
							class="custom-button custom-button-primary"
							onclick="accpetRequest('<%=userName%>',
                                        '<%=requestNodes[i].getName()%>')"
							value="Accept" /></li>
						<%
							}
						%>
					</ul>
				</div>
				<%
					}               
				            
				        }
				%>
			</ul>
		</div>
	</div>
	<%
		function privacy(privacyVar){
	var show="true";
	if(currentNodeProvided && (request.getRemoteUser()!='anonymous')){
	    if(privacyVar=='connection'){
	        if(connectionCom=='true'){
	            show="true"; 
	        }else if((request.getRemoteUser()+"")==(companyNode.creatorEmailId+"")){
	            show="true";
	        }else{
	            show="false";
	        }          
	    }else if(privacyVar=='onlyMe'){
	        show="false";
	        if((request.getRemoteUser()+"")==(companyNode.creatorEmailId+"")){
	                show="true";
	        }
	    }else if(privacyVar=='everyOne'){
	        show="true";
	    }
	 }
	    return show;
	}
	%>
</body>
</html>