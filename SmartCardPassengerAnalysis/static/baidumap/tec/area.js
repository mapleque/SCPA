/**
 * 实现根据已知路径画区域的功能
 * @author mapleque@163.com
 * @version 1.0
 * @date 2013.05.28
 */
;(function(window,document){
	if (window.mapleque==undefined)
		window.mapleque={};
	if (window.mapleque.area!=undefined)
		return;
	
	/**
	 * 组件对外接口
	 */
	var proc={
		/**
		 * 接收canvas对象，并在此上画区域（区域路径已经设置）
		 * @param context
		 */
		paint:function(context){paint(this,context);},
		/**
		 * 设置路径点
		 * @param path路径
		 * @param st强度
		 */
		set:function(path,st){init(this,path,st);},
	};
	
	var init=function(a,path,st){
		a.path=path;//起点
		a.st=st;//强度
	};
	var paint=function(a,context){
		var st=a.st||1;
		if (context==undefined)
			return;
	};
	
	var area=new Function();
	area.prototype=proc;
	window.mapleque.area=area;
})(window,document);
