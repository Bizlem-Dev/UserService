(function(e,t,n){"use strict";function u(t){if(r.logging&&e.console&&e.console.log){e.console.log(t)}}function a(e,t,n,i,s,o){var u=r.Util.getCSS(t,e,s),a,f,l,c;if(u.length===1){c=u[0];u=[];u[0]=c;u[1]=c}if(u[0].toString().indexOf("%")!==-1){l=parseFloat(u[0])/100;f=n.width*l;if(e!=="backgroundSize"){f-=(o||i).width*l}}else{if(e==="backgroundSize"){if(u[0]==="auto"){f=i.width}else{if(u[0].match(/contain|cover/)){var h=r.Util.resizeBounds(i.width,i.height,n.width,n.height,u[0]);f=h.width;a=h.height}else{f=parseInt(u[0],10)}}}else{f=parseInt(u[0],10)}}if(u[1]==="auto"){a=f/i.width*i.height}else if(u[1].toString().indexOf("%")!==-1){l=parseFloat(u[1])/100;a=n.height*l;if(e!=="backgroundSize"){a-=(o||i).height*l}}else{a=parseInt(u[1],10)}return[f,a]}function f(e){return{zindex:e,children:[]}}function l(e,t){var n=[];return{storage:n,width:e,height:t,clip:function(){n.push({type:"function",name:"clip",arguments:arguments})},translate:function(){n.push({type:"function",name:"translate",arguments:arguments})},fill:function(){n.push({type:"function",name:"fill",arguments:arguments})},save:function(){n.push({type:"function",name:"save",arguments:arguments})},restore:function(){n.push({type:"function",name:"restore",arguments:arguments})},fillRect:function(){n.push({type:"function",name:"fillRect",arguments:arguments})},createPattern:function(){n.push({type:"function",name:"createPattern",arguments:arguments})},drawShape:function(){var e=[];n.push({type:"function",name:"drawShape",arguments:e});return{moveTo:function(){e.push({name:"moveTo",arguments:arguments})},lineTo:function(){e.push({name:"lineTo",arguments:arguments})},arcTo:function(){e.push({name:"arcTo",arguments:arguments})},bezierCurveTo:function(){e.push({name:"bezierCurveTo",arguments:arguments})},quadraticCurveTo:function(){e.push({name:"quadraticCurveTo",arguments:arguments})}}},drawImage:function(){n.push({type:"function",name:"drawImage",arguments:arguments})},fillText:function(){n.push({type:"function",name:"fillText",arguments:arguments})},setVariable:function(e,t){n.push({type:"variable",name:e,arguments:t})}}}var r={},i,s,o;r.Util={};r.Util.trimText=function(e){return function(t){if(e){return e.apply(t)}else{return((t||"")+"").replace(/^\s+|\s+$/g,"")}}}(String.prototype.trim);r.Util.parseBackgroundImage=function(e){var t=" \r\n	",n,r,i,s,o,u=[],a,f=0,l=0,c,h;var p=function(){if(n){if(r.substr(0,1)==='"'){r=r.substr(1,r.length-2)}if(r){h.push(r)}if(n.substr(0,1)==="-"&&(s=n.indexOf("-",1)+1)>0){i=n.substr(0,s);n=n.substr(s)}u.push({prefix:i,method:n.toLowerCase(),value:o,args:h})}h=[];n=i=r=o=""};p();for(var d=0,v=e.length;d<v;d++){a=e[d];if(f===0&&t.indexOf(a)>-1){continue}switch(a){case'"':if(!c){c=a}else if(c===a){c=null}break;case"(":if(c){break}else if(f===0){f=1;o+=a;continue}else{l++}break;case")":if(c){break}else if(f===1){if(l===0){f=0;o+=a;p();continue}else{l--}}break;case",":if(c){break}else if(f===0){p();continue}else if(f===1){if(l===0&&!n.match(/^url$/i)){h.push(r);r="";o+=a;continue}}break}o+=a;if(f===0){n+=a}else{r+=a}}p();return u};r.Util.Bounds=function(t){var n,r={};if(t.getBoundingClientRect){n=t.getBoundingClientRect();r.top=n.top;r.bottom=n.bottom||n.top+n.height;r.left=n.left;r.width=n.width||n.right-n.left;r.height=n.height||n.bottom-n.top;return r}};r.Util.getCSS=function(e,o,u){function l(t,n){var r=e.runtimeStyle&&e.runtimeStyle[t],i,s=e.style;if(!/^-?[0-9]+\.?[0-9]*(?:px)?$/i.test(n)&&/^-?\d/.test(n)){i=s.left;if(r){e.runtimeStyle.left=e.currentStyle.left}s.left=t==="fontSize"?"1em":n||0;n=s.pixelLeft+"px";s.left=i;if(r){e.runtimeStyle.left=r}}if(!/^(thin|medium|thick)$/i.test(n)){return Math.round(parseFloat(n))+"px"}return n}var a,f=o.match(/^background(Size|Position)$/);if(i!==e){s=t.defaultView.getComputedStyle(e,null)}a=s[o];if(f){a=(a||"").split(",");a=a[u||0]||a[0]||"auto";a=r.Util.trimText(a).split(" ");if(o==="backgroundSize"&&(!a[0]||a[0].match(/cover|contain|auto/))){}else{a[0]=a[0].indexOf("%")===-1?l(o+"X",a[0]):a[0];if(a[1]===n){if(o==="backgroundSize"){a[1]="auto";return a}else{a[1]=a[0]}}a[1]=a[1].indexOf("%")===-1?l(o+"Y",a[1]):a[1]}}else if(/border(Top|Bottom)(Left|Right)Radius/.test(o)){var c=a.split(" ");if(c.length<=1){c[1]=c[0]}c[0]=parseInt(c[0],10);c[1]=parseInt(c[1],10);a=c}return a};r.Util.resizeBounds=function(e,t,n,r,i){var s=n/r,o=e/t,u,a;if(!i||i==="auto"){u=n;a=r}else{if(s<o^i==="contain"){a=r;u=r*o}else{u=n;a=n/o}}return{width:u,height:a}};r.Util.BackgroundPosition=function(e,t,n,r,i){var s=a("backgroundPosition",e,t,n,r,i);return{left:s[0],top:s[1]}};r.Util.BackgroundSize=function(e,t,n,r){var i=a("backgroundSize",e,t,n,r);return{width:i[0],height:i[1]}};r.Util.Extend=function(e,t){for(var n in e){if(e.hasOwnProperty(n)){t[n]=e[n]}}return t};r.Util.Children=function(e){var t;try{t=e.nodeName&&e.nodeName.toUpperCase()==="IFRAME"?e.contentDocument||e.contentWindow.document:function(e){var t=[];if(e!==null){(function(e,t){var r=e.length,i=0;if(typeof t.length==="number"){for(var s=t.length;i<s;i++){e[r++]=t[i]}}else{while(t[i]!==n){e[r++]=t[i++]}}e.length=r;return e})(t,e)}return t}(e.childNodes)}catch(r){u("html2canvas.Util.Children failed with exception: "+r.message);t=[]}return t};r.Util.Font=function(){var e={};return function(t,r,i){if(e[t+"-"+r]!==n){return e[t+"-"+r]}var s=i.createElement("div"),o=i.createElement("img"),u=i.createElement("span"),a="Hidden Text",f,l,c;s.style.visibility="hidden";s.style.fontFamily=t;s.style.fontSize=r;s.style.margin=0;s.style.padding=0;i.body.appendChild(s);o.src="data:image/gif;base64,R0lGODlhAQABAIABAP///wAAACwAAAAAAQABAAACAkQBADs=";o.width=1;o.height=1;o.style.margin=0;o.style.padding=0;o.style.verticalAlign="baseline";u.style.fontFamily=t;u.style.fontSize=r;u.style.margin=0;u.style.padding=0;u.appendChild(i.createTextNode(a));s.appendChild(u);s.appendChild(o);f=o.offsetTop-u.offsetTop+1;s.removeChild(u);s.appendChild(i.createTextNode(a));s.style.lineHeight="normal";o.style.verticalAlign="super";l=o.offsetTop-s.offsetTop+1;c={baseline:f,lineWidth:1,middle:l};e[t+"-"+r]=c;i.body.removeChild(s);return c}}();(function(){r.Generate={};var e=[/^(-webkit-linear-gradient)\(([a-z\s]+)([\w\d\.\s,%\(\)]+)\)$/,/^(-o-linear-gradient)\(([a-z\s]+)([\w\d\.\s,%\(\)]+)\)$/,/^(-webkit-gradient)\((linear|radial),\s((?:\d{1,3}%?)\s(?:\d{1,3}%?),\s(?:\d{1,3}%?)\s(?:\d{1,3}%?))([\w\d\.\s,%\(\)\-]+)\)$/,/^(-moz-linear-gradient)\(((?:\d{1,3}%?)\s(?:\d{1,3}%?))([\w\d\.\s,%\(\)]+)\)$/,/^(-webkit-radial-gradient)\(((?:\d{1,3}%?)\s(?:\d{1,3}%?)),\s(\w+)\s([a-z\-]+)([\w\d\.\s,%\(\)]+)\)$/,/^(-moz-radial-gradient)\(((?:\d{1,3}%?)\s(?:\d{1,3}%?)),\s(\w+)\s?([a-z\-]*)([\w\d\.\s,%\(\)]+)\)$/,/^(-o-radial-gradient)\(((?:\d{1,3}%?)\s(?:\d{1,3}%?)),\s(\w+)\s([a-z\-]+)([\w\d\.\s,%\(\)]+)\)$/];r.Generate.parseGradient=function(t,n){var r,i,s=e.length,o,u,a,f,l,c,h,p,d,v;for(i=0;i<s;i+=1){o=t.match(e[i]);if(o){break}}if(o){switch(o[1]){case"-webkit-linear-gradient":case"-o-linear-gradient":r={type:"linear",x0:null,y0:null,x1:null,y1:null,colorStops:[]};a=o[2].match(/\w+/g);if(a){f=a.length;for(i=0;i<f;i+=1){switch(a[i]){case"top":r.y0=0;r.y1=n.height;break;case"right":r.x0=n.width;r.x1=0;break;case"bottom":r.y0=n.height;r.y1=0;break;case"left":r.x0=0;r.x1=n.width;break}}}if(r.x0===null&&r.x1===null){r.x0=r.x1=n.width/2}if(r.y0===null&&r.y1===null){r.y0=r.y1=n.height/2}a=o[3].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\)(?:\s\d{1,3}(?:%|px))?)+/g);if(a){f=a.length;l=1/Math.max(f-1,1);for(i=0;i<f;i+=1){c=a[i].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\))\s*(\d{1,3})?(%|px)?/);if(c[2]){u=parseFloat(c[2]);if(c[3]==="%"){u/=100}else{u/=n.width}}else{u=i*l}r.colorStops.push({color:c[1],stop:u})}}break;case"-webkit-gradient":r={type:o[2]==="radial"?"circle":o[2],x0:0,y0:0,x1:0,y1:0,colorStops:[]};a=o[3].match(/(\d{1,3})%?\s(\d{1,3})%?,\s(\d{1,3})%?\s(\d{1,3})%?/);if(a){r.x0=a[1]*n.width/100;r.y0=a[2]*n.height/100;r.x1=a[3]*n.width/100;r.y1=a[4]*n.height/100}a=o[4].match(/((?:from|to|color-stop)\((?:[0-9\.]+,\s)?(?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\)\))+/g);if(a){f=a.length;for(i=0;i<f;i+=1){c=a[i].match(/(from|to|color-stop)\(([0-9\.]+)?(?:,\s)?((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\))\)/);u=parseFloat(c[2]);if(c[1]==="from"){u=0}if(c[1]==="to"){u=1}r.colorStops.push({color:c[3],stop:u})}}break;case"-moz-linear-gradient":r={type:"linear",x0:0,y0:0,x1:0,y1:0,colorStops:[]};a=o[2].match(/(\d{1,3})%?\s(\d{1,3})%?/);if(a){r.x0=a[1]*n.width/100;r.y0=a[2]*n.height/100;r.x1=n.width-r.x0;r.y1=n.height-r.y0}a=o[3].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\)(?:\s\d{1,3}%)?)+/g);if(a){f=a.length;l=1/Math.max(f-1,1);for(i=0;i<f;i+=1){c=a[i].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\))\s*(\d{1,3})?(%)?/);if(c[2]){u=parseFloat(c[2]);if(c[3]){u/=100}}else{u=i*l}r.colorStops.push({color:c[1],stop:u})}}break;case"-webkit-radial-gradient":case"-moz-radial-gradient":case"-o-radial-gradient":r={type:"circle",x0:0,y0:0,x1:n.width,y1:n.height,cx:0,cy:0,rx:0,ry:0,colorStops:[]};a=o[2].match(/(\d{1,3})%?\s(\d{1,3})%?/);if(a){r.cx=a[1]*n.width/100;r.cy=a[2]*n.height/100}a=o[3].match(/\w+/);c=o[4].match(/[a-z\-]*/);if(a&&c){switch(c[0]){case"farthest-corner":case"cover":case"":h=Math.sqrt(Math.pow(r.cx,2)+Math.pow(r.cy,2));p=Math.sqrt(Math.pow(r.cx,2)+Math.pow(r.y1-r.cy,2));d=Math.sqrt(Math.pow(r.x1-r.cx,2)+Math.pow(r.y1-r.cy,2));v=Math.sqrt(Math.pow(r.x1-r.cx,2)+Math.pow(r.cy,2));r.rx=r.ry=Math.max(h,p,d,v);break;case"closest-corner":h=Math.sqrt(Math.pow(r.cx,2)+Math.pow(r.cy,2));p=Math.sqrt(Math.pow(r.cx,2)+Math.pow(r.y1-r.cy,2));d=Math.sqrt(Math.pow(r.x1-r.cx,2)+Math.pow(r.y1-r.cy,2));v=Math.sqrt(Math.pow(r.x1-r.cx,2)+Math.pow(r.cy,2));r.rx=r.ry=Math.min(h,p,d,v);break;case"farthest-side":if(a[0]==="circle"){r.rx=r.ry=Math.max(r.cx,r.cy,r.x1-r.cx,r.y1-r.cy)}else{r.type=a[0];r.rx=Math.max(r.cx,r.x1-r.cx);r.ry=Math.max(r.cy,r.y1-r.cy)}break;case"closest-side":case"contain":if(a[0]==="circle"){r.rx=r.ry=Math.min(r.cx,r.cy,r.x1-r.cx,r.y1-r.cy)}else{r.type=a[0];r.rx=Math.min(r.cx,r.x1-r.cx);r.ry=Math.min(r.cy,r.y1-r.cy)}break}}a=o[5].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\)(?:\s\d{1,3}(?:%|px))?)+/g);if(a){f=a.length;l=1/Math.max(f-1,1);for(i=0;i<f;i+=1){c=a[i].match(/((?:rgb|rgba)\(\d{1,3},\s\d{1,3},\s\d{1,3}(?:,\s[0-9\.]+)?\))\s*(\d{1,3})?(%|px)?/);if(c[2]){u=parseFloat(c[2]);if(c[3]==="%"){u/=100}else{u/=n.width}}else{u=i*l}r.colorStops.push({color:c[1],stop:u})}}break}}return r};r.Generate.Gradient=function(e,n){if(n.width===0||n.height===0){return}var i=t.createElement("canvas"),s=i.getContext("2d"),o,a,f,l;i.width=n.width;i.height=n.height;o=r.Generate.parseGradient(e,n);if(o){if(o.type==="linear"){a=s.createLinearGradient(o.x0,o.y0,o.x1,o.y1);for(f=0,l=o.colorStops.length;f<l;f+=1){try{a.addColorStop(o.colorStops[f].stop,o.colorStops[f].color)}catch(c){u(["failed to add color stop: ",c,"; tried to add: ",o.colorStops[f],"; stop: ",f,"; in: ",e])}}s.fillStyle=a;s.fillRect(0,0,n.width,n.height)}else if(o.type==="circle"){a=s.createRadialGradient(o.cx,o.cy,0,o.cx,o.cy,o.rx);for(f=0,l=o.colorStops.length;f<l;f+=1){try{a.addColorStop(o.colorStops[f].stop,o.colorStops[f].color)}catch(c){u(["failed to add color stop: ",c,"; tried to add: ",o.colorStops[f],"; stop: ",f,"; in: ",e])}}s.fillStyle=a;s.fillRect(0,0,n.width,n.height)}else if(o.type==="ellipse"){var h=t.createElement("canvas"),p=h.getContext("2d"),d=Math.max(o.rx,o.ry),v=d*2,m;h.width=h.height=v;a=p.createRadialGradient(o.rx,o.ry,0,o.rx,o.ry,d);for(f=0,l=o.colorStops.length;f<l;f+=1){try{a.addColorStop(o.colorStops[f].stop,o.colorStops[f].color)}catch(c){u(["failed to add color stop: ",c,"; tried to add: ",o.colorStops[f],"; stop: ",f,"; in: ",e])}}p.fillStyle=a;p.fillRect(0,0,v,v);s.fillStyle=o.colorStops[f-1].color;s.fillRect(0,0,i.width,i.height);s.drawImage(h,o.cx-o.rx,o.cy-o.ry,2*o.rx,2*o.ry)}}return i};r.Generate.ListAlpha=function(e){var t="",n;do{n=e%26;t=String.fromCharCode(n+64)+t;e=e/26}while(e*26>26);return t};r.Generate.ListRoman=function(e){var t=["M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"],n=[1e3,900,500,400,100,90,50,40,10,9,5,4,1],r="",i,s=t.length;if(e<=0||e>=4e3){return e}for(i=0;i<s;i+=1){while(e>=n[i]){e-=n[i];r+=t[i]}}return r}})();r.Parse=function(i,s){function y(){return Math.max(Math.max(c.body.scrollWidth,c.documentElement.scrollWidth),Math.max(c.body.offsetWidth,c.documentElement.offsetWidth),Math.max(c.body.clientWidth,c.documentElement.clientWidth))}function b(){return Math.max(Math.max(c.body.scrollHeight,c.documentElement.scrollHeight),Math.max(c.body.offsetHeight,c.documentElement.offsetHeight),Math.max(c.body.clientHeight,c.documentElement.clientHeight))}function w(e,t){var n=parseInt(v(e,t),10);return isNaN(n)?0:n}function E(e,t,n,r,i,s){if(s!=="transparent"){e.setVariable("fillStyle",s);e.fillRect(t,n,r,i);a+=1}}function S(e,t){switch(t){case"lowercase":return e.toLowerCase();case"capitalize":return e.replace(/(^|\s|:|-|\(|\))([a-z])/g,function(e,t,n){if(e.length>0){return t+n.toUpperCase()}});case"uppercase":return e.toUpperCase();default:return e}}function x(e){return/^(normal|none|0px)$/.test(e)}function T(e,t,n,i){if(e!==null&&r.Util.trimText(e).length>0){i.fillText(e,t,n);a+=1}}function N(e,t,n,i){var s=false,o=v(t,"fontWeight"),u=v(t,"fontFamily"),a=v(t,"fontSize");switch(parseInt(o,10)){case 401:o="bold";break;case 400:o="normal";break}e.setVariable("fillStyle",i);e.setVariable("font",[v(t,"fontStyle"),v(t,"fontVariant"),o,a,u].join(" "));e.setVariable("textAlign",s?"right":"left");if(n!=="none"){return r.Util.Font(u,a,c)}}function C(e,t,n,r,i){switch(t){case"underline":E(e,n.left,Math.round(n.top+r.baseline+r.lineWidth),n.width,1,i);break;case"overline":E(e,n.left,Math.round(n.top),n.width,1,i);break;case"line-through":E(e,n.left,Math.ceil(n.top+r.middle+r.lineWidth),n.width,1,i);break}}function k(e,t,n,i){var s;if(h.rangeBounds){if(n!=="none"||r.Util.trimText(t).length!==0){s=L(t,e.node,e.textOffset)}e.textOffset+=t.length}else if(e.node&&typeof e.node.nodeValue==="string"){var o=i?e.node.splitText(t.length):null;s=A(e.node);e.node=o}return s}function L(e,t,n){var r=c.createRange();r.setStart(t,n);r.setEnd(t,n+e.length);return r.getBoundingClientRect()}function A(e){var t=e.parentNode,n=c.createElement("wrapper"),i=e.cloneNode(true);n.appendChild(e.cloneNode(true));t.replaceChild(n,e);var s=r.Util.Bounds(n);t.replaceChild(i,n);return s}function O(e,t,n){var i=n.ctx,o=v(e,"color"),u=v(e,"textDecoration"),a=v(e,"textAlign"),f,l,c={node:t,textOffset:0};if(r.Util.trimText(t.nodeValue).length>0){t.nodeValue=S(t.nodeValue,v(e,"textTransform"));a=a.replace(["-webkit-auto"],["auto"]);l=!s.letterRendering&&/^(left|right|justify|auto)$/.test(a)&&x(v(e,"letterSpacing"))?t.nodeValue.split(/(\b| )/):t.nodeValue.split("");f=N(i,e,u,o);if(s.chinese){l.forEach(function(e,t){if(/.*[\u4E00-\u9FA5].*$/.test(e)){e=e.split("");e.unshift(t,1);l.splice.apply(l,e)}})}l.forEach(function(e,t){var n=k(c,e,u,t<l.length-1);if(n){T(e,n.left,n.bottom,i);C(i,u,n,f,o)}})}}function M(e,t){var n=c.createElement("boundelement"),i,s;n.style.display="inline";i=e.style.listStyleType;e.style.listStyleType="none";n.appendChild(c.createTextNode(t));e.insertBefore(n,e.firstChild);s=r.Util.Bounds(n);e.removeChild(n);e.style.listStyleType=i;return s}function _(e){var t=-1,n=1,r=e.parentNode.childNodes;if(e.parentNode){while(r[++t]!==e){if(r[t].nodeType===1){n++}}return n}else{return-1}}function D(e,t){var n=_(e),i;switch(t){case"decimal":i=n;break;case"decimal-leading-zero":i=n.toString().length===1?n="0"+n.toString():n.toString();break;case"upper-roman":i=r.Generate.ListRoman(n);break;case"lower-roman":i=r.Generate.ListRoman(n).toLowerCase();break;case"lower-alpha":i=r.Generate.ListAlpha(n).toLowerCase();break;case"upper-alpha":i=r.Generate.ListAlpha(n);break}i+=". ";return i}function P(e,t,n){var r,i,s=t.ctx,o=v(e,"listStyleType"),u;if(/^(decimal|decimal-leading-zero|upper-alpha|upper-latin|upper-roman|lower-alpha|lower-greek|lower-latin|lower-roman)$/i.test(o)){i=D(e,o);u=M(e,i);N(s,e,"none",v(e,"color"));if(v(e,"listStylePosition")==="inside"){s.setVariable("textAlign","left");r=n.left}else{return}T(i,r,u.bottom,s)}}function H(e){var t=i[e];if(t&&t.succeeded===true){return t.img}else{return false}}function B(e,t){var n=Math.max(e.left,t.left),r=Math.max(e.top,t.top),i=Math.min(e.left+e.width,t.left+t.width),s=Math.min(e.top+e.height,t.top+t.height);return{left:n,top:r,width:i-n,height:s-r}}function j(e,t){var n;if(!t){n=f(0);return n}if(e!=="auto"){n=f(e);t.children.push(n);return n}return t}function F(e,t,n,r,i){var s=w(t,"paddingLeft"),o=w(t,"paddingTop"),u=w(t,"paddingRight"),a=w(t,"paddingBottom");G(e,n,0,0,n.width,n.height,r.left+s+i[3].width,r.top+o+i[0].width,r.width-(i[1].width+i[3].width+s+u),r.height-(i[0].width+i[2].width+o+a))}function I(e){return["Top","Right","Bottom","Left"].map(function(t){return{width:w(e,"border"+t+"Width"),color:v(e,"border"+t+"Color")}})}function q(e){return["TopLeft","TopRight","BottomRight","BottomLeft"].map(function(t){return v(e,"border"+t+"Radius")})}function U(e,t,n,r){var i=function(e,t,n){return{x:e.x+(t.x-e.x)*n,y:e.y+(t.y-e.y)*n}};return{start:e,startControl:t,endControl:n,end:r,subdivide:function(s){var o=i(e,t,s),u=i(t,n,s),a=i(n,r,s),f=i(o,u,s),l=i(u,a,s),c=i(f,l,s);return[U(e,o,f,c),U(c,l,a,r)]},curveTo:function(e){e.push(["bezierCurve",t.x,t.y,n.x,n.y,r.x,r.y])},curveToReversed:function(r){r.push(["bezierCurve",n.x,n.y,t.x,t.y,e.x,e.y])}}}function z(e,t,n,r,i,s,o){if(t[0]>0||t[1]>0){e.push(["line",r[0].start.x,r[0].start.y]);r[0].curveTo(e);r[1].curveTo(e)}else{e.push(["line",s,o])}if(n[0]>0||n[1]>0){e.push(["line",i[0].start.x,i[0].start.y])}}function W(e,t,n,r,i,s,o){var u=[];if(t[0]>0||t[1]>0){u.push(["line",r[1].start.x,r[1].start.y]);r[1].curveTo(u)}else{u.push(["line",e.c1[0],e.c1[1]])}if(n[0]>0||n[1]>0){u.push(["line",s[0].start.x,s[0].start.y]);s[0].curveTo(u);u.push(["line",o[0].end.x,o[0].end.y]);o[0].curveToReversed(u)}else{u.push(["line",e.c2[0],e.c2[1]]);u.push(["line",e.c3[0],e.c3[1]])}if(t[0]>0||t[1]>0){u.push(["line",i[1].end.x,i[1].end.y]);i[1].curveToReversed(u)}else{u.push(["line",e.c4[0],e.c4[1]])}return u}function X(e,t,n){var r=e.left,i=e.top,s=e.width,o=e.height,u=t[0][0],a=t[0][1],f=t[1][0],l=t[1][1],c=t[2][0],h=t[2][1],p=t[3][0],d=t[3][1],v=s-f,m=o-c,g=s-h,y=o-d;return{topLeftOuter:R(r,i,u,a).topLeft.subdivide(.5),topLeftInner:R(r+n[3].width,i+n[0].width,Math.max(0,u-n[3].width),Math.max(0,a-n[0].width)).topLeft.subdivide(.5),topRightOuter:R(r+v,i,f,l).topRight.subdivide(.5),topRightInner:R(r+Math.min(v,s+n[3].width),i+n[0].width,v>s+n[3].width?0:f-n[3].width,l-n[0].width).topRight.subdivide(.5),bottomRightOuter:R(r+g,i+m,h,c).bottomRight.subdivide(.5),bottomRightInner:R(r+Math.min(g,s+n[3].width),i+Math.min(m,o+n[0].width),Math.max(0,h-n[1].width),Math.max(0,c-n[2].width)).bottomRight.subdivide(.5),bottomLeftOuter:R(r,i+y,p,d).bottomLeft.subdivide(.5),bottomLeftInner:R(r+n[3].width,i+y,Math.max(0,p-n[3].width),Math.max(0,d-n[2].width)).bottomLeft.subdivide(.5)}}function V(e,t,n,r,i){var s=v(e,"backgroundClip"),o=[];switch(s){case"content-box":case"padding-box":z(o,r[0],r[1],t.topLeftInner,t.topRightInner,i.left+n[3].width,i.top+n[0].width);z(o,r[1],r[2],t.topRightInner,t.bottomRightInner,i.left+i.width-n[1].width,i.top+n[0].width);z(o,r[2],r[3],t.bottomRightInner,t.bottomLeftInner,i.left+i.width-n[1].width,i.top+i.height-n[2].width);z(o,r[3],r[0],t.bottomLeftInner,t.topLeftInner,i.left+n[3].width,i.top+i.height-n[2].width);break;default:z(o,r[0],r[1],t.topLeftOuter,t.topRightOuter,i.left,i.top);z(o,r[1],r[2],t.topRightOuter,t.bottomRightOuter,i.left+i.width,i.top);z(o,r[2],r[3],t.bottomRightOuter,t.bottomLeftOuter,i.left+i.width,i.top+i.height);z(o,r[3],r[0],t.bottomLeftOuter,t.topLeftOuter,i.left,i.top+i.height);break}return o}function $(e,t,n){var r=t.left,i=t.top,s=t.width,o=t.height,u,a,f,l,c,h,p=q(e),d=X(t,p,n),v={clip:V(e,d,n,p,t),borders:[]};for(u=0;u<4;u++){if(n[u].width>0){a=r;f=i;l=s;c=o-n[2].width;switch(u){case 0:c=n[0].width;h=W({c1:[a,f],c2:[a+l,f],c3:[a+l-n[1].width,f+c],c4:[a+n[3].width,f+c]},p[0],p[1],d.topLeftOuter,d.topLeftInner,d.topRightOuter,d.topRightInner);break;case 1:a=r+s-n[1].width;l=n[1].width;h=W({c1:[a+l,f],c2:[a+l,f+c+n[2].width],c3:[a,f+c],c4:[a,f+n[0].width]},p[1],p[2],d.topRightOuter,d.topRightInner,d.bottomRightOuter,d.bottomRightInner);break;case 2:f=f+o-n[2].width;c=n[2].width;h=W({c1:[a+l,f+c],c2:[a,f+c],c3:[a+n[3].width,f],c4:[a+l-n[2].width,f]},p[2],p[3],d.bottomRightOuter,d.bottomRightInner,d.bottomLeftOuter,d.bottomLeftInner);break;case 3:l=n[3].width;h=W({c1:[a,f+c+n[2].width],c2:[a,f],c3:[a+l,f+n[0].width],c4:[a+l,f+c]},p[3],p[0],d.bottomLeftOuter,d.bottomLeftInner,d.topLeftOuter,d.topLeftInner);break}v.borders.push({args:h,color:n[u].color})}}return v}function J(e,t){var n=e.drawShape();t.forEach(function(e,t){n[t===0?"moveTo":e[0]+"To"].apply(null,e.slice(1))});return n}function K(e,t,n){if(n!=="transparent"){e.setVariable("fillStyle",n);J(e,t);e.fill();a+=1}}function Q(e,t,n){var r=c.createElement("valuewrap"),i=["lineHeight","textAlign","fontFamily","color","fontSize","paddingLeft","paddingTop","width","height","border","borderLeftWidth","borderTopWidth"],s,o;i.forEach(function(t){try{r.style[t]=v(e,t)}catch(n){u("html2canvas: Parse: Exception caught in renderFormValue: "+n.message)}});r.style.borderColor="black";r.style.borderStyle="solid";r.style.display="block";r.style.position="absolute";if(/^(submit|reset|button|text|password)$/.test(e.type)||e.nodeName==="SELECT"){r.style.lineHeight=v(e,"height")}r.style.top=t.top+"px";r.style.left=t.left+"px";s=e.nodeName==="SELECT"?(e.options[e.selectedIndex]||0).text:e.value;if(!s){s=e.placeholder}o=c.createTextNode(s);r.appendChild(o);d.appendChild(r);O(e,o,n);d.removeChild(r)}function G(e){e.drawImage.apply(e,Array.prototype.slice.call(arguments,1));a+=1}function Y(n,i){var s=e.getComputedStyle(n,i);if(!s||!s.content||s.content==="none"||s.content==="-moz-alt-content"){return}var o=s.content+"",u=o.substr(0,1);if(u===o.substr(o.length-1)&&u.match(/'|"/)){o=o.substr(1,o.length-2)}var a=o.substr(0,3)==="url",f=t.createElement(a?"img":"span");f.className=m+"-before "+m+"-after";Object.keys(s).filter(Z).forEach(function(e){f.style[e]=s[e]});if(a){f.src=r.Util.parseBackgroundImage(o)[0].args[0]}else{f.innerHTML=o}return f}function Z(t){return isNaN(e.parseInt(t,10))}function et(e,t){var n=Y(e,":before"),r=Y(e,":after");if(!n&&!r){return}if(n){e.className+=" "+m+"-before";e.parentNode.insertBefore(n,e);ht(n,t,true);e.parentNode.removeChild(n);e.className=e.className.replace(m+"-before","").trim()}if(r){e.className+=" "+m+"-after";e.appendChild(r);ht(r,t,true);e.removeChild(r);e.className=e.className.replace(m+"-after","").trim()}}function tt(e,t,n,r){var i=Math.round(r.left+n.left),s=Math.round(r.top+n.top);e.createPattern(t);e.translate(i,s);e.fill();e.translate(-i,-s)}function nt(e,t,n,r,i,s,o,u){var a=[];a.push(["line",Math.round(i),Math.round(s)]);a.push(["line",Math.round(i+o),Math.round(s)]);a.push(["line",Math.round(i+o),Math.round(u+s)]);a.push(["line",Math.round(i),Math.round(u+s)]);J(e,a);e.save();e.clip();tt(e,t,n,r);e.restore()}function rt(e,t,n){E(e,t.left,t.top,t.width,t.height,n)}function it(e,t,n,i,s){var o=r.Util.BackgroundSize(e,t,i,s),u=r.Util.BackgroundPosition(e,t,i,s,o),a=v(e,"backgroundRepeat").split(",").map(function(e){return e.trim()});i=ot(i,o);a=a[s]||a[0];switch(a){case"repeat-x":nt(n,i,u,t,t.left,t.top+u.top,99999,i.height);break;case"repeat-y":nt(n,i,u,t,t.left+u.left,t.top,i.width,99999);break;case"no-repeat":nt(n,i,u,t,t.left+u.left,t.top+u.top,i.width,i.height);break;default:tt(n,i,u,{top:t.top,left:t.left,width:i.width,height:i.height});break}}function st(e,t,n){var i=v(e,"backgroundImage"),s=r.Util.parseBackgroundImage(i),o,a=s.length;while(a--){i=s[a];if(!i.args||i.args.length===0){continue}var f=i.method==="url"?i.args[0]:i.value;o=H(f);if(o){it(e,t,n,o,a)}else{u("html2canvas: Error loading background:",i)}}}function ot(e,t){if(e.width===t.width&&e.height===t.height){return e}var n,r=c.createElement("canvas");r.width=t.width;r.height=t.height;n=r.getContext("2d");G(n,e,0,0,e.width,e.height,0,0,t.width,t.height);return r}function ut(e,t,n){var r=v(t,"opacity")*(n?n.opacity:1);e.setVariable("globalAlpha",r);return r}function at(e,t,n){var i=l(!t?y():n.width,!t?b():n.height),o={ctx:i,zIndex:j(v(e,"zIndex"),t?t.zIndex:null),opacity:ut(i,e,t),cssPosition:v(e,"position"),borders:I(e),clip:t&&t.clip?r.Util.Extend({},t.clip):null};if(s.useOverflow===true&&/(hidden|scroll|auto)/.test(v(e,"overflow"))===true&&/(BODY)/i.test(e.nodeName)===false){o.clip=o.clip?B(o.clip,n):n}o.zIndex.children.push(o);return o}function ft(e,t,n){var r={left:t.left+e[3].width,top:t.top+e[0].width,width:t.width-(e[1].width+e[3].width),height:t.height-(e[0].width+e[2].width)};if(n){r=B(r,n)}return r}function lt(e,t,n){var i=r.Util.Bounds(e),s,o=p.test(e.nodeName)?"#efefef":v(e,"backgroundColor"),a=at(e,t,i),f=a.borders,l=a.ctx,c=ft(f,i,a.clip),h=$(e,i,f);J(l,h.clip);l.save();l.clip();if(c.height>0&&c.width>0){rt(l,i,o);st(e,c,l)}l.restore();h.borders.forEach(function(e){K(l,e.args,e.color)});if(!n){et(e,a)}switch(e.nodeName){case"IMG":if(s=H(e.getAttribute("src"))){F(l,e,s,i,f)}else{u("html2canvas: Error loading <img>:"+e.getAttribute("src"))}break;case"INPUT":if(/^(text|url|email|submit|button|reset)$/.test(e.type)&&(e.value||e.placeholder).length>0){Q(e,i,a)}break;case"TEXTAREA":if((e.value||e.placeholder||"").length>0){Q(e,i,a)}break;case"SELECT":if((e.options||e.placeholder||"").length>0){Q(e,i,a)}break;case"LI":P(e,a,c);break;case"CANVAS":F(l,e,e,i,f);break}return a}function ct(e){return v(e,"display")!=="none"&&v(e,"visibility")!=="hidden"&&!e.hasAttribute("data-html2canvas-ignore")}function ht(e,t,n){if(ct(e)){t=lt(e,t,n)||t;if(!p.test(e.nodeName)){r.Util.Children(e).forEach(function(r){if(r.nodeType===1){ht(r,t,n)}else if(r.nodeType===3){O(e,r,t)}})}}}function pt(e,t){function u(e){var t=r.Util.Children(e),n=t.length,i,s,a,f,l;for(l=0;l<n;l+=1){f=t[l];if(f.nodeType===3){o+=f.nodeValue.replace(/</g,"<").replace(/>/g,">")}else if(f.nodeType===1){if(!/^(script|meta|title)$/.test(f.nodeName.toLowerCase())){o+="<"+f.nodeName.toLowerCase();if(f.hasAttributes()){i=f.attributes;a=i.length;for(s=0;s<a;s+=1){o+=" "+i[s].name+'="'+i[s].value+'"'}}o+=">";u(f);o+="</"+f.nodeName.toLowerCase()+">"}}}}var n=new Image,i=y(),s=b(),o="";u(e);n.src=["data:image/svg+xml,","<svg xmlns='http://www.w3.org/2000/svg' version='1.1' width='"+i+"' height='"+s+"'>","<foreignObject width='"+i+"' height='"+s+"'>","<html xmlns='http://www.w3.org/1999/xhtml' style='margin:0;'>",o.replace(/\#/g,"%23"),"</html>","</foreignObject>","</svg>"].join("");n.onload=function(){t.svgRender=n}}function dt(){var e=lt(o,null);if(h.svgRendering){pt(t.documentElement,e)}Array.prototype.slice.call(o.children,0).forEach(function(t){ht(t,e)});e.backgroundColor=v(t.documentElement,"backgroundColor");d.removeChild(g);return e}e.scroll(0,0);var o=s.elements===n?t.body:s.elements[0],a=0,c=o.ownerDocument,h=r.Util.Support(s,c),p=new RegExp("("+s.ignoreElements+")"),d=c.body,v=r.Util.getCSS,m="___html2canvas___pseudoelement",g=c.createElement("style");g.innerHTML="."+m+'-before:before { content: "" !important; display: none !important; }'+"."+m+'-after:after { content: "" !important; display: none !important; }';d.appendChild(g);i=i||{};var R=function(e){return function(t,n,r,i){var s=r*e,o=i*e,u=t+r,a=n+i;return{topLeft:U({x:t,y:a},{x:t,y:a-o},{x:u-s,y:n},{x:u,y:n}),topRight:U({x:t,y:n},{x:t+s,y:n},{x:u,y:a-o},{x:u,y:a}),bottomRight:U({x:u,y:n},{x:u,y:n+o},{x:t+s,y:a},{x:t,y:a}),bottomLeft:U({x:u,y:a},{x:u-s,y:a},{x:t,y:n+o},{x:t,y:n})}}}(4*((Math.sqrt(2)-1)/3));return dt()};r.Preload=function(i){function y(e){v.href=e;v.href=v.href;var t=v.protocol+v.host;return t===o}function b(){u("html2canvas: start: images: "+s.numLoaded+" / "+s.numTotal+" (failed: "+s.numFailed+")");if(!s.firstRun&&s.numLoaded>=s.numTotal){u("Finished loading images: # "+s.numTotal+" (failed: "+s.numFailed+")");if(typeof i.complete==="function"){i.complete(s)}}}function w(t,r,o){var u,a=i.proxy,f;v.href=t;t=v.href;u="html2canvas_"+l++;o.callbackname=u;if(a.indexOf("?")>-1){a+="&"}else{a+="?"}a+="url="+encodeURIComponent(t)+"&callback="+u;f=h.createElement("script");e[u]=function(t){if(t.substring(0,6)==="error:"){o.succeeded=false;s.numLoaded++;s.numFailed++;b()}else{k(r,o);r.src=t}e[u]=n;try{delete e[u]}catch(i){}f.parentNode.removeChild(f);f=null;delete o.script;delete o.callbackname};f.setAttribute("type","text/javascript");f.setAttribute("src",a);o.script=f;e.document.body.appendChild(f)}function E(t,n){var i=e.getComputedStyle(t,n),s=i.content;if(s.substr(0,3)==="url"){a.loadImage(r.Util.parseBackgroundImage(s)[0].args[0])}N(i.backgroundImage,t)}function S(e){E(e,":before");E(e,":after")}function x(e,t){var i=r.Generate.Gradient(e,t);if(i!==n){s[e]={img:i,succeeded:true};s.numTotal++;s.numLoaded++;b()}}function T(e){return e&&e.method&&e.args&&e.args.length>0}function N(e,t){var i;r.Util.parseBackgroundImage(e).filter(T).forEach(function(e){if(e.method==="url"){a.loadImage(e.args[0])}else if(e.method.match(/\-?gradient$/)){if(i===n){i=r.Util.Bounds(t)}x(e.value,i)}})}function C(e){var t=false;try{r.Util.Children(e).forEach(function(e){C(e)})}catch(i){}try{t=e.nodeType}catch(s){t=false;u("html2canvas: failed to access some element's nodeType - Exception: "+s.message)}if(t===1||t===n){S(e);try{N(r.Util.getCSS(e,"backgroundImage"),e)}catch(i){u("html2canvas: failed to get background-image - Exception: "+i.message)}N(e)}}function k(t,r){t.onload=function(){if(r.timer!==n){e.clearTimeout(r.timer)}s.numLoaded++;r.succeeded=true;t.onerror=t.onload=null;b()};t.onerror=function(){if(t.crossOrigin==="anonymous"){e.clearTimeout(r.timer);if(i.proxy){var n=t.src;t=new Image;r.img=t;t.src=n;w(t.src,t,r);return}}s.numLoaded++;s.numFailed++;r.succeeded=false;t.onerror=t.onload=null;b()}}var s={numLoaded:0,numFailed:0,numTotal:0,cleanupDone:false},o,a,f,l=0,c=i.elements[0]||t.body,h=c.ownerDocument,p=h.images,d=p.length,v=h.createElement("a"),m=function(e){return e.crossOrigin!==n}(new Image),g;v.href=e.location.href;o=v.protocol+v.host;a={loadImage:function(t){var r,o;if(t&&s[t]===n){r=new Image;if(t.match(/data:image\/.*;base64,/i)){r.src=t.replace(/url\(['"]{0,}|['"]{0,}\)$/ig,"");o=s[t]={img:r};s.numTotal++;k(r,o)}else if(y(t)||i.allowTaint===true){o=s[t]={img:r};s.numTotal++;k(r,o);r.src=t}else if(m&&!i.allowTaint&&i.useCORS){r.crossOrigin="anonymous";o=s[t]={img:r};s.numTotal++;k(r,o);r.src=t;r.customComplete=function(){if(!this.img.complete){this.timer=e.setTimeout(this.img.customComplete,100)}else{this.img.onerror()}}.bind(o);r.customComplete()}else if(i.proxy){o=s[t]={img:r};s.numTotal++;w(t,r,o)}}},cleanupDOM:function(r){var o,a;if(!s.cleanupDone){if(r&&typeof r==="string"){u("html2canvas: Cleanup because: "+r)}else{u("html2canvas: Cleanup after timeout: "+i.timeout+" ms.")}for(a in s){if(s.hasOwnProperty(a)){o=s[a];if(typeof o==="object"&&o.callbackname&&o.succeeded===n){e[o.callbackname]=n;try{delete e[o.callbackname]}catch(f){}if(o.script&&o.script.parentNode){o.script.setAttribute("src","about:blank");o.script.parentNode.removeChild(o.script)}s.numLoaded++;s.numFailed++;u("html2canvas: Cleaned up failed img: '"+a+"' Steps: "+s.numLoaded+" / "+s.numTotal)}}}if(e.stop!==n){e.stop()}else if(t.execCommand!==n){t.execCommand("Stop",false)}if(t.close!==n){t.close()}s.cleanupDone=true;if(!(r&&typeof r==="string")){b()}}},renderingDone:function(){if(g){e.clearTimeout(g)}}};if(i.timeout>0){g=e.setTimeout(a.cleanupDOM,i.timeout)}u("html2canvas: Preload starts: finding background-images");s.firstRun=true;C(c);u("html2canvas: Preload: Finding images");for(f=0;f<d;f+=1){a.loadImage(p[f].getAttribute("src"))}s.firstRun=false;u("html2canvas: Preload: Done.");if(s.numTotal===s.numLoaded){b()}return a};r.Renderer=function(e,i){function s(e){var t=[];var n=function(e){var r=[],i=[];e.children.forEach(function(e){if(e.children&&e.children.length>0){r.push(e);i.push(e.zindex)}else{t.push(e)}});i.sort(function(e,t){return e-t});i.forEach(function(e){var t;r.some(function(n,r){t=r;return n.zindex===e});n(r.splice(t,1)[0])})};n(e.zIndex);return t}function o(e){var t;if(typeof i.renderer==="string"&&r.Renderer[e]!==n){t=r.Renderer[e](i)}else if(typeof e==="function"){t=e(i)}else{throw new Error("Unknown renderer")}if(typeof t!=="function"){throw new Error("Invalid renderer defined")}return t}return o(i.renderer)(e,i,t,s(e),r)};r.Util.Support=function(e,t){function r(){var e=new Image,r=t.createElement("canvas"),i=r.getContext===n?false:r.getContext("2d");if(i===false){return false}r.width=r.height=10;e.src=["data:image/svg+xml,","<svg xmlns='http://www.w3.org/2000/svg' width='10' height='10'>","<foreignObject width='10' height='10'>","<div xmlns='http://www.w3.org/1999/xhtml' style='width:10;height:10;'>","sup","</div>","</foreignObject>","</svg>"].join("");try{i.drawImage(e,0,0);r.toDataURL()}catch(s){return false}u("html2canvas: Parse: SVG powered rendering available");return true}function i(){var e,n,r,i,s=false;if(t.createRange){e=t.createRange();if(e.getBoundingClientRect){n=t.createElement("boundtest");n.style.height="123px";n.style.display="block";t.body.appendChild(n);e.selectNode(n);r=e.getBoundingClientRect();i=r.height;if(i===123){s=true}t.body.removeChild(n)}}return s}return{rangeBounds:i(),svgRendering:e.svgRendering&&r()}};e.html2canvas=function(t,n){t=t.length?t:[t];var i,s,o={logging:false,elements:t,background:"#fff",proxy:null,timeout:0,useCORS:false,allowTaint:false,svgRendering:false,ignoreElements:"IFRAME|OBJECT|PARAM",useOverflow:true,letterRendering:false,chinese:false,width:null,height:null,taintTest:true,renderer:"Canvas"};o=r.Util.Extend(n,o);r.logging=o.logging;o.complete=function(e){if(typeof o.onpreloaded==="function"){if(o.onpreloaded(e)===false){return}}i=r.Parse(e,o);if(typeof o.onparsed==="function"){if(o.onparsed(i)===false){return}}s=r.Renderer(i,o);if(typeof o.onrendered==="function"){o.onrendered(s)}};e.setTimeout(function(){r.Preload(o)},0);return{render:function(e,t){return r.Renderer(e,r.Util.Extend(t,o))},parse:function(e,t){return r.Parse(e,r.Util.Extend(t,o))},preload:function(e){return r.Preload(r.Util.Extend(e,o))},log:u}};e.html2canvas.log=u;e.html2canvas.Renderer={Canvas:n};r.Renderer.Canvas=function(e){function f(e,t){e.beginPath();t.forEach(function(t){e[t.name].apply(e,t["arguments"])});e.closePath()}function l(e){if(i.indexOf(e["arguments"][0].src)===-1){o.drawImage(e["arguments"][0],0,0);try{o.getImageData(0,0,1,1)}catch(t){s=r.createElement("canvas");o=s.getContext("2d");return false}i.push(e["arguments"][0].src)}return true}function c(e){return e==="transparent"||e==="rgba(0, 0, 0, 0)"}function h(t,n){switch(n.type){case"variable":t[n.name]=n["arguments"];break;case"function":if(n.name==="createPattern"){if(n["arguments"][0].width>0&&n["arguments"][0].height>0){try{t.fillStyle=t.createPattern(n["arguments"][0],"repeat")}catch(r){u("html2canvas: Renderer: Error creating pattern",r.message)}}}else if(n.name==="drawShape"){f(t,n["arguments"])}else if(n.name==="drawImage"){if(n["arguments"][8]>0&&n["arguments"][7]>0){if(!e.taintTest||e.taintTest&&l(n)){t.drawImage.apply(t,n["arguments"])}}}else{t[n.name].apply(t,n["arguments"])}break}}e=e||{};var r=t,i=[],s=t.createElement("canvas"),o=s.getContext("2d"),a=e.canvas||r.createElement("canvas");return function(e,t,r,i,s){var o=a.getContext("2d"),f,l,p,d,v,m;a.width=a.style.width=t.width||e.ctx.width;a.height=a.style.height=t.height||e.ctx.height;m=o.fillStyle;o.fillStyle=c(e.backgroundColor)&&t.background!==n?t.background:e.backgroundColor;o.fillRect(0,0,a.width,a.height);o.fillStyle=m;if(t.svgRendering&&e.svgRender!==n){o.drawImage(e.svgRender,0,0)}else{for(l=0,p=i.length;l<p;l+=1){f=i.splice(0,1)[0];f.canvasPosition=f.canvasPosition||{};o.textBaseline="bottom";if(f.clip){o.save();o.beginPath();o.rect(f.clip.left,f.clip.top,f.clip.width,f.clip.height);o.clip()}if(f.ctx.storage){f.ctx.storage.forEach(h.bind(null,o))}if(f.clip){o.restore()}}}u("html2canvas: Renderer: Canvas renderer done - returning canvas obj");p=t.elements.length;if(p===1){if(typeof t.elements[0]==="object"&&t.elements[0].nodeName!=="BODY"){v=s.Util.Bounds(t.elements[0]);d=r.createElement("canvas");d.width=v.width;d.height=v.height;o=d.getContext("2d");o.drawImage(a,v.left,v.top,v.width,v.height,0,0,v.width,v.height);a=null;return d}}return a}}})(window,document)