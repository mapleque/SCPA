<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String id=request.getParameter("id");
    String d=request.getParameter("d");
    String t=request.getParameter("t");
    String path=request.getSession().getServletContext().getRealPath("/");
    System.out.println(System.currentTimeMillis()+"##read data: "+path+"/data/chart/1/"+d+"/"+t+"/"+id+".csv");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chart</title>
<style>
html,body {
	margin: 0;
	background:#fef;
	opacity:0.9;
}
</style>
</head>
<body>
	<div style="width: 100%; height: 100px;" id="graphdiv"></div>
	<script type="text/javascript" src="../js/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="../js/dygraph.js"></script>
	<script type="text/javascript" src="../js/area_1_data.js"></script>
	<script type="text/javascript">
		var div = document.getElementById("graphdiv");
		$(div).height($(window).height());
		g = new Dygraph(div, "<%out.print("../data/chart/1/"+d+"/"+t+"/"+id+".csv");%>", {
			errorBars : true
			//valueRange : [ 0, 10000 ]
		}

		);
	</script>

</body>
</html>