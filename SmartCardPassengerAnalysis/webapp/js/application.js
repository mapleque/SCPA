;
(function(window, document) {
	if (window.mapleque == undefined)
		window.mapleque = {};
	if (window.mapleque.application != undefined)
		return;
	
	var createPopup=function(a,e){
		var ele=document.createElement("div");
		ele.setAttribute("class", "popup");
		ele.setAttribute("id","popup_div");
		$(ele).css("top",e.Xa.y);
		$(ele).css("left",e.Xa.x);
		ele.innerHTML='<iframe style="border:0;width:400px;height:250px;" src="./jsp/chart.jsp?id='+a+'"></iframe>';
		
		var del=document.createElement("div");
		del.setAttribute("class", "popup_del");
		del.setAttribute("id","popup_del");
		del.onclick=function(){removePopup();};
		ele.appendChild(del);
		document.body.appendChild(ele);
	};
	var havePopup=function(){
		return document.getElementById("popup_div")?true:false;
	};
	var movePopup=function(e){
		var ele=document.getElementById("popup_div");
		$(ele).css("top",e.Xa.y);
		$(ele).css("left",e.Xa.x);
	};
	var removePopup=function(){
		var ele=document.getElementById("popup_div");
		$(ele).remove();
	};
	
	var app = {};
	app.popup = function(a,e) {
		if (havePopup)
			removePopup();
		createPopup(a,e);
	};
	app.remove_popup = function(a) {
		if (havePopup())
			removePopup();
	};
	app.move_popup=function(a,e){
		if (havePopup()){
			movePopup(e);
		}else{
			createPopup(a,e);
		}
	};
	
	app.strong2color=function(st){
		st=parseInt((1-st)*255);
		var cl=st+st*256+250*256*256;
		cl=cl.toString(16);
		return "#"+cl;
	};
	window.mapleque.application = app;
})(window, document);