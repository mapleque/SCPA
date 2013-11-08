/**
 * 实现两点间画箭头的功能
 * 
 * @author mapleque@163.com
 * @version 1.0
 * @date 2013.05.23
 */
;
(function(window, document) {
	if (window.mapleque == undefined)
		window.mapleque = {};
	if (window.mapleque.arrow != undefined)
		return;

	/**
	 * 组件对外接口
	 */
	var proc = {
		/**
		 * 接收canvas对象，并在此上画箭头（箭头起止点已经设置）
		 * 
		 * @param context
		 */
		paint : function(context) {
			paint(this, context);
		},
		/**
		 * 设置箭头起止点
		 * 
		 * @param sp起点
		 * @param ep终点
		 * @param st强度
		 */
		set : function(id, sp, ep, st) {
			init(this, id, sp, ep, st);
		},
		/**
		 * 设置箭头外观
		 * 
		 * @param args
		 */
		setPara : function(args) {
			this.size = args.arrow_size;// 箭头大小
			this.sharp = args.arrow_sharp;// 箭头锐钝
		}
	};

	var init = function(a, id, sp, ep, st) {
		a.id = id;
		a.sp = sp;// 起点
		a.ep = ep;// 终点
		a.st = st;// 强度
	};
	var paint = function(a, context) {
		var sp = a.sp;
		var ep = a.ep;
		var st = a.st || 1;
		if (context == undefined)
			return;
		var clt = window.mapleque.application.strong2color(st);
		var h = _calcH(a, sp, ep, st, context);
		var polyline = new google.maps.Polyline({
			path : [ new google.maps.LatLng(sp.y, sp.x),
					new google.maps.LatLng(ep.y, ep.x) ],
			strokeColor : clt,
			strokeWeight : st * 10,
			strokeOpacity : 0.8
		});
		console.log("color-weight", clt, st * 10);
		 google.maps.event.addListener(polyline, "mouseover", function(a) {
			return function(e) {
				window.mapleque.application.popup(a, e);
			};
		}(a.id));
		google.maps.event.addListener(polyline, "mouseout", function(a) {
			return function(e) {
				console.log("out");
				window.mapleque.application.remove_popup(a, e);
			};
		}(a.id));
		google.maps.event.addListener(polyline, "mousemove", function(a) {
			return function(e) {
				console.log("move:");
				window.mapleque.application.move_popup(a, e);
			};
		}(a.id));
		polyline.setMap(context);
		polyline = new google.maps.Polyline({
			path : [ new google.maps.LatLng(h.h1.y, h.h1.x),
					new google.maps.LatLng(ep.y, ep.x) ],
			strokeColor : clt,
			strokeWeight : st * 10,
			strokeOpacity : 0.5
		});
		polyline.setMap(context);
		polyline = new google.maps.Polyline({
			path : [ new google.maps.LatLng(h.h2.y, h.h2.x),
					new google.maps.LatLng(ep.y, ep.x) ],
			strokeColor : clt,
			strokeWeight : st * 10,
			strokeOpacity : 0.5
		});
		polyline.setMap(context);

		// add label
		// var point=new google.maps.LatLng((ep.x+sp.x)/2, (ep.y+sp.y)/2);
		// console.log(point);
		// var label=new BMap.Label(st,{"position":point});
		// context.setMap(label);
	};
	// 计算头部坐标
	var _calcH = function(a, sp, ep, st, context) {
		var theta = Math.atan((ep.x - sp.x) / (ep.y - sp.y));
		var cep = _scrollXOY(ep, -theta);
		var csp = _scrollXOY(sp, -theta);
		var ch1 = {
			x : 0,
			y : 0
		};
		var ch2 = {
			x : 0,
			y : 0
		};
		var l = cep.y - csp.y;
		ch1.x = cep.x + l * (a.sharp || 0.025) * (1 + st / 10);
		ch1.y = cep.y - l * (a.size || 0.05) * (1 + st / 10);
		ch2.x = cep.x - l * (a.sharp || 0.025) * (1 + st / 10);
		ch2.y = cep.y - l * (a.size || 0.05) * (1 + st / 10);
		var h1 = _scrollXOY(ch1, theta);
		var h2 = _scrollXOY(ch2, theta);
		return {
			h1 : h1,
			h2 : h2
		};
	};
	// 旋转坐标
	var _scrollXOY = function(p, theta) {
		return {
			x : p.x * Math.cos(theta) + p.y * Math.sin(theta),
			y : p.y * Math.cos(theta) - p.x * Math.sin(theta)
		};
	};

	var arrow = new Function();
	arrow.prototype = proc;
	window.mapleque.arrow = arrow;
})(window, document);
