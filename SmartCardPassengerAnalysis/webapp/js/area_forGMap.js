/**
 * 实现根据已知路径画区域的功能
 * 
 * @author mapleque@163.com
 * @version 1.0
 * @date 2013.05.28
 */
;
(function(window, document) {
	if (window.mapleque == undefined)
		window.mapleque = {};
	if (window.mapleque.area != undefined)
		return;

	/**
	 * 组件对外接口
	 */
	var proc = {
		/**
		 * 接收canvas对象，并在此上画区域（区域路径已经设置）
		 * 
		 * @param context
		 */
		paint : function(context) {
			paint(this, context);
		},
		/**
		 * 设置路径点
		 * 
		 * @param path路径
		 * @param st强度
		 */
		set : function(id, path, st) {
			init(this, id, path, st);
		},
	};

	var init = function(a, id, path, st) {
		a.id = id;
		a.path = path;// 路径
		a.st = st;// 强度
	};
	var paint = function(a, context) {
		var st = a.st || 1;
		if (context == undefined)
			return;
		var bp = [];
		for ( var i = 0, L = a.path.length; i < L; i++) {
			bp[i] = new google.maps.LatLng(a.path[i].y, a.path[i].x);
		}
		
		var clt=window.mapleque.application.strong2color(st);
		var polygon = new google.maps.Polygon({
			paths:bp, 
			strokeColor : clt,
			strokeWeight : 1,
			strokeOpacity : st,
			fillColor : clt,
			fileOpacity : st
		});
//		google.maps.event.addListener(polygon,"mouseover", function(a) {
//			return function(e){
//			window.mapleque.application.popup(a,e);
//			};
//		}(a.id));
//		google.maps.event.addListener(polygon,"mouseout", function(a) {
//			return function(e){
//			window.mapleque.application.remove_popup(a,e);
//			};
//		}(a.id));
//		
//		google.maps.event.addListener(polygon,"mousemove", function(a) {
//			return function(e){
//			window.mapleque.application.move_popup(a,e);
//			};
//		}(a.id));
		
		google.maps.event.addListener(polygon,"click", function(a) {
			return function(e){
			window.mapleque.application.popup(a,e);
			};
		}(a.id));
		
		polygon.setMap(context);
	};

	var area = new Function();
	area.prototype = proc;
	window.mapleque.area = area;
})(window, document);
