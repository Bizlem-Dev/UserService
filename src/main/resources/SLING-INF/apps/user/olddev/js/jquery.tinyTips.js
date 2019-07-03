(function($){  
	$.fn.tinyTips = function (tipColor, supCont) {
		var ajax_request;
		if (tipColor === 'null') {
			tipColor = 'light';
		} 
		var tipName = tipColor + 'Tip';
		var tipFrame = '<div class="' + tipName + '"><div class="contentL"></div><div class="bottom">&nbsp;</div></div>';
		var animSpeed = 300;
		var tinyTip;
		var tText;
		$(this).hover(function() {
			$('body').append(tipFrame);
			var divTip = 'div.'+tipName;
			tinyTip = $(divTip);
			tinyTip.hide();
			if(typeof ajax_request !== 'undefined'){
			ajax_request.abort();	
			}
			ajax_request =  $.ajax({
								url:supCont+'/servlet/company/show.getInfo',
								type:'POST',
								data:'value='+$(this).attr('title')+'&type='+$(this).attr('lang'),
								success:function(data){
									$(divTip + ' .contentL').html(data);
								}
							});
			if (supCont === 'title') {
				var tipCont = $(this).attr('title');
			} else if (supCont !== 'title') {
				var tipCont = supCont;
			}
			$(divTip + ' .contentL').html("<div align='center'>Loading...!!</div>");
			tText = $(this).attr('title');
			$(this).attr('title', '');
			var yOffset = tinyTip.height() + 2;
			var xOffset = (tinyTip.width() / 2) - ($(this).width() / 2);
			var pos = $(this).offset();
			var nPos = pos;
			nPos.top = pos.top - yOffset;
			nPos.left = pos.left - xOffset;
			tinyTip.css('position', 'absolute').css('z-index', '1000');
			tinyTip.css(nPos).fadeIn(animSpeed);
			
		}, function() {	
			$(this).attr('title', tText);
			tinyTip.fadeOut(animSpeed, function() {
				$(this).remove();
			});		
		});	
	}
})(jQuery);