
<section class="product-sub">
    <div class="container">
     <div class="row"> <ol class="breadcrumb">
     <li><a href="<%=request.getContextPath()%>/servlet/service/index.pageview">Home</a></li>
 
     
    <%   
     var cat=request.getParameter("catid");
      var url="/servlet/service/index.catlist?catid=";
var cNode;

  

  		if(cat.length()==2){
    cNode=currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(cat).getNodes();
    out.print("<li class='active'>"+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(cat).name+"</li>");
  		}else if(cat.length()==4){
  		var s=cat.split("");
  		 out.print("<li><a href='"+request.getContextPath()+"/servlet/service/index.catlist?catid="+s[1]+""+s[2]+"&search="+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).name+"'>"+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).name+"</a></li>");
  out.print("<li class='active'>"+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).getNode(s[3]+""+s[4]).name+"</li>");
 cNode=currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).getNode(s[3]+""+s[4]).getNodes();
  			}else if(cat.length()==6){
  		var s=cat.split("");
  			out.print("<li><a href='"+request.getContextPath()+"/servlet/service/index.catlist?catid="+s[1]+""+s[2]+"&search="+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).name+"'>"+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).name+"</a></li>");
	out.print("<li><a href='"+request.getContextPath()+"/servlet/service/index.catlist?catid="+s[1]+""+s[2]+s[3]+""+s[4]+"&search="+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).getNode(s[3]+""+s[4]).name+"'>"+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).getNode(s[3]+""+s[4]).name+"</a></li>");
  out.print("<li class='active'>"+currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+""+s[2]).getNode(s[3]+""+s[4]).getNode(s[5]+s[6]).name+"</li>");
 
 cNode=currentNode.session.getRootNode().getNode("content").getNode("unspsc").getNode(s[1]+s[2]).getNode(s[3]+s[4]).getNode(s[5]+s[6]).getNodes();		
  				
  				url="/servlet/service/productselection.productlist?query=";
  				}
	
out.print("</ol>");
	var rowc=0;
	
	
		var result=cNode.length/4;
		var integerPart = Math.floor(result);
		var remain;
		if((cNode.length)-(integerPart*4)!=0){
			remain=(cNode.length)-(integerPart*4);
		}
		rowc=integerPart;
	var n=0;
	if(cat.length()!=6){
   for(u=0;u<4;u++){   
    
		%>
		
		
		<div class="col-sm-3">
		<% 	for(i=n;i<integerPart;i++){
			//out.print(n);
			if(!"undefined".equals(cNode[i])){
			
    %>
        
        <fieldset>
        <legend> <%=cNode[i].name%></legend><!--<img src="/portal/apps/images/energy3.jpg" alt="" />-->
            
          	    <% if(cNode[i].hasNodes()){
          	    	out.print("<ul class='list-inline readmorelink'>");
 						var childNode=cNode[i].getNodes();
 						//out.print(childNode.length);
 						//out.print(childNode[0].name);
							var c=0;
 							for(var v=0;v<childNode.length;v++){
 							//out.print(v);
 								//if(!"undefined".equals(childNode[j])){
 							//			out.print(childNode[v].name);
                 out.print("<li><a href='"+request.getContextPath()+url+cat+cNode[i].getName()+"&search="+childNode[v].name+"'>"+childNode[v].name+"</a></li>");
         			 						  
         								   							  c++;
         								
         								     
          						//}
               
			 				}
 							out.print("</ul>");
					} %>
            
      </fieldset>
     
       <% }       
			 
			n++; }
		integerPart=integerPart+rowc;	%>
		</div>
		<%}
 	for(i=n;i<(integerPart+remain);i++){
			if(!"undefined".equals(cNode[i])){
			
    %>
       <div class="col-sm-3"> 
        <fieldset>
        <legend> <%=cNode[i].name%></legend><!--<img src="/portal/apps/images/energy3.jpg" alt="" />-->
            
          	    <% if(cNode[i].hasNodes()){
          	    	out.print("<ul class='list-inline readmorelink'>");
 						var childNode=cNode[i].getNodes();
 						//out.print(childNode.length);
 						//out.print(childNode[0]);
							var c=0;
 							for(var v=0;v<childNode.length;v++){
 								if(!"undefined".equals(childNode[v])){
 									
                 out.print("<li><a href='"+request.getContextPath()+url+cat+cNode[i].getName()+"&search="+childNode[v].name+"'>"+childNode[v].name+"</a></li>");
         			 						  
         								   							  c++;
         								
         								     
          						}
               
			 				}
 							out.print("</ul>");
					} %>
            
      </fieldset>
     </div>
       <% }       
			 
			n++; }
		%>
		 <% }else{n=0; 
  %> 	
   <div class="col-sm-3">
	   <fieldset>
	   <ul class='list-inline readmorelink'>
	<% 
		for(i=n;i<integerPart;i++){
			
			if(!"undefined".equals(cNode[i])){

                 out.print("<li><a href='"+request.getContextPath()+url+cat+cNode[n].getName()+"&search="+cNode[n].name+"'>"+cNode[n].name+"</a></li>");
n++;
			 				}
 							
					} %>
           </ul> 
      </fieldset>
 </div>
 
 <div class="col-sm-3">
	   <fieldset>
	   <ul class='list-inline readmorelink'>
	<% 
		for(i=n;i<integerPart*2;i++){
			
			if(!"undefined".equals(cNode[i])){

                 out.print("<li><a href='"+request.getContextPath()+url+cat+cNode[n].getName()+"&search="+cNode[n].name+"'>"+cNode[n].name+"</a></li>");
n++;
			 				}
 							
					} %>
           </ul> 
      </fieldset>
 </div>
 <div class="col-sm-3">
	   <fieldset>
	   <ul class='list-inline readmorelink'>
	<% 
		for(i=n;i<integerPart*3;i++){
			
			if(!"undefined".equals(cNode[i])){

                 out.print("<li><a href='"+request.getContextPath()+url+cat+cNode[n].getName()+"&search="+cNode[n].name+"'>"+cNode[n].name+"</a></li>");
n++;
			 				}
 							
					} %>
           </ul> 
      </fieldset>
 </div>
 <div class="col-sm-3">
	   <fieldset>
	   <ul class='list-inline readmorelink'>
	<% 
		for(i=n;i<cNode.length;i++){
			
			if(!"undefined".equals(cNode[i])){

                 out.print("<li><a href='"+request.getContextPath()+url+cat+cNode[n].getName()+"&search="+cNode[n].name+"'>"+cNode[n].name+"</a></li>");
n++;
			 				}
 							
					} %>
           </ul> 
      </fieldset>
 </div>


  <% }%>

    </div>
 </div>
</section>